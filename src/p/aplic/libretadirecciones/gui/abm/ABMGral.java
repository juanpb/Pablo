package p.aplic.libretadirecciones.gui.abm;

import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.model.Model;
import p.gui.ComboItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: May 4, 2005
 * Time: 6:58:51 PM
 */
public class ABMGral extends JDialog{
    private JTabbedPane tabPane = new JTabbedPane();
    private TabABMGral tabGral = new TabABMGral();
    private TabNotas tabNotas = new TabNotas();

    private JCheckBox chkHistorico = new JCheckBox("Histórico");
    private JLabel lblCateg = new JLabel("Categoría");
    private JComboBox cmbCateg = new JComboBox();
    private JList lstCategIncluidas = new JList(new DefaultListModel());
    private JScrollPane scrollerCat = new JScrollPane(lstCategIncluidas);
    private JButton btnAgregarCat = new JButton("+");
    private JButton btnSacarCat = new JButton("-");
    private JButton btnAceptar = new JButton("Aceptar");
    private JButton btnCancelar = new JButton("Cancelar");
    private Direccion dir;

    public ABMGral() {
        this(null);
    }

    public ABMGral(Direccion dir) {
        super();
        init();
        if (dir != null){
            setDireccion(dir);
        }
    }

    private void agregarCategoria() {
        Object o = cmbCateg.getSelectedItem();
        if (o != null){
            ComboItem ci = (ComboItem)o;
            cmbCateg.removeItem(ci);

            ((DefaultListModel)lstCategIncluidas.getModel()).addElement(ci.getValor());
        }
    }
    private void sacarCategorias() {
        List selectedValuesList = lstCategIncluidas.getSelectedValuesList();
        for(Object o : selectedValuesList) {
            if (o != null){
                String v = (String)o;
                ComboItem ci = new ComboItem(v, v);
                cmbCateg.addItem(ci);
                ((DefaultListModel)lstCategIncluidas.getModel()).removeElement(v);
            }
        }
    }

    private void agregarCategoria(String categoria) {
        ComboItem ci = new ComboItem(categoria,  categoria);
        cmbCateg.removeItem(ci);

        ((DefaultListModel)lstCategIncluidas.getModel()).addElement(categoria);
    }

    private void setCategorias(List<String> categoria) {
        if (categoria != null){
            for(String c : categoria) {
                agregarCategoria(c);
            }
        }
    }

    private List<String> getCategorias() {
        List<String> res = new ArrayList<String>();
        DefaultListModel m = (DefaultListModel) lstCategIncluidas.getModel();
        for(int i = 0; i < m.getSize(); i++) {
            res.add((String) m.get(i));
        }
        return res;
    }


    public void setDireccion(Direccion dir){
        this.dir = dir;
        tabGral.setDireccion(dir);
        setCategorias(dir.getCategorias());
        tabNotas.setDireccion(dir);
        chkHistorico.setSelected(dir.isHistorico());
    }

    /**
     * Devuelve una nueva dirección
     * @return
     */
    public Direccion getDireccion(){
        return dir;
    }

    private void init() {
        getContentPane().setLayout(null);
        addComponentes();
        setSizes();
        addListeners();
        cargarCombo();
    }

    private void cargarCombo(){
        List<String> categorias = Model.getCategorias();
        for (String c : categorias) {
            ComboItem ci = new ComboItem(c, c);
            cmbCateg.addItem(ci);
        }
    }

    private void addListeners() {
        getRootPane().setDefaultButton(btnAceptar);
        addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent e) {
                dir = null;
             }
        });

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

        btnAgregarCat.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                agregarCategoria();
            }
        });

        btnSacarCat.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                sacarCategorias();
            }
        });

        lstCategIncluidas.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_DELETE){
                    sacarCategorias();
                }
            }
        });
    }                                       

    private void aceptar() {
        dir = tabGral.getDireccion();
        dir.setNotas(tabNotas.getNotas());
        dir.setCategorias(getCategorias());
        dir.setHistorico(chkHistorico.isSelected());
        cerrar();
    }

    private void cancelar() {
        dir = null;
        cerrar();
    }

    private void cerrar() {
        this.dispose();
    }

    private void addComponentes() {
        tabPane.addTab("General", tabGral);
        tabPane.addTab("Notas", tabNotas);
        getContentPane().add(btnAceptar);
        getContentPane().add(btnCancelar);
        getContentPane().add(lblCateg);
        getContentPane().add(cmbCateg);
        getContentPane().add(chkHistorico);
        getContentPane().add(tabPane);
        getContentPane().add(scrollerCat);
        getContentPane().add(btnAgregarCat);
        getContentPane().add(btnSacarCat);
    }

    private void setSizes() {
        tabPane.setBounds(10, 10, 500, 400);
        int x = tabPane.getX() + tabPane.getWidth() + 15;
        int y = 50;
        btnAceptar.setBounds(x, y, 120, 20);
        y +=30;
        btnCancelar.setBounds(x, y, 120, 20);

        y +=60;
        lblCateg.setBounds(x, y, 120, 20);
        y +=21;
        cmbCateg.setBounds(x, y, 120, 20);
        y +=30;
        btnAgregarCat.setMargin(new Insets(0,0,0,0));
        btnSacarCat.setMargin(new Insets(0,0,0,0));
        btnAgregarCat.setBounds(x+30, y, 20, 20);
        btnSacarCat.setBounds(x+70, y, 20, 20);
        y += 25;
        scrollerCat.setBounds(x, y, 120, 100);
        y += 130;

        chkHistorico.setBounds(x, y, 120, 20);

        this.setSize(660, 450);
    }
}

