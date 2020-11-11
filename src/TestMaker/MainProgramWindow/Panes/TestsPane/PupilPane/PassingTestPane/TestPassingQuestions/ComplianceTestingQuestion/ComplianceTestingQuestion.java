package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.TestPassingQuestions.ComplianceTestingQuestion;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;

public class ComplianceTestingQuestion implements QuestionControllerInterface, TestsConstants {
    @FXML
    private VBox question_vBox;

    @FXML
    private VBox answer_vBox;

    private int numberOfQuestions = 0;
    private int numberOfAnswers = 0;

    @FXML
    void initialize() {
    }

    public void createNewQuestionVariant(String variantText) {
        numberOfQuestions++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        //CheckBox
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
//            setChoicesBoxesValues(); TODO: THIS I JUST COMMENT
        //TextField
        TextArea textArea = new TextArea();
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setText(variantText);
        textArea.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        textArea.setPrefHeight(TEXT_AREA_PREF_HEIGHT);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        //Compound elements
        hBox.getChildren().addAll(textArea, choiceBox);
        question_vBox.getChildren().add(question_vBox.getChildren().size(), hBox);

        //This block created to prevent duplicate choice box values. It looks for all another boxes values and equalized with them
        choiceBox.setOnAction(event -> {
            //Prevent user to choose 2 identical variants
            for (int j = 0; j < question_vBox.getChildren().size(); j++) {
                HBox questionHBox = (HBox) question_vBox.getChildren().get(j);
                ChoiceBox choiceBox1 = (ChoiceBox) questionHBox.getChildren().get(1);
                if (choiceBox1 != choiceBox) {
                    if (choiceBox1.getValue() == choiceBox.getValue() && choiceBox.getValue() != null && choiceBox1.getValue() != null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        choiceBox.setValue(null);
                        alert.setHeaderText(null);
                        alert.setContentText("Даний варіант уже використовується, оберіть інший варіант");
                        alert.show();
                    }
                }
            }
        });
    }

    public void createNewAnswerVariant(String variantText) {
        numberOfAnswers++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        //CheckBox
        Label numberLabel = new Label(Integer.toString(numberOfAnswers));
        numberLabel.setFont(Font.font("System", FontWeight.BOLD, 18.0));
        //TextField
        TextArea textArea = new TextArea();
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setText(variantText);
        textArea.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        textArea.setPrefHeight(TEXT_AREA_PREF_HEIGHT);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        //Compound elements
        hBox.getChildren().addAll(numberLabel, textArea);
        answer_vBox.getChildren().add(answer_vBox.getChildren().size(), hBox);
//            setChoicesBoxesValues();
    }

    /**
     * Set choice box values like answer labels
     * Set on action for every choice box to prevent duplicate answer
     */
    private void setChoicesBoxesValues() {
        //Get all labels that's used at the moment
        ArrayList<Integer> answersNumbers = new ArrayList<>();
//        answersNumbers.add(null); TODO: I can't decide is I need that or not
        for (int i = 0; i < answer_vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) answer_vBox.getChildren().get(i);
            Label label = (Label) hBox.getChildren().get(0);
            answersNumbers.add(Integer.parseInt(label.getText()));
        }
        //Create list of them and use that list as a list of choice box variants
        ObservableList<Integer> observableList = FXCollections.observableList(answersNumbers);
        for (int i = 0; i < question_vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            ChoiceBox<Integer> choiceBox = (ChoiceBox<Integer>) hBox.getChildren().get(1);
            //This block created just to save current variant when choice box values are updating
//                if (choiceBox.getValue() != null) {
//                    choiceBox.setItems(observableList);
//                    choiceBox.setValue(currentNumber);
//                } else {
            choiceBox.setItems(observableList);
//                }
        }
    }

    /**
     * just add questions into array list in the order in which they are recorded
     *
     * @return arraylist of questions
     */
    @Override
    public ArrayList<String> getQuestionsVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < question_vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            String questionText = ((TextArea) hBox.getChildren().get(1)).getText();
            if (!Objects.equals(questionText, null) && !Objects.equals(questionText, "")) {
                arrayList.add(questionText);
            }
        }
        return arrayList;
    }

    /**
     * Situate answer variants in order of questions:
     * questions added to arraylist just in the order in which they are recorded.
     * method searching label with number equal to number in choice box near the question
     * unused answers adding to the end of array list
     *
     * @return arraylist of answers
     */
    @Override
    public ArrayList<String> getAnswersVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < question_vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            ChoiceBox choiceBox = (ChoiceBox) hBox.getChildren().get(1);
            for (int j = 0; j < answer_vBox.getChildren().size(); j++) {
                HBox answerHBox = (HBox) answer_vBox.getChildren().get(j);
                Label label = (Label) answerHBox.getChildren().get(0);
                if (label.getText().equals(String.valueOf(choiceBox.getValue()))) {
                    String answerText = ((TextArea) answerHBox.getChildren().get(1)).getText();
                    if (!Objects.equals(answerText, null) && !Objects.equals(answerText, "")) {
                        arrayList.add(answerText);
                    }
                }
            }
        }

        for (int j = 0; j < answer_vBox.getChildren().size(); j++) {
            HBox answerHBox = (HBox) answer_vBox.getChildren().get(j);
            String answerText = ((TextArea) answerHBox.getChildren().get(1)).getText();
            if (!arrayList.contains(answerText) && !Objects.equals(answerText, null) && !Objects.equals(answerText, "")) {
                arrayList.add(answerText);
            }
        }
        return arrayList;
    }

    @Override
    public void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariantsList, List<String> answerVariantsList) {
        numberOfAnswers = 0;
        numberOfQuestions = 0;

        for (String variant : questionVariantsList) {
            createNewQuestionVariant(variant);
        }
        for (String variant : answerVariantsList) {
            createNewAnswerVariant(variant);
        }
        setChoicesBoxesValues();
    }

    //Don't used now, but could be useful
    @Override
    public void setDefaultCorrectAnswers(VBox vBoxWithChoiceBoxes) {
        for (int i = 0; i < vBoxWithChoiceBoxes.getChildren().size(); i++) {
            HBox hBox = (HBox) vBoxWithChoiceBoxes.getChildren().get(i);
            ChoiceBox choiceBox = (ChoiceBox) hBox.getChildren().get(1);
            choiceBox.setValue(null);
        }
    }

    @Override
    public boolean isDuplicateVariantsExists() {
        ArrayList<String> questionsVariantsList = getQuestionsVariantsList();
        ArrayList<String> answersVariantsList = getAnswersVariantsList();

        Set<String> tempSet = new HashSet<>(questionsVariantsList);
        if (tempSet.size() < questionsVariantsList.size()) {
            return true;
        }
        tempSet = new HashSet<>(answersVariantsList);
        if (tempSet.size() < answersVariantsList.size()) {
            return true;
        }
        return false;
    }
}
