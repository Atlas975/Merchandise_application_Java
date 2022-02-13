package IA_components;

public class ProductDataModel {
    String productCode;
    String itemName;
    String perishable;
    int branch;
    String itemCategory;
    String supplierName;
    String supplierContact;
    int itemPrice;
    int itemQuantity;

    public ProductDataModel(String productCode, String itemName, String perishable, int branch, String itemCategory, String supplierName, String supplierContact, int itemPrice, int itemQuantity) {
        this.productCode = productCode;
        this.itemName = itemName;
        this.perishable = perishable;
        this.branch = branch;
        this.itemCategory = itemCategory;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getProductCode() { return productCode; }

    public String getItemName() { return itemName; }

    public String getPerishable() {
        return perishable;
    }

    public int getBranch() {
        return branch;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

}
