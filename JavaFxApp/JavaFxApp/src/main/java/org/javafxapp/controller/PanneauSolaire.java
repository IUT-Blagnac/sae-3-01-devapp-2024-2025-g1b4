package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.PanneauSolaireViewController;

public class PanneauSolaire {
    private Stage panneauStage;
    private PanneauSolaireViewController PSVC;
    public PanneauSolaire(Stage appStage) {

        try {
            FXMLLoader loader = new FXMLLoader(PanneauSolaireViewController.class.getResource("panneau.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            this.panneauStage = new Stage();
            this.panneauStage.initModality(Modality.WINDOW_MODAL);
            this.panneauStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.panneauStage);
            this.panneauStage.setScene(scene);
            this.panneauStage.setTitle("Panneaux solaires");
            this.panneauStage.setResizable(false);

            this.PSVC = loader.getController();
            this.PSVC.initContext(this.panneauStage,this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doPanneauDialog() {
        PSVC.displayDialog();
    }
}

