package p.aplic;

import org.apache.log4j.Logger;
import p.util.Fechas;
import p.util.UtilString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Calendar;
import java.util.Date;


public class Horarios extends JFrame implements Runnable{
    private static final Logger logger = Logger.getLogger(Horarios.class);

    String      _dir        = "C:\\";
    String      ENCABEZADO = "Fecha          Hora inicial   Hora final     Total";
    String      _nuevaLinea;
    static boolean     _oculto    = false;
    static String     _backup    = null;

    JTextArea   _txtDesde   = new JTextArea();
    JLabel      _lblDesde   = new JLabel();
    JLabel      _lblTotal   = new JLabel();
    JTextArea   _txtTotal   = new JTextArea();
    JButton     _btn        = new JButton();
    String      _btnTxt     = "CERRAR";
    String      _desde      = "";
    String      _total      = "";
    String      _fileNameTemp   = "C:\\horasTemp.txt";
    String      _fileNameLog    = "C:\\horario.log";
    long        _horaInicial = 0;
    String      _fileMensual = "";

    Thread  _thread     = null;
    int     _intervalo  = 1000;
    boolean _pararThread = false;
    boolean _enEjecucion = false;
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    String _annus;
    String  _mes;
    String  _numDia;

    public static void main(String [] arg){
        if (arg.length > 0){

            for (int i = 0; i < arg.length; i++) {

                String x = arg[0];

                if (x.equalsIgnoreCase("oculto"))
                    _oculto = true;
                else if (x.startsWith("-backup=")){
                    _backup = x.substring(8);
                }
            }
        }
        new Horarios();
    }

