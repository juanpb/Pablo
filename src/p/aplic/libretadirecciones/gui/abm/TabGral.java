package p.aplic.libretadirecciones.gui.abm;

import p.aplic.libretadirecciones.bobj.Direccion;
import p.gui.TextField;

import javax.swing.*;

/**
 * User: JPB
 * Date: May 4, 2005
 * Time: 6:38:20 PM
 */
public class TabGral extends JPanel{
    private JLabel _lblNombre = new JLabel("Nombre:");
    private TextField _txtNombre = new TextField();

    private JLabel _lblApellido = new JLabel("Apellido:");
    private TextField _txtApellido = new TextField();

    private JLabel _lblTrabajo = new JLabel("Trabajo:");
    private TextField _txtTrabajo = new TextField();

    private JLabel _lblCasa = new JLabel("Casa:");
    private TextField _txtCasa = new TextField();

    private JLabel _lblMovil = new JLabel("Móvil:");
    private TextField _txtMovil = new TextField();

    private JLabel _lblCorreo = new JLabel("Correo:");
    private TextField _txtCorreo = new TextField();

    private JLabel _lblDireccion = new JLabel("Dirección:");
    private TextField _txtDireccion = new TextField();
    private Direccion _dir;


    public TabGral() {
        init();
    }

    private void init() {
        setLayout(null);
        setBounds();
        addComponentes();
//        setPropiedades();
//        addListeners();
//        setVisible(true);
    }

    private void setBounds(){
        int x = 10;
        int alt = 20;
        int sepX = 20;
        int y = 10;
        int anL = 100;
        int anT = 300;
        int x2 = x + anL + 10;

        _lblNombre.setBounds(x, y, anL, alt);
        _txtNombre.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblApellido.setBounds(x, y, anL, alt);
        _txtApellido.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblCasa.setBounds(x, y, anL, alt);
        _txtCasa.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblMovil.setBounds(x, y, anL, alt);
        _txtMovil.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblTrabajo.setBounds(x, y, anL, alt);
        _txtTrabajo.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblCorreo.setBounds(x, y, anL, alt);
        _txtCorreo.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        _lblDireccion.setBounds(x, y, anL, alt);
        _txtDireccion.setBounds(x2, y, anT, alt);
        y += alt + sepX;

        int i = _txtDireccion.getX() + _txtDireccion.getWidth() + 20;
        int ii = _txtDireccion.getY() + _txtDireccion.getHeight() + 20;
        setSize(i, ii);
    }

    private void addComponentes() {
        add(_lblNombre);
        add(_txtNombre);
        add(_lblApellido);
        add(_txtApellido);
        add(_lblCasa);
        add(_txtCasa);
        add(_lblMovil);
        add(_txtMovil);
        add(_lblTrabajo);
        add(_txtTrabajo);
        add(_lblCorreo);
        add(_txtCorreo);
        add(_lblDireccion);
        add(_txtDireccion);
    }

    public void setDireccion(Direccion d){
        _dir = d;
        _txtNombre.setText(d.getNombre());
        _txtApellido.setText(d.getAppellido());
        _txtCasa.setText(d.getTelCasa());
        _txtTrabajo.setText(d.getTelTrabajo());
        _txtMovil.setText(d.getMovil());
        _txtCorreo.setText(d.getCorreo());
        _txtDireccion.setText(d.getDirec());
    }

    /**
     * Devuelve una nueva dirección
     * @return
     */
    public Direccion getDireccion(){
        _dir = new Direccion();

        _dir.setNombre(_txtNombre.getText());
        _dir.setApellido(_txtApellido.getText());
        _dir.setTelCasa(_txtCasa.getText());
        _dir.setTelTrabajo(_txtTrabajo.getText());
        _dir.setMovil(_txtMovil.getText());
        _dir.setCorreo(_txtCorreo.getText());
        _dir.setDirec(_txtDireccion.getText());
        
        return _dir;
    }
}
