import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;
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
	private boolean debugMode = false;
	boolean startedPlaying = false; 
	boolean lostGame = false;
	
	private JLabel elapsedTime;
	public int timeSinceGameStarted = 0;

	public ScheduledExecutorService globalTimer;
	
	private LinkedList<Point> mineLocations;
	
 	public MinesweeperPanel(JLabel player, MinesweeperDifficulty diff){
		super(new GridLayout(diff.getSize()[0],diff.getSize()[1]));
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
				if(currentButton == null){
//					System.out.println("no mine at:"+(new Point(i,j).toString()));
				}
			}
		}
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
//			System.out.println("added mine at location:"+newMine.getLocation().toString());
			buttons[randX][randY] = newMine;
			newMine.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
//					clickedMine(boardSize);
//					newMine.onClicked();
				}
			});
			
			newMine.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					if(startedPlaying&&!lostGame){
						if(e.getButton() == MouseEvent.BUTTON3){
							if(!newMine.isClicked()){
								changeFlagged(newMine);
							}
						}else if(!newMine.isFlagged()){
							newMine.onClicked();
							newMine.setFlagged(true);
							changeFlagged(newMine);
							clickedMine();
						}
					}else if(!startedPlaying&&!lostGame){
						startGame();
						mousePressed(e);
					}
				}
				
			});
			
		}
	}
	
	private void startGame(){
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
//						System.out.println("added blank square at location:"+currentButton.getLocation().toString());
						currentButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								
								/*currentButton.onClicked();
								toggleNearbySquares(currentButton);*/
							}
						});
						
						currentButton.addMouseListener(new MouseAdapter(){
							@Override
							public void mousePressed(MouseEvent e){
								if(startedPlaying&&!lostGame){
									if(e.getButton() == MouseEvent.BUTTON3){
										if(!currentButton.isClicked()){
											changeFlagged(currentButton);
										}
									}else if(!currentButton.isFlagged()
											&&!currentButton.isClicked()){
										toggleNearbySquares(currentButton);
										currentButton.setClicked(true);
										currentButton.onClicked();
									}
								}else if(!startedPlaying&&!lostGame){
									startGame();
									mousePressed(e);
								}
							}
						});
						
					}else{
						NumberButton currentButton = 
								new NumberButton(numSurroundingMines,
								new Point(i,j));
//						System.out.println("added number button at location:"+currentButton.getLocation().toString());
						buttons[i][j] = currentButton;
						currentButton.addActionListener(new ActionListener(){
	
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
//								currentButton.onClicked();
							}
						});
						
						currentButton.addMouseListener(new MouseAdapter(){
							@Override
							public void mousePressed(MouseEvent e){
								if(startedPlaying&&!lostGame){
									if(e.getButton() == MouseEvent.BUTTON3){
										if(!currentButton.isClicked()){
											changeFlagged(currentButton);
										}
									}else if(!currentButton.isFlagged()){
										currentButton.onClicked();
										currentButton.setFlagged(true);
										changeFlagged(currentButton);
									}
								}else if(!startedPlaying&&!lostGame){
									startGame();
									mousePressed(e);
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
				JOptionPane.showMessageDialog(this, "Congratulations you won!", "Congratulations!", JOptionPane.PLAIN_MESSAGE);;
				clickedMine();
			}
		}else{
			buttonToBeFlagged.setIcon(null);
			buttonToBeFlagged.setFlagged(false);
		}
	}
	
	private boolean checkGameSolved(){
		boolean isSolved = false;
		System.out.println("checking game");
		for(Point mine : mineLocations){
			int x = mine.x;
			int y = mine.y;
			System.out.println("mine location:"+mine.toString());
			if(buttons[x][y].isFlagged()){
				isSolved = true;
			}else{
				System.out.println("failed at:"+
						buttons[y][x].getLocation().toString());
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
							System.out.println("failed at:"+
									currentButton.getLocation().toString());
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
//					System.out.println("calling function with blank at this location:" + currentNeighbor.getLocation().toString());
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

