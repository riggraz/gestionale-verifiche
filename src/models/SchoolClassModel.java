package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entities.SchoolClass;
import utils.DBManager;
import utils.SQLUtils;

public class SchoolClassModel {
	
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
						SQLUtils.escapeString(rs.getString("name"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DBManager getDBManager() {
		return dbManager;
	}
	
	public List<SchoolClass> getL() {
		return l;
	}

}
