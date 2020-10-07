package TestMaker;

import java.io.File;

public class TestMakerTestFile {
    private int idInTestsList;
    private String testName;
    private File testFile;

    public TestMakerTestFile(int idInTestsList, String testName) {
        this.testName = testName;
        this.idInTestsList = idInTestsList;
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
