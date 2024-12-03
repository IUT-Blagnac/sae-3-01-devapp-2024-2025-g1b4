package org.javafxapp.view;

import org.javafxapp.controller.MainMenu;

import javafx.stage.Stage;

public class ChooseDataRoomViewController {

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
    
}
