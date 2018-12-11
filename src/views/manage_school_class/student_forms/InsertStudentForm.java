package views.manage_school_class.student_forms;

import javax.swing.JComponent;

import models.StudentModel;
import utils.FormUtils;

public class InsertStudentForm extends StudentForm {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	String selectedSchoolClassName;

	public InsertStudentForm(JComponent parent, StudentModel studentModel, String selectedSchoolClassName) {
		super(parent, "Aggiungi studente");
		
		schoolClassCmbBox.addItem(selectedSchoolClassName);
		
		this.studentModel = studentModel;
		this.selectedSchoolClassName = selectedSchoolClassName;
	}

	@Override
	public void save() {
		if (checkErrorsAndUpdateUI() == 0) {
			studentModel.insertRow(
					FormUtils.capitalizeFirstLetter(firstNameTxt.getText()),
					FormUtils.capitalizeFirstLetter(lastNameTxt.getText()),
					selectedSchoolClassName);
			dispose();
		}
	}

}
