import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MinesweeperPanel extends JPanel{
	private final int numMines;
	private MineButton[][] buttons;
	private final int boardSize[];
	private boolean debugMode = false;
	public Boolean startedPlaying = false; 
	public Boolean lostGame = false;
	
	private JLabel elapsedTime;
	public int timeSinceGameStarted = 0;

	public ScheduledExecutorService globalTimer;
	
	private LinkedList<Point> mineLocations;
	
 	public MinesweeperPanel
 		(JLabel player, MinesweeperDifficulty diff){
		super(new GridLayout(diff.getSize()[0],diff.getSize()[1]));
		this.boardSize = diff.getSize();
		this.mineLocations = new LinkedList<Point>();
		buttons = new MineButton[boardSize[0]][boardSize[1]];
		globalTimer = null;
		//Get how many mines should be on the board based on what difficulty
		this.numMines = getNumMinesFromXSize(boardSize[0]);
		//Add the mines to the baord
		this.addMinesToBoard();
		//Add the number tiles and the blank squares to the board
		this.addNonMineSquaresToBoard();
		
		//After adding the squares, this will go through each button, add its
		//neighbors to its linked list that stores what its neighbors are. Also
		//this changes the color of the button based on what type of button
		//it is
		for(int i = 0 ; i < boardSize[0]; i ++){
			for(int j = 0 ; j < boardSize[1]; j ++){
				MineButton currentButton = buttons[i][j];
				this.linkAdjacentSquares(currentButton);
				currentButton.setPreferredSize(new Dimension(20,20));
				if(currentButton instanceof Mine&&debugMode){
					currentButton.setBackground(Color.RED);
					currentButton.setOpaque(true);
					currentButton.setBorderPainted(false);
				}else if(currentButton instanceof BlankSquareButton&&debugMode){
					currentButton.setBackground(Color.WHITE);
					currentButton.setOpaque(true);
					currentButton.setBorderPainted(false);
				}
				this.add(currentButton);
			}
		}
	}
	
 	public boolean isStartedPlaying() {
		return startedPlaying;
	}


	public void setStartedPlaying(boolean startedPlaying) {
		this.startedPlaying = startedPlaying;
	}
	
	public int getTimeSinceGameStarted() {
		return timeSinceGameStarted;
	}

	public void resetTimeSinceGameStarted() {
		this.timeSinceGameStarted = 0;
	}
	//Gets the number of mines based on what game mode it is 
	private int getNumMinesFromXSize(int x){
		switch(x){
		case 8:
			return 10;
		case 16:
			return 40;
		case 30:
			return 99;
		default:
			return 10;
		}
	}
	
	//Adds all the mines to the board
	private void addMinesToBoard(){
		Random randomGenerator = new Random();
		for(int i = 0 ; i < this.numMines; i++){
			int randX = randomGenerator.nextInt(boardSize[0]);
			int randY = randomGenerator.nextInt(boardSize[1]);
			while(buttons[randX][randY]!=null){
				//Ensures this location does not already have a mine
				randX = randomGenerator.nextInt(boardSize[0]);
				randY = randomGenerator.nextInt(boardSize[1]);
			}
			//Adds it once it finds a location that does not already have a
			//mine
			Mine newMine = new Mine(new Point(randX,randY));
			//Updates the locations of the miens
			mineLocations.add(newMine.getLocation());
			//Updates the 2D buttons array with this button
			buttons[randX][randY] = newMine;
			//Adds a mouse listener to end the game once this mine is clicked
			newMine.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					switch(e.getButton()){
					//Checks if the click was a right click, thus the user
					//wanting to flag it, rather than a left click, which would
					//imply that they wanted to click it
						case(MouseEvent.BUTTON3):
							actionOnMineClick(true,newMine);
							break;
						default:
							actionOnMineClick(false,newMine);
							break;
					}
				}
				
			});
			
		}
	}
	
	//Function to end game once the mine has been clicke
	public void actionOnMineClick(boolean isRightClick, MineButton mine){
		if(startedPlaying&&!lostGame){
			//If its a right click, then user intended to flag this button
			if(isRightClick){
				if(!mine.isClicked()){
					changeFlagged(mine);
				}
			}//If it is not a right click, then the plyer has lost
			else if(!mine.isFlagged()){
				mine.onClicked();
				//This removes any icon on the button
				mine.setFlagged(true);
				changeFlagged(mine);
				//This function actually ends the game.
				clickedMine();
			}
		}//Start the game if they haven't lost and game hasn't started yet.
		else if(!startedPlaying&&!lostGame){
			startGame();
			//Call the function again so the user's initial click is registered.
			actionOnMineClick(isRightClick,mine);
		}
	}
	
	//Handles what should happen if a blank button is clicked. Namely, it should
	//toggle all nearby blank buttons to be clicked as well.
	public void actionOnBlankButtonClick
		(boolean isRightClick, MineButton button){
		if(startedPlaying&&!lostGame){
			//Flag the button if it is a right click
			if(isRightClick){
				if(!button.isClicked()){
					changeFlagged(button);
				}
			}//Otherwise, click all nearby blank squares
			else if(!button.isFlagged()
					&&!button.isClicked()){
				toggleNearbySquares(button);
				button.setClicked(true);
				button.onClicked();
			}
		}//Start the game if they haven't lost and game hasn't started yet.
		else if(!startedPlaying&&!lostGame){
			startGame();
			//Call the function again so the user's initial click is registered.
			actionOnBlankButtonClick(isRightClick,button);
		}
	}
	//Reveals a number button after it has been clicked
	public void actionOnNumberButtonClick
	(boolean isRightClick, MineButton button){
		if(startedPlaying&&!lostGame){
			if(isRightClick){
				//If it is a right click, then mark it as being flagged
				if(!button.isClicked()){
					changeFlagged(button);
				}
			}else if(!button.isFlagged()){
				//Remove the flag icon of the button if it is still there
				button.onClicked();
				button.setFlagged(true);
				changeFlagged(button);
			}
		}//Start the game if they haven't lost and game hasn't started yet.
		else if(!startedPlaying&&!lostGame){
			startGame();
			//Call the function again so the user's initial click is registered.
			actionOnNumberButtonClick(isRightClick,button);
		}
	}
	
	public void startGame(){
		//Start the game if it hasn't already
		if(!startedPlaying){
			startedPlaying = true;
		}
		//Create new thread that updates the score label every second with how
		//long it has been since the game has started.
		this.globalTimer = Executors.newSingleThreadScheduledExecutor();
		globalTimer.scheduleWithFixedDelay(new Runnable(){
			@Override
			public void run() {
				timeSinceGameStarted++;
				elapsedTime.setText("Time Since Game Started:" +
				timeSinceGameStarted);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	//Add the blank squares to the board and the number tiles
	private void addNonMineSquaresToBoard(){
		//Loop through the buttons array
		for(int i = 0 ; i < boardSize[0]; i++){
			for(int j = 0 ; j < boardSize[1] ; j++){
				//Check for any location that does not have a button already
				if(buttons[i][j]==null){
					int numSurroundingMines = 0;
					//Determine how many mines surround this tile
					for(int xOffset = -1 ; xOffset < 2; xOffset++){
						for(int yOffset = -1 ; yOffset < 2; yOffset++){
							if(xOffset != 0 || yOffset != 0){
								//Prevent from checking a button that is out
								//of bounds of the board.
								if(i + xOffset >= 0 && i + xOffset < boardSize[0]
								 &&j + yOffset >= 0 && j + yOffset < boardSize[1]
								 && buttons[i + xOffset][j+yOffset] != null 
								 && buttons[i + xOffset][j+yOffset] instanceof Mine){
									numSurroundingMines++;
								}
							}
						}
					}
					//If there are no mines surrounding the tile, then it is a
					//blank square tile
					if(numSurroundingMines == 0){
						BlankSquareButton currentButton = 
								new BlankSquareButton(new Point(i,j));
						buttons[i][j] = currentButton;
						//Add the mouse listener to the button
						currentButton.addMouseListener(new MouseAdapter(){
							@Override
							public void mousePressed(MouseEvent e){
								switch(e.getButton()){
								//Flag the button if you right click
								case(MouseEvent.BUTTON3):
									actionOnBlankButtonClick(
											true,currentButton);
									break;
								//Otherwise, simply click the button
								default:
									actionOnBlankButtonClick(
											false,currentButton);
									break;
							}
							}
						});
						
					}else{
						//If it is a number button (thus has mines surrounding
						//it)
						NumberButton currentButton = 
								new NumberButton(numSurroundingMines,
								new Point(i,j));
						
						buttons[i][j] = currentButton;
						//Add the mouse listener to the button
						currentButton.addMouseListener(new MouseAdapter(){
							@Override
							public void mousePressed(MouseEvent e){
								switch(e.getButton()){
								//If the button is a right click, then simply
								//flag the button
								case(MouseEvent.BUTTON3):
									actionOnNumberButtonClick(
											true,currentButton);
									break;
								//If the button is a left click, then reveal
								//the number
								default:
									actionOnNumberButtonClick(
											false,currentButton);
									break;
								}
							}
								
						});
						
						
					}
					
				}
			}
		}
	}
	
	public JLabel getElapsedTimeLabel(){
		return this.elapsedTime;
	}
	
	public void setElapsedTimeLabel(JLabel elapsedTimeLabel){
		this.elapsedTime = elapsedTimeLabel;
	}
	//This function adds which buttons are nearby to a button to speed up
	//the revelation of blank squares, and so the tile can calculate how many
	//mines surround it 
	private void linkAdjacentSquares(MineButton currentButton){
		int x = currentButton.getLocation().x;
		int y = currentButton.getLocation().y;
		//Looks at all squares near the button
		for(int xOffset = -1; xOffset < 2 ; xOffset++){
			for(int yOffset = -1; yOffset < 2 ; yOffset++){
				//Check to see that the button is not out of the bounds of the
				//board
				if(!(xOffset + x < 0||xOffset + x >= boardSize[0]) &&
						   !(yOffset + y < 0||yOffset + y >= boardSize[1])&&
						   (yOffset!=0||xOffset!=0)){
					//If the button is not outside the bounds of the board
					//then add it to the linkedlist of surrounding squares
					MineButton nearbyButton = buttons[x+xOffset][y+yOffset];
					currentButton.addToSurroundingSquares(nearbyButton);
				}
			}
		}
	}
	//Change whether a square is flagged or not. Also, each flagging event
	//triggers a check to see whether the user has solved the game yet.
	private void changeFlagged(MineButton buttonToBeFlagged){
		if(!buttonToBeFlagged.isFlagged()){
			//Sets the button to be flagged.
			buttonToBeFlagged.setFlagged(true);
			//Reads the image data.
			Image img;
			try {
				//Reads the image data and sets the image icon of the button
				//to be that of a flag.
				img = ImageIO.read(
				getClass().getResource("flag.png"));
				ImageIcon flagImg = new ImageIcon(img);
				buttonToBeFlagged.setIcon(flagImg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//Check to see if the game is solved or not
			if(checkGameSolved()){
				//Tell the user that they won
				JOptionPane.showMessageDialog(this, 
						"Congratulations you won!", 
						"Congratulations!", 
						JOptionPane.PLAIN_MESSAGE);
				HighScores checkScores = new HighScores();
				//Check the high scores to see if their new score beats the 
				//previous scores
				if(checkScores.isNewHighScore(timeSinceGameStarted)){
					//Prompts the user to enter their username
					String s = (String)JOptionPane.showInputDialog(""
							+ "Please input a valid name for your high score.");
					//Checks to see if the username is valid
					while(!HighScores.isValidInput(s)){
						s = (String)JOptionPane.showInputDialog(
								"Please input a valid name");
					}
					//If there is no error with adding the high score, then add
					//it to the high score
					if(!checkScores.addToHighScores(
							new Score(s,timeSinceGameStarted))){
						JOptionPane.showMessageDialog(this, 
								"Congratulations you won!", 
								"Congratulations!", 
								JOptionPane.PLAIN_MESSAGE);
						
					}
					
				}//If it was not a high score, then alert the user of this.
				else{
					JOptionPane.showMessageDialog(this, 
							"Great effort, however you did not make the "
									+ "high score list. Better luck next time",
							"We're sorry",
							JOptionPane.PLAIN_MESSAGE);
				}
				//This reveals all of the mines in the board.
				clickedMine();
			}
		}else{
			buttonToBeFlagged.setIcon(null);
			buttonToBeFlagged.setFlagged(false);
		}
	}
	
	public void addToHighScore(){
		//Show that the user won
		JOptionPane.showMessageDialog(this, 
				"Congratulations you won!", 
				"Congratulations!", 
				JOptionPane.PLAIN_MESSAGE);
		
		HighScores checkScores = new HighScores();
		//Check to see if the score the user recieved was a new high score
		if(checkScores.isNewHighScore(timeSinceGameStarted)){
			String s = (String)JOptionPane.showInputDialog("hello");
			while(!HighScores.isValidInput(s)){
				s = (String)JOptionPane.showInputDialog(
						"Please input a valid name for the scoreboard."
						+ "A valid name is one that does not have any spaces"
						+ "or numeric characters");
			}//If the score is a new high score then tell the user
			if(!checkScores.addToHighScores(
					new Score(s,timeSinceGameStarted))){
				JOptionPane.showMessageDialog(this, 
						"Congratulations you won!", 
						"Congratulations!", 
						JOptionPane.PLAIN_MESSAGE);
			}
			
		}//If the score is not a new high score, then tell the user
		else{
			JOptionPane.showMessageDialog(this, 
					"Great effort, however you did not make the "
							+ "high score list. Better luck next time",
					"We're sorry",
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	//Checks if the game is solved after a button has been flagged
	private boolean checkGameSolved(){
		boolean isSolved = false;
		//Go through all of the mine locations
		for(Point mine : mineLocations){
			int x = mine.x;
			int y = mine.y;
			//Check if hte mine is flagged
			if(buttons[x][y].isFlagged()){
				isSolved = true;
			}//If not all of the mines are flagged, then the user has not won
			else{
				isSolved = false;
				break;
			}
		}//If the user has flagged all of the mines, then check to see that
		//only the squares that are mines are flagged
		if(isSolved){
			//Go through all of the buttons on the board
			for(int x = 0 ; x < boardSize[0]; x++){
				for(int y = 0 ; y < boardSize[1]; y++){
					//Get the button
					MineButton currentButton = buttons[x][y];
					//Check to see if the button is flagged
					if(currentButton.isFlagged()){
						if(mineLocations.contains(currentButton.getLocation())){
							isSolved = true;
						}//If you have any button that is not flagged then the
						//user has a button flagged that is not a mine
						else{
							isSolved = false;
							break;
						}
					}
				}
			}
		}
		return isSolved;
	}
	//If a blank square is clicked, then click all of the blank squares nearby
	private void toggleNearbySquares(MineButton buttonClicked){
		LinkedList<MineButton> nearbySquares = 
				buttonClicked.surroundingSquares;
		Iterator<MineButton> squareIterator = nearbySquares.iterator();
		//Check all of the squares adjacent to the square
		while(squareIterator.hasNext()){
			MineButton currentNeighbor = squareIterator.next();
			//Check to see if the surrounding square is a blank square
			//if so, then click it, and then toggle all squares adjacent to it.
			if(currentNeighbor instanceof BlankSquareButton){
				if(!currentNeighbor.isClicked()){
					currentNeighbor.setClicked(true);
					toggleNearbySquares(currentNeighbor);
					currentNeighbor.onClicked();
				}
			}//Check to see if the surrounding squares are numbers, if so
			//click them
			else if(currentNeighbor instanceof NumberButton){
				if(!currentNeighbor.isClicked()){
					currentNeighbor.onClicked();
					currentNeighbor.setClicked(true);
				}
			}
		}
	}
	//User clicked the mine, this function ends the game
	private void clickedMine(){
		lostGame = true;
		try{
			//Stops the timer from continuing to run
			if(globalTimer==null){
			}else{
				globalTimer.shutdown();
			}
			//Read the image of hte mine
			Image img = ImageIO.read(
					getClass().getResource("mine.png"));
			ImageIcon imgIcon = new ImageIcon(img);
			for(int i = 0 ; i < boardSize[0] ; i ++){
				for(int j = 0 ; j < boardSize[1] ; j ++){
					MineButton currentButton = buttons[i][j];
					//Check to see if a button is a mine, if so then set the
					//icon of the button to that of a mine
					if(currentButton instanceof Mine){
						currentButton.setIcon(null);
						currentButton.setIcon(imgIcon);
					}
					currentButton.setEnabled(false);
				}
			}
		}catch(IOException e){
			System.out.println("Could not read image");
		}
	}
}

