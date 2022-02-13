package SQL_Connection;

import IA_components.EmployeeDataModel;
import IA_components.ProductDataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SQLConnect {
    private static Connection connection;
    public static Connection getConnection() {
        String dbName = "ia_database";
        String userName = "root";
        String password = "RotiPol5";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localHost:3306/" + dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static ObservableList<EmployeeDataModel> getEmployeeData(){
        Connection conn = getConnection();
        ObservableList<EmployeeDataModel> list1 = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ia_database.employee;");
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()){
                list1.add(new EmployeeDataModel(rs1.getInt("employeeID"),rs1.getString("firstName"),rs1.getString("lastName"),
                        rs1.getDate("joinDate"), rs1.getString("gender"),rs1.getString("jobTitle"),
                        rs1.getInt("salary"),rs1.getInt("superID"),rs1.getInt("branchID")));
            }
        } catch (Exception e) {
        }
        return list1;
    }

     public static ObservableList<ProductDataModel> getProductData(){
        Connection conn2 = getConnection();
        ObservableList<ProductDataModel> list2 = FXCollections.observableArrayList();
        try {
           PreparedStatement ps2 = conn2.prepareStatement("SELECT * FROM ia_database.products;");
           ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()){
                list2.add(new ProductDataModel(rs2.getString("productCode"),rs2.getString("itemName"),rs2.getString("perishable"),
                       rs2.getInt("branch"), rs2.getString("itemCategory"),rs2.getString("supplierName"),
                       rs2.getString("supplierContact"),rs2.getInt("itemPrice"),rs2.getInt("itemQuantity")));
           }
        } catch (Exception e) {

        }
        return list2;
    }

    public static String Query(String command) throws SQLException {
        connection = SQLConnect.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result= statement.executeQuery(command);
        while (result.next()) {
            return result.getString(1);
        }
        return null;
    }


}
