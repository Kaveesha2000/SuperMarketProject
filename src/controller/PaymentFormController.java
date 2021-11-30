package controller;

import bo.BOFactory;
import bo.custom.PurchaseOrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import dao.custom.QueryDAO;
import dto.CustomDTO;
import dto.CustomerDTO;
import dto.OrderDTO;
import entity.Orders;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.tdm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentFormController {
    public AnchorPane paymentContext;
    public TableView<Orders> tblPayments;
    public TableColumn colOrderId;
    public TableColumn colCustomerId;
    public TableColumn colCost;

    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        loadAllOrders();
    }

    private void loadAllOrders() {
        tblPayments.getItems().clear();
        try {
            ArrayList<Orders> arrayList = orderDAO.getAll();
            for (Orders customDTO :arrayList ) {
                tblPayments.getItems().add(new Orders(customDTO.getOrderId(),customDTO.getOrderDate(),
                        customDTO.getOrderTime(),customDTO.getCost(),customDTO.getCustomer()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void moveToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/CashierMainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) paymentContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }
}
