package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import utils.DBManager;
import views.manage_school_class.ManageSchoolClass;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	JTabbedPane tabbedPane;
	
	public MainFrame(DBManager dbManager) {
		super("Gestionale verifiche");
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add("Gestione verifiche", new JPanel());
		tabbedPane.add("Gestione classi", new ManageSchoolClass(dbManager));
		
		add(tabbedPane);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(680, 600);
		setVisible(true);
	}

}
