package views.manage_test.test_forms;

import java.util.UUID;

import models.QuestionModel;
import models.TestModel;
import utils.DBManager;

public class InsertTestForm extends TestForm {

	private static final long serialVersionUID = 1L;

	public InsertTestForm(DBManager dbManager, TestModel testModel) {
		super("Nuova verifica", testModel);
		
		UUID testId = testModel.insertRow("Nuova verifica", "");
		setTestId(testId);
		setTestName("Nuova verifica");
		
		setQuestionModel(new QuestionModel(dbManager));
	}

}
