package IA_components;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

    public class comploBotControl implements Initializable {
        public Button button;
        public Label text;
        public TextField email;
        public String [] complimentArray=
               { "I Bet You Make Babies Smile", "You Have Impeccable Manners" ,
                       "You Are The Most Perfect You There Is" , "You Should Be Proud Of Yourself" ,
                       "You’re More Helpful Than You Realize",
                 "You're wonderful","Nice haircut","You're a great person","You're the best",
                "You're amazing", "You have a nice smile","You’re more fun than bubble wrap",
                "You make the world a brighter place", "Christian is abused", "He deserves it", };
        public String temp;

        public void Button1(ActionEvent event) {
            Random rand = new Random();
            int random = rand.nextInt(complimentArray.length);
            temp=complimentArray[random];
            text.setText(temp);
        }

        public void Button2(ActionEvent event) {
            EmailConnect.compile(temp, email.getText() );
        }

        @Override
        public void initialize(URL url, ResourceBundle rb) {}
    }
