package nl.kvtulder.trivia;

import java.io.Serializable;

public class GameObject implements Serializable {

    // initial game conditions
    private int lives = 5;
    private int score = 0;
    private int completedQuestions = 0;
    private int correctQuestions = 0;
    private Boolean gameOver = false;

    // checks if the provided answer is correct and updates the score & lives
    public Boolean checkAnswer(QuestionObject question, String answer){

        completedQuestions++;

        // check if correct
        if(question.getAnwser().equals(answer)){
            correctQuestions++;
            addScore(question.getDifficulty());
            return true;
        }
        else {
            depleteLive();
            return false;
        }
    }

    // handles a wrong answer
    private void depleteLive(){
        lives--;
        if(lives<=0)
            gameOver = true;
    }

    // handles a correct question
    private void addScore(int points){
        if(!gameOver)
            score += points;
    }

    // getters:
    public int getScore(){
        return score;
    }

    public int getCompletedQuestions(){
        return completedQuestions;
    }

    public int getLives(){
        return lives;
    }

    public Boolean getGameOver(){
        return gameOver;
    }
    
    public int getCorrectQuestions() {
        return correctQuestions;
    }
}
