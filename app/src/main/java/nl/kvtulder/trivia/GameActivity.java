package nl.kvtulder.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback{

    TextView question;
    TextView livesText;
    TextView scoreText;
    GameObject game;
    QuestionObject questionObject;
    List<String> answersList = new ArrayList<>();
    Boolean canAnswer = false;
    final static int[] buttons = {R.id.button1,R.id.button2,R.id.button3,R.id.button4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // load views
        question = findViewById(R.id.question);
        livesText = findViewById(R.id.livesText);
        scoreText = findViewById(R.id.scoreText);

        Intent intent = getIntent();
        game = (GameObject) intent.getSerializableExtra("game");

        // check if there is a running game
        if(savedInstanceState == null)
            loadQuestion();
        else {
            // retrieve question and answers
            questionObject = (QuestionObject) savedInstanceState.getSerializable("currentQuestion");
            question.setText(questionObject.getQuestion());
            answersList = savedInstanceState.getStringArrayList("answers");
            updateButtons();
        }
        updateUI();
    }

    // restore the buttons on post resume
    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateButtons();
    }

    // updates the score/lives text, and checks if the game is over
    public void updateUI(){

        if(game.getGameOver()){
            // game over :( close the activity and show the end screen
            Intent intent = new Intent(this,GameOverActivity.class);
            intent.putExtra("game",game);
            this.finish();
            startActivity(intent);
        }

        livesText.setText(String.valueOf(game.getLives()));
        scoreText.setText(String.valueOf(game.getScore()));
    }

    // updates the buttons and resets the colour
    public void updateButtons(){

        if(!answersList.isEmpty())
            canAnswer = true;
        
        for(int i = 0;i < buttons.length && i < answersList.size();i++){
            Button button = findViewById(buttons[i]);
            button.setText(answersList.get(i));
            button.setOnClickListener(new AnswerButtonOnClickListener());
            button.setBackgroundResource(android.R.drawable.btn_default);
        }
    }


    // save the current question and answers
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentQuestion",questionObject);
        outState.putStringArrayList("answers", (ArrayList<String>) answersList);
    }

    // load a new question
    public void loadQuestion(){
        TriviaHelper triviaHelper = new TriviaHelper(this,this);
        triviaHelper.getQuestion();
    }

    // callback for the loadquestion function
    @Override
    public void gotQuestion(QuestionObject questionObject) {
        this.questionObject = questionObject;
        question.setText(questionObject.getQuestion());
    }

    // handle error
    @Override
    public void gotQuestionError(String message) {
        // error :( probably a null value in the questionobject, load another one
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
        loadQuestion();

    }

    @Override
    public void gotAnswers(List<String> answers) {
        answersList = answers;
        canAnswer = true;
        updateButtons();
    }

    @Override
    public void gotAnswerError(String message) {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
        loadQuestion();
    }

    public class AnswerButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String answer = ((Button) view).getText().toString();

            // check if player didn't already answer and make sure it can't answer again
            if(!canAnswer)
                return;
            else
                canAnswer = false;

            // show the answer by coloring the buttons
            if(game.checkAnswer(questionObject,answer)){
                ((Button) view).setBackgroundColor(Color.GREEN);
            }
            else
            {
                // wrong answer! make the pressed button red
                ((Button) view).setBackgroundColor(Color.RED);

                // and the right button green
                int[] buttons = {R.id.button1,R.id.button2,R.id.button3,R.id.button4};
                for(int buttonId : buttons){
                    Button button = findViewById(buttonId);
                    if(button.getText().equals(questionObject.getAnwser())) {
                        button.setBackgroundColor(Color.GREEN);
                        break;
                    }
                }

            }
            // update the interface
            updateUI();
            if(!game.getGameOver())
                loadQuestion();
        }
    }

}
