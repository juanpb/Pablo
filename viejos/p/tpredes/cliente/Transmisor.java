package p.tpredes.cliente;

import p.tpredes.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class Transmisor extends Thread{

    private Socket _socketInput;

    private Socket _socketOutput1;
    private Socket _socketOutput2;
    private Socket _socketOutput3;

    private int _port1 = 5001;
    private int _port2 = 5002;
    private int _port3 = 5003;

    private ObjectOutputStream _outputStrem1 = null;
    private ObjectOutputStream _outputStrem2 = null;
    private ObjectOutputStream _outputStrem3 = null;

    private String _host = "ARRIBA";

    private double _idTx;
    private int _offset = 0;

    public Transmisor(Socket s){
        _socketInput = s;
    }

    public void run(){
        init();
        ObjectInputStream in = null;
        try{
            if(true){//habría que hacer acá un buffer
                in = new ObjectInputStream(_socketInput.getInputStream());
                Object o = in.readObject();
                String str = (String)o;
                byte[] datos = str.getBytes();
                System.out.println("cant. de bytes a tx = " + datos.length);
                for (int i = 0; i < datos.length; i++) {
                    byte[] d = new byte[1];
                    d[0] = datos[i];
                    Mensaje msg = new Mensaje(_idTx, _offset++, d);
                    enviarMsg(msg);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void init(){
        try {
            _idTx = java.lang.Math.random();

            _socketOutput1 = new Socket(_host, _port1);
            _outputStrem1 = new ObjectOutputStream(
                _socketOutput1.getOutputStream());

            _socketOutput2 = new Socket(_host, _port2);
            _outputStrem2 = new ObjectOutputStream(
                _socketOutput2.getOutputStream());

            _socketOutput3 = new Socket(_host, _port3);
            _outputStrem3 = new ObjectOutputStream(
                _socketOutput3.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Acá se decide por qué conexión sale el msg
     */
    private void enviarMsg(Mensaje msg){
        String m = new String(msg.getDatos());
        try {
            long x = (msg.getOffeset() % 3);
            log("Enviando msg.'" + m + "' por la conexión " + (x + 1));
            if (x == 0){
                _outputStrem1.writeObject(msg);
            }
            else if (x == 1){
                _outputStrem2.writeObject(msg);
            }
            else{
                _outputStrem3.writeObject(msg);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void log(String x){
        System.out.println(x);
    }
}