package p.aplic;

import javax.swing.*;
import java.io.File;

/**
 * User: JPB
 * Date: Mar 23, 2009
 * Time: 11:04:04 AM
 */
public class ListarDiscos {

    public static void main(String[] args) {
        // "M:\\Discos.txt"
        if (args.length != 2){
            System.out.println("Mal los parámetros.");
            System.out.println("Modo de uso: p.aplic.ListarDiscos [directorio raíz] [archivo salida]");
            System.exit(0);
        }
        String raiz = args[0];
        String salida = args[1];
        File fr = new File(raiz);
        if (!fr.exists()){
            raiz = JOptionPane.showInputDialog(null, "Seleccionar directorio raíz ", raiz);
            salida = JOptionPane.showInputDialog(null, "Seleccionar archivo salida ", salida);
        }
        //creo el archivo con los discos
        File f = new File (salida);
        try {
            f.createNewFile();
            ListaCompleta lc = new ListaCompleta();
            lc.discos(f,raiz);
            System.out.println("Generado: " + salida);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
