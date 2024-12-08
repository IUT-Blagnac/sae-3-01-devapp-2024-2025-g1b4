package org.javafxapp.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;
import org.javafxapp.controller.DataRoomLeft;
import org.javafxapp.tools.JsonInteract;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataRoomLeftViewController {

    private DataRoomLeft dialogController;

    private Stage appStage;

    private String valueSelect;

    private LineChart<Number,Number> lineChart;

    @FXML
    private ComboBox<String> dataSelectionComboBox;

    @FXML
    private VBox vBox;

    @FXML
    private VBox vBox2;

    @FXML
    private Button appliqueButton;


    /**
     * Initialise le contexte actuel avec un contrôle de scène et une scène d'application.
     *
     * @param appStage la scène d'application principale
     * @param stageControl le contrôle de la scène
     */
    public void initContext(Stage appStage, DataRoomLeft stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());

        JsonInteract jsInt = new JsonInteract();
        JSONArray chosenData = (JSONArray) jsInt.get("communes.chosenData");
        JSONObject traduction = (JSONObject) jsInt.get("traduction");
        JSONArray salles = (JSONArray) jsInt.get("communes.chosenRooms");

        // Créer une liste pour les éléments traduits
        ObservableList<String> translatedData = FXCollections.observableArrayList();

        // Ajouter les traductions à la liste
        for (Object item : chosenData) {
        String key = item.toString();
        String translatedValue = traduction.get(key).toString();
        translatedData.add(translatedValue); // Ajouter la traduction à la liste
        }

        ObservableList<String> sallesList = FXCollections.observableArrayList(
        salles.toList().stream()
            .map(Object::toString) // Convertir chaque élément en String
            .collect(Collectors.toList()) // Collecter dans une liste
        );

        // Ajouter les éléments à la ComboBox
        dataSelectionComboBox.setItems(sallesList);

        // Action à effectuer lors de la sélection d'un élément
        dataSelectionComboBox.setOnAction(event -> {
        String selectedSalle = dataSelectionComboBox.getSelectionModel().getSelectedItem();
        System.out.println("Salle sélectionnée : " + selectedSalle);
        });

        CheckComboBox<String> checkComboBox = new CheckComboBox<>();
        for (Object item : translatedData) {
        checkComboBox.getItems().add(item.toString());
        }

        vBox.getChildren().add(checkComboBox);
    }

    /**
     * Le gestionnaire d'action du bouton de retour.
     */
    @FXML
    private void retourButton() {
        this.dialogController.openDataRoom();
    }

    /**
     * Le gestionnaire d'action du bouton d'application.
     */
    @FXML
    private void onAppliqueButtonClick() {
        // Chercher un CheckComboBox parmi les enfants de vBox
        CheckComboBox<String> checkComboBox = null;
        for (javafx.scene.Node child : vBox.getChildren()) {
            if (child instanceof CheckComboBox) {
                checkComboBox = (CheckComboBox<String>) child;
                break;
            }
        }

        if (checkComboBox != null) {
            ObservableList<String> selectedData = checkComboBox.getCheckModel().getCheckedItems();
            System.out.println("Données sélectionnées : " + selectedData);

            // Vérifier que des données sont sélectionnées
            if (selectedData.isEmpty()) {
                System.out.println("Aucune donnée sélectionnée.");
                return;
            }

            // Définir valueSelect avec la première donnée sélectionnée (ou toute autre logique)

            // Vider vBox2 avant d'ajouter les nouveaux graphiques
            vBox2.getChildren().clear();

            // Récupérer la salle sélectionnée
            String selectedSalle = dataSelectionComboBox.getSelectionModel().getSelectedItem();
            System.out.println("Salle sélectionnée : " + selectedSalle);

            if (selectedSalle == null || selectedData.isEmpty()) {
                System.out.println("Aucune salle ou donnée sélectionnée.");
                return;
            }

            int cpt=0;
            // Créer un graphique pour chaque donnée sélectionnée
            for (String data : selectedData) {
                this.valueSelect = selectedData.get(cpt);  // Assigner la première donnée sélectionnée à valueSelect
                cpt++;
                ArrayList<Integer> dataValues = dataBySalle(selectedSalle);
                System.out.println("Données pour la salle " + selectedSalle + " : " + dataValues);

                if (dataValues == null || dataValues.isEmpty()) {
                    System.out.println("Pas de données pour la salle : " + selectedSalle);
                    continue;
                }

                // Créer le graphique pour la donnée
                createGraphForData(data, dataValues);
            }
        } else {
            System.out.println("Aucun CheckComboBox trouvé dans vBox.");
        }
    }

    /**
     * Crée un graphique pour un ensemble de données.
     *
     * @param data le nom des données
     * @param dataValues les valeurs des données
     */
    private void createGraphForData(String data, ArrayList<Integer> dataValues) {
        // Créer les axes pour le graphique
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Temps (Index)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valeur de " + data); // Titre dynamique en fonction de la donnée

        // Créer le LineChart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Graphique de " + data);

        // Préparer les données pour le graphique (série de données)
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(data);

        // Ajouter les valeurs à la série (index, valeur)
        for (int i = 0; i < dataValues.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, dataValues.get(i)));
        }

        // Ajouter la série au graphique
        lineChart.getData().add(series);

        // Ajouter le graphique à vBox2
        vBox2.getChildren().add(lineChart);
    }

    /**
     * Affiche la boîte de dialogue.
     */
    public void displayDialog() {
        this.appStage.show();
    }

    /**
     * Cartographie une sélection à une clé JSON correspondante.
     *
     * @param selection la sélection à cartographier
     * @return la clé JSON correspondante
     */
    private String mapSelectionToJsonKey(String selection) {
        JsonInteract jsInt=new JsonInteract();
        JSONArray chosenData = (JSONArray) jsInt.get("communes.chosenData");
        JSONObject traduction = (JSONObject) jsInt.get("traduction");
        
        for (Object item : chosenData) {
            String key = item.toString();
            String translatedValue = traduction.get(key).toString().trim();
            if (translatedValue.equals(selection)) {
                return key;  // Retourner la clé réelle
            }
        }
        return null; 
    }

    /**
     * Récupère les données par salle spécifique.
     *
     * @param nomSalle le nom de la salle
     * @return la liste des valeurs de données pour cette salle
     */
    private ArrayList<Integer> dataBySalle(String nomSalle) {
        try {
            // Assurer que valueSelect est bien définie et correspond à la clé dans le JSON
            if (this.valueSelect == null || this.valueSelect.isEmpty()) {
                System.out.println("Erreur : valueSelect n'est pas défini.");
                return new ArrayList<>();
            }
    
            // Mapper la sélection de l'utilisateur à la clé JSON correspondante
            String key = mapSelectionToJsonKey(this.valueSelect.trim());
    
            // Lire le fichier JSON
            JsonInteract jsInt = new JsonInteract();
            String cheminRelatif = (String) jsInt.get("communes.pathResultJson");
            String content = Files.readString(Paths.get(cheminRelatif));
    
            // Récupérer les données de la salle spécifiée
            JSONObject dataParSalle = new JSONObject(content).getJSONObject(nomSalle);
            ArrayList<Integer> donnePreciseParSalle = new ArrayList<>();
    
            // Afficher les données de la salle pour le debugging
            System.out.println("Données pour la salle " + nomSalle + " : " + dataParSalle.toString(2));
    
            // Parcourir les données de la salle
            for (String keyIndex : dataParSalle.keySet()) {
                JSONObject roomData = dataParSalle.getJSONObject(keyIndex);
    
                // Vérifier si la clé (humidity, température, co2) est présente
                if (roomData.has(key)) {
                    double value = roomData.getDouble(key);  // Utiliser la clé correcte
                    donnePreciseParSalle.add((int) value);  // Convertir en entier si nécessaire
                } else {
                    System.out.println("Clé introuvable : " + key);
                }
            }
    
            return donnePreciseParSalle;
    
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de lire le fichier result.json.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'extraction des données JSON : " + e.getMessage());
            e.printStackTrace();
        }
    
        return new ArrayList<>();  // Retourner une liste vide en cas d'erreur
    }
    
    
    
}
