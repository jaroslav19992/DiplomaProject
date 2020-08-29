package TestMaker.LoginWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import TestMaker.Main;
import TestMaker.UserDataChecker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField password_text_field;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_text_field;

    @FXML
    private Label password_label;

    @FXML
    private Pane login_pane;

    @FXML
    private Label invalid_data_label;

    @FXML
    private Label login_label;

    @FXML
    void initialize() {
        //Hide invalid sing in data label
        invalid_data_label.setVisible(false);
        //Enter pressed listener
        setGlobalEventHandler(login_pane);

        login_button.setOnAction(event -> {
            /* LogIn and Password Checker from DB */
            UserDataChecker checker = new UserDataChecker(login_text_field.getText().hashCode(), password_text_field.getText().hashCode());

            if (checker.isAccessGained()) {

                //Open main program window
                Parent root = null;
                try {
                    root = FXMLLoader.load(Main.class.getResource("MainProgramWindow/MainWindow.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                assert root != null;
                stage.setScene(new Scene(root));
                stage.show();

                //Hide LogIn window
                login_pane.getScene().getWindow().hide();
            } else {
                login_text_field.clear();
                password_text_field.clear();
                invalid_data_label.setVisible(true);
            }
        });
    }

    /**
     * Enter pressed handler
     * @param root object, add handler to
     */
    private void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                login_button.fire();
                ev.consume();
            }
        });
    }
}

