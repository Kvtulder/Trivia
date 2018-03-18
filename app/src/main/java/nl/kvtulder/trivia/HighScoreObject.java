package nl.kvtulder.trivia;


public class HighScoreObject {

    private  int highscore;
    private String username;

    public HighScoreObject(){
        // empty constructor needed for firebase
    }

    public HighScoreObject(int highscore, String username) {
        this.highscore = highscore;
        this.username = username;
    }

    public int getHighscore() {
        return highscore;
    }

    public String getUsername() {
        return username;
    }
}
