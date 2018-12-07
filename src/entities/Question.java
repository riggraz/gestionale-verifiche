package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {
	
	private UUID id;
	private int number;
	private String body;
	private UUID testId;
	private int correctAnswer;
	
	private List<Answer> answers;
	
	public Question(UUID id, int number, String body, UUID testId, int correctAnswer) {
		super();
		this.id = id;
		this.number = number;
		this.body = body;
		this.testId = testId;
		this.correctAnswer = correctAnswer;
		
		this.answers = new ArrayList<Answer>();
	}

	public Question(int number, String body, UUID testId, int correctAnswer) {
		super();
		this.id = UUID.randomUUID();
		this.number = number;
		this.body = body;
		this.testId = testId;
		this.correctAnswer = correctAnswer;
		
		this.answers = new ArrayList<Answer>();
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		if (number >= 0) this.number = number;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public UUID getTestId() {
		return testId;
	}
	public void setTestId(UUID testId) {
		this.testId = testId;
	}
	
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
}
