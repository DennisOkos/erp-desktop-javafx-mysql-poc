package app.ui;

import data.CustomerDAO;
import domain.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class CustomerView extends VBox {
    private final TableView<Customer> table = new TableView<>();
    private final ObservableList<Customer> data = FXCollections.observableArrayList();
    private final CustomerDAO dao = new CustomerDAO();

    public CustomerView() {
        setPadding(new Insets(10));
        setSpacing(10);

        TableColumn<Customer, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()));
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhone()));
        TableColumn<Customer, String> createdCol = new TableColumn<>("Created At");
        createdCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCreatedAt()));

        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, createdCol);
        table.setItems(data);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button exportBtn = new Button("Export CSV");

        HBox controls = new HBox(5, nameField, emailField, phoneField, addBtn, updateBtn, deleteBtn, exportBtn);

        addBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                showAlert("Validation", "Name and Email required.");
                return;
            }
            Customer c = new Customer(0, nameField.getText(), emailField.getText(), phoneField.getText(), null);
            dao.create(c);
            refresh();
        });

        updateBtn.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            selected.setName(nameField.getText());
            selected.setEmail(emailField.getText());
            selected.setPhone(phoneField.getText());
            dao.update(selected);
            refresh();
        });

        deleteBtn.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            dao.delete(selected.getId());
            refresh();
        });

        exportBtn.setOnAction(e -> {
            try {
                dao.exportToCSV("customers_export.csv");
                showAlert("Export", "Exported to customers_export.csv");
            } catch (Exception ex) {
                showAlert("Export Error", ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nameField.setText(newSel.getName());
                emailField.setText(newSel.getEmail());
                phoneField.setText(newSel.getPhone());
            }
        });

        getChildren().addAll(table, controls);
        refresh();
    }

    private void refresh() {
        List<Customer> customers = dao.findAll();
        data.setAll(customers);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}

