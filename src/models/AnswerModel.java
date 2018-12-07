package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entities.Answer;
import utils.DBManager;

public class AnswerModel {
	
	private DBManager dbManager;
	
	public AnswerModel(DBManager dbManager) {
		this.dbManager = dbManager;
		
		try {
			dbManager.executeQuery("SELECT * FROM Answer LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Answer");
				dbManager.executeUpdate("CREATE TABLE Answer (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"number INTEGER," +
						"body TEXT," +
						"questionId VARCHAR(50) REFERENCES Question(id) ON DELETE CASCADE ON UPDATE CASCADE)");
			} catch (SQLException e1) {
				System.err.println("***Si è verificato un errore nella creazione della tabella Answer***");
				e1.printStackTrace();
			}
		}
	}
	
	public List<Answer> loadByQuestionId(UUID questionId) {
		ResultSet rs;
		String query = String.format(
				"SELECT * FROM Answer WHERE questionId='%s' ORDER BY number",
				questionId);
		
		try {
			rs = dbManager.executeQuery(query);
			List<Answer> l = new ArrayList<Answer>();
			while (rs.next()) {
				l.add(new Answer(UUID.fromString(rs.getString("id")),
						rs.getInt("number"),
						rs.getString("body"),
						UUID.fromString(rs.getString("questionId"))));
			}
			
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertItemsByQuestionId(UUID questionId) {
		for (int number = 0; number < 4; number++) {
			Answer a = new Answer(number, "", questionId);
			String query = String.format(
					"INSERT INTO Answer (id, number, body, questionId) VALUES ('%s', %d, '%s', '%s')",
					a.getId(),
					a.getNumber(),
					a.getBody(),
					questionId);
			
			try {
				dbManager.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteItemsByQuestionId(UUID questionId) {
		String query = String.format(
				"DELETE FROM Answer WHERE questionId='%s'",
				questionId);
		
		try {
			dbManager.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBody(UUID answerId, String newBody) {
		String query = String.format(
				"UPDATE Answer SET body='%s' WHERE id='%s'",
				newBody,
				answerId);
		
		try {
			dbManager.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
