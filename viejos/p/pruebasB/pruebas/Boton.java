package p.pruebas;

import java.awt.*;
import javax.swing.*;

public class Boton extends JButton{

    int _ancho = 50;
    int _alto  = _ancho;

    private int _estado = 0;

    public static final int LIBRE = 0;
    public static final int OCUPADO = 1;
    public static final int SELECCIONADO = 2;
    public static final int NULL = 3;

    private static final String TEXTO_LIBRE = "( )";
    private static final String TEXTO_OCUPADO = "X";

    private static final Color COLOR_LIBRE = Color.yellow;
    private static final Color COLOR_OCUPADO = Color.red;
    private static final Color COLOR_SELECCIONADO = Color.pink;

    public Boton() {
        init();
    }

    public Boton(int estado) {
        init();
        setEstado(estado);
    }

    private void init(){
        Dimension dim = new Dimension(_ancho, _alto);
        setSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
    }

//    public void setText(String x){
//    }
//    public void setBounds(int a, int b, int c, int d){
//        super.setBounds(a, b, _ancho, _alto);
//    }

    public int getEstado(){
        return _estado;
    }


    public void setEstado(int estado){
        _estado = estado;
        if (estado == LIBRE){
            setText(TEXTO_LIBRE);
            setBackground(COLOR_LIBRE);
            setVisible(true);
        }else if (estado == OCUPADO){
            setText(TEXTO_OCUPADO);
            setBackground(COLOR_OCUPADO);
            setVisible(true);
        }else if (estado == SELECCIONADO){
            setBackground(COLOR_SELECCIONADO);
            setVisible(true);
        }else if (estado == NULL){
            setVisible(false);
        }
    }
}