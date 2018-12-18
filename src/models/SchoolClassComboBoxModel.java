package models;

import java.sql.SQLException;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;

import entities.SchoolClass;
import utils.DBManager;
import utils.SQLUtils;

public class SchoolClassComboBoxModel extends DefaultComboBoxModel<SchoolClass> {

	private static final long serialVersionUID = 1L;
	
	private SchoolClassModel schoolClassModel;
	
	public SchoolClassComboBoxModel(DBManager dbManager) {
		this.schoolClassModel = new SchoolClassModel(dbManager);
	}
	
	public void insertItem(String name) throws SQLException {
		SchoolClass sc = new SchoolClass(name);
		String query = String.format(
				"INSERT INTO SchoolClass (id, name) VALUES ('%s', '%s')",
				sc.getId().toString(),
				SQLUtils.escapeString(sc.getName()));
				
		try {
			schoolClassModel.getDBManager().executeUpdate(query);
			schoolClassModel.getL().add(sc);
			Collections.sort(schoolClassModel.getL());
			fireContentsChanged(this, 0, schoolClassModel.getL().size()-1);
			setSelectedItem(sc);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void updateItem(int index, String name) throws SQLException {
		String query = String.format(
				"UPDATE SchoolClass SET name='%s' WHERE id='%s'",
				SQLUtils.escapeString(name),
				schoolClassModel.getL().get(index).getId());
		
		try {
			schoolClassModel.getDBManager().executeUpdate(query);
			schoolClassModel.getL().get(index).setName(name);
			Collections.sort(schoolClassModel.getL());
			fireContentsChanged(this, 0, schoolClassModel.getL().size()-1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void deleteItem(int index) {
		String query = String.format(
				"DELETE FROM SchoolClass WHERE id='%s'",
				schoolClassModel.getL().get(index).getId());
		
		try {
			schoolClassModel.getDBManager().executeUpdate(query);
			schoolClassModel.getL().remove(index);
			Collections.sort(schoolClassModel.getL());
			fireContentsChanged(this, 0, schoolClassModel.getL().size()-1);
			if (schoolClassModel.getL().size() > 0) setSelectedItem(schoolClassModel.getL().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getSize() {
		return schoolClassModel.getL().size();
	}

	@Override
	public SchoolClass getElementAt(int index) {
		return schoolClassModel.getL().get(index);
	}

}
