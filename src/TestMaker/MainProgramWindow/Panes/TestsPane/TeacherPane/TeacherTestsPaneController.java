package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.Pupil;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane.EditConfigTestPaneController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.GainPupilAccessPane.GainPupilAccessPaneController;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class TeacherTestsPaneController {

    @FXML
    private Button removeTest_button;
    @FXML
    private Button removeAccess_button;
    @FXML
    private Button addTest_button;
    @FXML
    private ListView<TestMakerTest> createdTests_listView;
    @FXML
    private AnchorPane main_pane;
    @FXML
    private Button editTest_button;
    @FXML
    private ListView<Pupil> testsAccess_listView;
    @FXML
    private Button gainAccess_button;
    @FXML
    private Button reload_button;
    private final int maxTestsNumber = 1000;
    //                       ↓test id in testsList table
//    accessedPupilsList[3];
//                       ^---AccessedPupil0 <-\
//                       ^---AccessedPupil1 < -- pupils who has access to current test
//                       ^---AccessedPupil2 <-/
    private final ArrayList[] accessedPupilsList = new ArrayList[maxTestsNumber];

    @FXML
    void initialize() {
        setButtonsActions();
        try {
            getTestsList();
            getTestsAccessList();
            createdTests_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            createdTests_listView.getSelectionModel().selectedIndexProperty().addListener((
                    observable, oldIndex, newIndex) -> showTestsAccess(createdTests_listView.getSelectionModel().getSelectedIndex()));
            testsAccess_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (SQLException exception) {
            exception.printStackTrace();
            showAlert(exception);
        }
    }

    /**
     * Set every button on action
     */
    private void setButtonsActions() {
        addTest_button.setOnAction(event -> {
            WindowTools windowTools = new WindowTools();
            windowTools.openNewWindowAndWait("/TestMaker/MainProgramWindow/" +
                            "Panes/TestsPane/TeacherPane/AddTestPane/ConfigTestPane.fxml",
                    false, Modality.APPLICATION_MODAL);
            try {
                getTestsList();
                updatePupilsTestsAccess();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        editTest_button.setOnAction(event -> {
            if (createdTests_listView.getSelectionModel().getSelectedItem() == null) {
                Alert noTestAndPupilChosenAlert = new Alert(Alert.AlertType.WARNING);
                noTestAndPupilChosenAlert.setHeaderText(null);
                noTestAndPupilChosenAlert.setContentText("Оберіть тест");
                noTestAndPupilChosenAlert.showAndWait();
            } else {
                WindowTools windowTools = new WindowTools();
                EditConfigTestPaneController controller = (EditConfigTestPaneController) windowTools.openNewWindow(
                        "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/EditTestPane/EditConfigTestPane.fxml",
                        false, Modality.APPLICATION_MODAL);
                controller.setTestProperties(createdTests_listView.getSelectionModel().getSelectedItem());
            }
        });

        removeTest_button.setOnAction(event -> {
            try {
                removeTest();
                updatePupilsTestsAccess();
                getTestsList();
            } catch (SQLException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Помилка виконання операції видалення");
                alert.setContentText(exception.getMessage());
                alert.show();
            }
        });

        gainAccess_button.setOnAction(event -> {
            if (createdTests_listView.getSelectionModel().getSelectedItem() == null) {
                Alert noTestAndPupilChosenAlert = new Alert(Alert.AlertType.WARNING);
                noTestAndPupilChosenAlert.setHeaderText(null);
                noTestAndPupilChosenAlert.setContentText("Оберіть тест для якого бажаєте надати доступ");
                noTestAndPupilChosenAlert.showAndWait();
            } else {
                gainAccessToPupils();
            }
        });

        removeAccess_button.setOnAction(event -> {
            try {
                //Remove record from the table
                removePupilTestAccess();
                updatePupilsTestsAccess();
                showTestsAccess(createdTests_listView.getSelectionModel().getSelectedIndex());
            } catch (SQLException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Помилка виконання операції");
                alert.setContentText(exception.getMessage());
                alert.show();
            }
        });
        reload_button.setOnAction(event -> {
            try {
                getTestsList();
                updatePupilsTestsAccess();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void gainAccessToPupils() {
        WindowTools windowTools = new WindowTools();
        GainPupilAccessPaneController controller = (GainPupilAccessPaneController) windowTools.openNewWindow(
                "/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                        "TeacherPane/GainPupilAccessPane/GainPupilAccessPane.fxml", true, Modality.APPLICATION_MODAL);
        createdTests_listView.getSelectionModel().getSelectedItem().
                setAccessedPupils(testsAccess_listView.getItems());
        controller.setChosenTest(createdTests_listView.getSelectionModel().getSelectedItem());
    }

    private void removeTest() throws SQLException {
        if (createdTests_listView.getSelectionModel().getSelectedItem() == null) {
            Alert noTestAndPupilChosenAlert = new Alert(Alert.AlertType.WARNING);
            noTestAndPupilChosenAlert.setHeaderText(null);
            noTestAndPupilChosenAlert.setContentText("Оберіть тест");
            noTestAndPupilChosenAlert.showAndWait();
        } else {
            if (getRemoveConfirmation("Ви дійсно бажаєте видалити тест \"" +
                    createdTests_listView.getSelectionModel().getSelectedItem() + "\"?")) {
                //Remove from teacher tests list
                String SQLQuery = "DELETE FROM " + DBConstants.DB_NAME + "." + DBConstants.TEACHERS_TESTS_TABLE_NAME
                        + " WHERE (`" + DBConstants.USER_NAME_HASH + "` = '"
                        + UserInfoHandler.userName.hashCode() + "') and (`" + DBConstants.ID_TESTS_LIST + "` = '"
                        + createdTests_listView.getSelectionModel().getSelectedItem().getIdInTestsList() + "');";
                DBHandler.loadDataToDB(SQLQuery);

                //Remove from pupils tests list
                SQLQuery = "DELETE FROM " + DBConstants.DB_NAME + "." + DBConstants.PUPILS_TESTS_TABLE_NAME
                        + " WHERE (`" + DBConstants.ID_TESTS_LIST + "` = '" +
                        createdTests_listView.getSelectionModel().getSelectedItem().getIdInTestsList() + "');";
                DBHandler.loadDataToDB(SQLQuery);

                //Remove from tests list
                SQLQuery = "DELETE FROM " + DBConstants.DB_NAME + "." + DBConstants.TESTS_LIST_TABLE_NAME
                        + " WHERE (`" + DBConstants.ID_TESTS_LIST + "` = '" +
                        createdTests_listView.getSelectionModel().getSelectedItem().getIdInTestsList() + "');";
                DBHandler.loadDataToDB(SQLQuery);
            }
        }
    }

    /**
     * Clear accessedPupilsTestList and re-adds all pupils to show correct access list
     */
    private void updatePupilsTestsAccess() throws SQLException {
        for (ArrayList<Pupil> accessedPupils : accessedPupilsList) {
            if (accessedPupils != null) {
                accessedPupils.clear();
            }
        }
        getTestsAccessList();
    }

    /**
     * Remove lint with pupils username and test id from pupilsTests table in DB
     */
    private void removePupilTestAccess() throws SQLException {
        if (createdTests_listView.getSelectionModel().getSelectedItem() == null ||
                testsAccess_listView.getSelectionModel().getSelectedItems().isEmpty()) {
            Alert noTestAndPupilChosenAlert = new Alert(Alert.AlertType.WARNING);
            noTestAndPupilChosenAlert.setTitle("Помилка");
            noTestAndPupilChosenAlert.setHeaderText(null);
            noTestAndPupilChosenAlert.setContentText("Оберіть тест та користувача для якого бажаєте видалити доступ до даного тесту");
            noTestAndPupilChosenAlert.showAndWait();
        } else {
            StringBuilder contentTextString = new StringBuilder("Ви дійсно бажаєте обмежити доступ користувача(ів): \n");
            for (Pupil pupil : testsAccess_listView.getSelectionModel().getSelectedItems()) {
                contentTextString.append("\t" + pupil.getFirstName()
                        + " " + pupil.getLastName() + "\n");
            }
            contentTextString.append("до тесту: \n\t" + createdTests_listView.getSelectionModel().getSelectedItem());
            if (getRemoveConfirmation(contentTextString.toString())) {
                for (Pupil pupil : testsAccess_listView.getSelectionModel().getSelectedItems()) {
                    String SQLQuery = "DELETE FROM " + DBConstants.DB_NAME + "." + DBConstants.PUPILS_TESTS_TABLE_NAME
                            + " WHERE (`" + DBConstants.USER_NAME_HASH + "` = '"
                            + pupil.getUsernameHash() + "') and (`" + DBConstants.ID_TESTS_LIST + "` = '"
                            + createdTests_listView.getSelectionModel().getSelectedItem().getIdInTestsList() + "');";
                    DBHandler.loadDataToDB(SQLQuery);
                }
            }
        }
    }

    /**
     * ask user to confirm removing pupil access to some test
     *
     * @return is user is agree
     */
    private boolean getRemoveConfirmation(String contentTextString) {
        Alert confirmRemoveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmRemoveAlert.setTitle("Підтвердіть дію");
        confirmRemoveAlert.setHeaderText(null);

        confirmRemoveAlert.setContentText(contentTextString);
        ButtonType remove = new ButtonType("Видалити");
        ButtonType cancel = new ButtonType("Скасувати");
        confirmRemoveAlert.getButtonTypes().clear();
        confirmRemoveAlert.getButtonTypes().addAll(remove, cancel);
        Optional<ButtonType> selection = confirmRemoveAlert.showAndWait();
        return selection.get() != cancel && selection.get() != null;
    }

    /**
     * Show alert to user
     *
     * @param exception what cause alert (no internet connection expected)
     */
    private void showAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText("Помилка з'єднання з сервером\n" + exception.getMessage());
        alert.showAndWait();
    }

    /**
     * Get list of which pupil could access which test
     *
     * @throws SQLException if no connection
     */
    private void getTestsAccessList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.FIRST_NAME + ", "
                + DBConstants.LAST_NAME + ", " + DBConstants.USER_NAME_HASH + ", " + DBConstants.CLASS_ROOM
                + " FROM " + DBConstants.PUPILS_TESTS_TABLE_NAME
                + " INNER JOIN " + DBConstants.USERS_INFO_TABLE_NAME
                + " USING (" + DBConstants.USER_NAME_HASH + ") INNER JOIN " + DBConstants.TESTS_LIST_TABLE_NAME
                + " USING (" + DBConstants.ID_TESTS_LIST + ")";
        ResultSet testsAccessResultSet = DBHandler.getDataFromDB(SQLQuery);

        //Id for current test
        int currentTestId;

        /*
         * adds into arraylist cell in [currentTestId] position
         * arraylist with all pupils that have access to that test
         */
        while (testsAccessResultSet.next()) {
            int usernameHash = testsAccessResultSet.getInt(DBConstants.USER_NAME_HASH);
            String firstName = testsAccessResultSet.getString(DBConstants.FIRST_NAME);
            String lastName = testsAccessResultSet.getString(DBConstants.LAST_NAME);
            currentTestId = testsAccessResultSet.getInt(DBConstants.ID_TESTS_LIST);
            String classRoom = testsAccessResultSet.getString(DBConstants.CLASS_ROOM);

            /*
             * If there is no pupils added into array list cell in [currentTestId] position
             * create new Arraylist in this position and add new pupil with test access to it.
             * If any pupil already have access to that test, just add new pupil to arraylist
             */
            if (accessedPupilsList[currentTestId] == null) {
                accessedPupilsList[currentTestId] = new ArrayList<>();
                accessedPupilsList[currentTestId].add(new Pupil(usernameHash, firstName, lastName, classRoom));
            } else {
                accessedPupilsList[currentTestId].add(new Pupil(usernameHash, firstName, lastName, classRoom));
            }
        }
    }

    /**
     * Show testes created by current user at createdTests_listView
     * create TestMakerTestFile exemplars without file inside
     */
    private void getTestsList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.TEST_NAME + ", " + DBConstants.EV_SYSTEM
                + ", " + DBConstants.AMOUNT_OF_QUESTIONS + ", " + DBConstants.TIME_LIMIT + ", " + DBConstants.NUMBER_OF_ATTEMPTS
                + " FROM " + DBConstants.TEACHERS_TESTS_TABLE_NAME + " INNER JOIN " + DBConstants.USERS_INFO_TABLE_NAME
                + " USING (" + DBConstants.USER_NAME_HASH + ") INNER JOIN " + DBConstants.TESTS_LIST_TABLE_NAME
                + " USING (" + DBConstants.ID_TESTS_LIST + ") WHERE " + DBConstants.USER_NAME_HASH + " = " +
                UserInfoHandler.userName.hashCode() + ";";
        ResultSet testsList = DBHandler.getDataFromDB(SQLQuery);
        showTestsList(testsList);
    }

    /**
     * Add entries to list view with tests list
     *
     * @throws SQLException if no connection
     */
    private void showTestsList(ResultSet testsList) throws SQLException {
        //clear list view with tests
        createdTests_listView.getItems().clear();
        //set position to add nwe test before 0
        int pointer = -1;
        //while there is lines in result set ...
        while (testsList.next()) {
            pointer++;
            //add testMakerFile to tests list
            TestMakerTest test = new TestMakerTest(testsList.getInt(DBConstants.ID_TESTS_LIST),
                    testsList.getString(DBConstants.TEST_NAME), testsList.getInt(DBConstants.EV_SYSTEM),
                    testsList.getInt(DBConstants.AMOUNT_OF_QUESTIONS), testsList.getInt(DBConstants.TIME_LIMIT),
                    testsList.getInt(DBConstants.NUMBER_OF_ATTEMPTS));
            createdTests_listView.getItems().add(pointer, test);
        }

    }

    /**
     * Show pupils who have access to currently selected test in tests list view
     *
     * @param createdTests_currentIndex - id of currently selected test in tests list view from DB table
     */
    private void showTestsAccess(int createdTests_currentIndex) {
        //if any test is chosen
        if (createdTests_currentIndex >= 0) {
            ArrayList<Pupil> accessedPupils = accessedPupilsList[createdTests_listView.getItems()
                    .get(createdTests_currentIndex).getIdInTestsList()];
            testsAccess_listView.getItems().clear();
            try {
                for (Pupil pupil : accessedPupils) {
                    testsAccess_listView.getItems().add(pupil);
                }
            } catch (NullPointerException exception) {
                System.out.println("No accessed pupils for this test");
            }
            // else clear test access list
        } else {
            testsAccess_listView.getItems().clear();
        }
    }
}

