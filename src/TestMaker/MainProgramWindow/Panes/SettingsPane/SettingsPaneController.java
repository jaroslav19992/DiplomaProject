package TestMaker.MainProgramWindow.Panes.SettingsPane;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SettingsPaneController {
    @FXML
    private AnchorPane main_pane;



    @FXML
    void initialize() {
        System.out.println("get Parent:"+main_pane.getParent());

//        main_anchorPane
    }

}

