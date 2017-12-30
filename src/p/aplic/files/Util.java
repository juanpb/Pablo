package p.aplic.files;

import org.apache.log4j.Logger;
import p.gui.JTextArea_;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * User: JP
 * Date: 05/08/2005
 * Time: 23:31:49
 */
public class Util {
    private static String _raiz =  "C:\\temp\\" + new Date().getTime();
    private static final Logger logger = Logger.getLogger(Util.class);

    public static void main(String[] args) throws IOException {
        Util u = new Util();
        u.mostrar();
    }

    public void mostrar(){
        JPanel botonera = new JPanel(new BorderLayout());
        final JTextArea_ txt = new JTextArea_();
        JButton btn = new JButton("Aceptar");
        JScrollPane sp = new JScrollPane(txt);
        JPanel p = new JPanel(new BorderLayout());
        final JDialog dlg = new JDialog();
        dlg.getContentPane().add(p);



        int an = 330;
        sp.setPreferredSize(new Dimension(an, 420));
        botonera.setPreferredSize(new Dimension(an, 30));

        botonera.add(btn);
        p.add(sp, BorderLayout.CENTER);
        p.add(botonera, BorderLayout.SOUTH);

        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String x = txt.getText();
                hacerDirs(x);
                dlg.setVisible(false);
            }
        });
        dlg.pack();
        dlg.setVisible(true);
    }

    private void hacerDirs(String x) {
        List list = parsear(x);
        List dirs = new Vector();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            dirs.add(o);
        }
        try {
            crearDirectorios(dirs, _raiz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List parsear(String pTxt) {
        String sep = "\n";
        String linea = "";
        int hasta;
        int desde = 0;
        Vector res = new Vector();
        while((hasta = pTxt.indexOf(sep, desde)) > 0){
            linea = pTxt.substring(desde, hasta);
            //System.out.println( linea);
            if (!linea.equals(""))
                res.add(linea);
            desde = hasta + 1;
        }
        if (pTxt.length() > desde){
            linea = pTxt.substring(desde).trim();
            if (!linea.equals(""))
                res.add(linea);
        }
        return res;
    }

    public void crearDirectorios(List dirs, String raiz) throws IOException {
        crearDirectorios(dirs, new File(raiz));
    }

    public void crearDirectorios(List dirs, File raiz) throws IOException {
        raiz.mkdirs();
        //elimino repetidos
        HashMap ht = new HashMap();
        for (int i = 0; i < dirs.size(); i++) {
            String s = (String) dirs.get(i);
            ht.put(s, s);
        }
        Iterator it = ht.keySet().iterator();
        while (it.hasNext()) {
            String s  = (String) it.next();
            System.out.println("raiz = " + raiz.getAbsolutePath());
            System.out.println("s = " + s);
            File f = new File(raiz, s);
            System.out.println("f.getAbsolutePath() = " + f.getAbsolutePath());
            f.mkdir();
        }

    }
}
