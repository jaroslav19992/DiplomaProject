package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.ComplianceQuestion;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionControllerInterface;
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

import java.util.*;

public class ComplianceQuestionControllerInterface extends TestsConstants implements QuestionControllerInterface {
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
    private final String TO_LITTLE_NUMBER_OF_ANSWERS_ALERT_TEXT = "Кількість варіантів зправа не повинна " +
            "бути меншою ніж кількість варіантів зліва.";

    private int numberOfQuestions = 0;
    private int numberOfAnswers = 0;

    @FXML
    void initialize() {
        for (int i = 0; i < DEFAULT_NUMBER_OF_VARIANTS; i++) {
            createNewQuestionVariant(null);
            createNewAnswerVariant(null);
        }
        numberOfQuestions = question_vBox.getChildren().size() - 1;
        numberOfAnswers = answer_vBox.getChildren().size() - 1;
        addQuestionVariant_button.setOnAction(event -> createNewQuestionVariant(null));
        addAnswerVariant_button.setOnAction(event -> createNewAnswerVariant(null));

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
        setRemoveQuestionVariantButtonAction(button);
        setChoicesBoxesValues();
        //This block created to prevent duplicate choice box values. It looks for all another boxes values and equalized with them

        choiceBox.setOnAction(event -> {
            //Prevent user to choose answer for empty question variant
                String questionText = textArea.getText();
            if (Objects.equals(questionText, "") || Objects.equals(questionText, null)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                choiceBox.setValue(null);
                alert.setHeaderText(null);
                alert.setContentText("Не можливо вибрати відповідь для пустого варіанту. \nЗаповніть вибраний варіант");
                alert.show();
                event.consume();
            }
            //Prevent user to choose empty answer variant as answer
            for (int i = 0; i < answer_vBox.getChildren().size() - 1; i++) {
                HBox questionHBox = (HBox) answer_vBox.getChildren().get(i);
                String answerText = ((TextArea)questionHBox.getChildren().get(1)).getText();
                int answerNumber = Integer.parseInt(((Label)questionHBox.getChildren().get(0)).getText());
                if (Objects.equals(choiceBox.getValue(), answerNumber) ) {
                    if (Objects.equals(answerText, "") || Objects.equals(answerText, null)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        choiceBox.setValue(null);
                        alert.setHeaderText(null);
                        alert.setContentText("Не можливо вибрати пустий варіант. \nЗаповніть вибраний варіант");
                        alert.show();
                        event.consume();
                    }
                }
            }
            //Prevent user to choose 2 identical variants
            for (int j = 0; j < question_vBox.getChildren().size() - 1; j++) {
                HBox questionHBox = (HBox) question_vBox.getChildren().get(j);
                ChoiceBox choiceBox1 = (ChoiceBox) questionHBox.getChildren().get(2);
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
        setRemoveAnswerVariantButtonAction(button);
//        clearChoiceBoxValues();
        setChoicesBoxesValues();
    }

    /**
     * Set button actions for all buttons which should to remove answer variant
     */
    private void setRemoveAnswerVariantButtonAction(Button button) {
        button.setOnAction(event -> {
            int questionsVariants = question_vBox.getChildren().size() - 1;
            int answersVariants = answer_vBox.getChildren().size() - 1;

            if (answersVariants - 1 >= questionsVariants){
                answer_vBox.getChildren().remove(button.getParent());
                setChoicesBoxesValues();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText(TO_LITTLE_NUMBER_OF_ANSWERS_ALERT_TEXT);
                alert.show();
            }
        });

    }

    /**
     * Set button actions for all buttons which should to remove question variant
     */
    private void setRemoveQuestionVariantButtonAction(Button button) {
        button.setOnAction(event -> question_vBox.getChildren().remove(button.getParent()));
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
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
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
     * questions added to arraylist just in the order in which they are recorded
     * method searching label with number equal to number in choice box near the question
     * unused answers adding to the end of array list
     *
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
                    String answerText = ((TextArea) answerHBox.getChildren().get(1)).getText();
                    if (!Objects.equals(answerText, null) && !Objects.equals(answerText, "")) {
                        arrayList.add(answerText);
                    }
                }
            }
        }

        for (int j = 0; j < answer_vBox.getChildren().size() - 1; j++) {
            HBox answerHBox = (HBox) answer_vBox.getChildren().get(j);
            String answerText = ((TextArea) answerHBox.getChildren().get(1)).getText();
            if (!arrayList.contains(answerText) && !Objects.equals(answerText, null) && !Objects.equals(answerText, "")) {
                arrayList.add(answerText);
            }
        }
        return arrayList;
    }

    @Override
    public void setQuestion(String questionType, String questionText, List<String> questionVariants, List<String> answerVariants) {
        question_vBox.getChildren().remove(0, question_vBox.getChildren().size() - 1);
        answer_vBox.getChildren().remove(0, answer_vBox.getChildren().size() - 1);
        numberOfAnswers = 0;
        numberOfQuestions = 0;
        if (!questionVariants.isEmpty()) {
            for (String variant : questionVariants) {
                createNewQuestionVariant(variant);
                //This is setting choice box variants (Just like: first questionVariant -> choiceBox.setValue(1);)
//                clearChoiceBoxVariants();
                HBox hBox = (HBox) question_vBox.getChildren().get(questionVariants.indexOf(variant));
                ChoiceBox<Integer> choiceBox = (ChoiceBox<Integer>) hBox.getChildren().get(2);
                choiceBox.setValue(null);
                choiceBox.setValue(questionVariants.indexOf(variant) + 1);
            }
        } else {
            createNewQuestionVariant(null);
            createNewQuestionVariant(null);
        }
        if (!answerVariants.isEmpty()) {
            for (String variant : answerVariants) {
                createNewAnswerVariant(variant);
            }
        } else {
            createNewAnswerVariant(null);
            createNewAnswerVariant(null);
        }
    }

    private void clearChoiceBoxVariants() {
        for (int i = 0; i < question_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) question_vBox.getChildren().get(i);
            ChoiceBox choiceBox = (ChoiceBox) hBox.getChildren().get(2);
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
