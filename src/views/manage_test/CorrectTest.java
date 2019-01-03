package views.manage_test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import models.CorrectionModel;
import models.QuestionModel;
import models.SchoolClassComboBoxModel;
import models.StudentModel;
import utils.DBManager;
import entities.Correction;
import entities.Question;
import entities.SchoolClass;
import entities.Student;

public class CorrectTest extends JFrame  implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPnl;
	private JPanel correctAnswerPnl;
	private QuestionModel questionModel;
	private List<Question> questionList;
	private JLabel nameTestLbl;
	private JLabel correctAnswerLbl;
	private JLabel stringCorrectAnswer;
	private JPanel upgrateButtonPnl;
	private JPanel classPnl;
	private JPanel votePnl;
	private List<JPanel> singlePersonListPnl;
	private JPanel singlePersonPnl;
	private JPanel personPnl;
	private JPanel numUpgrateDb;
	private JComboBox<SchoolClass> schoolClassCmbBox;
	private List<JLabel> nameList;
	private List<JTextField> voteListTxf;
	private List<UUID> uuidStudentList;
	private JButton upgrateBtn;
	

	private SchoolClassComboBoxModel schoolClassComboBoxModel; 
	private CorrectionModel correctionModel;
	private StudentModel studentModel;
	
	private List<Correction> correctionList;
	private List<Student> studentList;
	
	private UUID idTest;
	
	private Border errorBorder;
	private int numVoteUpdate;
	
	public CorrectTest(DBManager dbManager, UUID idTest, String nameTest)  {
		super("Correzione Verifica");
		this.idTest= idTest;
		mainPnl = new JPanel();
		
		mainPnl.setLayout(new BoxLayout(mainPnl,BoxLayout.PAGE_AXIS));
		getRootPane().setBorder(new EmptyBorder(16, 16, 16, 16));
		
		correctAnswerPnl = new JPanel();
		correctAnswerPnl.setBorder(new EmptyBorder(16,16,16,16));
		
		nameTestLbl = new JLabel(nameTest);
		nameTestLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameTestLbl.setFont(new Font(new JLabel().getFont().getFamily(), Font.BOLD, 14));
		
		stringCorrectAnswer = new JLabel("Stringa risposte corrette: ");
		stringCorrectAnswer.setFont(new Font(new JLabel().getFont().getFamily(), Font.PLAIN, 15));
		correctAnswerLbl = new JLabel("");
		correctAnswerLbl.setFont(new Font(new JLabel().getFont().getFamily(), Font.PLAIN, 15));
		questionModel = new QuestionModel(dbManager);
		questionModel.loadByTestId(idTest);
		questionList = questionModel.getQuestions();
		
		findCorrectAnswer();
		
	
		correctAnswerPnl.add(stringCorrectAnswer);
		correctAnswerPnl.add(correctAnswerLbl);	
		correctAnswerPnl.setMaximumSize(new Dimension(1000,50));
		
		
		schoolClassCmbBox = new JComboBox<SchoolClass>();
		schoolClassComboBoxModel = new SchoolClassComboBoxModel(dbManager);
		schoolClassCmbBox.setModel(schoolClassComboBoxModel);

		
		correctionModel = new CorrectionModel(dbManager);
		studentModel = new StudentModel(dbManager);
		
		correctionList = new ArrayList<Correction>();

		
		if (schoolClassCmbBox.getItemCount() > 0) {
			schoolClassCmbBox.setSelectedIndex(0);
			loadCorrectionStudentList(schoolClassCmbBox.getItemAt(0));
			if(correctionList.isEmpty()) loadCorrectionListEmpty(schoolClassCmbBox.getItemAt(0));			
		}
		
		classPnl = new JPanel(new GridLayout(1,2));
		JLabel stringSelClass = new JLabel("Selezione la classe: ");
		stringSelClass.setFont(new Font(new JLabel().getFont().getFamily(), Font.PLAIN, 15));
		stringSelClass.setBorder(new EmptyBorder(0,20,0,0));
		
		classPnl.add(stringSelClass);
		classPnl.add(schoolClassCmbBox);
		classPnl.setMaximumSize(new Dimension(550,30));
		
		votePnl = new JPanel();
		votePnl.setLayout(new BoxLayout(votePnl,BoxLayout.PAGE_AXIS));
		votePnl.setMaximumSize(new Dimension(800,100));
		addStudentVoteAtPanel();	

		JScrollPane voteScrollPane = new JScrollPane(votePnl);
		voteScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		setBorders();
		upgrateBtn = new JButton("Correggi");
		upgrateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		upgrateButtonPnl = new JPanel();
		upgrateButtonPnl.add(upgrateBtn);
		upgrateButtonPnl.setMaximumSize(new Dimension(1000,100));
		
		
		schoolClassCmbBox.addItemListener(this);
		upgrateBtn.addActionListener(this);
		
		numUpgrateDb = new JPanel();
		numUpgrateDb.add(new JLabel(""));
		numUpgrateDb.setMaximumSize(new Dimension(500,100));
		
		mainPnl.add(nameTestLbl);
		mainPnl.add(Box.createRigidArea(new Dimension(30,15)));
		mainPnl.add(correctAnswerPnl);
		mainPnl.add(Box.createRigidArea(new Dimension(40,40)));
		mainPnl.add(classPnl);
		mainPnl.add(Box.createRigidArea(new Dimension(30,30)));
		mainPnl.add(voteScrollPane);
		mainPnl.add(upgrateButtonPnl);
		mainPnl.add(numUpgrateDb);
		this.add(mainPnl);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(550, 650);
		setVisible(true);
	}

	private void findCorrectAnswer() {
		for(int i =0; i<questionList.size();i++) {
			if(questionList.get(i).getCorrectAnswer() == 0) {
				correctAnswerLbl.setText(correctAnswerLbl.getText() + " A");
			}
			if(questionList.get(i).getCorrectAnswer() == 1) {
				correctAnswerLbl.setText(correctAnswerLbl.getText() + " B");
			}
			if(questionList.get(i).getCorrectAnswer() == 2) {
				correctAnswerLbl.setText(correctAnswerLbl.getText() + " C");
			}
			if(questionList.get(i).getCorrectAnswer() == 3) {
				correctAnswerLbl.setText(correctAnswerLbl.getText() + " D");
			}
			if(questionList.get(i).getCorrectAnswer() == -1) {
				correctAnswerLbl.setText(correctAnswerLbl.getText() + " ?");
			}
		}
	}
	
	private void loadCorrectionListEmpty(SchoolClass schoolClass) {
		
			correctionModel.insertItem(idTest, studentList);
			correctionList = correctionModel.loadByIdTestAndClass(schoolClass.getName(),idTest);
	}
	
	private void addStudentVoteAtPanel() {
		nameList = new ArrayList<JLabel>();
		voteListTxf = new ArrayList<JTextField>();
		uuidStudentList = new ArrayList<UUID>();
		singlePersonListPnl = new ArrayList<JPanel>();
		
		for(Student s : studentList) {
			Correction c = correctionModel.returnCorrectionByUUIDStudent(s.getId());
			uuidStudentList.add(s.getId());
			nameList.add(new JLabel(s.getLastName() + " " + s.getFirstName()));
			if(c.getVote() == -1.0) voteListTxf.add(new JTextField(""));
			else voteListTxf.add(new JTextField(Double.toString(c.getVote())));

		}
		
		for(int i =0;i<nameList.size();i++) {
			singlePersonPnl = new JPanel();
			singlePersonPnl.setLayout(new BoxLayout(singlePersonPnl,BoxLayout.LINE_AXIS));
			singlePersonPnl.setMaximumSize(new Dimension(400,40));
			
			personPnl = new JPanel(new GridLayout(2,2));
			

			personPnl.add(nameList.get(i));
			personPnl.add(voteListTxf.get(i));
			personPnl.add(new JLabel(""));
			singlePersonPnl.add(personPnl);
			singlePersonListPnl.add(singlePersonPnl);
		}
		
		for(int i = 0;i<nameList.size();i++) {
			votePnl.add(singlePersonListPnl.get(i));
		}
	}
	
	private void loadCorrectionStudentList(SchoolClass s) {
		studentModel.loadBySchoolClassName(s.getName());
		studentList = studentModel.getListStudent();	
		Collections.sort(studentList);
		correctionList = correctionModel.loadByIdTestAndClass(s.getName(),idTest);
		
		if(!correctionList.isEmpty()) {
			List<Student> newStudentList = new ArrayList<Student>();
			for(Student stud : studentList) {
				int findStudent =0;
				for(Correction c : correctionList) {
					if(stud.getId().equals(c.getIdStudent())) findStudent=1;
				}
				if(findStudent ==0) {
					newStudentList.add(stud);
				}
			}
				if(newStudentList.size() !=0) {
					correctionModel.insertItem(idTest,newStudentList);
					Collections.sort(studentList);
					repaintStudent();
				}
		}
	}
	
	private void repaintStudent() {
		
		votePnl.removeAll();	
		addStudentVoteAtPanel();
		votePnl.revalidate();
		votePnl.repaint();
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == schoolClassCmbBox) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				loadCorrectionStudentList(((SchoolClass)e.getItem()));
				if(correctionList.isEmpty()) loadCorrectionListEmpty(((SchoolClass)e.getItem()));
				
				repaintStudent();
				numUpgrateDb.removeAll();
				numUpgrateDb.add(new JLabel(""));
				numUpgrateDb.revalidate();
				numUpgrateDb.repaint();
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JLabel nUpgradeDbVote = new JLabel("");
		numVoteUpdate=0;

		if(e.getSource() == upgrateBtn) {
			

			
			if(checkCorrectVote()) {
				Thread t = new Thread(new Runnable() {
					public void run() {
		
						numUpgrateDb.removeAll();
						numUpgrateDb.add(new JLabel("Attendere.. Stiamo inserendo i nuovi valori"));
						numUpgrateDb.revalidate();
						numUpgrateDb.repaint();
			
						
						for(int i =0;i<uuidStudentList.size();i++) {
							for(Correction c : correctionList) {
						
								if(uuidStudentList.get(i).equals(c.getIdStudent())) {
									if(voteListTxf.get(i).getText().equals("")) {
										if(Double.parseDouble("-1.0") != c.getVote()) {
											correctionModel.updateVote(uuidStudentList.get(i), idTest,Double.parseDouble("-1.0"));
											numVoteUpdate++;
										}
									} else {
										if(Double.parseDouble(voteListTxf.get(i).getText()) != c.getVote()) {
											correctionModel.updateVote(uuidStudentList.get(i), idTest,Double.parseDouble(voteListTxf.get(i).getText()));
											numVoteUpdate++;
										}
									}
								}
							}
						}
						
						repaintBorderTextField();
			
						correctionList = correctionModel.loadByIdTestAndClass(schoolClassCmbBox.getItemAt(schoolClassCmbBox.getSelectedIndex()).getName(),idTest);
			
						numUpgrateDb.removeAll();
						nUpgradeDbVote.setText("Correzione compleatata, inseriti i nuovi " + numVoteUpdate + " valori");
						numUpgrateDb.add(nUpgradeDbVote);
						numUpgrateDb.revalidate();
						numUpgrateDb.repaint();
			
					}     
				    	});
				    	t.start();
			
			}else {
				JOptionPane.showMessageDialog(this,
					    "ERROR: I voti inseriti sono negativi o maggiori di 10",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	private void setBorders() {
		Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		Border redLine = BorderFactory.createLineBorder(Color.RED);
		
		errorBorder = BorderFactory.createCompoundBorder(
				redLine,
				BorderFactory.createCompoundBorder(emptyBorder, emptyBorder));
		
	}
	
	private boolean checkCorrectVote() {
		boolean allCorrect =true;
		for(JTextField voteString : voteListTxf) {
			if(!voteString.getText().equals("")) {
				Double voteDouble = Double.parseDouble(voteString.getText());
				if(voteDouble < 0 || voteDouble > 10) {
					voteString.setBorder(errorBorder);
					allCorrect = false;
				}
			}
		}
		return allCorrect;
	}
	
	private void repaintBorderTextField() {
		JTextField normalJtextField = new JTextField();
		for(JTextField voteString : voteListTxf) {
			voteString.setBorder(normalJtextField.getBorder());
		}
	}
}
