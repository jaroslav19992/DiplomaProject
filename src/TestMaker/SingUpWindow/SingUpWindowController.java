package TestMaker.SingUpWindow;


import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SingUpWindowController {

    @FXML
    private TextField lastName;

    @FXML
    private TextField firstName_textField;

    @FXML
    private TextField email_textField;

    @FXML
    private TextField password_textField;

    @FXML
    private Button SingUpButton;

    @FXML
    private TextField userName_textField;

    @FXML
    public void initialize() {
        SingUpButton.setOnAction(event -> {
            DBHandler dbhandler = new DBHandler();
            dbhandler.singUpNewUser(userName_textField.getText(),
                    password_textField.getText(), firstName_textField.getText(), lastName.getText(),
                    email_textField.getText(), Constants.TEACHER_ACCESS_TOKEN);
            System.out.println("user " + userName_textField.getText() + " singed up");

        });
    }


}
