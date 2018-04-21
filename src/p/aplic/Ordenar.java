package p.aplic;

import org.apache.log4j.Logger;
import p.gui.JTextArea_;
import p.util.Constantes;
import p.util.Fechas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class Ordenar implements p.util.Constantes{

    private JFrame      _frame = new JFrame("JP");
    private JTextArea_   _textArea = new JTextArea_();
    private JButton     _btnOrdenar = new JButton();
    private JButton     _btnAbrirArchivo = new JButton();
    private JButton     _btnSetGet = new JButton();
    private StringBuffer _salidaSG   = new StringBuffer();
    private Vector      _constructor;
    private static final String TAB = "    ";
    private String _nuevaLinea  = "";
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton btnX = new JButton();
    private JButton btnInvertir = new JButton();
    private JButton btnSacarRepetidos = new JButton();

    private static final Logger logger = Logger.getLogger(Ordenar.class);

    public static void main(String[] args){
        new Ordenar();
    }

    public Ordenar(){
//        _nuevaLinea  = System.getProperty("line.separator");
        _nuevaLinea = "\n";
        try{
            jbInit();
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void jbInit() throws Exception{
        _textArea.setText("");
        _frame.getContentPane().setLayout(gridBagLayout1);
        _btnOrdenar.setMargin(new Insets(2, 1, 2, 1));
        _btnOrdenar.setText("Ordenar");
        _btnOrdenar.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                String txt = _textArea.getText();
                txt = p.util.Util.ordenar(txt);
                _textArea.setText(txt);
            }
        });
        _btnAbrirArchivo.setMargin(new Insets(2, 1, 2, 1));
        _btnAbrirArchivo.setText("Ordenar archivo");
        _btnAbrirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btmAbrirArchivo();
            }
        });
        _frame.addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        JScrollPane _scrollPane = new JScrollPane(_textArea);
        _btnSetGet.setMargin(new Insets(2, 1, 2, 1));
        _btnSetGet.setText("set /  get");
        _btnSetGet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _btnSetGet_actionPerformed();
            }
        });
        btnX.setMargin(new Insets(2, 1, 2, 1));
        btnX.setText("gastos");
        btnX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parsearGastos();
            }
        });
        btnInvertir.setMargin(new Insets(2, 1, 2, 1));
        btnInvertir.setText("Invertir");
        btnInvertir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imprimirInvertido();
            }
        });
        btnSacarRepetidos.setMargin(new Insets(2, 1, 2, 1));
        btnSacarRepetidos.setText("Sacar Repetidos");
        btnSacarRepetidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sacarRepetidos();
            }
        });
        _frame.getContentPane().add(_scrollPane,  new GridBagConstraints(0, 0, 10, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(13, 16, 0, 11), 474, 439));
        _frame.getContentPane().add(_btnOrdenar,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 16, 5, 0), 2, -4));
        _frame.getContentPane().add(_btnAbrirArchivo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 1, 5, 21), 1, -4));
        _frame.getContentPane().add(_btnSetGet, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -4));
        _frame.getContentPane().add(btnX,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        _frame.getContentPane().add(btnInvertir,  new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        _frame.getContentPane().add(btnSacarRepetidos,  new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        _frame.pack();
        _frame.setVisible(true);
    }

    void btmAbrirArchivo(){
        JFileChooser chooser = new JFileChooser("C:\\");
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String txt = p.util.Util.ordenar(file);
            _textArea.setText(txt);
        }
    }

    void _btnSetGet_actionPerformed() {
        _salidaSG = new StringBuffer();
        _constructor = new Vector();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            hacerSG(linea);
        }
        String cons = hacerConstructor();
        txt = cons + _nuevaLinea + _salidaSG.toString();
        _textArea.setText(txt);
        p.util.Util.pegarEnElPortapapeles(txt);
    }

    private String hacerConstructor(){
        String res = TAB + "public ZZZ( " ;
        String res2 = "";

        String nombre, tipo, aux;

        int i, j;
        for (i = 0; i < _constructor.size() - 1; i++) {
            aux = (String) _constructor.elementAt(i);
            j = aux.indexOf(",");
            tipo = aux.substring(0, j);
            nombre = aux.substring(j + 1, aux.length());
            String dis = "";
            String nom = nombre;
            if (nombre.startsWith("_"))
                nom = nombre.substring(1);
            else
                dis = "this.";
            res  += tipo + " " + nom + ", ";
            res2 += TAB + TAB + dis + nombre + " = " + nom  + ";" + _nuevaLinea;

        }
        aux = (String) _constructor.elementAt(i);
        j = aux.indexOf(",");
        tipo = aux.substring(0, j);
        nombre = aux.substring(j + 1, aux.length());
        String dis = "";
        String nom = nombre;
        if (nombre.startsWith("_"))
            nom = nombre.substring(1);
        else
            dis = "this.";
        res  += tipo + " " + nom + "){ " + _nuevaLinea;
        res2 += TAB + TAB + dis + nombre + " = " + nom + ";" + _nuevaLinea;

        return res + res2 + TAB + "}";
    }

    private void hacerSG(String linea){
        // linea = {'' | private | public} {tipo}{nombre}{''| = "...."}
        // ej    = private String nombre = "";
        linea = linea.trim();
        if (linea.startsWith("private ")){
            linea = linea.substring(7, linea.length());
            linea = linea.trim();
        }else if (linea.startsWith("public ")){
            linea = linea.substring(6, linea.length());
            linea = linea.trim();
        }
        int x = linea.indexOf(" ");
        String tipo = linea.substring(0, x);
        linea = linea.substring(x, linea.length());
        linea = linea.trim();
        x = linea.indexOf("=");
        String nombre;
        if (x > 0)
            nombre = linea.substring(0, x).trim();
        else
            nombre = linea.substring(0, linea.indexOf(";")).trim();
        hacerSet(tipo, nombre);
        if (tipo.equals("boolean"))
            hacerIs(nombre);
        else
            hacerGet(tipo, nombre);
        _salidaSG.append(_nuevaLinea);
        _constructor.add(tipo + "," + nombre);
    }

    private void hacerSet(String tipo, String nombre){
        _salidaSG.append(TAB + "public void set");
        String dis = "";
        String nom = nombre;
        if (nombre.startsWith("_"))
            nom = nombre.substring(1);
        else
            dis = "this.";
        _salidaSG.append(mayuscula(nom) + "(" + tipo + " p) {" + _nuevaLinea);
        _salidaSG.append(TAB + TAB + dis + nombre + " = p;" + _nuevaLinea );
        _salidaSG.append(TAB + "}" + _nuevaLinea);
    }

    private void hacerGet(String tipo, String nombre){
        _salidaSG.append(TAB + "public " + tipo + " get");
        String dis = "";
        String nom = nombre;
        if (nombre.startsWith("_"))
            nom = nombre.substring(1);
        else
            dis = "this.";
        _salidaSG.append(mayuscula(nom) + "() {" + _nuevaLinea);
        _salidaSG.append(TAB + TAB + "return " + dis + nombre + ";" + _nuevaLinea );
        _salidaSG.append(TAB + "}" + _nuevaLinea);
    }

    private void hacerIs(String nombre){
        _salidaSG.append(TAB + "public boolean is");
        String dis = "";
        String nom = nombre;
        if (nombre.startsWith("_"))
            nom = nombre.substring(1);
        else
            dis = "this.";
        _salidaSG.append(mayuscula(nom) + "() {" + _nuevaLinea);
        _salidaSG.append(TAB + TAB + "return " + dis + nombre + ";" + _nuevaLinea );
        _salidaSG.append(TAB + "}" + _nuevaLinea);
    }

    //supongo x.length > 1
    private String mayuscula(String x){
        String pl = x.substring(0,1);
        return pl.toUpperCase() + x.substring(1, x.length());
    }

    private void imprimirInvertido(){
        StringBuffer salida = new StringBuffer();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        List<String> sal = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            sal.add(linea);
        }
        for (int i = sal.size()-1; i > -1; i--) {
            String s =  sal.get(i);
            salida.append(s).append(Constantes.NUEVA_LINEA);

        }
        p.util.Util.pegarEnElPortapapeles(salida.toString());
        _textArea.setText(salida.toString());
    }
    private void parsearGastos(){
        StringBuffer salida = new StringBuffer();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        List<Gasto> gs = new ArrayList<Gasto>();
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            gs.add(lineaAGasto(linea));
        }
        Collections.sort(gs);
        for(Gasto g : gs) {
            salida.append(g.nombre).append(Constantes.TAB_CHARACTER).
                    append(Fechas.getAAAAMMDD("/", g.fecha.getTime())).
                    append(Constantes.TAB_CHARACTER).
                    append(g.importe.toString().replace(".", ",")).
                    append(Constantes.NUEVA_LINEA);
        }


        p.util.Util.pegarEnElPortapapeles(salida.toString());
        _textArea.setText(salida.toString());
    }

    private Gasto lineaAGasto(String g){
        return new Gasto(g);
    }

    private void sacarRepetidos(){
        StringBuffer salida = new StringBuffer();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        Set map = new HashSet();
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            boolean noEstaba = map.add(linea);
            if (noEstaba){
                salida.append(linea).append(Constantes.NUEVA_LINEA);
            }
        }
        
        p.util.Util.pegarEnElPortapapeles(salida.toString());
        _textArea.setText(salida.toString());
    }

    //asume que la línea viene asi: AAN,Mensajeria Internacional
    //si el segundo campo no viene asume que es igual al primero
    private void hacerConstructores(){
        StringBuffer salida = new StringBuffer();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        String clase = "pirulo";
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            StringTokenizer st2 = new StringTokenizer(linea, ",", false);
            try {
                String c1 = st2.nextToken();
                String c2;
                if (st2.hasMoreElements())
                    c2 = st2.nextToken();
                else
                    c2 = c1;
                String n = "res.add(new " + clase + "(\"" + c1 + "\", \"" + c2 + "\"));";
                salida.append(n).append(Constantes.NUEVA_LINEA);
            }
            catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        p.util.Util.pegarEnElPortapapeles(salida.toString());
        _textArea.setText(salida.toString());
    }


    void _btnCrearCarpetas_actionPerformed() {
        StringBuilder salida = new StringBuilder();
        String txt = _textArea.getText();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        while (st.hasMoreTokens()) {
            String linea = st.nextToken();
            if (linea != null) {
                int sep = linea.indexOf("\"");
                if (sep > -1)
                    linea = linea.substring(0, sep).trim();
                salida.append(linea).append(NUEVA_LINEA);
            }
        }
        _textArea.setText(salida.toString());
//        long ini = System.currentTimeMillis();
//
//
//        String destinoRaiz = "C:\\temp\\grabar" + System.currentTimeMillis() + "\\";
//        String raizMusica = "M:\\";
//
//        File ff  = new File(destinoRaiz);
//        ff.mkdirs();
//        /*
//        //genero backup y  lo muevo.
//        try {
//            String[] args = new String[1];
//            Backup.main(args);
//            ff = new File(args[0]);
//            String name = ff.getName();
//            name = UtilString.reemplazarTodo(name, ".zip", "");
//            File des = new File(destinoRaiz, name);
//
//            ff.renameTo(des);
//        } catch (Exception ex1) {
//            JOptionPane.showMessageDialog(_frame, ex1.getMessage());
//        }
//        */
//
//        while (st.hasMoreTokens()) {
//            String linea = st.nextToken();
//            if (linea != null && linea.trim().length() > 0) {
//                try {
//                    int sep = linea.indexOf(TAB_CHARACTER);
//                    String art = linea.substring(0, sep).trim();
//                    String alb = linea.substring(sep + 1).trim();
//                    String destino = destinoRaiz + art + PATH_SEPARATOR + alb;
//                    String postRaiz = art + PATH_SEPARATOR + alb;
//                    String origen = raizMusica + postRaiz;
//
//                    File arch = new File(origen);
//                    boolean preguntar = !arch.exists();
//                    while (preguntar){
//                        String msg = "'Cancelar' -> ignora " + NUEVA_LINEA +
//                                "vacío + 'Aceptar' para reintentar" +
//                                  NUEVA_LINEA +
//                                "o nueva dir + 'Aceptar'";
//                        String dir = JOptionPane.showInputDialog(msg, arch.getAbsolutePath());
//                        if (dir == null) //se apretó cancelar
//                            preguntar = false;
//                        else if (!dir.trim().equals(""))
//                            //si se ingresó algo pruebo con la nueva dir
//                            arch = new File (dir.trim());
//                        //else si se dejó en blanco vuelvo a probar con la dir
//                        //original
//                        preguntar = preguntar && !arch.exists();
//                    }
//                    _frame.setTitle("Copiando " + arch.getPath());
//                    UtilFile.copiarDir(arch.getPath(), destino);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(_frame, ex.getMessage());
//                }
//            }
//        }
//        _frame.setTitle("Listo");
//        long fin = System.currentTimeMillis();
//        long tot = (fin-ini)/1000;
//        _textArea.setText("Demoró: " + tot + " seg.");
    }

    private BigDecimal string2BD(String s){
        s = s.replace(",", ".");
        return new BigDecimal(s);
    }

    private Date string2Date(String s){
        return Fechas.getDateDD_MM_AA(s);
    }

    @SuppressWarnings({"NonStaticInnerClassInSecureContext"})
    private class Gasto implements Comparable{
        public Date fecha;
        public String nombre;
        public BigDecimal importe;

        public Gasto(String g){
            StringTokenizer st = new StringTokenizer(g, Constantes.TAB_CHARACTER+"");
            fecha = string2Date(st.nextToken());
            nombre = st.nextToken();
            if (st.hasMoreTokens())
                importe = string2BD(st.nextToken());
            else
                importe = new BigDecimal(0);
        }

        @Override
        public String toString() {
            String i = importe.toString();
            String im = i.replace(".", ",");
            return Fechas.getDDMMAA("/", fecha.getTime()) +
                    Constantes.TAB_CHARACTER + nombre +
                    Constantes.TAB_CHARACTER + im;
        }

        //a negative integer, zero, or a positive integer as 
        // this object is less than,
        // equal to, or
        // greater than the specified object.
        public int compareTo(Object o) {
            Gasto g = (Gasto) o;

            //primero por nombre
            String n1 = nombre.toLowerCase();
            String n2 = g.nombre.toLowerCase();
            if (n1.compareTo(n2) != 0)
                return n1.compareTo(n2) ;

            //luego por fecha. Van primero los de fecha mayor
            return g.fecha.compareTo(fecha);
        }
    }
}

