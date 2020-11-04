package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane;

import java.io.File;

public class CurrentTest {
    private final String testName;
    private final int evSystem;
    private final int amountOfQuestions;
    private final int timeLimit;
    private final int isRetestingAllowed;
    private File currentTestFile;
    private double currentUserMark;

    public CurrentTest(String testName, int evSystem, int amountOfQuestions, int timeLimit, int isRetstingAllowed) {
        this.testName = testName;
        this.evSystem = evSystem;
        this.amountOfQuestions = amountOfQuestions;
        this.timeLimit = timeLimit;
        this.isRetestingAllowed = isRetstingAllowed;
    }

    @Override
    public String toString() {
        return testName;
    }

    public String getTestName() {
        return testName;
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

    public int getIsRetestingAllowed() {
        return isRetestingAllowed;
    }

    public File getCurrentTestFile() {
        return currentTestFile;
    }

    public void setCurrentTestFile(File currentTestFile) {
        this.currentTestFile = currentTestFile;
    }

    public double getCurrentUserMark() {
        return currentUserMark;
    }

    public void setCurrentUserMark(double currentUserMark) {
        this.currentUserMark = currentUserMark;
    }
}
