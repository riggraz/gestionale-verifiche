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

public class StudentModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	List<Student> l;
	
	private final String[] columnNames = new String[] {
			"id", "Nome", "Cognome", "Classe"
	};

	private final Class<?>[] columnClass = new Class<?>[] {
			String.class, String.class, String.class, String.class
	};
	
	public StudentModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<Student>();
		
		try {
			dbManager.executeQuery("SELECT * FROM student LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS student");
				dbManager.executeUpdate("CREATE TABLE student (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"first_name VARCHAR(50)," +
						"last_name VARCHAR(50)," +
						"school_class_name VARCHAR(50) REFERENCES school_class(name) ON DELETE CASCADE ON UPDATE CASCADE)");
			} catch (SQLException e1) {
				System.err.println("***Si è verificato un errore nella creazione della tabella student***");
				e1.printStackTrace();
			}
		}
	}
	
	public void loadBySchoolClassName(String schoolClassName) {
		ResultSet rs;
		String query = String.format(
				"SELECT * FROM student WHERE school_class_name='%s' ORDER BY last_name, first_name",
				schoolClassName);
		
		int previousSize = l.size();
		l.clear();
		if (previousSize > 0) fireTableRowsDeleted(0, previousSize-1);
		
		try {
			rs = dbManager.executeQuery(query);
			while (rs.next()) {
				l.add(new Student(UUID.fromString(rs.getString("id")),
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("school_class_name")));
				fireTableRowsInserted(0, l.size()-1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertRow(String firstName, String lastName, String schoolClassName) {
		Student s = new Student(firstName, lastName, schoolClassName);
		String query = String.format(
				"INSERT INTO student (id, first_name, last_name, school_class_name) VALUES ('%s', '%s', '%s', '%s')",
				s.getId(),
				s.getFirstName(),
				s.getLastName(),
				s.getSchoolClassName()
				);
		
		try {
			dbManager.executeUpdate(query);
			l.add(s);
//			Collections.sort(l);
			fireTableRowsInserted(l.size()-1, l.size()-1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRows(int[] rows) {
		if (rows.length == 0) return;
		
		// reverse the array to avoid index problem during deletion
		for (int i = 0; i < rows.length / 2; i++) {
			int tmp = rows[i];
			rows[i] = rows[rows.length - i - 1];
			rows[rows.length - i - 1] = tmp;
		}
		
		for (int i = 0; i < rows.length; i++) {
			String query = String.format(
					"DELETE FROM student WHERE id='%s'",
					l.get(rows[i]).getId()
					);
			
			try {
				dbManager.executeUpdate(query);
				l.remove(rows[i]);
//				Collections.sort(l);
				fireTableRowsDeleted(rows[i], rows[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
		case 3: return l.get(rowIndex).getSchoolClassName();
		default: return null;
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String query = "UPDATE student SET ";
		
		switch (columnIndex) {
		case 1: query += "first_name='" + aValue + "'"; break;
		case 2: query += "last_name='" + aValue + "'"; break;
		case 3: query += "school_class_name='" + aValue + "'"; break;
		}
		
		query += " WHERE id='" + l.get(rowIndex).getId() + "'";
		
		try {
			dbManager.executeUpdate(query);
			
			switch (columnIndex) {
			case 1: l.get(rowIndex).setFirstName((String)aValue); break;
			case 2: l.get(rowIndex).setLastName((String)aValue); break;
			case 3: l.get(rowIndex).setSchoolClassName((String)aValue); break;
			}
			
//			Collections.sort(l);
			fireTableCellUpdated(rowIndex, columnIndex);
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
		return columnIndex > 0;
	}

}
