package MineButton;

@SuppressWarnings("serial")
public class IllegalSquareException extends Exception {
	public IllegalSquareException(String msg){
		System.out.println("Illegal Square Exception: " + msg);
	}
}
