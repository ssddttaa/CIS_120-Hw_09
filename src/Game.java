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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
		
		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startingFrame.setVisible(false);
				JFrame instructionsFrame = new JFrame();
				JPanel instructionsPanel = new JPanel();
				JTextArea instructions = new JTextArea(
						"Welcome to Minesweeper, made by Sadat Shaik for "+
						"the CIS 120 final project! In order to start the game "
						+"simply click the main-menu button, and click single "+
						"player to begin! To play the game, "+
						"you must left click to reveal any of the squares, and "
						+"right click in order to flag any of the squares. "+
						"The goal of the game is to use the information given "+
						"by the numbers (which let you know how many mines "+
						"surround that square among the 8 squares surrounding "+
						"it) to determine what squares are mines and what are "+
						"not. Once you have found a mine, you can flag it by "+
						"right-clicking it. Note that in order to win, you "+
						"must successfully flag all of the mines. To win the "+
						"game it is not sufficient to just click all squares "+
						"that are not mines. Have fun!"
						);
				instructions.setLineWrap(true);
				instructions.setWrapStyleWord(true);
				instructions.setSize(300,300);
				instructions.setEditable(false);
				JButton mainMenu = mainMenuButton(instructionsFrame);
				instructionsPanel.add(instructions);
				instructionsFrame.add(instructionsPanel, BorderLayout.CENTER);
				instructionsFrame.add(mainMenu, BorderLayout.SOUTH);
				instructionsFrame.setLocation(300,300);
				instructionsFrame.pack();
				instructionsFrame.setVisible(true);
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
				
				JButton mainMenuButton = mainMenuButton(settingsFrame);
				
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
				JButton mainMenuButton = mainMenuButton(highScoresFrame);
				highScoresFrame.add(highScores, BorderLayout.CENTER);
				highScoresFrame.add(mainMenuButton, BorderLayout.SOUTH);
				highScoresFrame.pack();
				highScoresFrame.setLocation(600, 400);
				highScoresFrame.setVisible(true);
			}
		});
		
		startScreen.add(singlePlayerMode);
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

	
	public void displaySinglePlayer(){
		MinesweeperPanel mp =
				new MinesweeperPanel(new JLabel("Player 1"),
						    this.mode);
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
				displaySinglePlayer();
				
			}
		});
		mainMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msFrame.dispose();
				startFrame.setVisible(true);
			}
			
		});
		mf.setVisible(true);
	}
	
	public JButton mainMenuButton(JFrame frameToDispose){
		JButton mainMenuButton = new JButton("Main Menu");
		mainMenuButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frameToDispose.dispose();
				startFrame.setVisible(true);
			}
		});
		return mainMenuButton;
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
