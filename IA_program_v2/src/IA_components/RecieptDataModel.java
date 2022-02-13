package IA_components;

public class RecieptDataModel {

    private final String Product;
    private final Integer Quantity;
    private final String Code;

    public RecieptDataModel(String Code, String Product, Integer Quantity) {
        this.Product = Product;
        this.Quantity = Quantity;
        this.Code=Code;
    }

    public String getProduct(){
        return Product;
    }
    public Integer getQuantity(){
        return Quantity;
    }
    public String getCode(){
        return Code;
    }
}
