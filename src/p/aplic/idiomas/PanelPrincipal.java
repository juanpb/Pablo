package p.aplic.idiomas;

import javax.swing.*;
import java.awt.*;

/**
 * User: Administrador
 * Date: 07/12/2006
 * Time: 20:48:57
 */
public class PanelPrincipal extends JFrame {

    JLabel imagen = new JLabel();
    JLabel texto = new JLabel();


    public PanelPrincipal(){
        setLayout(new BorderLayout());
        add(imagen,  BorderLayout.NORTH);
        add(texto,  BorderLayout.CENTER);
        setVisible(true);
    }

    public void setImagen(Icon i){
        imagen.setIcon(i);
    }

    public void setTexto(Icon i){
        texto.setIcon(i);
    }
}
