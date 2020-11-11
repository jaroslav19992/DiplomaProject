package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.QuestionControllerInterface;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

import static TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants.*;

public class TestingQuestionBaseController {
//    private final String FREE_ANSWER = "Питання з довільною відповіддю"; TODO: for future versions

    @FXML
    private TextArea questionText_textArea;
    @FXML
    private AnchorPane main_anchorPane;
    @FXML
    private BorderPane inside_borderPane;

    private QuestionControllerInterface currentQuestionController;
    private final WindowTools windowTools = new WindowTools();

    @FXML
    public void initialize() {

    }

    public ArrayList<String> getAnswerVariants() {
        return currentQuestionController.getAnswersVariantsList();
    }

    public void setQuestion(String questionType, double questionScore, String questionText, java.util.List<String> questionVariants, List<String> answerVariants) {
        questionText_textArea.setText(questionText);
        switch (questionType) {
            case ONE_ANSWER: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "PupilPane/PassingTestPane/TestPassingQuestions/" +
                                        "OneAnswerTestingQuestion/OneAnswerTestingQuestion.fxml");
                break;
            }
            case SEVERAL_ANSWERS: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "PupilPane/PassingTestPane/TestPassingQuestions/" +
                                        "SeveralAnswersTestingQuestion/SeveralAnswersTestingQuestion.fxml");
                break;
            }
            case COMPLIANCE_QUESTION: {
                currentQuestionController = (QuestionControllerInterface)
                        windowTools.setUpNewPaneOnBorderPane(inside_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                                        "PupilPane/PassingTestPane/TestPassingQuestions/" +
                                        "ComplianceTestingQuestion/ComplianceTestingQuestion.fxml");
                break;
            }
        }
        currentQuestionController.setQuestion(questionType, questionScore, questionText, questionVariants, answerVariants);
    }
    public Parent getMainPane() {
        return main_anchorPane;
    }
}

