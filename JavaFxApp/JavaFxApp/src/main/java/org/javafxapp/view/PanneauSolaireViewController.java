package org.javafxapp.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;

/**
 * Contrôleur de la vue pour le panneau solaire.
 */
public class PanneauSolaireViewController {

    // Le graphique à barres à afficher dans la vue.
    @FXML
    private LineChart<String, Number> graphiqueBarres;

    // Initialisation du contrôleur
    @FXML
    public void initialize() {
        System.out.println("Initialisation du contrôleur PanneauSolaireViewController...");

        // Charger les données initiales et mettre à jour le graphique
        mettreAJourGraphique();

        // Mettre à jour périodiquement le graphique toutes les 5 secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(365), event -> {
            mettreAJourGraphique();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Répéter indéfiniment
        timeline.play(); // Lancer le rafraîchissement automatique
    }

    /**
     * Charge les données du panneau solaire depuis le fichier result.json.
     *
     * @return un dico des données chargées, triées par heure
     */    private Map<Integer, Double> chargerDonnees() {
        Map<Integer, Double> donneesTriees = new TreeMap<>();

        try {
            // Lire le fichier JSON
            String cheminRelatif = "./JavaFxApp/JavaFxApp/result.json";
            System.out.println("Chemin courant (pour result.json) : " + Paths.get(".").toAbsolutePath());
            String content = Files.readString(Paths.get(cheminRelatif));
            JSONObject solarData = new JSONObject(content).getJSONObject("solarpanel");

            // Parcourir les données JSON et les ajouter à la map triée
            for (String key : solarData.keySet()) {
                int heure = Integer.parseInt(key);
                double puissance = solarData.getDouble(key);
                donneesTriees.put(heure, puissance);
            }

        } catch (IOException e) {
            System.out.println("Erreur : Impossible de lire le fichier result.json.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'extraction des données JSON : " + e.getMessage());
            e.printStackTrace();
        }

        return donneesTriees;
    }

    /**
     * Met à jour le graphique avec les données les plus récentes.
     */
    private void mettreAJourGraphique() {
        System.out.println("Mise à jour du graphique avec de nouvelles données...");

        // Charger les données
        Map<Integer, Double> donnees = chargerDonnees();

        // Effacer les anciennes données du graphique
        graphiqueBarres.getData().clear();

        // Ajouter les nouvelles données au graphique
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Données des Panneaux Solaires");

        for (Map.Entry<Integer, Double> entry : donnees.entrySet()) {
            String heure = entry.getKey() + "h";
            Double puissance = entry.getValue();
            System.out.println("Ajout au graphique : Heure = " + heure + ", Puissance = " + puissance);
            serie.getData().add(new XYChart.Data<>(heure, puissance));
        }

        graphiqueBarres.getData().add(serie);
        System.out.println("Graphique mis à jour avec succès.");
    }
}
