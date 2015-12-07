/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private JFrame startFrame;
	private MinesweeperFrame msFrame;
	private MinesweeperFrame aiFrame;
	private MinesweeperDifficulty mode = MinesweeperDifficulty.BEGINNER;
	public void run() {
		final JFrame startingFrame = new JFrame("TOP LEVEL FRAME");	
		JPanel startScreen = new JPanel(new GridLayout(4,1));
		startingFrame.setLocation(300, 300);
		final JButton singlePlayerMode = new JButton("Single Player");
		
		singlePlayerMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displaySinglePlayer();
				startingFrame.setVisible(false);
			}
		});
		
		final JButton againstComputer = new JButton("1 vs. Computer");
		againstComputer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayAIPane();
				startingFrame.setVisible(false);
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

	private void displayAIPane() {
		MinesweeperPanel aiMP =
				new MinesweeperPanel(new JLabel("AI"),
						    this.mode, true);
		
		MinesweeperPanel mp =
				new MinesweeperPanel(new JLabel("Player 1"),
						    this.mode, false);
		
		MinesweeperFrame aiFrame =
				new MinesweeperFrame(aiMP,new Point(300,300), "AI Board");
		
		MinesweeperFrame msFrame 
				= new MinesweeperFrame(mp, new Point(600,300), "User Board");

		this.msFrame = msFrame;
		this.aiFrame = aiFrame;
		
		this.aiFrame.setVisible(true);
		this.msFrame.setVisible(true);

		ScheduledExecutorService checkIfGameStarted
			= Executors.newSingleThreadScheduledExecutor();
		
		checkIfGameStarted.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mp.isStartedPlaying()){
					aiMP.startGame();
					checkIfGameStarted.shutdown();
				}
				
			}
		}, 0, 1, TimeUnit.SECONDS);
		
		
		
		JButton resetButtonAI = this.aiFrame.getResetButton();
		JButton resetButtonMS = this.msFrame.getResetButton();
		ActionListener resetAIFrame = (new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aiFrame.dispose();
				msFrame.dispose();
				displayAIPane();
			}
		});
		resetButtonAI.addActionListener(resetAIFrame);
		resetButtonMS.addActionListener(resetAIFrame);
		
		JButton mainMenuButton = msFrame.getMainMenuButton();
		mainMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aiFrame.dispose();
				msFrame.dispose();
				startFrame.setVisible(true);
			}
			
		});
	}
	
	public void displaySinglePlayer(){
		MinesweeperPanel mp =
				new MinesweeperPanel(new JLabel("Player 1"),
						    this.mode, false);
		MinesweeperFrame mf
		= new MinesweeperFrame(mp, new Point(300,300), "User Board");
		JButton resetButton = mf.getResetButton();
		JButton mainMenuButton = mf.getMainMenuButton();
		mf.setVisible(true);
		
		this.msFrame = mf;
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msFrame.dispose();
				if(aiFrame!=null){
					aiFrame.dispose();
					displaySinglePlayer();
				}else{
					displayAIPane();
				}
				
			}
		});
		mainMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msFrame.dispose();
				if(aiFrame!=null){
					aiFrame.dispose();
				}
				startFrame.setVisible(true);
			}
			
		});
		mf.setVisible(true);
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
