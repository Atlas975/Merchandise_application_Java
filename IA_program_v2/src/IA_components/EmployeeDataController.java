package IA_components;

import SQL_Connection.SQLConnect;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

  public class EmployeeDataController extends Component implements Initializable {
      public TextField FX_addID;
      public TextField FX_addFirst;
      public TextField FX_addLast;
      public DatePicker FX_addDate;
      public TextField FX_addSex;
      public TextField FX_addTitle;
      public TextField FX_addSalary;
      public TextField FX_addSuper;
      public TextField FX_addbranch;
      public TextField FX_addPass;
      public TextField tableFilter;
      @FXML private TableView<EmployeeDataModel> EmployeeTable;
      @FXML private TableColumn<EmployeeDataModel, Integer> FX_empID;
      @FXML private TableColumn<EmployeeDataModel, String> FX_firstName;
      @FXML private TableColumn<EmployeeDataModel, String> FX_lastName;
      @FXML private TableColumn<EmployeeDataModel, Date> FX_joinDate;
      @FXML private TableColumn<EmployeeDataModel, String> FX_gender;
      @FXML private TableColumn<EmployeeDataModel, String> FX_jobTitle;
      @FXML private TableColumn<EmployeeDataModel, Integer> FX_salary;
      @FXML private TableColumn<EmployeeDataModel, Integer> FX_superID;
      @FXML private TableColumn<EmployeeDataModel, Integer> FX_branchID;
      ObservableList<EmployeeDataModel> listE;
      ObservableList<EmployeeDataModel> listFilter;
      public Button FX_return;
      public Button FX_calc;
      PreparedStatement pst=null;
      PreparedStatement pst2=null;
      Connection conn=null;
      Connection conn2=null;
      int index = -1;

      public void Add()  {   // Method used to add data to the employee database
          conn= SQLConnect.getConnection();
          conn2=SQLConnect.getConnection();
          String addEmp="INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?);";
          String addPass="INSERT INTO loginAlpha VALUES (?,?);";
          try{
              pst=conn.prepareStatement(addEmp);
              pst2=conn.prepareStatement(addPass);
              pst.setInt(1, Integer.parseInt(FX_addID.getText()));
              pst.setString(2, FX_addFirst.getText());
              pst.setString(3, FX_addLast.getText());
              pst.setDate(4, Date.valueOf(FX_addDate.getValue()));
              pst.setString(5, FX_addSex.getText());
              pst.setString(6, FX_addTitle.getText());
              pst.setInt(7, Integer.parseInt(FX_addSalary.getText()));
              pst.setInt(8, Integer.parseInt(FX_addSuper.getText()));
              pst.setString(9, FX_addbranch.getText());
              pst2.setInt(1,Integer.parseInt(FX_addID.getText()));
              pst2.setString(2, FX_addPass.getText());
              pst.execute();
              pst2.execute();
              JOptionPane.showMessageDialog(null,"User add successful");
              Refresh();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
      }

      public void getSelected (MouseEvent event){  // Auto-fills text fields when selecting a row in a table
          index = EmployeeTable.getSelectionModel().getSelectedIndex();
          if (index <= -1){
              return;
          }
          FX_addID.setText(FX_empID.getCellData(index).toString());
          FX_addFirst.setText(FX_firstName.getCellData(index));
          FX_addLast.setText(FX_lastName.getCellData(index));
          FX_addDate.setValue(LocalDate.parse(FX_joinDate.getCellData(index).toString()));
          FX_addSex.setText(FX_gender.getCellData(index));
          FX_addTitle.setText(FX_jobTitle.getCellData(index));
          FX_addSalary.setText(FX_salary.getCellData(index).toString());
          FX_addSuper.setText(FX_superID.getCellData(index).toString());
          FX_addbranch.setText(FX_branchID.getCellData(index).toString());
      }

      public void Delete(){ //Deletes selected employee from database
          int confirm=JOptionPane.showConfirmDialog(this,"Are you sure you wish to delete "+FX_addFirst.getText()+" "+FX_addLast.getText()+" from the database?","Confirm",JOptionPane.YES_NO_OPTION);
          if(confirm==JOptionPane.YES_NO_OPTION) {
              conn = SQLConnect.getConnection();
              String del = "Delete from employee where EmployeeID=?";
              try {
                  pst = conn.prepareStatement(del);
                  pst.setString(1, FX_addID.getText());
                  pst.execute();
                  Refresh();
              } catch (Exception e) {
                  JOptionPane.showMessageDialog(null, e);
              }
          }
      }

      public void Update() throws SQLException { // Updates an employees data
          try {
              conn = SQLConnect.getConnection();
              conn2= SQLConnect.getConnection();
              String update1 = (FX_addID.getText());
              String update2 = FX_addFirst.getText();
              String update3 = FX_addLast.getText();
              String update4 = String.valueOf(FX_addDate.getValue());
              String update5 = FX_addSex.getText();
              String update6 = FX_addTitle.getText();
              String update7 = FX_addSalary.getText();
              String update8 = FX_addSuper.getText();
              String update9 = FX_addbranch.getText();
              String update10= FX_addPass.getText();
              String updateInfo = "UPDATE ia_database.employee SET employeeID="+update1+", firstName='"+update2+"',lastName='"+update3+"',joinDate='"+update4+"',gender='"+update5+
                      "',jobtitle='"+update6+"',salary="+update7+",superID="+update8+",branchID="+update9+ " WHERE employeeID=" +update1+";";
              pst = conn.prepareStatement(updateInfo);
              String updatePass="UPDATE ia_database.loginAlpha SET pass='"+update10+"' WHERE ID="+update1;
              pst2=conn2.prepareStatement(updatePass);
              pst.execute(updateInfo);
              pst2.execute(updatePass);
              JOptionPane.showMessageDialog(null, "Employee update successful");
              Refresh();
          } catch (NumberFormatException e) {
              e.printStackTrace();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
          catch(Exception e){
          }
      }

      public void menuReturn() throws IOException {
          int key=ManagerController.getKey();
          if (key == 1){
              Stage stage = (Stage) FX_return.getScene().getWindow();
              stage.close();
              Stage primaryStage = new Stage();
              Parent root = FXMLLoader.load(getClass().getResource("ManagerMenu.fxml"));
              primaryStage.setTitle("Manager menu page");
              primaryStage.setScene(new Scene(root, 750, 500));
              primaryStage.show();
          }
          else{
              Stage stage = (Stage) FX_return.getScene().getWindow();
              stage.close();
              Stage primaryStage = new Stage();
              Parent root = FXMLLoader.load(getClass().getResource("StaffMenu.fxml"));
              primaryStage.setTitle("Staff menu page");
              primaryStage.setScene(new Scene(root, 750, 500));
              primaryStage.show();
          }
      }

      public void Refresh() throws SQLException {
          FX_empID.setCellValueFactory(new PropertyValueFactory<>("empID"));
          FX_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
          FX_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
          FX_joinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
          FX_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
          FX_jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
          FX_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
          FX_superID.setCellValueFactory(new PropertyValueFactory<>("superID"));
          FX_branchID.setCellValueFactory(new PropertyValueFactory<>("branchID"));
          listE=SQLConnect.getEmployeeData();
          EmployeeTable.setItems(listE);
      }

      public void Calculator() throws IOException {
          Stage stage = (Stage) FX_calc.getScene().getWindow();
          Stage primaryStage = new Stage();
          Parent root = FXMLLoader.load(getClass().getResource("Calculator.fxml"));
          primaryStage.setTitle("Calculator");
          primaryStage.setScene(new Scene(root, 360, 390));
          primaryStage.show();
      }

      public void tableControl() throws SQLException { // Populates table with data from SQL database
          FX_empID.setCellValueFactory(new PropertyValueFactory<>("empID"));
          FX_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
          FX_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
          FX_joinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
          FX_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
          FX_jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
          FX_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
          FX_superID.setCellValueFactory(new PropertyValueFactory<>("superID"));
          FX_branchID.setCellValueFactory(new PropertyValueFactory<>("branchID"));
          listFilter = SQLConnect.getEmployeeData();
          EmployeeTable.setItems(listFilter);
          FilteredList<EmployeeDataModel> filteredData = new FilteredList<>(listFilter, b -> true);
          tableFilter.textProperty().addListener((observable, oldValue, newValue) -> {  // Controls table filter
              filteredData.setPredicate(filter -> {
                  if (newValue == null || newValue.isEmpty()) {
                      return true;
                  }
                  String lowerCaseFilter = newValue.toLowerCase();
                  if (String.valueOf(filter.getEmpID()).indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (filter.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (filter.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (String.valueOf(filter.getJoinDate()).indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (filter.getGender().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (filter.getJobTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (String.valueOf(filter.getSalary()).indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (String.valueOf(filter.getSuperID()).indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (String.valueOf(filter.getBranchID()).indexOf(lowerCaseFilter) != -1)
                      return true;
                  else
                      return false;
              })  ;
          });
          SortedList<EmployeeDataModel> listFilter = new SortedList<>(filteredData);
          listFilter.comparatorProperty().bind(EmployeeTable.comparatorProperty());
          EmployeeTable.setItems(listFilter);
      }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tableControl();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String[] suggest = {"Accountant","Cashier","Manager","Senior manager","Customer service","Sales Associate","Human resources"};
        TextFields.bindAutoCompletion(FX_addTitle, suggest);
    }
}
