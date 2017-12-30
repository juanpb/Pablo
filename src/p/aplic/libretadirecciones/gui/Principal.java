package p.aplic.libretadirecciones.gui;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.libretadirecciones.bobj.Direccion;
import p.aplic.libretadirecciones.gui.abm.ABMGral;
import p.aplic.libretadirecciones.model.Model;
import p.aplic.libretadirecciones.proc.AnalizarDiferencias;
import p.aplic.libretadirecciones.proc.expimp.Exportar;
import p.gui.JTextArea_;
import p.util.Constantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: May 3, 2005
 * Time: 5:25:41 PM
 */
/**
 * Pendientes:

    al grabar -> nuevo thread
    en tabla direcciones -> botón suprimir
    en tabla direcciones -> menú ctx con eliminar, cambiar categ., copiar fila, copiar celda
    Menú con acceso a herramientas
    abm categorías + persistencia
 */
public class Principal extends JFrame implements Constantes{
    private PrincipalPanelDetalleDireccion pnlDetDir
            = new PrincipalPanelDetalleDireccion();
    private PrincipalPanelDirecciones pnlDirs
            = new PrincipalPanelDirecciones(this);
    private JSplitPane splitPane
            = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlDirs, pnlDetDir);

    private MenuBar _menuBar = new MenuBar();

    private String categoria = "Todos";
    private PrincipalCabecera cabeceraPnl = new PrincipalCabecera(this);
    private boolean windowClosed;
    private static final Logger logger = Logger.getLogger(Principal.class);


    public Principal(){
        init();
    }

    public static void main(String[] args) {
        if (args.length == 1){
            String xml = args[0];
            Model.setXMLFile(xml);
        }
        else{
            System.out.println("Se debe pasar el xml con los datos como parámetro");
            System.exit(-1);
        }

        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new Principal();
             }
        });
    }



    protected void processKeyEvent(KeyEvent e) {
    }

    private void addListeners() {
    }

    protected void cambioSeleccion() {
        actualizarPanelDer();
    }
    
    protected void cambioHistorico() {
        if (cabeceraPnl == null)
            return ;
        boolean b = cabeceraPnl.isHistorico();
        pnlDirs.verHistoricos(b);
        pnlDetDir.setDireccion(null);
    }

    protected void cambioCategoria() {
        if (cabeceraPnl == null)
            return ;
        String nc = cabeceraPnl.getCategoria();
        if (!nc.equals(categoria)){
            categoria = nc;
            pnlDirs.verSoloCategoria(nc);
        }
        pnlDetDir.setDireccion(null);
    }

    private void addComponentes() {
        setMenuBar(_menuBar);
        getContentPane().add(cabeceraPnl, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }


    private void init(){
        armarMenuBar();
        addComponentes();
        addListeners();
        setSizes();
        setVisible(true);
        setPropiedades();
        boolean verHistoricos = false;
        cabeceraPnl.setVerHistoricos(verHistoricos);
        pnlDirs.verHistoricos(verHistoricos);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pnlDirs.requestFocus();
    }

    private void armarMenuBar() {
        Menu mnuH = new Menu("Herramientas");
        MenuItem itemC = new MenuItem("Comparar versión");
        MenuItem itemExp = new MenuItem("Exportar para imprimir");
        MenuItem itemExpVisibles = new MenuItem("Exportar para imprimir Visibles");
        MenuItem itemCSV = new MenuItem("Exportar CSV");

        mnuH.add(itemC);
        mnuH.add(itemExp);
        mnuH.add(itemExpVisibles);
        mnuH.add(itemCSV);
        _menuBar.add(mnuH);

        itemC.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                compararVersion();
            }
        });

        itemExp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                exportarParaImprimir();
            }
        });
        itemCSV.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                exportarCSV();
            }
        });
        itemExpVisibles.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                exportarVisibles();
            }
        });
    }

    private void exportarVisibles() {
        String s = JOptionPane.showInputDialog("Archivo de salida", "C:\\dirs.txt");
        try {
            List<Direccion> dirs = pnlDirs.getDireccionesVisibles();
            Exportar.separadoPorComa(s, dirs);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage());
            return ;
        }
        String msg = "Se creó el archivo " + s;
        JOptionPane.showMessageDialog(this, msg);
    }

    private void exportarCSV() {
        String s = JOptionPane.showInputDialog("Archivo de salida", "C:\\dirs.csv");
        try {
            Exportar.separadoPorComa(s, Model.getDirecciones());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage());
            return ;
        }
        String msg = "Se creó el archivo " + s;
        JOptionPane.showMessageDialog(this, msg);
    }

    private void exportarParaImprimir() {
        String s = JOptionPane.showInputDialog("Archivo de salida", "C:\\dirs.txt");
        try {
            List<Direccion> dirs = Model.getDirecciones();
            dirs = p.aplic.libretadirecciones.Util.filtrar("Todas", false, dirs);
            Exportar.separadoPorComaParaImprimir(s, dirs);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage());
            return ;
        }
        String msg = "Se creó el archivo " + s;
        JOptionPane.showMessageDialog(this, msg);
    }

    private void compararVersion() {
        JFileChooser chooser = new JFileChooser("C:\\");
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                List dirsViejas = Model.getDirecciones(file);
                List dirsActual = Model.getDirecciones();
                AnalizarDiferencias ad =
                        new AnalizarDiferencias(dirsViejas, dirsActual);
                String res = "Eliminados:" + Constantes.NUEVA_LINEA;
                List soloD1 = ad.getSoloD1();
                if (soloD1.size() == 0){
                    res += "    Ninguno"+ Constantes.NUEVA_LINEA;
                }
                else{
                    for (Object aSoloD1 : soloD1) {
                        Direccion d = (Direccion) aSoloD1;
                        String x = d.toString();
                        res += "    " + x + Constantes.NUEVA_LINEA;
                    }
                }

                res +=  Constantes.NUEVA_LINEA + "Nuevos: " + Constantes.NUEVA_LINEA;
                List soloD2 = ad.getSoloD2();
                if (soloD2.size() == 0){
                    res += "    Ninguno"+ Constantes.NUEVA_LINEA;
                }
                else{
                    for (Object aSoloD2 : soloD2) {
                        Direccion d = (Direccion) aSoloD2;
                        String x = d.toString();
                        res += "    " + x + Constantes.NUEVA_LINEA;
                    }
                }

                res +=  Constantes.NUEVA_LINEA + "Modificados: " + Constantes.NUEVA_LINEA;
                Map mods = ad.getDifs();
                if (mods.size() == 0){
                    res += "    Ninguno"+ Constantes.NUEVA_LINEA;
                }
                else{
                    for (Object o : mods.keySet()) {
                        Direccion dV = (Direccion) o;
                        Direccion dN = (Direccion) mods.get(dV);
                        String x = "N: " + dN.toString() + Constantes.NUEVA_LINEA;
                        x += "V: " + dV.toString() + Constantes.NUEVA_LINEA + Constantes.NUEVA_LINEA;
                        res += x;
                    }
                }
                JDialog jd = new JDialog(this, "Diferencias", true);
                JTextArea_ txt = new JTextArea_();
                JScrollPane jp = new JScrollPane(txt);
                jd.getContentPane().add(jp);
                txt.setText(res);
                jd.setSize(800,700);
                jd.setVisible(true);

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } catch (JDOMException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void setPropiedades() {
        splitPane.setDividerLocation(0.75);
    }

    private void setSizes() {
        Dimension minimumSize = new Dimension(0, 0);
        pnlDetDir.setMinimumSize(minimumSize);
        pnlDirs.setMinimumSize(minimumSize);


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)dim.getWidth() - 45;
        int h = (int)dim.getHeight() - 35;

        this.setSize(w,h);
    }

    private void actualizarPanelDer(){
        Direccion dir = pnlDirs.getSeleccionada();
        pnlDetDir.setDireccion(dir);
    }

    protected void nuevo() {
        nuevo(null);
    }
    protected void nuevo(Direccion d) {
        final ABMGral abm = new ABMGral(d);
        final Principal dis = this;
        windowClosed = false;
        abm.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e) {
                if (windowClosed)
                    return ;
                windowClosed = true;
                try {
                    Direccion d = abm.getDireccion();
                    if (d == null)
                        return ;
                    Model.agregarDireccion(d);
                    pnlDirs.actualizar();
                } catch (Exception e1) {
                    logger.error(e1.getMessage(), e1);
                    String msg = e1.getMessage();
                    JOptionPane.showMessageDialog(dis, msg, "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        abm.setModal(true);
        abm.setBounds(200 ,200, abm.getWidth(), abm.getHeight());
        abm.setVisible(true);
    }
}
