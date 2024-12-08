package org.javafxapp.view;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javafx.stage.WindowEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import org.controlsfx.control.textfield.TextFields;
import org.ini4j.Wini;
import org.javafxapp.controller.RoomPicker;
import org.javafxapp.tools.JsonInteract;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Contrôleur de la vue pour le sélecteur de salle.
 */
public class RoomPickerViewController {

    // La scène d'application principale.
    private Stage appStage;

    // La liste de toutes les salles disponibles.
    private JSONArray allRoomList;

    // L'utilitaire pour interagir avec des données JSON.
    private JsonInteract jsInt;

    // La liste observable des salles pour la vue.
    private ObservableList<String> olRoomList;

    // Le sélecteur de salle pour le dialogue.
    private RoomPicker roomPicker;

    // L'outil de liaison pour la complétion automatique pour le nom de la salle.
    private AutoCompletionBinding bindingTextField;

    /**
     * Initialise le contexte actuel avec une scène d'application et un sélecteur de salle.
     *
     * @param appStage la scène d'application principale
     * @param roomPicker le sélecteur de salle
     */
    public void initContext(Stage appStage, RoomPicker roomPicker) {
        this.appStage=appStage;
        this.roomPicker=roomPicker;
        this.appStage.setOnCloseRequest(e -> this.closeWindow(e));
        this.configure();
    }

    /**
     * Configure les éléments nécessaires pour le dialogue.
     */
    private void configure() {
        this.olRoomList=this.roomList.getItems();
        this.jsInt=new JsonInteract();
        this.allRoomList=(JSONArray) jsInt.get("roomNames");
        this.bindingTextField=TextFields.bindAutoCompletion(this.roomName, this.allRoomList.toList());


        this.olRoomList.addAll(this.getPrevConfig());
    }

    /**
     * Récupère la configuration précédente.
     *
     * @return une collection contenant la configuration précédente
     */
    private Collection<String> getPrevConfig() {
        return new ArrayList<>();
    }


    /**
     * Affiche la boîte de dialogue pour la sélection de salles.
     *
     * @param rooms la liste des salles à pré-remplir dans la boîte de dialogue
     * @return la liste des salles sélectionnées par l'utilisateur
     */
    public ObservableList<String> displayDialog(List<String> rooms) {

        this.olRoomList.addAll(rooms);

        this.appStage.showAndWait();

        this.jsInt.properClose();
        return this.olRoomList;
    }

    /**
     * Gestion de la fermeture de la fenêtre par l'utilisateur.
     *
     * @param e Evénement associé à la fermeture de la fenêtre
     * @return null toujours (inutilisé)
     *
     * @see #properClose()
     */
    private Object closeWindow(WindowEvent e) {
        this.properClose();
        e.consume();
        return null;
    }

    @FXML
    ListView<String> roomList;

    @FXML
    TextField roomName;

    /**
     * Ajoute une salle à la liste des salles si elle n'est pas déjà présente.
     */
    @FXML
    public void doAjouterSalle(){
        if(!this.olRoomList.contains(roomName.getText())){
            if(!this.allRoomList.toList().contains(roomName.getText())){
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"La Salle est inconnue voulez-vous vraiment l'ajouter?");
                alert.showAndWait();

                if(alert.getResult()!=ButtonType.OK)
                    return;

                this.allRoomList.put(roomName.getText());
                this.bindingTextField.dispose();
                this.bindingTextField=TextFields.bindAutoCompletion(this.roomName, this.allRoomList.toList());


            }

            this.olRoomList.add(roomName.getText());
            roomName.clear();

        }
    }

    /**
     * Supprime une salle de la liste des salles.
     */
    @FXML
    public void doSupprimerSalle(){
        if(this.roomList.getSelectionModel().getSelectedIndex()>=0) {
            this.olRoomList.remove(this.roomList.getSelectionModel().getSelectedItem());
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING, "Vous devez sélectionner une salle à supprimer de la liste");
            alert.show();
        }
    }

    /**
     * Supprime toutes les salles de la liste des salles.
     */
    @FXML
    public void doSupprimerToutesSalles(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Vous allez supprimer toutes les salles de la liste!");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK)
            this.olRoomList.clear();
    }

    /**
     * Ferme la boîte de dialogue et confirme les modifications réalisées.
     */
    @FXML
    public void doConfirm(){
        this.properClose();
    }

    /**
     * Annule les modifications réalisées et ferme la boîte de dialogue.
     */
    @FXML
    public void doCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Les modifications seront abandonnées!! Êtes-vous certain de vouloir annuler?");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK) {
            this.olRoomList.clear();
            this.properClose();
        }
    }

    /**
     * Ferme correctement la boîte de dialogue.
     */
    public void properClose(){
        this.appStage.close();
    }
}
