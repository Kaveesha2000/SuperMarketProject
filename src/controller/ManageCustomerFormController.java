package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import com.jfoenix.controls.JFXTextField;
import dto.CustomerDTO;
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
import view.tdm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ManageCustomerFormController {
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalCode;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtCustomerProvince;
    public JFXTextField txtCustomerCity;
    public JFXTextField txtCustomerPostalCode;
    public AnchorPane customerContext;
    public Button btnSave;

    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.CUSTOMER);

    public void initialize(){

        txtCustomerId.setText(generateNewId());
        txtCustomerId.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("customerTitle"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("customerProvince"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));

        loadAllCustomers();
        btnSave.setDisable(true);
        storeValidations();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtCustomerId.setText(newValue.getCustomerId());
                txtCustomerTitle.setText(newValue.getCustomerTitle());
                txtCustomerName.setText(newValue.getCustomerName());
                txtCustomerAddress.setText(newValue.getCustomerAddress());
                txtCustomerCity.setText(newValue.getCustomerCity());
                txtCustomerProvince.setText(newValue.getCustomerProvince());
                txtCustomerPostalCode.setText(newValue.getCustomerPostalCode());
                txtCustomerId.setDisable(true);
                btnSave.setDisable(true);
            }
        });
    }

    /*public void getDetails(){
        String id = txtCustomerId.getText();
        String title = txtCustomerTitle.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();
        String city = txtCustomerCity.getText();
        String province = txtCustomerProvince.getText();
        String postalCode = txtCustomerPostalCode.getText();
    }*/

    private void loadAllCustomers() {
        tblCustomer.getItems().clear();
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomer();
            for (CustomerDTO customer : allCustomers) {
                tblCustomer.getItems().add(new CustomerTM(customer.getCustomerId(), customer.getCustomerTitle(), customer.getCustomerName(),
                        customer.getCustomerAddress(),customer.getCustomerCity(),customer.getCustomerProvince(),customer.getCustomerPostalCode()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustomerName.getText();
        String address=txtCustomerAddress.getText();
        String city=txtCustomerCity.getText();
        String province=txtCustomerProvince.getText();
        String postalCode=txtCustomerPostalCode.getText();

        try {
            if (existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, id + " Already Exists").show();
            }
            else{
                new Alert(Alert.AlertType.CONFIRMATION,  "Saved...!").show();
                clear();
                CustomerDTO customerDTO = new CustomerDTO(id, title, name, address, city, province, postalCode);
                customerBO.addCustomer(customerDTO);
                tblCustomer.getItems().add(new CustomerTM(id, title, name, address, city, province, postalCode));
                txtCustomerId.setText(generateNewId());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.ifCustomerExist(id);
    }

    public void updateOnAction(ActionEvent actionEvent) {

        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustomerName.getText();
        String address=txtCustomerAddress.getText();
        String city=txtCustomerCity.getText();
        String province=txtCustomerProvince.getText();
        String postalCode=txtCustomerPostalCode.getText();


        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            else {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!").show();
                clear();
                CustomerDTO customerDTO = new CustomerDTO(id, title, name, address, city, province, postalCode);
                customerBO.updateCustomer(customerDTO);
                txtCustomerId.setText(generateNewId());
                btnSave.setDisable(false);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + id + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        CustomerTM selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        selectedCustomer.setCustomerName(name);
        selectedCustomer.setCustomerTitle(title);
        selectedCustomer.setCustomerAddress(address);
        selectedCustomer.setCustomerCity(city);
        selectedCustomer.setCustomerProvince(province);
        selectedCustomer.setCustomerPostalCode(postalCode);
        tblCustomer.refresh();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        String id = tblCustomer.getSelectionModel().getSelectedItem().getCustomerId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!").show();
                customerBO.deleteCustomer(id);
                tblCustomer.getItems().remove(tblCustomer.getSelectionModel().getSelectedItem());
                tblCustomer.getSelectionModel().clearSelection();
                clear();
                txtCustomerId.setText(generateNewId());
                btnSave.setDisable(false);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        txtCustomerId.clear();
        txtCustomerTitle.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerCity.clear();
        txtCustomerProvince.clear();
        txtCustomerPostalCode.clear();
    }

    private String generateNewId() {
        try {
            return customerBO.generateNewID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (tblCustomer.getItems().isEmpty()) {
            return "C001";
        } else {
            String id = getLastCustomerId();
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        }
    }

    private String getLastCustomerId() {
        List<CustomerTM> tempCustomersList = new ArrayList<>(tblCustomer.getItems());
        Collections.sort(tempCustomersList);
        return tempCustomersList.get(tempCustomersList.size() - 1).getCustomerId();
    }

    public void moveToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/CashierMainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) customerContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern titlePattern = Pattern.compile("^[A-z ]{1,5}$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{6,30}$");
    Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
    Pattern provincePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern postalCodePattern = Pattern.compile("^[0-9]{3,10}$");

    private void storeValidations() {
        map.put(txtCustomerTitle, titlePattern);
        map.put(txtCustomerName, namePattern);
        map.put(txtCustomerAddress, addressPattern);
        map.put(txtCustomerProvince, provincePattern);
        map.put(txtCustomerCity, cityPattern);
        map.put(txtCustomerPostalCode, postalCodePattern);
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
