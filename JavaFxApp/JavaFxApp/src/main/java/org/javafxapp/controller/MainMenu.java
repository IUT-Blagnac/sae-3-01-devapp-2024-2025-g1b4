package org.javafxapp.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.javafxapp.view.MainMenuViewController;

public class MainMenu extends Application {

    private Stage appStage;
    @Override
    public void start(Stage stage) throws Exception {
        this.appStage=stage;

        try {
            FXMLLoader loader = new FXMLLoader(MainMenuViewController.class.getResource("mainMenuView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);

            this.appStage.setScene(scene);
            this.appStage.setTitle("Menu");
            MainMenuViewController mMVC = loader.getController();
            mMVC.initContext(this.appStage, this);

            mMVC.displayDialog();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openConfig(){
        ConfigForm conForm=new ConfigForm(this.appStage);
        conForm.doConfigFormDialog();
    }

    public void openPanneau() {
        PanneauSolaire panneauSolaire = new PanneauSolaire(this.appStage);
        panneauSolaire.afficherFenetre();
    }
    

    public void openDataRoom() {
        ChooseDataRoom cDataRoom = new ChooseDataRoom(this.appStage);
    }

    public static void main2(String[] args) {
        launch();
    }
}
