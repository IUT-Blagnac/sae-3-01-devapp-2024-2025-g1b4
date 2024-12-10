package org.javafxapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.javafxapp.controller.MainMenu;

/**
 * Classe de contrôleur pour le menu principal de l'application.
 */
public class MainMenuViewController {

    private MainMenu mainMenuDialogController;

    private Stage appStage;

    /**
     * Initialise le contexte pour cette classe avec le stage de l'application et l'instance du contrôleur de menu principal.
     *
     * @param appStage La stage de l'application
     * @param mainMenu L'instance du contrôleur de menu principal
     */
    public void initContext(Stage appStage, MainMenu mainMenu) {
        this.mainMenuDialogController = mainMenu;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }


    /**
     * Affiche une boîte de dialogue qui demande à l'utilisateur s'il souhaite accéder au panneau de configuration ou lancer l'application avec la configuration actuelle.
     */
    public void displayDialog(boolean firstLaunch) {
        ButtonType lancer = new ButtonType("Lancer"),
                config = new ButtonType("Configurer");

        this.appStage.show();

        if (firstLaunch) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Voulez-vous accéder au panel de configuration ou lancer l'application avec la configuration actuelle?", lancer, config);
            alert.showAndWait();

            if (alert.getResult() == config)
                this.doOpenConfig();
            else
                this.mainMenuDialogController.launchPython();
        }

    }

    /**
     * Méthode pour fermer la fenêtre à l'aide de la croix.
     *
     * @param e événement associé (actuellement non utilisé)
     * @return null (actuellement non utilisé)
     */
    private Object closeWindow(WindowEvent e) {
        this.doQuit();
        e.consume();
        return null;
    }


    /**
     * Méthode FXML pour tester la connexion.
     */
    @FXML
    public void doTesterConnex() {
        this.mainMenuDialogController.testConnexion();
    }

    /**
     * Méthode FXML pour ouvrir le formulaire de configuration.
     */
    @FXML
    public void doOpenConfig() {
        this.mainMenuDialogController.openConfig();
    }

    /**
     * Méthode FXML pour ouvrir les informations sur le panneau solaire.
     */
    @FXML
    public void doOpenSolarInfos() {
        this.mainMenuDialogController.openPanneau();
    }

    /**
     * Méthode FXML pour ouvrir les informations de la salle de données.
     */
    @FXML
    public void doOpenRoomData() {
        this.mainMenuDialogController.openDataRoom();
    }

    /**
     * Méthode FXML pour quitter l'application.
     */
    @FXML
    public void doQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vous allez quitter l'application. Voulez vous vraiment quitter?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            this.appStage.close();
    }
}