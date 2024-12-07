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

public class RoomPickerViewController {
    private Stage appStage;

    private JSONArray allRoomList;
    private JsonInteract jsInt;

    private ObservableList<String> olRoomList;
    private RoomPicker roomPicker;


    private AutoCompletionBinding bindingTextField;

    public void initContext(Stage appStage, RoomPicker roomPicker) {
        this.appStage=appStage;
        this.roomPicker=roomPicker;
        this.appStage.setOnCloseRequest(e -> this.closeWindow(e));
        this.configure();
    }

    private void configure() {
        this.olRoomList=this.roomList.getItems();
        this.jsInt=new JsonInteract();
        this.allRoomList=(JSONArray) jsInt.get("roomNames");
        this.bindingTextField=TextFields.bindAutoCompletion(this.roomName, this.allRoomList.toList());


        this.olRoomList.addAll(this.getPrevConfig());
    }

    private Collection<String> getPrevConfig() {
        return new ArrayList<>();
    }


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

    @FXML
    public void doSupprimerSalle(){
        if(this.roomList.getSelectionModel().getSelectedIndex()>=0) {
            this.olRoomList.remove(this.roomList.getSelectionModel().getSelectedItem());
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING, "Vous devez sélectionner une salle à supprimer de la liste");
            alert.show();
        }
    }

    @FXML
    public void doSupprimerToutesSalles(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Vous allez supprimer toutes les salles de la liste!");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK)
            this.olRoomList.clear();
    }

    @FXML
    public void doConfirm(){
        this.properClose();
    }

    @FXML
    public void doCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Les modifications seront abandonnées!! Êtes-vous certain de vouloir annuler?");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK) {
            this.olRoomList.clear();
            this.properClose();
        }
    }

    public void properClose(){
        this.appStage.close();
    }
}
