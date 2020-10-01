package TestMaker.MainProgramWindow;

import TestMaker.UserDataTransfer;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static TestMaker.MainProgramWindow.Constants.ACTIVE_ITEM_COLOR;


public class MainWindowController {

    @FXML
    private Label settings_label;

    @FXML
    private AnchorPane rightSide_anchorPane;

    @FXML
    private Label userInfo_label;

    @FXML
    private ImageView settings_icon;

    @FXML
    private SplitPane main_splitPane;

    @FXML
    private StackPane tests_stackPane;

    @FXML
    private AnchorPane leftSide_anchorPane;

    @FXML
    private StackPane userInfo_stackPane;

    @FXML
    private ImageView userinfo_icon;

    @FXML
    private ImageView tests_icon;

    @FXML
    private StackPane settings_stackPane;

    @FXML
    private VBox leftSide_vBox;

    @FXML
    private Label tests_label;

    @FXML
    private BorderPane main_borderPane;

    private Stage stage;


    @FXML
    void initialize() {
        setOnHoverColorChange();
        setUpSceneOnClickChange();

        main_splitPane.setOnMouseClicked(event -> {
            main_splitPane.getScene().getWindow().setOnCloseRequest(event1 -> {
                System.out.println("adawdawdawd");
            });
        });


    }


    /**
     * Change scene when another menu item clicked
     */
    private void setUpSceneOnClickChange() {
        WindowTools windowTools = new WindowTools();
        userInfo_stackPane.setOnMouseClicked(event -> {
            if (UserDataTransfer.accessToken.equals("teacherAT")) {
                windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                        "/TestMaker/MainProgramWindow/Panes/UserInfoPane/TeacherPane/TeacherUserInfoPane.fxml");
            }else if (UserDataTransfer.accessToken.equals("pupilAT")) {
                windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                        "/TestMaker/MainProgramWindow/Panes/UserInfoPane/PupilPane/PupilUserInfoPane.fxml");
            }
        });
        tests_stackPane.setOnMouseClicked(event -> {
            windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                    "/TestMaker/MainProgramWindow/Panes/TablesPane/TablesPane.fxml");
        });
        settings_stackPane.setOnMouseClicked(event -> {
            windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                    "/TestMaker/MainProgramWindow/Panes/SettingsPane/SettingsPane.fxml");
        });
    }

    /**
     * Highlights elements when mouse is over them
     */
    private void setOnHoverColorChange() {
        userInfo_stackPane.setOnMouseEntered(event -> {
            userInfo_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR);
        });
        userInfo_stackPane.setOnMouseExited(event -> {
            userInfo_stackPane.setStyle("");
        });

        tests_stackPane.setOnMouseEntered(event -> {
            tests_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR);
        });
        tests_stackPane.setOnMouseExited(event -> {
            tests_stackPane.setStyle("");
        });

        settings_stackPane.setOnMouseEntered(event -> {
            settings_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR);
        });
        settings_stackPane.setOnMouseExited(event -> {
            settings_stackPane.setStyle("");
        });
    }

}
