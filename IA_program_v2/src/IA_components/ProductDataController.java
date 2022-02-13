package IA_components;

import SQL_Connection.SQLConnect;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductDataController extends Component implements Initializable {
    public TextField FX_addCode;
    public TextField FX_addItem;
    public RadioButton FX_addPerish;
    public TextField FX_addBranch;
    public TextField FX_addType;
    public TextField FX_addSupply;
    public TextField FX_addContact;
    public TextField FX_addPrice;
    public TextField FX_addQuan;
    public TextField mailItem;
    public TextField mailContact;
    public TextField tableFilter;

    @FXML
    private TableView<ProductDataModel> ProductTable;
    @FXML
    private TableColumn<ProductDataModel, String> FX_prodCode;
    @FXML
    private TableColumn<ProductDataModel, String> FX_name;
    @FXML
    private TableColumn<ProductDataModel, String> FX_perish;
    @FXML
    private TableColumn<ProductDataModel, Integer> FX_branch;
    @FXML
    private TableColumn<ProductDataModel, String> FX_type;
    @FXML
    private TableColumn<ProductDataModel, String> FX_supply;
    @FXML
    private TableColumn<ProductDataModel, String> FX_contact;
    @FXML
    private TableColumn<ProductDataModel, Integer> FX_price;
    @FXML
    private TableColumn<ProductDataModel, Integer> FX_quan;
    ObservableList<ProductDataModel> listFilter;
    int index = -1;
    public Slider mailSlider;
    public Label totBranch4;
    public Button FX_return;
    public Button FX_calc;

    PreparedStatement pst = null;
    Connection conn = null;

    public void getSelected(MouseEvent mouseEvent) {    // Method adds data from selected row into each text field
        index = ProductTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        FX_addCode.setText(FX_prodCode.getCellData(index).toString());
        FX_addItem.setText(FX_name.getCellData(index).toString());
        FX_addPerish.setText(FX_perish.getCellData(index).toString());
        if ((FX_perish.getCellData(index)).equals("True")) {
            FX_addPerish.setSelected(true);
        }
        FX_addBranch.setText(FX_branch.getCellData(index).toString());
        FX_addType.setText(FX_type.getCellData(index).toString());
        FX_addSupply.setText(FX_supply.getCellData(index).toString());
        FX_addContact.setText(FX_contact.getCellData(index).toString());
        FX_addPrice.setText(FX_price.getCellData(index).toString());
        FX_addQuan.setText(FX_quan.getCellData(index).toString());
        mailItem.setText(FX_name.getCellData(index).toString());
        mailContact.setText(FX_contact.getCellData(index).toString());
    }

    public void Add(ActionEvent actionEvent) {
        if(FX_addCode.getText().equals("") || FX_addItem.getText().equals("") || FX_addPerish.getText().equals("")
                || FX_addBranch.getText().equals("") || FX_addType.getText().equals("") || FX_addSupply.getText().equals("")
                || FX_addContact.getText().equals("") || FX_addPrice.getText().equals("") ||
                FX_addQuan.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please fill in all fields");
        }
        else {
            conn = SQLConnect.getConnection();
            String addProduct = "INSERT INTO products VALUES (?,?,?,?,?,?,?,?,?);";
            try {
                pst = conn.prepareStatement(addProduct);
                pst.setString(1, FX_addCode.getText());
                pst.setString(2, FX_addItem.getText());
                if (FX_addPerish.isSelected()) {
                    pst.setString(3, FX_addPerish.getText());
                } else {
                    pst.setString(3, "False");
                }
                pst.setInt(4, Integer.parseInt(FX_addBranch.getText()));
                pst.setString(5, FX_addType.getText());
                pst.setString(6, FX_addSupply.getText());
                pst.setString(7, FX_addContact.getText());
                pst.setInt(8, Integer.parseInt(FX_addPrice.getText()));
                pst.setInt(9, Integer.parseInt(FX_addQuan.getText()));
                pst.execute();
                JOptionPane.showMessageDialog(null, "Product add successful");
                tableControl();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void Update(ActionEvent actionEvent) {
        if(FX_addCode.getText().equals("") || FX_addItem.getText().equals("") || FX_addPerish.getText().equals("") || FX_addBranch.getText().equals("")
                || FX_addType.getText().equals("") || FX_addSupply.getText().equals("") || FX_addContact.getText().equals("") || FX_addPrice.getText().equals("")
                || FX_addQuan.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please fill in all fields");
        }
        else {
            try {
                conn = SQLConnect.getConnection();
                String update1 = FX_addCode.getText();
                String update2 = FX_addItem.getText();
                String update3;
                if (FX_addPerish.isSelected()) {
                    update3 = "True";
                } else {
                    update3 = "False";
                }
                String update4 = FX_addBranch.getText();
                String update5 = FX_addType.getText();
                String update6 = FX_addSupply.getText();
                String update7 = FX_addContact.getText();
                String update8 = FX_addPrice.getText();
                String update9 = FX_addQuan.getText();
                String updateInfo = "UPDATE ia_database.products SET productCode='" + update1 + "',itemName ='" + update2 + "',perishable='" +
                        update3 + "',branch=" + update4 + ",itemCategory='" + update5 + "',supplierName='" +
                        update6 + "',supplierContact='" + update7 + "',itemPrice=" + update8 + ",itemQuantity=" +
                        update9 + " WHERE productCode='" + update1 + "';";
                pst = conn.prepareStatement(updateInfo);
                pst.execute(updateInfo);
                tableControl();
                JOptionPane.showMessageDialog(null, "Product update successful");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void Delete(ActionEvent actionEvent) throws SQLException {
        if(FX_addCode.getText().equals("") || FX_addItem.getText().equals("") || FX_addPerish.getText().equals("") || FX_addBranch.getText().equals("")
                || FX_addType.getText().equals("") || FX_addSupply.getText().equals("") || FX_addContact.getText().equals("") || FX_addPrice.getText().equals("")
                || FX_addQuan.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please fill in all fields");
        }
        else {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete product " +
                            FX_addCode.getText() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                conn = SQLConnect.getConnection();
                String del = "DELETE FROM products WHERE productCode='" + FX_addCode.getText() + "'";
                pst = conn.prepareStatement(del);
                pst.execute();
                tableControl();
                JOptionPane.showMessageDialog(null, "Product deletion successful");
            }
        }
    }

    public void menuReturn() throws IOException {  //Controls the menu the user returns to
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

    public void Calculator() throws IOException {
        Stage stage = (Stage) FX_calc.getScene().getWindow();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Calculator.fxml"));
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(new Scene(root, 360, 390));
        primaryStage.show();
    }

    public void tableControl() throws SQLException {   //Populates the TableView with information from the database
        FX_prodCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        FX_name.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        FX_perish.setCellValueFactory(new PropertyValueFactory<>("perishable"));
        FX_branch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        FX_type.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        FX_supply.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        FX_contact.setCellValueFactory(new PropertyValueFactory<>("supplierContact"));
        FX_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        FX_quan.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        listFilter = SQLConnect.getProductData();
        ProductTable.setItems(listFilter);
        //ProductTable.setStyle("-fx-background-color: #B22222");
        FilteredList<ProductDataModel> filteredData = new FilteredList<>(listFilter, b -> true);
        tableFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(filter -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (filter.getProductCode().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (filter.getItemName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (filter.getPerishable().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(filter.getBranch()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (filter.getItemCategory().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (filter.getSupplierName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (filter.getSupplierContact().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(filter.getItemPrice()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(filter.getItemQuantity()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<ProductDataModel> listFilter = new SortedList<>(filteredData);
        listFilter.comparatorProperty().bind(ProductTable.comparatorProperty());
        ProductTable.setItems(listFilter);
    }


    public void sendEmail(){
        ProgressBar progress = new ProgressBar();
        String email=mailContact.getText().substring(mailContact.getLength()-4);
        if(email.equals(".com") || email.equals(".net")){
            EmailConnect.compile(mailItem.getText(),String.valueOf(Math.round(mailSlider.getValue())),mailContact.getText());
            JOptionPane.showMessageDialog(null, "Email sent successfully");
        }
        else{
            JOptionPane.showMessageDialog(null, "Please enter a valid email address");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tableControl();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String[] suggest = {"Tools","Food","Books","Medicine"};
        TextFields.bindAutoCompletion(FX_addType, suggest);
        ProductTable.setStyle( "-fx-background-color:  #798291;");

        ProductTable.setRowFactory(tv -> new TableRow<ProductDataModel>() {
            @Override
            public void updateItem(ProductDataModel item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item==null) {
                    setStyle("");
                } else if (item.getItemQuantity()==0) {
                    setStyle("-fx-background-color: #e8523f;");
                } else {
                    setStyle("");
                }
            }
        });
    }
}