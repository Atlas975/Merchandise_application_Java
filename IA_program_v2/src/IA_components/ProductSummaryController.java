package IA_components;

import SQL_Connection.SQLConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductSummaryController implements Initializable {

    public PieChart FX_costChart;
    public BarChart FX_bar;
    public Slider mailSlider;
    public Label FX_percent;
    public Label FX_barselect;
    public Label totBranch2;
    public Label totBranch3;
    public Label totBranch4;
    public Label totCosts;
    public Label totProducts;
    public Label totSupply;
    public Button FX_return;

    PreparedStatement pst = null;
    PreparedStatement pst2 = null;
    Connection conn = null;

    public void Queries() throws SQLException {  // Retrieves specific stats related to product data
        conn = SQLConnect.getConnection();
        int pie2 = 0;
        int pie3 = 0;
        int pie4 = 0;
        int pieTotal = 0;
        int bookBar = 0;
        int toolBar = 0;
        int foodBar = 0;
        int medBar = 0;

        String que4 = "SELECT SUM(itemPrice*itemQuantity) FROM products WHERE branch=2;";
        Statement statment4 = conn.createStatement();
        ResultSet rs4 = statment4.executeQuery(que4);
        while (rs4.next()) {
            pie2 = Integer.parseInt(rs4.getString(1));
            totBranch2.setText(rs4.getString(1) + " AED");
        }

        String que5 = "SELECT SUM(itemPrice*itemQuantity) FROM products WHERE branch=3;";
        Statement statment5 = conn.createStatement();
        ResultSet rs5 = statment5.executeQuery(que5);
        while (rs5.next()) {
            pie3 = Integer.parseInt(rs5.getString(1));
            totBranch3.setText(rs5.getString(1) + " AED");
        }

        String que6 = "SELECT SUM(itemPrice*itemQuantity) FROM products WHERE branch=4;";
        Statement statment6 = conn.createStatement();
        ResultSet rs6 = statment6.executeQuery(que6);
        while (rs6.next()) {
            pie4 = Integer.parseInt(rs6.getString(1));
            totBranch4.setText(rs6.getString(1) + " AED");
        }

        String que3 = "SELECT SUM(itemPrice*itemQuantity) FROM products;";
        Statement statment3 = conn.createStatement();
        ResultSet rs3 = statment3.executeQuery(que3);
        while (rs3.next()) {
            pieTotal = Integer.parseInt(rs3.getString(1));
            totCosts.setText(rs3.getString(1) + " AED");
        }

        totCosts.setText(SQLConnect.Query("SELECT SUM(itemPrice*itemQuantity) FROM products;"));

        pieTotal=Integer.parseInt(SQLConnect.Query("SELECT SUM(itemPrice*itemQuantity) FROM products;"));

        totSupply.setText(SQLConnect.Query("SELECT COUNT(DISTINCT supplierName) FROM ia_database.products;"));

        totProducts.setText(SQLConnect.Query("SELECT COUNT(productCode) FROM (ia_database.products);"));




        ObservableList<Data> pieList = FXCollections.observableArrayList(
                new PieChart.Data("Branch 2", pie2),
                new PieChart.Data("Branch 3", pie3),
                new PieChart.Data("Branch 4", pie4)
        );

        FX_costChart.setData(pieList);

        for (final PieChart.Data data : FX_costChart.getData()) {
            int finalPieTotal = (pieTotal / 100);
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FX_percent.setText(String.valueOf(Math.round(data.getPieValue() / (finalPieTotal)) + "%"));
                }
            });
        }

        String bar1 = "SELECT SUM(itemPrice*itemQuantity) FROM ia_database.products WHERE itemCategory='Books';";
        Statement statmentB1 = conn.createStatement();
        ResultSet rsB = statmentB1.executeQuery(bar1);
        while (rsB.next()) {
            bookBar = Integer.parseInt(rsB.getString(1));
        }

        String bar2 = "SELECT SUM(itemPrice*itemQuantity) FROM ia_database.products WHERE itemCategory='Tools';";
        Statement statmentB2 = conn.createStatement();
        ResultSet rsB2 = statmentB2.executeQuery(bar2);
        while (rsB2.next()) {
            toolBar = Integer.parseInt(rsB2.getString(1));
        }

        String bar3 = "SELECT SUM(itemPrice*itemQuantity) FROM ia_database.products WHERE itemCategory='Food';";
        Statement statmentB3 = conn.createStatement();
        ResultSet rsB3 = statmentB3.executeQuery(bar3);
        while (rsB3.next()) {
            foodBar = Integer.parseInt(rsB3.getString(1));
        }

        String bar4 = "SELECT SUM(itemPrice*itemQuantity) FROM ia_database.products WHERE itemCategory='Medicine';";
        Statement statmentB4 = conn.createStatement();
        ResultSet rsB4 = statmentB4.executeQuery(bar4);
        while (rsB4.next()) {
            medBar = Integer.parseInt(rsB4.getString(1));
        }

        FX_bar.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        String items = "SELECT DISTINCT itemCategory FROM products;";
        series.getData().add(new XYChart.Data<String, Number>("Books", bookBar));
        series.getData().add(new XYChart.Data<String, Number>("Tools", toolBar));
        series.getData().add(new XYChart.Data<String, Number>("Food", foodBar));
        series.getData().add(new XYChart.Data<String, Number>("Medicine", medBar));
        FX_bar.getData().add(series);

        for (final XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FX_barselect.setText(String.valueOf(data.getYValue() + " AED"));
                }
            });
        }
    }



    public void menuReturn() throws IOException {
        int key = ManagerController.getKey();
        if (key == 1) {
            Stage stage = (Stage) FX_return.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ManagerMenu.fxml"));
            primaryStage.setTitle("Manager menu page");
            primaryStage.setScene(new Scene(root, 750, 500));
            primaryStage.show();
        } else {
            Stage stage = (Stage) FX_return.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("StaffMenu.fxml"));
            primaryStage.setTitle("Staff menu page");
            primaryStage.setScene(new Scene(root, 750, 500));
            primaryStage.show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Queries();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}