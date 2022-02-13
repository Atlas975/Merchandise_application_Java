package IA_components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorController implements Initializable {
    @FXML
    private Label result;
    public Label working;
    public Button FX1;
    public Button FX2;
    public Button FX3;
    public Button FX4;
    public Button FX5;
    public Button FX6;
    public Button FX7;
    public Button FX8;
    public Button FX9;
    public Button FX0;
    public Button FXEqual;
    public Button FXPlus;
    public Button FXMinus;
    public Button FXTimes;
    public Button FXDivide;
    public Button FXClear;
    private long temp;
    private long finish;
    private String operation;

    public void one(){
        String originalValue=result.getText();
        String number="1";
        result.setText(originalValue+number);
    }
    public void two(){
        String originalValue=result.getText();
        String number="2";
        result.setText(originalValue+number);
    }
    public void three(){
        String originalValue=result.getText();
        String number="3";
        result.setText(originalValue+number);
    }
    public void four(){
        String originalValue=result.getText();
        String number="4";
        result.setText(originalValue+number);
    }
    public void five(){
        String originalValue=result.getText();
        String number="5";
        result.setText(originalValue+number);
    }
    public void six(){
        String originalValue=result.getText();
        String number="6";
        result.setText(originalValue+number);
    }
    public void seven(){
        String originalValue=result.getText();
        String number="7";
        result.setText(originalValue+number);
    }
    public void eight(){
        String originalValue=result.getText();
        String number="8";
        result.setText(originalValue+number);
    }
    public void nine(){
        String originalValue=result.getText();
        String number="9";
        result.setText(originalValue+number);
    }
    public void zero(){
        String originalValue=result.getText();
        String number="0";
        result.setText(originalValue+number);
    }
    public void equal(){
        switch(operation){
            case "+":
                String originalValue=result.getText();
                this.finish=Integer.parseInt(originalValue);
                long plus=this.temp+this.finish;
                result.setText(String.valueOf(plus));
                String oldWork=working.getText();
                working.setText(oldWork+originalValue);
                break;
            case "-":
                String originalValue2=result.getText();
                this.finish=Integer.parseInt(originalValue2);
                long plus2=this.temp-this.finish;
                result.setText(String.valueOf(plus2));
                String oldWork2=working.getText();
                working.setText(oldWork2+originalValue2);
                break;
            case "÷":
                String originalValue3=result.getText();
                this.finish=Integer.parseInt(originalValue3);
                long plus3=this.temp/this.finish;
                result.setText(String.valueOf(plus3));
                String oldWork3=working.getText();
                working.setText(oldWork3+originalValue3);
                break;
            case "x":
                String originalValue4=result.getText();
                this.finish=Integer.parseInt(originalValue4);
                long plus4=this.temp*this.finish;
                result.setText(String.valueOf(plus4));
                String oldWork4=working.getText();
                working.setText(oldWork4+originalValue4);
                break;

        }

    }
    public void divide(){
        String originalValue=result.getText();
        long value=Integer.parseInt(originalValue);
        this.temp=value;
        result.setText("");
        working.setText(originalValue+"÷");
        operation="÷";

    }
    public void multiply(){
        String originalValue=result.getText();
        long value=Integer.parseInt(originalValue);
        this.temp=value;
        result.setText("");
        working.setText(originalValue+"x");
        operation="x";

    }
    public void plus(){
        String originalValue=result.getText();
        long value=Integer.parseInt(originalValue);
        this.temp=value;
        result.setText("");
        working.setText(originalValue+"+");
        operation = "+";
    }
    public void minus(){
        String originalValue=result.getText();
        long value=Integer.parseInt(originalValue);
        this.temp=value;
        result.setText("");
        working.setText(originalValue+"-");
        operation="-";
    }
    public void clear(){
        working.setText("");
        result.setText("");
        this.temp=0;
        this.finish=0;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void Click(MouseEvent mouseEvent) {
    }
}

