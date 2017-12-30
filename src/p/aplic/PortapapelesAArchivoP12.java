package p.aplic;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.GUI;
import p.util.Util;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class PortapapelesAArchivoP12 extends JFrame{
    private JButton _btn = new JButton("o");
    private JButton _btnInv = new JButton("Inv");
    private static File _archSal;
    private static final Logger logger = Logger.getLogger(PortapapelesAArchivoP12.class);
    private Set<Integer> hashes = new TreeSet<Integer>();

    private static final int TAM_MAXIMO = 850;

    private static List<String> textosInicio;
    private static int cantPalabras;
    private static int PALABRAS_POR_HOJA = (7681+9860+5354)/(16+21+12);
    private static List<String> textosFin;
    private static final String SEPARADOR_NOTAS = "Nueva nota:";

    /*
     5354 palab -> 12 hojas => 446 p/h
     9860 / 21 => 469 p/h
     7681/16 => 480
     */

    public static void main(String[] args) throws IOException {
        parseInit(args);
        new PortapapelesAArchivoP12();
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


    private PortapapelesAArchivoP12() {
        init();
    }


    private void init() {
        //Drag and drop
        _btn.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }

            public boolean importData(TransferHandler.TransferSupport support) {

                String data;
                try {
                    data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    copiar(data);
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (java.io.IOException e) {
                    return false;
                }
                System.out.println(data);
                return true;
            }});


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        GUI.centrar(this);
        setVisible(true);
    }

    private void copiar() {
        try {
            copiar(Util.copiarDesdeElPortapapeles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copiar(String txt) {
        try {
            String s = limpieza(txt);
            boolean nuevo = hashes.add(s.hashCode());
            if (!nuevo){
                JOptionPane.showMessageDialog(this, "Texto repetido");
            }else{
                List<String> partir = partir(s);
                s = partido2String(partir);
                UtilFile.agregarAlFinal(s, _archSal);

                int cant = s.split(" ").length;
                cantPalabras += cant;
                int cantHojas = cantPalabras/ PALABRAS_POR_HOJA;
                _btn.setText(cantHojas+"");


                String titulo;
                if (s.trim().length() > 10)
                    titulo = s.trim().substring(0, 10);
                else
                    titulo = s.trim();

                setTitle(titulo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String limpieza(String s) {
        Map<Integer,String> map = new HashMap<Integer, String>();
        map.put(8220, "\"");
        map.put(8221, "\"");
        map.put(8211, "-");
        map.put(8216, "'");
        map.put(8217, "'");

        for (Integer i : map.keySet()) {
            char c = (char) i.intValue();
            String replacement = map.get(i);
            s = s.replaceAll(c + "", replacement);
        }

        s = s.replaceAll("PáginaI12", "Página");

        return s;


//        int a = 8220;
//        char c = (char) a;
//        int i = s.indexOf(c);
//        int x = s.indexOf(8221);
//        s = s.replaceAll(c+"", "\"");
//
//        c = (char) 8221;
//        i = s.indexOf(c);
//        s = s.replaceAll(c+"", "\"");

//        return s;
    }

    private String partido2String(List<String> partir) {
        StringBuilder res = new StringBuilder();
        for (String s : partir) {
            res.append(s);
            res.append(Constantes.NUEVA_LINEA);
            res.append(Constantes.NUEVA_LINEA);
        }
        res.append(SEPARADOR_NOTAS);
        res.append(Constantes.NUEVA_LINEA);
        res.append(Constantes.NUEVA_LINEA);
        return res.toString();
    }

    private List<String> partir(String s) {
        String[] split = s.split(Pattern.quote("\n"));
        List<String> res = cortarSiEsLargo(split);
        return res;
    }

    private List<String> cortarSiEsLargo(String[] split) {
        List<String> res = new ArrayList<String>();
        for (String s : split) {
            if (!s.trim().isEmpty()){
                if (s.length() > TAM_MAXIMO){
                    res.addAll(dividir(s));
                }
                else
                    res.add(s);
            }
        }
        return res;
    }

    private List<String>  dividir(String s) {
        List<String> res = new ArrayList<String>();
        String[] lineas = s.split(Pattern.quote("."));
        String tmp = lineas[0];
        for (int i = 1; i < lineas.length; i++) {
            String n = lineas[i];
            if (tmp.length() + n.length() < TAM_MAXIMO)
                tmp += ". " + n;
            else {
                res.add(tmp);
                tmp = n;
            }
        }
        res.add(tmp);
        return res;
    }



}
