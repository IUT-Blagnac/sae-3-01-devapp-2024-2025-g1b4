package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;

public class ConfigForm {
    private Stage configStage;
    private ConfigFormViewController cFVM;
    public ConfigForm(Stage appStage) {

        try {
            FXMLLoader loader = new FXMLLoader(ConfigFormViewController.class.getResource("configFormView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);

            this.configStage = new Stage();
            this.configStage.initModality(Modality.WINDOW_MODAL);
            this.configStage.initOwner(appStage);
            StageManagement.manageCenteringStage(appStage, this.configStage);
            this.configStage.setScene(scene);
            this.configStage.setTitle("Selection des salles");
            this.configStage.setResizable(false);

            this.cFVM = loader.getController();
            this.cFVM.initContext(this.configStage,this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doConfigFormDialog() {
        cFVM.displayDialog();
    }

    public void openRoomPicker() {
        RoomPicker rp=new RoomPicker(this.configStage);
        rp.doRoomPickerDialog();
    }
}
