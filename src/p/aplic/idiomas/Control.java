package p.aplic.idiomas;

import p.util.Hora;

import javax.swing.*;

/**
 * User: Administrador
 * Date: 07/12/2006
 * Time: 21:00:12
 */
public class Control {
    private String raiz = "D:\\";
    private long intervalo = Hora.MS_EN_SEG * 2;

    static final String INGLES = "LAN01";
    static final String JAPONES = "LAN02";
    static final String FRANCES = "LAN03";
    static final String CASTELLANO = "LAN04";
    static final String ITALIANO = "LAN05";

    public static void main(String[] args) {
        PanelPrincipal pp = new PanelPrincipal();
        Icon i = new ImageIcon("D:\\P\\Fotos\\mdp\\_n\\2014-01-03_13 Juliana.jpg");
        pp.setTexto(i);
//        pp.setTexto(i2);
    }

}
