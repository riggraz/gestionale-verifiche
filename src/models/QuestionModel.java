package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import entities.Question;
import utils.DBManager;
import utils.SQLUtils;

public class QuestionModel {
	
	private DBManager dbManager;
	private List<Question> l;
	
	private AnswerModel answerModel;
	
	public QuestionModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<Question>();
		
		answerModel = new AnswerModel(dbManager);
		
		try {
			dbManager.executeQuery("SELECT * FROM Question LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Question");
				dbManager.executeUpdate("CREATE TABLE Question (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"number INTEGER," +
						"body TEXT," +
						"testId VARCHAR(50) REFERENCES Test(id) ON DELETE CASCADE ON UPDATE CASCADE," +
						"correctAnswer INTEGER)");
			} catch (SQLException e1) {
				System.err.println("***Si e' verificato un errore nella creazione della tabella Question***");
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
				Question q = new Question(UUID.fromString(rs.getString("id")),
						rs.getInt("number"),
						rs.getString("body"),
						UUID.fromString(rs.getString("testId")),
						rs.getInt("correctAnswer"));
				
				l.add(q);
			}
			
			for (Question q : l) q.setAnswers(answerModel.loadByQuestionId(q.getId())); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertItem(String body, UUID testId) {
		int number = l.size();
		Question q = new Question(number, body, testId, -1);
		String query = String.format(
				"INSERT INTO Question (id, number, body, testId, correctAnswer) VALUES ('%s', %d, '%s', '%s', %d)",
				q.getId(),
				q.getNumber(),
				SQLUtils.escapeString(q.getBody()),
				testId,
				q.getCorrectAnswer());
		
		try {
			dbManager.executeUpdate(query);
			
			answerModel.insertItemsByQuestionId(q.getId());
			q.setAnswers(answerModel.loadByQuestionId(q.getId()));
			
			l.add(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// da finire
	public void deleteItem(UUID id) {
		if (getQuestionIndexById(id) == -1) return;
		
		String query = String.format(
				"DELETE FROM Question WHERE id='%s'",
				id);
		
		try {
			dbManager.executeUpdate(query);
			
			int questionNumber = getQuestionIndexById(id);
			l.remove(questionNumber);
			for (int i = questionNumber; i < l.size(); i++) {
				updateNumber(l.get(i).getId(), i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateNumber(UUID id, int newQuestionNumber) {
		if (getQuestionIndexById(id) == -1) return;
		
		String query = String.format(
				"UPDATE Question SET number=%d WHERE id='%s'",
				newQuestionNumber,
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getQuestionIndexById(id)).setNumber(newQuestionNumber);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBody(UUID id, String newBody) {
		if (getQuestionIndexById(id) == -1) return;
		
		String query = String.format(
				"UPDATE Question SET body='%s' WHERE id='%s'",
				SQLUtils.escapeString(newBody),
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getQuestionIndexById(id)).setBody(newBody);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCorrectAnswer(UUID id, int newCorrectAnswer) {
		if (getQuestionIndexById(id) == -1) return;
		
		String query = String.format(
				"UPDATE Question SET correctAnswer=%d WHERE id='%s'",
				newCorrectAnswer,
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getQuestionIndexById(id)).setCorrectAnswer(newCorrectAnswer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAnswerBody(UUID answerId, String newAnswerBody) {
		answerModel.updateBody(answerId, newAnswerBody);
	}
	
	public int getItemsCount() {
		return l.size();
	}
	
	public List<Question> getQuestions() {
		return Collections.unmodifiableList(l);
	}
	
	private int getQuestionIndexById(UUID id) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getId() == id) return i; 
		}
		return -1;
	}

}
