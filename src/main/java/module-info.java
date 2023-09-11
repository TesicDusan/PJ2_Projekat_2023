module com.pj2.pj2_projekat_2023 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.pj2.pj2_projekat_2023 to javafx.fxml;
    exports com.pj2.pj2_projekat_2023;
    exports com.pj2.pj2_projekat_2023.vozila;
    opens com.pj2.pj2_projekat_2023.vozila to javafx.fxml;
    exports com.pj2.pj2_projekat_2023.terminali;
    opens com.pj2.pj2_projekat_2023.terminali to javafx.fxml;
    exports com.pj2.pj2_projekat_2023.kontroleri;
    opens com.pj2.pj2_projekat_2023.kontroleri to javafx.fxml;
}