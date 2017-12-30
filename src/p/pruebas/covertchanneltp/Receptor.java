package p.pruebas.covertchanneltp;

import p.util.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: JPB
 * Date: 08/05/17
 * Time: 12:42
 */
public class Receptor extends JFrame {
    private Queue<Character> cola = new ConcurrentLinkedQueue<Character> ();
    private File logFile = new File("D:\\temp\\cc.log");
    private FileOutputStream fos;
    private String binaryTmp = "";
    private int ultimaLineaLeida = 0;

    private JTextField txtInput = new JTextField();
    private long verLogPostTime;

    public static void main(String[] args) throws AWTException, IOException {
        new Receptor(args);
    }

    public Receptor(String[] args) throws AWTException, IOException {
        verLogPostTime = System.currentTimeMillis();
        if (args.length > 0){
            logFile =  new File(args[0]);
            System.out.println("args[0] = " + args[0]);
        }


        initGui();
        TimerTask task = new FileWatcher( logFile) {
            protected void onChange( File file ) {
                leer();
            }
        };

        Timer timer = new Timer();
        // repeat the check every second
        timer.schedule( task , 0, 500 );
    }

    private void initGui() {
        txtInput.setFont(Util.font);
        setTitle("Receptor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(txtInput, BorderLayout.CENTER);
        setSize(Util.dimension);

        txtInput.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    txtInput.setText("");
            }

        });

        GUI.centrar(this);
        setVisible(true);
    }

    private void leer() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");//2017/05/15 16:39:30:845
        List<String> archivoPorLinea;
        try {
            System.out.println("logFile.exists() = " + logFile.exists());
            if (!logFile.exists()){
                System.out.println("Aún no existe archivo de entrada");
                return ;
            }
            long horaLog=0;
            archivoPorLinea = Util.getArchivoPorLinea(logFile); // acá ya vienen solo las líneas con data
            int contLinea = 0;
            for (String s : archivoPorLinea) {
                String hora = s.substring(0, 23);
                horaLog = formatter.parse(hora).getTime();
                //if (horaLog > verLogPostTime){
                if (contLinea >= ultimaLineaLeida){
                    char c = s.charAt(22);
                    int i = c % 2;
                    push(i) ;
                }
                contLinea++;
            }
            ultimaLineaLeida = contLinea;

            verLogPostTime = horaLog;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void push(int i) {
        binaryTmp = binaryTmp + i;
        if (binaryTmp.length() == 8){
            txtInput.setText(txtInput.getText() + Util.binary2ascii(binaryTmp));
            binaryTmp = "";
        }
    }
}
