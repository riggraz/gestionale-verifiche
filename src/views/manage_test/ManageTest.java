package views.manage_test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import models.TestModel;
import print.PdfTest;
import utils.DBManager;
import views.manage_test.test_forms.EditTestForm;
import views.manage_test.test_forms.InsertTestForm;

public class ManageTest extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	DBManager dbManager;
	
	TestModel testModel;
	
	JButton insertTestBtn;
	
	JScrollPane tableScrollPane;
	JTable testsTable;
	
	JPanel testManagementPnl;
	JButton editTestBtn;
	JButton deleteTestBtn;
	JButton saveTestBtn;
	JButton printTestBtn;
	
	PdfTest pdfTest;
	
	public ManageTest(DBManager dbManager) {
		this.dbManager = dbManager;
		this.testModel = new TestModel(dbManager);
		
		setLayout(new BorderLayout(24, 24));
		setBorder(new EmptyBorder(16, 16, 16, 16));
		
		insertTestBtn = new JButton("Nuova verifica");
		insertTestBtn.setPreferredSize(new Dimension(250, 35));
		insertTestBtn.addActionListener(this);
		
		testsTable = new JTable();
		testsTable.setModel(testModel);
		testsTable.getSelectionModel().addListSelectionListener(this);
		testsTable.removeColumn(testsTable.getColumnModel().getColumn(0)); // nasconde la prima colonna (id)
		testsTable.removeColumn(testsTable.getColumnModel().getColumn(4)); // nasconde la colonna hasErrors
		testsTable.setRowHeight(20);
		tableScrollPane = new JScrollPane(testsTable);
		testsTable.setFillsViewportHeight(true);
		testsTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && testsTable.getSelectedRow() != -1) {
					new EditTestForm(
							dbManager,
							testModel,
							(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0),
							(String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1),
							(String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(),  2));
				}
			}
		});
		
		editTestBtn = new JButton("Modifica verifica");
		editTestBtn.setMaximumSize(new Dimension(250, 35));
		editTestBtn.setEnabled(false);
		editTestBtn.addActionListener(this);
		
		
		deleteTestBtn = new JButton("Elimina verifiche (0)");
		deleteTestBtn.setMaximumSize(new Dimension(250, 35));
		deleteTestBtn.setEnabled(false);
		deleteTestBtn.addActionListener(this);
		
		saveTestBtn = new JButton("Salva verifica");
		saveTestBtn.setMaximumSize(new Dimension(250, 35));
		saveTestBtn.setEnabled(false);
		saveTestBtn.addActionListener(this);
		
		printTestBtn = new JButton("Stampa verifica");
		printTestBtn.setMaximumSize(new Dimension(250, 35));
		printTestBtn.setEnabled(false);
		printTestBtn.addActionListener(this);
		
		testManagementPnl = new JPanel();
		testManagementPnl.setLayout(new BoxLayout(testManagementPnl, BoxLayout.Y_AXIS));
		testManagementPnl.add(editTestBtn);
		testManagementPnl.add(deleteTestBtn);
		testManagementPnl.add (Box.createRigidArea(new Dimension (0,40)));
		testManagementPnl.add(printTestBtn);
		testManagementPnl.add(saveTestBtn);
		
		add(insertTestBtn, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(testManagementPnl, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertTestBtn) {
			new InsertTestForm(dbManager, testModel);
		} else if (e.getSource() == editTestBtn) {
			new EditTestForm(
				dbManager,
				testModel,
				(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0),
				(String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1),
				(String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(),  2));
		} else if (e.getSource() == printTestBtn) {
			int dialogResult = 0;
			
			int hasErrors = (int) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 5);
			if (hasErrors == 1) {
				String testName = (String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1);
				dialogResult = JOptionPane.showConfirmDialog(this,
						"La verifica '" + testName + "' non e' stata compilata correttamente.\n"
						+ "Vuoi comunque procedere alla stampa?", "Sei sicuro?",
						JOptionPane.YES_NO_OPTION);
			}
			
			if (hasErrors == 0 || dialogResult == JOptionPane.YES_OPTION) {
				pdfTest = new PdfTest(dbManager,(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0));
			} else {
				System.out.println("Niente stampa");
			}
		} else if (e.getSource() == deleteTestBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(this,
					"Vuoi davvero eliminare le " + testsTable.getSelectedRowCount() +
					" verifiche selezionate?", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				testModel.deleteRows(testsTable.getSelectedRows());
			}
		}else if(e.getSource() == saveTestBtn) {
			pdfTest = new PdfTest(dbManager,(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0));
			pdfTest.save();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (testsTable.getSelectedRow() == -1 && testsTable.getRowCount() > 0) testsTable.setRowSelectionInterval(0, 0);
		
		deleteTestBtn.setText("Elimina verifiche (" + testsTable.getSelectedRowCount() + ")");
		deleteTestBtn.setEnabled(testsTable.getSelectedRowCount() > 0);
		editTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
		printTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
		saveTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
	}

}
