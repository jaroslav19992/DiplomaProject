package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.questionBaseController;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class creationTestPaneController {
    @FXML
    public Pagination pagination;

    @FXML
    private Label testName_label;


    private static String testName;
    private static int numberOfQuestions;
    private static boolean isRetestingAllowed;
    private static int timeLimit;
    private static String evaluationSystem;

    private questionBaseController currentPageController;

    @FXML
    public void initialize() {
        pagination.setPageCount(numberOfQuestions);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Should be used before opening new window with this controller
     * @param testName name of current test
     * @param numberOfPages number of pages in pagination/number of questions
     * @param isRetestingAllowed can you retesting or not
     * @param timeLimit time limit dor test
     */
    public static void setTestProperties(String testName, String evaluationSystem, int numberOfPages, boolean isRetestingAllowed, Integer timeLimit) throws IOException {
        creationTestPaneController.evaluationSystem = evaluationSystem;
        creationTestPaneController.testName = testName;
        creationTestPaneController.numberOfQuestions = numberOfPages;
        creationTestPaneController.isRetestingAllowed = isRetestingAllowed;
        creationTestPaneController.timeLimit = timeLimit;
    }

    private Node createPage(Integer pageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        currentPageController = (questionBaseController) WindowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/CreationTestPane" +
                        "/QuestionsTypes/questionBase.fxml");
        return mainQuestionPane;
    }
}
