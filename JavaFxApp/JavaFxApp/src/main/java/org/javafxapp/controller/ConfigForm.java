package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.ini4j.Wini;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

        this.dataChoice=this.cFVM.displayDialog();
        if(!this.dataChoice.isEmpty())
            this.alterConfigFile();
        cFVM.displayDialog();
    }

    public void openRoomPicker() {
        RoomPicker rp=new RoomPicker(this.configStage);
        rp.doRoomPickerDialog();
    }

    private void alterConfigFile() {
//        try {
//            Wini ini = new Wini(new File(Main.IOTPath));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void openRoomPicker() {
        RoomPicker rp=new RoomPicker(this.configStage);
        this.roomChoice=rp.doRoomPickerDialog();
    }

    private List<String> roomChoice;
    private List<String> dataChoice;

}
