module MyContacts {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;

    opens com.example;
    opens com.example.datamodel;
}