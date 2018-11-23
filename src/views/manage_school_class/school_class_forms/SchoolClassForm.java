package views.manage_school_class.school_class_forms;

import javax.swing.JLabel;
import javax.swing.JTextField;

import views.GenericForm;

public abstract class SchoolClassForm extends GenericForm {

	private static final long serialVersionUID = 1L;
	
	JLabel schoolClassNameLbl;
	JTextField schoolClassNameTxt;
	
	public SchoolClassForm(String frameName) {
		super(frameName);
		
		schoolClassNameLbl = new JLabel("Nome: ");
		schoolClassNameTxt = new JTextField();
		
		add(schoolClassNameLbl);
		add(schoolClassNameTxt);
		add(saveBtn);
		add(cancelBtn);
		
		getRootPane().setDefaultButton(saveBtn);
		setDefaultCloseOperation(SchoolClassForm.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
