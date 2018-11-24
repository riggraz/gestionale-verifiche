package views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public abstract class GenericForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	protected JButton saveBtn;
	protected JButton cancelBtn;
	
	public GenericForm(String frameName) {
		super(frameName);
		
		setLayout(new GridLayout(0, 2, 12, 6));
		
		saveBtn = new JButton("Salva");
		saveBtn.addActionListener(this);
		
		cancelBtn = new JButton("Annulla");
		cancelBtn.addActionListener(this);
		
		// i button vengono solo creati, ci penserà ogni singolo form
		// ad aggiungerli al frame
		
		getRootPane().setDefaultButton(saveBtn);
		setDefaultCloseOperation(GenericForm.DISPOSE_ON_CLOSE);
		//pack() dev'essere chiamato nei form specifici
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveBtn) {
			save();
		} else if (e.getSource() == cancelBtn) {
			dispose();
		}
	}
	
	// Ogni form dovrà implementare la propria funzione save
	public abstract void save();

}
