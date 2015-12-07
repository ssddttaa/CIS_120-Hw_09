/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private JFrame startFrame;
	private MinesweeperPanel mp;
	private MinesweeperPanel aiMP;
	private JFrame msFrame;
	private JFrame aiFrame;
	private MinesweeperDifficulty mode = MinesweeperDifficulty.BEGINNER;
	public void run() {
		final JFrame startingFrame = new JFrame("TOP LEVEL FRAME");	
		JPanel startScreen = new JPanel(new GridLayout(4,1));
		startingFrame.setLocation(300, 300);
		msFrame = new JFrame("Top Level Frame");
		aiFrame = new JFrame("AI Frame");
		final JButton singlePlayerMode = new JButton("Single Player");
		singlePlayerMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displaySinglePlayer(startingFrame);
			}
		});
		final JButton againstComputer = new JButton("1 vs. Computer");
		againstComputer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayAIPane(startingFrame);
				displaySinglePlayer(startingFrame);
				
			}
			
		});
		
		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		final JButton settings = new JButton("Settings");
		settings.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		startScreen.add(singlePlayerMode);
		startScreen.add(againstComputer);
		startScreen.add(instructions);
		startScreen.add(settings);
		
		startingFrame.add(startScreen);
		startingFrame.setResizable(false);
		startingFrame.pack();
		startingFrame.setSize(250, 240);
		startingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startingFrame.setVisible(true);
		this.startFrame = startingFrame;
	}

	private void displayAIPane(JFrame startingFrame) {
		startingFrame.setVisible(false);
		MinesweeperPanel mp =
				new MinesweeperPanel(new JLabel("AI"),
						    this.mode);
		if(this.aiMP != null){
			aiFrame.getContentPane().remove(this.aiMP);
			if(this.aiMP.globalTimer!=null){
				this.aiMP.globalTimer.shutdown();
				this.aiMP.resetTimeSinceGameStarted();
			}
		}
		this.aiMP = mp;
		JPanel controlPanel = new JPanel(new GridLayout(2,1));
		controlPanel.setSize(50,240);
		
		JButton resetButton = new JButton("Reset");
		
		JButton mainMenuButton = new JButton("Main Menu");
		controlPanel.add(resetButton);
		controlPanel.add(mainMenuButton);
		
		JPanel timerPanel = new JPanel();
		JLabel initialTimeLabel = new JLabel("Time Since Game Started: 0");
		aiMP.setElapsedTimeLabel(initialTimeLabel);
		
		timerPanel.add(aiMP.getElapsedTimeLabel(), BorderLayout.NORTH);
		
		aiFrame.setLocation(0, 300);
		aiFrame.add(aiMP, BorderLayout.WEST);
		aiFrame.add(timerPanel, BorderLayout.NORTH);
		aiFrame.add(controlPanel, BorderLayout.EAST);
		aiFrame.setResizable(false);
		aiFrame.pack();
		
		aiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aiFrame.setVisible(true);
		
		
		
		Game temp = this;
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayAIPane(aiFrame);
			}
		});
		mainMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aiFrame.setVisible(false);
				MinesweeperPanel newAIMP =
						new MinesweeperPanel(new JLabel("AI"),
								    temp.mode);
				if(temp.aiMP != null){
					aiFrame.getContentPane().remove(temp.aiMP);
					if(temp.aiMP.globalTimer!=null){
						temp.aiMP.globalTimer.shutdown();
						temp.aiMP.resetTimeSinceGameStarted();
					}
				}
				aiFrame.add(newAIMP, BorderLayout.WEST);
				temp.aiMP = newAIMP;
				if(temp.mp!=null){
					msFrame.getContentPane().remove(temp.mp);
					if(temp.mp.globalTimer!=null){
						temp.mp.globalTimer.shutdown();
						temp.mp.resetTimeSinceGameStarted();
					}
				}
				MinesweeperPanel newMP =
						new MinesweeperPanel(new JLabel("Player 1"),
								    temp.mode);
				msFrame.add(newMP, BorderLayout.WEST);
				temp.mp = newMP;
				
				
				startFrame.setVisible(true);
				
			}
			
		});
	}
	
	public void displaySinglePlayer(JFrame frameToToggle){
		frameToToggle.setVisible(false);
		MinesweeperPanel mp =
				new MinesweeperPanel(new JLabel("Player 1"),
						    this.mode);
		if(this.mp != null){
			msFrame.getContentPane().remove(this.mp);
			if(this.mp.globalTimer!=null){
				this.mp.globalTimer.shutdown();
				this.mp.resetTimeSinceGameStarted();
			}
		}
		this.mp = mp;
		JPanel controlPanel = new JPanel(new GridLayout(2,1));
		controlPanel.setSize(50,240);
		
		JButton resetButton = new JButton("Reset");
		
		JButton mainMenuButton = new JButton("Main Menu");
		controlPanel.add(resetButton);
		controlPanel.add(mainMenuButton);
		
		JPanel timerPanel = new JPanel();
		JLabel initialTimeLabel = new JLabel("Time Since Game Started: 0");
		mp.setElapsedTimeLabel(initialTimeLabel);
		
		timerPanel.add(mp.getElapsedTimeLabel(), BorderLayout.NORTH);
		
		msFrame.setLocation(300, 300);
		msFrame.add(mp, BorderLayout.WEST);
		msFrame.add(timerPanel, BorderLayout.NORTH);
		msFrame.add(controlPanel, BorderLayout.EAST);
		msFrame.setResizable(false);
		msFrame.pack();
		
		msFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		msFrame.setVisible(true);
		
		
		
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displaySinglePlayer(msFrame);
			}
		});
		Game temp = this;
		mainMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msFrame.setVisible(false);
				if(temp.mp!=null){
					msFrame.getContentPane().remove(temp.mp);
					if(temp.mp.globalTimer != null){
						temp.mp.globalTimer.shutdown();
						temp.mp.resetTimeSinceGameStarted();
					}
				}
				MinesweeperPanel newMP =
						new MinesweeperPanel(new JLabel("Player 1"),
								   	mode);
				msFrame.add(newMP, BorderLayout.WEST);
				temp.mp = newMP;
				
				if(aiMP != null){
					aiFrame.getContentPane().remove(temp.aiMP);
					if(temp.aiMP.globalTimer!=null){
						temp.aiMP.globalTimer.shutdown();
						temp.aiMP.resetTimeSinceGameStarted();
					}
					MinesweeperPanel newAIMP =
							new MinesweeperPanel(new JLabel("AI"),temp.mode);
					aiFrame.add(newAIMP, BorderLayout.WEST);
					aiMP = newAIMP;
				}
				
				startFrame.setVisible(true);
				
			}
			
		});
		
	}
	
	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
