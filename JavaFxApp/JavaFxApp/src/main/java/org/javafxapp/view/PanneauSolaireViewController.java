package org.javafxapp.view;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.javafxapp.controller.PanneauSolaire;

public class PanneauSolaireViewController {

    private Stage appStage;
    private PanneauSolaire panneauLoader;

    public void initContext(Stage appStage, PanneauSolaire pSolaire) {
        this.appStage=appStage;
        this.panneauLoader = pSolaire;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {

        this.appStage.showAndWait();

        this.alterPanneauFile();
    }

    private void alterPanneauFile() {
    }

    @FXML
    AutoCompletionTextFieldBinding<String> roomInput;

    
}


    
