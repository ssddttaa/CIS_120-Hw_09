/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
		JPanel startScreen = new JPanel(new GridLayout(5,1));
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
				startingFrame.setVisible(false);
			}
			
		});
		
		final JButton settings = new JButton("Settings");
		settings.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startingFrame.setVisible(false);
				JFrame settingsFrame = new JFrame();
				JPanel difficultyPanel = new JPanel(new GridLayout(1,4));
				JButton easy = new JButton("Beginner");
				easy.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						mode = MinesweeperDifficulty.BEGINNER;
					}
				});
				JButton intermediate = new JButton("Intermediate");
				intermediate.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						mode = MinesweeperDifficulty.INTERMEDIATE;
					}
				});
				JButton expert = new JButton("Expert");
				expert.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						mode = MinesweeperDifficulty.EXPERT;
					}
				});
				
				JButton mainMenuButton = new JButton("Main Menu");
				mainMenuButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						settingsFrame.dispose();
						startingFrame.setVisible(true);
						
					}
				});
				
				difficultyPanel.add(new JLabel("Difficulty:"));
				difficultyPanel.add(easy);
				difficultyPanel.add(intermediate);
				difficultyPanel.add(expert);
				settingsFrame.add(difficultyPanel, BorderLayout.NORTH);
				settingsFrame.add(mainMenuButton, BorderLayout.SOUTH);
				settingsFrame.setLocation(300, 300);
				settingsFrame.pack();
				settingsFrame.setVisible(true);
				
				
				
			}
			
		});
		
		final JButton highScores = new JButton("High Scores");
		highScores.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startingFrame.setVisible(false);
				JFrame highScoresFrame = new JFrame();
				JPanel highScores = new JPanel(new GridLayout(10,1));
				HighScores scores = new HighScores();
				ArrayList<Score> allScores = scores.getScores();
				for(Score a : allScores){
					highScores.add(new JLabel(a.name + "    " + a.score));
				}
				
				JButton mainMenuButton = new JButton("Main Menu");
				mainMenuButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						highScoresFrame.dispose();
						startingFrame.setVisible(true);
						
					}
				});
				highScoresFrame.add(highScores, BorderLayout.CENTER);
				highScoresFrame.add(mainMenuButton, BorderLayout.SOUTH);
				highScoresFrame.pack();
				highScoresFrame.setLocation(600, 400);
				highScoresFrame.setVisible(true);
			}
		});
		
		startScreen.add(singlePlayerMode);
		startScreen.add(againstComputer);
		startScreen.add(instructions);
		startScreen.add(settings);
		startScreen.add(highScores);
		
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
		
		JButton mainMenuButtonMS = msFrame.getMainMenuButton();
		JButton mainMenuButtonAI = aiFrame.getMainMenuButton();
		
		ActionListener mainMenuListener = (new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aiFrame.dispose();
				msFrame.dispose();
				startFrame.setVisible(true);
			}
			
		});
		
		mainMenuButtonMS.addActionListener(mainMenuListener);
		mainMenuButtonAI.addActionListener(mainMenuListener);
		
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
		
		this.aiFrame = null;
		
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msFrame.dispose();
				if(aiFrame!=null){
					aiFrame.dispose();
					displayAIPane();
				}else{
					displaySinglePlayer();
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
					aiFrame = null;
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
