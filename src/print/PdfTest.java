package print;

import static print.PrintUtils.divideLine;
import static print.PrintUtils.printQeA;
import static print.PrintUtils.printWithDialogAndAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import entities.Answer;
import entities.Question;
import models.AnswerModel;
import models.QuestionModel;
import utils.DBManager;

public class PdfTest {

	final static float QUESTION_FONT_SIZE = 12;
	PDDocument document;
	
	private QuestionModel questionModel;
	private AnswerModel answerModel;
	
	private String nameTest;
	
	private List<Question> lsQuestion;
	private List<Answer> lsAnswerTemp;
	private List<Answer> lsAnswer;
	private List<String> dividedQuestion;
	
	private PagePosition posPag;
	private PageProperty pagProp;
	private PDPageContentStream contentStream;
	
	private JFileChooser fileChooser;
	
	public PdfTest(DBManager dbManager, UUID idQuestion, String nameTest) {
		
		this.nameTest = nameTest;
		questionModel = new QuestionModel(dbManager);
		answerModel = new AnswerModel(dbManager);
		
		questionModel.loadByTestId(idQuestion);			
		
		lsQuestion = new ArrayList<Question>();
		lsQuestion = questionModel.getQuestions();
		
		lsAnswerTemp = new ArrayList<Answer>();
		lsAnswer = new ArrayList<Answer>();
		
		dividedQuestion = new ArrayList<String>();
		
		contentStream = null;
		
		for(int i =0;i<questionModel.getItemsCount();i++) {
			Question questionTemp = lsQuestion.get(i);
			lsAnswerTemp = answerModel.loadByQuestionId(questionTemp.getId());
			
			for(int ind =0;ind<4;ind++) {
				lsAnswer.add(lsAnswerTemp.get(ind));
			}
			
		}			    		 

		createDocument();
		
	}
	
	private void createDocument() {
						
		
		document = new PDDocument();   
		      
	    //Retrieving the pages of the document 
	    PDPage page = new PDPage(PDRectangle.A4);
	    document.addPage(page);
		    
	    try {
	    	contentStream = new PDPageContentStream(document,page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore nella creazione del file");;
		}
		    
	    pagProp = new PageProperty(PDType1Font.TIMES_ROMAN,page,QUESTION_FONT_SIZE,72);
	    posPag = new PagePosition(0,contentStream);
		   
		//Begin the Content stream 
		try {
			posPag.getContentStream().beginText();

		       
			//Setting the font to the Content stream  
			posPag.getContentStream().setFont(pagProp.getPdfFont(), QUESTION_FONT_SIZE);
		    
			posPag.getContentStream().newLineAtOffset(pagProp.getStartX(), pagProp.getStartY());
			//Adding text in the form of string 
			      
		    posPag.getContentStream().showText("Nome _____________   Cognome _____________    Classe ______  Data _________" );
	 	    posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	 	    posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	       	
	       	
	  		posPag.getContentStream().setFont(PDType1Font.TIMES_BOLD, 15);
 			posPag.getContentStream().showText(" " + nameTest );
	   		posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	   		posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	   		posPag.setLine(posPag.getLine() +4);
		      
  	        for(int dim=0;dim<lsQuestion.size();dim++) {
  	        	dividedQuestion = divideLine(lsQuestion.get(dim).getBody(),pagProp);
  	        	printQeA(dividedQuestion,posPag,lsAnswer,dim,document,pagProp);
  	        }	
  	        
  	        //Ending the content stream
	   	    posPag.getContentStream().endText();
	   	    posPag.getContentStream().close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Errore nella scrittura del file");
			} 
	}
	
	public void print() {
		printWithDialogAndAttributes(document);
		
   	    try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore nella chiusura del file");
		}
	}
	
	public void save() {
		
		fileChooser = new JFileChooser();
		
		if((fileChooser.showSaveDialog(null)) == JFileChooser.APPROVE_OPTION) {
			try {
				System.out.println();
				document.save(fileChooser.getCurrentDirectory() + "/" + fileChooser.getSelectedFile().getName() + ".pdf") ;
			} catch (IOException e) {
//			 	TODO Auto-generated catch block
				System.err.println("Errore nel salvataggio file");
			}
		}
		
   	    try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore nella chiusura del file");
		}
	}
}
