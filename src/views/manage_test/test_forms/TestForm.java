package views.manage_test.test_forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entities.Question;
import models.QuestionModel;
import models.TestModel;

public abstract class TestForm extends JFrame implements DocumentListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private TestModel testModel;
	private UUID testId;
	
	private QuestionModel questionModel;
	private List<Question> questionsList;
	
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTxt;
	private JLabel descriptionLbl;
	private JTextField descriptionTxt;
	
	private JScrollPane qAndAScrollPane;
	private JPanel qAndAMainPnl;
	
	private List<JPanel> qAndAPnls;
	
	private List<JPanel> qInfoPnls;
	private List<JLabel> qNumberLbls;
	private List<JButton> qDeleteBtns; 
	
	private List<JTextArea> qBodyTxts;
	
	private JButton addQuestionBtn;
	
	private JPanel bottomPnl;
	private JLabel errorsCountLbl;

	public TestForm(String frameName, TestModel testModel) {
		super(frameName);
		this.testModel = testModel;
		
//		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		
		infoPnl = new JPanel(new GridLayout(2, 2));
		infoPnl.setMaximumSize(new Dimension(600, 100));
		infoPnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
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
		qAndAScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		qAndAPnls = new ArrayList<JPanel>();
		
		qInfoPnls = new ArrayList<JPanel>();
		qNumberLbls = new ArrayList<JLabel>();
		qDeleteBtns = new ArrayList<JButton>();
		
		qBodyTxts = new ArrayList<JTextArea>();
		
		addQuestionBtn = new JButton("Nuova domanda");
		addQuestionBtn.setFocusable(false);
		addQuestionBtn.setAlignmentX(CENTER_ALIGNMENT);
		addQuestionBtn.addActionListener(this);
		qAndAMainPnl.add(addQuestionBtn);
		
		bottomPnl = new JPanel();
		bottomPnl.setMaximumSize(new Dimension(600, 50));
		bottomPnl.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		errorsCountLbl = new JLabel("0 errori");
		errorsCountLbl.setFont(new Font("Serif", Font.PLAIN, 24));
		bottomPnl.add(errorsCountLbl);
		
		nameTxt.getDocument().addDocumentListener(this);
		descriptionTxt.getDocument().addDocumentListener(this);
		
		add(infoPnl, BorderLayout.NORTH);
		add(qAndAScrollPane, BorderLayout.CENTER);
		add(bottomPnl, BorderLayout.SOUTH);
		
		updateErrorsCountLbl();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 800);
        setVisible(true);
	}
	
	protected void addQAndA(Question question) {
		JPanel qAndAPnl = new JPanel();
		qAndAPnl.setLayout(new BoxLayout(qAndAPnl, BoxLayout.Y_AXIS));
		qAndAPnl.setMaximumSize(new Dimension(600, 200));
		qAndAPnl.setPreferredSize(new Dimension(500, 200));
		qAndAPnl.setMinimumSize(new Dimension(400, 200));
		qAndAPnl.setAlignmentX(Component.CENTER_ALIGNMENT);
		qAndAPnl.setBorder(new CompoundBorder(new EmptyBorder(24, 8, 24, 8), null));
		
		// informations about Question
		JPanel qInfoPnl = new JPanel();
		JLabel qNumberLbl = new JLabel("Domanda " + (question.getNumber() + 1));
		qNumberLbl.setFont(new Font("Serif", Font.PLAIN, 18));
		JButton qDeleteBtn = new JButton("elimina");
		qDeleteBtn.setFocusable(false);
		
		final JTextArea qBodyTxt = new JTextArea(question.getBody());
		
		qDeleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete from db
				questionModel.deleteItem(question.getId());
				
				// delete from memory
				qAndAMainPnl.remove(qAndAPnl);
				qInfoPnls.remove(qInfoPnl);
				qNumberLbls.remove(qNumberLbl);
				qDeleteBtns.remove(qDeleteBtn);
				qBodyTxts.remove(qBodyTxt);
				
				// update view
				updateQNumberLbls();
				updateErrorsCountLbl();
				qAndAMainPnl.revalidate();
				qAndAMainPnl.repaint();
			}
		});
		qInfoPnl.add(qNumberLbl);
		qInfoPnl.add(qDeleteBtn);
		
		// body of Question
		JScrollPane qBodyScrollPane = new JScrollPane(qBodyTxt);
		qBodyScrollPane.removeMouseWheelListener(qBodyScrollPane.getMouseWheelListeners()[0]);
		qBodyScrollPane.setPreferredSize(new Dimension(350, 60));
		qBodyTxt.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				questionModel.updateBody(question.getId(), qBodyTxt.getText());
				updateErrorsCountLbl();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) { insertUpdate(e); }
			
			@Override
			public void changedUpdate(DocumentEvent e) { insertUpdate(e); }
		});
		qBodyTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                	qBodyTxt.transferFocus();
                    e.consume();
                }
            }
        });
		
		qAndAPnl.add(qInfoPnl);
		qAndAPnl.add(qBodyScrollPane);
		
		// add to Lists created components
		qInfoPnls.add(qInfoPnl);
		qNumberLbls.add(qNumberLbl);
		qDeleteBtns.add(qDeleteBtn);
		qBodyTxts.add(qBodyTxt);
		
		qAndAPnls.add(qAndAPnl); //aggiunge il nuovo pannello alla lista (non mostra niente)
		qAndAMainPnl.add(qAndAPnl); //aggiunge il nuovo pannello alla vista
		
		qAndAMainPnl.remove(addQuestionBtn);
		qAndAMainPnl.add(addQuestionBtn);
		
		updateErrorsCountLbl();
		
		// repaint
		qAndAMainPnl.revalidate();
		qAndAMainPnl.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addQuestionBtn) {
			questionModel.insertItem("", testId);
			
			Question newQuestion = questionsList.get(questionsList.size() - 1);
			addQAndA(
				new Question(newQuestion.getId(),
						newQuestion.getNumber(),
						newQuestion.getBody(),
						newQuestion.getTestId())
			);
			qBodyTxts.get(questionsList.size()-1).grabFocus();
			qAndAScrollPane.validate();
			qAndAScrollPane.getVerticalScrollBar().setValue(qAndAScrollPane.getVerticalScrollBar().getMaximum());
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (e.getDocument() == nameTxt.getDocument()) {
			testModel.updateName(testId, nameTxt.getText());
		} else if (e.getDocument() == descriptionTxt.getDocument()) {
			testModel.updateDescription(testId, descriptionTxt.getText());
		}
		updateErrorsCountLbl();
	}

	@Override
	public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

	@Override
	public void changedUpdate(DocumentEvent e) { insertUpdate(e); }

	public void setTestId(UUID testId) {
		this.testId = testId;
	}
	
	public void setTestName(String testName) {
		nameTxt.setText(testName);
	}
	
	public void setTestDescription(String testDescription) {
		descriptionTxt.setText(testDescription);
	}
	
	public void setQuestionModel(QuestionModel questionModel) {
		this.questionModel = questionModel;
		this.questionsList = questionModel.getQuestions();
	}
	
	private void updateQNumberLbls() {
		for (int i = 0; i < questionsList.size(); i++) {
			qNumberLbls.get(i).setText("Domanda " + (i+1));
		}
	}
	
	private void updateErrorsCountLbl() {
		int count = 0;
		
		if (nameTxt.getText().equals("")) {
			count++;
			nameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		} else {
			nameTxt.setBorder(new JTextField().getBorder());
		}
		
		if (descriptionTxt.getText().equals("")) {
			count++;
			descriptionTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		} else {
			descriptionTxt.setBorder(new JTextField().getBorder());
		}
		
		for (int i = 0; i < qBodyTxts.size(); i++) {
			if (qBodyTxts.get(i).getText().equals("")) {
				count++;
				qBodyTxts.get(i).setBorder(new LineBorder(Color.RED, 2));
			} else {
				qBodyTxts.get(i).setBorder(new JTextArea().getBorder());
			}
		}
		
		errorsCountLbl.setText(Integer.toString(count) + " errori");
		
		if (count == 0) errorsCountLbl.setForeground(Color.BLUE);
		else errorsCountLbl.setForeground(Color.RED);
	}

}
