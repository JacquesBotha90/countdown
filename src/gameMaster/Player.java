package gameMaster;

/**
 * Simple class to store player details.
 * @author BothaJ
 *
 */
public class Player {
  
  public String name;
  private int score; 
  
  public Player(String playerName){
    this.name = playerName;
    this.score = 0;
  }
  
  public void addPoints(int points){
    score += points;
  }
  
  public int getScore(){
    return score;
  }

}
