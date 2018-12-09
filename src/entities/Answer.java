package entities;

import java.util.UUID;

public class Answer {
	
	private UUID id;
	private int number;
	private String body;
	private UUID questionId;
	
	public Answer(UUID id, int number, String body, UUID questionId) {
		super();
		this.id = id;
		this.number = number;
		this.body = body;
		this.questionId = questionId;
	}
	
	public Answer(int number, String body, UUID questionId) {
		this(UUID.randomUUID(), number, body, questionId);
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
		if (number >= 0 && number <= 3) this.number = number;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public UUID getQuestionId() {
		return questionId;
	}
	public void setQuestionId(UUID questionId) {
		this.questionId = questionId;
	}

	@Override
	protected Answer clone() throws CloneNotSupportedException {
		return new Answer(
				this.getId(),
				this.getNumber(),
				this.getBody(),
				this.getQuestionId());
	}

}
