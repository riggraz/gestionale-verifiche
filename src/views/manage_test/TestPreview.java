package views.manage_test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.SchoolClass;
import models.SchoolClassListModel;
import utils.DBManager;
import utils.print.ImagePanel;
import utils.print.PdfTest;

public class TestPreview extends JFrame implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private PdfTest pdfTest;
	private List<ImagePanel> testPages;
	private int copiesToPrint;
	
	private SchoolClassListModel schoolClassListModel;
	private JPanel schoolClassPnl;
	private JLabel schoolClassLbl;
	private JList<SchoolClass> schoolClassList;
	private JScrollPane schoolClassScrollPane;
	private JLabel copiesToPrintLbl;
	
	private JPanel previewPnl;
	
	private JLabel nOfPagesLbl;
	
	private JPanel savePrintPnl;
	private JButton saveBtn;
	private JCheckBox openAfterSaveCheckBox;
	private JButton printBtn;
	
	public TestPreview(PdfTest pdfTest, DBManager dbManager) {
		super("Anteprima verifica");
		
		this.pdfTest = pdfTest;
		this.copiesToPrint = 0;
		
		setLayout(new BorderLayout(24, 24));
		getRootPane().setBorder(new EmptyBorder(16, 16, 16, 16));
		
		testPages = new ArrayList<ImagePanel>();
		
		previewPnl = new JPanel();
		previewPnl.setLayout(new BoxLayout(previewPnl, BoxLayout.Y_AXIS));
		
		savePrintPnl = new JPanel();
		savePrintPnl.setLayout(new BoxLayout(savePrintPnl, BoxLayout.Y_AXIS));
		
		nOfPagesLbl = new JLabel(pdfTest.getPreviewImages().size() + " pagine");
		nOfPagesLbl.setFont(new Font(new JLabel().getFont().getFamily(), Font.PLAIN, 18));
		
		saveBtn = new JButton("Salva con nome");
		saveBtn.setMaximumSize(new Dimension(250, 35));
		saveBtn.addActionListener(this);
		
		openAfterSaveCheckBox = new JCheckBox("Apri verifica dopo salvataggio");
		
		printBtn = new JButton("Stampa");
		printBtn.setMaximumSize(new Dimension(250, 35));
		printBtn.addActionListener(this);
		
		schoolClassListModel = new SchoolClassListModel(dbManager);
		
		schoolClassPnl = new JPanel();
		schoolClassPnl.setLayout(new BoxLayout(schoolClassPnl, BoxLayout.Y_AXIS));
		schoolClassPnl.setMaximumSize(new Dimension(250, 150));
		schoolClassPnl.setAlignmentX(SwingConstants.CENTER);
		
		schoolClassLbl = new JLabel("Per quali classi:");
		schoolClassLbl.setAlignmentX(CENTER_ALIGNMENT);
		schoolClassList = new JList<SchoolClass>();
		schoolClassList.setAlignmentX(CENTER_ALIGNMENT);
		schoolClassList.setModel(schoolClassListModel);
		schoolClassList.addListSelectionListener(this);
		
		schoolClassScrollPane = new JScrollPane();
		schoolClassScrollPane.setViewportView(schoolClassList);
		
		copiesToPrintLbl = new JLabel("0 copie");
		copiesToPrintLbl.setAlignmentX(CENTER_ALIGNMENT);
		
		schoolClassPnl.add(schoolClassLbl);
		schoolClassPnl.add(schoolClassScrollPane);
		schoolClassPnl.add(copiesToPrintLbl);
		
		savePrintPnl.add(saveBtn);
		savePrintPnl.add(openAfterSaveCheckBox);
		savePrintPnl.add(Box.createRigidArea(new Dimension (0, 20)));
		savePrintPnl.add(printBtn);
		savePrintPnl.add(schoolClassPnl);
		
		JScrollPane previewScrollPane = new JScrollPane(previewPnl);
		previewScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		for (String imagePath : pdfTest.getPreviewImages()) {
			ImagePanel imagePanel = new ImagePanel(imagePath);
			testPages.add(imagePanel);
		}
		
		for (ImagePanel imagePanel : testPages) {
			JLabel image = new JLabel(new ImageIcon(imagePanel.getBufferedImage()));
			image.setAlignmentX(CENTER_ALIGNMENT);
			previewPnl.add(image);
		}
		
		add(previewScrollPane, BorderLayout.CENTER);
		add(savePrintPnl, BorderLayout.EAST);
		add(nOfPagesLbl, BorderLayout.SOUTH);
		
		setSize(840, 560);
		WindowListener exitListener = new WindowAdapter() {			
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	pdfTest.close();	
		    	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    }
		};		
		this.addWindowListener(exitListener);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveBtn) {
			pdfTest.save(openAfterSaveCheckBox.isSelected());
		} else if (e.getSource() == printBtn) {
			pdfTest.print(copiesToPrint);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int copiesToPrint = 0;
		for (SchoolClass selectedSchoolClass : schoolClassList.getSelectedValuesList()) {
			copiesToPrint += schoolClassListModel.getStudentCountBySchoolClassName(selectedSchoolClass.getName());
		}
		copiesToPrintLbl.setText(copiesToPrint + " copie");
		this.copiesToPrint = copiesToPrint;
	}

}
