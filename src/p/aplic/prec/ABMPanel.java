package p.aplic.prec;

import org.apache.log4j.Logger;
import p.aplic.libretadirecciones.gui.TablaRenderer;
import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.gui.ComboBox;
import p.gui.ComboItem;
import p.gui.LabelTextField;
import p.gui.ListFiltered;
import p.util.Fechas;
import p.util.GUI;
import p.util.SVN;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: Apr 16, 2008
 * Time: 7:13:52 PM
 */
public class ABMPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(ABMPanel.class);

    private LabelTextField txtPrecio = new LabelTextField("Precio");
    private ComboBox cmbSuper = new ComboBox("Super");
    private ListFiltered lstProd = new ListFiltered ("Producto", true);
    private ComboBox cmbFecha = new ComboBox("Fecha");
    private JButton btnGrabar = new JButton("Grabar");
    private JButton btnGrabarYGS = new JButton("Grabar y GS");
    private JButton btnBorrar = new JButton("-");
    private JButton btnGenerarSalida = new JButton("Generar salida");
    private JButton btnModifProd = new JButton("M");
    private JButton btnAltaProd = new JButton("A");

    private ABMTableModel tablaModel = new ABMTableModel();
    
    private JTable  tabla = new JTable(tablaModel);
    private JTable  tablaHist = new JTable();
    private Map<ProductoDO, ABMTableModelHist> cachePPP =
            new HashMap<ProductoDO, ABMTableModelHist>();

    private JScrollPane scrollTable = new JScrollPane(tabla,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private ABMProducto abmProd;

    private JTabbedPane tabbedPane = new JTabbedPane();

    public ABMPanel() throws IOException {
        init();
    }

    private void init() throws IOException {
        setLayout(null);

        int w = 260;
        int h =  40;
        int x1 = 10;
        int sepHor = 30;
        int x2 = x1 + w + sepHor;
        int y = 10;
        int sep = 20;
        int altProds = 190;
        int altTabla = 200;


        cmbFecha.setBounds(x1, y, w, h);
        cmbSuper.setBounds(x2, y, w, h);
        y += h + sep;
        lstProd.setBounds(x1, y, w, altProds);
        btnAltaProd.setBounds(lstProd.getX()+ lstProd.getWidth() + 2,
                lstProd.getY() + lstProd.getHeight() / 2 -25,
                50, 20);
        btnModifProd.setBounds(lstProd.getX()+ lstProd.getWidth() + 2,
                lstProd.getY() + lstProd.getHeight() / 2 +5, 
                50, 20);
        txtPrecio.setBounds(x2, y, w, h);
        y += altProds + sep*2;

        tabbedPane.setBounds(x1, y, w*2+sepHor, altTabla+10);

        y += altTabla + sep;
        btnGrabar.setBounds(x1 + 50, y, 150, 20);
        btnGrabarYGS.setBounds(btnGrabar.getX() + btnGrabar.getWidth() + sep, y, 150, 20);
        btnGenerarSalida.setBounds(btnGrabarYGS.getX() + btnGrabarYGS.getWidth() + sep,
                y, 150, 20);

        add(cmbFecha);
        add(cmbSuper);
        add(lstProd);
        add(txtPrecio);
        tabbedPane.add(getPanelTabNuevos(), "Nuevos");
        tabbedPane.add(getPanelTabHistoricos(), "Viejos");
        add(tabbedPane);
        add(btnGrabar);
        add(btnGrabarYGS);
        add(btnGenerarSalida);
        add(btnAltaProd);
        add(btnModifProd);


        setPreferredSize(new Dimension(500, 900));

        cmbFecha.setEditable(true);

        cargarCombos();
        addListeners();
        TablaRenderer render = new TablaRenderer();
        tabla.setDefaultRenderer(Object.class, render);
    }

    private Component getPanelTabNuevos() {
        JPanel panel = new JPanel(new GridBagLayout());
        btnBorrar.setMargin(new Insets(0,5,0,5));

        panel.add(scrollTable, new GridBagConstraints(0, 0, 1,1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 500, 300 ));
        panel.add(btnBorrar, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0,0,0,0), 0, 0 ));

        return panel;
    }

    private Component getPanelTabHistoricos() {
        JPanel panel = new JPanel(new GridBagLayout());
        JScrollPane st = new JScrollPane(tablaHist,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        panel.add(st, new GridBagConstraints(0, 0, 1,1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0 ));

        return panel;
    }

    private void cambioProducto() {
        ProductoDO p = (ProductoDO) lstProd.getSelectedValue();
        if (p != null){
            ABMTableModelHist model = getModeloPorProd(p);
            tablaHist.setModel(model);
        }
    }

    private ABMTableModelHist getModeloPorProd(ProductoDO p){
        if (p == null)
            return null;

        ABMTableModelHist model = cachePPP.get(p);
        if (model == null){
            model = new ABMTableModelHist(p);
            cachePPP.put(p, model);
        }
        return model;
    }

    private void addListeners() {
        lstProd.addDobleClicListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                cambioProducto();
                modifProd();
            }
        });
        lstProd.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                cambioProducto();
            }
        });

        txtPrecio.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 agregarPrecio();
             }
        });

        btnGrabar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabarPrecios();
            }
        });
        btnGrabarYGS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabarPrecios();
                generarSalida();
            }
        });

        btnGenerarSalida.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 generarSalida();
             }
        });

        btnBorrar.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 int i = tabla.getSelectedRow();
                 if (i >= 0){
                     tablaModel.removeFila(i);
                 }
             }
        });

        btnAltaProd.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 altaProd();
             }
        });

        btnModifProd.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 modifProd();
             }
        });
    }

    private void generarSalida() {
        try {
            String s = Model.generarSalida();
            String msg = "Se ha generado el archivo de salida: " + s;
            JOptionPane.showMessageDialog(null, msg);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }
    }

    private void modifProd(){
        ProductoDO p = (ProductoDO)lstProd.getSelectedValue();
        final int selInd = lstProd.getSelectedIndex();
        if ( p != null){

            abmProd = new ABMProducto(p);

            abmProd.addWindowListener(new WindowAdapter(){
                public void windowClosed(WindowEvent e) {
                    modificarProducto(abmProd.getProducto());
                    lstProd.setSelectedIndex(selInd);
                    abmProd.setProducto(null);//parche xq windowClosed se llama dos veces
                    txtPrecio.requestFocus();
                }
            });

            GUI.centrar(abmProd);
            abmProd.setVisible(true);
        }
    ;}


    private void altaProd(){
        int pdId = Model.getIdProductoNuevo();
        ProductoDO p = new ProductoDO(pdId);
        abmProd = new ABMProducto(p);
        abmProd.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e) {
                agregarProducto(abmProd.getProducto());
                abmProd.setProducto(null);//parche xq windowClosed se llama dos veces
            }
        });
        GUI.centrar(abmProd);
        abmProd.setVisible(true);
    }

    private void modificarProducto(ProductoDO p){
        if (p == null)
            return;
        try {
            Model.modificarProducto(p);
            cargarComboProductos();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void agregarProducto(ProductoDO p){
        if (p == null)
            return;
        try {
            Model.agregarProducto(p);
            cargarComboProductos();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void grabarPrecios() {
        List<PrecioDO> list = tablaModel.getFilas();
        try {
            Model.agregarPrecios(list);

            //limpio la tabla
            tablaModel.clear();

            commit();
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }
    }

    //hace commit del directorio
    private void commit() throws Exception {
        String prodsPath = System.getProperty("archivoProductos");
        File f = new File(prodsPath);
        SVN.commit(f.getParent());
    }

    private void agregarPrecio() {
        Date d = getFecha();
        ProductoDO prod = (ProductoDO) lstProd.getSelectedValue();
        SuperDO su = (SuperDO) cmbSuper.getSelectedItem();
        Double pr = getPrecio();
        if (pr != null && d != null && prod != null && su != null){
            PrecioDO precioDO = new PrecioDO(prod, pr, su, d);
            tablaModel.addFila(precioDO);

            //limpio
            txtPrecio.setText("");
            lstProd.setText("");
        }
    }

    private Date getFecha() {
        Date res=null;
        Object o = cmbFecha.getSelectedItem();
        if (o != null){
            String s = o.toString();
            if (s.length()>= 10){
                res = Fechas.getDateAAAA_MM_DD(s);
            }
        }
        return res;
    }

    private Double getPrecio() {
        try {
            String t = txtPrecio.getText();
            t = t.replaceAll(",", ".");
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void cargarCombos() throws IOException {
        //combo super
        java.util.List<SuperDO> list = Model.getSupers();
        cmbSuper.addItems(list);

        //combo prods
        cargarComboProductos();

        //combo fechas
        int ciclar = 5;
        if (ciclar > 0){
            String f = Fechas.getAAAAMMDD("-");
            f += " (hoy)";
            ComboItem item = new ComboItem(Integer.toString(0), f);
            cmbFecha.addItem(item);
        }
        if (ciclar > 1){
            String f = Fechas.getAAAAMMDD(-1 ,"-");
            f += " (ayer)";
            ComboItem item = new ComboItem(Integer.toString(1), f);
            cmbFecha.addItem(item);
        }
        for (int i = 2; i < ciclar; i++){
            String f = Fechas.getAAAAMMDDDiaSemana(-i, "-");
            ComboItem item = new ComboItem(Integer.toString(i), f);

            cmbFecha.addItem(item);
        }
    }

    private void cargarComboProductos() throws IOException {
        List<ProductoDO> productos = Model.getProductos();
        lstProd.setModel(productos);
    }
}
