package p.aplic.peliculas;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.peliculas.util.ExportarGUI;
import p.aplic.peliculas.util.Log;
import p.aplic.peliculas.util.Util;
import p.gui.ComboBox;
import p.gui.LabelTextField;
import p.util.GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:13:34 PM
 */
public class PeliculasFrame extends JFrame implements ListDataListener {
    private static final Logger logger = Logger.getLogger(PeliculasFrame.class);

    private ComboBox estadosCmb = new ComboBox("Estados", new Dimension(90, 40));
    private static final String ITEM_TODOS = "Todos";
    private LabelTextField filtroNombre = new LabelTextField("Nombre", new Dimension(90, 40));
    private LabelTextField filtroAño = new LabelTextField("Años (cvs)", new Dimension(100, 40));
    private LabelTextField filtroId = new LabelTextField("Id", new Dimension(100, 40));
    private Checkbox chkConProblemas = new Checkbox("Sólo Problemas");
    private Checkbox chkCommitAuto = new Checkbox("Commit auto", true);
    private Modelo listModel;
    private JList tabla = null;
    private JScrollPane listScroller;
    private Peliculas peliculas;

    private MenuBar _menuBar = new MenuBar();

    private JButton btnAgregar = new JButton("Agregar");
    private JButton btnAgregarPP   = new JButton("Ag.Portapapeles");
    private JButton btnGenerarHTML = new JButton("Generar HTML");

    private p.gui.List lstOrdenarPor;
    public static final String IT_OR_NOMBRE = "Nombre";
    public static final String IT_OR_ESTADO = "Estado";
    public static final String IT_OR_ANTIGUEDAD = "Antiguedad";
    public static final String IT_OR_ID = "Id";
    public static final String IT_OR_AÑO = "Año";

    public PeliculasFrame(Peliculas pls ) {
        logMemory(true);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        peliculas = pls;
        listModel = new Modelo();
        listModel.addListDataListener(this);
        tabla = new JList(listModel){
            public String getToolTipText(MouseEvent e) {
                logMemory("getToolTipText");
                //selecciono la fila
                int i = tabla.locationToIndex(e.getPoint());
                tabla.setSelectedIndex(i);

                Pelicula p = getPeliculaSeleccionada();

                String res = null;
                if (p != null) {
                    List<String> ps = p.getPosters();
                    String po;
                    if (ps.size() > 0){
                        String raizFotos = System.getProperty("dirRaizFotosCompleto");
                        po = ps.get(0);
                        String file = raizFotos + "\\" + po;
                        ImageIcon ii = new ImageIcon(file);
                        int h = ii.getIconHeight();
                        int w = ii.getIconWidth();
                        double proporcionHW = (double) h/w;
                        String x = "file:" + file;
                        int alt = (int) (proporcionHW * 150); 
                        res = "<html><img height='" + alt + "' width='150' src='" + x + "'>";
                    }
                }
                return res;
            }
        };
        listScroller = new JScrollPane(tabla);

        List estados = new ArrayList();
        estados.add(ITEM_TODOS);
        estados.addAll(Pelicula.getEstados());
        cargarComboEstados(estados);
        actualizarModelo();

        hacerPantalla();
        addListeners();
        ponerTitulo();
    }

