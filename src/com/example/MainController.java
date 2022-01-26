package com.example;

import com.example.datamodel.Contact;
import com.example.datamodel.ContactData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML
    public TableView<Contact> contactsTableView;

    @FXML
    public TableColumn<Contact, String> firstNameColumn;
    @FXML
    public TableColumn<Contact, String> lastNameColumn;
    @FXML
    public TableColumn<Contact, String> phoneNumberColumn;
    @FXML
    public TableColumn<Contact, String> notesColumn;

    @FXML
    public BorderPane mainPanel;

    private ContactData contactData;

    public void initialize() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Edit");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showEditContactDialog(event);
            }
        });
        MenuItem menuItem2 = new MenuItem("Delete");
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showDeleteAlert(event);
            }
        });
        contextMenu.getItems().addAll(menuItem, menuItem2);


        contactsTableView.setContextMenu(contextMenu);

        // load data and set to table
        contactData = ContactData.getInstance();
        contactData.loadContacts();
        contactsTableView.setItems(contactData.getContacts());
    }

    @FXML
    public void showAddContactDialog(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Contact");
        dialog.setHeaderText("Add Contact...");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contact_dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException ex) {
            ex.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ContactDialogController contactDialogController = fxmlLoader.getController();

            Contact newContact = contactDialogController.getNewContact();
            contactData.addContact(newContact);
        }
    }

    @FXML
    public void showEditContactDialog(ActionEvent event) {
        Contact contact = contactsTableView.getSelectionModel().getSelectedItem();
        if (contact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact selected");
            alert.setHeaderText("No Contact selected");
            alert.setContentText("Please select a Contact to edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("Edit Contact...");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contact_dialog.fxml"));
        try {
            DialogPane dialogPane = fxmlLoader.load();
            dialogPane.setHeaderText("Fill in information for the edit the Contact");
            dialog.getDialogPane().setContent(dialogPane);

        }catch (IOException ex) {
            System.out.println("Couldn't not load the dialog");
            ex.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactDialogController contactDialogController = fxmlLoader.getController();
        contactDialogController.editContact(contact);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactDialogController.updateContact(contact);
        }
    }

    @FXML
    public void showDeleteAlert(ActionEvent event) {
        Contact selectedContact = contactsTableView.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact selected");
            alert.setHeaderText("No Contact selected");
            alert.setContentText("Please select the Contact to delete");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete the Contact");
        alert.setHeaderText("Delete the Contact...");
        alert.setContentText("Are you sure you want to delete the selected contact: " +
                selectedContact.getFirstName() + " " + selectedContact.getLastName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactData.deleteContact(selectedContact);
        }
    }

    public void closeApp(ActionEvent event) {
        Platform.exit();
    }
}
