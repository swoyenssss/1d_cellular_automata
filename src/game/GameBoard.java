package game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameBoard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GamePanel gamePanel;
	private JPanel buttonPanel;
	private JButton startButton;
	private JLabel ruleLabel;
	private JTextField ruleText;
	private Thread gameThread;

	public GameBoard() {
		super("Cellular Automata");
		this.setSize(1400,1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		gameThread = new Thread(gamePanel);
		gamePanel = new GamePanel();
		this.add(gamePanel);

		createButtonPanel();
		this.add(buttonPanel,BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		ruleLabel = new JLabel("Rule: ");

		ruleText = new JTextField(4);
		ruleText.setText(30 + "");

		startButton = new JButton();
		startButton.setText("Start");
		startButton.addActionListener(e->{
			
				int rule = Integer.parseInt(ruleText.getText());
				
				if(rule >= 0  && rule < 256) {
					gamePanel.setRuleset(rule);
					gamePanel.reinit();
				}
				else {
					JOptionPane.showMessageDialog(this,"Please Enter a Number Between 0 - 255");
				}
			
		});

		buttonPanel.add(ruleLabel);
		buttonPanel.add(ruleText);
		buttonPanel.add(startButton);

	}
	
	public void start() {
		while(true) {
			gamePanel.run();
		}
	}

}
