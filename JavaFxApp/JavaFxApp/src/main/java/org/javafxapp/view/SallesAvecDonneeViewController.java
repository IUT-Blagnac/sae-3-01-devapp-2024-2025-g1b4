package org.javafxapp.view;

import org.javafxapp.controller.SallesAvecDonnee;

import javafx.stage.Stage;

public class SallesAvecDonneeViewController {
    private SallesAvecDonnee dialogController;

    private Stage appStage;

    public void initContext(Stage appStage, SallesAvecDonnee stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {
        this.appStage.show();
    }
    
}
