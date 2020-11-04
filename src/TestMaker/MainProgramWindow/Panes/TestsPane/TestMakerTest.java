package TestMaker.MainProgramWindow.Panes.TestsPane;

import javafx.collections.ObservableList;

import java.io.File;

public class TestMakerTest {
    private final Integer idInTestsList;
    private final String testName;
    private File testFile;
    private int evSystem;
    private int amountOfQuestions;
    private int timeLimit;
    private int numberOfAttempts;
    private double currentUserMark;
    private int currentUserUsedAttempts;
    private ObservableList<Pupil> accessedPupils;

    //usually for pupil
    public TestMakerTest(int idInTestsList, String testName, int evSystem,
                         int amountOfQuestions, int timeLimit, int numberOfAttempts, double currentUserMark) {
        this.idInTestsList = idInTestsList;
        this.testName = testName;
        this.evSystem = evSystem;
        this.amountOfQuestions = amountOfQuestions;
        this.timeLimit = timeLimit;
        this.numberOfAttempts = numberOfAttempts;
        testFile = null;
        this.currentUserMark = currentUserMark;
    }

    //usually for teacher
    public TestMakerTest(int idInTestsList, String testName) {
        this.testName = testName;
        this.idInTestsList = idInTestsList;
        this.testFile = null;
        currentUserMark = 0;
    }

    public ObservableList<Pupil> getAccessedPupils() {
        return accessedPupils;
    }

    public void setAccessedPupils(ObservableList<Pupil> accessedPupils) {
        this.accessedPupils = accessedPupils;
    }

    public int getCurrentUserUsedAttempts() {
        return currentUserUsedAttempts;
    }

    public void setCurrentUserUsedAttempts(int currentUserUsedAttempts) {
        this.currentUserUsedAttempts = currentUserUsedAttempts;
    }

    public int getEvSystem() {
        return evSystem;
    }

    public int getAmountOfQuestions() {
        return amountOfQuestions;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public double getCurrentUserMark() {
        return currentUserMark;
    }

    public void setCurrentUserMark(double currentUserMark) {
        this.currentUserMark = currentUserMark;
    }

    public void setTestFile(File testFile) {
        this.testFile = testFile;
    }

    public File getTestFile() {
        return testFile;
    }

    public int getIdInTestsList() {
        return idInTestsList;
    }

    public String getTestName() {
        return testName;
    }

    @Override
    public String toString() {
        return testName;
    }
}
