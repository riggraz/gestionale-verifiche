package views.manage_school_class.student_forms;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import views.GenericForm;

public abstract class StudentForm extends GenericForm {

	private static final long serialVersionUID = 1L;
	
	JLabel firstNameLbl;
	JTextField firstNameTxt;
	
	JLabel lastNameLbl;
	JTextField lastNameTxt;
	
	JLabel schoolClassLbl;
	JComboBox<String> schoolClassCmbBox;

	public StudentForm(JComponent parent, String frameName) {
		super(parent, frameName);
		
		firstNameLbl = new JLabel("Nome: ");
		firstNameTxt = new JTextField();
		
		lastNameLbl = new JLabel("Cognome: ");
		lastNameTxt = new JTextField();
		
		schoolClassLbl = new JLabel("Classe: ");
		schoolClassCmbBox = new JComboBox<String>();
		schoolClassCmbBox.setEnabled(false);
		
		add(firstNameLbl);
		add(firstNameTxt);
		add(lastNameLbl);
		add(lastNameTxt);
		add(schoolClassLbl);
		add(schoolClassCmbBox);
		add(saveBtn);
		add(cancelBtn);
		
		pack();
	}
	
	@Override
	protected int checkErrorsAndUpdateUI() {
		int errorsCount = 0;
		
		if (firstNameTxt.getText().equals("")) {
			errorsCount++;
			firstNameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		} else {
			firstNameTxt.setBorder(new JTextField().getBorder());
		}
		
		if (lastNameTxt.getText().equals("")) {
			errorsCount++;
			lastNameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		} else {
			lastNameTxt.setBorder(new JTextField().getBorder());
		}
		
		return errorsCount;
	}

}
