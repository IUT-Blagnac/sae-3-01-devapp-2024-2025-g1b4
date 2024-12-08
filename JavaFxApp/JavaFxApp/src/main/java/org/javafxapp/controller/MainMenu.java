package org.javafxapp.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.javafxapp.view.MainMenuViewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenu extends Application {

    private Stage appStage;

    private Process pythonProcess;
    @Override
    public void start(Stage stage) {

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
        this.launchPython();
    }

    public void openPanneau() {
        PanneauSolaire panneauSolaire = new PanneauSolaire(this.appStage);
        panneauSolaire.afficherFenetre();
    }
    

    public void openDataRoom() {
        ChooseDataRoom cDataRoom = new ChooseDataRoom(this.appStage);
    }

    public void launchPython(){

        if(this.pythonProcess!=null)
            this.pythonProcess.destroy();

        Thread.startVirtualThread(new Runnable()     {
            @Override
            public void run() {
                try {
                    MainMenu.this.pythonProcess = Runtime.getRuntime().exec("python ./iot/iot.py");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Wait for the process to finish and check the exit code
                try {
                    MainMenu.this.pythonProcess.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    public void testConnexion() {

    }

    public static void main2(String[] args) {
        launch();
    }
}
