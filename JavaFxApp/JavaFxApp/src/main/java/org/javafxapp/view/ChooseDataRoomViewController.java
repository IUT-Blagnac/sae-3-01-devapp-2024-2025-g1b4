package org.javafxapp.view;

import org.javafxapp.controller.ChooseDataRoom;
import org.javafxapp.controller.DataRoomLeft;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Classe contrôleur pour la vue qui permet à l'utilisateur de choisir une salle de données.
 */
public class ChooseDataRoomViewController {

    // Le contrôleur pour le dialog de choix de la salle de données
    private ChooseDataRoom dialogController;

    // Le stage principal de l'application
    private Stage appStage;

    /**
     * Initialise le contrôleur avec le contexte nécessaire.
     * @param appStage La scène principale de l'application.
     * @param stageControl Le contrôleur pour le dialog de choix de la salle de données.
     */
    public void initContext(Stage appStage, ChooseDataRoom stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    /**
     * Affiche le dialog de choix de salle de données.
     */
    public void displayDialog() {
        this.appStage.show();
    }


    /**
     * Action lors du clic sur le bouton gauche.
     * Ouvre la vue avec les données de la salle de gauche.
     */
    @FXML
    public void boutonGauche() {
        this.dialogController.openDataRoomLeft();
    }

    /**
     * Action lors du clic sur le bouton droit.
     * Ouvre la vue avec les données de la salle ayant des données.
     */
    @FXML
    public void boutonDroit() {
        this.dialogController.openSalleAvecDonnee();
    }

    /**
     * Action lors du clic sur le bouton retour.
     * Ouvre le menu principal.
     */
    @FXML
    public void boutonRetour() {
        this.dialogController.openMainMenu();
    }
}