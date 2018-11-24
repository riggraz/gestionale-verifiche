package views.manage_school_class;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import entities.SchoolClass;
import models.SchoolClassModel;
import utils.DBManager;
import views.manage_school_class.school_class_forms.EditSchoolClassForm;
import views.manage_school_class.school_class_forms.InsertSchoolClassForm;

public class ManageSchoolClass extends JPanel implements ActionListener, ItemListener, ListDataListener {

	private static final long serialVersionUID = 1L;
	
	SchoolClassModel schoolClassModel;
	
	JPanel schoolClassManagementPnl;
	JComboBox<SchoolClass> schoolClassCmbBox;
	JButton insertSchoolClassBtn, editSchoolClassBtn, deleteSchoolClassBtn;
	
	StudentTable studentTable;
	
	public ManageSchoolClass(DBManager dbManager) {
		setLayout(new BorderLayout(24, 24));
		
		schoolClassCmbBox = new JComboBox<SchoolClass>();
		schoolClassModel = new SchoolClassModel(dbManager);
		schoolClassCmbBox.setModel(schoolClassModel);
		schoolClassCmbBox.addItemListener(this);
		schoolClassModel.addListDataListener(this);
		
		insertSchoolClassBtn = new JButton("Nuova classe");
		insertSchoolClassBtn.addActionListener(this);
		
		editSchoolClassBtn = new JButton("Modifica classe");
		editSchoolClassBtn.addActionListener(this);
		
		deleteSchoolClassBtn = new JButton("Elimina classe");
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
			new InsertSchoolClassForm(schoolClassModel);
		}
		
		if (e.getSource() == editSchoolClassBtn) {
			new EditSchoolClassForm(schoolClassModel, schoolClassCmbBox.getSelectedIndex());
		}
		
		if (e.getSource() == deleteSchoolClassBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Vuoi davvero eliminare la classe " +
					((SchoolClass) schoolClassCmbBox.getSelectedItem()).getName() +
					"?\nVerranno eliminati anche tutti i suoi studenti.", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				schoolClassModel.deleteItem(schoolClassCmbBox.getSelectedIndex());
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
		studentTable.populateTable(((SchoolClass)schoolClassCmbBox.getSelectedItem()).getName());
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) {
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
	}

}
