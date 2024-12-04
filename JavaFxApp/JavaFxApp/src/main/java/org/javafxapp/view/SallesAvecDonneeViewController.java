package org.javafxapp.view;

import org.javafxapp.controller.SallesAvecDonnee;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class SallesAvecDonneeViewController {
    private SallesAvecDonnee dialogController;

    private Stage appStage;

    @FXML
    private ComboBox<String> dataSelectionComboBox;

    @FXML
    private LineChart<String, Number> lineChart;

    public void initContext(Stage appStage, SallesAvecDonnee stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());


        dataSelectionComboBox.getItems().addAll("Co2", "Truc 2", "Truc 3");

        dataSelectionComboBox.setOnAction(event -> {
            System.out.println("Vous avez sélectionné : " + dataSelectionComboBox.getValue());
        });
    }

    public void displayDialog() {
        this.appStage.show();
    }
    
}
