
//Enum to store the size of a board of a certain difficulty
public enum MinesweeperDifficulty {
	BEGINNER(8,8),
	INTERMEDIATE(16,16),
	EXPERT(30,16);
	
	
	private final int[] size;
	
	private MinesweeperDifficulty(int width, int height){
		this.size = new int[]{width,height};
	}
	
	public int[] getSize(){
		return this.size;
	}
}