    private void mostrarMenu(final Component inv, int x, int y, final Pelicula p) {
        JPopupMenu popup = new JPopupMenu();

        //Si el estado de la película seleccionada es = 'Sin Ver' => agrego los
        // ítems "Pasar a visto" y "Archivar"
        if ( p.getEstado().equals(Pelicula.Estado.SIN_VER)){
            JMenuItem mi = new JMenuItem("Pasar a visto");
            mi.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    pasarPeliculaSeleccionadaAVisto();
                }
            });
            popup.add(mi);
            
            mi = new JMenuItem("Archivar");
            mi.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    archivar();
                }
            });
            popup.add(mi);
        }

        //agrego ítem borrar
        JMenuItem itemBorrar = new JMenuItem("Borrar");
        itemBorrar.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                //Pido confirmación de borrado
                String msg = "Se va a borrar la película " + p.getNombre() +
                        " (" + p.getEstado () + ") ¿Confirma?";
                int res = JOptionPane.showConfirmDialog(inv, msg);
                if (res == JOptionPane.YES_OPTION)
                    borrar(p);
            }
        });
        popup.add(itemBorrar);

        //agrego ítem duplicar
        JMenuItem itemDuplicar = new JMenuItem("Duplicar");
        itemDuplicar.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                Pelicula nuevaPel = p.clonar();
                nuevaPel.setNombre(p.getNombre() + "+");
                nuevaPel.setEstado(Pelicula.Estado.BAJANDO);

                //como es un alta le borro el uniqueId
                nuevaPel.setUniqueId(null);

                mostrarPantallaAlta(nuevaPel);
            }
        });
        popup.add(itemDuplicar);

        popup.show(inv, x, y);
    }

    private void borrar(Pelicula pelicula) {
        peliculas.removePelicula(pelicula);
        try {
            grabarYActualizar();
        } catch (Exception e) {
            String x = Util.pelicula2XML(pelicula);
            p.util.Util.pegarEnElPortapapeles(x);
            String msg = "No se borra la peli. Msg: " + e.getMessage() + ". Se copió la peli al PP";
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    private void pasarPeliculaSeleccionadaAVisto() {
        logMemory("1 pasarPeliculaSeleccionadaAVisto");
        Pelicula p = getPeliculaSeleccionada();
        p.setEstado(Pelicula.Estado.VISTO);
        p.setContador(peliculas.getContadorVistos());
        peliculas.incrementarContadorVistos();
        modificarPelicula(p);
        logMemory("2 pasarPeliculaSeleccionadaAVisto");
    }

    private void archivar() {
        Pelicula p = getPeliculaSeleccionada();
        p.setEstado(Pelicula.Estado.ARCHIVADO_SIN_VER);
        modificarPelicula(p);
    }


    private void cargarComboEstados(List estados) {
        for (int i = 0; i < estados.size(); i++) {
            Object s = estados.get(i);
            estadosCmb.insertItemAt(s, i);
        }
        String ei = System.getProperty("estadoInicial");
        if (ei == null){
            ei = ITEM_TODOS;
        }
        estadosCmb.setSelectedItem(ei);
    }

    private void addListeners() {
        lstOrdenarPor.getModel().addListDataListener(new ListDataListener(){
            public void intervalAdded(ListDataEvent e){}

            public void intervalRemoved(ListDataEvent e){
                Object[] orX = ((DefaultListModel) lstOrdenarPor.getModel()).toArray();
                listModel.setOrdenarPor(orX);
            }

            public void contentsChanged(ListDataEvent e){
            }
        });

        tabla.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    mostrarPantallaEdicion();
                }
            }
        });

        chkConProblemas.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                listModel.setSoloProblemas(chkConProblemas.getState());
            }
        });

        tabla.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    mostrarPantallaEdicion();
                }
                else if (e.getButton() == MouseEvent.BUTTON3){
                    //selecciono la fila donde si hizo clic
                    int i = tabla.locationToIndex(e.getPoint());
                    tabla.setSelectedIndex(i);
                    
                    Pelicula p = getPeliculaSeleccionada();
                    if (p != null)
                        mostrarMenu(e.getComponent(), e.getX(), e.getY(), p);
                }
            }
        });


        btnAgregar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    mostrarPantallaAlta();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                }
            }
        });

        btnAgregarPP.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                agregarDesdePortapapeles();
            }
        });

        btnGenerarHTML.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    Util.generarHTML(peliculas);
                    Log.mensaje("Se han generado los HTML");
                } catch (IOException e1) {
                    Log.error(e1);
                } catch (JDOMException e1) {
                    Log.error(e1);
                }
            }
        });

        estadosCmb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                actualizarModelo();
            }
        });

        filtroNombre.addDocumentListener (new DocumentListener(){
            public void changedUpdate(DocumentEvent e) {}

            public void insertUpdate(DocumentEvent e) {
                listModel.setFiltroNombre(filtroNombre.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                listModel.setFiltroNombre(filtroNombre.getText());
            }
        });

        filtroId.addDocumentListener (new DocumentListener(){
            public void changedUpdate(DocumentEvent e) {}

            public void insertUpdate(DocumentEvent e) {
                listModel.setFiltroId(filtroId.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                listModel.setFiltroId(filtroId.getText());
            }
        });

        filtroAño.addActionListener (new ActionListener(){
             public void actionPerformed(ActionEvent e){
                listModel.setFiltroAños(filtroAño.getText());
            }
        });

        tabla.getModel().addListDataListener(new ListDataListener(){

            public void intervalAdded(ListDataEvent e) {}
            public void intervalRemoved(ListDataEvent e) {}

            public void contentsChanged(ListDataEvent e) {
                cambióModelo();
            }
        });
    }

    private void agregarDesdePortapapeles() {
        Pelicula pe;
        String x;
        try {
            x = p.util.Util.copiarDesdeElPortapapeles();
            pe = Util.parsearPelicula(x);
            //como es un alta le borro el uniqueId
            pe.setUniqueId(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
            return ;
        }
        mostrarPantallaAlta(pe);
    }

    private void cambióModelo() {
        ponerTitulo();
    }

    private void ponerTitulo() {
        //Título del frame
        String t = "Películas: " + listModel.getSize() + " / " +
                peliculas.getPeliculas().size();
        setTitle(t);
    }


    private void actualizarModelo() {
        logMemory("1 actualizarModelo");
        String estado = estadosCmb.getSelectedItem().toString();
        List<Pelicula> pel;
        logMemory("1 getPelisXXX");
        if (estado.equals(ITEM_TODOS))
            pel = peliculas.getPeliculas();
        else
            pel = peliculas.getPeliculas((Pelicula.Estado) estadosCmb.getSelectedItem());
        logMemory("2 getPelisXXX");
        listModel.setLista(pel);
        tabla.revalidate();
        logMemory("2 actualizarModelo");
    }


    private void mostrarPantallaEdicion() {
        Pelicula ps = getPeliculaSeleccionada();
        if (ps != null)
            mostrarPantallaEdicion(ps);
    }

    private void mostrarPantallaEdicion(final Pelicula pelicula) {
        //trabajo con una copia
        Pelicula copiaPelicula = pelicula.clonar();
        
        final PeliculaFrame pf = new PeliculaFrame(Pelicula.getEstados(),
                peliculas.getContadorDVD());
        pf.setPelicula(copiaPelicula);
        final long ts1 = peliculas.getTimestamp();
        pf.addWindowListener(new WindowAdapter(){
           public void windowClosed(WindowEvent e) {
               Pelicula peliculaEditada = pf.getPelicula();
               if (peliculaEditada != null){
                   long ts2 = peliculas.getTimestamp();
                   if (ts1 != ts2){
                       String msg = "Problemas con el Timestamp";
                       JOptionPane.showMessageDialog(null, msg);
                       mostrarPantallaEdicion(peliculaEditada);
                       return ;
                   }
                   if (pf.usóNuevoContadorDVD())
                        peliculas.incrementarDVD();

                   //si se pasó a visto
                   if (peliculaEditada.getEstado().equals(Pelicula.Estado.VISTO)
                           && !pelicula.getEstado().equals(Pelicula.Estado.VISTO)){
                       peliculaEditada.setContador(peliculas.getContadorVistos());
                       peliculas.incrementarContadorVistos();
                   }
                   //si se pasó a sin_ver
                   else if (peliculaEditada.getEstado().equals(Pelicula.Estado.SIN_VER)
                           && !pelicula.getEstado().equals(Pelicula.Estado.SIN_VER)){
                       peliculaEditada.setContador(peliculas.getContadorSinVer());
                       peliculas.incrementarContadorSinVer();
                   }


                   pelicula.cargar(peliculaEditada);
                   modificarPelicula(pelicula);
               }
           }
       });
    }

    private Pelicula getPeliculaSeleccionada() {
        int index = tabla.getSelectedIndex();
        if (index < 0)
            return null;
        else
            return listModel.getPeliculaAt(index);
    }

    private void mostrarPantallaAlta() {
        mostrarPantallaAlta(null);
    }
    private void mostrarPantallaAlta(Pelicula p) {
        final PeliculaFrame pf = new PeliculaFrame(Pelicula.getEstados(),
                peliculas.getContadorDVD());
        pf.setPelicula(p);
        pf.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e) {
                if (pf.usóNuevoContadorDVD())
                    peliculas.incrementarDVD();
                agregarPelicula(pf.getPelicula());
           }
       });
    }

    /**
     * Se actualizan tablas y archivos.
     */
    private void modificarPelicula(Pelicula pelicula) {
        if (pelicula != null) {
            try {
                grabarYActualizar();
            } catch (Exception e) {
                String x = Util.pelicula2XML(pelicula);
                p.util.Util.pegarEnElPortapapeles(x);
                String msg = "No se modifica la peli. Msg: " + e.getMessage() + ". Se copió la peli al PP";
                JOptionPane.showMessageDialog(this, msg);
            }
        }
    }

    /**
     * Se crea y graba una peli nueva
     */
    private void agregarPelicula(Pelicula pelicula) {
        if (pelicula != null) {
            validarRepetidas(pelicula.getNombre());

            //si se crea con estado = visto
            if (pelicula.getEstado().equals(Pelicula.Estado.VISTO)){
               pelicula.setContador(peliculas.getContadorVistos());
               peliculas.incrementarContadorVistos();
            }
            //si se crea con estado = sin_ver
            else if (pelicula.getEstado().equals(Pelicula.Estado.SIN_VER)){
               pelicula.setContador(peliculas.getContadorSinVer());
               peliculas.incrementarContadorSinVer();
            }

            peliculas.addPelicula(pelicula);
            try {
                grabarYActualizar();
            } catch (Exception e) {
                String x = Util.pelicula2XML(pelicula);
                p.util.Util.pegarEnElPortapapeles(x);
                String msg = "No se crea la peli. Msg: " + e.getMessage() + ". Se copió la peli al PP";
                JOptionPane.showMessageDialog(this, msg);
            }
        }
    }

    private void validarRepetidas(String nombre){
        List<Pelicula> ps = peliculas.getPeliculas();
        for(Pelicula p : ps) {
            if (p.getNombre().equalsIgnoreCase(nombre)){
                JOptionPane.showMessageDialog(null, "Película repetida. Revisar: " + nombre);
                break;
            }
        }
    }
    
    private void grabarYActualizar(){
        logMemory("antes de grabar");
        Util.grabar(System.getProperty("xmlPath"), peliculas, chkCommitAuto.getState());
        logMemory("luego de grabar");
        actualizarModelo();
    }

    private void armarMenuBar() {
        Menu mnuH = new Menu("Herramientas");
        MenuItem itemExp = new MenuItem("Exportar");

        mnuH.add(itemExp);
        _menuBar.add(mnuH);

        itemExp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                exportar();
            }
        });
    }

    private void exportar() {
        ExportarGUI ep = new ExportarGUI();
        ep.setVisible(true);
    }

    private void hacerPantalla() {
        setLayout(new GridBagLayout());
        armarMenuBar();
        setMenuBar(_menuBar);
        listScroller.setPreferredSize(new Dimension(480, 430));
        Insets ins1 = new Insets(5, 5, 5, 5);

        DefaultListModel listModelOrd = new DefaultListModel();
        listModelOrd.addElement(IT_OR_ESTADO);
        listModelOrd.addElement(IT_OR_ANTIGUEDAD);
        listModelOrd.addElement(IT_OR_ID);
        listModelOrd.addElement(IT_OR_NOMBRE);
        listModelOrd.addElement(IT_OR_AÑO);
        listModel.setOrdenarPor(listModelOrd.toArray());
        lstOrdenarPor = new p.gui.List(listModelOrd);
        JScrollPane listScrollerOrd = new JScrollPane(lstOrdenarPor);

        //Fila 1
        int gap = 2;
        int yy = 0;

        JPanel pnlHeader = new JPanel(new GridLayout(1, 0, gap, gap));
        JPanel pnlHeaderChk = new JPanel(new GridLayout(0, 1, 0, 0));

        pnlHeader.add(estadosCmb);


        pnlHeader.add(filtroNombre);
        pnlHeader.add(filtroAño);
        pnlHeader.add(filtroId);
        pnlHeaderChk.add(chkConProblemas);
        pnlHeaderChk.add(chkCommitAuto);
        pnlHeader.add(pnlHeaderChk);
        listScrollerOrd.setPreferredSize(new Dimension(90, 50));
        pnlHeader.add(listScrollerOrd);

        add(pnlHeader, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));

        //Fila 2
        yy++;
        add(listScroller, new GridBagConstraints(0, yy, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));

        //Fila 3
        yy++;
        JPanel pnlBotones = new JPanel(new GridLayout(1, 0, gap, gap));


        pnlBotones.add(btnGenerarHTML);
        pnlBotones.add(btnAgregarPP);
        pnlBotones.add(btnAgregar);
        add(pnlBotones, new GridBagConstraints(0, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));

        int hei = (int)GUI.getMaxSize(getGraphicsConfiguration()).getHeight();
        setPreferredSize(new Dimension(600, hei));
        pack();
        GUI.centrar(this);


        //muevo el scroll hacia abajo
        JViewport vp = listScroller.getViewport();
        int h = tabla.getHeight();
        vp.setViewPosition(new Point (0, h));
    }

    /**************************************************************************/
    /*  Métodos de ListDataListener     /*
    /**************************************************************************/
    public void contentsChanged(ListDataEvent e) {
        tabla.revalidate();
    }

    public void intervalAdded(ListDataEvent e) {
    }

    public void intervalRemoved(ListDataEvent e) {
    }


    private void logMemory() {
        logMemory(false);
    }
    private void logMemory(String msg) {
        logMemory(false, msg);
    }
    private void logMemory(boolean conTotalMemory) {
        logMemory(conTotalMemory, null);
    }
    private void logMemory(boolean conTotalMemory, String msg) {
        Runtime runtime = Runtime.getRuntime();
        if (conTotalMemory){
            logger.debug("Total memory: " + runtime.totalMemory());
            logger.debug("freeMemory: " + runtime.freeMemory());
            logger.debug("maxMemory: " + runtime.maxMemory());
        }

        if (msg == null)
            msg = "";
        else {
            msg = ". " + msg;
        }
        logger.debug("usada: " + (runtime.totalMemory() - runtime.freeMemory())+ msg);
    }
}

