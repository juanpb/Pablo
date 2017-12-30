package p.correo;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class EmisorCorreo {

    static String mailHost = "smtp.gmail.com";//"smtp.arnet.com.ar";
    static String _de     = "infraestructuracertificacionit@gmail.com";
    static String _para       = "xxx@xxx.com";
    static String _asunto  = "Asunto666_port 25";
    static String _cuerpo     = "Cuerpo";
    private static String smtpPort = "25";
    private static String password;

    public static void main (String argv[]){
        password = argv[0];
        sendMsgSSL(_para, _asunto, _cuerpo, _de);
//        sendMsg(_para, _asunto, _cuerpo, _de);
    }

    public static void sendMsg(String  pPara, String pAsunto, String pCuerpo,
                               String pDe, String pFilePath){
        sendMsg(pPara, pAsunto, pCuerpo, pDe, pFilePath, null);
    }

    public static void sendMsg(String  pPara, String pAsunto,
                                String pCuerpo, String pDe){
        sendMsg(pPara, pAsunto, pCuerpo, pDe, null);
    }

    public static void sendMsg(String  pPara, String pAsunto, String pCuerpo,
                              String pDe, String pFilePath, String pFileName){
        try {
            System.out.println("Enviando meil a :    " + pPara);
    	    System.out.println("From:      " + pDe);
    	    System.out.println("Subject:    " + pAsunto);
	        System.out.println("BodyText:  " + pCuerpo);

            // mail variables
            Properties props;
            Session    session;

            // create some properties and get the default Session
            props = System.getProperties();
            props.put("mail.smtp.host", mailHost);
            props.put("mail.smtp.port", smtpPort);

            session = Session.getDefaultInstance(props, null);
            session.setDebug(false);

            // create a mime message
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(pDe));
            InternetAddress[] address = {new InternetAddress(pPara)};
            mail.setRecipients(Message.RecipientType.TO, address);
            mail.setSubject(pAsunto);

            // create and fill the first message part (bodyText)
            MimeBodyPart bodyPart1 = new MimeBodyPart();
            bodyPart1.setText(pCuerpo);

            // create the Multipart and its parts to it
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart1);

            //Attach
            if (pFilePath != null){
                MimeBodyPart bodyPart2 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(pFilePath);

                bodyPart2.setDataHandler(new DataHandler(fds));
                if (pFileName != null)
                    bodyPart2.setFileName(pFileName);
                else
                    bodyPart2.setFileName(pFilePath);

                multiPart.addBodyPart(bodyPart2);
            }

            // add the Multipart to the message
            mail.setContent(multiPart);

            // set the Date: header
            mail.setSentDate(new Date());

            // send the message
            System.out.println("Casi Listo");
            Transport.send(mail);

            System.out.println("Listo");

      } catch (Exception mex){
    	  System.out.println(mex.toString());
	      mex.printStackTrace();
	    }
    }

    public static void sendMsgSSL(String  pPara, String pAsunto, String pCuerpo,
                              String pDe){
        System.out.println("Enviando meil a :    " + pPara);
        System.out.println("From:      " + pDe);
        System.out.println("Subject:    " + pAsunto);
        System.out.println("BodyText:  " + pCuerpo);

        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;

        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", smtpPort);
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        mailServerProperties.put("mail.smtp.ssl.trust", mailHost);
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        try {
            System.out.println("\n\n 2nd ===> get Mail Session..");
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(_para));
            generateMailMessage.setSubject(_asunto);
            String emailBody = _cuerpo;
            generateMailMessage.setContent(emailBody, "text/html");
            System.out.println("Mail Session has been created successfully..");

            // Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");

            // Enter your correct gmail UserID and Password
            // if you have 2FA enabled then provide App Specific Password
            transport.connect(mailHost, _de, password);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
