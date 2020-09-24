package TestMaker.LoginWindow.NetworkSettings;

import TestMaker.DBTools.Configs;
import TestMaker.DBTools.DefaultConfigs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class NetworkSettingsController {

    @FXML
    private Button cancel_button;

    @FXML
    private TextField user_textField;

    @FXML
    private Button resetToDefaults_button;

    @FXML
    private TextField port_textField;

    @FXML
    private TextField databaseName_textField;

    @FXML
    private TextField password_textField;

    @FXML
    private Button apply_button;

    @FXML
    private TextField host_textField;

    @FXML
    private Label error_label;

    @FXML
    private Button ok_button;

    @FXML
    void initialize(){
        error_label.setVisible(false);
        resetToDefaults_button.setOnAction(event -> {
            setDefaultConfigValues();
        });

        apply_button.setOnAction(event -> {
            if (checkForCorrectValues())
            setUserConfigValues();
        });

        ok_button.setOnAction(event -> {
            apply_button.fire();
            password_textField.getScene().getWindow().hide();
        });
    }

    /** TODO: доробити перевірки всякі
     * @return is user insert correct values into text fields
     */
    private boolean checkForCorrectValues() {
        if (host_textField.getText().equals("") || port_textField.getText().equals("") || user_textField.getText().equals("") ||
                password_textField.getText().equals("") || databaseName_textField.getText().equals("")){
            error_label.setTextFill(Color.RED);
            error_label.setText("Залишилися не заповнені поля");
            error_label.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * set default config values and show label
     */
    private void setUserConfigValues() {
        Configs.dbHost = host_textField.getText();
        Configs.dbPort = port_textField.getText();
        Configs.dbUser = user_textField.getText();
        Configs.dbPassword = password_textField.getText();
        Configs.dbName = databaseName_textField.getText();
        error_label.setTextFill(Color.GREEN);
        error_label.setText("Встановлено нові параметри");
        error_label.setVisible(true);
    }

    /**
     * set default config values and show label
     */
    private void setDefaultConfigValues() {
        Configs.dbHost = DefaultConfigs.default_dbHost;
        Configs.dbPort = DefaultConfigs.default_dbPort;
        Configs.dbUser = DefaultConfigs.default_dbUser;
        Configs.dbPassword = DefaultConfigs.default_dbPassword;
        Configs.dbName = DefaultConfigs.default_dbName;
        error_label.setTextFill(Color.GREEN);
        error_label.setText("Встановлені параметри за замовчанням");
        error_label.setVisible(true);
    }

}
