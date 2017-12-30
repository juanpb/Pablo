package p.tpredes;

import p.tpredes.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultipleStream {
    private static final int defaultBufferSize = 4096;
    private int _bufferSize;
    private Vector _outputs = new Vector();
    private byte[] _bufferOutput;
    private int _indice = 0;
    private int _numeroDeMsg = 0;
    private double _idTx = 0;

    public MultipleStream(String serverName, int[] puertos){
        this(serverName, puertos, defaultBufferSize);
    }
    public MultipleStream(String serverName, int[] puertos, int bufferSize){

        for (int i = 0; i < puertos.length; i++) {
            int port = puertos[i];
            try {
                Socket socket = new Socket(serverName, port);
                ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());
                _outputs.add(out);
            }
            catch (UnknownHostException ex) {
                System.out.println("No se puede encontrar el servidor = " +
                                   serverName);
                return;
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        _bufferOutput  = new byte[bufferSize];
        _bufferSize = bufferSize;

        System.out.println("Se estableció conexión con " + _outputs.size() +
                           " servidores.");

    }

    public void write(int c){
        byte[] b = new byte[1];
        b[0] = (byte)c;
        write(b, 0, 1);
    }

    public void write(byte[] b, int off, int len) {
        int libre = _bufferSize - _indice;
        int disponibles = len - off;
        if (libre > disponibles){
            for (int i = off; i < len; i++) {
                _bufferOutput[_indice++] = b[i];
            }
        }
        else{
            int cont = 0;
            for (int i = _indice; i < _bufferSize; i++) {
                _bufferOutput[_indice++] = b[off + cont];
                cont++;
            }
            escribir();
            _indice = 0;
            _bufferOutput = new byte[_bufferSize];

            //llamo recursivamente incrementando el offset
            write(b, off + cont, len);
        }
    }

    /**
     * No es necesario porque siempre que se pone algo un un output se hace flush()
     */
    public void flush(){
//        escribir();
//        for (int i = 0; i < _outputs.size(); i++) {
//            OutputStream os = (OutputStream) _outputs.elementAt(i);
//
//            try {
//                os.flush();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
    }

    public void close(){
        escribir();
        for (int i = 0; i < _outputs.size(); i++) {
            OutputStream os = (OutputStream) _outputs.elementAt(i);

            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Encapsula los bytes a enviar en la clase Mensaje y la envia por la
     * conexión que corresponda
     */
    private void escribir(){
        if (_numeroDeMsg == 0)
            _idTx = Math.random();

        //consigo la conexión por la que se enviará
        int numCon = _numeroDeMsg % _outputs.size();
        ObjectOutputStream out = (ObjectOutputStream) _outputs.elementAt(numCon);

        //armo el mensaje a enviar
        byte[] datos = null;
        if (_indice == _bufferOutput.length)
            datos = _bufferOutput;
        else{
            datos = new byte[_indice];
            for (int i = 0; i < _indice; i++) {
                datos[i] = _bufferOutput[i];
            }
        }
        Mensaje msg = new Mensaje(_idTx, _numeroDeMsg++, datos);
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}