package views.manage_test.test_forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entities.Answer;
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
	private JPanel namePnl;
	private JLabel nameLbl;
	private JTextField nameTxt;
	private JPanel descriptionPnl;
	private JLabel descriptionLbl;
	private JTextField descriptionTxt;
	
	private JScrollPane qAndAScrollPane;
	private JPanel qAndAMainPnl;
	
	private List<JPanel> qAndAPnls;
	
	private List<JPanel> qInfoPnls;
	private List<JLabel> qNumberLbls;
	private List<JButton> qDeleteBtns; 
	
	private List<JTextArea> qBodyTxts;
	
	private List<JPanel[]> aPnls;
	private List<JTextField[]> aTxts;
	private List<ButtonGroup> correctABtnGrp;
	private List<JRadioButton[]> correctABtns;
	
	private JButton addQuestionBtn;
	
	private JPanel bottomPnl;
	private JLabel errorsCountLbl;

	public TestForm(String frameName, TestModel testModel) {
		super(frameName);
		this.testModel = testModel;
		
//		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		
		infoPnl = new JPanel();
		infoPnl.setLayout(new BoxLayout(infoPnl, BoxLayout.Y_AXIS));
		infoPnl.setMaximumSize(new Dimension(600, 100));
		infoPnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		namePnl = new JPanel();
		namePnl.setLayout(new BoxLayout(namePnl, BoxLayout.X_AXIS));
		nameLbl = new JLabel("Nome:");
		nameLbl.setFont(new Font("Serif", Font.PLAIN, 18));
		nameTxt = new JTextField();
		nameTxt.setFont(new Font("Serif", Font.PLAIN, 24));
		descriptionPnl = new JPanel();
		descriptionPnl.setLayout(new BoxLayout(descriptionPnl, BoxLayout.X_AXIS));
		descriptionLbl = new JLabel("Descrizione:");
		descriptionLbl.setFont(new Font("Serif", Font.PLAIN, 18));
		descriptionTxt = new JTextField();
		namePnl.add(nameLbl);
		namePnl.add(nameTxt);
		descriptionPnl.add(descriptionLbl);
		descriptionPnl.add(descriptionTxt);
		infoPnl.add(namePnl);
		infoPnl.add(descriptionPnl);
		
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
		
		aPnls = new ArrayList<JPanel[]>();
		aTxts = new ArrayList<JTextField[]>();
		correctABtnGrp = new ArrayList<ButtonGroup>();
		correctABtns = new ArrayList<JRadioButton[]>();
		
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
        setSize(720, 800);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addQuestionBtn) {
			questionModel.insertItem("", testId);
			
			Question newQuestion = questionsList.get(questionsList.size() - 1);
			addQAndA(newQuestion);
			
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
	
	protected void addQAndA(Question question) {
		JPanel qAndAPnl = new JPanel();
		
		JPanel qInfoPnl = new JPanel();
		JLabel qNumberLbl = new JLabel("Domanda " + (question.getNumber() + 1));
		JButton qDeleteBtn = new JButton("elimina");
		
		final JTextArea qBodyTxt = new JTextArea(question.getBody());
		JScrollPane qBodyScrollPane = new JScrollPane(qBodyTxt);
		
		JPanel[] aPnls = new JPanel[4];
		ButtonGroup correctAnswersGroup = new ButtonGroup();
		JRadioButton[] correctAnswers = new JRadioButton[4];
		JTextField[] answers = new JTextField[4];
		
		qAndAPnl.setLayout(new BoxLayout(qAndAPnl, BoxLayout.Y_AXIS));
		qAndAPnl.setMaximumSize(new Dimension(800, 300));
		qAndAPnl.setPreferredSize(new Dimension(700, 300));
		qAndAPnl.setMinimumSize(new Dimension(600, 300));
		qAndAPnl.setAlignmentX(Component.CENTER_ALIGNMENT);
		qAndAPnl.setBorder(new CompoundBorder(new EmptyBorder(24, 8, 24, 8), null));
		
		// informations about Question
		qNumberLbl.setFont(new Font("Serif", Font.PLAIN, 18));
		
		qDeleteBtn.setFocusable(false);
		final List<JPanel[]> aPnlsRef = this.aPnls;
		qDeleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete from db
				questionModel.deleteItem(question.getId());
				testModel.updateUpdatedAt(testId);
				
				// delete from memory
				qAndAMainPnl.remove(qAndAPnl);
				qInfoPnls.remove(qInfoPnl);
				qNumberLbls.remove(qNumberLbl);
				qDeleteBtns.remove(qDeleteBtn);
				qBodyTxts.remove(qBodyTxt);
				aPnlsRef.remove(aPnls);
				aTxts.remove(answers);
				correctABtnGrp.remove(correctAnswersGroup);
				correctABtns.remove(correctAnswers);
				
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
		qBodyScrollPane.removeMouseWheelListener(qBodyScrollPane.getMouseWheelListeners()[0]);
		qBodyScrollPane.setPreferredSize(new Dimension(350, 60));
		qBodyTxt.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				questionModel.updateBody(question.getId(), qBodyTxt.getText());
				testModel.updateUpdatedAt(testId);
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
		
		for (int i = 0; i < 4; i++) {
			aPnls[i] = new JPanel();
			aPnls[i].setLayout(new BoxLayout(aPnls[i], BoxLayout.X_AXIS));
			
			correctAnswers[i] = new JRadioButton("", question.getCorrectAnswer() == i);
			correctAnswers[i].setFocusable(false);
			final int j = i;
			correctAnswers[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					questionModel.updateCorrectAnswer(question.getId(), j);
					updateErrorsCountLbl();
				}
			});
			correctAnswersGroup.add(correctAnswers[i]);
			
			final Answer answer = question.getAnswers().get(i);
			answers[i] = new JTextField(answer.getBody());
			final JTextField currentAnswerTxt = answers[i];
			answers[i].getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					questionModel.updateAnswerBody(answer.getId(), currentAnswerTxt.getText());
					updateErrorsCountLbl();
				}	
				@Override
				public void removeUpdate(DocumentEvent e) {	insertUpdate(e); }
				@Override
				public void changedUpdate(DocumentEvent e) { insertUpdate(e); }
			});
			
			aPnls[i].add(correctAnswers[i]);
			aPnls[i].add(answers[i]);
			
			qAndAPnl.add(aPnls[i]);
		}
		
		// add to Lists created components
		qInfoPnls.add(qInfoPnl);
		qNumberLbls.add(qNumberLbl);
		qDeleteBtns.add(qDeleteBtn);
		qBodyTxts.add(qBodyTxt);
		correctABtnGrp.add(correctAnswersGroup);
		correctABtns.add(correctAnswers);
		this.aPnls.add(aPnls);
		aTxts.add(answers);
		
		qAndAPnls.add(qAndAPnl); //aggiunge il nuovo pannello alla lista (non mostra niente)
		qAndAMainPnl.add(qAndAPnl); //aggiunge il nuovo pannello alla vista
		
		qAndAMainPnl.remove(addQuestionBtn);
		qAndAMainPnl.add(addQuestionBtn);
		
		updateErrorsCountLbl();
		
		// repaint
		qAndAMainPnl.revalidate();
		qAndAMainPnl.repaint();
	}
	
	private void updateQNumberLbls() {
		for (int i = 0; i < questionsList.size(); i++) {
			qNumberLbls.get(i).setText("Domanda " + (i+1));
		}
	}
	
	private void updateErrorsCountLbl() {
		int count = 0;
		final int BORDER_WIDTH = 1;
		
		// test name
		if (nameTxt.getText().equals("")) {
			count++;
			nameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_WIDTH));
		} else {
			nameTxt.setBorder(new JTextField().getBorder());
		}
		// test description
		if (descriptionTxt.getText().equals("")) {
			count++;
			descriptionTxt.setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_WIDTH));
		} else {
			descriptionTxt.setBorder(new JTextField().getBorder());
		}
		
		for (int i = 0; i < qBodyTxts.size(); i++) {
			// question body
			if (qBodyTxts.get(i).getText().equals("")) {
				count++;
				qBodyTxts.get(i).setBorder(new LineBorder(Color.RED, BORDER_WIDTH));
			} else {
				qBodyTxts.get(i).setBorder(new JTextArea().getBorder());
			}
			
			// answers body
			for (int j = 0; j < 4; j++) {
				if (aTxts.get(i)[j].getText().equals("")) {
					count++;
					aTxts.get(i)[j].setBorder(new LineBorder(Color.RED, BORDER_WIDTH));
				} else {
					aTxts.get(i)[j].setBorder(new JTextField().getBorder());
				}
			}
			
			// correct answer
			if (correctABtnGrp.get(i).getSelection() == null) {
				count++;
				for (int j = 0; j < 4; j++) { correctABtns.get(i)[j].setBackground(Color.RED); correctABtns.get(i)[j].setOpaque(true); }
			} else {
				for (int j = 0; j < 4; j++) { correctABtns.get(i)[j].setBorder(new JRadioButton().getBorder()); correctABtns.get(i)[j].setOpaque(false); }
			}
		}
		
		qAndAMainPnl.revalidate();
		qAndAMainPnl.repaint();
		
		errorsCountLbl.setText(Integer.toString(count) + " errori");
		
		if (count == 0) errorsCountLbl.setForeground(Color.BLUE);
		else errorsCountLbl.setForeground(Color.RED);
	}

}