class Modelo extends AbstractListModel{

    private java.util.List<Pelicula> lista;
    private java.util.List<Pelicula> listaFiltrada;
    private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

    private String filtroNombre;
    private String filtroId;
    private boolean soloProblemas = false;
    private List<String> filtroAño = new ArrayList<String>();
    private Object[] ordenarPor;

    public Modelo(){
    }

    public boolean isSoloProblemas() {
        return soloProblemas;
    }

    public void setSoloProblemas(boolean soloProblemas) {
        this.soloProblemas = soloProblemas;
        actualizarModelo();
    }

    public void setFiltroNombre(String filtro) {
        if (filtro.trim().equals(filtroNombre))
            return ;
        filtroNombre = filtro.trim();
        actualizarModelo();
    }
    public void setFiltroId(String filtro) {
        if (filtro.trim().equals(filtroId))
            return ;
        filtroId = filtro.trim();
        actualizarModelo();
    }

    public void setFiltroAños(String s) {
        filtroAño.clear();
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreElements()) {
            filtroAño.add(st.nextToken().trim());
        }

        actualizarModelo();
    }

    private void actualizarModelo() {
        if ((filtroNombre == null || filtroNombre.trim().equals(""))
                && (filtroId == null || filtroId.trim().equals(""))
                && filtroAño.size() == 0
                && !soloProblemas ){
            listaFiltrada = lista;
        }else{
            listaFiltrada = new ArrayList<Pelicula>();
            for (Pelicula p : lista) {
                //filtro con nombre, aka e id
                if (pasaFiltros(p))
                    listaFiltrada.add(p);
            }
        }
        changedData();
    }

    @SuppressWarnings({"OverlyComplexBooleanExpression"})
    private boolean pasaFiltros(Pelicula p) {
        String id = p.getId();
        //la comparación no es key sensitive
        if (filtroNombre != null)
                filtroNombre = filtroNombre.toLowerCase(Util.locale);
        if (filtroId != null)
                filtroId = filtroId.toLowerCase(Util.locale);

        //primero filtro por nombre y Aka
        boolean b = (filtroNombre == null )
                ||  p.getNombre().toLowerCase(Util.locale).contains(filtroNombre)
                || ((p.getAka() != null) && p.getAka().toLowerCase(Util.locale).contains(filtroNombre));

        //filtro por Id
        if (b && filtroId != null && !filtroId.trim().equals("") )
            b = ((id != null) && id.toLowerCase(Util.locale).contains(filtroId));
        
        //filtro por año
        if (b)
            b = pasaFiltroAños(p);

        //filtro por problemas
        if (b && soloProblemas )
            b = p.getProblemas() != null;

        return b;
    }

    private boolean pasaFiltroAños(Pelicula p) {
        if (filtroAño.size() == 0)
            return true;
        else
            for(String a : filtroAño) {
                if (p.getAnnus() != null && p.getAnnus().equals(a))
                    return true;
            }
        return false;
    }

    public Pelicula getPeliculaAt(int i){
        return listaFiltrada.get(i);
    }

    private void changedData() {
        for (Object listener : listeners) {
            ListDataListener l = (ListDataListener) listener;
            l.contentsChanged(null);
        }
    }
    
    public void setLista(List<Pelicula> lista) {
        this.lista = Util.ordenar(lista, ordenarPor);
        actualizarModelo();
    }

    public int getSize() {
        if (listaFiltrada == null)
            return 0;
        return listaFiltrada.size();
    }

    public Object getElementAt(int index) {
        Pelicula p = listaFiltrada.get(index);

        String res = p.getNombre();
        if (p.getAka() != null)
            res +=  " (" + p.getAka() + ")";
        if (p.getAnnus() != null)
            res +=  " (" + p.getAnnus() + ")";
        if (p.getId() != null)
            res +=  "     [" + p.getId() + "]";
        else if (Pelicula.Estado.BAJANDO.equals(p.getEstado()))
            res =  "<html> <i>" + res + "</i></html>]";

        String problemas = p.getProblemas();
        if (problemas != null )
            res += " - con problemas - ";
        return res;
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void setOrdenarPor(Object[] objects) {
        ordenarPor = objects;
        setLista(lista);//para que reordene
    }

}