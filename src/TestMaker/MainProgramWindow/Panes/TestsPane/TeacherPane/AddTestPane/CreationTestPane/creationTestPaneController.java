package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

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
    private static int numberOfPages;
    private static boolean isRetestingAllowed;
    private static int timeLimit;

    @FXML
    public void initialize() throws IOException {
        /*Parent root = FXMLLoader.load(Main.class.getResource("/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/CreationTestPane/creationTestPane.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();*/

        pagination.setPageCount(numberOfPages);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
    }

    public static void setTestProperties(String testName, int numberOfPages, boolean isRetestingAllowed, Integer timeLimit) throws IOException {
        creationTestPaneController.testName = testName;
        creationTestPaneController.numberOfPages = numberOfPages;
        creationTestPaneController.isRetestingAllowed = isRetestingAllowed;
        creationTestPaneController.timeLimit = timeLimit;
    }




    private Node createPage(Integer pageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools.setUpNewPaneOnBorderPane(mainQuestionPane, "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/TeacherTestsPane.fxml");

        return mainQuestionPane;
    }
}
