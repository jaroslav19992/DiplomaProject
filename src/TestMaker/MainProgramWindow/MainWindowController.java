package TestMaker.MainProgramWindow;

import TestMaker.Assets.Animation.LoadingAnimation;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Optional;

public class MainWindowController extends WindowConstants {

    private static final String CLOSE_WINDOW_CONFIRMATION_CONTENT_TEXT = "Ви дійсно хочете закрити програму TestMaker?";
    private static final String CLOSE_WINDOW_CONFIRMATION_TITLE = "Підтвердіть дію";

    @FXML
    private AnchorPane rightSide_anchorPane;

    @FXML
    private SplitPane main_pane;

    @FXML
    private StackPane tests_stackPane;

    @FXML
    private StackPane journal_stackPane;

    @FXML
    private StackPane userInfo_stackPane;

    @FXML
    private StackPane settings_stackPane;

    @FXML
    private BorderPane main_borderPane;
    private Thread loadingThread;
    private LoadingAnimation loadingAnimation;

    @FXML
    void initialize() {
        setOnHoverColorChange();
        setUpSceneOnClickChange();
        Platform.runLater(this::setOnWindowClosed);
    }


    /**
     * Change scene when another menu item clicked
     */
    private void setUpSceneOnClickChange() {
        WindowTools windowTools = new WindowTools();
        userInfo_stackPane.setOnMouseClicked(event -> {
            loadingThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(rightSide_anchorPane);
                loadingAnimation.start();
                Platform.runLater(() -> {
                    if (UserInfoHandler.accessToken.equals("teacherAT")) {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/UserInfoPane/TeacherPane/TeacherUserInfoPane.fxml");
                    } else if (UserInfoHandler.accessToken.equals("pupilAT")) {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/UserInfoPane/PupilPane/PupilUserInfoPane.fxml");
                    }
                });
                loadingAnimation.interrupt();
            });
            loadingThread.start();
        });

        journal_stackPane.setOnMouseClicked(event -> {
            loadingThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(rightSide_anchorPane);
                loadingAnimation.start();
                Platform.runLater(() -> {
                    if (UserInfoHandler.accessToken.equals("teacherAT")) {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/JournalPane/TeacherJournalPane/TeacherJournalPane.fxml");
                    } else {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/JournalPane/PupilJournalPane/PupilJournalPane.fxml");
                    }
                });
                loadingAnimation.interrupt();
            });
            loadingThread.start();
        });
        tests_stackPane.setOnMouseClicked(event -> {
            loadingThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(rightSide_anchorPane);
                loadingAnimation.start();
                Platform.runLater(() -> {
                    if (UserInfoHandler.accessToken.equals("teacherAT")) {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/TeacherTestsPane.fxml");
                    } else {
                        windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                                "/TestMaker/MainProgramWindow/Panes/TestsPane/PupilPane/PupilTestsPane.fxml");
                    }
                });
                loadingAnimation.interrupt();
            });
            loadingThread.start();
        });/*
        settings_stackPane.setOnMouseClicked(event -> {
            loadingThread = new Thread(() -> {
                loadingAnimation = new LoadingAnimation(rightSide_anchorPane);
                loadingAnimation.start();
                Platform.runLater(() -> windowTools.setUpNewPaneOnBorderPane(main_borderPane,
                        "/TestMaker/MainProgramWindow/Panes/SettingsPane/SettingsPane.fxml"));
                loadingAnimation.interrupt();
            });
            loadingThread.start();
        });*/
    }

    /**
     * Highlights elements when mouse is over them
     */
    private void setOnHoverColorChange() {
        userInfo_stackPane.setOnMouseEntered(event -> userInfo_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR));
        userInfo_stackPane.setOnMouseExited(event -> userInfo_stackPane.setStyle(""));

        tests_stackPane.setOnMouseEntered(event -> tests_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR));
        tests_stackPane.setOnMouseExited(event -> tests_stackPane.setStyle(""));

        journal_stackPane.setOnMouseEntered(event -> journal_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR));
        journal_stackPane.setOnMouseExited(event -> journal_stackPane.setStyle(""));

        settings_stackPane.setOnMouseEntered(event -> settings_stackPane.setStyle("-fx-background-color: " + ACTIVE_ITEM_COLOR));
        settings_stackPane.setOnMouseExited(event -> settings_stackPane.setStyle(""));
    }

    public void setOnWindowClosed() {
        main_pane.getScene().getWindow().setOnCloseRequest(event -> {
            Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            closeConfirmation.setHeaderText(null);
            closeConfirmation.setTitle(CLOSE_WINDOW_CONFIRMATION_TITLE);
            closeConfirmation.setContentText(CLOSE_WINDOW_CONFIRMATION_CONTENT_TEXT);
            ButtonType close = new ButtonType("Закрити");
            ButtonType cancel = new ButtonType("Відміна");
            closeConfirmation.getButtonTypes().clear();
            closeConfirmation.getButtonTypes().addAll(cancel, close);
            Optional<ButtonType> selection = closeConfirmation.showAndWait();
            if (selection.get() == cancel) {
                event.consume();
            } else {
                System.exit(0);
            }
        });
    }

}
