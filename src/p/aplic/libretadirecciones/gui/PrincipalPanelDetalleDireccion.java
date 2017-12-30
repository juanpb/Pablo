package p.aplic.libretadirecciones.gui;

import p.aplic.libretadirecciones.bobj.Direccion;
import p.util.Constantes;
import p.util.UtilString;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: May 3, 2005
 * Time: 5:59:59 PM
 */
public class PrincipalPanelDetalleDireccion extends JPanel implements Constantes{
    private JTextArea _txt = new JTextArea();
    private JPanel _pnlCab = new JPanel(new BorderLayout());

    //Paneles usados para que deje un espacio
    private JPanel _pnlAuxI = new JPanel();
    private JPanel _pnlAuxD = new JPanel();

    private JLabel _lblNombre = new JLabel();
    private JLabel _lblCateg = new JLabel();


    public PrincipalPanelDetalleDireccion(){
        init();
    }

    private void init(){
        this.setLayout(new BorderLayout(5,5));
        setSizes();
        addComponentes();
        setPropiedades();
    }

    private void setPropiedades() {
        // Background de componentes
        //cabecera
        _pnlCab.setBackground(new Color(0, 0, 255));
        _lblNombre.setBackground(new Color(0, 0, 255));
        _lblNombre.setForeground(Color.white);
        _lblCateg.setBackground(new Color(0, 0, 255));
        _lblCateg.setForeground(Color.white);

        //detalles
        Color bg = _txt.getBackground();
        this.setBackground(bg);
        _pnlAuxD.setBackground(bg);
        _pnlAuxI.setBackground(bg);


        //Fonts
        Font font = new Font("Dialog", Font.ITALIC, 18);
        _lblNombre.setFont(font);

        _txt.setLineWrap(true);
        _txt.setEditable(false);
    }

    private void setSizes() {
        Dimension d = new Dimension(0,0);
        _pnlAuxI.setPreferredSize(d);
        _pnlAuxD.setPreferredSize(d);
    }

    private void addComponentes() {
        _pnlCab.add(_lblNombre, BorderLayout.CENTER);
        _pnlCab.add(_lblCateg, BorderLayout.SOUTH);

        add(_pnlCab, BorderLayout.NORTH);
        add(_txt, BorderLayout.CENTER);
        add(_pnlAuxI, BorderLayout.WEST);
        add(_pnlAuxD, BorderLayout.EAST);
    }

    public void setDireccion(Direccion d){
        if (d == null){
            _lblNombre.setText("");
            _lblCateg.setText("");
            _txt.setText("");
            return ;
        }
        String s = d.getNombre() + " " + d.getAppellido();
        s = UtilString.reemplazarTodo(s, "null", "");
        _lblNombre.setText(" " + s.trim());
        if (d.getCategorias() != null)
            _lblCateg.setText("  " + UtilString.getValoresSeparadosPorComa(d.getCategorias()));
        else
            _lblCateg.setText("");

        String txt = "";
        String tem = d.getTelCasa();
        if (tem != null && !tem.equals(""))
            txt+= "Casa: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        tem = d.getMovil();
        if (tem != null && !tem.equals(""))
            txt+= "Móvil: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        tem = d.getTelTrabajo();
        if (tem != null && !tem.equals(""))
            txt+= "Trabajo: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        tem = d.getCorreo();
        if (tem != null && !tem.equals(""))
            txt+= "Correo: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        tem = d.getDirec();
        if (tem != null && !tem.equals(""))
            txt+= "Dirección: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        tem = d.getNotas();
        if (tem != null && !tem.equals(""))
            txt+= "Notas: " + tem + NUEVA_LINEA + NUEVA_LINEA;

        _txt.setText(txt);

    }
}
