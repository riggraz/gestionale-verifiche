package views.manage_school_class.student_forms;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import models.StudentModel;
import utils.FormUtils;
import views.GenericForm;

public class MassAddStudentForm extends GenericForm {

	private static final long serialVersionUID = 1L;
	private final String NEW_LINE_REGEX = "\\r?\\n";
	private final String SPACE_REGEX = "\\s+";
	
	StudentModel studentModel;
	String selectedSchoolClassName;
	
	JLabel infoLbl;
	JTextArea studentsTxtArea;

	public MassAddStudentForm(JComponent parent, StudentModel studentModel, String selectedSchoolClassName) {
		super(parent, "Aggiungi lista studenti");
		setLayout(new FlowLayout());
		setSize(400, 380);
		
		this.studentModel = studentModel;
		this.selectedSchoolClassName = selectedSchoolClassName;
		
		infoLbl = new JLabel(
				"<html>Scrivi tutti gli studenti che vuoi aggiungere<br />" +
				"rispettando il seguente formato:<br /><b>nome[spazio]cognome[a capo]</b></html>");
		
		studentsTxtArea = new JTextArea(13, 24);
		JScrollPane studentsTxtAreaScrollPane = new JScrollPane(studentsTxtArea);
		studentsTxtAreaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		studentsTxtAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(infoLbl);
		add(studentsTxtAreaScrollPane);
		
		add(saveBtn);
		add(cancelBtn);
	}

	@Override
	protected int checkErrorsAndUpdateUI() {
		int errorsCount = 0;
		
		if (studentsTxtArea.getText().equals("")) errorsCount++;
		else {
			String[] students = studentsTxtArea.getText().split(NEW_LINE_REGEX);
			
			for (String student : students) {
				if (student.split(SPACE_REGEX).length != 2) errorsCount++;
			}
		}
		
		if (errorsCount > 0) studentsTxtArea.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		return errorsCount;
	}

	@Override
	public void save() {
		String[] students = studentsTxtArea.getText().split(NEW_LINE_REGEX);
		
		for (String student : students) {
			String name = student.split(SPACE_REGEX)[0];
			String surname = student.split(SPACE_REGEX)[1];
			
			studentModel.insertRow(
					FormUtils.capitalizeFirstLetter(name),
					FormUtils.capitalizeFirstLetter(surname),
					selectedSchoolClassName);
			dispose();
		}
	}
}
