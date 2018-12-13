package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import entities.Test;
import utils.DBManager;
import utils.SQLUtils;

public class TestModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	private List<Test> l;
	
	private final String[] columnNames = new String[] {
			"id", "Nome", "Descrizione", "Creata", "Modificata", "Errori"
	};

	private final Class<?>[] columnClass = new Class<?>[] {
			String.class, String.class, String.class, String.class, String.class, Integer.class
	};
	
	public TestModel(DBManager dbManager) {
		this.dbManager = dbManager;
		this.l = new ArrayList<Test>();
		
		try {
			dbManager.executeQuery("SELECT * FROM Test LIMIT 1");			
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS Test");
				dbManager.executeUpdate("CREATE TABLE Test (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"name VARCHAR(50)," +
						"description TEXT," +
						"createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
						"updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
						"hasErrors INTEGER DEFAULT 1 NOT NULL)");
			} catch (SQLException e1) {
				System.err.println("***Si è verificato un errore nella creazione della tabella Test***");
				e1.printStackTrace();
			}
		}
		
		loadAll();
	}
	
	public void loadAll() {
		ResultSet rs;
		try {
			rs = dbManager.executeQuery("SELECT * FROM Test ORDER BY updatedAt DESC");
			
			while (rs.next()) {
				l.add(new Test(
						UUID.fromString(rs.getString("id")),
						rs.getString("name"),
						rs.getString("description"),
						rs.getString("createdAt"),
						rs.getString("updatedAt"),
						rs.getInt("hasErrors")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UUID insertRow(String name, String description) {
		Test t = new Test(name, description);
		String query = String.format(
				"INSERT INTO Test (id, name, description) VALUES ('%s', '%s', '%s')",
				t.getId().toString(),
				SQLUtils.escapeString(t.getName()),
				SQLUtils.escapeString(t.getDescription()));
				
		try {
			dbManager.executeUpdate(query);
			l.add(t);
			Collections.sort(l);
			fireTableDataChanged();
			return t.getId();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteRows(int[] rows) {
		if (rows.length == 0) return;
		
		// remove in inverse order to avoid index problems
		for (int i = rows.length-1; i >= 0; i--) {
			String query = String.format(
					"DELETE FROM Test WHERE id='%s'",
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
	
	public void updateName(UUID id, String newName) {
		String query = String.format(
				"UPDATE Test SET name='%s' WHERE id='%s'",
				SQLUtils.escapeString(newName),
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getTestIndexById(id)).setName(newName);
			
			updateUpdatedAt(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDescription(UUID id, String newDescription) {
		String query = String.format(
				"UPDATE Test SET description='%s' WHERE id='%s'",
				SQLUtils.escapeString(newDescription),
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getTestIndexById(id)).setDescription(newDescription);
			
			updateUpdatedAt(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateUpdatedAt(UUID id) {
		String query = String.format(
				"UPDATE Test SET updatedAt=CURRENT_TIMESTAMP WHERE id='%s'",
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getTestIndexById(id)).setUpdatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
			
			Collections.sort(l);
			fireTableDataChanged();
			
			System.out.println("Chiamando updateUpdatedAt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateHasErrors(UUID id, int hasErrors) {
		// update field only if necessary
		if (l.get(getTestIndexById(id)).hasErrors() == hasErrors) return;
		
		String query = String.format(
				"UPDATE Test SET hasErrors=%d WHERE id='%s'",
				hasErrors,
				id);
		
		try {
			dbManager.executeUpdate(query);
			l.get(getTestIndexById(id)).setHasErrors(hasErrors);
		} catch (SQLException e) {
			e.printStackTrace();
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
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: return l.get(rowIndex).getId();
		case 1: return l.get(rowIndex).getName();
		case 2: return l.get(rowIndex).getDescription();
		case 3: return l.get(rowIndex).getCreatedAt();
		case 4: return l.get(rowIndex).getUpdatedAt();
		case 5: return l.get(rowIndex).hasErrors();
		default: return null;
		}
	}
	
//	@Override
//	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		String query = "UPDATE Test SET ";
//		
//		switch (columnIndex) {
//		case 1: query += "name='" + (SQLUtils.escapeString((String)aValue)) + "'"; break;
//		case 2: query += "description='" + (SQLUtils.escapeString((String)aValue)) + "'"; break;
//		}
//		
//		query += " WHERE id='" + l.get(rowIndex).getId() + "'";
//		
//		try {
//			dbManager.executeUpdate(query);
//			
//			switch (columnIndex) {
//			case 1: l.get(rowIndex).setName((String)aValue); break;
//			case 2: l.get(rowIndex).setDescription((String)aValue); break;
//			}
//			
////			Collections.sort(l);
//			fireTableCellUpdated(rowIndex, columnIndex);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	private int getTestIndexById(UUID id) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getId() == id) return i; 
		}
		return -1;
	}

}
