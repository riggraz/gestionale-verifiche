package print;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class PageProperty {

	 	PDFont pdfFont;
	    PDPage page;
	    float fontSize;
	    float leading;
	    float margin;
	    float width;
	    float startX;
	    float startY;  
	    
	    
		public PageProperty(PDFont pdfFont, PDPage page, float fontSize,float margin) {
			super();
			this.pdfFont = pdfFont;
			this.page = page;
			this.fontSize = fontSize;
			this.leading = 1.5f * fontSize;
			this.margin = margin;
			PDRectangle mediabox = page.getMediaBox();
		    width = mediabox.getWidth() - 2*margin;
		    startX = mediabox.getLowerLeftX() + margin;
		    startY = mediabox.getUpperRightY() - margin;
		}
		
		public PDFont getPdfFont() {
			return pdfFont;
		}
		
		public void setPdfFont(PDFont pdfFont) {
			this.pdfFont = pdfFont;
		}
		
		public PDPage getPage() {
			return page;
		}
		
		public void setPage(PDPage page) {
			this.page = page;
		}
		
		public float getFontSize() {
			return fontSize;
		}
		
		public void setFontSize(float fontSize) {
			this.fontSize = fontSize;
		}
		
		public float getLeading() {
			return leading;
		}
		
		public void setLeading(float leading) {
			this.leading = leading;
		}
		
		public float getMargin() {
			return margin;
		}
		
		public void setMargin(float margin) {
			this.margin = margin;
		}
		
		public float getWidth() {
			return width;
		}
		
		public void setWidth(float width) {
			this.width = width;
		}
		
		public float getStartX() {
			return startX;
		}
		
		public void setStartX(float startX) {
			this.startX = startX;
		}
		
		public float getStartY() {
			return startY;
		}
		
		public void setStartY(float startY) {
			this.startY = startY;
		}
	    	    
	    
}
