package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTest;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
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
    private ArrayList<Question> questionsList;
    private Integer previousPageIndex;

    @FXML
    public void initialize() {
        setButtonActions();
        Platform.runLater(() -> {
            mainPane.getScene().getWindow().setOnCloseRequest(this::cancelTestConfirmation);
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
                "/TestMaker/MainProgramWindow/Panes/TestsPane/PupilPane/PassingTestPane/TestingQuestionBase.fxml");

        //If there is question on index [currentPageIndex] available load this question information to current page
        currentPageController.setQuestion(
                questionsList.get(currentPageIndex).getQuestionType(),
                questionsList.get(currentPageIndex).getQuestionScore(),
                questionsList.get(currentPageIndex).getQuestionText(),
                questionsList.get(currentPageIndex).getQuestionVariants(),
                questionsList.get(currentPageIndex).getAnswerVariants());

        previousPageIndex = currentPageIndex;
        return mainQuestionPane;
    }

    private void setButtonActions() {
        cancelTesting_button.setOnAction(this::cancelTestConfirmation);

        finishTesting_button.setOnAction(event -> {
            if (checkQuestions()) {
                finishTesting();
            }
        });
    }

    private void cancelTestConfirmation(Event event) {
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
                                Platform.runLater(() -> showTime(minutes, seconds));
                                Thread.currentThread().wait(1000);
                                if (minutes == 0 && seconds == 0) {
                                    System.out.println("Time out");
                                    this.interrupt();
                                }
                                else if (seconds == 0) {
                                    minutes--;
                                    seconds = 60;
                                } else {
                                    seconds--;
                                }
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

    public void setTest(TestMakerTest test) {
    this.currentTest = test;
    this.questionsList = test.getTestQuestions();
    }
}
