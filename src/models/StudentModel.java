package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import entities.Student;
import utils.DBManager;
import utils.MathUtils;
import utils.SQLUtils;

public class StudentModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	private List<Student> l;
	
	private CorrectionModel correctionModel;
	
	private final String[] columnNames = new String[] {
			"id", "Nome", "Cognome", "Media"
	};

	private final Class<?>[] columnClass = new Class<?>[] {
			String.class, String.class, String.class, Double.class
	};
	
	public StudentModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<Student>();
		
		this.correctionModel = new CorrectionModel(dbManager);
		
		try {
			dbManager.executeQuery("SELECT * FROM Student LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Student");
				dbManager.executeUpdate("CREATE TABLE Student (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"firstName VARCHAR(50)," +
						"lastName VARCHAR(50)," +
						"schoolClassName VARCHAR(50) REFERENCES SchoolClass(name) ON DELETE CASCADE ON UPDATE CASCADE)");
			} catch (SQLException e1) {
				System.err.println("***Si e'� verificato un errore nella creazione della tabella Student***");
				e1.printStackTrace();
			}
		}
	}
	
	public void loadBySchoolClassName(String schoolClassName) {
		ResultSet rs;
		String query = String.format(
				"SELECT * FROM Student WHERE schoolClassName='%s' ORDER BY lastName, firstName",
				SQLUtils.escapeString(schoolClassName));
		
		l.clear();
		
		try {
			rs = dbManager.executeQuery(query);
			while (rs.next()) {
				l.add(new Student(UUID.fromString(rs.getString("id")),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("schoolClassName")));
			}
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertRow(String firstName, String lastName, String schoolClassName) {
		Student s = new Student(firstName, lastName, schoolClassName);
		String query = String.format(
				"INSERT INTO Student (id, firstName, lastName, schoolClassName) VALUES ('%s', '%s', '%s', '%s')",
				s.getId(),
				SQLUtils.escapeString(s.getFirstName()),
				SQLUtils.escapeString(s.getLastName()),
				SQLUtils.escapeString(s.getSchoolClassName()));
		
		try {
			dbManager.executeUpdate(query);
			l.add(s);
			Collections.sort(l);
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateRow(UUID id, String firstName, String lastName, String schoolClassName) {
		String query = String.format(
				"UPDATE Student SET firstName='%s', lastName='%s', schoolClassName='%s' WHERE id='%s'",
				SQLUtils.escapeString(firstName),
				SQLUtils.escapeString(lastName),
				SQLUtils.escapeString(schoolClassName),
				id);
		
		try {
			dbManager.executeUpdate(query);
			for (Student s : l) {
				if (s.getId() == id) {
					s.setFirstName(firstName);
					s.setLastName(lastName);
					s.setSchoolClassName(schoolClassName);
				}
			}
			// se viene cambiata la SchoolClass rimuove lo studente in modo da non visualizzarlo in tabella
			l.removeIf(s -> s.getSchoolClassName() == schoolClassName);
			Collections.sort(l);
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRows(int[] rows) {
		if (rows.length == 0) return;
		
		// remove in inverse order to avoid index problems
		for (int i = rows.length-1; i >= 0; i--) {
			String query = String.format(
					"DELETE FROM Student WHERE id='%s'",
					l.get(rows[i]).getId());
			
			try {
				dbManager.executeUpdate(query);
				l.remove(rows[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		Collections.sort(l);
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return l.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: return l.get(rowIndex).getId();
		case 1: return l.get(rowIndex).getFirstName();
		case 2: return l.get(rowIndex).getLastName();
		case 3: return MathUtils.round(correctionModel.getAverageByStudentId(l.get(rowIndex).getId()), 2);
		default: return null;
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String query = "UPDATE Student SET ";
		
		switch (columnIndex) {
		case 1: query += "firstName='" + (SQLUtils.escapeString((String)aValue)) + "'"; break;
		case 2: query += "lastName='" + (SQLUtils.escapeString((String)aValue)) + "'"; break;
		}
		
		query += " WHERE id='" + l.get(rowIndex).getId() + "'";
		
		try {
			dbManager.executeUpdate(query);
			
			switch (columnIndex) {
			case 1: l.get(rowIndex).setFirstName((String)aValue); break;
			case 2: l.get(rowIndex).setLastName((String)aValue); break;
			}
			
			Collections.sort(l);
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != 3;
	}

	public List<Student> getListStudent(){
		return l;
	}
	
	public Student studentByUUID(UUID uuidStudent) {
		for(Student s : l) {
			if(s.getId().equals(uuidStudent)) return s;
		}
		return null;
	}
}
