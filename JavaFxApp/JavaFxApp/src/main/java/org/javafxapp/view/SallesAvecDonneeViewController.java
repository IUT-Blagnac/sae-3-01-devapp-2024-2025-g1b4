package org.javafxapp.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;
import org.javafxapp.controller.SallesAvecDonnee;
import org.javafxapp.tools.JsonInteract;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SallesAvecDonneeViewController {

    private SallesAvecDonnee dialogController;

    private Stage appStage;

    private String valueSelect;

    private Boolean firstClick = true;

    @FXML
    private ComboBox<String> dataSelectionComboBox;

    @FXML
    private CheckComboBox<String> checkComboBox;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private VBox vBox;

    @FXML
    private Button appliqueButton;



    public void initContext(Stage appStage, SallesAvecDonnee stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());

        appliqueButton.setDisable(true);

        JsonInteract jsInt = new JsonInteract();
        JSONArray chosenData = (JSONArray) jsInt.get("communes.chosenData");
        JSONObject traduction = (JSONObject) jsInt.get("traduction");
        JSONArray salles = (JSONArray) jsInt.get("communes.chosenRooms");


        lineChart.setTitle("Graphique des Salles");
        lineChart.getXAxis().setLabel("Temps");
        lineChart.getYAxis().setLabel("Valeurs");

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
            this.valueSelect = realName;
            appliqueButton.setDisable(false);
        });
        
        // Créer le CheckComboBox
        checkComboBox = new CheckComboBox<>();
        for (Object item : salles) {
            checkComboBox.getItems().add(item.toString());
        }
        
        // Ajouter le CheckComboBox à l'interface
        Label checkComboLabel = new Label("Liste des salles");

        vBox.getChildren().add(checkComboLabel);
        vBox.getChildren().add(checkComboBox);

    }

    public void displayDialog() {
        this.appStage.show();
    }
    
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

    @FXML
    private void retourButton() {
        this.dialogController.openDataRoom();
    }

    @FXML
    private void appliqueButton() {
        ObservableList<String> selectedItems = checkComboBox.getCheckModel().getCheckedItems();

        if (!this.firstClick) {
            lineChart.getData().clear();
        } else {
            this.firstClick = false;
        }

        // Afficher les éléments sélectionnés dans la console (ou les utiliser comme bon vous semble)
        for (String item : selectedItems) {
            ArrayList data = dataBySalle(item);
            if (data != null && !data.isEmpty()) {
                addDataToChart(item, data); // Ajouter les données de la salle au graphique
            }
        }
    }

    private ArrayList dataBySalle(String nomSalle) {
        try {
            // Lire le fichier JSON
            JsonInteract jsInt = new JsonInteract();
            String cheminRelatif = (String)jsInt.get("communes.pathResultJson");
            String content = Files.readString(Paths.get(cheminRelatif));

            JSONObject dataParSalee = new JSONObject(content).getJSONObject(nomSalle);

            ArrayList donnePreciseParSalle = new ArrayList<>();

            for (String key : dataParSalee.keySet()) {

                JSONObject roomData = dataParSalee.getJSONObject(key);
                
                int value = roomData.getInt(this.valueSelect);
                donnePreciseParSalle.add(value);
            }

            return donnePreciseParSalle;

        } catch (IOException e) {
            System.out.println("Erreur : Impossible de lire le fichier result.json.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'extraction des données JSON : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void addDataToChart(String salleName, ArrayList<Integer> data) {
        // Créer une nouvelle série de données pour cette salle
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(salleName);

        // Ajouter les données à la série
        for (int i = 0; i < data.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), data.get(i)));
        }

        // Ajouter la série au LineChart
        lineChart.getData().add(series);
    }

}
