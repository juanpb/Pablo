package p.gui.systemTray;

import org.apache.log4j.Logger;
import p.util.UtilFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * User: JPB
 * Date: Sep 1, 2008
 * Time: 12:10:16 PM
 */
public class Main implements ActionListener {
    private static final Logger logger = Logger.getLogger(Main.class);
    private TrayIcon trayIcon = null;
    private PopupMenu popup = new PopupMenu();
    private Map<String, Programa> map;

    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Mal los parámetros. Se debe pasar el ini");
        }
        new Main(args[0]);
    }

    private void init(String iniFile){
        List<Programa> programas = parsearIni(iniFile);
        for(Programa p : programas) {
            MenuItem mi = new MenuItem(p.getNombre());
            mi.addActionListener(this);
            popup.add(mi);
        }

        MenuItem salirItem = new MenuItem("Salir");
        salirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popup.addSeparator();
        popup.add(salirItem);
        programasAMap(programas);
    }

    private void programasAMap(List<Programa> programas){
        map = new HashMap<String, Programa>();
        for(Programa p : programas) {
            map.put(p.getNombre(), p);
        }
    }
    private java.util.List<Programa> parsearIni(String iniFile){
        java.util.List<Programa> res = new ArrayList<Programa>();
        try {
            List<String> lista = UtilFile.getArchivoPorLinea(iniFile);
            for(String s : lista) {
                StringTokenizer st = new StringTokenizer(s, ";");
                String nombre = st.nextToken();
                String clase = st.nextToken();
                String ini = null;
                if (st.hasMoreTokens())
                    ini = st.nextToken();
                res.add(new Programa(nombre, clase, ini));
            }
        } catch (IOException e) {
            System.out.println("Mal el archivo de ini");
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return res;
    }

    public Main(String iniFile) {

        if (!SystemTray.isSupported()){
            System.out.println("!SystemTray.isSupported()");
            return ;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("D:\\P\\caiIcon.jpg");
        init(iniFile);
        trayIcon = new TrayIcon(image, "JPB", popup);

        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }

    }

    public void actionPerformed(ActionEvent e) {
        MenuItem source = (MenuItem) e.getSource();
        Programa p = map.get(source.getLabel());
        p.ejecutar();
    }
}

