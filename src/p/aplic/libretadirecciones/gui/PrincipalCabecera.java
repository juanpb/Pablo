package p.aplic.libretadirecciones.gui;

import p.aplic.libretadirecciones.model.Model;
import p.gui.ComboItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * User: JPB
 * Date: May 10, 2005
 * Time: 2:39:39 PM
 */
public class PrincipalCabecera extends JPanel implements KeyListener {

    private JComboBox cmbCategorias = new JComboBox();
    private JLabel lblCat = new JLabel("Categoría:");

    private JLabel lblBuscar = new JLabel("Buscar:");
    private JTextField txtBuscar = new JTextField();

    private JCheckBox chkVerHistoricos = new JCheckBox("Ver históricos");
    private JButton btnNuevo = new JButton("Nuevo...");
    private Principal control = null;

    public PrincipalCabecera(Principal p) {
        control = p;
        init();
    }

    private void init() {
        setLayout(null);
        setSizes();
        addComponentes();
        addListeners();
        cargarDatos();
    }

    public void setVerHistoricos(boolean ver){
        chkVerHistoricos.setSelected(ver);
    }
    
    public String getCategoria(){
        ComboItem ci = (ComboItem) cmbCategorias.getSelectedItem();
        return (String)ci.getClave();
    }

    public boolean isHistorico(){
        return chkVerHistoricos.isSelected();
    }
    
    private void addListeners() {
        btnNuevo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });

        cmbCategorias.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cambioCategoria();
            }
        });
        chkVerHistoricos.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cambioHistorico();
            }
        });
    }

    private void cambioHistorico() {
        control.cambioHistorico();
    }
    private void cambioCategoria() {
        control.cambioCategoria();
    }

    private void nuevo() {
        control.nuevo();
    }

    private void addComponentes() {
        add(lblCat);
        add(cmbCategorias);
        add(lblBuscar);
        add(txtBuscar);
        add(btnNuevo);
        add(chkVerHistoricos);
    }

    private void cargarDatos() {
        cmbCategorias.addItem(new ComboItem("Todas","Todas"));
        List<String> cat = Model.getCategorias();
        for (String c : cat) {
            ComboItem ci = new ComboItem(c, c);
            cmbCategorias.addItem(ci);
        }
    }

    private void setSizes() {
        int x = 10;
        int y = 10;
        int alt = 20;

        lblCat.setBounds(x, y, 70, alt);
        int x2 = lblCat.getX() + +lblCat.getWidth() + 5;
        cmbCategorias.setBounds(x2 , y, 200, alt);

        x2 = 350;
        lblBuscar.setBounds(x2, y, 60, alt);
        x2 += lblBuscar.getWidth() + 5;
        txtBuscar.setBounds(x2, y, 110, alt);
        x2 += txtBuscar.getWidth() + 10;
        btnNuevo.setBounds(x2, y, 110, alt);
        x2 += btnNuevo.getWidth() + 10;
        chkVerHistoricos.setBounds(x2, y, 110, alt);

        setPreferredSize(new Dimension(400,40));
    }

    public void keyTyped(KeyEvent e) {
        System.out.println("e.getKeyChar() = " + e.getKeyChar());
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
