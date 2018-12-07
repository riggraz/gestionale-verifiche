package views.manage_school_class.school_class_forms;

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

}
