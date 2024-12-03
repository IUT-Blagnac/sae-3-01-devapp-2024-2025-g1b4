package org.javafxapp.controller;

import org.javafxapp.view.ChooseDataRoomViewController;
import org.javafxapp.view.MainMenuViewController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChooseDataRoom {
    
    private Stage fenetreDialogue;

    public ChooseDataRoom(Stage fenetreP) {

        this.fenetreDialogue = fenetreP;

        try {
            FXMLLoader cLoader = new FXMLLoader(MainMenuViewController.class.getResource("visualisationView.fxml"));

            BorderPane root = cLoader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            this.fenetreDialogue.setScene(scene);
            this.fenetreDialogue.setTitle("Menu");

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
