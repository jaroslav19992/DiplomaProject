package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionControllerInterface;
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

import static TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants.*;

public class TestingQuestionBaseController {
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
        }

        /*
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
        }*/

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

        public ArrayList<String> getAnswersVariantsList() {
            return currentQuestionController.getAnswersVariantsList();
        }

        public void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariants, List<String> answerVariants) {
            questionScore_textField.setText(String.valueOf(questionScore));
            questionText_textField.setText(questionText);
            questionType_choiceBox.setValue(questionType);
            currentQuestionController.setQuestion(questionType, questionScore, questionText, questionVariants, answerVariants);
        }

        public Parent getMainPane() {
            return main_anchorPane;
        }
    }

