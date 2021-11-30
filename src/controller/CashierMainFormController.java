package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class CashierMainFormController {
    public ImageView imgCustomer;
    public ImageView imgOrder;
    public ImageView imgPayment;
    public Label lblMenu;
    public Label lblDescription;
    public AnchorPane cashierMainFormContext;

    public void navigate(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgCustomer":
                    root = FXMLLoader.load(this.getClass().getResource("../view/ManageCustomerForm.fxml"));
                    break;
                case "imgOrder":
                    root = FXMLLoader.load(this.getClass().getResource("../view/ManageOrderForm.fxml"));
                    break;
                case "imgPayment":
                    root = FXMLLoader.load(this.getClass().getResource("../view/PaymentForm.fxml"));
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.cashierMainFormContext.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    public void playMouseEnterAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            switch (icon.getId()) {
                case "imgCustomer":
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "imgOrder":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
                case "imgPayment":
                    lblMenu.setText("Manage Payment");
                    lblDescription.setText("Click to do payment");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    public void exit(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/MainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) cashierMainFormContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }
}
