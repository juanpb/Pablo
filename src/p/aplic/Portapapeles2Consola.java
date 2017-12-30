package p.aplic;

import org.apache.log4j.Logger;
import p.util.GUI;
import p.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * User: JPB
 * Date: Sep 3, 2007
 * Time: 9:37:53 AM
 */
public class Portapapeles2Consola extends JWindow {

    private JButton btnArriba = new JButton("R");
    private JButton btnAbajo = new JButton("B");
    private JButton btnCentro = new JButton("C");
    private List<String> lineas;
    private long tiempo = 2500;
    private int indice;
    private Timer timer = new Timer();;
    private Tarea tarea;
    private boolean paradoPorMouse = false;
    private static final int CANTIDAD_CHARS = 80;
    private JTextArea txt = new JTextArea();
    private static final Logger logger = Logger.getLogger(Portapapeles2Consola.class);

    public static void main(String[] args) {

//        Locale[] availableLocales = Locale.getAvailableLocales();
//        for (int i = 0; i < availableLocales.length; i++) {
//            Locale l = availableLocales[i];
//            System.out.println(l.getDisplayName());
//        }
        new Portapapeles2Consola();
    }
    public Portapapeles2Consola() {
        init();
    }

    private void init(){
        tarea = new Tarea();
        timer.schedule(tarea,0, tiempo);

        getContentPane().setLayout(null);
        getContentPane().add(btnArriba);
        getContentPane().add(btnCentro);
        getContentPane().add(btnAbajo);

        int w = 30;
        int h = 20;
        btnArriba.setBounds(0, 0, w, h);
        btnCentro.setBounds(0, h, w, h);
        btnAbajo.setBounds(0, h*2, w, h);

        btnArriba.setMargin(new Insets(0,0,0,0));
        btnCentro.setMargin(new Insets(0,0,0,0));
        btnCentro.setMargin(new Insets(0,0,0,0));

        setSize(w,h*3);
        setPreferredSize(new Dimension(w,h*3));
//        btnArriba.addMouseListener(new MouseAdapter(){
//            public void mousePressed(MouseEvent e){
//                arriba();
//            }
//        });
        btnCentro.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                centro();
            }
        });

        btnAbajo.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                abajo();
            }
            public void mouseExited(MouseEvent e) {
                if (tarea.isContinuar()){
                    tarea.continuar(false);
                    paradoPorMouse = true;
                }
            }
            public void mouseEntered(MouseEvent e) {
                if (paradoPorMouse){
                    paradoPorMouse = false;
                    tarea.continuar(true);
                }                                         
            }
        });

        btnArriba.addMouseListener(new MouseAdapter(){
//            public void mouseEntered(MouseEvent e) {
//                System.out.println("6666666666666666666666666666666");
//                    System.out.println("<class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloBsC</class>\n" +
//                            "        <class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloQry</class>\n" +
//                            "        <class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloModAct</class>\n" +
//                            "        <class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloReplicarAct</class>\n" +
//                            "        <class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloST</class>\n" +
//                            "        <class>crm/appl/fct/facturacion/mxml/ciclos/FctCicloSTFnd</class>");
//                }
//            }
            public void mouseEntered(MouseEvent e) {
                if (paradoPorMouse){
                    paradoPorMouse = false;
                    tarea.continuar(true);
                }
            }
        });

        GUI.centrarALaDerecha(this);
        setVisible(true);

        //Frame
//        JFrame frame = new JFrame();
//        frame.setLayout(new BorderLayout());
//        JScrollPane sp = new JScrollPane(txt,
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        frame.add(sp, BorderLayout.CENTER);
//
//        frame.setSize(910, 120);
//        frame.setLocation(59, 582);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        txt.setWrapStyleWord(true);
//        txt.setLineWrap(true);
//        txt.setBackground(Color.BLACK);
//
//        tarea.setTextArea(txt);
    }

//    private void arriba(){
//        tarea.continuar(false);
//    }

    private void abajo(){
        tarea.continuar(true);
    }

    private void centro(){
        lineas = new ArrayList();
        indice = 0;
        try {
            String s = Util.copiarDesdeElPortapapeles();
            agregarLineas(s);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        tarea.setIndice(indice);
        tarea.setLineas(lineas);
    }

    private void agregarLineas(String s){
        StringTokenizer st = new StringTokenizer(s, "\n");
        while(st.hasMoreTokens()){
            String s1 = st.nextToken();

            lineas.addAll(partir(s1));
        }
    }

    private List<String> partir(String s){
        List<String> res = new ArrayList<String>();

        while(s.length() > CANTIDAD_CHARS){
            String sub = s.substring(0, CANTIDAD_CHARS);
            int i = sub.lastIndexOf(" ");
            sub = s.substring(0, i);
            res.add(sub);
            s = s.substring(i+1);
        }

        if (s.length() > 0)
            res.add(s);
        return res;
    }
}
class Tarea extends TimerTask {
    private int indice;
    private List<String> lineas;
    private boolean continuar = false;
    private Locale loc = new Locale("Spanish", "Argentina");
    private JTextArea textArea;

    public void setTextArea(JTextArea textArea){
        this.textArea = textArea;
    }


    public void setIndice(int ind){
        indice = ind;
    }
    public void setLineas(List<String> l){
        lineas = l;
    }

    public void continuar(boolean b){
        continuar = b;
    }


    public boolean isContinuar() {
        return continuar;
    }

    private boolean par = false;

    public void run() {
        if (!continuar)
            return ;
//        if (indice % 2 == 0)
//            textArea.setForeground(Color.RED);
//        else
//            textArea.setForeground(Color.GREEN);
        String txt;
        if (lineas != null && lineas.size() > indice)
//            System.out.format(lineas.get(indice++));
            txt = lineas.get(indice++);
        else{
            continuar = false;
            txt = "----------------------------------";
        }
        imprimir(txt + "\n");
    }

    private void imprimir(String x){
        System.out.print(x);

        if (textArea != null){
            textArea.append(x);
        }

    }
}