package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entities.Question;
import utils.DBManager;

public class QuestionModel {
	
	DBManager dbManager;
	List<Question> l;
	
	public QuestionModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<Question>();
		
		try {
			dbManager.executeQuery("SELECT * FROM Question LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Question");
				dbManager.executeUpdate("CREATE TABLE Question (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"number INTEGER," +
						"body TEXT," +
						"testId VARCHAR(50) REFERENCES Test(id) ON DELETE CASCADE ON UPDATE CASCADE)");
			} catch (SQLException e1) {
				System.err.println("***Si è verificato un errore nella creazione della tabella Question***");
				e1.printStackTrace();
			}
		}
	}
	
	public void loadByTestId(UUID testId) {
		ResultSet rs;
		String query = String.format(
				"SELECT * FROM Question WHERE testId='%s' ORDER BY number",
				testId);
		
		try {
			rs = dbManager.executeQuery(query);
			while (rs.next()) {
				l.add(new Question(UUID.fromString(rs.getString("id")),
						rs.getInt("number"),
						rs.getString("body"),
						UUID.fromString(rs.getString("testId"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertItem(String body, UUID testId) {
		int number = l.size() + 1;
		Question q = new Question(number, body, testId);
		String query = String.format(
				"INSERT INTO Question (id, number, body, testId) VALUES ('%s', %d, '%s', '%s')",
				q.getId(),
				q.getNumber(),
				q.getBody(),
				testId
				);
		try {
			dbManager.executeUpdate(query);
			l.add(q);
//			Collections.sort(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
