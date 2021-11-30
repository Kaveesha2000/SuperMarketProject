package controller;

import bo.BOFactory;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXTextField;
import dto.ItemDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import validation.ValidationUtil;
import view.tdm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageItemFormController {
    public JFXTextField txtItemId;
    public JFXTextField txtPackSize;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public JFXTextField txtItemDescription;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public AnchorPane itemContext;

    private final ItemBO itemBO = (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);

    public Button btnSave;

    public void initialize(){

        txtItemId.setText(generateNewId());
        txtItemId.setDisable(true);

        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadAllItems();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtItemId.setText(newValue.getItemCode());
                txtItemDescription.setText(newValue.getDescription());
                txtPackSize.setText(newValue.getPackSize());
                txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
                txtItemId.setDisable(true);
                btnSave.setDisable(true);
            }
        });
        btnSave.setDisable(true);
        storeValidations();
    }

    private void loadAllItems() {
        tblItem.getItems().clear();
        try {
            ArrayList<ItemDTO> allItems = itemBO.getAllItems();
            for (ItemDTO item : allItems) {
                tblItem.getItems().add(new ItemTM(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        txtItemId.clear();
        txtItemDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemBO.ifItemExist(code);
    }

    public void saveOnAction(ActionEvent actionEvent) {

        try {
            String itemId = txtItemId.getText();
            String description = txtItemDescription.getText();
            String packSize = txtPackSize.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            if (existItem(itemId)) {
                new Alert(Alert.AlertType.ERROR, itemId + " Already Exists").show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...!").show();
                ItemDTO dto = new ItemDTO(itemId, description, packSize, unitPrice, qtyOnHand);
                itemBO.addItem(dto);
                tblItem.getItems().add(new ItemTM(itemId, description, packSize, unitPrice, qtyOnHand));
                clear();
                txtItemId.setText(generateNewId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateOnAction(ActionEvent actionEvent) {

        String itemId = txtItemId.getText();
        String description = txtItemDescription.getText();
        String packSize = txtPackSize.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {

            if (!existItem(itemId)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + itemId).show();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!").show();
                ItemDTO dto = new ItemDTO(itemId, description, packSize, unitPrice, qtyOnHand);
                itemBO.updateItem(dto);
                ItemTM selectedItem = tblItem.getSelectionModel().getSelectedItem();
                selectedItem.setDescription(description);
                selectedItem.setPackSize(packSize);
                selectedItem.setQtyOnHand(qtyOnHand);
                selectedItem.setUnitPrice(unitPrice);
                tblItem.refresh();
                clear();
                btnSave.setDisable(false);
                txtItemId.setText(generateNewId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        String code = tblItem.getSelectionModel().getSelectedItem().getItemCode();
        try {
            if (!existItem(code)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + code).show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!").show();
                itemBO.deleteItem(code);
                tblItem.getItems().remove(tblItem.getSelectionModel().getSelectedItem());
                tblItem.getSelectionModel().clearSelection();
                clear();
                btnSave.setDisable(false);
                txtItemId.setText(generateNewId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the item " + code).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String generateNewId() {
        try {
            return itemBO.generateNewID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "I001";
    }

    public void moveToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminMainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) itemContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern discriptionPattern = Pattern.compile("^[A-z ]{1,}$");
    Pattern packSizePattern = Pattern.compile("^[A-z0-9 ]{1,20}$");
    Pattern unitPricePattern = Pattern.compile("^[0-9]{1,5}[.][0-9]{1,3}$");
    Pattern qtyOnHandPattern = Pattern.compile("^[0-9]{1,5}$");

    private void storeValidations() {
        map.put(txtItemDescription, discriptionPattern);
        map.put(txtPackSize, packSizePattern);
        map.put(txtUnitPrice, unitPricePattern);
        map.put(txtQtyOnHand, qtyOnHandPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnSave);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }
}
