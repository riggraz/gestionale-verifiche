package entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Correction {
	private UUID idTest;
	private UUID idStudent;
	private double vote; //vote by 0 from 10, -1 if student absent
	private String className;
	private String date;
	
	public Correction(UUID idTest, UUID idStudent, double vote, String className,String date) {
		super();
		this.idTest = idTest;
		this.idStudent = idStudent;
		this.className = className;
		this.vote = vote;
		this.date = date;
	}
	
	public Correction(UUID idTest, UUID idStudent, double vote, String className) {
		super();
		this.idTest = idTest;
		this.idStudent = idStudent;
		this.className = className;
		this.vote = vote;
		this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
	}

	public UUID getIdTest() {
		return idTest;
	}

	public void setIdTest(UUID idTest) {
		this.idTest = idTest;
	}

	public UUID getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(UUID idStudent) {
		this.idStudent = idStudent;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	

}
