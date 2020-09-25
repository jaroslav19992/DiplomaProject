package TestMaker.LoginWindow;

import TestMaker.DBTools.Configs;
import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import TestMaker.UserDataChecker;
import TestMaker.UserDataTransfer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static TestMaker.WindowTools.openNewWindow;

public class LoginWindowController {
    @FXML
    private ImageView networkSettings_image;

    @FXML
    private Button networkSettings_button;
    @FXML
    private Label error_label;

    @FXML
    private TextField userName_textField;

    @FXML
    private PasswordField password_passwordField;

    @FXML
    private Button login_button;

    @FXML
    private Label password_label;

    @FXML
    private ImageView imageView;

    @FXML
    private Button register_button;

    @FXML
    private TextField password_textField;

    @FXML
    private StackPane login_pane;

    @FXML
    private Label login_label;

    @FXML
    void initialize() {
        //Hide invalid sing in data label
        error_label.setVisible(false);
        //Enter pressed listener
        setGlobalEventHandler(login_pane);
        //Set up hidden or shown password image
        Image passwordIsHiddenImage = null;
        Image passwordIsShownImage = null;
        try {
            passwordIsHiddenImage = new Image(new File("src/Images/password/closed_eye.png").toURI().toURL().toString());
            passwordIsShownImage = new Image(new File("src/Images/password/open_eye.png").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setImage(passwordIsHiddenImage);

        /*----------------------------Login button action-----------------------------*/
        login_button.setOnAction(event -> {
            loginButtonAction();
        });
        /*----------------------------Login button action-----------------------------*/


        /*----------------------------Registration button action-----------------------------*/
        register_button.setOnAction(event -> {
            openNewWindow("SingUpWindow/SingUpWindow.fxml", false, Modality.NONE);
            login_pane.getScene().getWindow().hide();
        });
        /*----------------------------Registration button action-----------------------------*/


        /*----------------------------networkSettings button action-----------------------------*/
        networkSettings_image.setOnMouseClicked(event -> {
            networkSettings_button.fire();
        });
        networkSettings_button.setOnAction(event -> {
            openNewWindow("LoginWindow/NetworkSettings/NetworkSettings.fxml", false, Modality.APPLICATION_MODAL);
        });
        /*----------------------------networkSettings button action-----------------------------*/


        /*----------------------------Password functions-----------------------------*/
        //Password visibility
        passwordVisibility(passwordIsShownImage, passwordIsHiddenImage);

        //Sync password fields
        password_passwordField.setOnKeyReleased(event -> {
            password_textField.setText(password_passwordField.getText());
        });
        password_textField.setOnKeyReleased(event -> {
            password_passwordField.setText(password_textField.getText());
        });
        /*----------------------------Password functions-----------------------------*/

    }

    /**
     * Set up is password is visible in order to image pressed
     *
     * @param passwordIsShownImage  image that means that password is visible right now
     * @param passwordIsHiddenImage image that means that password is not visible right now
     */
    private void passwordVisibility(Image passwordIsShownImage, Image passwordIsHiddenImage) {
        imageView.setOnMousePressed(event -> {
            if (imageView.getImage().equals(passwordIsHiddenImage)) {
                password_textField.setVisible(true);
                password_passwordField.setVisible(false);
                imageView.setImage(passwordIsShownImage);
            } else {
                password_textField.setVisible(false);
                password_passwordField.setVisible(true);
                imageView.setImage(passwordIsHiddenImage);
                password_passwordField.setFocusTraversable(true);
            }

        });
    }

    /**
     * What is happens when login button is pressed
     */
    private void loginButtonAction() {
        error_label.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText("Помилка з'єднання з базою даних");

        /* LogIn and Password Checker from DB */
        UserDataChecker checker = null;
        try {
            checker = new UserDataChecker(userName_textField.getText().hashCode(),
                    password_passwordField.getText().hashCode());
        } catch (Exception exception) {
            exception.printStackTrace();
            alert.showAndWait();
        }

        if (checker.isAccessGained()) {
//            UserDataTransfer.accessToken = ""; TODO: nahui?
            setLastVisitDate();
            UserDataTransfer.userName = userName_textField.getText();
            UserDataTransfer.password = password_passwordField.getText();
            //Open main program window
            openNewWindow("MainProgramWindow/MainWindow.fxml", false, Modality.NONE);
            //Hide LogIn window
            login_pane.getScene().getWindow().hide();
            System.out.println("----------------------------\n" +
                    "connected via: \n" +
                    "Host: " + Configs.dbHost +
                    "; Port: " + Configs.dbPort +
                    "; User: " + Configs.dbUser +
                    "; Password: " + Configs.dbPassword +
                    "; DBName: " + Configs.dbName +
                    "\n----------------------------");

        } else {
            userName_textField.clear();
            password_passwordField.clear();
            password_textField.clear();
            error_label.setText("Не правильний логін та/або пароль");
            error_label.setVisible(true);
        }
    }

    /**
     * Set up user last visit date
     */
    private void setLastVisitDate() {
        DBHandler dbHandler = new DBHandler();
        Date date = new Date();
        SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

        try {
            dbHandler.loadDataTODB("UPDATE " + "`" + Configs.dbName + "`" + "." + "`" + Constants.USERS_INFO_TABLE_NAME + "`" +
                    " SET " + "`" + Constants.LAST_VISIT_DATE + "`" + " = " + "'" + formatForVisitDate.format(date) + "'" + " WHERE "
                    + "`" + Constants.USER_NAME_HASH + "`" + " = " + "'" +userName_textField.getText().hashCode() + "'" + ";");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Enter pressed handler
     *
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

    //get user info snd give it to transfer info class
    private void getUserInfo() {
        UserDataTransfer.userName = userName_textField.getText();
        UserDataTransfer.password = password_passwordField.getText();
    }


}

