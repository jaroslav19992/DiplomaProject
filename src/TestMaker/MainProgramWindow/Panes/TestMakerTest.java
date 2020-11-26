package TestMaker.MainProgramWindow.Panes;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.DOM.DOMxmlReader;
import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import javafx.collections.ObservableList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public TestMakerTest(int idInTestsList, String testName, int evSystem,
                         int amountOfQuestions, int timeLimit, int numberOfAttempts) {
        this.idInTestsList = idInTestsList;
        this.testName = testName;
        this.evSystem = evSystem;
        this.amountOfQuestions = amountOfQuestions;
        this.timeLimit = timeLimit;
        this.numberOfAttempts = numberOfAttempts;
        testFile = null;
        this.currentUserMark = 0.0;
    }

    public TestMakerTest(int idInTestsList, String testName, int evSystem, int currentUserUsedAttempts, double currentUserMark) {
        this.idInTestsList = idInTestsList;
        this.testName = testName;
        this.evSystem = evSystem;
        this.amountOfQuestions = 0;
        this.timeLimit = 0;
        this.numberOfAttempts = 0;
        this.currentUserUsedAttempts = currentUserUsedAttempts;
        testFile = null;
        this.currentUserMark = currentUserMark;
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

    public File getTestFileFromBD() throws SQLException {
        if (testFile == null) {
            ResultSet resultSet = DBHandler.getDataFromDB("SELECT " + DBConstants.TEST_FILE + " FROM " + DBConstants.DB_NAME + "."
                    + DBConstants.TESTS_LIST_TABLE_NAME + " WHERE " + DBConstants.ID_TESTS_LIST + " = '" + idInTestsList + "';");
            resultSet.next();
            Blob testFileInBlob = resultSet.getBlob(DBConstants.TEST_FILE);
            File tempFile = null;
            try {
                tempFile = File.createTempFile("tempFile", "xml");
                FileOutputStream out = new FileOutputStream(tempFile);
                out.write(resultSet.getBlob(DBConstants.TEST_FILE).getBytes(1,
                        (int) resultSet.getBlob(DBConstants.TEST_FILE).length()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            testFile = tempFile;
            tempFile.delete();
        }
        return testFile;
    }

    public ArrayList<Question> getTestQuestions() {
        try {
            DOMxmlReader reader = new DOMxmlReader(getTestFileFromBD());
            return reader.getQuestionsList();
        } catch (ParserConfigurationException | IOException | SAXException | SQLException e) {
            e.printStackTrace();
        }
        return null;
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
