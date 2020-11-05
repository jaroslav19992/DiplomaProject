package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.GainPupilAccessPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.Classroom;
import TestMaker.MainProgramWindow.Panes.TestsPane.Pupil;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestMakerTest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class GainPupilAccessPaneController {

    private static final double TITLED_PANE_MAX_HEIGHT = 300;
    private static final String CLOSE_WINDOW_CONFIRMATION_CONTENT_TEXT = "Закрити поточне вікно?";
    private static final String CLOSE_WINDOW_CONFIRMATION_TITLE = "Підтвердіть дію";
    private static final String GAIN_ACCESS_CONFIRMATION_TITLE = "Підтвердіть дію";
    private static final String GAIN_ACCESS_CONFIRMATION_CONTENT_TEXT = "Надати вибраним користувачам доступ до тесту ";
    private static final String DUPLICATE_USER_ALERT_CONTEXT_TEXT = "Обрано користувача(ів) з наявним доступом до даного тесту.\n" +
            "Даний(ні) користувач(чі) не будуть враховані в запиті";

    @FXML
    private Button cancel_button;

    @FXML
    private Button addUser_button;

    @FXML
    private Button chooseAll_button;

    @FXML
    private VBox classes_vBox;

    @FXML
    private Button removeUser_button;

    @FXML
    private ListView<Pupil> accessedPupils_listView;

    @FXML
    private Button gainAccess_button;

    private TestMakerTest chosenTest;
    private ArrayList<ListView> listViews;

    @FXML
    void initialize() {
        accessedPupils_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            ArrayList<Classroom> classrooms = getClassrooms();
            createUsersList(classrooms);
            setButtonActions();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        Platform.runLater(() -> {
            cancel_button.getScene().getWindow().setOnCloseRequest(event -> {
                if (!closeWindowRequest()) {
                    event.consume();
                }
            });
        });
    }

    private void setButtonActions() {
        cancel_button.setOnAction(event -> {
            if (closeWindowRequest()) {
                cancel_button.getScene().getWindow().hide();
            }
        });
        gainAccess_button.setOnAction(event -> {
            if (!accessedPupils_listView.getItems().isEmpty()) {
                Alert gainAccessConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                gainAccessConfirmation.setHeaderText(null);
                gainAccessConfirmation.setTitle(GAIN_ACCESS_CONFIRMATION_TITLE);
                gainAccessConfirmation.setContentText(GAIN_ACCESS_CONFIRMATION_CONTENT_TEXT + chosenTest.getTestName() + "?");
                ButtonType gainAccess = new ButtonType("Продовжити");
                ButtonType cancel = new ButtonType("Скасувати");
                gainAccessConfirmation.getButtonTypes().clear();
                gainAccessConfirmation.getButtonTypes().addAll(cancel, gainAccess);
                Optional<ButtonType> selection = gainAccessConfirmation.showAndWait();
                if (selection.get() == gainAccess) {
                    gainTestAccess();
                } else {
                    event.consume();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText("Список користувачів порожній");
                alert.show();
            }
        });
        addUser_button.setOnAction(event -> {
            //get every listView with pupils whom we want to give access
            for (ListView<Pupil> listView : listViews) {
                //get every selected pupil
                for (Pupil pupil : listView.getSelectionModel().getSelectedItems()) {
                    //add pupil if another list view isn't contains such
                    if (!accessedPupils_listView.getItems().contains(pupil)) {
                        accessedPupils_listView.getItems().add(pupil);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Помилка");
                        alert.setHeaderText(null);
                        alert.setContentText("Користувач " + pupil + " уже присутній в списку");
                        alert.show();
                    }
                }
                listView.getSelectionModel().clearSelection();
            }
        });

        //Remove selected users from list
        removeUser_button.setOnAction(event -> {
            accessedPupils_listView.getItems().removeAll(accessedPupils_listView.getSelectionModel().getSelectedItems());
            accessedPupils_listView.requestFocus();
        });

        chooseAll_button.setOnAction(event -> {
            accessedPupils_listView.getSelectionModel().selectAll();
            accessedPupils_listView.requestFocus();
        });
    }

    /**
     * add record to the pupils tests table to gain access
     */
    private void gainTestAccess() {
        String SQLQuery;
        try {
            ObservableList<Pupil> list = chosenTest.getAccessedPupils();
        for (Pupil pupil : accessedPupils_listView.getItems()) {
            if (!list.contains(pupil)) {
                SQLQuery = "INSERT INTO " + DBConstants.DB_NAME + "." + DBConstants.PUPILS_TESTS_TABLE_NAME + " (`"
                        + DBConstants.USER_NAME_HASH + "`, `" + DBConstants.ID_TESTS_LIST + "`, `" + DBConstants.MARK
                        + "`, `" + DBConstants.USED_ATTEMPTS + "`) VALUES (?, ?, ?, ?);\n";
                PreparedStatement statement = DBHandler.getDbConnection().prepareStatement(SQLQuery);
                statement.setInt(1, pupil.getUsernameHash());
                statement.setInt(2, chosenTest.getIdInTestsList());
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.execute();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText(DUPLICATE_USER_ALERT_CONTEXT_TEXT);
                alert.showAndWait();
            }
        }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успіх операції");
            alert.setHeaderText(null);
            alert.setContentText("Доступ до тесту успішно надано. Оновіть сторінку тестів");
            alert.showAndWait();
            gainAccess_button.getScene().getWindow().hide();

        } catch (SQLException exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка з'єднання з сервером\n" + exception.getMessage());
            alert.showAndWait();
        }
    }

    private boolean closeWindowRequest() {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        closeConfirmation.setHeaderText(null);
        closeConfirmation.setTitle(CLOSE_WINDOW_CONFIRMATION_TITLE);
        closeConfirmation.setContentText(CLOSE_WINDOW_CONFIRMATION_CONTENT_TEXT);
        ButtonType close = new ButtonType("Закрити");
        ButtonType cancel = new ButtonType("Скасувати");
        closeConfirmation.getButtonTypes().clear();
        closeConfirmation.getButtonTypes().addAll(cancel, close);
        Optional<ButtonType> selection = closeConfirmation.showAndWait();
        return selection.get() == close;
    }

    /**
     * Create accordion children (Vbox, listview, selectAll button), compose them and sel list views elements
     *
     * @param classrooms list of classrooms
     */
    private void createUsersList(ArrayList<Classroom> classrooms) {
        //init listView array
        listViews = new ArrayList<>();
        //Fit classes vBox to parent
        AnchorPane.setRightAnchor(classes_vBox, 0.0);
        AnchorPane.setLeftAnchor(classes_vBox, 0.0);
        AnchorPane.setTopAnchor(classes_vBox, 0.0);
        AnchorPane.setBottomAnchor(classes_vBox, 0.0);
        //Titled panes list
        ObservableList<TitledPane> classesPanes = FXCollections.observableArrayList();
        //For each classroom
        for (Classroom classroom : classrooms) {
            //create and fit to parent vBox
            VBox vBox = new VBox();
            AnchorPane.setRightAnchor(vBox, 0.0);
            AnchorPane.setLeftAnchor(vBox, 0.0);
            AnchorPane.setTopAnchor(vBox, 0.0);
            AnchorPane.setBottomAnchor(vBox, 0.0);
            vBox.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(vBox, Priority.ALWAYS);
            vBox.setSpacing(10.0);

            //create list view and add pupils
            ListView<Pupil> pupilsListView = new ListView<>();
            pupilsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            pupilsListView.getItems().addAll(classroom.getClassPupils());
            listViews.add(pupilsListView);

            //TODO: Try to create this
            //clear selection when empty line clicked
//            pupilsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pupil>() {
//                @Override
//                public void changed(ObservableValue<? extends Pupil> observable, Pupil oldValue, Pupil newValue) {
//                    System.out.println(newValue);
//                    if (Objects.equals(newValue, null)) {
//                        pupilsListView.getSelectionModel().clearSelection();
//                    }
//                }
//            });

            //create selectAll button
            Button button = new Button("Вибрати всіх");
            button.setOnAction(event -> {
                pupilsListView.getSelectionModel().selectAll();
                pupilsListView.requestFocus();
            });
            vBox.getChildren().addAll(pupilsListView, button);

            //created titled pane for all this
            TitledPane classPane = new TitledPane(classroom.getClassroomName(), vBox);
            //set unselect all in list view when pane is expanding
            classPane.expandedProperty().addListener((observable, oldValue, newValue)
                    -> pupilsListView.getSelectionModel().clearSelection());
            //set pane max height
            classPane.setMaxHeight(TITLED_PANE_MAX_HEIGHT);
            classPane.setExpanded(false);
            classesPanes.add(classPane);
        }
        classes_vBox.getChildren().addAll(classesPanes);
    }

    /**
     * Creating arraylist of classrooms with all pupils witch are belong to those classrooms inside
     *
     * @return arraylist of classrooms
     * @throws SQLException if unable to connect to DB
     */
    private ArrayList<Classroom> getClassrooms() throws SQLException {
        HashSet<String> classroomsNames = new HashSet<>();
        ArrayList<Pupil> pupils = new ArrayList<>();
        ArrayList<Classroom> classrooms = new ArrayList<>();

        //Get all pupils info
        String pupilsSQLQuery = "SELECT " + DBConstants.USER_NAME_HASH + ", " + DBConstants.FIRST_NAME + ", "
                + DBConstants.LAST_NAME + ", " + DBConstants.CLASS_ROOM + " FROM " + DBConstants.DB_NAME + "."
                + DBConstants.USERS_INFO_TABLE_NAME + " WHERE " + DBConstants.ACCESS_TOKEN + " = '"
                + DBConstants.PUPIL_ACCESS_TOKEN + "';";
        ResultSet pupilsRS = DBHandler.getDataFromDB(pupilsSQLQuery);

        //get all pupils to array list and classrooms to hashset
        while (pupilsRS.next()) {
            pupils.add(new Pupil(pupilsRS.getInt(DBConstants.USER_NAME_HASH), pupilsRS.getString(DBConstants.FIRST_NAME),
                    pupilsRS.getString(DBConstants.LAST_NAME), pupilsRS.getString(DBConstants.CLASS_ROOM)));
            classroomsNames.add(pupilsRS.getString(DBConstants.CLASS_ROOM));
        }

        //Create classrooms
        for (String classroom : classroomsNames) {
            classrooms.add(new Classroom(classroom));
        }
        //Foreach pupil check his/her classroom and add to needed classroom
        for (Pupil pupil : pupils) {
            for (Classroom classroom : classrooms) {
                if (classroom.getClassroomName().equals(pupil.getClassroom())) {
                    classroom.addPupil(pupil);
                }
            }
        }
        return classrooms;
    }

    public void setChosenTest(TestMakerTest test) {
        this.chosenTest = test;
    }
}

