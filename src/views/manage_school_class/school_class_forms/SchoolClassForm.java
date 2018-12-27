package views.manage_school_class.school_class_forms;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import views.GenericForm;

public abstract class SchoolClassForm extends GenericForm {

	private static final long serialVersionUID = 1L;
	
	JLabel schoolClassNameLbl;
	JTextField schoolClassNameTxt;
	
	public SchoolClassForm(JComponent parent, String frameName) {
		super(parent, frameName);
		
		schoolClassNameLbl = new JLabel("Nome: ");
		schoolClassNameTxt = new JTextField();
		
		add(schoolClassNameLbl);
		add(schoolClassNameTxt);
		add(saveBtn);
		add(cancelBtn);
		pack();
	}
	
	@Override
	protected int checkErrorsAndUpdateUI() {
		int errorsCount = 0;
		
		if (schoolClassNameTxt.getText().equals("")) {
			errorsCount++;
			schoolClassNameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		}
		
		return errorsCount;
	}

}
