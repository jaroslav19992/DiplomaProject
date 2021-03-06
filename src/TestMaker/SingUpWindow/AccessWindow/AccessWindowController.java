package TestMaker.SingUpWindow.AccessWindow;


import TestMaker.SingUpWindow.SingUpWindowController;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;

import java.sql.SQLException;

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

    private final String teacherAccessKey = "THISISTEACHERACCESSKEY1234"; //TODO: this is shit...
    private SingUpWindowController singUpWindowController;

    @FXML
    public void initialize() {
        setGlobalEventHandler(main_pane);
        error_label.setVisible(false);
        accept_button.setOnAction(event -> {
            if (isAccessGained(accessKey_textField.getText())) {
                UserInfoHandler.isAccessGained = true;
                main_pane.getScene().getWindow().hide();
//                singUpWindowController.loadingAnimation.start();
                Platform.runLater(() -> {
                    singUpWindowController.transferUserInfo();
                    try {
                        singUpWindowController.createUserInDB();
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
//                    singUpWindowController.loadingAnimation.interrupt();
//                    singUpWindowController.singUpThread.interrupt();
                });
            } else {
                UserInfoHandler.isAccessGained = false;
                error_label.setVisible(true);
            }
        });
        //set up close button
        cancel_button.setOnAction(event1 -> {
            UserInfoHandler.isAccessGained = false;
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


    public void giveAccess(SingUpWindowController singUpWindowController) {
        this.singUpWindowController = singUpWindowController;
    }
}
