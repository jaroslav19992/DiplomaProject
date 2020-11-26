package TestMaker.LoginWindow.NetworkSettings;

import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.DBTools.Configs;
import TestMaker.DBTools.DefaultConfigs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class NetworkSettingsController {

    @FXML
    private AnchorPane main_pane;

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
    void initialize() throws IOException {
        NetworkSettingsConfigsReader properties = new NetworkSettingsConfigsReader();
        error_label.setVisible(false);

        getConfigsFromFile(properties);
        setConfigsToTextFields();

        resetToDefaults_button.setOnAction(event -> setDefaultConfigValues(properties));

        apply_button.setOnAction(event -> {
            if (checkForCorrectValues())
            setUserConfigValues(properties);
        });

        ok_button.setOnAction(event -> {
            apply_button.fire();
            password_textField.getScene().getWindow().hide();
        });

        cancel_button.setOnAction(event -> password_textField.getScene().getWindow().hide());
    }

    /**
     * Insert TestMaker.Assets.configs to text fields from TestMaker.Assets.configs file
     */
    private void setConfigsToTextFields() {
        host_textField.setText(Configs.dbHost);
        port_textField.setText(Configs.dbPort);
        user_textField.setText(Configs.dbUser);
        password_textField.setText(Configs.dbPassword);
        databaseName_textField.setText(Configs.dbName);
    }

    /**
     * Get TestMaker.Assets.configs from file to Configs.java
     * @param properties TestMaker.Assets.configs reader/writer class
     */
    private void getConfigsFromFile(NetworkSettingsConfigsReader properties) {
        Configs.dbHost = properties.getConfig("dbHost");
        Configs.dbPort = properties.getConfig("dbPort");
        Configs.dbUser = properties.getConfig("dbUser");
        Configs.dbPassword = properties.getConfig("dbPassword");
        Configs.dbName = properties.getConfig("dbName");
    }

    /** TODO: доробити перевірки всякі
     * @return is user insert correct values into text fields
     */
    private boolean checkForCorrectValues() {
        return true;
    }

    /**
     * set default config values and show label
     * @param properties TestMaker.Assets.configs reader/writer class
     */
    private void setUserConfigValues(NetworkSettingsConfigsReader properties) {
        properties.setConfig("dbHost", (!host_textField.getText().equals(""))?host_textField.getText():DefaultConfigs.default_dbHost);
        properties.setConfig("dbPort", (!port_textField.getText().equals(""))?port_textField.getText():DefaultConfigs.default_dbPort);
        properties.setConfig("dbUser", (!user_textField.getText().equals(""))?user_textField.getText():DefaultConfigs.default_dbUser);
        properties.setConfig("dbPassword", (!password_textField.getText().equals(""))?password_textField.getText():DefaultConfigs.default_dbPassword);
        properties.setConfig("dbName", (!databaseName_textField.getText().equals(""))?databaseName_textField.getText():DefaultConfigs.default_dbName);
        properties.writeConfigs();
        getConfigsFromFile(properties);
        setConfigsToTextFields();

        error_label.setTextFill(Color.GREEN);
        error_label.setText("Встановлено нові параметри");
        error_label.setVisible(true);

        //-------------------DEBUG-------------------//
        System.out.println("----------------------------\n"+
                "Host: " + Configs.dbHost +
                "; Port: " + Configs.dbPort +
                "; User: " + Configs.dbUser +
                "; Password: " + Configs.dbPassword +
                "; DBName: " + Configs.dbName +
                "\n----------------------------");
        //-------------------DEBUG-------------------//
    }

    /**
     * set default config values and show label
     * @param properties TestMaker.Assets.configs reader/writer class
     */
    private void setDefaultConfigValues(NetworkSettingsConfigsReader properties) {
        properties.setConfig("dbHost", DefaultConfigs.default_dbHost);
        properties.setConfig("dbPort", DefaultConfigs.default_dbPort);
        properties.setConfig("dbUser", DefaultConfigs.default_dbUser);
        properties.setConfig("dbPassword", DefaultConfigs.default_dbPassword);
        properties.setConfig("dbName", DefaultConfigs.default_dbName);
        properties.writeConfigs();
        getConfigsFromFile(properties);
        setConfigsToTextFields();

        error_label.setTextFill(Color.GREEN);
        error_label.setText("Встановлені параметри за замовчанням");
        error_label.setVisible(true);
    }

}
