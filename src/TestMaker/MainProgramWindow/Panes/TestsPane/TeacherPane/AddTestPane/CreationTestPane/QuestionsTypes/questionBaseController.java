package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes;

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


    @FXML
    void initialize() {
        setChoiceBoxVariants();
        setOneAnswerQuestion();
        questionType_choiceBox.setOnAction(event -> {
            if (questionType_choiceBox.getValue().equals(ONE_ANSWER)) {
                WindowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
            }
            if (questionType_choiceBox.getValue().equals(SEVERAL_ANSWERS)) {
                WindowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/SeveralAnswers/SeveralAnswers.fxml");
            }
            if (questionType_choiceBox.getValue().equals(COMPLIANCE_QUESTION)) {
                WindowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                        "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/ComplianceQuestion/ComplianceQuestion.fxml");
            }
        });


    }

    private void setOneAnswerQuestion() {
        WindowTools.setUpNewPaneOnBorderPane(inside_borderPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                "TeacherPane/AddTestPane/CreationTestPane/QuestionsTypes/OneAnswer/OneAnswer.fxml");
    }

    private void setChoiceBoxVariants() {
        ObservableList<String> evaluationSystemsList = FXCollections.observableArrayList(
                ONE_ANSWER, SEVERAL_ANSWERS, COMPLIANCE_QUESTION);
        questionType_choiceBox.setItems(evaluationSystemsList);
        questionType_choiceBox.setValue(ONE_ANSWER);
    }

}
