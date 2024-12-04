package org.javafxapp.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ini4j.Wini;
import org.javafxapp.controller.ConfigForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigFormViewController {

    private Stage appStage;
    private ConfigForm conFormLoader;
    private List<String> selectedData;


    public void initContext(Stage appStage, ConfigForm configForm) {
        this.appStage=appStage;
        this.conFormLoader=configForm;
        this.appStage.setOnCloseRequest(this::closeWindow);
        this.selectedData=new ArrayList<>();
    }
    public List<String> displayDialog(List<String> data) {
        this.selectedData=data;
        if (!this.selectedData.isEmpty()) {

            ObservableList<Node> checkBoxes = this.getAllCheckBoxes();
            CheckBox checkBox;

            for (Node nd : checkBoxes) {
                checkBox = (CheckBox) nd;
                checkBox.setSelected(this.selectedData.contains(checkBox.getId()));
            }
        }

        this.appStage.showAndWait();

        return this.selectedData;
    }

    private ObservableList<Node> getAllCheckBoxes() {
        ObservableList<Node> sons=this.selection.getChildren();
        ObservableList<Node> grandSons=FXCollections.observableList(new ArrayList<>());

        for(Node nd : sons)
            if(nd instanceof HBox)
                grandSons.addAll(((HBox)nd).getChildren());

        return grandSons;
    }

    /**
     * Gestion de la fermeture de la fenêtre par l'utilisateur.
     *
     * @param e Evénement associé à la fermeture de la fenêtre
     * @return null toujours (inutilisé)
     *
     */
    private Object closeWindow(WindowEvent e) {
        this.selectedData.clear();
        this.properClose();
        e.consume();
        return null;
    }

    public void properClose(){
        this.appStage.close();
    }

    private void getSelection() {
        ObservableList<Node> checkBoxes = this.getAllCheckBoxes();
        CheckBox checkBox;
        this.selectedData.clear();

        for (Node nd : checkBoxes) {
            checkBox=(CheckBox)nd;
            if (checkBox.isSelected())
                this.selectedData.add(checkBox.getId());
        }

        if(!this.selectedData.isEmpty())
            return;

        Alert noDataSelected=new Alert(Alert.AlertType.WARNING,"Vous devez sélectioner des données!!");
        noDataSelected.show();
    }

    @FXML
    VBox selection;

    @FXML
    public void doOpenRoom(){
        this.conFormLoader.openRoomPicker();
    }

    @FXML
    public void doConfirm(){
        this.getSelection();

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Souhaitez-vous régler tous les seuils, ou seulement ceux qui n'ont pas encore de valeurs?",ButtonType.YES, ButtonType.NO);
        alert.showAndWait();


        this.conFormLoader.getSeuilSelection(this.selectedData);

        this.properClose();
    }

    @FXML
    public void doCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Les modifications seront abandonnées!! Êtes-vous certain de vouloir annuler?");
        alert.showAndWait();
        if(alert.getResult()== ButtonType.OK) {
            this.selectedData.clear();
            this.properClose();
        }
    }
}
