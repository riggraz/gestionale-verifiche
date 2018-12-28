package models;

import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import utils.DBManager;

public class CorrectionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	CorrectionModel correctionModel;
	TestModel testModel;
	
	private final String[] columnNames = new String[] {
			"Verifica", "Voto", "Data"
	};

	private final Class<?>[] columnClass = new Class<?>[] {
			String.class, Double.class, String.class
	};
	
	public CorrectionTableModel(DBManager dbManager) {
		this.correctionModel = new CorrectionModel(dbManager);
		this.testModel = new TestModel(dbManager);
	}

	@Override
	public int getRowCount() {
		return correctionModel.getSanitizedL().size();
	}

	@Override
	public int getColumnCount() {
		return columnClass.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			UUID testId = correctionModel.getSanitizedL().get(rowIndex).getIdTest();
			return testModel.getTestById(testId).getName();
		case 1: return correctionModel.getSanitizedL().get(rowIndex).getVote();
		case 2: return correctionModel.getSanitizedL().get(rowIndex).getDate().substring(0, 10);
		default: return null;
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
		return false;
	}
	
	public CorrectionModel getParentModel() {
		return correctionModel;
	}
	
	public void loadByStudentId(UUID studentId) {
		correctionModel.loadByStudentId(studentId);
		fireTableDataChanged();
	}

}
