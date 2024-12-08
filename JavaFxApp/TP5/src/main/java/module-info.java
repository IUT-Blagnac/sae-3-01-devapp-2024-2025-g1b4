module org.example.tp5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.tp5 to javafx.fxml;
    exports org.example.tp5;
}