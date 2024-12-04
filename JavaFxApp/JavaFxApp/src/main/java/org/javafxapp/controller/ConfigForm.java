package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.ini4j.Profile;
import org.ini4j.Wini;
import org.javafxapp.tools.JsonInteract;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class ConfigForm {
    private Stage configStage;
    private ConfigFormViewController cFVM;
    public ConfigForm(Stage appStage) {

        this.roomChoice=new ArrayList<>();
        this.dataChoice=new ArrayList<>();

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

    private Wini wini;

    public void doConfigFormDialog() {
        JsonInteract jsInt=new JsonInteract();
        try{
            this.wini=new Wini(new File((String)jsInt.get("config.winiFilePath")));

            loadRoomData:{
                String data = wini.get("donnees", "salles");

                if (data == null)
                    break loadRoomData;

                String[] rooms = data.split(",");

                if (rooms.length < 1)
                    break loadRoomData;

                this.roomChoice.addAll(Arrays.asList(rooms));

            }

            String data=wini.get("donnees","donnees");

            if(data!=null)
                this.dataChoice.addAll(Arrays.asList(data.split(",")));

        }catch(IOException e){
            Alert alert=new Alert(Alert.AlertType.ERROR, "Le fichier de configuration(config.ini) est introuvable!! Vérifiez le chemin(appData.json)!!");
            alert.show();
            e.printStackTrace();
        }

        this.dataChoice=this.cFVM.displayDialog(this.dataChoice);
        if(!this.dataChoice.isEmpty() && !this.roomChoice.isEmpty())
            this.alterConfigFile();
    }

    private void alterConfigFile() {


        String choixDonnees=this.dataChoice.toString();
        choixDonnees=choixDonnees.substring(1,choixDonnees.length()-1);

        String choixSalles=this.roomChoice.toString();
        choixSalles=choixSalles.substring(1,choixSalles.length()-1);

        this.wini.put("donnees","donnees",choixDonnees.replaceAll("\\s",""));
        this.wini.put("donnees","salles",choixSalles.replaceAll("\\s", ""));

        try {
            this.wini.store();
        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR,"Le fichier config n'a pas pu être modifié!!");
            alert.show();
            e.printStackTrace();
        }
    }

    public void openRoomPicker() {
        RoomPicker rp=new RoomPicker(this.configStage);
        this.roomChoice=rp.doRoomPickerDialog(this.roomChoice);


    private List<String> roomChoice;
    private List<String> dataChoice;

    private List<String> seuils;

    public void getSeuilSelection(List<String> selectedData) {

        loadData:{
            Map<String,String> sec=wini.get("seuil");

            for(Map.Entry<String,String> entry : sec.entrySet()){
            }
        }
    }
}
