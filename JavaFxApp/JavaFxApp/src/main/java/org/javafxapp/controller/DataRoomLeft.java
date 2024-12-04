package org.javafxapp.controller;

import org.javafxapp.tools.JsonInteract;
import org.javafxapp.view.DataRoomLeftViewController;
import org.javafxapp.view.MainMenuViewController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DataRoomLeft {
    
    private Stage fenetreDialogue;

    public DataRoomLeft(Stage fenetreP) {
        this.fenetreDialogue = fenetreP;

        

        try {
            FXMLLoader cLoader = new FXMLLoader(MainMenuViewController.class.getResource("dataRoomLeftView.fxml"));
            BorderPane root = cLoader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            this.fenetreDialogue.setScene(scene);
            this.fenetreDialogue.setTitle("Choix de la salle et des Données");
            DataRoomLeftViewController drlController = cLoader.getController();
            drlController.initContext(this.fenetreDialogue, this);
            drlController.displayDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}