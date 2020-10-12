package p.aplic.renombrarFotos;

import org.apache.log4j.Logger;
import p.gui.ComboItem;
import p.gui.LabelTextField;
import p.gui.TextField;
import p.gui.dd.FileTransfer;
import p.gui.treefs.TreeFileSystem;
import p.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

//import javax.swing.text.JTextComponent;
//import java.awt.datatransfer.*;

/**
 * Funcionalidades no visibles:
 * 1) Botón derecho sobre el panel: menú contextual
 * 2) Botón derecho sobre el txt de artista, álbum o comentario: pasa a
 *      mayúscula la primera letra de cada palabra y deja las demás en minúscula
 * 3) Clic sobre el label '---MP3---' abre panel para fotos.
 * 4) Botón derecho sobre el botón 'Numerar' y numera de arriba a abajo
 */
public class RenombrarGUI extends JFrame implements FileTransfer{
    private static final Logger logger = Logger.getLogger(RenombrarGUI.class);

    private Robot robot = null;

    private TreeFileSystem tree = null;
    private JPanel panelFotos = new JPanel(null);
    private JPanel panelThumb = new JPanel(new GridBagLayout());

    private JScrollPane scrollPane1 = null;
    private JScrollPane scrollPaneTree = null;
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            tree, panelThumb);
    private JComboBox cmbDirectorio = new JComboBox();
    private JComboBox cmbInsertar = new JComboBox();
    private JList lstInsertarNombres = new JList();
    private JScrollPane listScrollPaneInsertarNombres = new JScrollPane(lstInsertarNombres);
    private JComboBox cmbInsertarFechas = new JComboBox();

    static final ComboItem ITEM_VACIO = new ComboItem("", "");
    static final String ITEM_INS_AL_PRINCICIO = "ITEM_INS_AL_PRINCICIO";
    static final String ITEM_INS_AL_FINAL = "ITEM_INS_AL_FINAL";
    static final String ITEM_INS_ANTES_DE = "ITEM_INS_ANTES_DE";
    static final String ITEM_INS_DESPUES_DE = "ITEM_INS_DESPUES_DE";

    private JCheckBox chkSoloSeleccionado = new JCheckBox("Solo seleccionado");


    private JButton btnGrabar = new JButton("Grabar");
    private JButton btnRenombrar = new JButton("V V V");
    private LabelTextField txtAgregarAlFinal = new LabelTextField("Agregar al final");

    private URL url = getClass().getResource("ic_reload.JPG");

    private Icon icon = new ImageIcon(url);
    private JButton btnActualizar = new JButton(icon);
    private JButton btnPegar = new JButton("Pegar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JCheckBox chkInsertarClic = new JCheckBox("Insertar c/clic");
    private JButton btnInsertarExif = new JButton("Insertar EXIF");
    private JButton btnInsertarFecha = new JButton("Insertar fecha");

    private JLabel lblRenombrar = new JLabel("Renombrar");
    private JLabel lblInsertar = new JLabel("Insertar");
    private JLabel lblEliminarHasta = new JLabel("Eliminar hasta: ");
    private JLabel lblEliminarCant = new JLabel("Eliminar cant.: ");

    private TextField txtViejo = new TextField();
    private TextField txtNuevo = new TextField();
     private TextField txtEliminarHasta = new TextField();

    private TextField txtEliminarCant = new TextField();

    private TextField txtInsertar = new TextField();
    private TextField txtInsertarEn = new TextField();
    private TextField txtRenombrarFotos = new TextField();
    private Renombrar control = null;

    private boolean ignorarEvento = false;

    private RenombrarGUI dis = null;
    private Thumb filaClic = null; //tiene la fila donde si hizo clic

    private boolean soloNombres = false;
    private List<Thumb> thumbs;

    public RenombrarGUI(Renombrar c) {
        dis = this;

        control = c;

        try {
            jbInit();
            robot = new Robot();
        }
        catch(Exception e) {
          logger.error(e.getMessage(), e);
        }

        p.gui.dd.FileTransferHandler fth = new p.gui.dd.FileTransferHandler(this);

        btnGrabar.setTransferHandler(fth);
        panelFotos.setTransferHandler(fth);

        cmbDirectorio.setTransferHandler(fth);
        splitPane.setTransferHandler(fth);
    }


    public void setTitulo(String x){
        this.setTitle(x);
    }
/*****************************************************************************/
/*                        Métodos privados                                   */
/*****************************************************************************/

    private void jbInit() throws Exception {
        super.getContentPane().setLayout(null);
        String s = System.getProperty("GENERAR_ARBOL");
        if (s != null && s.toLowerCase().startsWith("s"))
            tree = new TreeFileSystem();
        armarCombos();
        setBounds();
        addComponentes();
        setPropiedades();

        addListeners();
        setVisible(true);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    private void usarEditor(){
        p.aplic.exportar.GUI pEd = new p.aplic.exportar.GUI(false);
        pEd.ejecutar(null);
        pEd.setFile(new File(getDirectorio()));
    }

    private void armarCombos(){
        //Si se modifica el orden de los ítems se deben modificar
        //las constantes de arriba


        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_PRINCICIO, "Al principio"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_FINAL, "Al final"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_ANTES_DE, "Antes de..."));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_DESPUES_DE, "Después de..."));

        //combo cmbDirectorio
        cmbDirectorio.addItem(ITEM_VACIO);
        String dirs = System.getProperty("DIRECTORIOS");
        if (dirs != null && !dirs.trim().equals("")){
            StringTokenizer tok = new StringTokenizer(dirs, ";", false);
            while (tok.hasMoreTokens()){
                String d = tok.nextToken().trim();
                ComboItem item = new ComboItem(d, d);
                cmbDirectorio.addItem(item);
            }
        }
        //fin combo cmbDirectorio

        //combo cmbInsertarFechas
        cmbInsertarFechas.addItem(ITEM_VACIO);
        String cant = System.getProperty("CANT_DIAS");
        int ciclar = 5; //valor por defecto
        if (cant != null && !cant.trim().equals("")){
            ciclar = Integer.parseInt(cant.trim());
        }
        if (ciclar > 0){
            String f = Fechas.getAAAAMMDD("-");
            f += " (hoy)";
            ComboItem item = new ComboItem(Integer.toString(0), f);
            cmbInsertarFechas.addItem(item);
        }
        if (ciclar > 1){
            String f = Fechas.getAAAAMMDD(-1 ,"-");
            f += " (ayer)";
            ComboItem item = new ComboItem(Integer.toString(1), f);
            cmbInsertarFechas.addItem(item);
        }
        for (int i = 2; i < ciclar; i++){
            String f = Fechas.getAAAAMMDDDiaSemana(-i, "-");
            ComboItem item = new ComboItem(Integer.toString(i), f);

            cmbInsertarFechas.addItem(item);
        }
        //fin combo cmbInsertarFechas

        //combo lstInsertarNombres
        DefaultListModel listModel = new DefaultListModel();
        lstInsertarNombres.setModel(listModel);
        listModel.addElement(ITEM_VACIO);
        String nombres = System.getProperty("NOMBRES");
        if (nombres != null && !nombres.trim().equals("")){
            StringTokenizer tok = new StringTokenizer(nombres, ";", false);
            while (tok.hasMoreTokens()){
                String n = tok.nextToken().trim();
                ComboItem item = new ComboItem(n, n);
                listModel.addElement(item);
            }
        }
        //fin combo lstInsertarNombres
    }

    private void setPropiedades(){
        splitPane.setDividerLocation(0.015);  //queda casi oculto
//        splitPane.setDividerLocation(0.15); así se ve bien

        cmbDirectorio.setEditable(true);
        chkSoloSeleccionado.setSelected(false);

        setFonts();

        lstInsertarNombres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //se habilita uno solo
        txtEliminarCant.setEnabled(false);
        txtEliminarHasta.setEnabled(true);
        chkInsertarClic.setSelected(true);
    }

    private void setFonts(){
        Map map = new HashMap();
        map.put(TextAttribute.SIZE, (float) 11);
        Font f = new Font(map);
        txtViejo.setFont(f);
        txtNuevo.setFont(f);
        txtRenombrarFotos.setFont(f);
        txtEliminarHasta.setFont(f);
        txtEliminarCant.setFont(f);

        btnRenombrar.setFont(f);
        btnGrabar.setFont(f);
        txtAgregarAlFinal.setFont(f);
        btnPegar.setFont(f);
        btnActualizar.setFont(f);
        btnLimpiar.setFont(f);
        btnInsertarExif.setFont(f);
        btnInsertarFecha.setFont(f);

        chkInsertarClic.setFont(f);
        chkSoloSeleccionado.setFont(f);
        lblRenombrar.setFont(f);
        lblEliminarHasta.setFont(f);
        lblEliminarCant.setFont(f);

        cmbDirectorio.setFont(f);
        cmbInsertar.setFont(f);
        cmbInsertarFechas.setFont(f);
        lstInsertarNombres.setFont(f);
    }

    private void addListeners() {
        txtEliminarHasta.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                txtEliminarCant.setEnabled(false);
                txtEliminarHasta.setEnabled(true);
            }
        });

        txtEliminarCant.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                txtEliminarCant.setEnabled(true);
                txtEliminarHasta.setEnabled(false);
            }
        });

        cmbInsertarFechas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignorarEvento)
                    return;
                actualizarRenombrarFotos();
            }
        });

        lstInsertarNombres.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (ignorarEvento)
                    return;
                //copio el nombre del combo al campo 'InsertarAlFinal'
                String n = lstInsertarNombres.getSelectedValue().toString();
                txtAgregarAlFinal.setText(" " + n);

                actualizarRenombrarFotos();
            }
        });

        cmbInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();

                if (item.getClave().equals(ITEM_INS_AL_PRINCICIO)
                        || item.getClave().equals(ITEM_INS_AL_FINAL))
                {
                    txtInsertarEn.setText("");
                    txtInsertarEn.setEnabled(false);
                }
                else
                    txtInsertarEn.setEnabled(true);

            }
        });

        cmbDirectorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarThumb();
            }
        });


        btnGrabar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    limpiar();
                }
                catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });

        btnRenombrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renombrar();
            }
        });

        btnInsertarExif.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.insertarExif();
            }
        });

        btnInsertarFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.insertarFecha();
            }
        });

        txtAgregarAlFinal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.insertarAlFinal(txtAgregarAlFinal.getText());
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarThumb();
                } catch (Exception ex) {
                    ex.printStackTrace( );
                }
            }
        });

        btnPegar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String pp = Util.copiarDesdeElPortapapeles();
                    setDirectorio(pp);
                } catch (Exception ex) {
                    ex.printStackTrace( );
                }
            }
        });


        //enter
        txtInsertar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                insertar();
            }
        });
        txtInsertarEn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                insertar();
            }
        });

        txtRenombrarFotos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renombrarFotos();
            }
        });

        txtNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renombrar();
            }
        });
        txtViejo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renombrar();
            }
        });
    }

    private String getExtension(String x) {
        int i = x.lastIndexOf(".");
        if (i > 0)
            return x.substring(i+1);
        else
            return null;
    }

    private String sacarExtension(String txt) {
        int i = txt.lastIndexOf(".");
        if (i > 0)
            txt = txt.substring(0, i);
        return txt;
    }


    private void grabar() {
        GUI.startWaitCursor(dis.getRootPane());
        try {
            control.grabar();
        }
        catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
        finally{
            GUI.stopWaitCursor(dis.getRootPane());
        }
    }

    private void actualizarRenombrarFotos() {
        String mostrar = "";
        String f = cmbInsertarFechas.getSelectedItem().toString();
        if (f != null && f.length() > 9){
            mostrar = f.substring(0, 10);
        }
        String n = lstInsertarNombres.getSelectedValue().toString();
        if (mostrar.equals(""))
            mostrar = n;
        else if (n != null && !n.equals(""))
            mostrar += "_" + n;

        txtRenombrarFotos.setText(mostrar);
    }

    private void limpiar() {
        txtRenombrarFotos.setText("");

        txtEliminarHasta.setText("");
        txtEliminarCant.setText("");
        txtInsertar.setText("");
        txtInsertarEn.setText("");
        txtNuevo.setText("");
        txtViejo.setText("");

    }

    private void insertar(){
        String nue = txtInsertar.getText();
        String cond = txtInsertarEn.getText();
        ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();
        if (item.getClave().equals(ITEM_INS_AL_FINAL))
            control.insertarAlFinal(nue);
        else if (item.getClave().equals(ITEM_INS_AL_PRINCICIO))
            control.insertarAlPrincipio(nue);
        else if (item.getClave().equals(ITEM_INS_ANTES_DE))
            control.insertarAntesDe(nue, cond);
        else if (item.getClave().equals(ITEM_INS_DESPUES_DE))
            control.insertarDespuesDe(nue, cond);
    }

    private void addComponentes(){
        if (tree != null)
            splitPane.add(scrollPaneTree, JSplitPane.LEFT);
        splitPane.add(scrollPane1, JSplitPane.RIGHT);

        getContentPane().add(splitPane, null);
        getContentPane().add(cmbDirectorio, null);
        getContentPane().add(btnActualizar, null);
        getContentPane().add(btnPegar, null);
        getContentPane().add(chkSoloSeleccionado, null);
        getContentPane().add(btnGrabar, null);
        getContentPane().add(btnLimpiar, null);
        getContentPane().add(btnRenombrar, null);

        getContentPane().add(lblRenombrar, null);
        getContentPane().add(txtNuevo, null);
        getContentPane().add(txtViejo, null);

        getContentPane().add(lblInsertar, null);
        getContentPane().add(txtInsertar, null);
        getContentPane().add(txtInsertarEn, null);
        getContentPane().add(cmbInsertar, null);

        getContentPane().add(lblEliminarHasta, null);
        getContentPane().add(txtEliminarHasta, null);
        getContentPane().add(lblEliminarCant, null);
        getContentPane().add(txtEliminarCant, null);

        //panel fotos
        panelFotos.add(cmbInsertarFechas, null);
        panelFotos.add(listScrollPaneInsertarNombres, null);
        panelFotos.add(txtRenombrarFotos, null);
        panelFotos.add(chkInsertarClic, null);
        panelFotos.add(btnInsertarExif, null);
        panelFotos.add(btnInsertarFecha, null);
        panelFotos.add(txtAgregarAlFinal, null);

        getContentPane().add(panelFotos, null);
    }

    private void setBounds(){
        chkSoloSeleccionado.setMargin(new Insets(0,0,0,0));
//        int anchoTabla = 500;// monitor de 15"
        int anchoPnlThumb = 1780; //monitor de 19" - Default
//        int anchoPnlThumb = 1200; //monitor de 14" - Default


        int anchoCombo = 470;
        int altTabla = 960;//660;
        int alt2 = 18;
        int alt3 = 40;
        int sep = 1;
        int ancChk = 110;
        int ancIc = 40;
        int anc2 = 120;
        int anc3 = 70;
        int ancFiltro = 80;
        int sepVertical = 20;


        //scrollPane1 = new JScrollPane(tabla);
        scrollPane1 = new JScrollPane(panelThumb);
        scrollPaneTree = new JScrollPane(tree,
                                          JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setBounds( sep, sep * 2 + alt2, anchoPnlThumb - 30, altTabla);
        cmbDirectorio.setBounds(sep, sep, anchoCombo, alt2);
        splitPane.setBounds(sep, sep * 2 + alt2, anchoPnlThumb, altTabla);

        int y2 = sep;
        int x1 = anchoCombo + sep;
        btnActualizar.setBounds(x1, y2, ancIc, alt2);
        x1 += sep + ancIc;
        btnPegar.setBounds(x1, y2, anc3, alt2);
        x1 += sep + anc3;

        chkSoloSeleccionado.setBounds(x1, y2, ancChk, alt2);

        int x2 = sep * 2 + anchoPnlThumb;

        y2 += alt2 + sep;
        btnGrabar.setBounds(x2, y2, anc2, alt2);

        y2 += alt2 + sep;
        btnLimpiar.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;


        /****    renombrar    ****/
        y2 += sepVertical;
        lblRenombrar.setBounds(x2 ,y2 , anc2, alt2);
        y2 += alt2;
        txtViejo.setBounds(x2 , y2, anc2, alt2);
        y2 += alt2;
        btnRenombrar.setBounds(x2, y2 , anc2, alt2);
        y2 += alt2;
        txtNuevo.setBounds(x2, y2, anc2, alt2);
        /****   fin renombrar   ****/

        /****   insertar    ***/
        y2 += sepVertical;
        lblInsertar.setBounds(x2 ,y2 , anc2, alt2);
        y2 += alt2;
        txtInsertar.setBounds(x2 , y2, anc2, alt2);
        y2 += alt2;
        cmbInsertar.setBounds(x2 , y2, anc2, alt2);
        y2 += alt2;
        txtInsertarEn.setBounds(x2, y2, anc2, alt2);
        /****   fin insertar    ***/

        /****    eliminar    ****/
        y2 += alt2 + sep * 2 + sepVertical;
        lblEliminarHasta.setBounds(x2 ,y2 , 73, alt2);
        txtEliminarHasta.setBounds(x2 + 71 ,y2 , 40, alt2);
        y2 += alt2;
        lblEliminarCant.setBounds(x2 ,y2 , 73, alt2);
        txtEliminarCant.setBounds(x2 + 71 ,y2 , 40, alt2);

        y2 += alt2 + sep * 4 + sepVertical;
        /****   fin eliminar   ****/

        /****   FOTOS  ***/
        int yF = 1;
        int xF = 0;
        cmbInsertarFechas.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep;
        int altLst = alt2 * 10;
        listScrollPaneInsertarNombres.setBounds(xF, yF, anc2, altLst);
        yF += altLst + sep;
        txtRenombrarFotos.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep * 2;
        chkInsertarClic.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep * 2;
        btnInsertarExif.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep * 2;
        btnInsertarFecha.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep * 2;
        txtAgregarAlFinal.setBounds(xF, yF, anc2, alt3);
        yF += alt3 + sep * 2;

        panelFotos.setBounds(x2, y2, anc2, yF);
        /****   FIN FOTOS  ***/

        /****   MP3    ***/
        int yPM = y2;


        Dimension dim = new Dimension(x2 + anc2 + sep ,
                                      sep * 4 + altTabla + 45);
//        setBounds(1, 1, (int) dim.getWidth()+20, (int) dim.getHeight()+20);
        setBounds(0, 0, (int) dim.getWidth()+30, (int) dim.getHeight()+30);
    }

    public boolean renombrarOnClick(){
        return chkInsertarClic.isSelected();
    }
    /**
     * Actualiza la tabla
     */
    public void actualizarThumb(){
        String str = getDirectorio();
        actualizarPanelThumb(str);
    }
/*****************************************************************************/
/*                               Eventos                                     */
/*****************************************************************************/

    private void actualizarPanelThumb(String pDir){
        File file = new File (pDir);
        if (!file.exists()){
            setTitle("No existe el directorio: " + pDir);
            return ;
        }else
            setTitle("");

        GUI.startWaitCursor(dis.getRootPane());

        panelThumb.removeAll();

        if (pDir == null || pDir.trim().equals(""))
            return;
        File dir = new File(pDir);
        if (!dir.exists() || !dir.isDirectory())
            return;

        try {
            List<String> directorios = new ArrayList<String>();
            directorios.add(pDir);

            String ext = "";
            String[] exts;
            {
                String extsFot = System.getProperty("EXTENSIONES_FOTOS");
                if (extsFot != null && !extsFot.trim().equals("")){
                    StringTokenizer tok =
                            new StringTokenizer(extsFot, ";", false);
                    exts = new String[tok.countTokens()];
                    int i = 0;
                    while (tok.hasMoreTokens()){
                        String n = tok.nextToken().trim();
                        exts[i++] = n;
                    }
                }
                else{
                    exts = new String[2];
                    exts[0] = "jpg";
                    exts[1] = "jpeg";
                }
            }

            int CANT_COL = 5;
            int c = 0;
            int f = 0;
            int in = 0;
            DirFileFilter filtro = new DirFileFilter(false, true, exts, true);
            for (String d : directorios) {
                File dd = new File(d);
                File[] archivos = dd.listFiles(filtro);
                ordenarArchivos(archivos);
                thumbs = new ArrayList<Thumb>();
                int cont = 0;
                for (File archivo : archivos) {
                    setTitle("Cargando " + cont + "/" + archivos.length);
                    Thumb thumb = new Thumb(archivo, this);
                    thumbs.add(thumb);
                    panelThumb.add(thumb.getPanel(), new GridBagConstraints(c, f, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(in, in, 10, in), 0, 0));
                    cont++;
                    c++;
                    if (c == CANT_COL){
                        c = 0;
                        f++;
                    }
                }
                setTitle("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            GUI.stopWaitCursor(dis.getRootPane());
        }
    }

    private void ordenarArchivos(File[] archivos){
        if (archivos == null || archivos.length < 1)
            return;
        String dir = archivos[0].getParent();
        String[] nom = new String[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            nom[i] = archivos[i].getName();
        }
        Util.ordenar(nom);
        for (int i = 0; i < nom.length; i++) {
            File f = new File(dir + "\\" + nom[i]);
            archivos[i] = f;
        }
    }

    public boolean soloSeleccionado(){
        return chkSoloSeleccionado.isSelected();
    }

    private void renombrarFotos() {
        String ren = txtRenombrarFotos.getText();
        if (ren == null)
            ren = "";
        String fecha;

        int x = ren.indexOf("_");
        if (x > 0){
            fecha = ren.substring(0, x);
            ren = ren.substring(x+1);
        }
        else{
            fecha = ren;
            ren = "";
        }
        control.renombrarFoto(fecha, ren);
    }


    private void mostrarDialogo(String x){
        JOptionPane.showMessageDialog(this, x);
    }

    private void mostrarError(String x){
        mostrarDialogo(x);
    }


    private void renombrar() {
        String viejo = txtViejo.getText();
        String nuevo = txtNuevo.getText();
        if (viejo == null || viejo.equals(""))
            return ;
        if (nuevo == null)
            nuevo = "";
        control.renombrar(viejo, nuevo);
    }

    public static void main(String[] argsK) {
        Renombrar.main(argsK);
    }

    public void setDirectorio(String dir){
        cmbDirectorio.addItem(dir);
        cmbDirectorio.setSelectedItem(dir);
    }

    //implementación de FileTransfer
    public void setFile(File f){
        if (f != null)
            setDirectorio(f.getAbsolutePath());
    }

    public void setFiles(List<File> files) {
        //actualizarThumb(files);
    }

    private String getDirectorio(){
        Object o = cmbDirectorio.getSelectedItem();
        if (o == null)
            return null;
        else
            return o.toString();
    }


    public static void log(String s){
        File f = new File ("C:\\jRen.log");
        try {
            UtilFile.agregarAlFinal(s, f);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<Thumb> getThumbs() {
        return thumbs;
    }

    public String getAInsertarEnClic() {
        return txtRenombrarFotos.getText();
    }
}