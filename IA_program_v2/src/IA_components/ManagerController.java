package IA_components;

import SQL_Connection.SQLConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    public static int TransferKey=0;
    public Button FX_logout;
    public Label time;
    public Button M_PRbutton;
    public Button M_EMPbutton;
    public Label FX_date;
    public Label FX_sal;
    public Label FX_job;
    public Connection conn = null;
    public Label FX_intro;

   public void Manager_employee (ActionEvent actionEvent) throws IOException {
       Stage stage = (Stage) M_EMPbutton.getScene().getWindow();
       stage.close();
       Stage primaryStage = new Stage();
       Parent root = FXMLLoader.load(getClass().getResource("EmployeeData.fxml"));
       primaryStage.setTitle("Employee details");
       primaryStage.setScene(new Scene(root, 980,675));
       primaryStage.show();
       TransferKey=1;
   }
    public void Manager_complobot (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) M_EMPbutton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ComploBot.fxml"));
        primaryStage.setTitle("ComploBot");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
        TransferKey=1;
    }

    public void Manager_employeeSum (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) M_EMPbutton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("EmployeeSummary.fxml"));
        primaryStage.setTitle("Employee details");
        primaryStage.setScene(new Scene(root, 860, 550));
        primaryStage.show();
        TransferKey=1;
    }

   public void Manager_product (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) M_PRbutton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ProductData.fxml"));
        primaryStage.setTitle("Product menu");
        primaryStage.setScene(new Scene(root, 1100, 790));
        primaryStage.show();
   }

    public void Manager_productSum (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) M_PRbutton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ProductSummary.fxml"));
        primaryStage.setTitle("Product menu");
        primaryStage.setScene(new Scene(root, 860, 550));
        primaryStage.show();
    }

    public void Manager_receipt (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) M_PRbutton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("RecieptGeneration.fxml"));
        primaryStage.setTitle("Receipt menu");
        primaryStage.setScene(new Scene(root, 1110, 790));
        primaryStage.show();
    }

    public void Manager_logout (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) FX_logout.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        primaryStage.setTitle("Login page");
        primaryStage.setScene(new Scene(root, 435, 400));
        primaryStage.show();
   }

   public void Queries(int userKey) throws SQLException{ // Retrieves user personal details
       conn = SQLConnect.getConnection();
       String empSal="SELECT salary FROM ia_database.Employee WHERE employeeID="+userKey+";";
       Statement statment=conn.createStatement();
       ResultSet rs=statment.executeQuery(empSal);
       while(rs.next()){
           FX_sal.setText(rs.getString(1)+" AED");
       }

       String empDate="SELECT joinDate FROM ia_database.Employee WHERE employeeID="+userKey+";";
       Statement statement2=conn.createStatement();
       ResultSet rs2=statement2.executeQuery(empDate);
       while(rs2.next()){
           FX_date.setText(rs2.getString(1));

       }
       String empJob="SELECT jobtitle FROM ia_database.Employee WHERE employeeID="+userKey+";";
       Statement statement3=conn.createStatement();
       ResultSet rs3=statement3.executeQuery(empJob);
       while(rs3.next()){
           FX_job.setText(rs3.getString(1));
       }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SimpleDateFormat format= new SimpleDateFormat("dd-MM-YYYY");
        Date date = new Date(System.currentTimeMillis());
        time.setText(format.format(date));
        int userKey=LoginController.TransferID;
        Connection conn = SQLConnect.getConnection();
        String emp="SELECT firstName FROM ia_database.Employee WHERE employeeID="+userKey+";";
        Statement statement= null;
        try {
            statement = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs= null;
        try {
            rs = statement.executeQuery(emp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                FX_intro.setText("Welcome " +rs.getString(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            Queries(userKey);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setKey(1);
        }

    public static void setKey(int user){ TransferKey=user;}
    public static int getKey()  { return TransferKey; }
}
