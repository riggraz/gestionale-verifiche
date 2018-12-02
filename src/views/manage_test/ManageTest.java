package views.manage_test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import models.TestModel;
import utils.DBManager;
import views.manage_test.test_forms.InsertTestForm;

public class ManageTest extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	DBManager dbManager;
	
	TestModel testModel;
	
	JButton insertTestBtn;
	
	JScrollPane tableScrollPane;
	JTable testsTable;
	
	JPanel testManagementPnl;
	JButton deleteTestBtn;
	
	public ManageTest(DBManager dbManager) {
		this.dbManager = dbManager;
		this.testModel = new TestModel(dbManager);
		
		setLayout(new BorderLayout(24, 24));
		
		insertTestBtn = new JButton("Nuova verifica");
		insertTestBtn.addActionListener(this);
		
		testsTable = new JTable();
		testsTable.setModel(testModel);
		testsTable.getSelectionModel().addListSelectionListener(this);
		testsTable.removeColumn(testsTable.getColumnModel().getColumn(0)); // nasconde la prima colonna (id)
		tableScrollPane = new JScrollPane(testsTable);
		testsTable.setFillsViewportHeight(true);
		
		deleteTestBtn = new JButton("Elimina verifiche (0)");
		deleteTestBtn.setEnabled(false);
		deleteTestBtn.addActionListener(this);
		testManagementPnl = new JPanel();
		testManagementPnl.setLayout(new BoxLayout(testManagementPnl, BoxLayout.Y_AXIS));
		testManagementPnl.add(deleteTestBtn);
		
		add(insertTestBtn, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(testManagementPnl, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertTestBtn) {
			new InsertTestForm(dbManager, testModel);
		} else if (e.getSource() == deleteTestBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Vuoi davvero eliminare le " + testsTable.getSelectedRowCount() +
					" verifiche selezionate?", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				testModel.deleteRows(testsTable.getSelectedRows());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		deleteTestBtn.setText("Elimina verifiche (" + testsTable.getSelectedRowCount() + ")");
		deleteTestBtn.setEnabled(testsTable.getSelectedRowCount() > 0);
	}

}
