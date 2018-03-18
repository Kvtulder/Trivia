package nl.kvtulder.trivia;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HighScoreHelper {

    public static HighScoreHelper instance;
    FirebaseDatabase database;

    public static HighScoreHelper getInstance(){
        // make the class a singleton
        if(instance==null)
            instance = new HighScoreHelper();

        return instance;
    }

    // create interface to force to implentate callback functions
    public interface Callback{
        void gotHighScores(List<HighScoreObject> highScoreObjectList);
        void gotHighScoresError(String message);
    }

    private HighScoreHelper(){
        // initialize the database
        database = FirebaseDatabase.getInstance();
        database.setLogLevel(Logger.Level.DEBUG);
    }

    public void getHighScores(Callback activity){
        // get high scores
        DatabaseReference myRef = database.getReference("highscores");
        Query ordered = myRef.orderByChild("highscore");
        ordered.addValueEventListener(new DataValueEventListener(activity));
    }

    public  void addHighScore(HighScoreObject highScoreObject){
        // push new highscore
        DatabaseReference myRef = database.getReference("highscores");
        DatabaseReference score = myRef.push();
        score.setValue(highScoreObject);
    }

    public class DataValueEventListener implements com.google.firebase.database.ValueEventListener{

        Callback activity;

        public DataValueEventListener(Callback activity){
            this.activity = activity;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            List<HighScoreObject> highscores = new ArrayList<>();
            for(DataSnapshot child : dataSnapshot.getChildren()){
                HighScoreObject highScoreObject = child.getValue(HighScoreObject.class);
                highscores.add(highScoreObject);
            }
            // highscores are ordered from small to big, so we need to reverse that
            Collections.reverse(highscores);
            activity.gotHighScores(highscores);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            activity.gotHighScoresError(databaseError.getMessage());
        }
    }
}
