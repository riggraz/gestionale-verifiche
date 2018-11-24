package entities;

import java.util.UUID;

public class SchoolClass {
	
	private UUID id;
	private String name;
	
	public SchoolClass(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
	}
	
	public SchoolClass(UUID id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return name;
	}

}