package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.TestPassingQuestions.SeveralAnswersTestingQuestion;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;

public class SeveralAnswersTestingQuestion implements QuestionControllerInterface, TestsConstants {
    @FXML
    private VBox answerVariants_vBox;
    @FXML
    private Button addVariant_button;

    private int numberOfVariants = 0;
    private boolean isAnswersShown = false;
    private ArrayList<String> correctAnswers;

    @FXML
    void initialize() {
    }

    public void createNewVariant(String variantText) {
        numberOfVariants++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(hBOX_SPACING);
        //CheckBox
        CheckBox checkBox = new CheckBox("");
        //TextField
        TextField textField = new TextField();
        textField.setEditable(false);
        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(variantText);
        textField.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        //Compound elements
        hBox.getChildren().addAll(checkBox, textField);
        answerVariants_vBox.getChildren().add(answerVariants_vBox.getChildren().size(), hBox);
    }

    @Override
    public ArrayList<String> getQuestionsVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < answerVariants_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
            String questionText = ((TextField) hBox.getChildren().get(1)).getText();
            if (questionText != null && !questionText.isEmpty()) {
                arrayList.add(questionText);
            }
        }
        return arrayList;
    }

    @Override
    public ArrayList<String> getAnswersVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < answerVariants_vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
            if (((CheckBox) hBox.getChildren().get(0)).isSelected()) {
                String answerText = ((TextField) hBox.getChildren().get(1)).getText();
                if (answerText != null && !answerText.isEmpty()) {
                    arrayList.add(answerText);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariants, List<String> answerVariants) {
        answerVariants_vBox.getChildren().remove(0, answerVariants_vBox.getChildren().size());
        numberOfVariants = 0;
        for (String variant : questionVariants) {
            createNewVariant(variant);
        }
        if (isAnswersShown) {
            for (int i = 0; i < answerVariants_vBox.getChildren().size(); i++) {
                HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
                ((CheckBox) hBox.getChildren().get(0)).setDisable(true);
                for (String answer : correctAnswers) {
                    if (Objects.equals(((TextField) hBox.getChildren().get(1)).getText(), answer)) {
                        hBox.setStyle("-fx-background-color:" + RIGHT_ANSWER_COLOR);
                    }
                }
            }
        }
        if (!answerVariants.isEmpty()) {
            for (int i = 0; i < answerVariants_vBox.getChildren().size(); i++) {
                HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
                for (String answer : answerVariants) {
                    if (Objects.equals(((TextField) hBox.getChildren().get(1)).getText(), answer)) {
                        ((CheckBox) hBox.getChildren().get(0)).setSelected(true);
                        if (isAnswersShown) {
                            if (!correctAnswers.contains(answer)) {
                                hBox.setStyle("-fx-background-color:" + WRONG_ANSWER_COLOR);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isDuplicateVariantsExists() {
        ArrayList<String> questionsVariantsList = getQuestionsVariantsList();
        Set<String> tempSet = new HashSet<>(questionsVariantsList);
        return tempSet.size() < questionsVariantsList.size();
    }

    @Override
    public void showAnswers(ArrayList<String> correctAnswers) {
        this.isAnswersShown = true;
        this.correctAnswers = correctAnswers;
    }

}


