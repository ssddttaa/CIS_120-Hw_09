import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MinesweeperFrame extends JFrame{
	private MinesweeperPanel mp;
	private JButton resetButton;
	private JButton mainMenuButton;
	public MinesweeperFrame(MinesweeperPanel mp){
		this.mp = mp;
		
		JPanel controlPanel = new JPanel(new GridLayout(2,1));
		controlPanel.setSize(50,240);
		
		this.resetButton = new JButton("Reset");
		this.mainMenuButton = new JButton("Main Menu");
		
		controlPanel.add(resetButton);
		controlPanel.add(mainMenuButton);
		
		JPanel timerPanel = new JPanel();
		JLabel initialTimeLabel = new JLabel("Time Since Game Started: 0");
		mp.setElapsedTimeLabel(initialTimeLabel);
		
		timerPanel.add(mp.getElapsedTimeLabel(), BorderLayout.NORTH);
		
		setLocation(300, 300);
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