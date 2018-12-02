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
	
	public Test(UUID id, String name, String description, String createdAt, String updatedAt) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public Test(String name, String description) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.description = description;
		this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
		this.updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));

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

}
