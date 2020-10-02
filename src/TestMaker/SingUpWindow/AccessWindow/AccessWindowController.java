package TestMaker.SingUpWindow.AccessWindow;


import TestMaker.UserDataTransfer;
import TestMaker.WindowTools;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        setGlobalEventHandler(main_pane);
        error_label.setVisible(false);
        accept_button.setOnAction(event -> {
            if (isAccessGained(accessKey_textField.getText())) {
                UserDataTransfer.isAccessGained = true;
                WindowTools.closeCurrentWindow(main_pane);
            } else {
                UserDataTransfer.isAccessGained = false;
                error_label.setVisible(true);
            }
        });
        //set up close button
        cancel_button.setOnAction(event1 -> {
            UserDataTransfer.isAccessGained = false;
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

    /**
     * Enter pressed handler
     *
     * @param root object, add handler to
     */
    private void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                accept_button.fire();
                ev.consume();
            }
            if (ev.getCode() == KeyCode.ESCAPE) {
                cancel_button.fire();
            }
        });
    }
}
