package views.manage_test.test_forms;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import models.QuestionModel;
import models.TestModel;

public abstract class TestForm extends JFrame implements DocumentListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private TestModel testModel;
	private UUID testId;
	
	private QuestionModel questionModel;
	
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTxt;
	private JLabel descriptionLbl;
	private JTextField descriptionTxt;
	
	private JScrollPane qAndAScrollPane;
	private JPanel qAndAMainPnl;
	private JButton addQuestionBtn;

	public TestForm(String frameName, TestModel testModel, QuestionModel questionModel) {
		super(frameName);
		this.testModel = testModel;
		this.questionModel = questionModel;
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		infoPnl = new JPanel(new GridLayout(2, 2));
		infoPnl.setMaximumSize(new Dimension(600, 100));
		nameLbl = new JLabel("Nome:");
		nameTxt = new JTextField();
		descriptionLbl = new JLabel("Descrizione:");
		descriptionTxt = new JTextField();
		infoPnl.add(nameLbl);
		infoPnl.add(nameTxt);
		infoPnl.add(descriptionLbl);
		infoPnl.add(descriptionTxt);
		
		qAndAMainPnl = new JPanel();
		qAndAMainPnl.setLayout(new BoxLayout(qAndAMainPnl, BoxLayout.Y_AXIS));
		qAndAScrollPane = new JScrollPane(qAndAMainPnl);
		qAndAScrollPane.setBorder(BorderFactory.createEmptyBorder());
		qAndAScrollPane.setMaximumSize(new Dimension(1080, 680));
		
		addQuestionBtn = new JButton("Nuova domanda");
		addQuestionBtn.addActionListener(this);
		qAndAMainPnl.add(addQuestionBtn);
		
		nameTxt.getDocument().addDocumentListener(this);
		descriptionTxt.getDocument().addDocumentListener(this);
		
		add(infoPnl);
		add(qAndAMainPnl);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 640);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addQuestionBtn) {
			questionModel.insertItem("ciaoooo", testId);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (e.getDocument() == nameTxt.getDocument()) {
			testModel.updateName(testId, nameTxt.getText());
		} else if (e.getDocument() == descriptionTxt.getDocument()) {
			testModel.updateDescription(testId, descriptionTxt.getText());
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

	@Override
	public void changedUpdate(DocumentEvent e) { insertUpdate(e); }


	public void setTestId(UUID testId) {
		this.testId = testId;
	}

}
