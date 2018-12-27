package views.manage_school_class.school_class_forms;

import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import models.SchoolClassComboBoxModel;

public class InsertSchoolClassForm extends SchoolClassForm {

	private static final long serialVersionUID = 1L;
	
	private SchoolClassComboBoxModel schoolClassModel;
	
	public InsertSchoolClassForm(JComponent parent, SchoolClassComboBoxModel schoolClassModel) {
		super(parent, "Aggiungi classe");
		
		this.schoolClassModel = schoolClassModel;
	}

	@Override
	public void save() {
		try {
			schoolClassModel.insertItem(schoolClassNameTxt.getText());
		} catch (SQLException e) {
			if (e.getErrorCode() == 19) { // UNIQUE constraint failed on column name
				JOptionPane.showMessageDialog(this, "Esiste già una classe con questo nome.");
			}
		}
	}

}
