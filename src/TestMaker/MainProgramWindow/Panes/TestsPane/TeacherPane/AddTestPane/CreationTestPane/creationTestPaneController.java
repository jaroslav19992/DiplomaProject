package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.questionBaseController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.*;

public class creationTestPaneController implements TestsConstants {


    @FXML
    private StackPane main_pane;
    @FXML
    private Button removeCurrentQuestion;
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
    private static questionBaseController currentPageController;
    private ArrayList<Question> testQuestions;
    private Integer previousPageIndex;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            main_pane.getScene().getWindow().setOnCloseRequest(this::onWindowClosed);
        });
        setButtonActions();
    }

    public void setPageFactory() {
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Creates array list where all elements are null
     *
     * @param size number of elements in array list
     * @return created array list
     */
    public static ArrayList<Question> createPrefilledList(int size) {
        ArrayList<Question> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }


    private void setButtonActions() {
        removeCurrentQuestion.setOnAction(event -> {
            ButtonType remove = new ButtonType("Видалити");
            ButtonType cancel = new ButtonType("Скасувати");
            List<ButtonType> buttonTypeList = new ArrayList<>();
            buttonTypeList.add(remove);
            buttonTypeList.add(cancel);
            ButtonType warningAnswer = showWarningAlert("Підтвердіть дію", null,
                    "Видалити поточне питання?", buttonTypeList);
            if (warningAnswer.equals(remove)) {
                removeCurrentQuestion(pagination.getCurrentPageIndex());
            } else {
                event.consume();
            }
        });

        doneTestCreation_button.setOnAction(event -> {
            checkQuestions(event);
            createTestFile();
        });
    }

    private void createTestFile() {
    }

    /**
     * Should be used before opening new window with this controller
     *
     * @param testName           name of current test
     * @param numberOfPages      number of pages in pagination/number of questions
     * @param isRetestingAllowed can you retesting or not
     * @param timeLimit          time limit dor test
     */
    public void setTestProperties(String testName, String evaluationSystem, int numberOfPages,
                                  boolean isRetestingAllowed, Integer timeLimit) {
        this.evaluationSystem = evaluationSystem;
        this.testName = testName;
        this.amountOfQuestions = numberOfPages;
        this.isRetestingAllowed = isRetestingAllowed;
        this.timeLimit = timeLimit;
        testName_label.setText(testName);
        pagination.setPageCount(amountOfQuestions);
        testQuestions = createPrefilledList(amountOfQuestions);
    }

    private Node createPage(Integer currentPageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools windowTools = new WindowTools();
        questionBaseController previousPageController = currentPageController;
        currentPageController = (questionBaseController) windowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/CreationTestPane" +
                        "/QuestionsTypes/questionBase.fxml");

        //if it is not first shown page
        if (previousPageIndex != null) {
            if (!previousPageController.getQuestionText().isEmpty() ||
                    !previousPageController.getQuestionsVariantsList().isEmpty()) {
                createQuestion(previousPageController);
            } else {
                testQuestions.set(previousPageIndex, null);
            }
        }
        //If there is question on index [currentPageIndex] available load this question information to current page
        if (testQuestions.get(currentPageIndex) != null) {
            currentPageController.setQuestion(
                    testQuestions.get(currentPageIndex).getQuestionType(),
                    testQuestions.get(currentPageIndex).getQuestionScore(),
                    testQuestions.get(currentPageIndex).getQuestionText(),
                    testQuestions.get(currentPageIndex).getQuestionVariants(),
                    testQuestions.get(currentPageIndex).getAnswerVariants());
        }
        previousPageIndex = currentPageIndex;
        return mainQuestionPane;
    }

    /**
     * Creating question in array
     *
     * @param controller page controller with question information
     */
    private void createQuestion(questionBaseController controller) {
        testQuestions.set(previousPageIndex, new Question(
                controller.getQuestionType(),
                controller.getQuestionScore(evaluationSystem, amountOfQuestions),
                controller.getQuestionText(),
                controller.getQuestionsVariantsList(),
                controller.getAnswersVariantsList()));
        System.out.println("Created question at testQuestions[" + previousPageIndex + "]");
    }

    /**
     * Checking questions for wrong filling
     * Give user several variants of actions to solve problem questions
     *
     * @param event create test button action
     */
    private void checkQuestions(ActionEvent event) {
        //Create question if user just fill it and push create test button
        if (!currentPageController.getQuestionText().isEmpty() ||
                !currentPageController.getQuestionsVariantsList().isEmpty()) {
            createQuestion(currentPageController);
        } else {
            testQuestions.set(previousPageIndex, null);
        }

        //Check for empty questions and propose to remove or edit them
        for (int i = 0; i < testQuestions.size(); i++) {
            // If user don't create question
            if (testQuestions.get(i) == null) {
                nullTestCaseAction(i, event);
                return;
            }
            ArrayList<String> answers = testQuestions.get(i).getAnswerVariants();
            if (answers.isEmpty()) {
                emptyAnswerCaseAction(i, event);
                return;
            }
            if (Objects.equals(testQuestions.get(i).getQuestionText(), null)) {
                emptyQuestionTextCaseAction(i, event);
            }
        }
    }

    private void emptyQuestionTextCaseAction(int currentQuestionIndex, ActionEvent event) {
        ArrayList<ButtonType> buttonTypeArrayList = new ArrayList<>();
        ButtonType showQuestionButton = new ButtonType("Перейти до питання");
        ButtonType removeQuestionButton = new ButtonType("Видалити питання");
        ButtonType resumeTestCreationButton = new ButtonType("Повернутися до створення тесту");
        buttonTypeArrayList.add(showQuestionButton);
        buttonTypeArrayList.add(removeQuestionButton);
        buttonTypeArrayList.add(resumeTestCreationButton);
        ButtonType buttonType = showWarningAlert(QUESTION_ALERT_TITLE, QUESTION_ALERT_HEADER,
                EMPTY_QUESTION_TEXT_ALERT_CONTEXT, buttonTypeArrayList);
        if (Objects.equals(buttonType, showQuestionButton)) {
            pagination.setCurrentPageIndex(currentQuestionIndex);
            event.consume();
        }
        if (Objects.equals(buttonType, removeQuestionButton)) {
            ButtonType remove = new ButtonType("Видалити питання");
            ButtonType cancel = new ButtonType("Скасувати");
            Collection<ButtonType> buttonTypeList2 = new ArrayList<>();
            buttonTypeList2.add(remove);
            buttonTypeList2.add(cancel);
            ButtonType warningAnswer2 = showWarningAlert(
                    QUESTION_ALERT_TITLE,
                    QUESTION_ALERT_HEADER,
                    REMOVE_QUESTION_ALERT_CONTEXT, buttonTypeList2);
            if (Objects.equals(warningAnswer2, remove)) {
                removeCurrentQuestion(currentQuestionIndex);
                event.consume();
            }
            //Cancel button actually do not do anything
        }
        if (Objects.equals(buttonType, resumeTestCreationButton)) {
            pagination.setCurrentPageIndex(currentQuestionIndex);
            event.consume();
        }
    }

    /**
     * Tells user if there is empty answers in test, propose remove question, show it or resume test creation
     *
     * @param currentQuestionIndex - question index in array
     * @param event                create test button event
     */
    private void emptyAnswerCaseAction(int currentQuestionIndex, Event event) {
        ArrayList<ButtonType> buttonTypeArrayList = new ArrayList<>();
        ButtonType showQuestionButton = new ButtonType("Перейти до питання");
        ButtonType removeQuestionButton = new ButtonType("Видалити питання");
        ButtonType resumeTestCreationButton = new ButtonType("Повернутися до створення тесту");
        buttonTypeArrayList.add(showQuestionButton);
        buttonTypeArrayList.add(removeQuestionButton);
        buttonTypeArrayList.add(resumeTestCreationButton);
        ButtonType buttonType = showWarningAlert(QUESTION_ALERT_TITLE, QUESTION_ALERT_HEADER,
                EMPTY_ANSWER_ALERT_CONTEXT, buttonTypeArrayList);
        if (Objects.equals(buttonType, showQuestionButton)) {
            pagination.setCurrentPageIndex(currentQuestionIndex);
            event.consume();
        }
        if (Objects.equals(buttonType, removeQuestionButton)) {
            ButtonType remove = new ButtonType("Видалити питання");
            ButtonType cancel = new ButtonType("Скасувати");
            Collection<ButtonType> buttonTypeList2 = new ArrayList<>();
            buttonTypeList2.add(remove);
            buttonTypeList2.add(cancel);
            ButtonType warningAnswer2 = showWarningAlert(
                    QUESTION_ALERT_TITLE,
                    QUESTION_ALERT_HEADER,
                    REMOVE_QUESTION_ALERT_CONTEXT, buttonTypeList2);
            if (Objects.equals(warningAnswer2, remove)) {
                removeCurrentQuestion(currentQuestionIndex);
                event.consume();
            }
            //Cancel button actually do not do anything
        }
        if (Objects.equals(buttonType, resumeTestCreationButton)) {
            pagination.setCurrentPageIndex(currentQuestionIndex);
            event.consume();
        }
    }

    //Ask user is he really want to close window
    public void onWindowClosed(Event event) {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        closeConfirmation.setHeaderText(CLOSE_WINDOW_CONFIRMATION_HEADER);
        closeConfirmation.setTitle(CLOSE_WINDOW_CONFIRMATION_TITLE);
        closeConfirmation.setContentText(CLOSE_WINDOW_CONFIRMATION_CONTEXT);
        ButtonType close = new ButtonType("Закрити");
        ButtonType cancel = new ButtonType("Відміна");
        closeConfirmation.getButtonTypes().clear();
        closeConfirmation.getButtonTypes().addAll(cancel, close);
        Optional<ButtonType> selection = closeConfirmation.showAndWait();
        if (selection.get() == cancel) {
            event.consume();
        } else if (selection.get() == close) {
            this.previousPageIndex = null;
            this.testQuestions = null;
            this.testName = null;
            this.evaluationSystem = null;
            this.timeLimit = 0;
            this.isRetestingAllowed = false;
            this.amountOfQuestions = 0;
            main_pane.getScene().getWindow().hide();
        }
    }


    /**
     * Tells user if there is nullable questions in test, propose remove nullable question, show it or resume test creation
     *
     * @param currentQuestionIndex - question index in array
     * @param event                create test button event
     */
    private void nullTestCaseAction(int currentQuestionIndex, Event event) {
        ButtonType showQuestionButton = new ButtonType("Перейти до питання");
        ButtonType removeQuestionButton = new ButtonType("Видалити питання");
        ButtonType resumeTestCreationButton = new ButtonType("Повернутися до редагування");
        List<ButtonType> buttonTypeList = new ArrayList<>();
        buttonTypeList.add(showQuestionButton);
        buttonTypeList.add(removeQuestionButton);
        buttonTypeList.add(resumeTestCreationButton);
        ButtonType warningAnswer = showWarningAlert(QUESTION_ALERT_TITLE,
                QUESTION_ALERT_HEADER,
                NULLABLE_QUESTION_ALERT_CONTEXT,
                buttonTypeList);
        if (warningAnswer.equals(resumeTestCreationButton)) {
            event.consume();
        }
        if (warningAnswer.equals(removeQuestionButton)) {
            ButtonType remove = new ButtonType("Видалити питання");
            ButtonType cancel = new ButtonType("Скасувати");
            Collection<ButtonType> buttonTypeList2 = new ArrayList<>();
            buttonTypeList2.add(remove);
            buttonTypeList2.add(cancel);
            ButtonType warningAnswer2 = showWarningAlert(
                    QUESTION_ALERT_TITLE,
                    QUESTION_ALERT_HEADER,
                    REMOVE_QUESTION_ALERT_CONTEXT, buttonTypeList2);
            if (Objects.equals(warningAnswer2, remove)) {
                removeCurrentQuestion(currentQuestionIndex);
                event.consume();
            }
            //Cancel button actually do not do anything
        }
        if (Objects.equals(warningAnswer, showQuestionButton)) {
            pagination.setCurrentPageIndex(currentQuestionIndex);
            event.consume();
        }
    }

    private void removeCurrentQuestion(int questionIndex) {
        //Set previous index null because program read removed question as previous and save it to array
        previousPageIndex = null;
        testQuestions.remove(questionIndex);
        amountOfQuestions = testQuestions.size();
        pagination.setPageCount(amountOfQuestions);
        pagination.setMaxPageIndicatorCount(amountOfQuestions);
    }

    private ButtonType showWarningAlert(String alertTitle, String alertHeader, String alertText, Collection<? extends ButtonType> buttonsList) {
        Alert questionAlert = new Alert(Alert.AlertType.WARNING);
        questionAlert.setHeaderText(alertHeader);
        questionAlert.setTitle(alertTitle);
        questionAlert.setContentText(alertText);

        questionAlert.getButtonTypes().clear();
        questionAlert.getButtonTypes().addAll(buttonsList);
        Optional<ButtonType> selection = questionAlert.showAndWait();
        return selection.get();
    }
}
