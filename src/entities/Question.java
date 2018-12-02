package entities;

import java.util.UUID;

public class Question {
	
	private UUID id;
	private int number;
	private String body;
	private UUID testId;
	
	public Question(UUID id, int number, String body, UUID testId) {
		super();
		this.id = id;
		this.number = number;
		this.body = body;
		this.testId = testId;
	}

	public Question(int number, String body, UUID testId) {
		super();
		this.id = UUID.randomUUID();
		this.number = number;
		this.body = body;
		this.testId = testId;
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
		this.number = number;
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
	
}
