package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.OneAnswer;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;

public class OneAnswerController implements QuestionControllerInterface, TestsConstants {
    @FXML
    private VBox answerVariants_vBox;

    @FXML
    private final ToggleGroup correctAnswer_toggleGroup = new ToggleGroup();

    @FXML
    private Button addVariant_button;

    private int numberOfVariants = 0;

    @FXML
    void initialize() {
        showEmptyQuestion();
        numberOfVariants = answerVariants_vBox.getChildren().size() - 1;
        addVariant_button.graphicProperty().setValue(new ImageView("/TestMaker/Assets/Images/icons/plus32.png"));
        addVariant_button.setOnAction(event -> createNewVariant(null));
    }

    /**
     * Creates 2 empty variants of answer
     */
    private void showEmptyQuestion() {
        answerVariants_vBox.getChildren().remove(0, answerVariants_vBox.getChildren().size() - 1);
        for (int i = 0; i < DEFAULT_NUMBER_OF_VARIANTS; i++) {
            createNewVariant(null);
        }
        setDefaultCorrectAnswers(answerVariants_vBox);
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
        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(variantText);
        textField.setPromptText("Варіант " + numberOfVariants);
        textField.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        //Remove variant button
        ImageView imageView = new ImageView("/TestMaker/Assets/Images/icons/minus32.png");
        imageView.setFitHeight(REMOVE_VARIANT_BUTTON_IMAGE_FIT_WIDTH);
        imageView.setFitWidth(REMOVE_VARIANT_BUTTON_IMAGE_FIT_HEIGHT);
        Button button = new Button("", imageView);
        //Compound elements
        hBox.getChildren().addAll(radioButton, textField, button);
        answerVariants_vBox.getChildren().add(answerVariants_vBox.getChildren().size() - 1, hBox);
        //Set remove button actions
        button.setOnAction(event -> answerVariants_vBox.getChildren().remove(button.getParent()));
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
        for (int i = 0; i < answerVariants_vBox.getChildren().size() - 1; i++) {
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
    public void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariants, List<String> answerVariants) {
        answerVariants_vBox.getChildren().remove(0, answerVariants_vBox.getChildren().size() - 1);
        numberOfVariants = 0;
        if (!questionVariants.isEmpty()) {
            for (String variant : questionVariants) {
                createNewVariant(variant);
            }
        } else {
            showEmptyQuestion();
        }
        if (!answerVariants.isEmpty()) {
            for (int i = 0; i < answerVariants_vBox.getChildren().size() - 1; i++) {
                HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
                for (String answer : answerVariants) {
                    if (Objects.equals(((TextField) hBox.getChildren().get(1)).getText(), answer)) {
                        ((RadioButton) hBox.getChildren().get(0)).setSelected(true);
                    }
                }
            }
        } else {
            setDefaultCorrectAnswers(answerVariants_vBox);
        }
    }

    @Override
    public boolean isDuplicateVariantsExists() {
        ArrayList<String> questionsVariantsList = getQuestionsVariantsList();
        Set<String> tempSet = new HashSet<>(questionsVariantsList);
        return tempSet.size() < questionsVariantsList.size();
    }

}
