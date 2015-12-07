package MineButton;

import java.awt.Point;

@SuppressWarnings("serial")
public class Mine extends MineButton{

	public Mine(Point location){
		super(location);
	}
	@Override
	public void onClicked() {
		// TODO Auto-generated method stub
//		changeToClickedAppearance();
		setClicked(true);
	}

}
