package models;

import java.sql.SQLException;

import javax.swing.DefaultListModel;

import entities.SchoolClass;
import utils.DBManager;

public class SchoolClassListModel extends DefaultListModel<SchoolClass> {

	private static final long serialVersionUID = 1L;
	
	private SchoolClassModel schoolClassModel;
	
	public SchoolClassListModel(DBManager dbManager) {
		this.schoolClassModel = new SchoolClassModel(dbManager);
	}
	
	public int getStudentCountBySchoolClassName(String schoolClassName) {
		try {
			return schoolClassModel
					.getDBManager()
					.executeQuery("SELECT COUNT(*) AS studentCount FROM Student WHERE schoolClassName='" + schoolClassName + "'")
					.getInt("studentCount");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getSize() {
		return schoolClassModel.getL().size();
	}
	
	public SchoolClass getElementAt(int i) {
		return schoolClassModel.getL().get(i);
	}
	
}