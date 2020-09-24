package TestMaker.MainProgramWindow.Panes.SettingsPane;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;

public class SettingsPaneController {
    @FXML
    private AnchorPane main_anchorPane;

    @FXML
    private Pagination paggination;

    @FXML
    void initialize() {
        System.out.println("get Parent:"+main_anchorPane.getParent());

//        main_anchorPane
    }

}

