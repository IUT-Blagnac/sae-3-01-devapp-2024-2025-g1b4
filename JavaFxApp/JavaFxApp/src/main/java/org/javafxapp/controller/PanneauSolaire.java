package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PanneauSolaire {

    private Stage fenetreDialogue; // Fenêtre principale pour afficher le graphique

    public PanneauSolaire(Stage fenetrePrincipale) {
        try {
            // Charger le fichier FXML
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/org/javafxapp/view/PanneauSolaireView.fxml"));
            VBox racine = chargeur.load();

            // Configurer la fenêtre
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

    public void afficherFenetre() {
        this.fenetreDialogue.showAndWait();
    }
}
