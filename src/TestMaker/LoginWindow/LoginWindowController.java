package TestMaker.LoginWindow;

import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.DBTools.Configs;
import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.LoginWindow.NetworkSettings.NetworkSettingsConfigsReader;
import TestMaker.SingUpWindow.SingUpWindowController;
import TestMaker.UserDataLoader;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginWindowController {
    @FXML
    private ImageView openEye_imageView;
    @FXML
    private ImageView closedEye_imageView;
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
    private Button register_button;
    @FXML
    private TextField password_textField;
    @FXML
    private AnchorPane main_pane;
    @FXML
    private CheckBox rememberLogin_choiceBox;

    private static LoadingAnimation loadingAnimation;
    private static Thread loginThread;
    WindowTools windowTools = new WindowTools();
    private boolean isLogging;
    private File lastLoginFile;

    @FXML
    void initialize() {
        lastLoginActions();
        hiddenNetworkSettings();
        //Hide invalid sing in data label
        error_label.setVisible(false);
        //Set last saved configs
        getLastConfigs();
        //Enter pressed listener
        setGlobalEventHandler(main_pane);
        setButtonActions();
        setPasswordFieldBehavior();
        userName_textField.requestFocus();
        Platform.runLater(() -> main_pane.getScene().getWindow().setOnCloseRequest(event -> {
            rememberLastLogin();
            System.exit(0);
        }));
    }

    private void hiddenNetworkSettings() {
        networkSettings_button.setVisible(false);
        networkSettings_button.setDisable(true);
        if (userName_textField.getText().equals("admin") && password_textField.getText().equals("58Idovif")) {
            networkSettings_button.setVisible(true);
            networkSettings_button.setDisable(false);
        }
    }

    /**
     * Set last login
     */
    private void lastLoginActions() {
        try {
            lastLoginFile = new File("./configs/lastLogin.txt");
            FileReader fileReader = new FileReader(lastLoginFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lastLogin = bufferedReader.readLine();
            if (lastLogin != null) {
                rememberLogin_choiceBox.setSelected(true);
                userName_textField.setText(lastLogin);
            } else {
                rememberLogin_choiceBox.setSelected(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up visibility of password field
     */
    private void setPasswordFieldBehavior() {
        //Password visibility
        closedEye_imageView.setOnMouseClicked(event -> {
            password_textField.setVisible(true);
            password_passwordField.setVisible(false);
            password_textField.toFront();

            openEye_imageView.setVisible(true);
            closedEye_imageView.setVisible(false);
            openEye_imageView.toFront();
        });

        openEye_imageView.setOnMouseClicked(event -> {
            password_textField.setVisible(false);
            password_passwordField.setVisible(true);
            password_passwordField.toFront();

            openEye_imageView.setVisible(false);
            closedEye_imageView.setVisible(true);
            closedEye_imageView.toFront();

        });

        //Sync password fields
        password_passwordField.setOnKeyReleased(event -> password_textField.setText(password_passwordField.getText()));
        password_textField.setOnKeyReleased(event -> password_passwordField.setText(password_textField.getText()));
    }

    private void setButtonActions() {
        /*----------------------------Login button action-----------------------------*/
        login_button.setOnAction(event -> {
            isLogging = true;
            //creating another Thread from UI, starting animation, login user, stopping animation
            loginThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(main_pane);
                loadingAnimation.start();
                loginButtonAction();
                loadingAnimation.interrupt();
            });
            loginThread.start();
        });
        /*----------------------------Login button action-----------------------------*/


        /*----------------------------Registration button action-----------------------------*/
        register_button.setOnAction(event -> {
            SingUpWindowController windowController;
            windowController = (SingUpWindowController) windowTools.openNewWindow("SingUpWindow/SingUpWindow.fxml", false, Modality.NONE);
            windowController.setOnCloseRequest();
            main_pane.getScene().getWindow().hide();
        });
        /*----------------------------Registration button action-----------------------------*/


        /*----------------------------networkSettings button action-----------------------------*/

        networkSettings_button.setOnAction(event ->
                windowTools.openNewWindow("LoginWindow/NetworkSettings/NetworkSettings.fxml",
                        false, Modality.APPLICATION_MODAL));
        /*----------------------------networkSettings button action-----------------------------*/
    }

    /**
     *
     */
    private void getLastConfigs() {
        NetworkSettingsConfigsReader properties = null;
        try {
            properties = new NetworkSettingsConfigsReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert properties != null;
        Configs.dbHost = properties.getConfig("dbHost");
        Configs.dbPort = properties.getConfig("dbPort");
        Configs.dbUser = properties.getConfig("dbUser");
        Configs.dbPassword = properties.getConfig("dbPassword");
        Configs.dbName = properties.getConfig("dbName");
    }


    /**
     * What is happens when login button is pressed
     */
    private void loginButtonAction() {
        error_label.setVisible(false);
        rememberLastLogin();
        hiddenNetworkSettings();

        /* LogIn and Password Checker from DB */
        UserDataLoader loader = new UserDataLoader();
        if (!userName_textField.getText().isEmpty() && !password_textField.getText().isEmpty()) {
            try {
                loader.getUserData(userName_textField.getText().hashCode(),
                        password_passwordField.getText().hashCode());
                if (loader.isAccessGained()) {
                    setLastVisitDate();
                    UserInfoHandler.userName = userName_textField.getText();
                    UserInfoHandler.password = password_passwordField.getText();
                    //Open main program window
                    Platform.runLater(() -> {
                        windowTools.openNewWindow("MainProgramWindow/MainWindow.fxml", true, Modality.NONE);
                        loadingAnimation.interrupt();
                        loginThread.interrupt();
                        //Hide LogIn window
                        main_pane.getScene().getWindow().hide();
                    });

                    System.out.println("----------------------------\n" +
                            "connected via: \n" +
                            "Host: " + Configs.dbHost +
                            "; Port: " + Configs.dbPort +
                            "; User: " + Configs.dbUser +
                            "; Password: " + Configs.dbPassword +
                            "; DBName: " + Configs.dbName +
                            "\n----------------------------");
                } else {
                    Platform.runLater(() -> {
                        error_label.setText("Не правильний логін та/або пароль");
                        error_label.setVisible(true);
                        loginThread.interrupt();
                    });
                }
            } catch (Exception exception) {
                error_label.setVisible(false);
                Platform.runLater(() -> {
                    exception.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Помилка");
                    alert.setHeaderText(null);
                    alert.setContentText("Помилка з'єднання з сервером\nПричина:\n" + exception.getMessage());
                    alert.show();
                    loginThread.interrupt();
                });
            }
        } else {
            Platform.runLater(() -> {
                error_label.setText("Заповніть поля");
                error_label.setVisible(true);
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingAnimation.interrupt();
            loginThread.interrupt();
        }

    }

    private void rememberLastLogin() {
        try {
            if (rememberLogin_choiceBox.isSelected()) {
                FileOutputStream fooStream = new FileOutputStream(lastLoginFile, false); // true to append
                byte[] myBytes = userName_textField.getText().getBytes();                       // false to overwrite.
                fooStream.write(myBytes);
                fooStream.close();

            } else {
                FileWriter fwOb = new FileWriter(lastLoginFile, false);
                PrintWriter pwOb = new PrintWriter(fwOb, false);
                pwOb.flush();
                pwOb.close();
                fwOb.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up user last visit date
     */
    private void setLastVisitDate() {
        Date date = new Date();
        SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        try {
            DBHandler.loadDataToDB("UPDATE " + "`" + Configs.dbName + "`" + "." + "`" + DBConstants.USERS_INFO_TABLE_NAME + "`" +
                    " SET " + "`" + DBConstants.LAST_VISIT_DATE + "`" + " = " + "'" + formatForVisitDate.format(date) + "'" + " WHERE "
                    + "`" + DBConstants.USER_NAME_HASH + "`" + " = " + "'" + userName_textField.getText().hashCode() + "'" + ";");
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
                if (!isLogging) {
                    login_button.fire();
                }
                ev.consume();
            }
        });
    }
}

