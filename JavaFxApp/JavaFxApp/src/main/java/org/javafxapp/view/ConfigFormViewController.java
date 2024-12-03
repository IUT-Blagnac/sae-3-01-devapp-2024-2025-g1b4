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
import org.javafxapp.controller.ConfigForm;

import java.util.ArrayList;
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

    public List<String> displayDialog() {

        this.appStage.showAndWait();

        return this.selectedData;
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

}
