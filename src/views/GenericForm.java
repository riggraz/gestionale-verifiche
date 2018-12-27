package views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class GenericForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JLabel errorsLbl;
	JPanel errorsPnl;
	
	protected JButton saveBtn;
	protected JButton cancelBtn;
	
	public GenericForm(JComponent parent, String frameName) {
		super(frameName);
		
		setLayout(new GridLayout(0, 2, 12, 6));
		getRootPane().setBorder(new EmptyBorder(8, 16, 8, 16));
		
		saveBtn = new JButton("Salva");
		saveBtn.addActionListener(this);
		
		cancelBtn = new JButton("Annulla");
		cancelBtn.addActionListener(this);
		
		// i button vengono solo creati, ci penserà ogni singolo form
		// ad aggiungerli al frame
		
		getRootPane().setDefaultButton(saveBtn);
		setDefaultCloseOperation(GenericForm.DISPOSE_ON_CLOSE);
		//pack() dev'essere chiamato nei form specifici
		setLocationRelativeTo(parent);
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public final void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveBtn) {
			if (checkErrorsAndUpdateUI() == 0) {
				save();
				dispose();
			}
		} else if (e.getSource() == cancelBtn) {
			dispose();
		}
	}
	
	// ogni form può implementare il proprio controllo errori
	protected int checkErrorsAndUpdateUI() { return 0; }
	
	// Ogni form dovrà implementare la propria funzione save
	public abstract void save();
	
}
