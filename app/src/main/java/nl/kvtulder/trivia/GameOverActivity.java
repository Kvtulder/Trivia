package nl.kvtulder.trivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    GameObject game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // retrieve the played game
        Intent intent = getIntent();
        game = (GameObject) intent.getSerializableExtra("game");

        // fill all the textviews
        TextView score = findViewById(R.id.scoreText);
        score.setText("Score: " + game.getScore());

        TextView questionsAnsweredText = findViewById(R.id.questionsAnsweredText);
        questionsAnsweredText.setText("Questions answered: " + game.getCorrectQuestions() + "/" + game.getCompletedQuestions());

        // set the button listeners
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new menuButtonClickListener());

        Button submitHighscoreButton = findViewById(R.id.submitHighscoreButton);
        submitHighscoreButton.setOnClickListener(new HighScoreButtonClickListener());

        Button viewHighscoresButton = findViewById(R.id.viewHighscoresButton);
        viewHighscoresButton.setOnClickListener(new ViewHighScoresButtonClickListener());
    }

    public class menuButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            // return to menu -> close activity
            finish();
        }
    }

    public class HighScoreButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            // show alert dialog to ask for a username
            AlertDialog.Builder alert = new AlertDialog.Builder(GameOverActivity.this);
            alert.setTitle("Enter a username");
            alert.setMessage("Enter your username to sumbit your score to the leaderboard!");
            EditText editText = new EditText(getApplicationContext());
            alert.setView(editText);
            alert.setPositiveButton("Sumbit", new AlertConfirmListener(editText));
            alert.setNegativeButton("Cancel",null);
            alert.show();
        }
    }

    public class AlertConfirmListener implements DialogInterface.OnClickListener{

        EditText editText;

        public AlertConfirmListener(EditText editText){
            this.editText = editText;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            HighScoreHelper highScoreHelper = HighScoreHelper.getInstance();
            highScoreHelper.addHighScore(new HighScoreObject(game.getScore(),editText.getText().toString()));
            // redirect user to view his highscore
            Intent intent = new Intent(GameOverActivity.this,HighScoreActivity.class);
            startActivity(intent);
            // close activity so user can only enter his highscore once
            finish();

        }
    }

    public class ViewHighScoresButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            // goto highscores activity
            Intent intent = new Intent(GameOverActivity.this,HighScoreActivity.class);
            startActivity(intent);
            // close the activity so the user returns to the main menu after closing highscore view
            finish();
        }
    }
}
