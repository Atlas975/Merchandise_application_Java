package IA_components;

import SQL_Connection.SQLConnect;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ResourceBundle;

public class RecieptCOntroller implements Initializable {
    public TextField FX_addCode;
    public TextField FX_addItem;
    public TextField FX_addSupply;
    public TextField FX_addQuan;
    public TextField mailItem;
    public TextField mailContact;
    public TextField tableFilter;
    public Label FX_total;
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
    @FXML
    private TableView<RecieptDataModel> FX_Bill;
    @FXML
    private TableColumn<RecieptDataModel, String> FX_BillCode;
    @FXML
    private TableColumn<RecieptDataModel, String> FX_BillItem;
    @FXML
    private TableColumn<RecieptDataModel, Integer> FX_BillQuantity;
    RecieptDataModel generate=new RecieptDataModel(null,null,null);
    public LinkedList<RecieptDataModel> receipt = new LinkedList<>();
    ObservableList<ProductDataModel> listFilter;
    int index = -1;
    public Button FX_return;
    PreparedStatement pst = null;
    Connection conn = null;

    public void getSelected(MouseEvent mouseEvent) {  // Auto-fill textfields function
        index = ProductTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        FX_addCode.setText(FX_prodCode.getCellData(index).toString());
        FX_addItem.setText(FX_name.getCellData(index).toString());;
        FX_addQuan.setText(FX_quan.getCellData(index).toString());
    }


