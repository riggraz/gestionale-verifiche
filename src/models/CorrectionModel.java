package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entities.Correction;
import entities.Student;
import utils.DBManager;
import utils.SQLUtils;

public class CorrectionModel {

	private DBManager dbManager;
	private List<Correction> l;
	
	public CorrectionModel(DBManager dbManager) {
		this.dbManager=dbManager;
		l = new ArrayList<Correction>();
		
		try {
			dbManager.executeQuery("SELECT * FROM Correction LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Correction");
				dbManager.executeUpdate("CREATE TABLE Correction (" +
						"idTest VARCHAR(50) REFERENCES Test(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
						"idStudent VARCHAR(50) REFERENCES Student(id) ON DELETE CASCADE ON UPDATE CASCADE, " + 
						"vote FLOAT, " +
						"date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
						"schoolClassName VARCHAR(50) REFERENCES SchoolClass(name) ON DELETE CASCADE ON UPDATE CASCADE, " +
						"PRIMARY KEY(idTest, idStudent))");
			} catch (SQLException e1) {
				System.err.println("***Si e' verificato un errore nella creazione della tabella Correction***");
				e1.printStackTrace();
			}
		}
	}
	
	public List<Correction> loadByIdTestAndClass(String className, UUID idTest) {
		ResultSet rs;
		String query = String.format(
				"SELECT * FROM Correction WHERE schoolClassName='%s' AND idTest='%s'",
				SQLUtils.escapeString(className),
				idTest);
		
		l.clear();
		
		try {
			rs = dbManager.executeQuery(query);
			while (rs.next()) {
				l.add(new Correction(UUID.fromString(rs.getString("idTest")),
						UUID.fromString(rs.getString("idStudent")),
						rs.getDouble("vote"),
						rs.getString("date"),
						rs.getString("schoolClassName")));
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public void updateVote(UUID idStudent,UUID idTest ,double vote) {
		
		String query = String.format(
				"UPDATE Correction SET vote='%f' WHERE idStudent='%s' and idTest='%s'",
				vote,
				idStudent,
				idTest);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getCorrectionIndexByIdStudent(idStudent)).setVote(vote);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getCorrectionIndexByIdStudent(UUID idStudent) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getIdStudent().equals(idStudent)) return i; 
		}
		return -1;
	}
	
	
	public void insertItem(UUID idTest,List<Student> studList) {
		Correction c = null;
		
		for(Student s : studList) {
			c = new Correction(idTest,s.getId(),-1.0,s.getSchoolClassName());
		}
		
		String query = "INSERT INTO Correction (idTest, idStudent, vote, schoolClassName) VALUES ";
		for(int i =0;i<(studList.size()-1);i++) {
			query += String.format("('%s', '%s', '%f', '%s'), ",
					idTest,
					studList.get(i).getId(),
					-1.0,
					SQLUtils.escapeString(studList.get(i).getSchoolClassName()));
		}
		
		query += String.format("('%s', '%s', '%f', '%s');",
				idTest,
				studList.get(studList.size()-1).getId(),
				-1.0,
				SQLUtils.escapeString(studList.get(studList.size()-1).getSchoolClassName()));
		
		try {
			dbManager.executeUpdate(query);
			l.add(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
