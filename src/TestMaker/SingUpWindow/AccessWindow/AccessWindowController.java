package TestMaker.SingUpWindow.AccessWindow;


import TestMaker.DBTools.DBHandler;
import TestMaker.UserDataTransfer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

                //get register and visit date
                Date date = new Date();
                SimpleDateFormat formatForRegDate = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
                SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

                try {
                    dbHandler.singUpNewUser(UserDataTransfer.userName, UserDataTransfer.password, UserDataTransfer.firstName,
                            UserDataTransfer.lastName, UserDataTransfer.email, UserDataTransfer.accessToken,
                            formatForRegDate.format(date), formatForVisitDate.format(date));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                UserDataTransfer.isRegisterAccessGained = true;
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
