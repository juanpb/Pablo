package p.tpredes;

import p.tpredes.*;

import nt.util.*;


import java.net.*;
import java.util.*;
import java.io.*;

public class Servidor {

    private Vector _receptores = new Vector();
    static int[] _ports = null;
    private static String _archivoSalidaValor = null;
    private static String _pathSalidaDefault = "C:\\salida.txt";

    public static void main(String[] args) {
        //Caso en el que no se especifica el archivo de entrada
        if (args.length < 1) {
            System.out.println("Modo de uso: Servidor iniFilename");
            System.exit(0);
        }

        String filename = args[0];

        //Se abre el archivo de configuracion
        TagFile tg = new TagFile(filename);

        String baseServerPort = "ServerPort";
        String baseArchivoSalida = "ArchivoSalida";

        int i = 0;
        String serverPort;
        String archivoSalida;
        //Recorro el archivo de configuración
        serverPort = baseServerPort + i;
        archivoSalida = baseArchivoSalida + i;

        _archivoSalidaValor = tg.readTag(archivoSalida);
        String puertos = tg.readTag(serverPort);
        if (puertos != null) {
            StringTokenizer st = new StringTokenizer(puertos, ",");
            int cant = st.countTokens();
            _ports = new int[cant];
            int j = 0;
            while (st.hasMoreTokens()) {
                String t = st.nextToken().trim();
                _ports[j++] = Integer.parseInt(t);
            }
        }
        Servidor servidor1 = new Servidor();
    }

    public Servidor() {
        int i = 0;
        try {
            for (i = 0; i < _ports.length; i++) {
                ServerSocket ss = new ServerSocket(_ports[i]);
                Receptor rec = null;
                if (i == 0)
                    rec = new Receptor(ss, this);
                else
                    rec = new Receptor(ss);
                _receptores.add(rec);
                rec.start();
            }
        }
        catch (BindException bex){
            System.out.println("problemas con el puerto = " + _ports[i]);
            System.out.println(bex.getMessage());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fin(){
        unirMensajes();
    }
    /**
     * Se asume que los mensajes son de 1 byte
     */
    public void unirMensajes(){
        if (_archivoSalidaValor == null)
            _archivoSalidaValor = _pathSalidaDefault;
        File file = new File(_archivoSalidaValor);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int cont = 0;
            int offsetMsg = 0;
            boolean seguir = true;
            while(seguir) {
                //voy pidiendo el msg i-ésimo de cada receptor y lo mando al archivo
                for (int i = 0; i < _receptores.size(); i++) {
                    Receptor rec = (Receptor) _receptores.get(i);
                    List msgs = rec.getMensajes();
                    if (msgs.size() <=  cont){
                        seguir = false;
                        break;
                    }
                    Mensaje msg = (Mensaje)msgs.get(cont);
                    /*debug*/
                    if (offsetMsg != msg.getOffeset())
                        System.out.println("Se esperaba el msg " + offsetMsg +
                                           " y llegó el " + msg.getOffeset());
                    offsetMsg++;
                    /*fin debug*/
                    bos.write(msg.getDatos());
                }
                cont++;
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally{
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex1) {
            }
        }
    }

    private void log(String x){
        System.out.println(x);
    }
}