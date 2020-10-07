package TestMaker.MainProgramWindow.Panes.UserInfoPane.TeacherPane;

import TestMaker.DBTools.Constants;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public class TeacherUserInfoPaneController {

    @FXML
    private StackPane bottom_stackPane;

    @FXML
    private VBox right_vBox;

    @FXML
    private Label lastName_label;

    @FXML
    private Label email_label;

    @FXML
    private Label lastVisitDate_label;

    @FXML
    private VBox left_vBox;

    @FXML
    private Label regDate_label;

    @FXML
    private Label firstName_label;

    @FXML
    private Button changeInfo_button;

    @FXML
    private Label accessToken_label;

    @FXML
    void initialize() {
        initUserInfo();

        changeInfo_button.setOnAction(event -> {
            WindowTools.openNewWindowAndWait("/TestMaker/MainProgramWindow/Panes/UserInfoPane/ChangeUserInfoWindow/ChangeUserInfoWindow.fxml",
                    false, Modality.APPLICATION_MODAL);
        });
    }

    private void updateUserInfo() {
        initUserInfo();
    }

    /**
     * Set user info into pane labels
     */
    private void initUserInfo() {
        firstName_label.setText(UserInfoHandler.firstName);
        lastName_label.setText(UserInfoHandler.lastName);
        email_label.setText(UserInfoHandler.email);
        accessToken_label.setText(UserInfoHandler.accessToken);
        regDate_label.setText(Constants.REG_DATE_LABEL_TEXT + UserInfoHandler.registrationDate);
        lastVisitDate_label.setText(Constants.LAST_VISIT_DATE_LABEL_TEXT + UserInfoHandler.lastVisitDate);
    }

}
