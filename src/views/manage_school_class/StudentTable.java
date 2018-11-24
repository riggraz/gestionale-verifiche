package views.manage_school_class;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.SchoolClass;
import models.StudentModel;
import utils.DBManager;
import views.manage_school_class.student_forms.InsertStudentForm;

public class StudentTable extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	JComboBox<SchoolClass> schoolClassCmbBox;
	
	JScrollPane tableScrollPane;
	JTable table;
	
	JPanel studentManagementPnl;
	JButton insertStudentBtn;
	JButton deleteStudentBtn;
	
	public StudentTable(DBManager dbManager, JComboBox<SchoolClass> schoolClassCmbBox) {
		this.schoolClassCmbBox = schoolClassCmbBox;
		
		table = new JTable();
		studentModel = new StudentModel(dbManager);
		table.setModel(studentModel);
		table.getSelectionModel().addListSelectionListener(this);
		table.removeColumn(table.getColumnModel().getColumn(0)); // nasconde la prima colonna (id)
		
		tableScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		insertStudentBtn = new JButton("Nuovo studente");
		insertStudentBtn.addActionListener(this);
		
		deleteStudentBtn = new JButton("Elimina studenti (0)");
		deleteStudentBtn.addActionListener(this);
		deleteStudentBtn.setEnabled(false);
		
		studentManagementPnl = new JPanel();
		studentManagementPnl.setLayout(new BoxLayout(studentManagementPnl, BoxLayout.Y_AXIS));
		studentManagementPnl.add(insertStudentBtn);
		studentManagementPnl.add(deleteStudentBtn);
		
		add(tableScrollPane);
		add(studentManagementPnl);
	}
	
	public void populateTable(String schoolClassName) {
		studentModel.loadBySchoolClassName(schoolClassName);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertStudentBtn) {
			new InsertStudentForm(
					studentModel,
					((SchoolClass)schoolClassCmbBox.getSelectedItem()).getName());
		}
		
		if (e.getSource() == deleteStudentBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Vuoi davvero eliminare i " + table.getSelectedRowCount() +
					" studenti selezionati?", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				studentModel.deleteRows(table.getSelectedRows());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		deleteStudentBtn.setText("Elimina studenti (" + table.getSelectedRowCount() + ")");
		deleteStudentBtn.setEnabled(table.getSelectedRowCount() > 0);
	}

}
