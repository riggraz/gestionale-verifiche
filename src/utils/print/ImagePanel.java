package utils.print;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage bufferedImage;
	
	public ImagePanel(String imagePath) {
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bufferedImage, 0, 0, this);
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

}
