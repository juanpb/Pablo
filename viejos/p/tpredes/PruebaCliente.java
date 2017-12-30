package p.tpredes;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class PruebaCliente extends JFrame implements ActionListener{

    public static final int PORT = 4992;
    BufferedOutputStream _outputStream = null;


    private Socket socket;
    int cont = 0;
    JTextField _txtIPServidor = new JTextField();
    JTextField _txtPort = new JTextField();
    JLabel _lblIPServidor = new JLabel();
    JLabel _lblPortServidor = new JLabel();
    JTextArea _txtMsg = new JTextArea();
    JButton _btnEnviar = new JButton();
    JButton _btnEnviarArchivo = new JButton();
    JButton _btnCerrarCon = new JButton();

    public PruebaCliente() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        PruebaCliente pruebaCliente1 = new PruebaCliente();
    }


    public void actionPerformed(ActionEvent e){
        String msg = "";
        try{
            if (_outputStream == null){
                String host = _txtIPServidor.getText();
                msg = _txtMsg.getText();
                int port = Integer.parseInt(_txtPort.getText()) ;
                System.out.println("Intentando conexión a " + host + ":" + port);
                socket = new Socket(host, port);
                socket.setTcpNoDelay(true);
                _outputStream = new BufferedOutputStream(socket.getOutputStream());

                System.out.println("conectado...");
            }

            byte[] leido = msg.getBytes();
            _outputStream.write(leido);

            _outputStream.flush();

            System.out.println("mensaje enviado de tam = " + leido.length);
            System.out.println("");


        }
        catch(IOException ex){
            ex.printStackTrace();

        }

    }


    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);

        _txtIPServidor.setBounds(new Rectangle(11, 15, 197, 27));
        _txtPort.setBounds(new Rectangle(216, 15, 138, 27));
        _lblIPServidor.setBounds(new Rectangle(11, 1, 197, 15));
        _lblPortServidor.setBounds(new Rectangle(216, 1, 138, 15));
        _txtMsg.setBounds(new Rectangle(12, 50, 344, 135));
        _btnEnviar.setBounds(new Rectangle(13, 193, 345, 30));
        _btnEnviarArchivo.setBounds(new Rectangle(13, 235, 191, 28));
        _btnCerrarCon.setBounds(new Rectangle(215, 235, 144, 28));
        this.setSize(400,300);
        this.setLocation(centrar(this.getSize()));

        _btnEnviarArchivo.setText("Enviar archivo");
        _txtIPServidor.setText("localhost");
        _txtPort.setText(Integer.toString(PORT));
        _lblIPServidor.setText("Dirección del servidor");
        _lblPortServidor.setText("Puerto del servidor");
        _txtMsg.setText("");
        _btnEnviar.setText("Enviar");
        _btnCerrarCon.setText("Cerrar conexión");


        this.getContentPane().add(_txtIPServidor, null);
        this.getContentPane().add(_lblIPServidor, null);
        this.getContentPane().add(_txtPort, null);
        this.getContentPane().add(_lblPortServidor, null);
        this.getContentPane().add(_txtMsg, null);
        this.getContentPane().add(_btnEnviar, null);
        this.getContentPane().add(_btnEnviarArchivo, null);
        this.getContentPane().add(_btnCerrarCon, null);

        //Pongo a escuchar eventos
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        _btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviar();
            }
        });

        _btnCerrarCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarConexion();
            }
        });

        _btnEnviarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarArchivo();
            }
        });


        this.setVisible(true);
    }

    void abrirConexion(){
        String host = _txtIPServidor.getText();
        int port = Integer.parseInt(_txtPort.getText());
        System.out.println("Intentando conexión a " + host + ":" + port);
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            _outputStream = new BufferedOutputStream(socket.getOutputStream());

            System.out.println("conectado...");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
/******************************************************************************/
//              EVENTOS
/******************************************************************************/
    void enviar() {
        try{
            if (_outputStream == null) {
                abrirConexion();
            }
            String msg = _txtMsg.getText();
            byte[] leido = msg.getBytes();
            _outputStream.write(leido);
            _outputStream.flush();

            System.out.println("mensaje enviado de tam = " + leido.length);
            System.out.println("");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void cerrarConexion(){
        try {
            if (_outputStream != null) {
                _outputStream.flush();
                _outputStream.close();
                _outputStream = null;
            }
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
    }

    void enviarArchivo() {
        JFileChooser chooser = null;
        File f = new File("C:\\");
        if (f.exists())
            chooser = new JFileChooser(f);
        else
            chooser = new JFileChooser();

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();
        int tam = (int)file.length();
        byte[] leido = new byte[tam];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(leido);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        if (_outputStream == null)
            abrirConexion();
        try {
            System.out.println("enviando msg...");
            _outputStream.write(leido);
            _outputStream.flush();
            System.out.println("listo");
            System.out.println("");
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }

    }

    public static Point centrar(Dimension tam){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(d.getWidth() - tam.getWidth())/2;
        int y = (int)(d.getHeight() - tam.getHeight())/2;
        return new Point(x, y);
    }
}