package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionBaseController implements QuestionControllerInterface, TestsConstants {

//    private final String FREE_ANSWER = "Питання з довільною відповіддю"; TODO: for future versions

    @FXML
    private TextField questionScore_textField;
    @FXML
    private TextField questionText_textField;
    @FXML
    private AnchorPane main_anchorPane;
    @FXML
    private ChoiceBox<String> questionType_choiceBox;
    @FXML
    private BorderPane inside_borderPane;

    private QuestionControllerInterface currentQuestionController;
    private final WindowTools windowTools = new WindowTools();

    @FXML
    public void initialize() {
        setChoiceBoxVariants();
        setOneAnswerQuestion();
        setQuestionsTypeChange();
        setQuestionScoreListener();
    }

    private void setQuestionScoreListener() {
        questionScore_textField.setOnKeyReleased(event -> {
            if (!Objects.equals(questionScore_textField.getText(), "")) {
                try {
                    Double.valueOf(questionScore_textField.getText());
                } catch (NumberFormatException exception) {
                    questionScore_textField.clear();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Помилка");
                    alert.setHeaderText(null);
                    alert.setContentText("Не вірний формат поля вводу кількості балів");
                    alert.show();
                }
            }
        });
    }


    /**
     * set up needed question pane on pagination in order to question type changer choice box
     */
    private void setQuestionsTypeChange() {
        questionType_choiceBox.setOnAction(event -> {
            if (questionType_choiceBox.getValue().equals(ONE_ANSWER)) {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
            }
            if (questionType_choiceBox.getValue().equals(SEVERAL_ANSWERS)) {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/SeveralAnswers/SeveralAnswers.fxml");
            }
            if (questionType_choiceBox.getValue().equals(COMPLIANCE_QUESTION)) {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/ComplianceQuestion/" +
                                        "ComplianceQuestion.fxml");
            }
        });
    }

    /**
     * Set up question with single answer by defaults
     */
    private void setOneAnswerQuestion() {
        currentQuestionController = (QuestionControllerInterface) windowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
        questionType_choiceBox.setValue(ONE_ANSWER);
    }

    /**
     * set questions types to choice box
     */
    private void setChoiceBoxVariants() {
        ObservableList<String> evaluationSystemsList = FXCollections.observableArrayList(
                ONE_ANSWER, SEVERAL_ANSWERS, COMPLIANCE_QUESTION);
        questionType_choiceBox.setItems(evaluationSystemsList);
    }

    public String getQuestionType() {
        return questionType_choiceBox.getValue();
    }

    public String getQuestionText() {
        return questionText_textField.getText();
    }

    @Override
    public ArrayList<String> getAnswersVariantsList() {
        return currentQuestionController.getAnswersVariantsList();
    }

    @Override
    public ArrayList<String> getQuestionsVariantsList() {
        return currentQuestionController.getQuestionsVariantsList();
    }

    @Override
    public void setDefaultCorrectAnswers(VBox vBox) {
        currentQuestionController.setDefaultCorrectAnswers(vBox);
    }

    public double getQuestionScore(int evaluationSystem, int amountOfQuestions) {
        if (questionScore_textField.getText().isEmpty()) {
            switch (evaluationSystem) {
                case 5:
                    return (5.0 / amountOfQuestions);
                case 12:
                    return (12.0 / amountOfQuestions);
                case 100:
                    return (100.0 / amountOfQuestions);
            }
        }
        return Double.parseDouble(questionScore_textField.getText());
    }


    @Override
    public void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariants, List<String> answerVariants) {
        questionScore_textField.setText(String.valueOf(questionScore));
        questionText_textField.setText(questionText);
        questionType_choiceBox.setValue(questionType);
        currentQuestionController.setQuestion(questionType, questionScore, questionText, questionVariants, answerVariants);
    }

    @Override
    public boolean isDuplicateVariantsExists() {
        return currentQuestionController.isDuplicateVariantsExists();
    }

    public Parent getMainPane() {
        return main_anchorPane;
    }
}
