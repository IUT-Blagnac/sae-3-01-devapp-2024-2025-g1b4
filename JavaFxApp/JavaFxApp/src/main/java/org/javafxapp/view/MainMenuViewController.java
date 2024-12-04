package org.javafxapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.javafxapp.controller.MainMenu;

public class MainMenuViewController {

    private MainMenu mainMenuDialogController;

    private Stage appStage;

    public void initContext(Stage appStage, MainMenu mainMenu) {
        this.mainMenuDialogController = mainMenu;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {
        this.appStage.show();
    }

    /*
     * Méthode de fermeture de la fenêtre par la croix.
     *
     * @param e Evénement associé (inutilisé pour le moment)
     *
     * @return null toujours (inutilisé)
     */
    private Object closeWindow(WindowEvent e) {
        this.doQuit();
        e.consume();
        return null;
    }

    @FXML
    public void doOpenConfig() {
        this.mainMenuDialogController.openConfig();
    }

    @FXML
    public void doOpenSolarInfos() {
        this.mainMenuDialogController.openPanneau();
    }

    @FXML
    public void doOpenRoomData() {
        this.mainMenuDialogController.openDataRoom();
    }

    @FXML
    public void doQuit(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Vous allez quitter l'application. Voulez vous vraiment quitter?");
        alert.showAndWait();
        if(alert.getResult()==ButtonType.OK)
            this.appStage.close();
    }
}
