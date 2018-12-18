package views.manage_school_class;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import entities.SchoolClass;
import models.SchoolClassComboBoxModel;
import utils.DBManager;
import views.manage_school_class.school_class_forms.EditSchoolClassForm;
import views.manage_school_class.school_class_forms.InsertSchoolClassForm;

public class ManageSchoolClass extends JPanel implements ActionListener, ItemListener, ListDataListener {

	private static final long serialVersionUID = 1L;
	
	SchoolClassComboBoxModel schoolClassComboBoxModel;
	
	JPanel schoolClassManagementPnl;
	JComboBox<SchoolClass> schoolClassCmbBox;
	JButton insertSchoolClassBtn, editSchoolClassBtn, deleteSchoolClassBtn;
	
	StudentTable studentTable;
	
	public ManageSchoolClass(DBManager dbManager) {
		setLayout(new BorderLayout(24, 24));
		setBorder(new EmptyBorder(16, 16, 16, 16));
		
		schoolClassCmbBox = new JComboBox<SchoolClass>();
		schoolClassComboBoxModel = new SchoolClassComboBoxModel(dbManager);
		schoolClassCmbBox.setModel(schoolClassComboBoxModel);
		schoolClassCmbBox.addItemListener(this);
		schoolClassComboBoxModel.addListDataListener(this);
		
		insertSchoolClassBtn = new JButton("Nuova classe");
		insertSchoolClassBtn.setPreferredSize(new Dimension(150, 35));
		insertSchoolClassBtn.addActionListener(this);
		
		editSchoolClassBtn = new JButton("Modifica classe");
		editSchoolClassBtn.setEnabled(false);
		editSchoolClassBtn.setPreferredSize(new Dimension(150, 35));
		editSchoolClassBtn.addActionListener(this);
		
		deleteSchoolClassBtn = new JButton("Elimina classe");
		deleteSchoolClassBtn.setEnabled(false);
		deleteSchoolClassBtn.setPreferredSize(new Dimension(150, 35));
		deleteSchoolClassBtn.addActionListener(this);
		
		schoolClassManagementPnl = new JPanel();
		schoolClassManagementPnl.add(schoolClassCmbBox);
		schoolClassManagementPnl.add(editSchoolClassBtn);
		schoolClassManagementPnl.add(deleteSchoolClassBtn);
		schoolClassManagementPnl.add(insertSchoolClassBtn);
		
		studentTable = new StudentTable(dbManager, schoolClassCmbBox);
		
		add(schoolClassManagementPnl, BorderLayout.NORTH);
		add(studentTable, BorderLayout.CENTER);
		
		// inizializza la combobox alla prima classe
		if (schoolClassCmbBox.getItemCount() > 0) schoolClassCmbBox.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertSchoolClassBtn) {
			new InsertSchoolClassForm(this, schoolClassComboBoxModel);
		}
		
		if (e.getSource() == editSchoolClassBtn) {
			new EditSchoolClassForm(this, schoolClassComboBoxModel, schoolClassCmbBox.getSelectedIndex());
		}
		
		if (e.getSource() == deleteSchoolClassBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(this,
					"Vuoi davvero eliminare la classe " +
					((SchoolClass) schoolClassCmbBox.getSelectedItem()).getName() +
					"?\nVerranno eliminati anche tutti i suoi studenti.", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				schoolClassComboBoxModel.deleteItem(schoolClassCmbBox.getSelectedIndex());
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == schoolClassCmbBox) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				studentTable.populateTable(((SchoolClass)e.getItem()).getName());
			}
		}
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		SchoolClass selectedSchoolClass = (SchoolClass)schoolClassCmbBox.getSelectedItem();
		if (selectedSchoolClass != null) studentTable.populateTable(selectedSchoolClass.getName());
		
		if (schoolClassCmbBox.getItemCount() > 0) {
			editSchoolClassBtn.setEnabled(true);
			deleteSchoolClassBtn.setEnabled(true);
			studentTable.setEnabledOnInsertBtns(true);
		} else {
			editSchoolClassBtn.setEnabled(false);
			deleteSchoolClassBtn.setEnabled(false);
			studentTable.setEnabledOnInsertBtns(false);
		}
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) { }

	@Override
	public void intervalRemoved(ListDataEvent e) { }

}
