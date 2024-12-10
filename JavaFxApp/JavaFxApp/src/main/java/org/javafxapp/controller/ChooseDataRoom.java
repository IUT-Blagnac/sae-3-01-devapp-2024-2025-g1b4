package org.javafxapp.controller;

import org.javafxapp.view.ChooseDataRoomViewController;
import org.javafxapp.view.MainMenuViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Classe pour le choix de la salle de données.
 */
public class ChooseDataRoom {

    private Stage fenetreDialogue;

    /**
     * Constructeur pour choisir la salle de données.
     * @param fenetreP Le stage de l'application
     */
    public ChooseDataRoom(Stage fenetreP) {

        this.fenetreDialogue = fenetreP;

        try {
            // Charger la vue de visualisation FXML
            FXMLLoader cLoader = new FXMLLoader(MainMenuViewController.class.getResource("visualisationView.fxml"));

            BorderPane root = cLoader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            this.fenetreDialogue.setScene(scene);
            this.fenetreDialogue.setTitle("Menu Choix");
            ChooseDataRoomViewController cdrvController = cLoader.getController();
            cdrvController.initContext(this.fenetreDialogue, this);
            cdrvController.displayDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre les données de la salle avec les données.
     */
    public void openSalleAvecDonnee() {
        SallesAvecDonnee cSAD = new SallesAvecDonnee(this.fenetreDialogue);
    }

    /**
     * Ouvre les données de la salle laissée.
     */
    public void openDataRoomLeft(){
        DataRoomLeft cDRL = new DataRoomLeft(this.fenetreDialogue);
    }

    public void openMainMenu(){
        MainMenu cMM = new MainMenu();
        cMM.loadPage(this.fenetreDialogue,false);
    }

}