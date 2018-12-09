package entities;

import java.util.UUID;

public class Student implements Comparable<Student> {
	
	private UUID id;
	private String firstName;
	private String lastName;
	private String schoolClassName;
	
	public Student(UUID id, String firstName, String lastName, String schoolClassName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.schoolClassName = schoolClassName;
	}
	
	public Student(String firstName, String lastName, String schoolClassName) {
		this(UUID.randomUUID(), firstName, lastName, schoolClassName);
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getSchoolClassName() {
		return schoolClassName;
	}
	public void setSchoolClassName(String schoolClassName) {
		this.schoolClassName = schoolClassName;
	}

	@Override
	public int compareTo(Student s) {
		int i = this.getLastName().compareTo(s.getLastName());
		if (i != 0) return i;
		else return this.getFirstName().compareTo(s.getFirstName());
	}
	
}
