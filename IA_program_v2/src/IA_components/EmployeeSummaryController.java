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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class EmployeeSummaryController implements Initializable {
    public PieChart FX_pie;
    public BarChart FX_bar;
    public Label FX_pieSelect;
    public Label TotSal;
    public Label totEmp;
    public Label totMale;
    public Label totFem;
    public Label avSal;
    public Button FX_return;
    Connection conn=null;
    int index = -1;

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
            primaryStage.setScene(new Scene(root, 700, 500));
            primaryStage.show();
        }
    }

    public void Refresh() throws SQLException {
        Queries();
    }

    public void Queries() throws SQLException {
        conn = SQLConnect.getConnection();
        int pieTotal=0;
        int pie1 = 0;
        int pie2= 0;
        int pie3= 0;
        int pie4= 0;
        int pie5= 0;
        int pie6= 0;

        String empSal="SELECT SUM(salary) FROM ia_database.Employee;";
        Statement statment=conn.createStatement();
        ResultSet rs=statment.executeQuery(empSal);
        while(rs.next()){
            TotSal.setText(rs.getString(1)+" AED");
        }

        String empStaff="SELECT COUNT(EmployeeID) FROM ia_database.Employee;";
        Statement statement2=conn.createStatement();
        ResultSet rs2=statement2.executeQuery(empStaff);
        while(rs2.next()){
            pieTotal=Integer.parseInt(rs2.getString(1));
            totEmp.setText(rs2.getString(1));
        }

        String empFem="SELECT COUNT(gender) FROM ia_database.Employee WHERE gender='F';";
        Statement statement3=conn.createStatement();
        ResultSet rs3=statement3.executeQuery(empFem);
        while(rs3.next()){
            totFem.setText(rs3.getString(1));
        }

        String empMale="SELECT COUNT(gender) FROM ia_database.Employee WHERE gender='M';";
        Statement statement4=conn.createStatement();
        ResultSet rs4=statement4.executeQuery(empMale);
        while(rs4.next()){
            totMale.setText(rs4.getString(1));
        }
        String salAvrg="SELECT AVG(salary) FROM employee;";
        Statement statement5=conn.createStatement();
        ResultSet rs5=statement5.executeQuery(salAvrg);
        while(rs5.next()){
            avSal.setText(rs5.getInt(1)+" AED");
        }

        String pMngr="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Manager' OR jobtitle='Senior manager';";
        Statement statementP1=conn.createStatement();
        ResultSet rsP1=statementP1.executeQuery(pMngr);
        while(rsP1.next()){
            pie1=(rsP1.getInt(1));
        }

        String pHresource="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Human resources'";
        Statement statementP2=conn.createStatement();
        ResultSet rsP2=statementP2.executeQuery(pHresource);
        while(rsP2.next()){
            pie2=(rsP2.getInt(1));
        }

        String pSales="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Sales associate';";
        Statement statementP3=conn.createStatement();
        ResultSet rsP3=statementP3.executeQuery(pSales);
        while(rsP3.next()){
            pie3=(rsP3.getInt(1));
        }

        String pService="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Customer service'";
        Statement statementP4=conn.createStatement();
        ResultSet rsP4=statementP4.executeQuery(pService);
        while(rsP4.next()){
            pie4=(rsP4.getInt(1));
        }

        String pAccounts="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Accountant'";
        Statement statementP5=conn.createStatement();
        ResultSet rsP5=statementP5.executeQuery(pAccounts);
        while(rsP5.next()){
            pie5=(rsP5.getInt(1));
        }

        String pCash="SELECT COUNT(jobtitle) FROM ia_database.employee WHERE jobtitle='Cashier'";
        Statement statementP6=conn.createStatement();
        ResultSet rsP6=statementP6.executeQuery(pCash);
        while(rsP6.next()){
            pie6=(rsP6.getInt(1));
        }


        int counter=1;
        int bar;
        String branch;
        FX_bar.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        String barQ="SELECT SUM(Salary) FROM ia_database.employee GROUP BY branchID";
        Statement statementBar=conn.createStatement();
        ResultSet rsBar=statementBar.executeQuery(barQ);
        String branches="SELECT DISTINCT(branchID) FROM ia_database.employee";
        Statement statementBranch=conn.createStatement();
        ResultSet rsBranch=statementBranch.executeQuery(branches);
        while(rsBar.next() && rsBranch.next()){
            bar=Integer.parseInt(rsBar.getString(1));
            branch=(rsBranch.getString(1));
            series.getData().add(new XYChart.Data<String, Number>("Branch "+branch, bar));
            counter++;
        }
        FX_bar.getData().add(series);


        ObservableList<PieChart.Data> pieList= FXCollections.observableArrayList(
                new PieChart.Data("Managers",pie1),
                new PieChart.Data("Human resources",pie2),
                new PieChart.Data("Sales associate",pie3),
                new PieChart.Data("Customer service",pie4),
                new PieChart.Data("Accountant",pie5),
                new PieChart.Data("Cashier",pie6)
        );
        FX_pie.setData(pieList);

        for(final PieChart.Data data: FX_pie.getData()){
            int finalPieTotal = pieTotal;
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event){
                    FX_pieSelect.setText(String.valueOf(Math.round((data.getPieValue()/finalPieTotal)*100)+"%"));
                }
            });
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
