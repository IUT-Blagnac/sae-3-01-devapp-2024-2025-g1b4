package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;
import org.javafxapp.view.SeuilSeterViewController;

import java.util.ArrayList;

public class SeuilSeter {
    private Stage seuilStage;
    private SeuilSeterViewController sSVC;
    public SeuilSeter(Stage appStage,String dataType) {


        try {
            FXMLLoader loader = new FXMLLoader(ConfigFormViewController.class.getResource("seuilSeterView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            this.seuilStage = new Stage();
            this.seuilStage.initModality(Modality.WINDOW_MODAL);
            this.seuilStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.seuilStage);
            this.seuilStage.setScene(scene);
            this.seuilStage.setTitle("Réglage des seuils");
            this.seuilStage.setResizable(false);

            this.sSVC = loader.getController();
            this.sSVC.initContext(this.seuilStage,this,dataType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String displayDialog(String[] data){
        return this.sSVC.displayDialog(data);
    }
}
