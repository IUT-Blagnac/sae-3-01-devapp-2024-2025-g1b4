package org.javafxapp.view;

import org.javafxapp.controller.ChooseDataRoom;
import org.javafxapp.controller.DataRoomLeft;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ChooseDataRoomViewController {

    private ChooseDataRoom dialogController;

    private Stage appStage;

    public void initContext(Stage appStage, ChooseDataRoom stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {
        this.appStage.show();
    }
    

    @FXML
    public void boutonGauche() {
        this.dialogController.openDataRoomLeft();
    }

    @FXML
    public void boutonDroit() {
        this.dialogController.openSalleAvecDonnee();
    }

}
