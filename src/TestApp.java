import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {					
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("OptionPane.yesButtonText", "Si");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				new GestionaleVerifiche();
			}
		});
	}
}