package TestMaker.MainProgramWindow.Panes.JournalPane.PupilJournalPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
import TestMaker.UserInfoHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PupilJournalPaneController {

    @FXML
    private Label success_label;

    @FXML
    private Label testsAmount_label;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private ListView<TestMakerTest> tests_listView;

    @FXML
    private Label completedTests_label;

    private static final Image usedTestImage = new Image("/TestMaker/Assets/Images/icons/usedTest.png");
    private static final Image unUsedTestImage = new Image("/TestMaker/Assets/Images/icons/unusedTest.png");

    @FXML
    public void initialize() {
        ArrayList<TestMakerTest> testsList;
        try {
            testsList = getTestsList();
            showStatistics(testsList);
            setUpListView(testsList);
        } catch (SQLException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка з'єднання з сервером\n" + exception.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Set statistics labels text
     * @param testsList list of available tests
     */
    private void showStatistics(ArrayList<TestMakerTest> testsList) {
        int completedTestsAmount = 0;
        double successCoefficient = 0;
        for (TestMakerTest test : testsList) {
            if (test.getCurrentUserUsedAttempts() != 0) {
                completedTestsAmount++;
            }
            if (test.getCurrentUserMark() != 0) {
                successCoefficient += (test.getEvSystem() / test.getCurrentUserMark()) * 100;
            }
        }
        testsAmount_label.setText(String.valueOf(testsList.size()));
        completedTests_label.setText(String.valueOf(completedTestsAmount));
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        success_label.setText(decimalFormat.format(successCoefficient)+"%");
    }

    /**
     * Created arrayList of tests from DB query
     *
     * @return array list of tests
     * @throws SQLException
     */
    private ArrayList<TestMakerTest> getTestsList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.TEST_NAME + ", " + DBConstants.MARK
                + ", " + DBConstants.EV_SYSTEM + ", " + DBConstants.USED_ATTEMPTS + " FROM " + DBConstants.DB_NAME + "."
                + DBConstants.TESTS_LIST_TABLE_NAME + " INNER JOIN " + DBConstants.DB_NAME
                + "." + DBConstants.PUPILS_TESTS_TABLE_NAME + " Using (" + DBConstants.ID_TESTS_LIST + ") WHERE "
                + DBConstants.USER_NAME_HASH + " = " + UserInfoHandler.userName.hashCode() + ";";
        System.out.println(SQLQuery);
        ResultSet resultSet = DBHandler.getDataFromDB(SQLQuery);
        ArrayList<TestMakerTest> testsList = new ArrayList<>();
        while (resultSet.next()) {
            testsList.add(new TestMakerTest(resultSet.getInt(DBConstants.ID_TESTS_LIST),
                    resultSet.getString(DBConstants.TEST_NAME),
                    resultSet.getInt(DBConstants.EV_SYSTEM),
                    resultSet.getInt(DBConstants.USED_ATTEMPTS),
                    resultSet.getInt(DBConstants.MARK)));
        }
        return testsList;
    }

    /**
     * Set up list view element on action
     *
     * @param testsList list of available tests
     */
    private void setUpListView(ArrayList<TestMakerTest> testsList) {
        tests_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tests_listView.setCellFactory(listView -> new ListCell<TestMakerTest>() {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(TestMakerTest test, boolean empty) {
                super.updateItem(test, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage((test.getCurrentUserUsedAttempts() == 0) ? (unUsedTestImage) : (usedTestImage));
                    setText(test.toString());
                    setGraphic(imageView);
                }
            }
        });
        ObservableList<TestMakerTest> testsObservableList = FXCollections.observableArrayList();
        testsObservableList.addAll(testsList);
        tests_listView.setItems(testsObservableList);

    }
}

