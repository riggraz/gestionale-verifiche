package views.manage_school_class.student_forms;

import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import entities.SchoolClass;
import entities.Student;
import models.StudentModel;
import utils.FormUtils;

public class EditStudentForm extends StudentForm {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	UUID studentId;

	public EditStudentForm(JComponent parent, StudentModel studentModel, Student s, JComboBox<SchoolClass> schoolClassComboBox) {
		super(parent, "Modifica studente");
		
		this.studentModel = studentModel;
		this.studentId = s.getId();
		
		firstNameTxt.setText(s.getFirstName());
		lastNameTxt.setText(s.getLastName());
		for (int i = 0; i < schoolClassComboBox.getItemCount(); i++) {
			super.schoolClassCmbBox.addItem(schoolClassComboBox.getItemAt(i).getName());
		}
		super.schoolClassCmbBox.setSelectedIndex(schoolClassComboBox.getSelectedIndex());
		super.schoolClassCmbBox.setEnabled(true);
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
