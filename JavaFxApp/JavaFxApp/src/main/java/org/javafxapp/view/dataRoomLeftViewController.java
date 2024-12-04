package org.javafxapp.view;

import java.net.URL;

import java.util.ResourceBundle;

import org.javafxapp.controller.DataRoomLeft;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DataRoomLeftViewController implements Initializable {

    private DataRoomLeft dialogController;

    private Stage appStage;

    public void initContext(Stage appStage, DataRoomLeft stageControl) {
        this.dialogController = stageControl;
        this.appStage = appStage;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    @FXML
    private Label labelSalle;

    @FXML
    private ChoiceBox<String> myChoiceBoxSalle;

    private String[] salle = { "B103", "E101", "E207", "E209"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myChoiceBoxSalle.getItems().addAll(salle);
    }

    public void displayDialog() {
        this.appStage.show();
    }

}
    

