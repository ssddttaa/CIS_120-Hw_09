import javax.swing.JButton;

public class MineButtonOld extends JButton{
	private final boolean isMine;
	private final int surroundingMines;
	private boolean isFlagged = false;
	private boolean isUncovered = false;
	public MineButtonOld(boolean isMine, int surroundingMines){
		super();
		this.isMine = isMine;
		this.surroundingMines = surroundingMines;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}
	public boolean isUncovered() {
		return isUncovered;
	}
	public void setUncovered(boolean isUncovered) {
		this.isUncovered = isUncovered;
	}
	public boolean isMine() {
		return isMine;
	}
	public int getSurroundingMines() {
		return surroundingMines;
	}
}
