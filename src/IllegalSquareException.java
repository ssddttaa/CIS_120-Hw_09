

@SuppressWarnings("serial")
//Throws an exception if a square possesses incorrect properties.
public class IllegalSquareException extends Exception {
	public IllegalSquareException(String msg){
		System.out.println("Illegal Square Exception: " + msg);
	}
}
