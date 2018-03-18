package nl.kvtulder.trivia;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class HighScoreAdapter extends ArrayAdapter<HighScoreObject> {

    List<HighScoreObject> highscores;
    int resource;
    Context context;

    public HighScoreAdapter(@NonNull Context context,int resource, @NonNull List<HighScoreObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        highscores = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // create view if there is none
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(resource,
                    parent, false);
        }

        // fill in the details

        TextView rank = convertView.findViewById(R.id.rankText);
        rank.setTypeface(null, Typeface.NORMAL);
        rank.setText(String.valueOf(position + 1));

        TextView score = convertView.findViewById(R.id.score);
        score.setTypeface(null, Typeface.NORMAL);
        score.setText(String.valueOf(highscores.get(position).getHighscore()));

        TextView username = convertView.findViewById(R.id.username);
        username.setTypeface(null, Typeface.NORMAL);
        username.setText(highscores.get(position).getUsername());

        return convertView;
    }
}
