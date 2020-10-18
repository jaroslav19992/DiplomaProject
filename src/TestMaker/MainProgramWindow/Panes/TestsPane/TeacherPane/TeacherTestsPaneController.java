package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane;

import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTestFile;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
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
    private ListView<TestMakerTestFile> createdTests_listView;
    @FXML
    private AnchorPane main_pane;
    @FXML
    private Button editTest_button;
    @FXML
    private ListView<AccessedPupil> testsAccess_listView;
    @FXML
    private Button gainAccess_button;
    private final int maxTestsNumber = 1000;

    //                       ↓test id in testsList table
//    accessedPupilsList[6];
//                       ^---AccessedPupil1 <-\
//                       ^---AccessedPupil2 < -- pupils who has access to current test
//                       ^---AccessedPupil3 <-/
    private final ArrayList<AccessedPupil>[] accessedPupilsList = new ArrayList[maxTestsNumber];

    private LoadingAnimation loadingAnimation;

    @FXML
    void initialize() {
        setButtonsActions();
        loadingAnimation = new LoadingAnimation(main_pane);
        loadingAnimation.start();
        Platform.runLater(() -> {
            try {
                getTestsList();
                getTestsAccessList();
                createdTests_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                createdTests_listView.getSelectionModel().selectedIndexProperty().addListener((
                        observable, oldIndex, newIndex) -> {
                    showTestsAccess(createdTests_listView.getSelectionModel().getSelectedIndex());
                });
                testsAccess_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } catch (SQLException exception) {
                exception.printStackTrace();
                showAlert(exception);
                loadingAnimation.interrupt();
            }
        });
    }

    /**
     * Set every button on action
     */
    private void setButtonsActions() {
        addTest_button.setOnAction(event -> {
            WindowTools.openNewWindowAndWait(
                    "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/ConfigTestPane.fxml",
                    false, Modality.APPLICATION_MODAL);
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

        });

        removeAccess_button.setOnAction(event -> {
            try {
                //Remove record from the table
                removePupilTestAccess();
                updatePupilsTestsAccess();
                showTestsAccess(createdTests_listView.getSelectionModel().getSelectedIndex());

            } catch (SQLException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Помилка виконання операції видалення");
                alert.setContentText(exception.getMessage());
                alert.show();
            }
        });
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
                String SQLQuery = "DELETE FROM " + DBConstants.DB_NAME + "." + DBConstants.TEACHERS_TESTS_TABLE_NAME
                        + " WHERE (`" + DBConstants.USER_NAME_HASH + "` = '"
                        + UserInfoHandler.userName.hashCode() + "') and (`" + DBConstants.ID_TESTS_LIST + "` = '"
                        + createdTests_listView.getSelectionModel().getSelectedItem().getIdInTestsList() + "');";
                DBHandler.loadDataToDB(SQLQuery);
            }
        }
    }

    /**
     * Clear accessedPupilsTestList and re-adds all pupils to show correct access list
     *
     * @throws SQLException
     */
    private void updatePupilsTestsAccess() throws SQLException {
        for (ArrayList<AccessedPupil> accessedPupils : accessedPupilsList) {
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
            noTestAndPupilChosenAlert.setHeaderText(null);
            noTestAndPupilChosenAlert.setContentText("Оберіть тест та користувача для якого бажаєте видалити доступ до даного тесту");
            noTestAndPupilChosenAlert.showAndWait();
        } else {
            StringBuilder contentTextString = new StringBuilder("Ви дійсно бажаєте обмежити доступ користувача(ів): \n");
            for (AccessedPupil pupil : testsAccess_listView.getSelectionModel().getSelectedItems()) {
                contentTextString.append("\t" + pupil.getFirstName()
                        + " " + pupil.getLastName() + "\n");
            }
            contentTextString.append("до тесту: \n\t" + createdTests_listView.getSelectionModel().getSelectedItem());
            if (getRemoveConfirmation(contentTextString.toString())) {
                for (AccessedPupil pupil : testsAccess_listView.getSelectionModel().getSelectedItems()) {
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
                + DBConstants.LAST_NAME + ", " + DBConstants.USER_NAME_HASH + " FROM " +
                DBConstants.PUPILS_TESTS_TABLE_NAME + " INNER JOIN " + DBConstants.USERS_INFO_TABLE_NAME +
                " USING (" + DBConstants.USER_NAME_HASH + ") INNER JOIN " + DBConstants.TESTS_LIST_TABLE_NAME +
                " USING (" + DBConstants.ID_TESTS_LIST + ")";
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

            /*
             * If there is no pupils added into array list cell in [currentTestId] position
             * create new Arraylist in this position and add new pupil with test access to it.
             * If any pupil already have access to that test, just add new pupil to arraylist
             */
            if (accessedPupilsList[currentTestId] == null) {
                accessedPupilsList[currentTestId] = new ArrayList<>();
                accessedPupilsList[currentTestId].add(new AccessedPupil(usernameHash, firstName, lastName));
            } else {
                accessedPupilsList[currentTestId].add(new AccessedPupil(usernameHash, firstName, lastName));
            }
        }
    }

    /**
     * Show testes created by current user at createdTests_listView
     * create TestMakerTestFile exemplars without file inside
     */
    private void getTestsList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.TEST_NAME + " FROM " +
                DBConstants.TEACHERS_TESTS_TABLE_NAME + " INNER JOIN " + DBConstants.USERS_INFO_TABLE_NAME +
                " USING (" + DBConstants.USER_NAME_HASH + ") INNER JOIN " + DBConstants.TESTS_LIST_TABLE_NAME +
                " USING (" + DBConstants.ID_TESTS_LIST + ") WHERE " + DBConstants.USER_NAME_HASH + " = " +
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
            TestMakerTestFile test = new TestMakerTestFile(testsList.getInt(DBConstants.ID_TESTS_LIST),
                    testsList.getString(DBConstants.TEST_NAME));
            createdTests_listView.getItems().add(pointer, test);
        }
        //stop animation
        loadingAnimation.interrupt();
    }

    /**
     * Show pupils who have access to currently selected test in tests list view
     *
     * @param createdTests_currentIndex - id of currently selected test in tests list view from DB table
     */
    private void showTestsAccess(int createdTests_currentIndex) {
        //if any test is chosen
        if (createdTests_currentIndex >= 0) {
            ArrayList<AccessedPupil> accessedPupils = accessedPupilsList[createdTests_listView.getItems()
                    .get(createdTests_currentIndex).getIdInTestsList()];
            testsAccess_listView.getItems().clear();
            try {
                for (AccessedPupil pupil : accessedPupils) {
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

