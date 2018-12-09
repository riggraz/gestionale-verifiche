package entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Test {
	
	private UUID id;
	private String name;
	private String description;
	private String createdAt;
	private String updatedAt;
	private int hasErrors; // 0: no errors, 1: errors; we chose int (and not boolean) for coherency with sqlite datatypes
	
	public Test(UUID id, String name, String description, String createdAt, String updatedAt, int hasErrors) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.hasErrors = hasErrors;
	}
	
	public Test(String name, String description) {
		this(UUID.randomUUID(),
				name,
				description,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())),
				1);
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public int hasErrors() {
		return hasErrors;
	}
	public void setHasErrors(int hasErrors) {
		this.hasErrors = hasErrors;
	}

}
