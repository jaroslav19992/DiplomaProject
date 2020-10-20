package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.ComplianceQuestion;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Objects;

public class ComplianceQuestionController extends TestsConstants implements QuestionController {
    @FXML
    private AnchorPane main_anchorPane;

    @FXML
    private Button addAnswerVariant_button;

    @FXML
    private VBox question_vBox;

    @FXML
    private VBox answer_vBox;

    @FXML
    private Button addQuestionVariant_button;

    private int numberOfQuestions;
    private int numberOfAnswers;


    @FXML
    void initialize() {
        numberOfQuestions = question_vBox.getChildren().size();
        numberOfAnswers = answer_vBox.getChildren().size();
        addQuestionVariant_button.setOnAction(event -> createNewQuestionVariant(null));
        addAnswerVariant_button.setOnAction(event -> createNewAnswerVariant(null));
        setRemoveQuestionVariantButtonAction();
        setRemoveAnswerVariantButtonAction();

        setChoicesBoxesValues();
    }

    public void createNewQuestionVariant(String variantText) {
        numberOfQuestions++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        //CheckBox
        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        setChoicesBoxesValues();
        //TextField
        TextArea textArea = new TextArea();
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setText(variantText);
        textArea.setPromptText("Питання " + numberOfQuestions);
        textArea.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        textArea.setWrapText(true);
        textArea.setPrefHeight(TEXT_AREA_PREF_HEIGHT);
        //Remove variant button
        ImageView imageView = new ImageView("/TestMaker/Assets/Images/icons/minus32.png");
        imageView.setFitHeight(REMOVE_VARIANT_BUTTON_IMAGE_FIT_WIDTH);
        imageView.setFitWidth(REMOVE_VARIANT_BUTTON_IMAGE_FIT_HEIGHT);
        Button button = new Button("", imageView);
        //Compound elements
        hBox.getChildren().addAll(button, textArea, choiceBox);
        question_vBox.getChildren().add(question_vBox.getChildren().size() - 1, hBox);
        //Update remove button actions
        setRemoveQuestionVariantButtonAction();
        setChoicesBoxesValues();
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
        textArea.setPromptText("Відповідь " + numberOfAnswers);
        textArea.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        textArea.setWrapText(true);
        textArea.setPrefHeight(TEXT_AREA_PREF_HEIGHT);
        //Remove variant button
        ImageView imageView = new ImageView("/TestMaker/Assets/Images/icons/minus32.png");
        imageView.setFitHeight(REMOVE_VARIANT_BUTTON_IMAGE_FIT_WIDTH);
        imageView.setFitWidth(REMOVE_VARIANT_BUTTON_IMAGE_FIT_HEIGHT);
        Button button = new Button("", imageView);
        //Compound elements
        hBox.getChildren().addAll(numberLabel, textArea, button);
        answer_vBox.getChildren().add(answer_vBox.getChildren().size() - 1, hBox);
        //Update remove button actions
        setRemoveAnswerVariantButtonAction();
        setChoicesBoxesValues();
    }

    /**
     * Set button actions for all buttons which should to remove answer variant
     */
    private void setRemoveAnswerVariantButtonAction() {
        for (int i = 0; i < answer_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) answer_vBox.getChildren().get(i);
            Button button = (Button) hBox.getChildren().get(2);
            button.setOnAction(event -> {
                answer_vBox.getChildren().remove(button.getParent());
                setChoicesBoxesValues();
            });
        }
    }

    /**
     * Set button actions for all buttons which should to remove question variant
     */
    private void setRemoveQuestionVariantButtonAction() {
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            Button button = (Button) hBox.getChildren().get(0);
            button.setOnAction(event -> question_vBox.getChildren().remove(button.getParent()));
        }
    }

    /**
     * Set choice box values like answer labels
     * Set on action for every choice box to prevent duplicate answer
     */
    private void setChoicesBoxesValues() {
    //Get all labels that's used at the moment
        ArrayList<Integer> answersNumbers = new ArrayList<>();
        for (int i = 0; i < answer_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) answer_vBox.getChildren().get(i);
            Label label = (Label) hBox.getChildren().get(0);
            answersNumbers.add(Integer.parseInt(label.getText()));
        }
    //Create list of them and use that list as a list of choice box variants
        ObservableList<Integer> observableList = FXCollections.observableList(answersNumbers);
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            ChoiceBox<Integer> choiceBox = (ChoiceBox<Integer>) hBox.getChildren().get(2);
    //This block created just to save current variant when choice box values are updating
            if (choiceBox.getValue() != null) {
                int currentNumber = choiceBox.getValue();
                choiceBox.setItems(observableList);
                choiceBox.setValue(currentNumber);
            } else {
                choiceBox.setItems(observableList);
            }
    //This block created to prevent duplicate choice box values. It looks for all another boxes values and equalized with them
            choiceBox.setOnAction(event -> {
                for (int j = 0; j < question_vBox.getChildren().size() - 1; j++) {
                    HBox checkingHBox = (HBox) question_vBox.getChildren().get(j);
                    ChoiceBox<Integer> checkingChoiceBox = (ChoiceBox<Integer>) checkingHBox.getChildren().get(2);
                    if (!choiceBox.equals(checkingChoiceBox)) {
                        if (Objects.equals(choiceBox.getValue(), (checkingChoiceBox.getValue()))) {
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
    }

    /**
     * just add questions into array list in the order in which they are recorded
     * @return arraylist of questions
     */
    @Override
    public ArrayList<String> getQuestionsVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            TextArea textArea = (TextArea) hBox.getChildren().get(1);
            arrayList.add(textArea.getText());
        }
        return arrayList;
    }

    /**
     * Situate answer variants in order of questions:
     * questions added to arraylist just in the order in which they are recorded
     * method searching label with number equal to number in choice box near the question
     * unused answers adding to the end of array list
     * @return arraylist of answers
     */
    @Override
    public ArrayList<String> getAnswersVariantsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            ChoiceBox choiceBox = (ChoiceBox) hBox.getChildren().get(2);
            for (int j = 0; j < answer_vBox.getChildren().size() - 1; j++) {
                HBox answerHBox = (HBox) answer_vBox.getChildren().get(j);
                Label label = (Label) answerHBox.getChildren().get(0);
                if (label.getText().equals(String.valueOf(choiceBox.getValue()))) {
                    arrayList.add(((TextArea) answerHBox.getChildren().get(1)).getText());
                }
            }
        }
        for (int j = 0; j < answer_vBox.getChildren().size() - 1; j++) {
            HBox answerHBox = (HBox) answer_vBox.getChildren().get(j);
            String answerText = ((TextArea) answerHBox.getChildren().get(1)).getText();
            if (!arrayList.contains(answerText)) {
                arrayList.add(answerText);
            }
        }
        return arrayList;
    }
}
