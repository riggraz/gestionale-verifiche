package utils.print;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class PagePosition {
	
	int line;
	PDPageContentStream contentStream;
	
	public PagePosition(int line, PDPageContentStream contentStream) {
		super();
		this.line = line;
		this.contentStream = contentStream;
	}
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	
	public PDPageContentStream getContentStream() {
		return contentStream;
	}
	public void setContentStream(PDPageContentStream contentStream) {
		this.contentStream = contentStream;
	}
	
}
