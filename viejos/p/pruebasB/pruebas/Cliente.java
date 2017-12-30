package p.pruebas;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class Cliente{

    public static final int PORT = 5001;
    public static final String IP = "192.168.0.1";
    private Socket socket;

    public static void main(String[] arg){
        Cliente rc = new Cliente();
    }

    public Cliente(){
        conectarse();
    }



    public void conectarse(){
        String command = "el comando";
System.out.println("A");
        try{
            socket = new Socket("ARRIBA"/*IP*/, PORT);
System.out.println("B");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
System.out.println("C");
            out.writeObject(command);
System.out.println("D");
            socket.close();
System.out.println("E");
        }
        catch(IOException ex){
            ex.printStackTrace();

        }
    }
}