package org.javafxapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Validator;
import org.controlsfx.control.RangeSlider;
import org.javafxapp.controller.SeuilSeter;
import org.javafxapp.tools.JsonInteract;

import java.util.Arrays;
import java.util.List;


public class SeuilSeterViewController {

    private Stage appStage;

    private SeuilSeter seuilSeter;

    private Validator validator;

    private String values;
    public void initContext(Stage seuilStage, SeuilSeter seuilSeter,String dataType) {
        this.appStage=seuilStage;
        this.seuilSeter=seuilSeter;
        this.values=null;
        this.validator=new Validator();

        JsonInteract jsInt=new JsonInteract();
        this.dataType.setText((String)jsInt.get("traduction."+dataType));

        List<Double> seuils= Arrays.stream(((String)jsInt.get("seuils."+dataType)).split(","))
                .map((s) -> Double.parseDouble(s.trim())).toList();

        this.slider.setMin(seuils.get(0));
        this.slider.setMax(seuils.get(1));

        this.slider.setLowValue(0);
        this.slider.setHighValue(100);

        this.slider.setBlockIncrement(1);

        this.slider.setShowTickLabels(true);
    }

    public String displayDialog(String[] data) {

        this.valueMin.setText(data[0]);
        this.valueMax.setText(data[1]);

        StringConverter<Number> bindingTool=new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return number.toString();
            }

            @Override
            public Number fromString(String s) {
                if(s.isEmpty() || !s.matches("\\d+(\\.\\d){0,1}"))
                    return null;
                return Double.parseDouble(s);
            }
        };

        this.valueMin.textProperty().bindBidirectional(this.slider.lowValueProperty(), bindingTool);
        this.valueMax.textProperty().bindBidirectional(this.slider.highValueProperty(), bindingTool);


        this.setNumberCheck(this.valueMin);
        this.setNumberCheck(this.valueMax);

        Check minUnderMax=this.validator.createCheck();

        minUnderMax.dependsOn("1",this.valueMin.textProperty());
        minUnderMax.dependsOn("2",this.valueMax.textProperty());


        minUnderMax.withMethod( c -> {
            String minStr=this.valueMin.getText().trim(),
                    maxStr=this.valueMax.getText().trim();

            if(minStr.isEmpty() || maxStr.isEmpty() || !maxStr.matches("\\d+(\\.\\d){0,1}") || !minStr.matches("\\d+(\\.\\d){0,1}"))
                return;

            if(Double.parseDouble(minStr)>Double.parseDouble(maxStr))
                c.error("Le minimum doit être inferieur au maximum!");
        });
        minUnderMax.immediate();
        minUnderMax.decorates(this.valueMin);
        minUnderMax.decorates(this.valueMax);

        this.appStage.showAndWait();

        return this.values;
    }

    public void setNumberCheck(TextField node){
        Check isNum=this.validator.createCheck();

        isNum.dependsOn("value",node.textProperty());

        isNum.withMethod( c -> {
            String max=c.get("value");
            if(!max.trim().matches("\\d+(\\.\\d){0,1}") || max.isEmpty())
                c.error("Not a number");
        });

        isNum.immediate().decorates(node);

    }

    @FXML
    RangeSlider slider;

    @FXML
    TextField valueMin;

    @FXML
    TextField valueMax;

    @FXML
    Label dataType;


    @FXML
    public void doOk() {

        if (!this.valueMin.getText().trim().matches("\\d+(\\.\\d){0,1}") || !this.valueMin.getText().trim().matches("\\d+(\\.\\d){0,1}")
                || this.valueMin.getText().trim().isEmpty() || this.valueMax.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Les champs ne sont pas correctement remplis!\nPassez la souris sur la croix pour plus d'informations");
            alert.show();
            return;
        }

        this.values = this.valueMin.getText().trim() + "," + this.valueMax.getText().trim();

        this.appStage.close();
    }
}
