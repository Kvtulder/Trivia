package nl.kvtulder.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // retrieve the highscores
        HighScoreHelper helper = HighScoreHelper.getInstance();
        helper.getHighScores(new HighscoreCallback());
    }

    public class HighscoreCallback implements HighScoreHelper.Callback{
        @Override
        public void gotHighScores(List<HighScoreObject> highScoreObjectList) {
            // fill the listview
            ListView listView = findViewById(R.id.highscores);
            // set header
            ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.highscore_item,listView,false);
            listView.addHeaderView(header);
            listView.setAdapter(new HighScoreAdapter(getApplicationContext(),highScoreObjectList));
        }

        // handle error
        @Override
        public void gotHighScoresError(String message) {
            Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
