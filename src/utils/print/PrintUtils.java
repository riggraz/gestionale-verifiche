package utils.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;

import entities.Answer;
import utils.print.PagePosition;
import utils.print.PageProperty;

public class PrintUtils {

	public static void printQeA(List<String> dividedQuestion, PagePosition posPag, List<Answer> lsAnswer, int dim, PDDocument document, PageProperty pagProp) throws IOException {

		List<String> divideAnswer = new ArrayList<String>();
		
		posPag.getContentStream().setFont(PDType1Font.TIMES_BOLD, pagProp.getFontSize());
		posPag.getContentStream().showText("Domanda " + (dim+1));
		
		newLine(posPag,pagProp);
		
      	posPag.getContentStream().setFont(pagProp.getPdfFont(), pagProp.getFontSize());
		
		for (String line : dividedQuestion) {			
          	posPag.getContentStream().showText(" " + line);
          	newLine(posPag,pagProp);
          	
        	if(posPag.getLine() >= 38) {
        		createNewContent(posPag,document,pagProp);
          	}
      	}
      	
		posPag.getContentStream().setFont(pagProp.getPdfFont(), 11);
		
		for (int numRisp = 0; numRisp < 4; numRisp++) {
			
			pagProp.setWidth(pagProp.getWidth()-10);
			divideAnswer = divideLine(lsAnswer.get((4*dim)+numRisp).getBody(),pagProp);
			pagProp.setWidth(pagProp.getWidth()+10);
			
			String value = "a";
			int charValue = value.charAt(0);
			value = String.valueOf( (char) (charValue + numRisp));
			
			for (int i=0; i < divideAnswer.size(); i++)
	      	{
				if (i == 0) {
					posPag.getContentStream().showText("     "+ value + ") " + divideAnswer.get(i));
				} else {
					posPag.getContentStream().showText("          "+ divideAnswer.get(i));
				}
				
				newLine(posPag,pagProp);
	          	
	          	if(posPag.getLine() >= 38) {
	          		createNewContent(posPag,document,pagProp);
	          	}
	      	}	
		}
		
		newLine(posPag,pagProp);
      	
    	if(posPag.getLine() >= 38) {
      		createNewContent(posPag,document,pagProp);
      	}
      	
	}
	
	public static void newLine(PagePosition posPag,PageProperty pagProp) throws IOException {
		posPag.getContentStream().newLineAtOffset(0, -pagProp.getLeading());
      	posPag.setLine(posPag.getLine() +1);
	}
	
	public static void createNewContent(PagePosition posPag, PDDocument document, PageProperty pagProp) throws IOException {
		posPag.getContentStream().endText();
		posPag.getContentStream().close();
	      
	    pagProp.setPage(new PDPage(PDRectangle.A4)) ;
	    document.addPage(pagProp.getPage());
	    posPag.setContentStream(new PDPageContentStream(document,pagProp.getPage()));
	    posPag.getContentStream().beginText(); 
	    posPag.getContentStream().setFont(pagProp.getPdfFont(), pagProp.getFontSize());
	    posPag.getContentStream().newLineAtOffset(pagProp.getStartX(), pagProp.getStartY());
	      
	    posPag.setLine(0);
	}
	
	public static void printWithDialogAndAttributes(PDDocument document, int copiesToPrint) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPageable(new PDFPageable(document));
		
		PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
		attr.add(new Copies(copiesToPrint));
		attr.add(MediaSizeName.ISO_A4);
		
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		
	    if (job.printDialog(attr)) {
	        try {
				job.print(attr);
			} catch (PrinterException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public static List<String> divideLine(String text, PageProperty pagProp) {
		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		    
	    while (text.length() > 0) {
	        text = text.replace("\n", " ").replace("\r", " ");
	        int spaceIndex = text.indexOf(' ', lastSpace + 1);
	        
	        if (spaceIndex < 0) spaceIndex = text.length();
	        
	        String subString = text.substring(0, spaceIndex);
	        float size = 0;
	        
			try {
				size = pagProp.getFontSize() *  pagProp.getPdfFont().getStringWidth(subString) / 1000;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        if (size >  pagProp.getWidth()) {
	            if (lastSpace < 0) lastSpace = spaceIndex;
	            
	            subString = text.substring(0, lastSpace);
	            lines.add(subString);
	            text = text.substring(lastSpace).trim();
	            lastSpace = -1;
	        } else if (spaceIndex == text.length()) {
	            lines.add(text);
	            text = "";
	        } else {
	            lastSpace = spaceIndex;
	        }
	    }
	    
	    return lines;
	}
}
