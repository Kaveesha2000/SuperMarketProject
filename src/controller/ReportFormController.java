package controller;

import db.DbConnection;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class ReportFormController {
    public AnchorPane reportContext;
    public ImageView imgDailyReports;
    public ImageView imgMonthlyReports;
    public ImageView imgMovableItems;
    public Label lblMenu;
    public Label lblDescription;

    private JasperDesign design;
    private JasperReport compileReport;
    JasperPrint jasperPrint;

    public void moveToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminMainForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) reportContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    public void navigate(MouseEvent event) throws IOException, JRException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgDailyReports":
                    try {
                        design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/JasperReports/DailyIncome.jrxml"));
                        compileReport = JasperCompileManager.compileReport(design);

                        jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "imgMonthlyReports":
                    try {
                        design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/JasperReports/Items.jrxml"));
                        compileReport = JasperCompileManager.compileReport(design);

                        jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "imgMovableItems":
                    try {
                        design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/JasperReports/MovableItems.jrxml"));
                        compileReport = JasperCompileManager.compileReport(design);

                        jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.reportContext.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();
            }
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgDailyReports":
                    lblMenu.setText("View Daily Income");
                    lblDescription.setText("Click to view all daily incomes");
                    break;
                case "imgMonthlyReports":
                    lblMenu.setText("View Monthly Income");
                    lblDescription.setText("Click to view all monthly incomes");
                    break;
                case "imgMovableItems":
                    lblMenu.setText("View Most And Least Movable Items In The Sales");
                    lblDescription.setText("Click to view most and least movable item");
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

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("View Detail");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
}
