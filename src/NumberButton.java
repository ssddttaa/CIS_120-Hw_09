

import java.awt.Point;
import java.util.Iterator;

@SuppressWarnings("serial")
public class NumberButton extends MineButton{
	
	final int numberSurroundingMines;
	//Constructor for the number button
	public NumberButton(int numberSurroundingMines, Point location) {
		super(location);
		this.numberSurroundingMines = numberSurroundingMines;
	}
	//Finds the number of mines surrounding the square
	public int findNumSurroundingMines() throws IllegalSquareException{
		int numMines = 0;
		Iterator<MineButton> minesIterator = surroundingSquares.iterator();
		//Check all surrounding squares for a mine, and then increment the 
		//number of mines if it is a mine.
		while(minesIterator.hasNext()){
			if(minesIterator.next() instanceof Mine){
				numMines++;
			}
		}//Check to see that the number square has at least one mine, or else
		//it should be a blank square
		if(numMines == 0){
			throw new IllegalSquareException
			("Non blank square has 0 surrounding mine neighbors");
		}
		return numMines;
	}
	//Change the appearance of the button if it is clicked
	@Override
	public void onClicked() {
		changeToClickedAppearance();
		//Change the text of the button to match how many mines surround it
		this.setText(Integer.toString(this.numberSurroundingMines));
		setClicked(true);
	}
	
	public int getNumSurroundingMines(){
		return this.numberSurroundingMines;
	}
	
}
