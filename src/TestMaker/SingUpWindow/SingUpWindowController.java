package TestMaker.SingUpWindow;


import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.ClassRooms;
import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.SingUpWindow.AccessWindow.AccessWindowController;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SingUpWindowController {
    @FXML
    public TextField lastName_textField;
    @FXML
    public TextField firstName_textField;
    @FXML
    private TextField passwordRepeat_textField;
    @FXML
    public TextField email_textField;
    @FXML
    public TextField password_textField;
    @FXML
    private Label error_label;
    @FXML
    public TextField userName_textField;
    @FXML
    private RadioButton radioButton_teacher;
    @FXML
    private Button singUpButton;
    @FXML
    private ChoiceBox<String> class_choiceBox;
    @FXML
    public AnchorPane main_pane;

    public Thread singUpThread;
    public LoadingAnimation loadingAnimation;
    private final WindowTools windowTools = new WindowTools();
    private boolean isSingingUp = false;

    //TODO: Animation cause not javafx thread exception
    @FXML
    public void initialize() {
        loadingAnimation = new LoadingAnimation(main_pane);
        //set keys listener
        setGlobalEventHandler(main_pane);

        //set no error
        error_label.setVisible(false);

        setClassesVariants();

        //SingUp button click
        singUpButton.setOnAction(event -> {
            singUpThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(main_pane);
                loadingAnimation.start();
                singUpNewUser();
                loadingAnimation.interrupt();
            });
            singUpThread.start();
        });
    }


    //TODO побутися windowsTools. відкрити тут самому вікно получаючи екземпляр класу і в нього
    // передати дії кнопки які реєструють юзера
    private void singUpNewUser() {
        try {
            if (checkForCorrectInfo()) {
                error_label.setVisible(false);
                if (radioButton_teacher.isSelected()) {
                    Platform.runLater(() -> {
                            AccessWindowController controller = (AccessWindowController) windowTools.openNewWindow("SingUpWindow/AccessWindow/AccessWindow.fxml",
                                    false, Modality.APPLICATION_MODAL);
                            controller.giveAccess(this);
                    });
                } else {
                    UserInfoHandler.isAccessGained = true;
//                    loadingAnimation.start();
                    Platform.runLater(() -> {
                        transferUserInfo();
                        try {
                            createUserInDB();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                        WindowTools windowTools = new WindowTools();
                        windowTools.openNewWindow("MainProgramWindow/MainWindow.fxml", true, Modality.NONE);
                        main_pane.getScene().getWindow().hide();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loadingAnimation.interrupt();
                        singUpThread.interrupt();
                    });
                }

            } else {
            Thread.sleep(100);
            loadingAnimation.interrupt();
            singUpThread.interrupt();
            }
        } catch (SQLException | InterruptedException exception) {
            Platform.runLater(() -> {
                error_label.setVisible(false);
                exception.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText("Помилка з'єднання з сервером\nПричина:\n" + exception.getMessage());
                alert.showAndWait();
                loadingAnimation.interrupt();
                singUpThread.interrupt();
            });
        }
    }

    /**
     * Register user with pupil or teacher access token
     */
    public void createUserInDB() throws SQLException {
        if (UserInfoHandler.isAccessGained) {
            //get register and visit date
            Date date = new Date();
            SimpleDateFormat formatForRegDate = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            DBHandler.singUpNewUser(UserInfoHandler.userName, UserInfoHandler.password, UserInfoHandler.firstName,
                    UserInfoHandler.lastName, UserInfoHandler.email, UserInfoHandler.classroom, UserInfoHandler.accessToken,
                    formatForRegDate.format(date), formatForVisitDate.format(date));
        }
    }

    //get user info snd give it to transfer info class
    public void transferUserInfo() {
        UserInfoHandler.userName = userName_textField.getText();
        UserInfoHandler.password = password_textField.getText();
        UserInfoHandler.firstName = firstName_textField.getText();
        UserInfoHandler.lastName = lastName_textField.getText();
        UserInfoHandler.email = email_textField.getText();
        UserInfoHandler.classroom = class_choiceBox.getValue();
        if (radioButton_teacher.isSelected()) {
            UserInfoHandler.accessToken = DBConstants.TEACHER_ACCESS_TOKEN;
        } else {
            UserInfoHandler.accessToken = DBConstants.PUPIL_ACCESS_TOKEN;

        }
    }

    /**
     * set evaluation system variants and set 12-as default
     */
    private void setClassesVariants() {
        ObservableList<String> evaluationSystemsList = FXCollections.observableArrayList(ClassRooms.getClassesList());
        class_choiceBox.setItems(evaluationSystemsList);
    }

    /**
     * Check is user insert correct information
     *
     * @return is user can register
     */
    private boolean checkForCorrectInfo() throws SQLException {
        /*-------------------------------Empty fields check-----------------------------*/
        if (email_textField.getText().equals("") || password_textField.getText().equals("") || firstName_textField.getText().equals("") ||
                lastName_textField.getText().equals("") || userName_textField.getText().equals("") || class_choiceBox.getValue() == null) {
            showError("Заповніть усі поля");
            return false;
        }
        /*-------------------------------Empty fields check-----------------------------*/


        /*-------------------------------Password check-----------------------------*/
        if (!password_textField.getText().equals(passwordRepeat_textField.getText())) {
            showError("Паролі не зпівпадають");
            return false;
        }
        if (validateText(password_textField.getText())) {
            showError("Пароль не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < > .");
            return false;
        }
        if (password_textField.getText().length() < 8 || password_textField.getText().length() > 20) {
            showError("Пароль повинен містити не менше ніж 8 символів і не більше ніж 20");
            return false;
        }
        if (isOnlyLatLetters(password_textField.getText())) {
            showError("Пароль повинен містити лише букви латинського алфавіту та не містити пробілів");
            return false;
        }
        /*-------------------------------Password check-----------------------------*/


        /*-------------------------------Username check-----------------------------*/
        if (validateText(userName_textField.getText())) {
            showError("Логін не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < >");
            return false;
        }
        if (userName_textField.getText().length() > 20 || userName_textField.getText().length() < 5) {
            showError("Логін не повинен складатися менше ніж з 5 і більше ніж з 20 симаолів");
            return false;
        }
        if (isOnlyLatLetters(userName_textField.getText())) {
            showError("Логін повинен містити лише букви латинського алфавітута не містити пробілів");
            return false;
        }
        /*-------------------------------Username check-----------------------------*/


        /*-------------------------------E-mail check-----------------------------*/
        if (!email_textField.getText().contains("@") || !email_textField.getText().contains(".")) {
            showError("Не вірний формат E-mail");
            return false;
        }
        if (validateText(email_textField.getText())) {
            showError("Не вірний формат E-mail");
            return false;
        }
        /*-------------------------------E-mail check-----------------------------*/

        /*----------------------------Check if no user data duplicates-----------------------------*/
        //create queries
        String SQLQueryForUsername = "SELECT " + DBConstants.USER_NAME_HASH + " FROM " + DBConstants.USERS_INFO_TABLE_NAME + " WHERE " + DBConstants.USER_NAME_HASH + " = "
                + userName_textField.getText().hashCode();
        String SQLQueryForEmail = "SELECT " + DBConstants.EMAIL + " FROM " + DBConstants.USERS_INFO_TABLE_NAME + " WHERE " + DBConstants.EMAIL + " = "
                + "\"" + email_textField.getText() + "\"";

        //username check
        if (DBHandler.getDataFromDB(SQLQueryForUsername).next()) {
            showError("Користувач з таким ім'ям уже існує");
            return false;
        }
        //e-mail check
        if (DBHandler.getDataFromDB(SQLQueryForEmail).next()) {
            showError("Даний E-mail уже використовується");
            return false;
        }
        /*----------------------------Check if no user data duplicates-----------------------------*/
        return true;
    }

    private void showError(String msg) {
        Platform.runLater(() -> {
            error_label.setText(msg);
            error_label.setVisible(true);
        });
    }

    //check is there is no bad symbols
    public boolean validateText(String str) {
        return str.contains("\\") && !str.contains("/") && !str.contains("\"") && !str.contains("[") && !str.contains("]")
                && !str.contains(";") && !str.contains(":") && !str.contains("=") && !str.contains(",") && !str.contains("+")
                && !str.contains("*") && !str.contains("?") && !str.contains("<") && !str.contains(">");
    }


    //check is there is only Latin alphabet letters or not
    public boolean isOnlyLatLetters(String str) {
        for (char c : str.toCharArray()) {
            if (c != ' ') {
                return false;
            }
            if (!String.valueOf(c).matches("([а-я]|[А-Я])")) {
                return false;
            }
        }
        return true;
    }


    /**
     * Keys pressed handler
     *
     * @param root object, add handler to
     */
    private void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (!isSingingUp) {
                if (ev.getCode() == KeyCode.ENTER) {
                    isSingingUp = true;
                    singUpButton.fire();
                    ev.consume();
                }
            }
            if (ev.getCode() == KeyCode.ESCAPE) {
                windowTools.openNewWindow("LoginWindow/LoginWindow.fxml", false, Modality.NONE);
                main_pane.getScene().getWindow().hide();
            }
        });
    }

    public void setOnCloseRequest() {
        main_pane.getScene().getWindow().setOnCloseRequest(event -> {
            windowTools.openNewWindow("LoginWindow/LoginWindow.fxml", false, Modality.APPLICATION_MODAL);
            windowTools.closeCurrentWindow(main_pane);
            Thread.currentThread().interrupt();
            if (singUpThread != null) {
                singUpThread.interrupt();
            }
            loadingAnimation.interrupt();
        });
    }
}
