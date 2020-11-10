package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionControllerInterface;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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

    }

    public void setQuestion(Question question) {
        switch (question.getQuestionType()) {
            case ONE_ANSWER: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
                break;
            }
            case SEVERAL_ANSWERS: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/SeveralAnswers/SeveralAnswers.fxml");
                break;
            }
            case COMPLIANCE_QUESTION: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/ComplianceQuestion/" +
                                        "ComplianceQuestion.fxml");
                break;
            }
        }
    }
    public Parent getMainPane() {
        return main_anchorPane;
    }
}

