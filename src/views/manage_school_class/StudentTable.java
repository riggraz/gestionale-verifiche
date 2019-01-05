package views.manage_school_class;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler.ToolTipType;

import entities.SchoolClass;
import entities.Student;
import models.CorrectionTableModel;
import models.StudentModel;
import utils.DBManager;
import views.manage_school_class.student_forms.EditStudentForm;
import views.manage_school_class.student_forms.InsertStudentForm;
import views.manage_school_class.student_forms.MassAddStudentForm;

public class StudentTable extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	StudentModel studentModel;
	JComboBox<SchoolClass> schoolClassCmbBox;
	
	JScrollPane tableScrollPane;
	JTable table;
	
	JPanel studentManagementPnl;
	JButton insertStudentBtn;
	JButton massAddStudentBtn;
	JButton editStudentBtn;
	JButton deleteStudentBtn;
	
	JPanel studentInfoPnl;
	
	JLabel selectedStudentLbl;
	JLabel selectedStudentNOfTestsLbl;
	JLabel selectedStudentAvgLbl;
	
	CorrectionTableModel correctionTableModel;
	JScrollPane correctionsTableScrollPane;
	JTable correctionsTable;
	
	JPanel chartPnl;
	XYChart chart;
	
	public StudentTable(DBManager dbManager, JComboBox<SchoolClass> schoolClassCmbBox) {
		this.schoolClassCmbBox = schoolClassCmbBox;
		
		setLayout(new BorderLayout(24, 24));
		
		table = new JTable();
		studentModel = new StudentModel(dbManager);
		table.setModel(studentModel);
		table.getSelectionModel().addListSelectionListener(this);
		table.removeColumn(table.getColumnModel().getColumn(0)); // nasconde la prima colonna (id)
		table.setRowHeight(20);
		((DefaultTableCellRenderer)table.getDefaultRenderer(Double.class)).setHorizontalAlignment(SwingConstants.LEFT);
		
		tableScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		insertStudentBtn = new JButton("Nuovo studente");
		setButtonSettings(insertStudentBtn);
		
		massAddStudentBtn = new JButton("Aggiungi lista studenti");
		setButtonSettings(massAddStudentBtn);
		
		editStudentBtn = new JButton("Modifica studente");
		setButtonSettings(editStudentBtn);
		
		deleteStudentBtn = new JButton("Elimina studenti (0)");
		setButtonSettings(deleteStudentBtn);
		
		studentManagementPnl = new JPanel();
		studentManagementPnl.setLayout(new BoxLayout(studentManagementPnl, BoxLayout.Y_AXIS));
		studentManagementPnl.add(insertStudentBtn);
		studentManagementPnl.add(massAddStudentBtn);
		studentManagementPnl.add(editStudentBtn);
		studentManagementPnl.add(deleteStudentBtn);
		
		studentInfoPnl = new JPanel();
		studentInfoPnl.setLayout(new BoxLayout(studentInfoPnl, BoxLayout.Y_AXIS));
		
		selectedStudentLbl = new JLabel("Seleziona uno studente");
		setLabelSettings(selectedStudentLbl, 20);
		
		selectedStudentNOfTestsLbl = new JLabel("0 verifiche");
		setLabelSettings(selectedStudentNOfTestsLbl, 15);
		selectedStudentNOfTestsLbl.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));
		
		selectedStudentAvgLbl = new JLabel("Media: N/A");
		setLabelSettings(selectedStudentAvgLbl, 15);
		selectedStudentAvgLbl.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));
		
		correctionTableModel = new CorrectionTableModel(dbManager);
		correctionsTable = new JTable();
		correctionsTable.setModel(correctionTableModel);
		correctionsTable.getSelectionModel().addListSelectionListener(this);
		correctionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		((DefaultTableCellRenderer)correctionsTable.getDefaultRenderer(Double.class)).setHorizontalAlignment(SwingConstants.LEFT);
		correctionsTableScrollPane = new JScrollPane(correctionsTable);
		correctionsTableScrollPane.setPreferredSize(new Dimension(280, 280));
		
		studentInfoPnl.add(selectedStudentLbl);
		studentInfoPnl.add(selectedStudentNOfTestsLbl);
		studentInfoPnl.add(correctionsTableScrollPane);
		studentInfoPnl.add(selectedStudentAvgLbl);
		displayDefaultChart();
		
		add(tableScrollPane, BorderLayout.CENTER);
		add(studentManagementPnl, BorderLayout.WEST);
		add(studentInfoPnl, BorderLayout.EAST);
	}
	
	public void populateTable(String schoolClassName) {
		studentModel.loadBySchoolClassName(schoolClassName);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertStudentBtn) {
			new InsertStudentForm(
					this,
					studentModel,
					((SchoolClass)schoolClassCmbBox.getSelectedItem()).getName());
		}
		
		if (e.getSource() == massAddStudentBtn) {
			new MassAddStudentForm(
					this,
					studentModel,
					((SchoolClass)schoolClassCmbBox.getSelectedItem()).getName());
		}
		
		if (e.getSource() == editStudentBtn) {
			UUID id = (UUID) table.getModel().getValueAt(table.getSelectedRow(), 0);
			String firstName = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);
			String lastName = (String) table.getModel().getValueAt(table.getSelectedRow(), 2);
			String schoolClassName =  ((SchoolClass)schoolClassCmbBox.getSelectedItem()).getName();
			
			new EditStudentForm(
					this,
					studentModel,
					new Student(id, firstName, lastName, schoolClassName),
					schoolClassCmbBox);
		}
		
		if (e.getSource() == deleteStudentBtn) {
			int dialogResult = JOptionPane.showConfirmDialog(this,
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
		if (e.getSource() == table.getSelectionModel()) {
			deleteStudentBtn.setText("Elimina studenti (" + table.getSelectedRowCount() + ")");
			editStudentBtn.setEnabled(table.getSelectedRowCount() == 1);
			deleteStudentBtn.setEnabled(table.getSelectedRowCount() > 0);
			
			UUID selectedStudentId;
			String selectedStudentFirstName;
			String selectedStudentLastName;
			if (table.getSelectedRow() != -1) {
				selectedStudentId = (UUID) table.getModel().getValueAt(table.getSelectedRow(), 0);
				selectedStudentFirstName = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);
				selectedStudentLastName = (String) table.getModel().getValueAt(table.getSelectedRow(), 2);
				
				selectedStudentLbl.setText(selectedStudentFirstName + " " + selectedStudentLastName);
				selectedStudentAvgLbl.setText("Media: " + table.getModel().getValueAt(table.getSelectedRow(), 3));
				
				correctionTableModel.loadByStudentId(selectedStudentId);
				updateChart(selectedStudentId);
			} else {
				selectedStudentId = null;
				selectedStudentFirstName = null;
				selectedStudentLastName = null;
				
				selectedStudentLbl.setText("Seleziona uno studente");
				selectedStudentNOfTestsLbl.setText("0 verifiche");
				selectedStudentAvgLbl.setText("Media: N/A");
				
				correctionTableModel.getParentModel().clear();
				displayDefaultChart();
			}
		}
	}
	
	public void setEnabledOnInsertBtns(boolean enabled) {
		insertStudentBtn.setEnabled(enabled);
		massAddStudentBtn.setEnabled(enabled);
	}
	
	private void updateChart(UUID selectedStudentId) {
		double[] averageEvolution = correctionTableModel.getParentModel().getAverageEvolutionByStudentId(selectedStudentId);
		
		selectedStudentNOfTestsLbl.setText(averageEvolution.length + " verifiche");
		
		if (averageEvolution.length == 0) { displayDefaultChart(); return; }
		double[] xData = new double[averageEvolution.length];
		for (int i = 0; i < xData.length; i++) xData[i] = (double) (i+1); 
		
		if (chartPnl != null) studentInfoPnl.remove(chartPnl);
		
		chart = new XYChart(280, 280);
		setChartSettings(chart);
		chart.addSeries("andamento media", xData, averageEvolution);
		chartPnl = new XChartPanel<XYChart>(chart);
		
		studentInfoPnl.add(chartPnl);
		studentInfoPnl.revalidate();
	}
	
	private void displayDefaultChart() {
		if (chartPnl != null) studentInfoPnl.remove(chartPnl);
		chart = new XYChart(280, 280);
		double[] voidArr = { 0.0 };
		chart.addSeries("andamento media", voidArr, voidArr);
		setChartSettings(chart);
		chartPnl = new XChartPanel<XYChart>(chart);
		studentInfoPnl.add(chartPnl);
		studentInfoPnl.revalidate();
	}
	
	private void setChartSettings(XYChart c) {
		c.setTitle("Andamento media");
		c.setXAxisTitle("Verifiche");
		c.setYAxisTitle("Media");
		c.getStyler().setToolTipsEnabled(true);
		c.getStyler().setToolTipType(ToolTipType.yLabels);
		c.getStyler().setYAxisMin(0.0);
		c.getStyler().setYAxisMax(10.0);
		c.getStyler().setLegendVisible(false);
	}
	
	private void setButtonSettings(JButton b) {
		b.setEnabled(false);
		b.setMaximumSize(new Dimension(300, 35));
		b.addActionListener(this);
	}
	
	private void setLabelSettings(JLabel l, int fontSize) {
		l.setAlignmentX(CENTER_ALIGNMENT);
		l.setFont(new Font(new JLabel().getFont().getFamily(), Font.PLAIN, fontSize));
	}

}
