package nl.kvtulder.trivia;

import java.io.Serializable;

public class QuestionObject implements Serializable {

    private int difficulty;
    private int id;
    private String category;
    private String question;
    private String anwser;
    private int categoryId;

    public QuestionObject(int id,int difficulty, String category, int categoryId, String question, String anwser) {
        this.id = id;
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.anwser = anwser;
        this.categoryId = categoryId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnwser() {
        return anwser;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getId() {
        return id;
    }
}
