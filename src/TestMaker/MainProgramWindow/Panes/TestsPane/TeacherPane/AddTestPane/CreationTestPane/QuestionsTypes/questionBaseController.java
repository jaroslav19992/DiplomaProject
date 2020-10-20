package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.ComplianceQuestion.ComplianceQuestionController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.OneAnswer.OneAnswerController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.SeveralAnswers.SeveralAnswersController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class questionBaseController extends TestsConstants {

//    private final String FREE_ANSWER = "Питання з довільною відповіддю"; TODO: for future versions


    @FXML
    private TextField questionText_textField;

    @FXML
    private AnchorPane main_anchorPane;

    @FXML
    private ChoiceBox<String> questionType_choiceBox;

    @FXML
    private BorderPane inside_borderPane;


    private QuestionController currentQuestionController;
    private Question[] testQuestions;

    @FXML
    void initialize() {
        setChoiceBoxVariants();
        setOneAnswerQuestion();
        setQuestionsTypeChange();
    }

    public void setQuestionsAmount(int amount) {
        testQuestions = new Question[amount];
    }

    /**
     * set up needed question pane on pagination in order to question type changer choice box
     */
    private void setQuestionsTypeChange() {
        questionType_choiceBox.setOnAction(event -> {
            if (questionType_choiceBox.getValue().equals(ONE_ANSWER)) {
                currentQuestionController = (OneAnswerController)
                        WindowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
            }
            if (questionType_choiceBox.getValue().equals(SEVERAL_ANSWERS)) {
                currentQuestionController = (SeveralAnswersController)
                        WindowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/SeveralAnswers/SeveralAnswers.fxml");
            }
            if (questionType_choiceBox.getValue().equals(COMPLIANCE_QUESTION)) {
                currentQuestionController = (ComplianceQuestionController)
                        WindowTools.setUpNewPaneOnBorderPane(inside_borderPane,
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
        WindowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
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

}
