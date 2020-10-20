package TestMaker.MainProgramWindow.Panes.TestsPane;

import java.util.ArrayList;

public class Question {
    private String questionType;
    private String questionText;
    private ArrayList<String> answerVariants;
    private ArrayList<String> severalCorrectAnswers;

    public Question() {
    }

    public Question(String questionType, String questionText, ArrayList<String> answerVariants, ArrayList<String> severalCorrectAnswers) {
        this.questionType = questionType;
        this.questionText = questionText;
        this.answerVariants = answerVariants;
        this.severalCorrectAnswers = severalCorrectAnswers;
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

    public ArrayList<String> getAnswerVariants() {
        return answerVariants;
    }

    public ArrayList<String> getSeveralCorrectAnswers() {
        return severalCorrectAnswers;
    }

    public String getCorrectAnswer() {
        return severalCorrectAnswers.get(0);
    }

    public void setSeveralCorrectAnswers(ArrayList<String> severalCorrectAnswers) {
        this.severalCorrectAnswers = severalCorrectAnswers;
    }

    public void setAnswerVariants(ArrayList<String> answerVariants) {
        this.answerVariants = answerVariants;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "Question: "+questionText+"\nQuestion type: "+questionType;
    }
}
