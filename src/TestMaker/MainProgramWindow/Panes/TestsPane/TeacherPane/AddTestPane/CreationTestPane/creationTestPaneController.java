package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.questionBaseControllerInterface;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

public class creationTestPaneController {
    private static final String CLOSE_WINDOW_CONFIRMATION_HEADER = "Ви дійсно бажаєте закрити поточне вікно?";
    private static final String CLOSE_WINDOW_CONFIRMATION_CONTEXT = "Вся введена інформація буде втрачена";
    private static final String CLOSE_WINDOW_CONFIRMATION_TITLE = "Підтвердіть дію";

    @FXML
    private StackPane main_pane;

    @FXML
    private Button cancelTestCreation_button;

    @FXML
    private Button doneTestCreation_button;

    @FXML
    private Pagination pagination;

    @FXML
    private Label testName_label;

    private String testName;
    private int amountOfQuestions;
    private boolean isRetestingAllowed;
    private int timeLimit;
    private String evaluationSystem;
    private static questionBaseControllerInterface currentPageController;
    private static questionBaseControllerInterface previousPageController;
    private Question[] testQuestions;
    private Integer previousPageIndex;

    @FXML
    public void initialize() {
        Platform.runLater(this::setOnWindowClosed);
    }

    public void setPageFactory() {
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Should be used before opening new window with this controller
     *
     * @param testName           name of current test
     * @param numberOfPages      number of pages in pagination/number of questions
     * @param isRetestingAllowed can you retesting or not
     * @param timeLimit          time limit dor test
     */
    public void setTestProperties(String testName, String evaluationSystem, int numberOfPages, boolean isRetestingAllowed, Integer timeLimit) throws IOException {
        this.evaluationSystem = evaluationSystem;
        this.testName = testName;
        this.amountOfQuestions = numberOfPages;
        this.isRetestingAllowed = isRetestingAllowed;
        this.timeLimit = timeLimit;
        testName_label.setText(testName);
        pagination.setPageCount(amountOfQuestions);
        testQuestions = new Question[amountOfQuestions];
    }

    private Node createPage(Integer currentPageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools windowTools = new WindowTools();

        previousPageController = currentPageController;
        //TODO: перевірти на дублікати
//        if (previousPageController != null && previousPageController.isDuplicateVariantsExists()) {
//            pagination.setCurrentPageIndex(previousPageIndex);
//            System.out.println("Duplicate");
//            return previousPageController.getMainPane();
//        }
        currentPageController = (questionBaseControllerInterface) windowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/CreationTestPane" +
                        "/QuestionsTypes/questionBase.fxml");

        //if it first click on another page
        if (previousPageIndex != null) {
            testQuestions[previousPageIndex] = new Question(previousPageController.getQuestionType(), previousPageController.getQuestionText(),
                    previousPageController.getQuestionsVariantsList(), previousPageController.getAnswersVariantsList());
            System.out.println("Created question at testQuestions[" + previousPageIndex + "]");
            previousPageIndex = currentPageIndex;
        }
        //If there is question on index [currentPageIndex] available load this question information to current page
        if (testQuestions[currentPageIndex] != null) {
            currentPageController.setQuestion(
                    testQuestions[currentPageIndex].getQuestionType(),
                    testQuestions[currentPageIndex].getQuestionText(),
                    testQuestions[currentPageIndex].getQuestionVariants(),
                    testQuestions[currentPageIndex].getAnswerVariants());
        }
        previousPageIndex = currentPageIndex;
        return mainQuestionPane;
    }

    //Ask user is he really want to close window
    public void setOnWindowClosed() {
        main_pane.getScene().getWindow().setOnCloseRequest(event -> {
            Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            closeConfirmation.setHeaderText(CLOSE_WINDOW_CONFIRMATION_HEADER);
            closeConfirmation.setTitle(CLOSE_WINDOW_CONFIRMATION_TITLE);
            closeConfirmation.setContentText(CLOSE_WINDOW_CONFIRMATION_CONTEXT);
            ButtonType close = new ButtonType("Закрити");
            ButtonType cancel = new ButtonType("Відміна");
            closeConfirmation.getButtonTypes().clear();
            closeConfirmation.getButtonTypes().addAll(cancel, close);
            Optional<ButtonType> selection = closeConfirmation.showAndWait();
            if (selection.get() != close) {
                event.consume();
            }
            this.previousPageIndex = null;
            this.testQuestions = null;
            this.testName = null;
            this.evaluationSystem = null;
            this.timeLimit = 0;
            this.isRetestingAllowed = false;
            this.amountOfQuestions = 0;

        });
    }
}
