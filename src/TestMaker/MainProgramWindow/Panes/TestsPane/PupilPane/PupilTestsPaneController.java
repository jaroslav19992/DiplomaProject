package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.PassingTestPaneController;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PupilTestsPaneController {

    @FXML
    private Label attempts_label;

    @FXML
    private Label time_limit_label;

    @FXML
    private Label testMark_label;

    @FXML
    private Label evSystem_label;

    @FXML
    private ListView<TestMakerTest> availableTests_listView;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private Label testName_label;

    @FXML
    private Label amountOfQuestions_label;

    @FXML
    private Button startTest_button;
    private final String EMPTY_INFO_LABEL_TEXT = "...";
    private final String CONTINUE_TESTING_TEXT = "Пройти тест";
    private final String CANCEL_TESTING_TEXT = "Скасувати";

    @FXML
    void initialize() {
        updateTestsList();
        setUpListViewAction();
        setUpButtonActions();
    }

    /**
     * Show that no test is selected, get tests list and show it in tests list list view
     */
    private void updateTestsList() {
        setTestProperties("Оберіть тест", EMPTY_INFO_LABEL_TEXT, EMPTY_INFO_LABEL_TEXT,
                EMPTY_INFO_LABEL_TEXT, EMPTY_INFO_LABEL_TEXT, EMPTY_INFO_LABEL_TEXT);
        ResultSet testsDataSet;
        try {
            testsDataSet = getTestsList();
            showTestsList(testsDataSet);
        } catch (SQLException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка з'єднання з сервером\n" + exception.getMessage());
            alert.showAndWait();
        }
    }

    private void setUpButtonActions() {
        startTest_button.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Увага");
            alert.setHeaderText("");
            TestMakerTest currentTest = availableTests_listView.getSelectionModel().getSelectedItem();
            if (currentTest != null) {
                if (currentTest.getCurrentUserUsedAttempts() >= currentTest.getNumberOfAttempts()) {
                    alert.setContentText("У вас не залишилося спроб для проходження даного тесту");
                    alert.showAndWait();
                } else {
                    ButtonType continueButton = new ButtonType(CONTINUE_TESTING_TEXT);
                    ButtonType cancelButton = new ButtonType(CANCEL_TESTING_TEXT);
                    int timeLimit = availableTests_listView.getSelectionModel().getSelectedItem().getTimeLimit();
                    if (currentTest.getNumberOfAttempts() == 1) {
                        alert.setHeaderText(alert.getHeaderText() + "Тест не має можливості повторного проходження");
                        alert.setContentText(alert.getContentText() + "\tОбраний тест не має можливості можливого проходження.\n" +
                                "Ви можете пройти його лише раз");
                    }
                    if (timeLimit != 0) {
                        alert.setHeaderText(alert.getHeaderText() + "\nПроходження тесту має часове обмеження");
                        alert.setContentText(alert.getContentText() + "\n\tДля проходження даного тесту буде виділено " +
                                timeLimit + " хв.\n " +
                                "Відлік часу почнеться одразу після натискання кнопки \n\"Пройти тест\"");
                    }
                    alert.setContentText(alert.getContentText() + "\nПочати проходженя тесту?");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(continueButton, cancelButton);
                    Optional<ButtonType> selection = alert.showAndWait();
                    if (selection.get().equals(continueButton)) {
                        WindowTools windowTools = new WindowTools();
                        PassingTestPaneController controller = (PassingTestPaneController) windowTools.openNewWindow(
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/PupilPane/" +
                                        "PassingTestPane/PassingTestPane.fxml", false, Modality.APPLICATION_MODAL);
                        controller.setTest(availableTests_listView.getSelectionModel().getSelectedItem());
                        controller.setPageFactory();
                        controller.setTimer(timeLimit);

                        //update tests list when test is closed
                        controller.getMainPane().getScene().getWindow().setOnHidden(event1 -> {
                            updateTestsList();
                        });
                    } else {
                        event.consume();
                    }
                }
            } else {
                alert.setHeaderText(null);
                alert.setContentText("Оберіть тест зі списку");
                alert.show();
            }
        });

    }

    /**
     * Set up list view element on action
     */
    private void setUpListViewAction() {
        availableTests_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableTests_listView.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
            //To avoid exception when list is cleared and index become -1
            if ((int)newIndex >= 0) {
                TestMakerTest test = availableTests_listView.getItems().get((Integer) newIndex);
                setTestProperties(test.getTestName(), String.valueOf(test.getEvSystem()), String.valueOf(test.getAmountOfQuestions()),
                        (test.getCurrentUserMark() == 0) ? ("Тест не пройдено") : (String.valueOf(test.getCurrentUserMark())),
                        (test.getTimeLimit() == 0) ? ("Відсутній") : (test.getTimeLimit() + " хв."),
                        test.getCurrentUserUsedAttempts() + " використано із " + test.getNumberOfAttempts());
            }
        });
    }

    /**
     * Set test properties labels text
     *
     * @param testName           label with test name
     * @param evSystem           label with evaluation sustem ot current test
     * @param amountOfQuestions  label with amount of questions
     * @param mark               label with mark of current user
     * @param timeLimit          label with test time limit
     * @param isRetestingAllowed label with information is retesting is allowed for current test or not
     */
    private void setTestProperties(String testName, String evSystem, String amountOfQuestions, String mark,
                                   String timeLimit, String isRetestingAllowed) {
        testName_label.setText(testName);
        evSystem_label.setText(evSystem);
        amountOfQuestions_label.setText(amountOfQuestions);
        testMark_label.setText(mark);
        time_limit_label.setText(timeLimit);
        attempts_label.setText(isRetestingAllowed);
    }

    /**
     * Get list of tests with its properties available for current user from DB
     *
     * @return result set with test information
     * @throws SQLException if cant connect to DB
     */
    private ResultSet getTestsList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.TEST_NAME + ", " + DBConstants.EV_SYSTEM + ", "
                + DBConstants.AMOUNT_OF_QUESTIONS + ", " + DBConstants.TIME_LIMIT + ", " + DBConstants.USED_ATTEMPTS + ", "
                + DBConstants.NUMBER_OF_ATTEMPTS + ", " + DBConstants.MARK + " FROM " + DBConstants.DB_NAME + "."
                + DBConstants.TESTS_LIST_TABLE_NAME + " INNER JOIN " + DBConstants.DB_NAME
                + "." + DBConstants.PUPILS_TESTS_TABLE_NAME + " Using(" + DBConstants.ID_TESTS_LIST + ") WHERE "
                + DBConstants.USER_NAME_HASH + " = " + UserInfoHandler.userName.hashCode() + "";
        return DBHandler.getDataFromDB(SQLQuery);
    }

    /**
     * Set up list view with tests available for current user
     *
     * @param testsDataSet set of questions and their properties from DB
     * @throws SQLException
     */
    private void showTestsList(ResultSet testsDataSet) throws SQLException {
        //clear list view with tests
        availableTests_listView.getItems().clear();
        //set position to add nwe test before 0
        int pointer = -1;
        //while there is lines in result set ...
        while (testsDataSet.next()) {
            pointer++;
            //add testMakerFile to tests list
            TestMakerTest test = new TestMakerTest(testsDataSet.getInt(DBConstants.ID_TESTS_LIST),
                    testsDataSet.getString(DBConstants.TEST_NAME), testsDataSet.getInt(DBConstants.EV_SYSTEM),
                    testsDataSet.getInt(DBConstants.AMOUNT_OF_QUESTIONS), testsDataSet.getInt(DBConstants.TIME_LIMIT),
                    testsDataSet.getInt(DBConstants.NUMBER_OF_ATTEMPTS), testsDataSet.getDouble(DBConstants.MARK));
            test.setCurrentUserUsedAttempts(testsDataSet.getInt(DBConstants.USED_ATTEMPTS));
            availableTests_listView.getItems().add(pointer, test);
        }
    }

}

