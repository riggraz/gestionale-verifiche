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
import utils.DBManager;
import utils.print.PdfTest;
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
	JButton previewTestBtn;
	JButton correctTestBtn;
	
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
		setSettingBtn(editTestBtn);
		
		deleteTestBtn = new JButton("Elimina verifiche (0)");
		setSettingBtn(deleteTestBtn);
		
		previewTestBtn = new JButton("Anteprima verifica");
		setSettingBtn(previewTestBtn);
		
		correctTestBtn = new JButton("Correggi");
		setSettingBtn(correctTestBtn);
		
		testManagementPnl = new JPanel();
		testManagementPnl.setLayout(new BoxLayout(testManagementPnl, BoxLayout.Y_AXIS));
		testManagementPnl.add(editTestBtn);
		testManagementPnl.add(deleteTestBtn);
		testManagementPnl.add (Box.createRigidArea(new Dimension (0, 10)));
		testManagementPnl.add(previewTestBtn);
		testManagementPnl.add (Box.createRigidArea(new Dimension (0, 10)));
		testManagementPnl.add(correctTestBtn);
		
		add(insertTestBtn, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(testManagementPnl, BorderLayout.WEST);
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
			
		} else if (e.getSource() == deleteTestBtn) {
			
			int dialogResult = JOptionPane.showConfirmDialog(this,
					"Vuoi davvero eliminare le " + testsTable.getSelectedRowCount() +
					" verifiche selezionate?", "Sei sicuro?",
					JOptionPane.YES_NO_OPTION);
			
			if (dialogResult == JOptionPane.YES_OPTION) {
				testModel.deleteRows(testsTable.getSelectedRows());
			}
			
		} else if (e.getSource() == previewTestBtn) {
			
			int dialogResult = 0;
			
			int hasErrors = (int) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 5);
			if (hasErrors == 1) {
				String testName = (String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1);
				dialogResult = JOptionPane.showConfirmDialog(this,
						"La verifica '" + testName + "' non e' stata compilata\n"
						+ "corettamente. Vuoi procedere comunque?", "Sei sicuro?",
						JOptionPane.YES_NO_OPTION);
			}
			
			if (hasErrors == 0 || dialogResult == JOptionPane.YES_OPTION) {
				PdfTest pdfTest = new PdfTest(
						dbManager,
						(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0),
						(String)testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1));
				new TestPreview(pdfTest,dbManager);
			}
			
		} else if(e.getSource() == correctTestBtn) {
			new CorrectTest(
							dbManager,
							(UUID) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 0),
							(String) testsTable.getModel().getValueAt(testsTable.getSelectedRow(), 1));
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (testsTable.getSelectedRow() == -1 && testsTable.getRowCount() > 0) testsTable.setRowSelectionInterval(0, 0);
		
		deleteTestBtn.setText("Elimina verifiche (" + testsTable.getSelectedRowCount() + ")");
		deleteTestBtn.setEnabled(testsTable.getSelectedRowCount() > 0);
		editTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
		previewTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
		correctTestBtn.setEnabled(testsTable.getSelectedRowCount() == 1);
	}

	private void setSettingBtn(JButton btn) {
		btn.setMaximumSize(new Dimension(250, 35));
		btn.setEnabled(false);
		btn.addActionListener(this);
	}
}
