package views.manage_school_class;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entities.SchoolClass;
import models.SchoolClassModel;
import utils.DBManager;
import views.manage_school_class.school_class_forms.EditSchoolClassForm;
import views.manage_school_class.school_class_forms.InsertSchoolClassForm;

public class ManageSchoolClass extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	SchoolClassModel schoolClassModel;
	
	JPanel schoolClassPnl;
	JComboBox<SchoolClass> schoolClassCmbBox;
	JButton insertSchoolClassBtn, editSchoolClassBtn, deleteSchoolClassBtn;
	
	public ManageSchoolClass(DBManager dbManager) {
		setLayout(new BorderLayout(24, 24));
		
		schoolClassCmbBox = new JComboBox<SchoolClass>();
		schoolClassModel = new SchoolClassModel(dbManager);
		schoolClassCmbBox.setModel(schoolClassModel);
		
		insertSchoolClassBtn = new JButton("Nuova classe");
		insertSchoolClassBtn.addActionListener(this);
		
		editSchoolClassBtn = new JButton("Modifica classe");
		editSchoolClassBtn.addActionListener(this);
		
		deleteSchoolClassBtn = new JButton("Elimina classe");
		deleteSchoolClassBtn.addActionListener(this);
		
		schoolClassPnl = new JPanel();
		schoolClassPnl.add(schoolClassCmbBox);
		schoolClassPnl.add(editSchoolClassBtn);
		schoolClassPnl.add(deleteSchoolClassBtn);
		schoolClassPnl.add(insertSchoolClassBtn);
		
		add(schoolClassPnl);
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

}
