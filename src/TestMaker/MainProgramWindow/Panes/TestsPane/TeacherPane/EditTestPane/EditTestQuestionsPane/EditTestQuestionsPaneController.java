package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane.EditTestQuestionsPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.DOM.DOMxmlWriter;
import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class EditTestQuestionsPaneController implements TestsConstants {
    private static final String FILE_EXTENSION = ".xml";
    @FXML
    private StackPane main_pane;
    @FXML
    private Button removeCurrentQuestion;
    @FXML
    private Button addQuestion_button;
    @FXML
    private Button doneTestCreation_button;
    @FXML
    private Pagination pagination;
    @FXML
    private Label testName_label;

    private int currentTestId;
    private String testName;
    private int amountOfQuestions;
    private int timeLimit;
    private int evaluationSystem;
    private static QuestionBaseController currentPageController;
    private ArrayList<Question> testQuestions;
    private Integer previousPageIndex;
    private int numberOfAttempts;

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

        addQuestion_button.setOnAction(event -> {
            pagination.setPageCount(pagination.getPageCount() + 1);
            testQuestions.add(null);
            pagination.setCurrentPageIndex(pagination.getPageCount());
            amountOfQuestions++;
        });

        doneTestCreation_button.setOnAction(event -> {
            if (checkQuestions(event)) {
                try {
                    loadFileToDB(createTestFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Ask user is he really want to finish test creation, if true - save create test file and save it
     */
    private File createTestFile() {
        try {
            ButtonType create = new ButtonType("Оновити тест");
            ButtonType cancel = new ButtonType("Продовжити редагування");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Ви дійсно бажаєте завершити редагування та оновити тест?");
            alert.setHeaderText(null);
            alert.setTitle("Підтвердіть дію");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(create, cancel);
            Optional<ButtonType> selection = alert.showAndWait();
            if (selection.get().equals(create)) {
                System.out.println("Creating test " + testName);
                DOMxmlWriter writer = new DOMxmlWriter(testQuestions, testName, amountOfQuestions, numberOfAttempts, timeLimit, evaluationSystem);
                return writer.getTestFile(testName + FILE_EXTENSION);
            }
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load test file to database and create record in teachers tests table to link teacher with test
     *
     * @param file test file
     * @throws IOException
     */
    private void loadFileToDB(File file) throws IOException {
        try {
            //Try to insert file into table "tests list"
            String SQLQuery = "UPDATE `" + DBConstants.DB_NAME + "`.`" + DBConstants.TESTS_LIST_TABLE_NAME + "` SET `"
                    + DBConstants.TEST_NAME + "` = ?, `" + DBConstants.TEST_FILE + "` = ?, `" + DBConstants.EV_SYSTEM + "` = ?,"
                    + " `" + DBConstants.AMOUNT_OF_QUESTIONS + "` = ?, `" + DBConstants.TIME_LIMIT
                    + "` = ?, `" + DBConstants.NUMBER_OF_ATTEMPTS + "` = ? WHERE (`" + DBConstants.ID_TESTS_LIST + "` = '" + currentTestId + "');";
            PreparedStatement preparedStatement = DBHandler.getDbConnection().prepareStatement(SQLQuery);
            FileInputStream fileInputStream = new FileInputStream(file);
            preparedStatement.setString(1, testName);
            preparedStatement.setBinaryStream(2, fileInputStream);
            preparedStatement.setInt(3, evaluationSystem);
            preparedStatement.setInt(4, amountOfQuestions);
            preparedStatement.setInt(5, timeLimit);
            preparedStatement.setInt(6, numberOfAttempts);
            preparedStatement.execute();
        } catch (SQLException exception) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Деталі помилки:\n" + exception.getMessage());
            errorAlert.setHeaderText("Помилка завантеження тесту до бази даних\nПеревірте з'єднання з мережею інтернет");
            errorAlert.setTitle("Помилка");
            errorAlert.showAndWait();
            return;
        } catch (NullPointerException exception) {
            System.out.println("No file to create, it's ok");
        }
        //if no errors during test loading show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Успіх створення тесту");
        alert.setHeaderText("Тест " + testName + " успішно створено");
        alert.setContentText("Оновіть сторінку тестів");
        alert.showAndWait();
        main_pane.getScene().getWindow().hide();
    }

    /**
     * Should be used before opening new window with this controller
     *
     * @param testName      name of current test
     * @param numberOfPages number of pages in pagination/number of questions
     * @param timeLimit     time limit dor test
     */
    public void setTestProperties(int idInTestsList, String testName, int evaluationSystem, int numberOfPages,
                                  int numberOfAttempts, Integer timeLimit, ArrayList<Question> testQuestions) {
        currentTestId = idInTestsList;
        this.evaluationSystem = evaluationSystem;
        this.testName = testName;
        this.amountOfQuestions = numberOfPages;
        this.timeLimit = timeLimit;
        this.numberOfAttempts = numberOfAttempts;
        testName_label.setText(testName);
        pagination.setPageCount(amountOfQuestions);
        this.testQuestions = testQuestions;
    }

    private Node createPage(Integer currentPageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools windowTools = new WindowTools();
        QuestionBaseController previousPageController = currentPageController;
        currentPageController = (QuestionBaseController) windowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/EditTestPane/" +
                        "EditTestQuestionsPane/QuestionBase.fxml");

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
    private void createQuestion(QuestionBaseController controller) {
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
    private boolean checkQuestions(ActionEvent event) {
        //Create question if user just fill it and push create test button
        if (!currentPageController.getQuestionText().isEmpty() ||
                !currentPageController.getQuestionsVariantsList().isEmpty()) {
            createQuestion(currentPageController);
        } else {
            testQuestions.set(previousPageIndex, null);
        }

        //Check all questions
        for (int i = 0; i < testQuestions.size(); i++) {
            // If there is empty question
            if (testQuestions.get(i) == null) {
                showTestCreationWarning(i, event, NULLABLE_QUESTION_ALERT_CONTEXT);
                return false;
            }

            ArrayList<String> variants = testQuestions.get(i).getQuestionVariants();
            ArrayList<String> answers = testQuestions.get(i).getAnswerVariants();

            // If there is single answer variant
            if (variants.size() == 1) {
                showTestCreationWarning(i, event, SINGLE_QUESTION_ALERT_CONTEXT);
                return false;
            }
            // If there is empty answer variants
            if (answers.isEmpty()) {
                showTestCreationWarning(i, event, EMPTY_ANSWER_ALERT_CONTEXT);
                return false;
            }
            // If there is question without main text
            if (Objects.equals(testQuestions.get(i).getQuestionText(), null) ||
                    Objects.equals(testQuestions.get(i).getQuestionText(), "")) {
                showTestCreationWarning(i, event, EMPTY_QUESTION_TEXT_ALERT_CONTEXT);
                return false;
            }
            // If there is question with duplicate variants
            Set<String> variantsSet = new HashSet<>(variants);
            Set<String> answerSet = new HashSet<>(answers);
            if (variants.size() > variantsSet.size()) {
                showTestCreationWarning(i, event, DUPLICATES_VARIANTS_QUESTION_ALERT_CONTEXT);
                return false;
            }
            if (answers.size() > answerSet.size()) {
                showTestCreationWarning(i, event, DUPLICATES_ANSWERS_QUESTION_ALERT_CONTEXT);
                return false;
            }
        }
        return true;
    }

    private void showTestCreationWarning(int currentQuestionIndex, ActionEvent event, String warningText) {
        ArrayList<ButtonType> buttonTypeArrayList = new ArrayList<>();
        ButtonType showQuestionButton = new ButtonType("Перейти до питання");
        ButtonType removeQuestionButton = new ButtonType("Видалити питання");
        ButtonType resumeTestCreationButton = new ButtonType("Повернутися до створення тесту");
        buttonTypeArrayList.add(showQuestionButton);
        buttonTypeArrayList.add(removeQuestionButton);
        buttonTypeArrayList.add(resumeTestCreationButton);
        ButtonType buttonType = showWarningAlert(QUESTION_ALERT_TITLE, QUESTION_ALERT_HEADER,
                warningText, buttonTypeArrayList);
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
            this.evaluationSystem = 0;
            this.timeLimit = 0;
            this.numberOfAttempts = 0;
            this.amountOfQuestions = 0;
            main_pane.getScene().getWindow().hide();
        }
    }

    private void removeCurrentQuestion(int questionIndex) {
        //Set previous index null because program read removed question as previous and save it to array
        previousPageIndex = null;
        testQuestions.remove(questionIndex);
        amountOfQuestions = testQuestions.size();
        pagination.setPageCount(amountOfQuestions);
        pagination.setCurrentPageIndex(previousPageIndex);
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
