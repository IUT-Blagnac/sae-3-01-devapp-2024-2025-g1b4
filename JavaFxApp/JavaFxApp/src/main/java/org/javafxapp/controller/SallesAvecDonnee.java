package org.javafxapp.controller;

import org.javafxapp.view.MainMenuViewController;
import org.javafxapp.view.SallesAvecDonneeViewController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Classe pour la gestion de l'affichage des salles avec des données.
 */
public class SallesAvecDonnee {

    // Le stage pour l'affichage des salles avec des données
    private Stage fenetreDialogue;

    /**
     * Constructeur pour la gestion de l'affichage des salles avec des données.
     *
     * @param fenetreP Le stage de l'application principal.
     */
    public SallesAvecDonnee(Stage fenetreP) {
        this.fenetreDialogue = fenetreP;

        try {
            // Charger la vue des salles avec des données FXML
            FXMLLoader cLoader = new FXMLLoader(MainMenuViewController.class.getResource("sallesAvecDonnee.fxml"));
            BorderPane root = cLoader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            // Configurer le stage pour l'affichage
            this.fenetreDialogue.setScene(scene);
            this.fenetreDialogue.setTitle("Salles avec données");
            SallesAvecDonneeViewController sadController = cLoader.getController();
            sadController.initContext(this.fenetreDialogue, this);
            sadController.displayDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre les données de la salle.
     */
    public void openDataRoom() {
        ChooseDataRoom cDataRoom = new ChooseDataRoom(this.fenetreDialogue);
    }
}