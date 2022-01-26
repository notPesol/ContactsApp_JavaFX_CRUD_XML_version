package com.example;

import com.example.datamodel.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactDialogController {
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField phoneNumberField;
    @FXML
    public TextField notesField;

    public Contact getNewContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        if (passTest(firstName, lastName, phoneNumber, notes)) {
            return new Contact(firstName, lastName, phoneNumber, notes);

        }

        return null;
    }

    public void editContact(Contact contact) {
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        notesField.setText(contact.getNotes());
    }

    public void updateContact(Contact contact) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        if (passTest(firstName, lastName, phoneNumber, notes)) {
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setPhoneNumber(phoneNumber);
            contact.setNotes(notes);
        }
    }

    private boolean passTest(String firstName, String lastName, String phoneNumber, String notes) {
        return !firstName.isEmpty() && !phoneNumber.isEmpty() && !lastName.isEmpty() && !notes.isEmpty();
    }
}
