package utils.print;

import static utils.print.PrintUtils.divideLine;
import static utils.print.PrintUtils.printQeA;
import static utils.print.PrintUtils.printWithDialogAndAttributes;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
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
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

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
		
		for(int i = 0; i < questionModel.getItemsCount(); i++) {
			Question questionTemp = lsQuestion.get(i);
			lsAnswerTemp = answerModel.loadByQuestionId(questionTemp.getId());
			
			for(int ind = 0; ind < 4; ind++) {
				lsAnswer.add(lsAnswerTemp.get(ind));
			}
		}

		createDocument();
	}
	
	private void createDocument() {		
		document = new PDDocument();   
		      
	    // Retrieving the pages of the document 
	    PDPage page = new PDPage(PDRectangle.A4);
	    document.addPage(page);
		    
	    try {
	    	contentStream = new PDPageContentStream(document, page);
	    	
	    	pagProp = new PageProperty(PDType1Font.TIMES_ROMAN, page, QUESTION_FONT_SIZE, 72);
		    posPag = new PagePosition(0, contentStream);
		    
		    // Begin the Content stream
		    posPag.getContentStream().beginText();
		    
		    //Setting the font to the Content stream  
			posPag.getContentStream().setFont(pagProp.getPdfFont(), QUESTION_FONT_SIZE);
		    
			posPag.getContentStream().newLineAtOffset(pagProp.getStartX(), pagProp.getStartY());
			      
		    posPag.getContentStream().showText("Nome _____________   Cognome _____________    Classe ______  Data _________" );
	 	    posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	 	    posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	       	
	       	
	  		posPag.getContentStream().setFont(PDType1Font.TIMES_BOLD, 15);
 			posPag.getContentStream().showText(" " + nameTest);
	   		posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	   		posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
	   		posPag.setLine(posPag.getLine() + 4);
	   		
	   		for(int dim = 0; dim < lsQuestion.size(); dim++) {
  	        	dividedQuestion = divideLine(lsQuestion.get(dim).getBody(), pagProp);
  	        	printQeA(dividedQuestion, posPag, lsAnswer, dim, document, pagProp);
  	        }	
  	        
  	        //Ending the content stream
	   	    posPag.getContentStream().endText();
	   	    posPag.getContentStream().close();
		    
		} catch (IOException e) {
			System.err.println("Errore nella creazione del file");
		}
	}
	
	public List<String> getPreviewImages() {
		List<String> images = new ArrayList<String>();
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		
		for (int page = 0; page < document.getNumberOfPages(); page++) {
			BufferedImage bufferedImage;
			String imageName = "pdf-" + (page+1) + ".png";
			try {
				bufferedImage = pdfRenderer.renderImage(page);
				ImageIOUtil.writeImage(bufferedImage, imageName, 300);
				images.add(imageName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return images;
	}
	
	public void print(int copiesToPrint) {
		printWithDialogAndAttributes(document, copiesToPrint);
	}
	
	public void save(boolean openAfterSave) {
		fileChooser = new JFileChooser();
		
		if ((fileChooser.showSaveDialog(null)) == JFileChooser.APPROVE_OPTION) {
			try {
				String filePath = fileChooser.getCurrentDirectory() + "/" + fileChooser.getSelectedFile().getName() + ".pdf";
				document.save(filePath) ;
				
				if (openAfterSave) Desktop.getDesktop().open(new java.io.File(filePath));
			} catch (IOException e) {
				System.err.println("Errore nel salvataggio file");
			}
		}
		
	}
	
	public void close() {
		try {
			document.close();
		} catch (IOException e) {
			System.err.println("Errore nella chiusura del file");
		}
	}
}
