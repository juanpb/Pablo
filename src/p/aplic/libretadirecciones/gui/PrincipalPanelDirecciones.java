package p.aplic.libretadirecciones.gui;

import org.apache.log4j.Logger;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.gui.abm.ABMGral;
import p.aplic.libretadirecciones.model.DirsTableModel;
import p.aplic.libretadirecciones.model.Model;
import p.gui.Tabla;
import p.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class PrincipalPanelDirecciones extends JPanel{

    private long when = 0;
    private Principal control;
    private JPopupMenu _popupMenu = new JPopupMenu();

    private DirsTableModel tablaModel = new DirsTableModel();
    private Direccion dirActual = null;
    private static final Logger logger = Logger.getLogger(PrincipalPanelDirecciones.class);

    //La usa para diferenciar el 'enter' sobre el panel ABMGral del que se
    //hace en la tabla
    private long cerroABMGral = 0;

    private Tabla tabla = new Tabla(){
        protected void processKeyEvent(KeyEvent e) {
            if (when == e.getWhen())
                return ;
            when = e.getWhen();

            if (((when - cerroABMGral)>400)
                    && (when - this.getCerroBusc())>400
                    && e.getKeyCode()==KeyEvent.VK_ENTER){
                enterEnLaTabla();
            }
            else //noinspection StatementWithEmptyBody
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                //Ignoro el enter y hago que el super también lo ignore
                ;
            else{
                super.processKeyEvent(e);
            }
        }
    };
    private JScrollPane _scrollPane = new JScrollPane(tabla);

    private void enterEnLaTabla() {
        mostrarDetalles();
    }


    public PrincipalPanelDirecciones(Principal p) {
        init();
        control = p;
    }

    private void init() {
        tabla.setModel(tablaModel);
        TablaRenderer render = new TablaRenderer();
        tabla.setDefaultRenderer(Object.class, render);
        setLayout(new BorderLayout());
        setBounds();
        addComponentes();
        setPropiedades();
        addListeners();
        armarPopupMenu();
        setVisible(true);
    }

    private void armarPopupMenu(){
        JMenuItem item = new JMenuItem("Borrar");
        JMenuItem itemHist = new JMenuItem("Pasar a histórico");
        JMenuItem itemReplicar = new JMenuItem("Replicar");
        JMenuItem itemCopiar = new JMenuItem("Copiar");

        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrar(tabla.getSelectedRow());
            }
        });
        itemHist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasarAHistorico(tabla.getSelectedRow());
            }
        });
        itemReplicar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                replicar(tabla.getSelectedRow());
            }
        });
        itemCopiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copiarAPP(tabla.getSelectedRow());
            }
        });
        _popupMenu.add(item);
        _popupMenu.add(itemHist);
        _popupMenu.add(itemReplicar);
        _popupMenu.add(itemCopiar);
    }

    private void replicar(int sr) {
        Direccion dir = tablaModel.getDir(sr);
        Direccion nuevaDir = dir.getCopia();

        control.nuevo(nuevaDir);
    }

    private void copiarAPP(int sr) {
        Direccion dir = tablaModel.getDir(sr);
        String d = dir.toString();
        String x = "; ; ";
        while(d.indexOf(x) > 0) {
            d = d.replaceAll(x, "; ");
        }
        d = d.trim();
        if (d.startsWith("; "))
            d = d.substring(2);
        if (d.endsWith(";"))
            d = d.substring(0, d.length()-1);
        Util.pergarEnElPortapapeles(d);
    }

    private void pasarAHistorico(int sr) {
        Direccion dir = tablaModel.getDir(sr);
        Direccion nuevaDir = dir.getCopia();
        
        nuevaDir.setHistorico(true);
        actualizarDetalles(nuevaDir, dir, false);
        try {
            actualizar();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void borrar(int sr) {
        Direccion dir = tablaModel.getDir(sr);
        String str = "¿Está seguro que desea borrar a " + dir.getNombre() +
                " " + dir.getAppellido() + "?";
        int i = JOptionPane.showConfirmDialog(this, str);
        if (i == JOptionPane.OK_OPTION){
            try {
                Model.borrar(dir);
                actualizar();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    private void addListeners() {
        this.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
                tabla.requestFocus();
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    Point p = e.getPoint();
                    mostrarDetalles(tabla.rowAtPoint(p));
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e)){
                    Point p = new Point(e.getX(), e.getY());
                    int f = tabla.rowAtPoint(p);
                    tabla.setRowSelectionInterval(f, f);
                    mostrarPopupMenu(e);
                }
            }
        });

        tabla.getSelectionModel().addListSelectionListener( new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                int f = tabla.getSelectedRow();
                filaSeleccionada(f);
            }
        });
    }

    private void mostrarPopupMenu(MouseEvent e){
        _popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
    }

    /**
     * Muestra los detalles de la fila seleccionada
     */
    private void mostrarDetalles() {
        int sr = tabla.getSelectedRow();
        mostrarDetalles(sr);
    }

    private void mostrarDetalles(final int i) {
        if (i < 0)
            return ;
        Direccion d = tablaModel.getDir(i);
        final ABMGral abm = new ABMGral(d);
        abm.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e) {
                actualizarDetalles(abm.getDireccion(), tablaModel.getDir(i), true);
                cerroABMGral = System.currentTimeMillis();
            }
        });

        abm.setModal(true);
        abm.setBounds(200 ,200, abm.getWidth(), abm.getHeight());
        abm.setVisible(true);
    }

    private void actualizarDetalles(Direccion nuevaDir, Direccion viejaDir, boolean comparar){
        if (nuevaDir == null)
            return ;
        if (!comparar || !viejaDir.equals(nuevaDir)){
            try {
                tablaModel.actualizar(viejaDir, nuevaDir);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                JOptionPane.showMessageDialog(this, e);
            }
        }
        int i = tablaModel.getPosicion(nuevaDir);
        tabla.setRowSelectionInterval(i, i);
        cambioSeleccion();
    }

    private void filaSeleccionada(int i) {
        if (i < 0)
            return ;
        Direccion d = tablaModel.getDir(i);
        if (d != null && !d.equals(dirActual)){
            dirActual = d;
            cambioSeleccion();
        }
    }


    private void setPropiedades() {
    }

    private void addComponentes() {
        add(_scrollPane, BorderLayout.CENTER);
    }

    private void setBounds(){
        this.setSize(600,400);
    }

    private void cambioSeleccion(){
        control.cambioSeleccion();
    }


    public Direccion getSeleccionada(){
        return dirActual;
    }

    public void verHistoricos(boolean b) {
        tablaModel.verHistoricos(b);
    }
    public void verSoloCategoria(String nc) {
        tablaModel.filtrarPorCategoria(nc);
    }

    public void actualizar() throws Exception {
        tablaModel.actualizar();
    }

    public List<Direccion> getDireccionesVisibles(){
        return tablaModel.getDireccionesVisibles();
    }
}
