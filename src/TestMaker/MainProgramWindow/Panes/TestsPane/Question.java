package TestMaker.MainProgramWindow.Panes.TestsPane;

import java.util.ArrayList;

public class Question {
    private String questionType;
    private String questionText;
    private ArrayList<String> questionVariants;
    private ArrayList<String> answerVariants;
    private Double questionScore;

    public Question() {
    }

    public Question(String questionType, Double questionScore, String questionText, ArrayList<String> questionVariants, ArrayList<String> answerVariants) {
        this.questionType = questionType;
        this.questionScore = questionScore;
        this.questionText = questionText;
        this.questionVariants = questionVariants;
        this.answerVariants = answerVariants;
    }

    public Double getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Double questionScore) {
        this.questionScore = questionScore;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getQuestionVariants() {
        return questionVariants;
    }

    public ArrayList<String> getAnswerVariants() {
        return answerVariants;
    }

    public String getCorrectAnswer() {
        return answerVariants.get(0);
    }

    public void setAnswerVariants(ArrayList<String> answerVariants) {
        this.answerVariants = answerVariants;
    }

    public void setQuestionVariants(ArrayList<String> questionVariants) {
        this.questionVariants = questionVariants;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "Question: "+questionText;
    }
}
