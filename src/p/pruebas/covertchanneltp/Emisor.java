package p.pruebas.covertchanneltp;

import p.util.GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: JPB
 * Date: 08/05/17
 * Time: 12:42
 */
public class Emisor  extends JFrame {
    private Queue<Character> cola = new ConcurrentLinkedQueue<Character> ();
    private File logFile = new File("D:\\temp\\cc.log");
    private FileOutputStream fos;
    int INTERVALO_DE_LOGUEO = 53;

    private JTextField txtInput = new JTextField();
    private Checkbox loguearChk = new Checkbox("Loguear");

    private long horaAnterior = 0;
    private Robot robot;

    public static void main(String[] args) throws AWTException, IOException {
        new Emisor(args);

    }

    public Emisor(String[] args) throws AWTException, IOException {
        boolean loguear = true;
        if (args.length > 0){
            String param = args[0];
            if ("-loguear".equals(param.toLowerCase()))
                loguear = true;
            else if ("-nologuear".equals(param.toLowerCase()))
                loguear = false;

        }

        if (args.length > 1){
            logFile =  new File(args[1]);
            System.out.println("args[1] = " + args[1]);
        }


        loguearChk.setState(loguear);

        addListeners();
        initGui();
        initArchivoLog();
        generarLog();
    }

    private void generarLog() {
        TimerTask task = new TimerTask( ) {
            @Override
            public void run() {
                try {
                    robot = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                }

                Point loc = MouseInfo.getPointerInfo().getLocation();
                loguearPosicionMouse((int)loc.getX(), (int)loc.getY());
            }
        };

        java.util.Timer timer = new java.util.Timer();
        // repeat the check every second
        timer.schedule( task , 0, INTERVALO_DE_LOGUEO);
    }

    private void loguearPosicionMouse (int x, int y){
        log("posición: x = " + x + ", y = " + y);
    }

    private void addListeners() {
        txtInput.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    txtInput.setText("");
            }

        });

        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                System.out.println("changedUpdate(); = ");
            }

            public void insertUpdate(DocumentEvent e) {
                String text = txtInput.getText();
                if (text.length() > 0){
                    String elChar = text.charAt(text.length()-1) + "";
                    mandarPorCC(elChar);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

        });
    }

    private void initArchivoLog() throws IOException {
        //Si el archivo destino no existe => lo crea
        if (!logFile.exists())
            logFile.createNewFile();
        else {//borra lo viejo
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
            bw.write("");
            bw.close();
        }
    }

    private void initGui() {
        txtInput.setFont(Util.font);
        setTitle("Emisor");
        txtInput.setSize(20,20);
        loguearChk.setSize(20,20);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(txtInput, BorderLayout.CENTER);
        getContentPane().add(loguearChk, BorderLayout.EAST);
        //getContentPane().add(btn, BorderLayout.CENTER);
        setSize(Util.dimension);
        GUI.centrar(this);
        setVisible(true);
    }

    private void mandarPorCC(String text) {
        for (byte b : text.getBytes()) {
            String s = Util.char2binary((char) b);
            if (s.length() == 16)
                s = s.substring(8, 16); //corto ya que a los á, é, ñ, etc le mete un byte todo con 1s
            System.out.println("se manda = " + s);
            for (int i = 0; i <s.length(); i++) {
                cola.add(s.charAt(i));
//                System.out.print(s.charAt(i));
            }
//            System.out.println("");
        }
    }

    private synchronized void log(String s)  {
        if (!loguearChk.getState())
            return ;
        try {
            fos = new FileOutputStream(logFile, true);

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date today = Calendar.getInstance().getTime();

            if (!cola.isEmpty()){
                //acá se manda la data
                String bit = cola.remove().toString();
                setBit(bit, today);
                s += "\t";
            }
            //s = df.format(today) + ": " + s + "\n";
            s = df.format(today) + " [Info] " + getClass().getCanonicalName() +": " + s + "\n";
            fos.write(s.getBytes());
            System.out.print(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    /**
     *
     * @param bit puede ser "0" o "1", es el valor que se quiere enviar en el segundo parámetro.
     * @param today es el parámetro de entrada/salida, que, si es necesario, se modifica para que los
     *              milisegundos de esta fecha sean un número par o impar, si el parámetro
     *              bit es igual a "0" o "1" respectivamente
     */
    private void setBit(String bit, Date today) {
        long time = today.getTime(); //Returns the number of milliseconds since 1/1/1970, 00:00:00 GMT
        long resto = time % 2; //acá queda el resto de los ms módulo 2

        if (("0".equals(bit) && resto == 1) //si el bit = 0, pero los ms son impar
                || ("1".equals(bit) && resto == 0)  ){//o si el bit = 1, pero los ms son par
            today.setTime(time+1);//incrementa la hora en un milisegundo
        }
    }
}
