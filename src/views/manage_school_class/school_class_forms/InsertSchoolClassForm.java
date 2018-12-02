package views.manage_school_class.school_class_forms;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import models.SchoolClassModel;

public class InsertSchoolClassForm extends SchoolClassForm {

	private static final long serialVersionUID = 1L;
	
	private SchoolClassModel schoolClassModel;
	
	public InsertSchoolClassForm(SchoolClassModel schoolClassModel) {
		super("Aggiungi classe");
		
		this.schoolClassModel = schoolClassModel;
	}

	@Override
	public void save() {
		try {
			schoolClassModel.insertItem(schoolClassNameTxt.getText());
			dispose();
		} catch (SQLException e) {
			if (e.getErrorCode() == 19) { // UNIQUE constraint failed on column name
				JOptionPane.showMessageDialog(this, "Esiste già una classe con questo nome.");
			}
		}
	}

}
