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
	private ArrayList<Score> scores;
	public HighScores(){
		 this.scores = new ArrayList<Score>();
		 this.retrieveScoresFromFile();
	}
	public ArrayList<Score> getScores(){
		return this.scores;
	}
	private void retrieveScoresFromFile(){
		try{
			File highScoresFile = new File(HIGH_SCORES_FILE);
			if(!highScoresFile.exists()){
				highScoresFile.createNewFile();
			}else{
				FileReader r = new FileReader(HIGH_SCORES_FILE);
				BufferedReader br = new BufferedReader(r);
				String currentLine;
				while((currentLine = br.readLine())!=null){
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
	public boolean isNewHighScore(int newScore){
		if(scores.size() < 10){
			return true;
		}
		for (int i = 0; i < scores.size(); i++) {
			if(newScore >= scores.get(i).score || scores.size() < 10){
				if(!(i == 9 && newScore == scores.get(i).score)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean addToHighScores(Score newScore){
		if(isNewHighScore(newScore.score)&&isValidInput(newScore.name)){
			try {
				
				FileReader r = new FileReader(HIGH_SCORES_FILE);
				BufferedReader br = new BufferedReader(r);
				String output = "";
				String currentLine;
				boolean alreadyAdded = false;
				while((currentLine = br.readLine())!=null){
					String currName = currentLine;
					currentLine = br.readLine();
					int currentScore = Integer.parseInt(currentLine);
					if(newScore.score <= currentScore&&!alreadyAdded){
						output+=newScore.name + "\n";
						output+=newScore.score+"\n";
						alreadyAdded = true;
					}
					output+=currName+"\n";
					output+=currentLine+"\n";
				}
				if(scores.size() < 10&&!alreadyAdded){
					output+=newScore.name + "\n";
					output+=newScore.score+"\n";
				}
				br.close();
				
				
				OutputStream outputStream = 
						new FileOutputStream(HIGH_SCORES_FILE);
				Writer outputWriter = new OutputStreamWriter(outputStream);
				outputWriter.write(output);
				outputWriter.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("file not found exception");
				return false;
			}catch (IOException e) {
				System.out.println("io exception");
				// TODO Auto-generated catch block
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	public static boolean isValidInput(String input){
		if(input.length() == 0)
			return false;
		for(char a : input.toCharArray()){
			if(!(Character.isAlphabetic(a)||Character.isDigit(a))){
				return false;
			}
		}
		return true;
	}
	
	public static String getHighscoresfilename() {
		return HIGH_SCORES_FILE;
	}
}
