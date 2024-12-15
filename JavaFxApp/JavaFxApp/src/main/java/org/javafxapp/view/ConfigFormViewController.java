package org.javafxapp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Validator;
import org.ini4j.Wini;
import org.javafxapp.controller.ConfigForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contrôleur de la vue pour le formulaire de configuration.
 */
public class ConfigFormViewController {

    // Le stage principal de l'application.
    private Stage appStage;
    // Le contrôleur pour le formulaire de configuration
    private ConfigForm conFormLoader;
    // Les données sélectionnées.
    private List<String> selectedData;


    /**
     * Initialise le contexte du contrôleur.
     * @param appStage le stage principal de l'application.
     * @param configForm le contrôleur du formulaire de configuration.
     */
    public void initContext(Stage appStage, ConfigForm configForm) {
        this.appStage = appStage;
        this.conFormLoader = configForm;
        this.appStage.setOnCloseRequest(this::closeWindow);
        this.selectedData = new ArrayList<>();


        // Création et configuration du validateur des données.
        Validator validator=new Validator();
        validator.createCheck().dependsOn("value",this.tps.textProperty()).withMethod( c -> {
            String max=c.get("value");
            if(!max.trim().matches("\\d+") || max.trim().isEmpty())
                c.error("Not a number");
        }).immediate().decorates(this.tps);
    }

    /**
     * Affiche le dialogue avec les données et le temps de seuil.
     * @param data Les données à afficher.
     * @param tps Le temps à utiliser sur le seuil.
     * @return Les données sélectionnées.
     */
    public List<String> displayDialog(List<String> data, String tps) {
        this.selectedData = data;

        this.tps.setText(tps);
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

    /**
     * Récupère toutes les checkbox de la sélection.
     * @return Une liste observable de tous les noeuds de type CheckBox.
     */
    private ObservableList<Node> getAllCheckBoxes() {
        ObservableList<Node> sons = this.selection.getChildren();
        ObservableList<Node> grandSons = FXCollections.observableList(new ArrayList<>());

        for (Node nd : sons)
            if (nd instanceof HBox)
                grandSons.addAll(((HBox) nd).getChildren());

        return grandSons;
    }

    /**
     * Gestion de l'événement de fermeture de la fenêtre par l'utilisateur.
     */
    private void closeWindow(WindowEvent e) {
        this.selectedData.clear();
        this.properClose();
        e.consume();
    }

    /**
     * Ferme correctement la fenêtre.
     */
    public void properClose() {
        this.appStage.close();
    }

    /**
     * Récupère la sélection de l'utilisateur.
     */
    private void getSelection() {
        ObservableList<Node> checkBoxes = this.getAllCheckBoxes();
        CheckBox checkBox;
        this.selectedData.clear();

        for (Node nd : checkBoxes) {
            checkBox = (CheckBox) nd;
            if (checkBox.isSelected())
                this.selectedData.add(checkBox.getId());
        }

        // Affiche un avertissement si aucune donnée n'est sélectionnée.
        if (!this.selectedData.isEmpty())
            return;

        Alert noDataSelected = new Alert(Alert.AlertType.WARNING, "Vous devez sélectioner des données!!");
        noDataSelected.show();
    }

    @FXML
    VBox selection;

    @FXML
    TextField tps;

    /**
     * Ouvre le sélecteur de salle.
     */
    @FXML
    public void doOpenRoom() {
        this.conFormLoader.openRoomPicker();
    }

    /**
     * Confirme la sélection et ferme le dialogue.
     */
    @FXML
    public void doConfirm() {
        this.getSelection();

        this.conFormLoader.getSeuilSelection(this.selectedData);

        this.properClose();
    }

    /**
     * Annule la sélection et demande une confirmation avant de fermer le dialogue.
     */
    @FXML
    public void doCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Les modifications seront abandonnées!! Êtes-vous certain de vouloir annuler?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            this.selectedData.clear();
            this.properClose();
        }
    }
}