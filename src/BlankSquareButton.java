

import java.awt.Point;
//Class to represent the blank square
@SuppressWarnings("serial")
public class BlankSquareButton extends MineButton{
	public BlankSquareButton(Point location){
		super(location);
	}
	@Override
	public void onClicked() {
		changeToClickedAppearance();
		this.setClicked(true);
	}
}
