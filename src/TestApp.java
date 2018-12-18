import javax.swing.SwingUtilities;

public class TestApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GestionaleVerifiche();
			}
		});
	}
}