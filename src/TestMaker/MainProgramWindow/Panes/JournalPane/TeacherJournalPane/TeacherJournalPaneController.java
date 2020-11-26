package TestMaker.MainProgramWindow.Panes.JournalPane.TeacherJournalPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.Pupil;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TeacherJournalPaneController {
    private static final String EMPTY_TESTS_TABLE_VIEW_MESSAGE = "Список тестів порожній";
    private static final String EMPTY_MARKS_TABLE_VIEW_MESSAGE = "Відсутні учні з доступом до даного тесту";

    @FXML
    private ChoiceBox<TestMakerTest> tests_choiceBox;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private ScrollPane table_scrollPane;

    @FXML
    private TableView<TestMakerTest> tests_tableView;

    @FXML
    private TableView<PupilsMarks> marks_tableView;

    @FXML
    private ChoiceBox<String> classrooms_choiceBox;

    @FXML
    private HBox tables_hBox;


    private ArrayList<TestMakerTest> testsList;
    private static final boolean IS_COLUMNS_RESIZABLE = true;
    private static final String ALL_DATA = "Всі";
    private static final String TABLE_COLUMN_NAME = "Тест";
    private static final int TABLE_COLUMN_PREF_WIDTH = 60;
    private static final int TABLE_COLUMN_MIN_WIDTH = 40;
    private static final int TABLE_COLUMN_MAX_WIDTH = 120;

    private static final double VBOX_PADDING = 5;
    private static final double ROTATE_LEFT = -90;

    @FXML
    void initialize() {
        setFillSpace(main_pane);
        setFillSpace(marks_tableView);
        setFillSpace(table_scrollPane);
        setFillSpace(tables_hBox);
        setFillSpace(tests_tableView);
        try {
            getTestsList(); //NOW WE HAVE ALL TESTS with all accessed pupils, each pupil has his own mark
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        setChoiceBoxesValues();
        setChoiceBoxesActions();
        fillTables(tests_choiceBox.getValue(), classrooms_choiceBox.getValue());
        Platform.runLater(() -> {
            ScrollBar TestsVerticalBar = (ScrollBar) tests_tableView.lookup(".scroll-bar:vertical");
            ScrollBar MarksVerticalBar = (ScrollBar) marks_tableView.lookup(".scroll-bar:vertical");
            MarksVerticalBar.valueProperty().addListener((obs, oldValue, newValue) ->
                    TestsVerticalBar.setValue(newValue.doubleValue()));
            TestsVerticalBar.valueProperty().addListener((obs, oldValue, newValue) ->
                    MarksVerticalBar.setValue(newValue.doubleValue()));

        });

        tests_tableView.setPlaceholder(new Label(EMPTY_TESTS_TABLE_VIEW_MESSAGE));
        marks_tableView.setPlaceholder(new Label(EMPTY_MARKS_TABLE_VIEW_MESSAGE));
    }

    private void setChoiceBoxesActions() {
        classrooms_choiceBox.setOnAction(event -> {
            tests_tableView.getColumns().clear();
            tests_tableView.refresh();
            marks_tableView.getColumns().clear();
            marks_tableView.refresh();
            fillTables(tests_choiceBox.getValue(), classrooms_choiceBox.getValue());
        });
        tests_choiceBox.setOnAction(event -> {
            tests_tableView.getColumns().clear();
            tests_tableView.refresh();
            marks_tableView.getColumns().clear();
            marks_tableView.refresh();
            fillTables(tests_choiceBox.getValue(), classrooms_choiceBox.getValue());
        });
    }


    private void fillTables(TestMakerTest test, String classroom) {
        double allColumnsWidth = 0;
        double prefTableLabelHeight = 0;
        //Get all tests pupils, avoid pupil duplicate usage
        ArrayList<Pupil> tablePupils = getPupils(test);

        //Get pupils witch classroom equals to <classroom>
        ArrayList<Pupil> classroomTablePupils = getClassroomPupils(tablePupils, classroom);

        //Create as many exemplars of pupilsMarks as many tests are chosen
        ObservableList<PupilsMarks> pupilsMarks = FXCollections.observableArrayList();
        if (test.getTestName().equals(ALL_DATA)) {
            for (int i = 0; i < testsList.size(); i++) {
                pupilsMarks.add(new PupilsMarks(classroomTablePupils, test));
            }
        } else {
            pupilsMarks.add(new PupilsMarks(classroomTablePupils, test));
        }
        for (Pupil pupil : classroomTablePupils) {
            //All classrooms selected add all pupils, is single classroom selected don't create columns with another classroom
            TableColumn<PupilsMarks, String> pupilColumn = new TableColumn<>("");
            //Pupil marks column text properties
            Label nameLabel = new Label(pupil.getFirstName() + "\n" + pupil.getLastName());
            nameLabel.setAlignment(Pos.CENTER);

            //Used to get label width before it will be shown on scene
            Text theText = new Text(nameLabel.getText());
            theText.setFont(nameLabel.getFont());
            double labelTextWidth = (int) theText.getBoundsInLocal().getWidth();

            VBox vbox = new VBox(nameLabel);
            vbox.setAlignment(Pos.CENTER);
            //Set vbox width by summing label width, vBox horizontal padding
            vbox.setPrefWidth((labelTextWidth * 1.2) + 2 * VBOX_PADDING);
            vbox.setRotate(ROTATE_LEFT);
            vbox.setPadding(new Insets(VBOX_PADDING, VBOX_PADDING, VBOX_PADDING, VBOX_PADDING));
            Group g = new Group(vbox);

            pupilColumn.setGraphic(g);
            pupilColumn.setPrefWidth(TABLE_COLUMN_PREF_WIDTH);
            pupilColumn.setMinWidth(TABLE_COLUMN_MIN_WIDTH);
            pupilColumn.setMaxWidth(TABLE_COLUMN_MAX_WIDTH);
            pupilColumn.setSortable(false);
            pupilColumn.setResizable(IS_COLUMNS_RESIZABLE);
            marks_tableView.edit(0, pupilColumn);

            //Seriously now we have array of hash maps <Pupil, arrayOfMarksForAllTestes>
            //For first row we get pupil and try to get first marks array element
            //For second row we get pupil and try to get second marks array element
            pupilColumn.setCellValueFactory(data -> {
                int pupilIndex = pupilsMarks.indexOf(data.getValue());
                Double doubleData = data.getValue().getPupilsMarks().get(pupil)[pupilIndex];
                SimpleStringProperty property = new SimpleStringProperty(String.valueOf(doubleData));
                if (property.getValue().equals("null") ) {
                    property.setValue("✖");
                }
                    return  property;
            });

            marks_tableView.getColumns().add(pupilColumn);

            allColumnsWidth += pupilColumn.getWidth();

            double currentColumnHeight = vbox.getPrefWidth();
            prefTableLabelHeight = (Math.max((currentColumnHeight), (prefTableLabelHeight)));
        }

        //Test column text properties
        TableColumn<TestMakerTest, String> testsColumn = new TableColumn<>("");
        Label testColumnLabel = new Label(TABLE_COLUMN_NAME);
        testsColumn.setGraphic(testColumnLabel);
        testsColumn.setSortable(false);
        tests_tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tests_tableView.getColumns().add(testsColumn);
        ObservableList<TestMakerTest> observableTestsList = getSelectedTests(test);
        tests_tableView.setItems(observableTestsList);
        testColumnLabel.setPrefHeight(prefTableLabelHeight);
        testsColumn.setCellValueFactory(new PropertyValueFactory<>("testName"));

        marks_tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        marks_tableView.setItems(pupilsMarks);
        allColumnsWidth += testsColumn.getWidth();

        //Set fit size table to scrollPane or not
        double finalAllColumnsWidth = allColumnsWidth;
        Platform.runLater(() -> {
            System.out.println(main_pane.getParent());
            table_scrollPane.setFitToWidth(finalAllColumnsWidth < main_pane.getWidth());
        });
    }

    /**
     * Get list of tests according to chosen test
     *
     * @param test chosen test
     * @return observable list of tests
     */
    private ObservableList<TestMakerTest> getSelectedTests(TestMakerTest test) {
        ObservableList<TestMakerTest> list = FXCollections.observableArrayList();
        if (test.getTestName().equals(ALL_DATA)) {
            list.addAll(testsList);
            return list;
        }
        list.add(test);
        return list;
    }

    /**
     * Get list of pupils according to classroom
     * @param tablePupils all available pupils
     * @param classroom   chosen classroom
     * @return observable list with pupils
     */
    private ArrayList<Pupil> getClassroomPupils(ArrayList<Pupil> tablePupils, String classroom) {
        ArrayList<Pupil> pupils = new ArrayList<>();
        if (classroom.equals(ALL_DATA)) {
            pupils.addAll(tablePupils);
        } else {
            for (Pupil pupil : tablePupils) {
                if (pupil.getClassroom().equals(classroom)) {
                    pupils.add(pupil);
                }
            }
        }
        return pupils;
    }

    /**
     * Get all pupils of all tests (without duplicates) if all testes selected, or single test pupils if single test chosen
     *
     * @param test chosen test
     * @return hashset with pupils
     */
    private ArrayList<Pupil> getPupils(TestMakerTest test) {
        ArrayList<Pupil> pupilsList = new ArrayList<>();
        if (test.getTestName().equals(ALL_DATA)) {
            for (TestMakerTest currentTest : testsList) {
                for (Pupil pupil : currentTest.getAccessedPupils())
                    if (!pupilsList.contains(pupil)) {
                        pupilsList.add(pupil);
                    }
            }
        } else {
            pupilsList.addAll(test.getAccessedPupils());
        }
        return pupilsList;
    }

    /**
     * Set values to tests and classrooms choice boxes
     */
    private void setChoiceBoxesValues() {
        TestMakerTest plugForAllTestes = new TestMakerTest(0, ALL_DATA, 0,
                0, 0, 0);
        ObservableList<TestMakerTest> testsObservableList = FXCollections.observableArrayList();
        testsObservableList.add(plugForAllTestes);
        testsObservableList.addAll(testsList);
        tests_choiceBox.setItems(testsObservableList);
        tests_choiceBox.setValue(plugForAllTestes);

        HashSet<String> classroomsHashSet = new HashSet<>();
        ObservableList<String> classrooms = FXCollections.observableArrayList();
        classrooms.add(ALL_DATA);
        for (TestMakerTest test : testsList) {
            for (Pupil pupil : test.getAccessedPupils()) {
                classroomsHashSet.add(pupil.getClassroom());
            }
        }
        classrooms.addAll(classroomsHashSet);
        classrooms_choiceBox.setItems(classrooms);
        classrooms_choiceBox.setValue(ALL_DATA);
    }

    /**
     * Allow pane fill parent space
     *
     * @param pane child node
     */
    private void setFillSpace(Node pane) {
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);

        VBox.setVgrow(pane, Priority.ALWAYS);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }

    /**
     * Get tests info and accessed pupils for all tests from DB and add it to tests array
     */
    private void getTestsList() throws SQLException {
        String SQLQuery = "SELECT " + DBConstants.ID_TESTS_LIST + ", " + DBConstants.TEST_NAME + ", " + DBConstants.EV_SYSTEM + ", "
                + DBConstants.AMOUNT_OF_QUESTIONS + ", " + DBConstants.TIME_LIMIT + ", " + DBConstants.NUMBER_OF_ATTEMPTS + ", "
                + DBConstants.USER_NAME_HASH + ", " + DBConstants.FIRST_NAME + ", " + DBConstants.LAST_NAME + ", "
                + DBConstants.CLASS_ROOM + ", " + DBConstants.MARK + "  FROM " + DBConstants.DB_NAME + "." + DBConstants.USERS_INFO_TABLE_NAME
                + " inner join " + DBConstants.PUPILS_TESTS_TABLE_NAME + " using (" + DBConstants.USER_NAME_HASH + ") inner join "
                + DBConstants.TESTS_LIST_TABLE_NAME + " using (" + DBConstants.ID_TESTS_LIST + ") order by "
                +DBConstants.ID_TESTS_LIST + ";";
        ResultSet resultSet = DBHandler.getDataFromDB(SQLQuery);

        testsList = new ArrayList<>();
        int previousTestId = -1;
        int currentTestId;
        ObservableList<Pupil> currentTestAccessedPupils = FXCollections.observableArrayList();
        TestMakerTest test = null;

        while (resultSet.next()) {
            //If this is the first record add pupil to pupils array
            if (resultSet.isFirst()) {
                test = getTestFromResultSet(resultSet);
                currentTestAccessedPupils.add(getPupilFromResultSet(resultSet));
                currentTestId = resultSet.getInt(DBConstants.ID_TESTS_LIST);
                previousTestId = currentTestId;
                continue;
            }
            currentTestId = resultSet.getInt(DBConstants.ID_TESTS_LIST);

            if (currentTestId != previousTestId) {
                //Crete new list to avoid of clearing list witch is already inside current test
                ObservableList<Pupil> list = FXCollections.observableArrayList();
                list.addAll(currentTestAccessedPupils);
                if (test != null) {
                    test.setAccessedPupils(list);
                }
                testsList.add(test);
                test = getTestFromResultSet(resultSet);
                //clear pupils list
                currentTestAccessedPupils.clear();
            }
            currentTestAccessedPupils.add(getPupilFromResultSet(resultSet));
            previousTestId = currentTestId;
            if (resultSet.isLast()) {
                if (test != null) {
                    test.setAccessedPupils(currentTestAccessedPupils);
                }
                testsList.add(test);
            }
        }

    }

    private TestMakerTest getTestFromResultSet(ResultSet resultSet) throws SQLException {
        return new TestMakerTest(resultSet.getInt(DBConstants.ID_TESTS_LIST),
                resultSet.getString(DBConstants.TEST_NAME), resultSet.getInt(DBConstants.EV_SYSTEM),
                resultSet.getInt(DBConstants.AMOUNT_OF_QUESTIONS), resultSet.getInt(DBConstants.TIME_LIMIT),
                resultSet.getInt(DBConstants.NUMBER_OF_ATTEMPTS));
    }

    /**
     * Create pupil exemplar from result set record
     *
     * @param resultSet result set with pupil indo
     * @return pupil
     */
    private Pupil getPupilFromResultSet(ResultSet resultSet) {
        Pupil pupil = null;
        try {
            pupil = new Pupil(resultSet.getInt(DBConstants.USER_NAME_HASH), resultSet.getString(DBConstants.FIRST_NAME),
                    resultSet.getString(DBConstants.LAST_NAME), resultSet.getString(DBConstants.CLASS_ROOM),
                    resultSet.getDouble(DBConstants.MARK));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return pupil;
    }

    /**
     * Used to create rows tor marks table
     */
    private class PupilsMarks {
        HashMap<Pupil, Double[]> pupilsMarks;

        //Creating array list of hash maps <Pupil, tests marks[]>
        public PupilsMarks(ArrayList<Pupil> pupils, TestMakerTest currentTest) {
            ArrayList<TestMakerTest> currentTests = new ArrayList<>();
            if (currentTest.getTestName().equals(ALL_DATA)) {
                currentTests.addAll(testsList);
            } else {
                currentTests.add(currentTest);
            }

            pupilsMarks = new HashMap<>();
            for (int i = 0; i < currentTests.size(); i++) {
                TestMakerTest test = currentTests.get(i);
                for (Pupil pupil : pupils) {
                    int currentPupilIndex = test.getAccessedPupils().indexOf(pupil);
                    if (i == 0) {
                        Double[] marksArray = new Double[currentTests.size()];
                        if (currentPupilIndex != -1) {
                            marksArray[i] = test.getAccessedPupils().get(currentPupilIndex).getCurrentTestMark();
                            pupilsMarks.put(pupil, marksArray);
                        }
                    } else {
                        if (currentPupilIndex != -1) {
                            //If currentPupilIndex == -1 that means that current pupil have no access to current test
                            pupilsMarks.get(pupil)[i] = test.getAccessedPupils().get(currentPupilIndex).getCurrentTestMark();
                        }
                    }
                }
            }
        }

        public HashMap<Pupil, Double[]> getPupilsMarks() {
            return pupilsMarks;
        }
    }
}
