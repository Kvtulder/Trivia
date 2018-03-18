package nl.kvtulder.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton = findViewById(R.id.startGame);
        Button quitButton = findViewById(R.id.quitButton);
        Button highscoreButton = findViewById(R.id.highscoreButton);

        startGameButton.setOnClickListener(new StartGameButtonListener());
        quitButton.setOnClickListener(new QuitButtonListener());
        highscoreButton.setOnClickListener(new HighscoreButtonClickListener());

    }

    public class StartGameButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            GameObject game = new GameObject();
            intent.putExtra("game",game);
            startActivity(intent);
        }
    }

    public class QuitButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            // quit game: close activity
            finish();
        }
    }

    public class HighscoreButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,HighScoreActivity.class);
            startActivity(intent);
        }
    }
}
