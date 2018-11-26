package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;

import entities.SchoolClass;
import utils.DBManager;

public class SchoolClassModel extends DefaultComboBoxModel<SchoolClass> {

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	private List<SchoolClass> l;
	
	public SchoolClassModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<SchoolClass>();
		
		try {
			dbManager.executeQuery("SELECT * FROM SchoolClass LIMIT 1");
		} catch (SQLException e) {
			try {
				dbManager.executeUpdate("DROP TABLE IF EXISTS SchoolClass");
				dbManager.executeUpdate("CREATE TABLE SchoolClass (" +
						"id VARCHAR(50) PRIMARY KEY, " +
						"name VARCHAR(10) UNIQUE)");
			} catch (SQLException e1) {
				System.err.println("***Si è verificato un errore nella creazione della tabella SchoolClass***");
				e1.printStackTrace();
			}
		}
		
		loadAll();
	}
	
	public void loadAll() {
		ResultSet rs;
		try {
			rs = dbManager.executeQuery("SELECT * FROM SchoolClass ORDER BY name");
			
			while (rs.next()) {
				l.add(new SchoolClass(
						UUID.fromString(rs.getString("id")),
						rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertItem(String name) throws SQLException {
		SchoolClass sc = new SchoolClass(name);
		String query = String.format(
				"INSERT INTO SchoolClass (id, name) VALUES ('%s', '%s')",
				sc.getId().toString(),
				sc.getName());
				
		try {
			dbManager.executeUpdate(query);
			l.add(sc);
//			Collections.sort(l);
			fireIntervalAdded(this, l.size()-1, l.size()-1);
			setSelectedItem(sc);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void updateItem(int index, String name) throws SQLException {
		String query = String.format(
				"UPDATE SchoolClass SET name='%s' WHERE id='%s'",
				name,
				l.get(index).getId());
		
		try {
			dbManager.executeUpdate(query);
			l.get(index).setName(name);
//			Collections.sort(l);
			fireContentsChanged(this, index, index);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void deleteItem(int index) {
		String query = String.format(
				"DELETE FROM SchoolClass WHERE id='%s'",
				l.get(index).getId());
		
		try {
			dbManager.executeUpdate(query);
			l.remove(index);
//			Collections.sort(l);
			fireIntervalRemoved(this, index, index);
			if (l.size() > 0) setSelectedItem(l.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getSize() {
		return l.size();
	}

	@Override
	public SchoolClass getElementAt(int index) {
		return l.get(index);
	}

}
