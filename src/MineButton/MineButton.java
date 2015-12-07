package MineButton;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;

public abstract class MineButton extends JButton{
	boolean isFlagged = false;

	boolean isClicked = false;
	
	public LinkedList<MineButton> surroundingSquares;
	final Point location;

	public abstract void onClicked();
	
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
