package MineButton;

import java.awt.Point;

@SuppressWarnings("serial")
public class BlankSquareButton extends MineButton{
	public BlankSquareButton(Point location){
		super(location);
	}
	@Override
	public void onClicked() {
		// TODO Auto-generated method stub
		changeToClickedAppearance();
		this.setClicked(true);
	}
}
