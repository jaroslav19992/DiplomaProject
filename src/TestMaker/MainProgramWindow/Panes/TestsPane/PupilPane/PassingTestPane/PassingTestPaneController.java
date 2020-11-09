package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.QuestionBaseController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTest;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class PassingTestPaneController {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button cancelTesting_button;
    @FXML
    private Pagination pagination;
    @FXML
    private Label timer_label;
    @FXML
    private Label attempt_label;
    @FXML
    private Button finishTesting_button;
    private static final String CANCEL_TEST_PASSING_ALERT_CONTEXT_TEXT = "Використану спробу не буде відновлено. " +
            "Якщо тест не має можливості повторного проходження ви отримаєте" +
            " результат складений на основі питань на які ви дали відповідь.\nПродовжити?";
    private TestMakerTest currentTest;
    private Thread timerThread;
    private TestingQuestionBaseController currentPageController;

    @FXML
    public void initialize() {
        setButtonActions();
        Platform.runLater(() -> {
            mainPane.getScene().getWindow().setOnCloseRequest(event -> {
                cancelTesting_button.fire();
            });
        });
    }

    public void setPageFactory() {
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(Integer currentPageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools windowTools = new WindowTools();
        TestingQuestionBaseController previousPageController = currentPageController;
        currentPageController = (TestingQuestionBaseController) windowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/CreationTestPane" +
                        "/QuestionsTypes/QuestionBase.fxml");
        //TODO: CONTINUE creation this block
        return null;
    }

    private void setButtonActions() {
        cancelTesting_button.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Скасувати проходження тесту?");
            alert.setContentText(CANCEL_TEST_PASSING_ALERT_CONTEXT_TEXT);
            ButtonType accept = new ButtonType("Продовжити");
            ButtonType cancel = new ButtonType("Відміна");
            alert.getButtonTypes().setAll(accept, cancel);
            Optional<ButtonType> selection = alert.showAndWait();
            if (selection.get() == accept) {
                finishTesting();
            } else {
                event.consume();
            }
        });

        finishTesting_button.setOnAction(event -> {
            if (checkQuestions()) {
                finishTesting();
            }
        });
    }

    private boolean checkQuestions() {
        return false;
    }

    /**
     * Creating timer thread if time limit is set or hide timer if not
     * @param timeLimit start count time
     */
    public void setTimer(final int timeLimit) {
        timer_label.setText("Залишок часу: ..:..");
        if (timeLimit == 0) {
            timer_label.setVisible(false);
        } else {
            timerThread = new Thread() {
                int minutes = timeLimit;
                int seconds = 0;
                @Override
                public void run() {
                    synchronized (this) {
                        try {
                            while (true) {
                                Thread.currentThread().wait(1000);
                                if (minutes == 0 && seconds == 0) {
                                    System.out.println("Time out");
                                    this.interrupt();
                                }
                                else if (seconds == 0) {
                                    minutes--;
                                    seconds = 60;
                                }

                                Platform.runLater(() -> showTime(minutes, seconds));
                                seconds--;
                            }
                        } catch (InterruptedException e) {
                            finishTesting();
                        }
                    }
                }
            };
            timerThread.start();
        }
    }

    public void startTimer() {
        timerThread.run();
    }
    public void stopTimer() {
        timerThread.interrupt();
    }

    /**
     * Show timer in timer label
     * @param minutes minutes
     * @param seconds
     */
    private void showTime(int minutes, int seconds) {
        String minutesString = (minutes<10)?("0"+minutes): String.valueOf((minutes));
        String secondsString = (seconds<10)?("0"+seconds): String.valueOf((seconds));
        timer_label.setText("Залишок часу: " + minutesString + ":" + secondsString);
    }

    private void finishTesting() {

    }
}
