package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.DefaultListModel;

import entities.SchoolClass;
import utils.DBManager;
import utils.SQLUtils;

public class JListSchoolClassModel extends DefaultListModel<SchoolClass> {


	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	private List<SchoolClass> l;
	
	public JListSchoolClassModel(DBManager dbManager) {
		this.dbManager = dbManager;
		l = new ArrayList<SchoolClass>();
		
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
	
	public int getSize() {
		return l.size();
	}
	
	public SchoolClass getElementAt(int i) {
		return l.get(i);
	}
	
}

