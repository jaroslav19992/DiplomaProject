package TestMaker.MainProgramWindow.Panes.UserInfoPane;

import TestMaker.DBTools.Constants;
import TestMaker.UserDataTransfer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserInfoPaneController {

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
    }

    /**
     * Set user info into pane labels
     */
    private void initUserInfo() {
        firstName_label.setText(UserDataTransfer.firstName);
        lastName_label.setText(UserDataTransfer.lastName);
        email_label.setText(UserDataTransfer.email);
        accessToken_label.setText(UserDataTransfer.accessToken);
        regDate_label.setText(Constants.REG_DATE_LABEL_TEXT + UserDataTransfer.registrationDate);
        lastVisitDate_label.setText(Constants.LAST_VISIT_DATE_LABEL_TEXT + UserDataTransfer.lastVisitDate);
    }

}
