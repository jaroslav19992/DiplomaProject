package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTest;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

public class PassingTestPaneController implements TestsConstants {
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
    private ArrayList<ArrayList<String>> userAnswers;

    @FXML
    public void initialize() {
        attempt_label.setVisible(false);
        setButtonActions();
        Platform.runLater(() -> {
            mainPane.getScene().getWindow().setOnCloseRequest(this::cancelTestConfirmation);
        });
    }

    public void setPageFactory() {
        pagination.setPageCount(questionsList.size());
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Creating page with question.
     * Creating pane, open base question pane and allow it to choose type of question itself after using set question method
     * When user chose some page data from previous page loading to the userAnswers arrayList
     * @param currentPageIndex index of just chosen page
     * @return pane with question
     */
    private Node createPage(Integer currentPageIndex) {
        BorderPane mainQuestionPane = new BorderPane();
        WindowTools windowTools = new WindowTools();
        TestingQuestionBaseController previousPageController = currentPageController;
        if (previousPageController != null) {
            userAnswers.set(previousPageIndex, previousPageController.getAnswerVariants());
        }
        currentPageController = (TestingQuestionBaseController) windowTools.setUpNewPaneOnBorderPane(mainQuestionPane,
                "/TestMaker/MainProgramWindow/Panes/TestsPane/PupilPane/PassingTestPane/TestingQuestionBase.fxml");

        //load question information to current page
        currentPageController.setQuestion(questionsList.get(currentPageIndex).getQuestionType(),
                questionsList.get(currentPageIndex).getQuestionScore(),
                questionsList.get(currentPageIndex).getQuestionText(),
                questionsList.get(currentPageIndex).getQuestionVariants(),
                (userAnswers.get(currentPageIndex).isEmpty() && questionsList.get(currentPageIndex).getQuestionType().equals(COMPLIANCE_QUESTION)
                        ? (questionsList.get(currentPageIndex).getAnswerVariants())
                        : (userAnswers.get(currentPageIndex))));

        previousPageIndex = currentPageIndex;
        return mainQuestionPane;
    }

    private void setButtonActions() {
        cancelTesting_button.setOnAction(this::cancelTestConfirmation);

        finishTesting_button.setOnAction(event -> {
            userAnswers.set(pagination.getCurrentPageIndex(), currentPageController.getAnswerVariants());
            if (checkQuestions()) {
                finishTesting();
            }
        });
    }

    //Ask user is he/she really wants to close test
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

    /**
     * Creates array list where all elements are null
     *
     * @param size number of elements in array list
     * @return created array list
     */
    public static ArrayList<ArrayList<String>> createPrefilledList(int size) {
        ArrayList<ArrayList<String>> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(new ArrayList<>());
        }
        return list;
    }

    private boolean checkQuestions() {
        return true;
    }

    /**
     * Creating timer thread if time limit is set or hide timer if not
     *
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
                                    Thread.currentThread().wait(1000);
                                    System.out.println("Time out");
                                    this.interrupt();
                                } else if (seconds == 0) {
                                    Thread.currentThread().wait(1000);
                                    minutes--;
                                    seconds = 59;
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
        timerThread.start();
    }

    public void stopTimer() {
        timerThread.interrupt();
    }

    /**
     * Show timer in timer label
     *
     * @param minutes minutes
     * @param seconds seconds
     */
    private void showTime(int minutes, int seconds) {
        String minutesString = (minutes < 10) ? ("0" + minutes) : String.valueOf((minutes));
        String secondsString = (seconds < 10) ? ("0" + seconds) : String.valueOf((seconds));
        timer_label.setText("Залишок часу: " + minutesString + ":" + secondsString);
    }

    public void setTest(TestMakerTest test) {
        this.currentTest = test;
        if (test.getNumberOfAttempts() ==  1) {
            attempt_label.setVisible(false);
        } else {
            attempt_label.setVisible(true);
            attempt_label.setText("Спроба " + (test.getCurrentUserUsedAttempts() + 1) + " з " + test.getNumberOfAttempts());
        }
        this.questionsList = test.getTestQuestions();
        shuffleVariants(questionsList);
        userAnswers = createPrefilledList(questionsList.size());
    }

    /**
     * Randomly re situated variants in questions variants list, if this is one or several answers questions, OR
     * in answer variants list if this is compliance question
     * @param questionsList
     */
    private void shuffleVariants(ArrayList<Question> questionsList) {
        for (Question question: questionsList) {
            if (question.getQuestionType().equals(COMPLIANCE_QUESTION)) {
                Collections.shuffle(question.getAnswerVariants(), new Random(System.currentTimeMillis()));
            } else {
                Collections.shuffle(question.getQuestionVariants(), new Random(System.currentTimeMillis()));
            }
        }
    }

    private void finishTesting() {
        for (ArrayList<String> list: userAnswers) {
            for(String str: list) {
                System.out.print(str+", ");
            }
            System.out.println();
        }
    }
}
