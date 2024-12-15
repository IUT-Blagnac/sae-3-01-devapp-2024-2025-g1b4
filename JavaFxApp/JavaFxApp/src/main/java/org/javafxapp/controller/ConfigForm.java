package org.javafxapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Pair;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.javafxapp.tools.JsonInteract;
import org.javafxapp.tools.StageManagement;
import org.javafxapp.view.ConfigFormViewController;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Classe de configuration du formulaire.
 */
public class ConfigForm {
    private Stage configStage;
    private ConfigFormViewController cFVM;

    /**
     * Constructeur pour le formulaire de configuration
     * @param appStage La scène de l'application
     */
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

    /**
     * Initialise le formulaire de configuration.
     */
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

            this.tps=wini.get("donnees","temps");

        } catch(IOException e){
            Alert alert=new Alert(Alert.AlertType.ERROR, "Le fichier de configuration(config.ini) est introuvable!! Vérifiez le chemin(appData.json)!!");
            alert.show();
            e.printStackTrace();
        }

        this.dataChoice=this.cFVM.displayDialog(this.dataChoice,this.tps);

        if(!this.dataChoice.isEmpty() && !this.roomChoice.isEmpty()) {
            this.alterConfigFile();
            this.rememberChoiceJSon();
        }
    }

    /**
     * Enregistre le choix dans un fichier JSON.
     */
    private void rememberChoiceJSon() {
        JsonInteract jsInt=new JsonInteract();

        JSONObject jsObj=(JSONObject) jsInt.get("communes");

        jsObj.put("chosenData",this.dataChoice);
        jsObj.put("chosenRooms",this.roomChoice);

        jsInt.properClose();
    }

    /**
     * Altere le fichier de configuration.
     */
    private void alterConfigFile() {
        String choixDonnees=this.dataChoice.toString();
        choixDonnees=choixDonnees.substring(1,choixDonnees.length()-1);
        String choixSalles=this.roomChoice.toString();
        choixSalles=choixSalles.substring(1,choixSalles.length()-1);

        this.wini.put("donnees","donnees",choixDonnees.replaceAll("\\s",""));
        this.wini.put("donnees","salles",choixSalles.replaceAll("\\s", ""));
        this.wini.put("donnees","temps",this.tps.replaceAll("\\s", ""));

        Profile.Section section=this.wini.get("seuil");
        section.putAll(this.seuils);

        try {
            this.wini.store();
        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR,"Le fichier config n'a pas pu être modifié!!");
            alert.show();
            e.printStackTrace();
        }
    }

    /**
     * Ouvre le choix de la salle.
     */
    public void openRoomPicker() {
        RoomPicker rp = new RoomPicker(this.configStage);
        this.roomChoice = rp.doRoomPickerDialog(this.roomChoice);
    }

    private List<String> roomChoice;
    private List<String> dataChoice;
    private String tps;
    private Map<String,String> seuils;

    /**
     * Obtient une sélection de seuil.
     * @param selectedData Les données sélectionnées
     */
    public void getSeuilSelection(List<String> selectedData) {
        Map<String,String> prevSeuils=wini.get("seuil");

        this.seuils=new HashMap<>();

        for(String str:selectedData){
            SeuilSeter seuilSeter=new SeuilSeter(this.configStage,str);
            String prevSeuil=prevSeuils.get(str)==null ? prevSeuils.get(str) : "0,100";
            String newSeuil=seuilSeter.displayDialog(prevSeuil.split(","));
            this.seuils.put(str,newSeuil!=null ? newSeuil : prevSeuil);
        }
    }
}