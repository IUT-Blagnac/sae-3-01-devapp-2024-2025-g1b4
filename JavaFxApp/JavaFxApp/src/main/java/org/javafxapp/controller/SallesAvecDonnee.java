package org.javafxapp.controller;

import org.javafxapp.view.MainMenuViewController;
import org.javafxapp.view.SallesAvecDonneeViewController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SallesAvecDonnee {
    
    private Stage fenetreDialogue;


    public SallesAvecDonnee(Stage fenetreP) {
        this.fenetreDialogue = fenetreP;

        try {
            FXMLLoader cLoader = new FXMLLoader(MainMenuViewController.class.getResource("sallesAvecDonnee.fxml"));
            BorderPane root = cLoader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            this.fenetreDialogue.setScene(scene);
            this.fenetreDialogue.setTitle("Salles avec données");
            SallesAvecDonneeViewController sadController = cLoader.getController();
            sadController.initContext(this.fenetreDialogue, this);
            sadController.displayDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
