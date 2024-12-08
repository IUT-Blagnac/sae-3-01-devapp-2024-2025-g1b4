package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;
import org.javafxapp.view.SeuilSeterViewController;

/**
 * Classe pour gérer les paramètres de seuillage.
 */
public class SeuilSeter {
    // Le stage pour la mise en évidence du seuil
    private Stage seuilStage;
    // Le contrôleur pour la vue de définition du seuil
    private SeuilSeterViewController sSVC;

    /**
     * Créer une nouvelle instance de la mise en évidence du seuil.
     * @param appStage Le stage d'application principal.
     * @param dataType Le type de données à seuiller.
     */
    public SeuilSeter(Stage appStage,String dataType) {
        try {
            // Charger la vue de définition du seuil.
            FXMLLoader loader = new FXMLLoader(ConfigFormViewController.class.getResource("seuilSeterView.fxml"));
            BorderPane root = loader.load();

            // Créer une nouvelle scène pour la mise en évidence du seuil.
            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            // Configurer le stage pour la mise en évidence du seuil.
            this.seuilStage = new Stage();
            this.seuilStage.initModality(Modality.WINDOW_MODAL);
            this.seuilStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.seuilStage);
            this.seuilStage.setScene(scene);
            this.seuilStage.setTitle("Réglage des seuils");
            this.seuilStage.setResizable(false);

            // Obtenir le contrôleur de la vue de définition du seuil.
            this.sSVC = loader.getController();
            this.sSVC.initContext(this.seuilStage,this,dataType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Affiche le dialog de définition du seuil et retourne l'intervalle de seuil défini.
     * @param data Données pour la définition du seuil.
     * @return L'intervalle de seuil défini.
     */
    public String displayDialog(String[] data){
        return this.sSVC.displayDialog(data);
    }
}