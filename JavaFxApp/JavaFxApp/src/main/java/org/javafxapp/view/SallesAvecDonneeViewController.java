package org.javafxapp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;
import org.javafxapp.controller.SallesAvecDonnee;
import org.javafxapp.tools.JsonInteract;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SallesAvecDonneeViewController {

    private SallesAvecDonnee dialogController;

    private Stage appStage;

    @FXML
    private ComboBox<String> dataSelectionComboBox;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private VBox vBox;



    public void initContext(Stage appStage, SallesAvecDonnee stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());

        JsonInteract jsInt = new JsonInteract();
        JSONArray chosenData = (JSONArray) jsInt.get("communes.chosenData");
        JSONObject traduction = (JSONObject) jsInt.get("traduction");

        // Créer une liste pour les éléments traduits
        ObservableList<String> translatedData = FXCollections.observableArrayList();

        // Ajouter les traductions à la liste
        for (Object item : chosenData) {
            String key = item.toString();
            String translatedValue = traduction.get(key).toString();
            translatedData.add(translatedValue); // Ajouter la traduction à la liste
        }

        // Ajouter la liste à la ComboBox
        dataSelectionComboBox.setItems(translatedData);

        // Action à effectuer lors de la sélection d'un élément
        dataSelectionComboBox.setOnAction(event -> {
            String selectedTranslated = dataSelectionComboBox.getSelectionModel().getSelectedItem();
            String realName = getRealNameFromTranslated(selectedTranslated, chosenData, traduction);
            System.out.println("Nom réel sélectionné : " + realName);
        });


        CheckComboBox<String> checkComboBox = new CheckComboBox<>();
        checkComboBox.getItems().addAll("Option 1", "Option 2", "Option 3", "Option 4");


        Label checkComboLabel = new Label("Liste des salles");
        vBox.getChildren().add(checkComboLabel);
        vBox.getChildren().add(checkComboBox);

    }

    // Méthode pour retrouver le nom réel en fonction de la traduction sélectionnée
    private String getRealNameFromTranslated(String translated, JSONArray chosenData, JSONObject traduction) {
        for (Object item : chosenData) {
            String key = item.toString();
            String translatedValue = traduction.get(key).toString();
            if (translatedValue.equals(translated)) {
                return key;  // Retourner la clé réelle
            }
        }
        return null;  // Si la traduction n'est pas trouvée
    }

    public void displayDialog() {
        this.appStage.show();
    }
}
