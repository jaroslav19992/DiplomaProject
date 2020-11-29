package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.Main;
import TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.TestResultsPane.TestResultsPaneController;
import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.UserInfoHandler;
import TestMaker.WindowTools;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class PassingTestPaneController implements TestsConstants {
    @FXML
    private Label testName_label;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button cancelTesting_button;
    @FXML
    private Pagination pagination;
    @FXML
    private Label timer_label;
    @FXML
    private StackPane content_stackPane;
    @FXML
    private Label attempt_label;
    @FXML
    private Button finishTesting_button;
    private static final String RESULTS_BACKGROUND_COLOR = "f5f5f5";
    private static final String CANCEL_TEST_PASSING_ALERT_CONTEXT_TEXT = "Використану спробу не буде відновлено. " +
            "Якщо тест не має можливості повторного проходження ви отримаєте" +
            " результат складений на основі питань на які ви дали відповідь.\nПерервати тестуавння?";
    private static final String EMPTY_QUESTION_ALERT_CONTEXT_TEXT = "Одне або декілька питань не мають варіанту відповіді";
    private TestMakerTest currentTest;
    private Thread timerThread;
    private TestingQuestionBaseController currentPageController;
    private ArrayList<Question> questionsList;
    private Integer previousPageIndex;
    private ArrayList<ArrayList<String>> userAnswers;
    //used for save correct order of compliance answer variants
    private ArrayList<ArrayList<String>> notShuffledAnswers;
    private boolean isAnswersShown = false;

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

    public Pane getMainPane() {
        return mainPane;
    }

    /**
     * Creating page with question.
     * Creating pane, open base question pane and allow it to choose type of question itself after using set question method
     * When user chose some page data from previous page loading to the userAnswers arrayList
     *
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

        //Show correct and wrong answers
        if (isAnswersShown) {
            if (questionsList.get(currentPageIndex).getQuestionType().equals(COMPLIANCE_QUESTION)) {
                currentPageController.showAnswers(notShuffledAnswers.get(currentPageIndex));
            } else {
                currentPageController.showAnswers(questionsList.get(currentPageIndex).getAnswerVariants());
            }
        }
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
        ButtonType accept = new ButtonType("Перервати");
        ButtonType cancel = new ButtonType("Продовжити");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Завершити проходження тесту?");
        alert.setContentText(null);
        for (ArrayList<String> questionAnswerList : userAnswers) {
            if (questionAnswerList.isEmpty()) {
                alert.setContentText(EMPTY_QUESTION_ALERT_CONTEXT_TEXT);
                ButtonType finishTesting = new ButtonType("Завершити тестування");
                ButtonType showQuestion = new ButtonType("Перейти до питання");
                alert.getButtonTypes().setAll(finishTesting, showQuestion);
                Optional<ButtonType> selection = alert.showAndWait();
                if (selection.get() == finishTesting) {
                    return true;
                } else {
                    pagination.setCurrentPageIndex(userAnswers.indexOf(questionAnswerList));
                    return false;
                }
            }
        }
        ButtonType finishTesting = new ButtonType("Завершити тестування");
        ButtonType continueTesting = new ButtonType("Продовжити тестування");
        alert.getButtonTypes().setAll(finishTesting, continueTesting);
        Optional<ButtonType> selection = alert.showAndWait();
        return selection.get() == finishTesting;
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
                                if (minutes == 0 && seconds <= 30) {
                                    timer_label.setTextFill(Color.RED);
                                }
                                if (minutes == 0 && seconds == 0) {
                                    Thread.currentThread().wait(1000);
                                    Platform.runLater(() -> {
                                        finishTesting();
                                    });
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
                            System.out.println("Timer was stopped");
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
        testName_label.setText(test.getTestName());
        if (test.getNumberOfAttempts() == 1) {
            attempt_label.setVisible(false);
        } else {
            attempt_label.setVisible(true);
            attempt_label.setText("Спроба " + (test.getCurrentUserUsedAttempts() + 1) + " з " + test.getNumberOfAttempts());
        }
        this.questionsList = test.getTestQuestions();

        userAnswers = createPrefilledList(questionsList.size());
        //used for save correct order of compliance answer variants
        notShuffledAnswers = createPrefilledList(questionsList.size());

        shuffleVariants(questionsList);
    }

    /**
     * Randomly re situated variants in questions variants list, if this is one or several answers questions, OR
     * in answer variants list if this is compliance question
     *
     * @param list list witch elements will be shuffle
     */
    private void shuffleVariants(ArrayList<Question> list) {
        for (Question question : list) {
            if (question.getQuestionType().equals(COMPLIANCE_QUESTION)) {
                ArrayList<String> notShuffledAnswerVariants = new ArrayList<>(question.getAnswerVariants());
                notShuffledAnswers.set(questionsList.indexOf(question), notShuffledAnswerVariants);
                Collections.shuffle(question.getAnswerVariants(), new Random(System.currentTimeMillis()));
            } else {
                Collections.shuffle(question.getQuestionVariants(), new Random(System.currentTimeMillis()));
            }
        }
    }

    private void finishTesting() {
        if (timerThread != null) {
            stopTimer();
        }
        //Get test score
        double score = 0;
        for (Question question : questionsList) {
            if (question.getQuestionType().equals(COMPLIANCE_QUESTION)) {
                //if userAnswerVariants array list equal questionAnswerVariants array list
                if (notShuffledAnswers.get(questionsList.indexOf(question)).
                        equals(userAnswers.get(questionsList.indexOf(question)))) {
                    score += question.getQuestionScore();
                }
            } else {
                HashSet<String> answers = new HashSet<>(userAnswers.get(questionsList.indexOf(question)));
                int oldSize = answers.size();
                answers.addAll(question.getAnswerVariants());
                int newSize = answers.size();
                if (oldSize == newSize) {
                    score += question.getQuestionScore();
                }
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("###.00", symbols);
        score = Double.parseDouble(decimalFormat.format(score));
        //Increment user used attempts
        currentTest.setCurrentUserUsedAttempts(currentTest.getCurrentUserUsedAttempts() + 1);

        //Load data to DB
        String SQLQuery = "UPDATE `" + DBConstants.DB_NAME + "`.`" + DBConstants.PUPILS_TESTS_TABLE_NAME + "` SET `"
                + DBConstants.MARK + "` = '" + score + "', `" + DBConstants.USED_ATTEMPTS + "` = '"
                + currentTest.getCurrentUserUsedAttempts() + "' WHERE (`" + DBConstants.USER_NAME_HASH
                + "` = '" + UserInfoHandler.userName.hashCode() + "') and (`" + DBConstants.ID_TESTS_LIST
                + "` = '" + currentTest.getIdInTestsList() + "')";
        try {
            DBHandler.loadDataToDB(SQLQuery);
        } catch (SQLException exception) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Помилка з'єднання з базою даних");
            alert.setContentText(null);
            alert.showAndWait();
        }

        //Show test score on new pane which will cover testing pane
        AnchorPane resultsAnchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(resultsAnchorPane, 0.0);
        AnchorPane.setTopAnchor(resultsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(resultsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(resultsAnchorPane, 0.0);
        resultsAnchorPane.setStyle("-fx-background-color: " + RESULTS_BACKGROUND_COLOR + ";");

        VBox contentVBox = new VBox();
        AnchorPane.setBottomAnchor(contentVBox, 0.0);
        AnchorPane.setTopAnchor(contentVBox, 0.0);
        AnchorPane.setLeftAnchor(contentVBox, 0.0);
        AnchorPane.setRightAnchor(contentVBox, 0.0);
        contentVBox.setAlignment(Pos.CENTER);
        resultsAnchorPane.getChildren().add(contentVBox);

        mainPane.getChildren().add(resultsAnchorPane);
        resultsAnchorPane.toFront();
        content_stackPane.toBack();
        TestResultsPaneController controller = new TestResultsPaneController(resultsAnchorPane);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                "PupilPane/PassingTestPane/TestResultsPane/TestResultsPane.fxml"));
        loader.setController(controller);
        try {
            Parent parent = loader.load();
            contentVBox.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.setTestResults(score, currentTest.getEvSystem(),
                currentTest.getNumberOfAttempts() - currentTest.getCurrentUserUsedAttempts());
        //Give access to results test pane controller to have possibility use it's buttons
        controller.giveAccess(this);
    }

    /**
     * Hide cancel test  and finish testing button
     */
    public void removeTestButtons() {
        cancelTesting_button.setVisible(false);
        cancelTesting_button.setDisable(true);
        finishTesting_button.setVisible(false);
        finishTesting_button.setDisable(true);
    }

    /**
     * Show witch answers user choose correct and witch are not
     */
    public void showCorrectAnswers() {
        isAnswersShown = true;
//        To avoid of showing previous page with not marked as right and wrong answers
        if (previousPageIndex == 0) {
            pagination.setCurrentPageIndex(1);
        }
        pagination.setCurrentPageIndex(0);
    }


}
