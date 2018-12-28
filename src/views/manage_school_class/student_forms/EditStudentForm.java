package views.manage_school_class.student_forms;

import java.util.UUID;

import javax.swing.JComponent;

import entities.Student;
import models.StudentModel;
import utils.FormUtils;

public class EditStudentForm extends StudentForm {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	UUID studentId;

	public EditStudentForm(JComponent parent, StudentModel studentModel, Student s) {
		super(parent, "Modifica studente");
		
		this.studentModel = studentModel;
		this.studentId = s.getId();
		
		firstNameTxt.setText(s.getFirstName());
		lastNameTxt.setText(s.getLastName());
		schoolClassCmbBox.addItem(s.getSchoolClassName());
	}

	@Override
	public void save() {
		studentModel.updateRow(
				studentId,
				FormUtils.capitalizeFirstLetter(firstNameTxt.getText()),
				FormUtils.capitalizeFirstLetter(lastNameTxt.getText()),
				(String)schoolClassCmbBox.getSelectedItem());
	}

}
