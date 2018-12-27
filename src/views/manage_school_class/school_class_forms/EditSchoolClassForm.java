package views.manage_school_class.school_class_forms;

import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import models.SchoolClassComboBoxModel;

public class EditSchoolClassForm extends SchoolClassForm {
	private static final long serialVersionUID = 1L;
	
	private SchoolClassComboBoxModel schoolClassModel;
	private int index;
	
	public EditSchoolClassForm(JComponent parent, SchoolClassComboBoxModel schoolClassModel, int index) {
		super(parent, "Modifica classe");
		
		this.schoolClassModel = schoolClassModel;
		this.index = index;
		
		schoolClassNameTxt.setText(schoolClassModel.getElementAt(index).getName());
	}

	@Override
	public void save() {
		try {
			schoolClassModel.updateItem(index, schoolClassNameTxt.getText());
		} catch (SQLException e) {
			if (e.getErrorCode() == 19) { // UNIQUE constraint failed
				JOptionPane.showMessageDialog(this, "Esiste già una classe con questo nome");
			}
		}
	}
}
