package TestMaker.AccessWindow;


import TestMaker.DBTools.DBHandler;
import TestMaker.SingUpWindow.SingUpWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class AccessWindowController {

    @FXML
    private Label error_label;

    @FXML
    private Button cancel_button;

    @FXML
    private TextField accessKey_textField;

    @FXML
    private Button accept_button;

    @FXML
    private StackPane main_pane;

    private final String teacherAccessKey = "THISISTEACHERACCESSKEY"; //TODO: this is shit...

    @FXML
    public void initialize() {
        error_label.setVisible(false);
        accept_button.setOnAction(event -> {
            if (isAccessGained(accessKey_textField.getText())) {
                //gain access, close window, register user, open main program window
                DBHandler dbHandler = new DBHandler();

                dbHandler.singUpNewUser(UserInfoTransfer.userName, UserInfoTransfer.password, UserInfoTransfer.firstName,
                        UserInfoTransfer.lastName, UserInfoTransfer.email, UserInfoTransfer.accessToken);

                UserInfoTransfer.isRegisterAccessGained = true;
                main_pane.getScene().getWindow().hide();
            } else {
                error_label.setVisible(true);
            }
        });
        //set up close button
        cancel_button.setOnAction(event1 -> {
            main_pane.getScene().getWindow().hide();
        });
    }

    /**
     * Check for correct access key
     *
     * @param userAccessKey - key that user insert into key field
     * @return is key is correct
     */
    public boolean isAccessGained(String userAccessKey) {
        return userAccessKey.equals(teacherAccessKey);
    }
}
