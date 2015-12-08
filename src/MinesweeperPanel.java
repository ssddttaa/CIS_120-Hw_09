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

import MineButton.BlankSquareButton;
import MineButton.Mine;
import MineButton.MineButton;
import MineButton.NumberButton;

@SuppressWarnings("serial")
public class MinesweeperPanel extends JPanel{
	private final int numMines;
	private MineButton[][] buttons;
	private final int boardSize[];
	private boolean debugMode = true;
	public Boolean startedPlaying = false; 
	public Boolean lostGame = false;
	
	private JLabel elapsedTime;
	public int timeSinceGameStarted = 0;

	public ScheduledExecutorService globalTimer;
	
	private LinkedList<Point> mineLocations;
	
	private boolean isAIPanel;
	
 	public MinesweeperPanel
 		(JLabel player, MinesweeperDifficulty diff, boolean isAIPanel){
		super(new GridLayout(diff.getSize()[0],diff.getSize()[1]));
		this.isAIPanel = isAIPanel;
		this.boardSize = diff.getSize();
		this.mineLocations = new LinkedList<Point>();
		buttons = new MineButton[boardSize[0]][boardSize[1]];
		globalTimer = null;
		this.numMines = getNumMinesFromXSize(boardSize[0]);
		this.addMinesToBoard();
		this.addNonMineSquaresToBoard();
		
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
		if(this.isAIPanel){
			ScheduledExecutorService newExecutor = 
					Executors.newSingleThreadScheduledExecutor();
			newExecutor.scheduleWithFixedDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
				}
				
			}, 0, 1, TimeUnit.SECONDS);
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
	
	private void addMinesToBoard(){
		Random randomGenerator = new Random();
		for(int i = 0 ; i < this.numMines; i++){
			int randX = randomGenerator.nextInt(boardSize[0]);
			int randY = randomGenerator.nextInt(boardSize[1]);
			while(buttons[randX][randY]!=null){
				randX = randomGenerator.nextInt(boardSize[0]);
				randY = randomGenerator.nextInt(boardSize[1]);
			}
			Mine newMine = new Mine(new Point(randX,randY));
			mineLocations.add(newMine.getLocation());
			buttons[randX][randY] = newMine;
			if(!isAIPanel){
				newMine.addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e){
						switch(e.getButton()){
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
	}
	
	
	public void actionOnMineClick(boolean isRightClick, MineButton mine){
		if(startedPlaying&&!lostGame){
			if(isRightClick){
				if(!mine.isClicked()){
					changeFlagged(mine);
				}
			}else if(!mine.isFlagged()){
				mine.onClicked();
				mine.setFlagged(true);
				changeFlagged(mine);
				clickedMine();
			}
		}else if(!startedPlaying&&!lostGame){
			startGame();
			actionOnMineClick(isRightClick,mine);
		}
	}
	
	public void actionOnBlankButtonClick
		(boolean isRightClick, MineButton button){
		if(startedPlaying&&!lostGame){
			if(isRightClick){
				if(!button.isClicked()){
					changeFlagged(button);
				}
			}else if(!button.isFlagged()
					&&!button.isClicked()){
				toggleNearbySquares(button);
				button.setClicked(true);
				button.onClicked();
			}
		}else if(!startedPlaying&&!lostGame){
			startGame();
			actionOnBlankButtonClick(isRightClick,button);
		}
	}
	
	public void actionOnNumberButtonClick
	(boolean isRightClick, MineButton button){
		if(startedPlaying&&!lostGame){
			if(isRightClick){
				if(!button.isClicked()){
					changeFlagged(button);
				}
			}else if(!button.isFlagged()){
				button.onClicked();
				button.setFlagged(true);
				changeFlagged(button);
			}
		}else if(!startedPlaying&&!lostGame){
			startGame();
			actionOnNumberButtonClick(isRightClick,button);
		}
	}
	
	public void startGame(){
		if(!startedPlaying){
			startedPlaying = true;
		}

		this.globalTimer = Executors.newSingleThreadScheduledExecutor();
		globalTimer.scheduleWithFixedDelay(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timeSinceGameStarted++;
				elapsedTime.setText("Time Since Game Started:" +
				timeSinceGameStarted);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	private void addNonMineSquaresToBoard(){
		for(int i = 0 ; i < boardSize[0]; i++){
			for(int j = 0 ; j < boardSize[1] ; j++){
				if(buttons[i][j]==null){
					int numSurroundingMines = 0;
					for(int xOffset = -1 ; xOffset < 2; xOffset++){
						for(int yOffset = -1 ; yOffset < 2; yOffset++){
							if(xOffset != 0 || yOffset != 0){
								if(i + xOffset >= 0 && i + xOffset < boardSize[0]
								 &&j + yOffset >= 0 && j + yOffset < boardSize[1]
								 && buttons[i + xOffset][j+yOffset] != null 
								 && buttons[i + xOffset][j+yOffset] instanceof Mine){
									numSurroundingMines++;
								}
							}
						}
					}
					if(numSurroundingMines == 0){
						BlankSquareButton currentButton = 
								new BlankSquareButton(new Point(i,j));
						buttons[i][j] = currentButton;
						if(!isAIPanel){
							currentButton.addMouseListener(new MouseAdapter(){
								@Override
								public void mousePressed(MouseEvent e){
									switch(e.getButton()){
									case(MouseEvent.BUTTON3):
										actionOnBlankButtonClick(
												true,currentButton);
										break;
									default:
										actionOnBlankButtonClick(
												false,currentButton);
										break;
								}
								}
							});
						}
						
					}else{
						NumberButton currentButton = 
								new NumberButton(numSurroundingMines,
								new Point(i,j));
						
						buttons[i][j] = currentButton;
						if(!isAIPanel){
							currentButton.addMouseListener(new MouseAdapter(){
								@Override
								public void mousePressed(MouseEvent e){
									switch(e.getButton()){
									case(MouseEvent.BUTTON3):
										actionOnNumberButtonClick(
												true,currentButton);
										break;
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
	}
	
	public JLabel getElapsedTimeLabel(){
		return this.elapsedTime;
	}
	
	public void setElapsedTimeLabel(JLabel elapsedTimeLabel){
		this.elapsedTime = elapsedTimeLabel;
	}
	
	private void linkAdjacentSquares(MineButton currentButton){
		int x = currentButton.getLocation().x;
		int y = currentButton.getLocation().y;
		for(int xOffset = -1; xOffset < 2 ; xOffset++){
			for(int yOffset = -1; yOffset < 2 ; yOffset++){
				if(!(xOffset + x < 0||xOffset + x >= boardSize[0]) &&
						   !(yOffset + y < 0||yOffset + y >= boardSize[1])&&
						   (yOffset!=0||xOffset!=0)){
					MineButton nearbyButton = buttons[x+xOffset][y+yOffset];
					currentButton.addToSurroundingSquares(nearbyButton);
				}
			}
		}
	}
	
	private void changeFlagged(MineButton buttonToBeFlagged){
		if(!buttonToBeFlagged.isFlagged()){
			buttonToBeFlagged.setFlagged(true);
			Image img;
			try {
				img = ImageIO.read(
				getClass().getResource("resources/flag.png"));
				ImageIcon flagImg = new ImageIcon(img);
				buttonToBeFlagged.setIcon(flagImg);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(checkGameSolved()){
				JOptionPane.showMessageDialog(this, 
						"Congratulations you won!", 
						"Congratulations!", 
						JOptionPane.PLAIN_MESSAGE);
				
				HighScores checkScores = new HighScores();
				if(checkScores.isNewHighScore(timeSinceGameStarted)){
					String s = (String)JOptionPane.showInputDialog("hello");
					while(!HighScores.isValidInput(s)){
						s = (String)JOptionPane.showInputDialog(
								"Please input a valid name");
					}
					if(!checkScores.addToHighScores(
							new Score(s,timeSinceGameStarted))){
						JOptionPane.showMessageDialog(this, 
								"Congratulations you won!", 
								"Congratulations!", 
								JOptionPane.PLAIN_MESSAGE);
						
					}
					
				}else{
					JOptionPane.showMessageDialog(this, 
							"Great effort, however you did not make the "
									+ "high score list. Better luck next time",
							"We're sorry",
							JOptionPane.PLAIN_MESSAGE);
				}
				clickedMine();
			}
		}else{
			buttonToBeFlagged.setIcon(null);
			buttonToBeFlagged.setFlagged(false);
		}
	}
	
	public void addToHighScore(){
		
		JOptionPane.showMessageDialog(this, 
				"Congratulations you won!", 
				"Congratulations!", 
				JOptionPane.PLAIN_MESSAGE);
		
		HighScores checkScores = new HighScores();
		if(checkScores.isNewHighScore(timeSinceGameStarted)){
			String s = (String)JOptionPane.showInputDialog("hello");
			while(!HighScores.isValidInput(s)){
				s = (String)JOptionPane.showInputDialog(
						"Please input a valid name for the scoreboard."
						+ "A valid name is one that does not have any spaces"
						+ "or numeric characters");
			}
			if(!checkScores.addToHighScores(
					new Score(s,timeSinceGameStarted))){
				JOptionPane.showMessageDialog(this, 
						"Congratulations you won!", 
						"Congratulations!", 
						JOptionPane.PLAIN_MESSAGE);
			}
			
		}else{
			JOptionPane.showMessageDialog(this, 
					"Great effort, however you did not make the "
							+ "high score list. Better luck next time",
					"We're sorry",
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private boolean checkGameSolved(){
		boolean isSolved = false;
		for(Point mine : mineLocations){
			int x = mine.x;
			int y = mine.y;
			if(buttons[x][y].isFlagged()){
				isSolved = true;
			}else{
				isSolved = false;
				break;
			}
		}
		if(isSolved){
			for(int x = 0 ; x < boardSize[0]; x++){
				for(int y = 0 ; y < boardSize[1]; y++){
					MineButton currentButton = buttons[x][y];
					if(currentButton.isFlagged()){
						if(mineLocations.contains(currentButton.getLocation())){
							isSolved = true;
						}else{
							isSolved = false;
							break;
						}
					}
				}
			}
		}
		return isSolved;
	}
	
	private void toggleNearbySquares(MineButton buttonClicked){
		LinkedList<MineButton> nearbySquares = 
				buttonClicked.surroundingSquares;
		Iterator<MineButton> squareIterator = nearbySquares.iterator();
		while(squareIterator.hasNext()){
			MineButton currentNeighbor = squareIterator.next();
			
			if(currentNeighbor instanceof BlankSquareButton){
				if(!currentNeighbor.isClicked()){
					currentNeighbor.setClicked(true);
					toggleNearbySquares(currentNeighbor);
					currentNeighbor.onClicked();
				}
			}else if(currentNeighbor instanceof NumberButton){
				if(!currentNeighbor.isClicked()){
					currentNeighbor.onClicked();
					currentNeighbor.setClicked(true);
				}
			}
		}
	}
	
	private void clickedMine(){
		lostGame = true;
		try{
			if(globalTimer==null){
			}else{
				globalTimer.shutdown();
			}
			Image img = ImageIO.read(
					getClass().getResource("resources/mine.png"));
			ImageIcon imgIcon = new ImageIcon(img);
			for(int i = 0 ; i < boardSize[0] ; i ++){
				for(int j = 0 ; j < boardSize[1] ; j ++){
					MineButton currentButton = buttons[i][j];
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

@SuppressWarnings("serial")
class MineException extends Exception{
	public MineException(){
		
	}
	
	public MineException(String msg){
		super(msg);
	}
	
}

