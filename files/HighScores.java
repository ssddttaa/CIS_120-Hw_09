import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class HighScores {
	public static final String HIGH_SCORES_FILE = "high_scores.txt";
	//Arraylist to store the scores
	private ArrayList<Score> scores;
	//Retreive all of the scores in the file
	public HighScores(){
		 this.scores = new ArrayList<Score>();
		 this.retrieveScoresFromFile();
	}
	//Returns the array list of scores
	public ArrayList<Score> getScores(){
		return this.scores;
	}
	//Gets the scores from the file
	private void retrieveScoresFromFile(){
		try{
			File highScoresFile = new File(HIGH_SCORES_FILE);
			//Creates new file if the file does not exist
			if(!highScoresFile.exists()){
				highScoresFile.createNewFile();
			}else{
				//Reads the file
				FileReader r = new FileReader(HIGH_SCORES_FILE);
				BufferedReader br = new BufferedReader(r);
				String currentLine;
				while((currentLine = br.readLine())!=null&&scores.size() <= 9){
					//Stores every odd line as a name, and every even line as
					//a number. Only stores the top 10 scores.
					String currentName = currentLine;
					currentLine = br.readLine();
					int currentScore = Integer.parseInt(currentLine);
					Score score = new Score(currentName,currentScore);
					scores.add(score);
				}
				br.close();
			}
		}catch(IOException e){
			System.out.println("io exception");
			e.printStackTrace();
		}
	}
	//Checks the existing database to see if the new score is a high score
	public boolean isNewHighScore(int newScore){
		//Automatically add to the list of high scores if there are not
		//ten high scores yet
		if(scores.size() < 10){
			return true;
		}
		//Otherwise, check to see if the score is larger than any of the
		//existing high scores. If so, then add it.
		for (int i = 0; i < scores.size(); i++) {
			if(newScore >= scores.get(i).score || scores.size() < 10){
				if(!(i == 9 && newScore == scores.get(i).score)){
					return true;
				}
			}
		}
		return false;
	}
	
	//Function that over-writes the file to include the new high score
	public boolean addToHighScores(Score newScore){
		if(isNewHighScore(newScore.score)&&isValidInput(newScore.name)){
			try {
				
				FileReader r = new FileReader(HIGH_SCORES_FILE);
				BufferedReader br = new BufferedReader(r);
				String output = "";
				String currentLine;
				boolean alreadyAdded = false;
				//Reads through the file
				while((currentLine = br.readLine())!=null){
					String currName = currentLine;
					currentLine = br.readLine();
					//Gets the score from the file
					int currentScore = Integer.parseInt(currentLine);
					//Add the score at the location where the new score is
					//better than an existing high score. Only once, however.
					if(newScore.score <= currentScore&&!alreadyAdded){
						output+=newScore.name + "\n";
						output+=newScore.score+"\n";
						alreadyAdded = true;
					}
					//Still append the rest of the data to the file, since we
					//are overwriting the file
					output+=currName+"\n";
					output+=currentLine+"\n";
				}
				if(scores.size() < 10&&!alreadyAdded){
					output+=newScore.name + "\n";
					output+=newScore.score+"\n";
				}
				br.close();
				
				//Print the data to the file.
				OutputStream outputStream = 
						new FileOutputStream(HIGH_SCORES_FILE);
				Writer outputWriter = new OutputStreamWriter(outputStream);
				outputWriter.write(output);
				outputWriter.close();
				return true;
			} catch (FileNotFoundException e) {
				System.out.println("file not found exception");
				return false;
			}catch (IOException e) {
				System.out.println("io exception");
				return false;
			}
		}else{
			return false;
		}
	}
	
	//Checks to see if a string is valid
	public static boolean isValidInput(String input){
		//Makes sure the string is of valid size
		if(input.length() == 0 || input.length()>=20)
			return false;
		for(char a : input.toCharArray()){
			//Makes sure the string only contains valid characters
			if(!(Character.isAlphabetic(a)||Character.isDigit(a))){
				return false;
			}
		}
		return true;
	}
	//Returns name of high scores file
	public static String getHighscoresfilename() {
		return HIGH_SCORES_FILE;
	}
}
