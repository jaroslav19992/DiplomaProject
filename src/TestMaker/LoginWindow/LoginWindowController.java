package TestMaker.LoginWindow;

import java.io.IOException;

import TestMaker.Main;
import TestMaker.UserDataChecker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LoginWindowController {

    @FXML
    private TextField password_text_field;

    @FXML
    private Button login_button;

    @FXML
    private Label error_label;

    @FXML
    private TextField login_text_field;

    @FXML
    private Label password_label;

    @FXML
    private Pane login_pane;

    @FXML
    private Label login_label;

    @FXML
    private Button register_button;

    @FXML
    void initialize() {
        //Hide invalid sing in data label
        error_label.setVisible(false);
        //Enter pressed listener
        setGlobalEventHandler(login_pane);


        login_button.setOnAction(event -> {
            /* LogIn and Password Checker from DB */
            UserDataChecker checker = new UserDataChecker(login_text_field.getText().hashCode(), password_text_field.getText().hashCode());

            if (!checker.isAccessGained()) {

                //Open main program window
                openNewWindow("MainProgramWindow/MainWindow.fxml");
                //Hide LogIn window
                login_pane.getScene().getWindow().hide();
                error_label.setVisible(false);

            } else {
                login_text_field.clear();
                password_text_field.clear();
                error_label.setText("Не правильний логін та/або пароль");
                error_label.setVisible(true);
            }
        });

        //Open registration window
        register_button.setOnAction(event -> {
           openNewWindow("SingUpWindow/SingUpWindow.fxml");
           login_pane.getScene().getWindow().hide();
        });
    }

    /**
     * Opens new window from fxml
     * @param fxmlFileLocation location of window fxml file
     */
    private void openNewWindow(String fxmlFileLocation) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(fxmlFileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
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

