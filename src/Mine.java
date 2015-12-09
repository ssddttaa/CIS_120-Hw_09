

import java.awt.Point;

@SuppressWarnings("serial")
//Button that represents a mine
public class Mine extends MineButton{

	public Mine(Point location){
		super(location);
	}
	@Override
	public void onClicked() {
		setClicked(true);
	}

}
