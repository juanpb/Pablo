package p.correo;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.util.StringTokenizer;

public class PopupAuthenticator extends Authenticator {
    private static final Logger logger = Logger.getLogger(PopupAuthenticator.class);

    public static void main(String[] args){
        try {
//        String host = "pop.mail.yahoo.com.ar";
            String host = "ABAJO";
            String hostSTMP = "ABAJO";
            Properties props = System.getProperties();
            props.put("mail.pop3.host", host);
            props.put("mail.smtp.host", hostSTMP);

            //props.put("mail.pop3.port", "888");

            Authenticator auth = new PopupAuthenticator();
            Session session = Session.getDefaultInstance(props, auth);

            // Get the store
            Store store = null;

            store = session.getStore("pop3");

            store.connect();
            Folder f = store.getFolder("INBOX");
            Folder folder = f;
            System.out.println("folder.getFullName() = " + folder.getFullName());
            System.out.println("folder.getName() = " + folder.getName());
            System.out.println("folder.getURLName() = " + folder.getURLName());
            System.out.println("folder.getMessageCount() = " + folder.getMessageCount());
            System.out.println("folder.getNewMessageCount() = " + folder.getNewMessageCount());

            folder.open(Folder.READ_WRITE);
            Message message[] = folder.getMessages();
            System.out.println("message.length = " + message.length);

            // Display from (only first) and subject of messages
            for (int z=0, n=message.length; z<n; z++) {
                Message msg = message[z];
                verMsg(msg);
            }

            // Close connection
            folder.close(false);

            store.close();
            System.out.println("Listo");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }
        System.exit(0);
    }

    private static void verMsg(Message msg) {
        try {

            System.out.println(": "
                    + msg.getFrom()[0]
                    + "\t" + msg.getSubject());

            String x = msg.getContent().toString();
            System.out.println("msg.getDescription() = " + msg.getDescription());
            System.out.println("msg.getDisposition() = " + msg.getDisposition());
            System.out.println("msg.writeTo(out) = ");
            msg.writeTo(System.out);
            System.out.println("x = " + x);

            System.out.println("Enviando...");
            Address[] adr = new Address[1];
            adr[0] = new InternetAddress("juanpb0@yahoo.com.ar");
            Transport.send(msg, adr);
            System.out.println("Enviado");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public PasswordAuthentication getPasswordAuthentication() {
        String username, password;

//        String result = JOptionPane.showInputDialog("Enter 'username,password'");
        String result = "pablo17x,akkenaton";

        StringTokenizer st = new StringTokenizer(result, ",");
        username = st.nextToken();
        password = st.nextToken();

        return new PasswordAuthentication(username, password);
    }
}
