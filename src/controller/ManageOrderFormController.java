package controller;

import bo.BOFactory;
import bo.custom.PurchaseOrderBO;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import validation.ValidationUtil;
import view.tdm.OrderDetailTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManageOrderFormController {
    public JFXTextField txtItemName;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyWant;
    public Label lblOrderId;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtCustomerName;
    public JFXComboBox<String> cmbItemId;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;
    public TableView<OrderDetailTM> tblOrder;
    public TableColumn colItemId;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public Label lblTotal;
    public AnchorPane orderContext;
    public Button btnAdd;
    public Button btnPlaceOrder;
    public Button btnClear;
    public TableColumn colDiscount;
    public Button payBtn;

    private String orderId;

    private final PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.PURCHASE_ORDER);

    public void initialize() throws SQLException, ClassNotFoundException {
        orderId =generateNewOrderId();
        lblOrderId.setText(orderId);
        lblDate.setText(LocalDate.now().toString());
        lblTime.setText(LocalTime.now().toString());
        btnPlaceOrder.setDisable(true);
        payBtn.setDisable(true);

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();

            if (newValue != null) {
                try {
                    try {
                        if (!existCustomer(newValue + "")) {
                            //"There is no such customer associated with the id " + id
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + newValue + "").show();
                        }
                        /*Search Customer*/
                        CustomerDTO customerDTO = purchaseOrderBO.searchCustomer(newValue + "");
                        txtCustomerName.setText(customerDTO.getCustomerName());

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + newValue + "" + e).show();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
            }
        });

        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQtyOnHand.setEditable(newItemCode != null);
            btnAdd.setDisable(newItemCode == null);

            if (newItemCode != null) {
                try {
                    if (!existItem(newItemCode + "")) {
                        //throw new NotFoundException("There is no such item associated with the id " + code);
                    }
                    /*Find Item*/
                    ItemDTO item = purchaseOrderBO.searchItem(newItemCode + "");
                    txtItemName.setText(item.getDescription());
                    txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                    Optional<OrderDetailTM> optOrderDetail = tblOrder.getItems().stream().filter(detail -> detail.getCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getQty() : item.getQtyOnHand()) + "");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                txtItemName.clear();
                txtQtyOnHand.clear();
                txtQtyWant.clear();
                txtUnitPrice.clear();
                txtDiscount.clear();
            }
        });


        colItemId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblOrder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {
            if (selectedOrderDetail != null) {
                cmbCustomerId.setDisable(true);
                txtCustomerName.setDisable(true);
                cmbItemId.setDisable(true);
                txtItemName.setDisable(true);
                txtQtyOnHand.setDisable(true);
                txtUnitPrice.setDisable(true);
                cmbItemId.setValue(selectedOrderDetail.getCode());
                btnAdd.setText("Update");
                txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
                txtQtyWant.setText(selectedOrderDetail.getQty() + "");
                txtDiscount.setText(selectedOrderDetail.getDiscount() + "");
            } else {
                btnAdd.setText("Add");
                cmbItemId.setDisable(false);
                cmbItemId.getSelectionModel().clearSelection();
                txtQtyOnHand.clear();
            }
        });
        loadAllCustomerIds();
        loadAllItemCodes();
        btnAdd.setDisable(true);
        storeValidations();
    }

    private void loadAllCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = purchaseOrderBO.getAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerId.getItems().add(customerDTO.getCustomerId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItemCodes() {
        try {
            ArrayList<ItemDTO> all = purchaseOrderBO.getAllItems();
            for (ItemDTO dto : all) {
                cmbItemId.getItems().add(dto.getItemCode());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.ifCustomerExist(id);
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.ifItemExist(code);
    }

    public void addOnAction(ActionEvent actionEvent) {
        String itemCode = cmbItemId.getSelectionModel().getSelectedItem();
        double unitPrice = Double.parseDouble((txtUnitPrice.getText()));
        int qtyWant = Integer.parseInt(txtQtyWant.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        double total = unitPrice*(qtyWant)-qtyWant*(unitPrice*(discount)/100);

        if(qtyWant<=Integer.parseInt(txtQtyOnHand.getText())){
            boolean exists = tblOrder.getItems().stream().anyMatch(detail -> detail.getCode().equals(itemCode));

            if (exists) {
                OrderDetailTM orderDetailTM = tblOrder.getItems().stream().filter(detail -> detail.getCode().equals(itemCode)).findFirst().get();

                if (btnAdd.getText().equalsIgnoreCase("Update")) {
                    orderDetailTM.setQty(qtyWant);
                    orderDetailTM.setTotal(total);
                    orderDetailTM.setDiscount(discount);
                    tblOrder.getSelectionModel().clearSelection();
                    btnAdd.setDisable(false);

                    txtItemName.setDisable(false);
                    txtQtyOnHand.setDisable(false);
                    txtUnitPrice.setDisable(false);

                } else {
                    orderDetailTM.setQty(orderDetailTM.getQty() + qtyWant);
                    total = orderDetailTM.getQty()*(unitPrice);
                    orderDetailTM.setTotal(total);
                }
                tblOrder.refresh();
            } else {
                tblOrder.getItems().add(new OrderDetailTM(itemCode, qtyWant, unitPrice, discount, total ));
            }
            cmbItemId.getSelectionModel().clearSelection();
            cmbCustomerId.requestFocus();
            calculateTotal();
            enableOrDisablePlaceOrderButton();
        }
        else {
            new Alert(Alert.AlertType.WARNING, "Stock Out...!").show();
            cmbItemId.getSelectionModel().clearSelection();
            txtItemName.clear();
            txtUnitPrice.clear();
            txtQtyOnHand.clear();
            txtQtyWant.clear();
            txtDiscount.clear();
        }

    }

    private void calculateTotal() {
        double total=0 ;
        for (OrderDetailTM detail : tblOrder.getItems()) {
            total = total+detail.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblOrder.getItems().isEmpty()));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        cmbCustomerId.setDisable(false);
        txtCustomerName.setDisable(false);
        txtItemName.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);

        boolean b = saveOrder(orderId, LocalDate.now(), LocalTime.now(), cmbCustomerId.getValue(),
                tblOrder.getItems().stream().map(tm -> new OrderDetailDTO(orderId,
                        tm.getCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList()), Double.parseDouble(lblTotal.getText()));
        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").show();
        }

        /*orderId = generateNewOrderId();
        lblOrderId.setText(orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemId.getSelectionModel().clearSelection();
        tblOrder.getItems().clear();
        txtQtyWant.clear();*/
        calculateTotal();
        payBtn.setDisable(false);
    }

    public String generateNewOrderId() {
        try {
            return purchaseOrderBO.generateNewOrderId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrder(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, List<OrderDetailDTO> orderDetails, double orderTotal) {
        try {
            OrderDTO orderDTO = new OrderDTO(orderId, orderDate, orderTime, customerId, orderDetails, orderTotal);
            return purchaseOrderBO.purchaseOrder(orderDTO);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clearOnAction(ActionEvent actionEvent) {
        tblOrder.getItems().remove(tblOrder.getSelectionModel().getSelectedItem());
        tblOrder.getSelectionModel().clearSelection();
    }

    public void moveToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/CashierMainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) orderContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern qtyPattern = Pattern.compile("^[0-9]{1,5}$");
    Pattern discountPattern = Pattern.compile("^[0-9]{1,5}[.][0-9]{1,3}|[0-9]{1,5}$");

    private void storeValidations() {
        map.put(txtQtyWant, qtyPattern);
        map.put(txtDiscount, discountPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }


    public void payOnAction(ActionEvent actionEvent) {

        String oId = lblOrderId.getText();
        String customerId = cmbCustomerId.getValue();
        String date = lblDate.getText();
        String time = lblTime.getText();
        double cost = Double.parseDouble(lblTotal.getText());

        HashMap map = new HashMap();
        map.put("orderId", oId);
        map.put("CustomerId", customerId);
        map.put("OrderDate", date);
        map.put("OrderTime", time);
        map.put("Cost", cost);

        JasperDesign design = null;
        try {
            design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/JasperReports/Payment.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        orderId = generateNewOrderId();
        lblOrderId.setText(orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemId.getSelectionModel().clearSelection();
        tblOrder.getItems().clear();
        txtQtyWant.clear();

        payBtn.setDisable(true);
    }
}
