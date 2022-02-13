package IA_components;

import SQL_Connection.SQLConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public static int TransferID;
    public TextField txtUser;
    public PasswordField txtPass;
    public Button txtLogin;
    public AnchorPane LogPane;
    public AnchorPane SignPane;
    public AnchorPane AuthPane;
    public Button Action_log;
    public Button Action_create;
    public Button AuthCheck;
    public TextField txtnUser;
    public TextField AuthPass;
    public PasswordField txtnPass;
    public Button FX_exit;
    public Button txtnEnter;
    public ComboBox txtJob;
    public PreparedStatement prep = null;
    public PreparedStatement prep2 = null;
    public ResultSet rs = null;
    public int attempt=0;
    Connection conn = null;

    public void LoginShow() {
        Action_create.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #f4333c; -fx-background-color: transparent;");
        Action_log.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #30CF3C; -fx-background-color: transparent;");
        LogPane.setVisible(true);
        SignPane.setVisible(false);
        AuthPane.setVisible(false);
    }

    public void SignupShow() {
        AuthPane.setVisible(false);
        LogPane.setVisible(false);
        SignPane.setVisible(true);
    }

    public void AuthShow() {
        Action_create.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #30CF3C; -fx-background-color: transparent;");
        Action_log.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #f4333c; -fx-background-color: transparent;");
        AuthPane.setVisible(true);
        LogPane.setVisible(false);
        SignPane.setVisible(false);
    }


    public void Login(ActionEvent actionEvent) throws Exception { // Method used to verify user
        conn = SQLConnect.getConnection();
        String log = "Select * from loginAlpha where ID=? and pass=?;";
        try {
            prep = conn.prepareStatement(log);
            prep.setString(1, txtUser.getText());
            prep.setString(2, txtPass.getText());
            rs = prep.executeQuery();
            if (txtUser.getText().equals("") || txtPass.getText().equals(""))
           {
               JOptionPane.showMessageDialog(null, "Username or password cannot be blank");
            }
            else{
            if (lengthCheck(Integer.parseInt(txtUser.getText()))==false|| lengthCheck(txtPass.getText())==false) {
                JOptionPane.showMessageDialog(null, "Username or password is too long");
            } else {
                if (rs.next()) {
                    attempt = 0;
                    JOptionPane.showMessageDialog(null, "Access granted");
                    Menu();
                } else {
                    attempt++;
                    if (attempt >= 3) {
                        Stage stage = (Stage) FX_exit.getScene().getWindow();
                        stage.close();
                        JOptionPane.showMessageDialog(null, "Access denied");
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect username or password");
                    }
                }
            }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void AddUser(ActionEvent event) throws SQLException {    // Method for adding users to the database
        conn = SQLConnect.getConnection();
        String auth = "SELECT pass FROM IA_database.loginAlpha WHERE jobTitle='Manager';";
        Statement statement = conn.createStatement();
        rs = statement.executeQuery(auth);
        rs.next();
        String authConfirm = (rs.getString(1));
        if (AuthPass.getText().equals(authConfirm)) {
            SignupShow();
            if (AuthPass.getText().equals("")) {
               JOptionPane.showMessageDialog(null, "All fields must be filled");
            } else {
                String dataUpdate = "INSERT INTO IA_database.Employee (employeeID, jobTitle) VALUES (?,?);";
                try {
                    prep = conn.prepareStatement(dataUpdate);
                    prep.setInt(1, Integer.parseInt(txtnUser.getText()));
                    prep.setString(2, txtJob.getValue().toString());
                    prep.execute();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String add = "INSERT INTO IA_database.loginAlpha values (?,?)";
                try {
                    prep2 = conn.prepareStatement(add);
                    prep2.setInt(1, Integer.parseInt(txtnUser.getText()));
                    prep2.setString(2, txtnPass.getText());
                    prep2.execute();
                    JOptionPane.showMessageDialog(null, "Saved");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            attempt++;
            if(attempt>=3){
                Stage stage = (Stage) FX_exit.getScene().getWindow();
                stage.close();
                JOptionPane.showMessageDialog(null, "Access denied");
            }
            else{
                JOptionPane.showMessageDialog(null, "Incorrect username or password");
            }
        }
    }

    public void Menu() throws SQLException, IOException {   // Determines the users access rights in the program
        SQLConnect SQLConnect = new SQLConnect();
        conn = SQLConnect.getConnection();
        int userID = Integer.parseInt(txtUser.getText());
        String transfer = ("SELECT jobTitle FROM ia_database.employee WHERE employeeID=" + txtUser.getText());
        Statement statement = conn.createStatement();
        rs = statement.executeQuery(transfer);
        rs.next();
        String control = (rs.getString(1));
        if (control.equals("Senior manager") || control.equals("Manager")) {
            setUser(userID);
            Stage stage = (Stage) txtLogin.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ManagerMenu.fxml"));
            primaryStage.setTitle("Manager menu page");
            primaryStage.setScene(new Scene(root, 750, 500));
            primaryStage.show();
        } else {
            setUser(userID);
            Stage stage = (Stage) txtLogin.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("StaffMenu.fxml"));
            primaryStage.setTitle("Staff menu page");
            primaryStage.setScene(new Scene(root, 750, 500));
            primaryStage.show();
        }
    }

    public static void setUser(int user) {  // Sets the users access rights
        TransferID = user;
    }

    public static int getUser() { // Gets the users access rights
        return TransferID;
    }

    public void Exit() {
        Stage stage = (Stage) FX_exit.getScene().getWindow();
        stage.close();
    }

    public static boolean lengthCheck(String input){
        boolean valid=true;
        int length=input.length();
        if (length>10){
            valid=false;
        }
        return valid;
    }

    public static boolean lengthCheck(int input){
        boolean valid=true;
        int length=String.valueOf(input).length();
        if (length>20){
            valid=false;
        }
        return valid;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = SQLConnect.getConnection();
        txtJob.getItems().addAll("Senior manager", "Manager", "Accountant", "Cashier", "Customer service", "Sales associate", "Human resources");
        int maxID = 0;
        String IDsuggest = "SELECT MAX(employeeID) FROM employee;";
        Statement statement = null;
        try {
            statement = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(IDsuggest);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                maxID = rs.getInt(1) + 1;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String[] suggest = {Integer.toString(maxID)};
            TextFields.bindAutoCompletion(txtUser, suggest);
            TextFields.bindAutoCompletion(txtnUser,suggest);
        }
    }
}
