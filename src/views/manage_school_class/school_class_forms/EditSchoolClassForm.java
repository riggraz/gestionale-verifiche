package views.manage_school_class.school_class_forms;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import models.SchoolClassModel;

public class EditSchoolClassForm extends SchoolClassForm {
	private static final long serialVersionUID = 1L;
	
	private SchoolClassModel schoolClassModel;
	private int index;
	
	public EditSchoolClassForm(SchoolClassModel schoolClassModel, int index) {
		super("Modifica classe");
		
		this.schoolClassModel = schoolClassModel;
		this.index = index;
		
		schoolClassNameTxt.setText(schoolClassModel.getElementAt(index).getName());
	}

	@Override
	public void save() {
		try {
			schoolClassModel.updateItem(index, schoolClassNameTxt.getText());
			dispose();
		} catch (SQLException e) {
			if (e.getErrorCode() == 19) { // UNIQUE constraint failed
				JOptionPane.showMessageDialog(this, "Esiste già una classe con questo nome");
			}
		}
	}
}
