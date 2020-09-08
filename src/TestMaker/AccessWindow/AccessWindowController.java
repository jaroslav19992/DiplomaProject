package TestMaker.AccessWindow;

import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import TestMaker.SingUpWindow.SingUpWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AccessWindowController {

    @FXML
    private Label error_label;

    @FXML
    private TextField accessKey_textField;

    @FXML
    private Button accept_button;

    private final String teacherAccessKey = "THISISTEACHERACCESSKEY"; //TODO: this is shit...

    @FXML
    public void initialize() {
        error_label.setVisible(false);
        accept_button.setOnAction(event -> {
            if (isAccessGained(accessKey_textField.getText())) {
                //gain access, close window, register user, open main program window
                SingUpWindowController swc = new SingUpWindowController();
                DBHandler dbHandler = new DBHandler();

                //TODO: розібратися з цьою хуйньою сука
                dbHandler.singUpNewUser(swc.userName_textField.getText(), swc.password_textField.getText(), swc.firstName_textField.getText(),
                        swc.lastName_textField.getText(), swc.email_textField.getText(), Constants.PUPIL_ACCESS_TOKEN);

                accessKey_textField.getScene().getWindow().hide();
            } else {
                error_label.setVisible(true);
            }
        });
    }

    /**
     * Check for correct access key
     * @param userAccessKey - key that user insert into key field
     * @return is key is correct
     */
    public boolean isAccessGained(String userAccessKey) {
        return userAccessKey.equals(teacherAccessKey);
    }
}
