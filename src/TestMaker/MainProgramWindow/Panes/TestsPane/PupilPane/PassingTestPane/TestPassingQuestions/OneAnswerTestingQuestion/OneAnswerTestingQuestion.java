package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.TestPassingQuestions.OneAnswerTestingQuestion;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;

public class OneAnswerTestingQuestion implements QuestionControllerInterface, TestsConstants {

    @FXML
    private VBox answerVariants_vBox;
    @FXML
    private final ToggleGroup correctAnswer_toggleGroup = new ToggleGroup();

    private int numberOfVariants = 0;

    @FXML
    void initialize() {
    }

    private void createNewVariant(String variantText) {
        numberOfVariants++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(hBOX_SPACING);
        //CheckBox
        RadioButton radioButton = new RadioButton("");
        radioButton.setToggleGroup(correctAnswer_toggleGroup);
        //TextField
        TextField textField = new TextField();
        textField.setEditable(false);
        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(variantText);
        textField.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        //Compound elements
        hBox.getChildren().addAll(radioButton, textField);
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
            RadioButton radioButton = (RadioButton) hBox.getChildren().get(0);
            if (radioButton.isSelected()) {
                String answerText = ((TextField) hBox.getChildren().get(1)).getText();
                if (answerText != null && !answerText.isEmpty()) {
                    arrayList.add(answerText);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void setQuestion(String questionType, double questionScore, String questionText,
                            List<String> questionVariants, List<String> answerVariants) {
        numberOfVariants = 0;
        for (String variant : questionVariants) {
            createNewVariant(variant);
        }
        if (!answerVariants.isEmpty()) {
            for (int i = 0; i < answerVariants_vBox.getChildren().size(); i++) {
                HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
                for (String answer : answerVariants) {
                    if (Objects.equals(((TextField) hBox.getChildren().get(1)).getText(), answer)) {
                        ((RadioButton) hBox.getChildren().get(0)).setSelected(true);
                        break;
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

}

