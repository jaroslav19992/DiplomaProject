package TestMaker.AccessWindow;

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

    private final String teacherAccessKey = "THISISTEACHERACCESSKEY";

    @FXML
    public void initialize() {
        error_label.setVisible(false);
        accept_button.setOnAction(event -> {
            if (!isAccessGained(accessKey_textField.getText())) {
                //gain access, close window, register user, open main program window
            } else {

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
