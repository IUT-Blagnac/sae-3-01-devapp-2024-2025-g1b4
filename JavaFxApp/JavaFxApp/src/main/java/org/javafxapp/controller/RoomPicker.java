package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ini4j.Wini;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;
import org.javafxapp.view.RoomPickerViewController;

import java.util.List;

/**
 * Classe pour le sélecteur de salle.
 */
public class RoomPicker {
    // Le stage pour le sélecteur de salle.
    private Stage roomPickerStage;
    // Le contrôleur pour la vue du sélecteur de salle.
    private RoomPickerViewController rPVM;

    /**
     * Créer une nouvelle instance du sélecteur de salle.
     * @param appStage Le stage principal de l'application.
     */
    public RoomPicker(Stage appStage) {
        try {
            // Charger la vue du sélecteur de salle
            FXMLLoader loader = new FXMLLoader(ConfigFormViewController.class.getResource("roomPickerView.fxml"));
            BorderPane root = loader.load();

            // Créer une nouvelle scène pour le sélecteur de salle
            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            // Configurer le stage pour le sélecteur de salle
            this.roomPickerStage = new Stage();
            this.roomPickerStage.initModality(Modality.WINDOW_MODAL);
            this.roomPickerStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.roomPickerStage);
            this.roomPickerStage.setScene(scene);
            this.roomPickerStage.setTitle("Selection des salles");
            this.roomPickerStage.setResizable(false);

            // Obtenir le contrôleur pour la vue du sélecteur de salle
            this.rPVM = loader.getController();
            this.rPVM.initContext(this.roomPickerStage,this);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Affiche le dialogue du sélecteur de salle et retourne la liste des salles sélectionnées.
     * @param rooms La liste des salles à afficher dans le sélecteur.
     * @return La liste des salles sélectionnées.
     */
    public List<String> doRoomPickerDialog(List<String> rooms) {
        return this.rPVM.displayDialog(rooms);
    }
}