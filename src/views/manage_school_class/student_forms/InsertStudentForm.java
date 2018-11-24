package views.manage_school_class.student_forms;

import models.StudentModel;

public class InsertStudentForm extends StudentForm {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	String selectedSchoolClassName;

	public InsertStudentForm(StudentModel studentModel, String selectedSchoolClassName) {
		super("Aggiungi studente");
		
		schoolClassCmbBox.addItem(selectedSchoolClassName);
		
		this.studentModel = studentModel;
		this.selectedSchoolClassName = selectedSchoolClassName;
	}

	@Override
	public void save() {
		studentModel.insertRow(
				firstNameTxt.getText(),
				lastNameTxt.getText(),
				selectedSchoolClassName);
		dispose();
	}

}
