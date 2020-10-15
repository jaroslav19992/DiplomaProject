package TestMaker.MainProgramWindow.Panes.TestsPane;

import java.io.File;

public class TestMakerTestFile {
    private Integer idInTestsList = null;
    private String testName = null;
    private File testFile = null;

    public TestMakerTestFile(int idInTestsList, String testName) {
        this.testName = testName;
        this.idInTestsList = idInTestsList;
        this.testFile = null;
    }

    public TestMakerTestFile(File testFile, String testName) {
        this.testName = testName;
        this.idInTestsList = null;
        this.testFile = testFile;
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
