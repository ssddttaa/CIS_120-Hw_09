

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
//Parent class of all of the buttons
@SuppressWarnings("serial")
public abstract class MineButton extends JButton{
	boolean isFlagged = false;

	boolean isClicked = false;
	
	//Stores which squares surround the button
	public LinkedList<MineButton> surroundingSquares;
	final Point location;

	//Each button performs a different action when clicked, therefore should
	//be abstract
	public abstract void onClicked();
	
	//Initialize the button with a location
	public MineButton(Point location){
		super();
		this.location = location;
		this.surroundingSquares = new LinkedList<MineButton>();
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}
	
	//General appearance for a clicked button
	public void changeToClickedAppearance(){
		this.setBorder(null);
		this.setClicked(true);
	}
	public Point getLocation() {
		return location;
	}
	public void addToSurroundingSquares(MineButton nearbyButton){
		surroundingSquares.add(nearbyButton);
	}
	
}
