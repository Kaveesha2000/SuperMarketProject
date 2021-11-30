package controller;

import bo.BOFactory;
import bo.custom.LoginBO;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import dto.LoginDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFormController {
    public AnchorPane mainFormContext;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Label errorLabel;

    private final LoginBO loginBO = (LoginBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.LOGIN);

    public void logInOnAction(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        String UserName=txtUserName.getText();
        String Password=txtPassword.getText();

        LoginDTO loginDTO = new LoginDTO(UserName,Password);
        loginBO.ifUserExists(UserName,Password);

        if (loginDTO.getUserName().equals("Admin") && loginDTO.getPassWord().equals("1234")){
            URL resource = getClass().getResource("../view/AdminMainForm.fxml");
            Parent load = FXMLLoader.load(resource);
            Stage window = (Stage) mainFormContext.getScene().getWindow();
            window.setTitle("Admin Form");
            window.setScene(new Scene(load));
        }else if (loginDTO.getUserName().equals("Cashier") && loginDTO.getPassWord().equals("1234")){
            URL resource = getClass().getResource("../view/CashierMainForm.fxml");
            Parent load = FXMLLoader.load(resource);
            Stage window = (Stage) mainFormContext.getScene().getWindow();
            window.setTitle("Cashier Form");
            window.setScene(new Scene(load));
        }else
            errorLabel.setText("Enter correct username or password");
    }

    public void goToPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }
}
