package org.javafxapp.view;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.javafxapp.controller.ConfigForm;

public class ConfigFormViewController {

    private Stage appStage;
    private ConfigForm conFormLoader;

    public void initContext(Stage appStage, ConfigForm configForm) {
        this.appStage=appStage;
        this.conFormLoader=configForm;
        this.appStage.setOnCloseRequest(e -> this.appStage.close());
    }

    public void displayDialog() {

        this.appStage.showAndWait();

        this.alterConfigFile();
    }

    private void alterConfigFile() {
    }

    @FXML
    AutoCompletionTextFieldBinding<String> roomInput;

     @FXML
    public void doOpenRoom(){
        this.conFormLoader.openRoomPicker();
     }
}
