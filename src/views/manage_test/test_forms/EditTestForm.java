package views.manage_test.test_forms;

import java.util.UUID;

import entities.Question;
import models.QuestionModel;
import models.TestModel;
import utils.DBManager;

public class EditTestForm extends TestForm {
	
	private static final long serialVersionUID = 1L;

	public EditTestForm(
			DBManager dbManager,
			TestModel testModel,
			UUID testId,
			String testName,
			String testDescription
			) {
		super("Modifica verifica", testModel);

		setTestId(testId);
		
		setTestName(testName);
		setTestDescription(testDescription);
		
		QuestionModel questionModel = new QuestionModel(dbManager);
		questionModel.loadByTestId(testId);
		setQuestionModel(questionModel);
		
		for (Question question : questionModel.getQuestions()) {
			addQAndA(question);
		}
	}
}
