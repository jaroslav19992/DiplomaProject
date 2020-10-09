package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane;

import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTestFile;
import TestMaker.UserInfoHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private int createdTests_currentIndex;

    private final ArrayList<AccessedPupil>[] accessedPupilsList = new ArrayList[1000];

    private LoadingAnimation loadingAnimation;

    @FXML
    void initialize() {
            loadingAnimation = new LoadingAnimation(main_pane);
            loadingAnimation.start();
            Platform.runLater(() -> {
                try {
                    getTestsList();
                    getTestsAccessList();
                    createdTests_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    createdTests_listView.getSelectionModel().selectedIndexProperty().addListener((
                            observable, oldIndex, newIndex) -> {
                        System.out.println("OLD Index: " + oldIndex + ",  NEW Index: " + newIndex);
                        createdTests_currentIndex = (int) newIndex;
                        showTestsAccess(createdTests_currentIndex);
                    });
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Помилка");
                    alert.setHeaderText(null);
                    alert.setContentText("Помилка з'єднання з сервером\n" + exception.getMessage());
                    alert.showAndWait();
                    loadingAnimation.interrupt();
                }
            });
    }

    /**
     * Get list of which pupil could access which test
     *
     * @throws SQLException if no connection
     */
    private void getTestsAccessList() throws SQLException {
        String SQLQuery = "SELECT " + Constants.ID_TESTS_LIST + ", " + Constants.FIRST_NAME + ", "
                + Constants.LAST_NAME + ", " + Constants.USER_NAME_HASH + " FROM " +
                Constants.PUPILS_TESTS_TABLE_NAME + " INNER JOIN " + Constants.USERS_INFO_TABLE_NAME +
                " USING (" + Constants.USER_NAME_HASH + ") INNER JOIN " + Constants.TESTS_LIST_TABLE_NAME +
                " USING (" + Constants.ID_TESTS_LIST + ")";
        ResultSet testsAccessResultSet = DBHandler.getDataFromDB(SQLQuery);

        //Id for current test
        int currentTestId;
        //Used for chose minimal possible arraylist length
//        int maxTestId = 0;
//
        /*//Choose minimal possible arraylist length
        ResultSet templeSet;
        testsAccessResultSet.clone();
        while (templeSet.next()) {
            currentTestId = templeSet.getInt(Constants.ID_TESTS_LIST);
            if (currentTestId > maxTestId) {
                maxTestId = currentTestId;
            }
        }*/

        /*
         * adds into arraylist cell in [currentTestId] position
         * arraylist with all pupils that have access to that test
         */
        while (testsAccessResultSet.next()) {
            int usernameHash = testsAccessResultSet.getInt(Constants.USER_NAME_HASH);
            String firstName = testsAccessResultSet.getString(Constants.FIRST_NAME);
            String lastName = testsAccessResultSet.getString(Constants.LAST_NAME);
            currentTestId = testsAccessResultSet.getInt(Constants.ID_TESTS_LIST);

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
        String SQLQuery = "SELECT " + Constants.ID_TESTS_LIST + ", " + Constants.TEST_NAME + " FROM " +
                Constants.TEACHERS_TESTS_TABLE_NAME + " INNER JOIN " + Constants.USERS_INFO_TABLE_NAME +
                " USING (" + Constants.USER_NAME_HASH + ") INNER JOIN " + Constants.TESTS_LIST_TABLE_NAME +
                " USING (" + Constants.ID_TESTS_LIST + ") WHERE " + Constants.USER_NAME_HASH + " = " +
                UserInfoHandler.userName.hashCode() + ";";

        showTestsList(SQLQuery);
    }

    /**
     * Add entries to list view with tests list
     *
     * @throws SQLException if no connection
     */
    private void showTestsList(String SQLQuery) throws SQLException {
        ResultSet testsList = DBHandler.getDataFromDB(SQLQuery);
        int pointer = -1;
        while (testsList.next()) {
            pointer++;
            TestMakerTestFile test = new TestMakerTestFile(testsList.getInt(Constants.ID_TESTS_LIST),
                    testsList.getString(Constants.TEST_NAME));
            createdTests_listView.getItems().add(pointer, test);
        }
        loadingAnimation.interrupt();
    }

    /**
     * Show pupils who have access to currently selected test in tests list view
     *
     * @param createdTests_currentIndex - id of currently selected test in tests list view from DB table
     */
    private void showTestsAccess(int createdTests_currentIndex) {
        ArrayList<AccessedPupil> awd = accessedPupilsList[createdTests_listView.getItems()
                .get(createdTests_currentIndex).getIdInTestsList()];
        testsAccess_listView.getItems().clear();
        for (AccessedPupil pupil : awd) {
            testsAccess_listView.getItems().add(pupil);
        }
    }
}

