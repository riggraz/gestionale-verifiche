package views.manage_test.test_forms;

import java.util.UUID;

import models.TestModel;

public class EditTestForm extends TestForm {
	
	private static final long serialVersionUID = 1L;
	
	private TestModel testModel;

	public EditTestForm(TestModel testModel) {
		super("Modifica verifica", testModel, null);
		
		this.testModel = testModel;
		
		// get test from db
//		setTestId(testId);
	}
}
