package org.javafxapp.view;

import java.net.URL;

import java.util.ResourceBundle;

import org.javafxapp.controller.DataRoomLeft;
import org.javafxapp.tools.JsonInteract;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.*;


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JsonInteract jsInt=new JsonInteract();

        System.out.println(jsInt.get("communes.chosenData"));
        System.out.println(((JSONArray)jsInt.get("communes.chosenData")).toList().stream().map(Object::toString).toList());

        myChoiceBoxSalle.getItems().addAll(((JSONArray)jsInt.get("communes.chosenData")).toList().stream().map(Object::toString).toList());
    }

    public void displayDialog() {
        this.appStage.show();
    }

}


