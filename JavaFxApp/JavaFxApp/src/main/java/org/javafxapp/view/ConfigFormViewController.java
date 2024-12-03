package org.javafxapp.view;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ini4j.Wini;
import org.javafxapp.controller.ConfigForm;

import java.io.File;
import java.io.IOException;
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

    public List<String> displayDialog(Wini wini) {
        loadData:{
            String data=wini.get("donnees","donnees");
            this.selectedData.addAll(Arrays.asList(data.split(",")));


            if (this.selectedData.isEmpty())
                break loadData;

            ObservableList<Node> checkBoxes=this.getSelectChildren();
            CheckBox checkBox;

            for (Node nd : checkBoxes) {
                checkBox = (CheckBox) nd;
                checkBox.setSelected(this.selectedData.contains(checkBox.getId()));
            }
        }

        this.appStage.showAndWait();

        return this.selectedData;
    }

    private ObservableList<Node> getSelectChildren() {
        ObservableList<Node> hBoxes= this.selection.getChildren();
        ObservableList<Node> allChildren;
        for(Node nd: hBoxes)
            ;

        return null;

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
        ObservableList<Node> checkBoxes = selection.getChildren();
        CheckBox checkBox;

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
    HBox selection;

    @FXML
    public void doOpenRoom(){
        this.conFormLoader.openRoomPicker();
    }

    @FXML
    public void doConfirm(){
        this.getSelection();
        this.properClose();
    }

    @FXML
    public void doCancel(){
        this.selectedData.clear();
        this.properClose();
    }

    public static void main(String[] args) {
        Wini wini= null;
        try {
            wini = new Wini(new File("C:\\Users\\Dell\\Documents\\Fichiers Persos\\Studies\\BUT Info\\S3\\SAÉS\\Temp\\sae-3-01-devapp-2024-2025-g1b4\\IOT\\config.ini"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(wini.get("donnees","donnees"));

    }
}
