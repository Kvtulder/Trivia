package nl.kvtulder.trivia;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TriviaHelper{

    private static final String API_LINK_RANDOM = "http://jservice.io/api/random";
    private static final String API_LINK_CATEGORY = "http://jservice.io/api/clues?category=";


    RequestQueue requestQueue;
    Context context;
    Callback activity;
    QuestionObject questionObject;

    public TriviaHelper(Context context,Callback activity){
        this.context = context;
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(context);
    }

    public interface Callback{
        void gotQuestion(QuestionObject questionObject);
        void gotQuestionError(String message);
        void gotAnswers(List<String> answers);
        void gotAnswerError(String message);
    }

    public void getQuestion(){

        QuestionResponseListener responseListener = new QuestionResponseListener();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(API_LINK_RANDOM,responseListener,responseListener);
        requestQueue.add(jsonObjectRequest);
    }


    public class QuestionResponseListener implements Response.Listener<JSONArray>,Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            activity.gotQuestionError(error.getMessage());
        }

        @Override
        public void onResponse(JSONArray response) {

            Log.e("trivia",response.toString());
            // parse JSON
            try {
                JSONObject object = (JSONObject) response.get(0);
                int id = object.getInt("id");
                String anwser = object.getString("answer");
                String question = object.getString("question");
                int difficulty = object.getInt("value");
                String category = object.getJSONObject("category").getString("title");
                int categoryId = object.getJSONObject("category").getInt("id");

                questionObject = new QuestionObject(id,difficulty,category,categoryId,question,anwser);

                activity.gotQuestion(questionObject);

                // download 3 possible answers
                AnswerResponseListener answerResponseListener = new AnswerResponseListener(questionObject);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(API_LINK_CATEGORY + questionObject.getCategoryId(),answerResponseListener,answerResponseListener);
                requestQueue.add(jsonArrayRequest   );



            } catch (JSONException e) {
                activity.gotQuestionError("Server returned unexpected output");
                Log.e("Trivia",e.toString());
            }
        }
    }

    public class AnswerResponseListener implements Response.Listener<JSONArray>,Response.ErrorListener{

        QuestionObject questionObject;

        public AnswerResponseListener(QuestionObject questionObject){
            this.questionObject = questionObject;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("asd",error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {

            List<String> answers = new ArrayList<>();
            List<Integer> loadedAnswers = new ArrayList<>();
            answers.add(questionObject.getAnwser());
            loadedAnswers.add(questionObject.getId());

            Random random = new Random();
            for(int i = 0;i < 3;i++) {
                try {
                    JSONObject object;
                    int randomInt;
                    do{
                        // get random answer
                        object = (JSONObject) response.get(random.nextInt(response.length()));

                        // check if the random answer isn't already given

                    }
                    while (loadedAnswers.contains(object.getInt("id")) && response.length() >= 4);

                    loadedAnswers.add(object.getInt("id"));
                    String answer = object.getString("answer");
                    answers.add(answer);

                } catch (JSONException e) {
                activity.gotAnswerError(e.toString());
                }
            }

            Collections.shuffle(answers);
            activity.gotAnswers(answers);

        }
    }
}
