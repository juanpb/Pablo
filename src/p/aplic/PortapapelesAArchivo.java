package p.aplic;

import org.apache.log4j.Logger;
import p.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PortapapelesAArchivo extends JFrame{
    private JButton _btn = new JButton("o");
    private JButton _btnInv = new JButton("Inv");
    private static File _archSal;
    private static final String SEPARADOR = Constantes.NUEVA_LINEA +
            "/////////////////////////////////////////////////////////////////";
    private static final Logger logger = Logger.getLogger(PortapapelesAArchivo.class);

    private static List<String> textosInicio;
    private static List<String> textosFin;

    public static void main(String[] args) throws IOException {
        parseInit(args);
        new PortapapelesAArchivo();
    }

    private static void parseInit(String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("Falta archivo de inicialización");
            throw new RuntimeException();
        }
        textosInicio = new ArrayList<String>();
        textosFin = new ArrayList<String>();

        File f = new File(args[0]);
        List<String> list = UtilFile.getArchivoPorLinea(f);
        for (String s : list) {
            if (s.startsWith("texto inicial = "))
                textosInicio.add(s.substring("texto inicial = ".length()));
            else if (s.startsWith("texto final = "))
                textosFin.add(s.substring("texto final = ".length()));
            else if (s.startsWith("archivo salida = "))
                _archSal = new File(s.substring("archivo salida = ".length()));    
        }
    }


    private PortapapelesAArchivo() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_btnInv, BorderLayout.WEST);
        getContentPane().add(_btn, BorderLayout.CENTER);
        setSize(230,80);
        _btn.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                copiar();
            }
        }
        );
        _btnInv.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                copiarInv();
            }
        }
        );
        GUI.centrar(this);
        setVisible(true);
    }

    private void copiarInv() {
        try {
            java.util.List<String> notas = new ArrayList<String>();
            String s = Util.copiarDesdeElPortapapeles();
            String sep = "Mi carpeta | Notas";
            int i = s.indexOf(sep);
            //tira la primer parte
            if (i > 0){
                s = s.substring(i + sep.length());
            }
            while ((i = s.indexOf(sep)) > 0){
                String nota = s.substring(0, i);
                notas.add(nota);
            }
            notas.add(s);
            String salida = "";
            int cant = notas.size();
            for (int j = 0; j < cant; j++) {
                String n = notas.get(cant - j -1);
                n += Constantes.NUEVA_LINEA;
                n += SEPARADOR;
                salida += n;
            }
            salida += SEPARADOR;
            UtilFile.agregarAlFinal(salida, _archSal);
            if (s.trim().length() > 10){
                String x = s.trim().substring(0, 10);
                setTitle(x);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void copiar() {
        try {
            String s = Util.copiarDesdeElPortapapeles();
//            s = limpiar(s);
            s = limpiar2011(s);
            UtilFile.agregarAlFinal(s + SEPARADOR, _archSal);
            if (s.trim().length() > 10){
                String x = s.trim().substring(0, 10);
                setTitle(x);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String limpiar2011(String s){
        String inicio = "Comentá";
        String fin1 = "A quien le interesó esta nota además leyó:";
        String fin2 = "Notas relacionadas";
        String fin3 = "¡Sé el primero en comentar!";
        String fin4 = "¡Enviá tu comentario!";
        String fin5 = "últimas notas de";

        //borro el principio hasta el texto "inicio"
        int desde = s.indexOf(inicio);
        if (desde > 0){
//            //busco cierre de paréntesis
//            desde = s.indexOf(")", desde);
            s = s.substring(desde + inicio.length()+1);
        }

        //borro el final desde el texto "fin1" o "fin2" ...
        int hasta = s.indexOf(fin1);
        if (hasta > 0)
            s = s.substring(0, hasta);

        hasta = s.indexOf(fin2);
        if (hasta > 0)
            s = s.substring(0, hasta);

        hasta = s.indexOf(fin3);
        if (hasta > 0)
            s = s.substring(0, hasta);

        hasta = s.indexOf(fin4);
        if (hasta > 0)
            s = s.substring(0, hasta);

        hasta = s.indexOf(fin5);
        if (hasta > 0)
            s = s.substring(0, hasta);
        return s.trim();
    }

    private String limpiar(String s){
        int i = -1;
        int j = 0;
        //limpio inicio
        while(i < 0 && textosInicio.size() > j){
            String ti = textosInicio.get(j++);
            i = s.indexOf(ti);
            if (i > 0)
                i += ti.length();
        }
        if (i < 0)
            i = 0;
        s = s.substring(i).trim();

        s = parcheInicio(s);

        //limpio fin
        i = -1;
        j = 0;
        while(i < 0 && textosFin.size() > j){
            String ti = textosFin.get(j++);
            i = s.indexOf(ti);
        }
        if (i < 0)
            i = s.length();
        s = s.substring(0, i);

        return s.trim();
    }

    /*
    Elimina:
    Facebook
      Twitter My Space Sonico Linkedin
      Link permanente

    FOTO
    */
    private String parcheInicio(String s){
        final List<String> list = UtilString.getTextAsList(s);
        if (list.size() > 5){
            int empezarEn = 0;
            if (list.get(0).trim().equals("Facebook"))
                empezarEn = 1;
            if (list.get(1).trim().startsWith("Twitter"))
                empezarEn = 2;
            if (list.get(2).trim().startsWith("Link permanente"))
                empezarEn = 3;
            if (list.get(3).trim().equals(""))
                empezarEn = 4;
            if (list.get(4).trim().startsWith("Link FOTO"))
                empezarEn = 5;
            return UtilString.list2String(list, empezarEn);
        }
        return s;
    }
}
