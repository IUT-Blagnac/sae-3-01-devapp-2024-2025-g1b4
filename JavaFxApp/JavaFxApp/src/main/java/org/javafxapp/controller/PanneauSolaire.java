package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe pour la gestion de l'affichage des données des panneaux solaires.
 */
public class PanneauSolaire {

    // Stage pour l'affichage de l'interface graphique relative au panneaux solaire
    private Stage fenetreDialogue;

    /**
     * Constructeur pour la gestion de l'affichage des données du panneau solaire.
     * @param fenetrePrincipale Le stage principal de l'application.
     */
    public PanneauSolaire(Stage fenetrePrincipale) {
        try {
            // Charge le fichier FXML pour l'interface graphique du panneau solaire.
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/org/javafxapp/view/PanneauSolaireView.fxml"));
            VBox racine = chargeur.load();

            // Configure le stage pour l'affichage
            this.fenetreDialogue = new Stage();
            this.fenetreDialogue.initModality(Modality.WINDOW_MODAL);
            this.fenetreDialogue.initOwner(fenetrePrincipale);
            this.fenetreDialogue.setTitle("Données des Panneaux Solaires");

            Scene scene = new Scene(racine);
            this.fenetreDialogue.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la fenêtre avec l'interface graphique pour le panneau solaire.
     */
    public void afficherFenetre() {
        this.fenetreDialogue.showAndWait();
    }
}