package p.aplic.peliculas;


import p.util.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Administrador
 * Date: 03/02/2007
 * Time: 00:37:05
 */
public class PeliculaFrame extends JFrame {
    private PeliculaPanel pelPanel = null;
    private JPanel botonera = new JPanel(new GridBagLayout());
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JButton btnLimpiar;
    private JButton btnPanelAPortaPap;
    private Pelicula pelicula;
    private Pelicula peliOriginal;

    public PeliculaFrame(java.util.List<Pelicula.Estado> estados, java.util.List<Tag> tags, Integer contDVD){
        pelPanel = new PeliculaPanel(tags);
        init();
        pelPanel.setEstados(estados);
        pelPanel.setContadorDVD(contDVD);
        pack();
        GUI.maximizar(this);
        AbstractAction cancelAction = new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                cancelar();
            }
        };
        GUI.addCancelByEscapeKey(this, cancelAction);
        setVisible(true);
    }


    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula p) {
        if (p != null)
            peliOriginal = p.clonar();
        pelPanel.setPelicula(p);
        pelicula = p;
    }

    private void init() {
        setLayout(new BorderLayout());
        hacerBotonera();
        addListeners();
        add(pelPanel, BorderLayout.CENTER);
        add(botonera, BorderLayout.SOUTH);

        //si el tamaño es mayor que el monitor => achico
        pack();//para que calculo los tamaños
        Dimension dim = getPreferredSize();
        GUI.achicarSiEsNecesario(dim);

        setPreferredSize(dim);
    }

    private void hacerBotonera() {
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        btnLimpiar = new JButton("Limpiar");
        btnPanelAPortaPap = new JButton("Panel->PP");

        botonera.add(btnAceptar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,10,0,0), 0, 0));
        botonera.add(btnCancelar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,0,0,10), 0, 0));
        botonera.add(btnLimpiar, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,0,0,10), 0, 0));
        botonera.add(btnPanelAPortaPap, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,0,0,10), 0, 0));
    }

    private void addListeners() {
        getRootPane().setDefaultButton(btnAceptar);
        btnAceptar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });

        btnCancelar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        btnPanelAPortaPap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panelAPortaPap();
            }
        });
    }

    private void panelAPortaPap() {
        pelPanel.panelAPortaPap();
    }

    private void limpiar() {
        pelPanel.limpiar();
    }

    private void aceptar() {
        pelicula = pelPanel.getPelicula();

        cerrar();
    }

    private void cancelar() {
        pelicula = pelPanel.getPelicula();
        boolean b = estaVacia(pelicula);
        if (!pelicula.equals(peliOriginal) && !b){
            String msg = "¿Confirma cancelación?";
            int res = JOptionPane.showConfirmDialog(
                    this, msg,"",  JOptionPane.YES_NO_OPTION);
            if (res != JOptionPane.YES_OPTION)
                return ;
        }

        pelicula = null;
        cerrar();
    }

    private boolean estaVacia(Pelicula p){
        Pelicula pelVacia = new Pelicula();
        pelVacia.setEstado(Pelicula.Estado.BAJANDO);
        return p.equals(pelVacia);
    }

    private void cerrar() {
        dispose();
    }

    public boolean usóNuevoContadorDVD() {
        //pelicula == null si canceló
        return pelicula != null &&  pelPanel.usóNuevoContadorDVD();
    }
}
