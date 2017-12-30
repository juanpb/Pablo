package p.gui.systemTray;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * User: JPB
 * Date: Sep 2, 2008
 * Time: 2:18:21 PM
 */
public class Prueba {

    public Prueba(){


        final TrayIcon trayIcon;

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("D:\\P\\caiIcon.jpg");

        PopupMenu popup = new PopupMenu();
        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting...");
                System.exit(0);
            }
        });
        popup.add(defaultItem);

        trayIcon = new TrayIcon(image, "Tray Demo", popup);

        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }

    }

    public static void main(String[] args) {
//        new Prueba();
        System.out.println("new Date(1231076976046L) = " + new Date(1231076976046L));
    }
}
