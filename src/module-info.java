module StateOfTheArtGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens sample to javafx.fxml;
    exports sample;
}