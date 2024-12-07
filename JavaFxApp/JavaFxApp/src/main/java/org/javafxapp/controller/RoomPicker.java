package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ini4j.Wini;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;
import org.javafxapp.view.RoomPickerViewController;

import java.util.List;

public class RoomPicker {
    private Stage roomPickerStage;

    private RoomPickerViewController rPVM;
    public RoomPicker(Stage appStage) {

        try {
            FXMLLoader loader = new FXMLLoader(ConfigFormViewController.class.getResource("roomPickerView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            this.roomPickerStage = new Stage();
            this.roomPickerStage.initModality(Modality.WINDOW_MODAL);
            this.roomPickerStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.roomPickerStage);
            this.roomPickerStage.setScene(scene);
            this.roomPickerStage.setTitle("Selection des salles");
            this.roomPickerStage.setResizable(false);

            this.rPVM = loader.getController();
            this.rPVM.initContext(this.roomPickerStage,this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> doRoomPickerDialog(List<String> rooms) {
        return this.rPVM.displayDialog(rooms);
    }
}
