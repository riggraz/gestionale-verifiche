package views.manage_test;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.QuestionModel;
import utils.DBManager;
import entities.Question;

public class CorrectTest extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel correctAnswerPnl;
	private QuestionModel questionModel;
	private List<Question> questionList;
	private JLabel correctAnswerLbl;
	private JLabel stringCorrectAnswer;
	
	public CorrectTest(DBManager dbManager, UUID idTest) {
		super("Correzzione Verifica");
		setLayout(new BorderLayout());
		getRootPane().setBorder(new EmptyBorder(16, 16, 16, 16));
		
		correctAnswerPnl = new JPanel(new GridLayout(1,2));
		correctAnswerPnl.setBorder(new EmptyBorder(16,16,16,16));
		
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
		
		this.add(correctAnswerPnl,BorderLayout.NORTH);
		
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
	
}
