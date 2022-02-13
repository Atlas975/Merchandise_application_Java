package IA_components;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailConnect {
    public static void compile(String product, String quantity, String supplierEmail) { //Method used to edit email content
        String from = "bahootlaga@gmail.com";
        String to = supplierEmail;

        Properties properties = createConfiguration();

        SmtpAuthenticator authentication = new SmtpAuthenticator();

        Session session = Session.getDefaultInstance(properties, authentication);

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            message.setSubject("Item restock request");
            message.setText("Requesting a restock of "+product+", "+quantity+" more items needed");
            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    public static void compile(String product, String supplierEmail) {
        String from = "sqltestmail77@gmail.com";
        String to = supplierEmail;

        Properties properties = createConfiguration();

        SmtpAuthenticator authentication = new SmtpAuthenticator();

        Session session = Session.getDefaultInstance(properties, authentication);

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            message.setSubject("Someone needs a compliment");
            message.setText(product);
            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private static Properties createConfiguration() { // Method used to connect to SMTP server
        return new Properties() {
            {
                put("mail.smtp.auth", "true");
                put("mail.smtp.host", "smtp.gmail.com");
                put("mail.smtp.port", "587");
                put("mail.smtp.starttls.enable", "true");
            }
        };
    }
    private String username = "sqltestmail77@gmail.com";
    private String password = "RotiPol5";

    private static class SmtpAuthenticator extends Authenticator { //Method used to connect to company gmail


        private String username = "sqltestmail77@gmail.com";
        private String password = "RotiPol5";

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
