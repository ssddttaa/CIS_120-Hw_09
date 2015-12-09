import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MinesweeperFrame extends JFrame{
	private MinesweeperPanel mp;
	private JButton resetButton;
	private JButton mainMenuButton;
	public MinesweeperFrame(MinesweeperPanel mp, Point location, String label){
		super(label);
		//Store the panel (game logic) to be displayed on the frame
		this.mp = mp;
		//Initialize the size of the contorl panel so it can hold the reset and
		//main menu buttons
		JPanel controlPanel = new JPanel(new GridLayout(2,1));
		controlPanel.setSize(50,240);
		
		this.resetButton = new JButton("Reset");
		this.mainMenuButton = new JButton("Main Menu");
		
		//Adds the buttons to the control panel
		controlPanel.add(resetButton);
		controlPanel.add(mainMenuButton);
		
		//Creates the timer panel which displays the current time of the game
		//which is also the score.
		JPanel timerPanel = new JPanel();
		JLabel initialTimeLabel = new JLabel("Time Since Game Started: 0");
		this.mp.setElapsedTimeLabel(initialTimeLabel);
		
		timerPanel.add(mp.getElapsedTimeLabel(), BorderLayout.NORTH);
		setLocation(location);
		add(mp, BorderLayout.WEST);
		add(timerPanel, BorderLayout.NORTH);
		add(controlPanel, BorderLayout.EAST);
		setResizable(false);
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JButton getResetButton() {
		return resetButton;
	}
	public void setResetButton(JButton resetButton) {
		this.resetButton = resetButton;
	}
	public JButton getMainMenuButton() {
		return mainMenuButton;
	}
	public void setMainMenuButton(JButton mainMenuButton) {
		this.mainMenuButton = mainMenuButton;
	}
}
