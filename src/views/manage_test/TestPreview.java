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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import utils.print.ImagePanel;
import utils.print.PdfTest;

public class TestPreview extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private PdfTest pdfTest;
	
	private List<ImagePanel> testPages;
	
	private JPanel previewPnl;
	private JLabel nOfPagesLbl;
	private JPanel savePrintPnl;
	private JButton saveBtn;
	private JButton printBtn;
	
	public TestPreview(PdfTest pdfTest) {
		super("Anteprima verifica");
		
		this.pdfTest = pdfTest;
		
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
		
		printBtn = new JButton("Stampa");
		printBtn.setMaximumSize(new Dimension(250, 35));
		printBtn.addActionListener(this);
		
		savePrintPnl.add(saveBtn);
		savePrintPnl.add(printBtn);
		
		JScrollPane previewScrollPane = new JScrollPane(previewPnl);
		previewScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		for (String imagePath : pdfTest.getPreviewImages()) {
			ImagePanel imagePanel = new ImagePanel(imagePath);
			testPages.add(imagePanel);
		}
		
		for (ImagePanel imagePanel : testPages) {
			JLabel icon = new JLabel(new ImageIcon(imagePanel.getBufferedImage()));
			icon.setAlignmentX(CENTER_ALIGNMENT);
			previewPnl.add(icon);
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
			pdfTest.save();
		} else if (e.getSource() == printBtn) {
			pdfTest.print();
		}
	}

}