    public void Add(ActionEvent actionEvent) throws SQLException {
        conn = SQLConnect.getConnection();
        int check=0;
        String valid = "SELECT itemQuantity FROM ia_database.products WHERE productCode='"+FX_addCode.getText()+"';";
        Statement statment = conn.createStatement();
        ResultSet rs = statment.executeQuery(valid);
        while (rs.next()){
            check=Integer.parseInt(rs.getString(1));
        }

        try {
            if(Integer.parseInt(FX_addQuan.getText())>check){
                JOptionPane.showMessageDialog(null, "Not enough item stock for request");
            }
            else {
                conn = SQLConnect.getConnection();
                int quan = Integer.parseInt(FX_addQuan.getText());
                if (quan > 0) {
                    String code = FX_addCode.getText();
                    String add = "UPDATE products SET itemQuantity=itemQuantity-" + quan + " WHERE productCode='" + code + "';";
                    pst = conn.prepareStatement(add);
                    pst.execute(add);
                    receipt.add(new RecieptDataModel(code, FX_addItem.getText(), quan));
                    BillControl();
                } else{
                    JOptionPane.showMessageDialog(null, "Please enter a valid quantity");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Wipe(ActionEvent actionEvent) throws SQLException {
        for(int i=0; i<receipt.size(); i++){
            try {
                conn = SQLConnect.getConnection();
            String reAdd = "UPDATE products SET itemQuantity=itemQuantity+"+receipt.get(i).getQuantity()+" " +
                    "WHERE productCode='" +receipt.get(i).getCode()+ "';";
            pst = conn.prepareStatement(reAdd);
            pst.execute(reAdd);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Click wipe to clear receipt");
            }
        }
        receipt.clear();
        BillControl();
    }

    public void BillControl() throws SQLException {
        FX_BillCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        FX_BillItem.setCellValueFactory(new PropertyValueFactory<>("Product"));
        FX_BillQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        ObservableList <RecieptDataModel> receiptTable= FXCollections.observableArrayList();
        for(int i=0; i<receipt.size(); i++){
           receiptTable.add(new RecieptDataModel(receipt.get(i).getCode(),receipt.get(i).getProduct(),receipt.get(i).getQuantity()));
        }
        FX_Bill.setItems(receiptTable);
        tableControl();
        Total();
    }

    public int Total() throws SQLException {
        int total=0;
        for (int i=0; i<receipt.size(); i++){
            String que = "SELECT itemPrice FROM ia_database.products where productCode='"+receipt.get(i).getCode()+"';";
            Statement statment= conn.createStatement();
            ResultSet rs= statment.executeQuery(que);
            while (rs.next()) {
                total=total+((Integer.parseInt(rs.getString(1))*receipt.get(i).getQuantity()));
            }
        }
        FX_total.setText(total+" AED");
        return total;
    }

    public void ReportMaker(){
     Font biggerText = new Font(Font.FontFamily.UNDEFINED,18,Font.BOLD);
     Font bigTextNormal = new Font(Font.FontFamily.UNDEFINED,15,Font.BOLD);
     Font redFont = new Font(Font.FontFamily.UNDEFINED, 12, Font.NORMAL, BaseColor.RED);
     int total=0;
     int noOfItems=0;
     SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
     Date date = new Date(System.currentTimeMillis());
        try{
            String file_name="C:\\IA_items\\PDF_generation\\Receipt.pdf";
            Document document=new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();
            Paragraph p1 = new Paragraph();
            p1.add(new Paragraph("RECEIPT", biggerText));
            addEmptyLine(p1, 1);

            p1.add(new Paragraph("Generated on: " +format.format(date)));
            addEmptyLine(p1, 1);

            Random rand = new Random();
            int random = rand.nextInt(100000);
            p1.add(new Paragraph("Receipt no #"+random));
            addEmptyLine(p1, 1);

            PdfPTable bill = new PdfPTable(4);

            PdfPCell c1 = new PdfPCell(new Phrase("Item"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            bill.addCell(c1);

            c1 = new PdfPCell(new Phrase("Quantity"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            bill.addCell(c1);

            c1 = new PdfPCell(new Phrase("Unit price"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            bill.addCell(c1);

            c1 = new PdfPCell(new Phrase("Amount"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            bill.addCell(c1);

            for(int i=0; i<receipt.size(); i++){
                bill.addCell(receipt.get(i).getProduct());
                bill.addCell(receipt.get(i).getQuantity().toString());
                String que = "SELECT itemPrice FROM ia_database.products where productCode='"+receipt.get(i).getCode()+"';";
                Statement statment= conn.createStatement();
                ResultSet rs= statment.executeQuery(que);
                while (rs.next()) {
                    bill.addCell(rs.getString(1));
                    noOfItems=noOfItems+receipt.get(i).getQuantity();
                    bill.addCell (String.valueOf((Integer.parseInt(rs.getString(1))*receipt.get(i).getQuantity())));

                }
            }
            p1.add(new Paragraph("Number of items purchased: "+noOfItems));
            addEmptyLine(p1, 2);

            p1.add(bill);
            addEmptyLine(p1, 1);

            p1.add(new Paragraph("SUBTOTAL: "+ String.valueOf(Total())+" AED"));
            addEmptyLine(p1, 1);

            p1.add(new Paragraph("VAT 5%: "+ String.valueOf(Math.round(Total()*0.05))+" AED"));
            addEmptyLine(p1, 1);

            p1.add(new Paragraph("TOTAL: "+ String.valueOf(Math.round(Total()*1.05))+" AED",bigTextNormal));
            addEmptyLine(p1, 2);

            p1.add(new Paragraph("______________________________________________________________________________",redFont));

            document.add(p1);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Receipt generated");
    }

    //private static void addEmptyLine(Paragraph paragraph, int number) {
      //  for (int i = 0; i < number; i++) {
       //     paragraph.add(new Paragraph(" "));
      //  }
    //}

    private static int addEmptyLine(Paragraph paragraph, int number) {
        if (number!=0) {
            paragraph.add(new Paragraph(" "));
            Paragraph p1 = new Paragraph();
            number--;
            return addEmptyLine(p1, number);
        }
        else{
            return number;
        }
    }


    public void Delete(ActionEvent actionEvent) throws SQLException { // Removes an item from the receipt
        try {
            conn = SQLConnect.getConnection();
            String code = receipt.getLast().getCode();
            int quan = receipt.getLast().getQuantity();
            String add = "UPDATE products SET itemQuantity=itemQuantity+" + quan + " WHERE productCode='" + code + "';";
            pst = conn.prepareStatement(add);
            pst.execute(add);
            receipt.removeLast();
            tableControl();
            BillControl();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Click clear to reset receipt");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void getBill(MouseEvent mouseEvent){
        index = FX_Bill.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        FX_addCode.setText(FX_BillCode.getCellData(index).toString());
        FX_addItem.setText(FX_BillItem.getCellData(index).toString());
        FX_addQuan.setText(FX_BillQuantity.getCellData(index).toString());
    }

    public void menuReturn() throws IOException { // Method used to transfer user access rights while navigating program
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

    public void tableControl() throws SQLException {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tableControl();
            BillControl();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ProductTable.setRowFactory(tv -> new TableRow<ProductDataModel>() {
            @Override
            public void updateItem(ProductDataModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getItemQuantity() == 0) {
                    setStyle("-fx-background-color: #f06c51;");
                } else {
                    setStyle("");
                }
            }
        });
    }
}