    public Horarios() {
        FileReader fileIn = null;
        File file = new File(_fileNameTemp);

        Calendar cal = Calendar.getInstance();
        _horaInicial = cal.getTime().getTime();
        _nuevaLinea  = System.getProperty("line.separator");

        try {

            _annus = Integer.toString(cal.get(Calendar.YEAR));
            _mes = Integer.toString(cal.get(Calendar.MONTH) + 1); //Porque devuelve un número menos
            _numDia = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            int diaSem = cal.get(Calendar.DAY_OF_WEEK);
            if (_mes.length() == 1)
                _mes = "0" + _mes;
            if (_numDia.length() == 1)
                _numDia = "0" + _numDia;

            _fileMensual = _dir + _annus + "-" + _mes + ".txt";

            if (!file.exists()){//si no existe => empieza nuevo día
                file.createNewFile();
                OutputStreamWriter osw =
                    new OutputStreamWriter(new FileOutputStream(file));
                String t = Long.toString(_horaInicial);
                osw.write(t);
                osw.close();

                //archivo de log
                File file3 = new File(_fileNameLog);
                if (!file3.exists())
                    file3.createNewFile();
                FileOutputStream fos = new FileOutputStream(_fileNameLog, true);
                String fecha = _numDia + "/" + _mes + "/" + _annus +
                               " desde: " + t + _nuevaLinea;
                fos.write(fecha.getBytes());

                //guardo en el archivo mensual
                String nomDia = Fechas.getNombreDia(diaSem);
                nomDia = UtilString.llenar(nomDia, 10);
                String x = UtilString.llenar(nomDia + _numDia, 15);
                x = UtilString.llenar(x + Fechas.getHHMMSS(cal), 30);
                File f = new File(_fileMensual);
                if (!f.exists()){
                    x = ENCABEZADO + _nuevaLinea + x;
                    f.createNewFile();
                }
                agregarAlFinal(_fileMensual, x);
            }
            else{
                fileIn = new FileReader(file);
                LineNumberReader lin = new LineNumberReader(fileIn);
                String txt = lin.readLine();
                _horaInicial = Long.parseLong(txt.trim());
                lin.close();
            }

            jbInit();
            _thread = new Thread( this );
            _thread.start();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void run() {
        _enEjecucion = true;

        while( _enEjecucion )
            {
            try {
                esperar( _intervalo );
            } catch( InterruptedException e ) {
                logger.error(e.getMessage(), e);
                return;
            }
            actualizar();
        }
    }

    private void jbInit() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(_horaInicial));
        String horaActual = p.util.Fechas.getHHMMSS(cal);

        _lblDesde.setText("Desde");
        _lblTotal.setText("Total");
        _txtTotal.setText(_total);
        _txtDesde.setText(horaActual);
        _btn.setText(_btnTxt);
        _btn.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (e.isShiftDown() || e.isAltDown() || e.isControlDown())
                    mostrarDetalle();
                else
                    btmCerrarClic();
            }
        }
        );

        this.setTitle(p.util.Fechas.getFecha(cal));

        this.getContentPane().setLayout(gridBagLayout1);

        this.getContentPane().add(_txtDesde,  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 5, 0, 20), 20, 6));
        this.getContentPane().add(_lblDesde,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 10, 6, 10), 0, 0));
        this.getContentPane().add(_lblTotal,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 6, 10), 0, 0));
        this.getContentPane().add(_txtTotal,  new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 0, 20), 20, 5));
        this.getContentPane().add(_btn,  new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 3, 7, 7), 12, -4));

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        if (!_oculto){
            this.pack();
            this.setVisible(true);
        }
        else
            System.exit(0);    
    }

    private synchronized void esperar( int lapso )
        throws InterruptedException {
        this.wait( lapso );
    }

    private void actualizar(){
        if (!_pararThread){
            Calendar actual = Calendar.getInstance();
            _total = p.util.Fechas.getHHMMSS(actual.getTime().getTime() - _horaInicial);
            _txtTotal.setText(_total);
        }
    }

    private void btmCerrarClic()
    {
        Calendar cal = Calendar.getInstance();
        long miliseg = cal.getTime().getTime();
        _pararThread = true;

        String x = p.util.Fechas.getHHMMSS(cal);

        x = UtilString.llenar(x, 15);
        long difMiliseg = miliseg - _horaInicial;
        String total = p.util.Fechas.getHHMMSS(difMiliseg);

        x = UtilString.llenar(x + total , 30) + _nuevaLinea;
        try{
            agregarAlFinal(_fileMensual, x);

            //archivo log
            FileOutputStream fos = new FileOutputStream(_fileNameLog, true);
            String fecha = _numDia + "/" + _mes + "/" + _annus +
                           " hasta: " + miliseg + _nuevaLinea;
            fos.write(fecha.getBytes());
            fos.close();

            //borro el archivo temporal
            File file = new File(_fileNameTemp);
            file.delete();
        }catch(Exception exep){
            exep.printStackTrace();
        }               
        this.getContentPane().remove(_btn);
        this.pack();
        if (_backup != null){
            try {
                Backup.hacerBackup(_backup);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void agregarAlFinal(String fileName, String datos) throws Exception{
        //agregarAlFinal(fileName, datos, false);
        FileOutputStream fos = new FileOutputStream(fileName, true);
        fos.write(datos.getBytes());
        fos.close();
    }

//    private void agregarAlFinal(String fileName, String datos, boolean trampa){
//        Vector vec = new Vector();
//        int i = 0;
//        String línea = "";
//        try{
//            File file = new File(fileName);
//            if (!file.exists())
//                file.createNewFile();
//            FileReader fileIn = new FileReader(file);
//            LineNumberReader lin = new LineNumberReader(fileIn);
//            while (lin.ready()){
//                línea = lin.readLine();
//                if (línea != null && !línea.trim().equals("")){
//                    vec.add(i, línea);
//                    i++;
//                }
//            }
//
//            OutputStreamWriter osw = new OutputStreamWriter(
//                                        new FileOutputStream(
//                                            new File(fileName)));
//
//            int hasta  = vec.size();
//            if (trampa)
//                hasta = hasta - 1;
//            for (int j = 0 ; j < hasta ; j++){
//                String x = (String) vec.elementAt(j) + _nuevaLinea;
//                osw.write(x);
//            }
//            if (trampa)
//                datos = línea + datos;
//            osw.write(datos);
//            osw.close();
//        }catch(Exception e){
//            logger.error(e.getMessage(), e);
//        }
//    }

    private void mostrarDetalle(){
        try {
            JFileChooser chooser = new JFileChooser("C:\\");
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String txt = "";

                File file = chooser.getSelectedFile();
                LineNumberReader lnr = new LineNumberReader(new FileReader(file));
                lnr.readLine(); //salteo el ENCABEZADO

                int dias    = 0;
                int hor     = 0;
                int min     = 0;
                int seg     = 0;
                String linea;
                while (lnr.ready()){
                    linea = lnr.readLine();
                    if (linea != null && linea.length() > 46){
                        dias++;
                        hor += Integer.parseInt(linea.substring(45, 47));
                        min += Integer.parseInt(linea.substring(48, 50));
                        seg += Integer.parseInt(linea.substring(51, 53));
                    }
                }
                lnr.close();
                min += seg/60;
                seg = seg % 60;
                hor += min/60;
                min = min % 60;
                String prom = p.util.Fechas.promedio(hor, min, seg, dias);
                txt = "Cantidad de días:   " + dias + _nuevaLinea +
                      "Total de horas:     " +
                            p.util.Fechas.getHHMMSS(hor, min, seg) +
                            _nuevaLinea +
                      "Promedio:           " + prom;

                JTextArea   txtArea   = new JTextArea();
                txtArea.setText(txt);
                JFrame frame = new JFrame();
                frame.getContentPane().add(txtArea);
                frame.pack();
                frame.setVisible(true);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}