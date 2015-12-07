package MineButton;

import java.awt.Point;
import java.util.Iterator;

@SuppressWarnings("serial")
public class NumberButton extends MineButton{
	
	final int numberSurroundingMines;
	
	public NumberButton(int numberSurroundingMines, Point location) {
		super(location);
		this.numberSurroundingMines = numberSurroundingMines;
	}
	
	public int findNumSurroundingMines() throws IllegalSquareException{
		int numMines = 0;
		Iterator<MineButton> minesIterator = surroundingSquares.iterator();
		while(minesIterator.hasNext()){
			if(minesIterator.next() instanceof Mine){
				numMines++;
			}
		}
		if(numMines == 0){
			throw new IllegalSquareException
			("Non blank square has 0 surrounding mine neighbors");
		}
		return numMines;
	}
	@Override
	public void onClicked() {
		// TODO Auto-generated method stub
		changeToClickedAppearance();
		this.setText(Integer.toString(this.numberSurroundingMines));
		setClicked(true);
	}
	
	public int getNumSurroundingMines(){
		return this.numberSurroundingMines;
	}
	
}
