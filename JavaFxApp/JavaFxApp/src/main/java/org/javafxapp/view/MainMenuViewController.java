package org.javafxapp.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.javafxapp.controller.MainMenu;

public class MainMenuViewController {

    private MainMenu mainMenuDialogController;

    private Stage appStage;
    public void initContext(Stage appStage, MainMenu mainMenu) {
        this.mainMenuDialogController = mainMenu;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {
        this.appStage.show();
    }

    @FXML
    public void doOpenConfig(){
        this.mainMenuDialogController.openConfig();
    }
}
