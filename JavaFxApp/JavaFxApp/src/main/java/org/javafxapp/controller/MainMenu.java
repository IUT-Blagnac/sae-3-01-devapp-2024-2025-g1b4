package org.javafxapp.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.javafxapp.view.MainMenuViewController;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Classe Main Menu pour l'application. C'est la classe principale qui gère les interactions utilisateur et ouvre diverses fenêtres dans l'application.
 * Elle gère également la connexion au service back-end python.
 */
public class MainMenu extends Application {

    public static Process pythonProcess;

    private Stage appStage; // Le stage de l'application.
    private AtomicBoolean checkingWarnings; // Booléen pour vérifier les alertes.

    /**
     * Le point de démarrage de l'application. Initialise le menu principal.
     * @param  stage  la stage de départ pour l'application
     */
    @Override
    public void start(Stage stage) {
        this.checkingWarnings=new AtomicBoolean(false);
        this.loadPage(stage, true);
    }

    public void loadPage(Stage stage,boolean firstLaunch){
        this.appStage=stage;
        try {
            FXMLLoader loader = new FXMLLoader(MainMenuViewController.class.getResource("mainMenuView.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
            this.appStage.setScene(scene);
            this.appStage.setTitle("Menu");
            MainMenuViewController mMVC = loader.getController();

            mMVC.initContext(this.appStage, this);
            mMVC.displayDialog(firstLaunch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre de configuration.
     */
    public void openConfig(){
        ConfigForm conForm=new ConfigForm(this.appStage);
        conForm.doConfigFormDialog();
        this.launchPython();
    }

    /**
     * Ouvre la fenêtre d'affichage des données des Panneaux Solaires.
     */
    public void openPanneau() {
        PanneauSolaire panneauSolaire = new PanneauSolaire(this.appStage);
        panneauSolaire.afficherFenetre();
    }

    /**
     * Ouvre la fenêtre d'affichage des données des salles.
     */
    public void openDataRoom() {
        ChooseDataRoom cDataRoom = new ChooseDataRoom(this.appStage);
    }

    /**
     * Lance le script Python en arrière-plan.
     */
    public void launchPython(){
        if(MainMenu.pythonProcess!=null) {
            MainMenu.pythonProcess.destroyForcibly();

            try {
                MainMenu.pythonProcess.waitFor();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.startVirtualThread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainMenu.pythonProcess = Runtime.getRuntime().exec("python ./iot/iot.py");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Wait for the process to finish and check the exit code
                try {
                    MainMenu.pythonProcess.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.startCheckingWarnings();
    }

    /**
     * Commence à vérifier les alertes du service Python.
     */
    public void startCheckingWarnings() {
        this.checkingWarnings=new AtomicBoolean(true);

        Thread.startVirtualThread(new Runnable() {
            @Override
            public void run() {
                File warnings=new File("./AlertPipe.txt");
                try {
                    warnings.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                while(checkingWarnings.get()){
                    try {
                        Thread.sleep(200);
                        Scanner warning=new Scanner(warnings).useDelimiter(".");
                        if(warning.hasNext()){
                            String info=warning.next();
                            if(info.trim().equals("start"))
                                Platform.runLater(()->{
                                    Notifications.create()
                                            .title("Début du Python")
                                            .text("Le python a bien été lancé!")
                                            .showInformation();
                                });
                            else
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title(info)
                                            .text(warning.next())
                                            .showWarning();
                                });
                            PrintWriter pw=new PrintWriter(warnings);
                            pw.print("");
                            pw.close();
                        }
                    } catch (FileNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * Teste la connexion au script Python.
     */
    public void testConnexion() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Process process;
                try {
                    process = Runtime.getRuntime().exec("python ./iot/iot.py test");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int exit = 1;
                // Wait for the process to finish and check the exit code
                try {
                    exit=process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(exit==0)
                    Notifications.create().text("La connexion est un succès ! ").showInformation();
                else
                    Notifications.create().text("La connexion a échouée!").showError();
            }
        });
    }

    /**
     * Fonction main pour démarrer l'application.
     * @param args les arguments de la ligne de commande.
     */
    public static void main2(String[] args) {
        launch();
    }
}