package p.aplic.renombrar;

import de.ueberdosis.mp3info.ID3Reader;
import de.ueberdosis.mp3info.gui.Id3JPanel;
import de.ueberdosis.mp3info.id3v2.ID3V2Frame;
import de.ueberdosis.mp3info.id3v2.ID3V2Tag;
import de.ueberdosis.mp3info.id3v2.ID3V2Writer;
import org.apache.log4j.Logger;
import p.fotos.Exif;
import p.gui.ComboItem;
import p.gui.LabelTextField;
import p.gui.TextField;
import p.gui.dd.FileTransfer;
import p.gui.treefs.Nodo;
import p.gui.treefs.TreeFileSystem;
import p.mp3.MP3File;
import p.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
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
    private static double ampliar = 1;

    private final ComboItem ITEM_FILTRO_FOTOS = new ComboItem(ITEM_FILTRO_FOTOS_LBL, "Fotos");
    static final String ITEM_FILTRO_TODO_LBL = "ITEM_FILTRO_TODO_LBL";
    static final String ITEM_FILTRO_MP3_LBL = "ITEM_FILTRO_MP3_LBL";
    static final String ITEM_FILTRO_DIRECTORIOS = "ITEM_FILTRO_DIRECTORIOS";
    static final String ITEM_FILTRO_FOTOS_LBL = "ITEM_FILTRO_FOTOS_LBL";
    static final String TEXTO_FILTRO_EDITABLE_LBL = "extensión";

    static final String ITEM_INS_AL_PRINCICIO = "ITEM_INS_AL_PRINCICIO";
    static final String ITEM_INS_AL_FINAL = "ITEM_INS_AL_FINAL";
    static final String ITEM_INS_AL_FINAL_DE_EXT = "ITEM_INS_AL_FINAL_DE_EXT";
    static final String ITEM_INS_ANTES_DE = "ITEM_INS_ANTES_DE";
    static final String ITEM_INS_DESPUES_DE = "ITEM_INS_DESPUES_DE";

    static final String MODO_FOTOS = "MODO_FOTOS";

    static final ComboItem ITEM_VACIO = new ComboItem("", "");

    static final int ITEM_TODO_POS = 1;
    static final int ITEM_MP3_POS = 0;
    static final int ITEM_DIRECTORIOS_POS = 2;
    static final int ITEM_FOTOS_POS = 3;
    static final int TEXTO_EDITABLE_POS = 4;

    private Robot robot = null;

    private TreeFileSystem tree = null;
    private JTable tabla;// = new JTable(); todo
    private JPanel panelMP3 = new JPanel(null);
    private JPanel panelFotos = new JPanel(null);

    private JScrollPane scrollPane1 = null;
    private JScrollPane scrollPaneTree = null;
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            tree, tabla);
    private JComboBox cmbDirectorio = new JComboBox();
    private JComboBox cmbFiltro = new JComboBox();
    private JComboBox cmbInsertar = new JComboBox();
    private JComboBox cmbInsertarNombres = new JComboBox();
    private JComboBox cmbInsertarFechas = new JComboBox();


    private JCheckBox chkRecursivo = new JCheckBox("Recursivo");
    private JCheckBox chkSoloSeleccionado = new JCheckBox("Solo seleccionado");


    private JButton btnGrabar = new JButton("Grabar");
    private JButton btnSeparador = new JButton("Separador");
    private JButton btnRenombrar = new JButton("V V V");
    private JButton btnMinuscula = new JButton("Mayú-Min");
    private JButton btnNumerar = new JButton("Numerar");
    private JButton btnCopiarPrimeroASegundo = new JButton("Copiar 1º a 2º");
    private LabelTextField txtAgregarAlFinal = new LabelTextField("Agregar al final");
    private JButton btnAAAAMMDD2AAAA_MM_DD = new JButton("A4MMDD->A-M-D");
    private JButton btnArch2Tit = new JButton("Arch. -> Tit.");
    private JButton btnSepMasArchATit = new JButton("Sep + A->T");
    private JButton btnLlenarConDirs = new JButton("Llenar con dirs");
    private JButton btnTodo = new JButton("Todo");

    private URL url = getClass().getResource("ic_reload.JPG");

    private Icon icon = new ImageIcon(url);
    private JButton btnActualizar = new JButton(icon);
    private JButton btnTit2Arch = new JButton("Tit. -> Arch.");
    private JButton btnMP3 = new JButton("MP3");
    private JButton btnPegar = new JButton("Pegar");
    private JButton btnEliminarDer = new JButton("desde la der.");
    private JButton btnEliminarIzq = new JButton("desde la izq.");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JCheckBox chkLoguear = new JCheckBox("Loguear", true);

    private JCheckBox chkInsertarClic = new JCheckBox("Insertar c/clic");
    private JButton btnInsertarExif = new JButton("Insertar EXIF");
    private JButton btnInsertarFecha = new JButton("Insertar fecha");

    private JLabel lblRenombrar = new JLabel("Renombrar");
    private JLabel lblInsertar = new JLabel("Insertar");
    private JLabel lblEliminarHasta = new JLabel("Eliminar hasta: ");
    private JLabel lblEliminarCant = new JLabel("Eliminar cant.: ");
    private JLabel lblMP3 = new JLabel("------------MP3-----------");
    private JLabel lblFotos = new JLabel("----------Fotos----------");

    private JCheckBox chkArtista = new JCheckBox("Artista");
    private JCheckBox chkAlbum = new JCheckBox("Álbum");
    private JCheckBox chkAnnus = new JCheckBox("Año");
    private JCheckBox chkComen = new JCheckBox("Comentario");


    private TextField txtViejo = new p.gui.TextField();
    private TextField txtNuevo = new TextField();
    private TextField txtArtista = new TextField();
    private TextField txtAlbum = new TextField();
    private TextField txtAnnus = new TextField();
    private TextField txtComen = new TextField();
    private TextField txtEliminarHasta = new TextField();

    private TextField txtEliminarCant = new TextField();

    private TextField txtInsertar = new TextField();
    private TextField txtInsertarEn = new TextField();
    private TextField txtRenombrarFotos = new TextField();

    private RenTableModel tablaModel = null;
    private Renombrar control = null;

    private boolean numerar = false;
    private boolean copiarPrimeroASegundo = false;
    private String textoDeCopiarPrimeroASegundo;
    private int contNumerar = 1;
    private boolean ignorarEvento = false;
    private boolean grabarID3v2 = true;

    private JPopupMenu popupMenu = new JPopupMenu();
    private RenombrarGUI dis = null;
    private Fila filaClic = null; //tiene la fila donde si hizo clic
    private JMenuItem itemVerID3v2 = null;


    private boolean soloNombres = false;

    public RenombrarGUI(Renombrar c) {
        dis = this;
        String s = System.getProperty("SOLO_NOMBRES");
        if (s != null && s.equalsIgnoreCase("s")){
            soloNombres = true;
            verPanelMP3(false);
        }
        String a = System.getProperty("AMPLIAR");
        if (a != null ){
            try {
                RenombrarGUI.ampliar = Double.parseDouble(a);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        control = c;

        tabla = new JTable(){
            public String getToolTipText(MouseEvent e) {
               //selecciono la fila
                int i = super.rowAtPoint(e.getPoint());
                Fila fila = tablaModel.getFila(i);
                final String archivo;
                try {
                    archivo = fila.getFile().getCanonicalPath();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return null;
                }
                if (archivo == null || !archivo.toLowerCase().endsWith(".jpg"))
                    return null;

                Exif exif = new Exif(archivo);
                ImageIcon ii = new ImageIcon(archivo);
                int h = ii.getIconHeight();
                int w = ii.getIconWidth();
                double proporcionHW = (double) h/w;
                //String x = "file:" + archivo;
                String x = "file:" + "D:\\P\\Fotos\\mdp\\_n\\2014-01-03_13 Juliana_tmb.jpg";
                int alt = (int) (proporcionHW * 150);
//                String encode = Base64.encode(exif.getThumbnailByte());
//                x = "data:image/jpeg;base64," + encode;
                return  "<html><img height='" + alt + "' width='150' src='" + x + "'>";
//                return  "<html><img height='" + alt + "' width='150' src='" + x + "'>";
            }
        };

        
        tablaModel = new RenTableModel();
        tabla.setModel(tablaModel);
        TableCellRenderer renderer = new CustomTableCellRenderer(this);
        tabla.setDefaultRenderer(Object.class, renderer);

        try {
            jbInit();
            robot = new Robot();
        }
        catch(Exception e) {
          logger.error(e.getMessage(), e);
        }

        tabla.setDragEnabled(true);
        p.gui.dd.FileTransferHandler fth = new p.gui.dd.FileTransferHandler(this);
        tabla.setTransferHandler(fth);

        btnGrabar.setTransferHandler(fth);
        panelMP3.setTransferHandler(fth);
        panelFotos.setTransferHandler(fth);
        
        cmbDirectorio.setTransferHandler(fth);
        splitPane.setTransferHandler(fth);

        super.setTransferHandler(fth);
        if (soloNombres){
            chkRecursivo.setSelected(false);
            cmbFiltro.setSelectedIndex(1);
        }
    }

    public RenTableModel getTableModel(){
        return tablaModel;
    }

    public List<Fila> getDatosTabla(){
        return tablaModel.getFilas();
    }

    public boolean modificarDirectorios(){
        Object o = cmbFiltro.getSelectedItem();
        if (o instanceof ComboItem){
            ComboItem ci = (ComboItem)o;
            return ci.getClave().equals(ITEM_FILTRO_DIRECTORIOS);
        }
        else
            return false;
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
        armarPopupMenu();
        setBounds();
        addComponentes();
        setPropiedades();

        addListeners();
        setVisible(true);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void armarPopupMenu()
    {
        JMenuItem item = new JMenuItem("Importar nombres desde panel");

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                armarPanelImportar();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Borrar ID3v2");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                armarPanelBorrarID3v2();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Usar Editor");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usarEditor();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Archivo -> Titulo | Artista"); 
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                armarPanelArchivo2TitArt();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Poner título");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                armarPanelBorrarPonerTitulo();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Configuración...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                armarPanelConfiguracion();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Obtener Artista desde Archivo...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                obtenerArtistaDesdeArchivo();
            }
        });
        popupMenu.add(item);

        itemVerID3v2 = new JMenuItem("Ver ID3 v2");
        itemVerID3v2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                armarPanelID3v2();
            }
        });
        popupMenu.add(itemVerID3v2);
    }

    private void obtenerArtistaDesdeArchivo(String d, String h, boolean ot) {
        control.obtenerArtistaDesdeArchivo(d, h, ot);
    }

    private void obtenerArtistaDesdeArchivo() {
        final JDialog jd = new JDialog(this);
        JLabel lblD = new JLabel("Desde:");
        final JTextField txtD = new JTextField(".");

        JLabel lblH = new JLabel("Hasta:");
        final JTextField txtH = new JTextField("-");

        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");

        final JCheckBox obtenerTitulo = new JCheckBox("Obtener título", true);

        cancelar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(false);
            }
        });
        aceptar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String d = txtD.getText();
                String h = txtH.getText();
                if (d == null || d.equals("") || h == null || h.equals("") )
                    return ;
                boolean ot = obtenerTitulo.isSelected();
                obtenerArtistaDesdeArchivo(d, h, ot);
                jd.setVisible(false);
            }
        });

        jd.getContentPane().setLayout(new GridLayout(0,2));
        jd.getContentPane().add(lblD);
        jd.getContentPane().add(txtD);
        jd.getContentPane().add(lblH);
        jd.getContentPane().add(txtH);
        jd.getContentPane().add(new JLabel(""));
        jd.getContentPane().add(obtenerTitulo);

        jd.getContentPane().add(aceptar);
        jd.getContentPane().add(cancelar);
        jd.setModal(true);
        jd.pack();
        jd.setVisible(true);

    }

    private void armarPanelID3v2() {
        if (filaClic == null
                || !filaClic.esMP3()){
            String msg = "No se hizo clic sobre un mp3";
            mostrarDialogo(msg);
        }
        else{
             final String fn = filaClic.getFileNameCompleto();
	        try {
    		    // Let ID3Reader do its thing...
                new ID3Reader (fn);
		        // If we have a V2 tag we can get into the nicer things...
		        final ID3V2Tag v2tag = ID3Reader.getV2Tag ();
		        if (v2tag != null) {
		            Vector displayPanels = new Vector ();
		    		Iterator iter = v2tag.getFrames ().iterator ();
		            // Gathering all available Panels that can display
		            // (if they could edit, just the better)
		            while (iter.hasNext ()) {
                        ID3V2Frame frm = (ID3V2Frame)iter.next ();

                        if (frm.canDisplay ())
                            // Although I asked the frame only if it can
                            // display its contents, I try to get an edit
                            // Component. All Frames should be safe there,
                            // ie. only creating display Components if
                            // they can't create an edit Component, even if
                            // the user asked for one.
                            displayPanels.add (frm.createJPanel(true, true));
		            }
                    // Usual GUI code...
                    final JFrame jframe = new JFrame ("mp3info editor");
                    jframe.setSize (500,600);
                    Container cp = jframe.getContentPane ();
                    cp.setLayout (new FlowLayout (FlowLayout.LEFT));
                    iter = displayPanels.iterator ();
                    while (iter.hasNext ()) {
                        Id3JPanel pnl = (Id3JPanel)iter.next();
                        cp.add(pnl);
                    }
                    jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    jframe.addWindowListener( new WindowAdapter () {
                        public void windowClosing (WindowEvent e) {
                            try
                            {
                                ID3V2Writer.writeTag (new File (fn), v2tag);
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                                mostrarError(e1.getMessage());
                            }
                        }
                        });
                    jframe.setVisible (true);

                    // Wait for the user to close the window...
                    // not too elegant, but this is example code, isn't it?
//                    try {
//                        while (!frameClosed) {
//                            Thread.sleep (500);
//                        }
//                    }
//		            catch (Exception ex) {};

		            // The user closed the window and presumably changed
		            // some information, so we write it.
//		            ID3V2Writer.writeTag (new File (args[0]), v2tag);
                }
                else{
                    String msg = "No tiene ID3 v2";
                    mostrarDialogo(msg);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                mostrarError(ex.getMessage());
            }
        }

    }

    private void armarPanelBorrarPonerTitulo(){
        final JFrame frame = new JFrame("Poner título");
        frame.getContentPane().setLayout(new BorderLayout());

        final TextField textArea = new TextField();

        JButton b = new JButton("Asignar");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String txt = textArea.getText();
                try {
                    GUI.startWaitCursor(dis.getRootPane());
                    control.ponerTitulo(txt);
                    frame.dispose();
                    GUI.stopWaitCursor(dis.getRootPane());
                } catch (Exception e1) {
                    mostrarError(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(b, BorderLayout.SOUTH);
        frame.setSize(300,200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    private void armarPanelConfiguracion(){
        final JFrame frame = new JFrame("Configuración");
        JPanel panel = new JPanel(new GridLayout(0,1));
        frame.getContentPane().setLayout(new BorderLayout());
        final Checkbox chk = new Checkbox("Grabar ID3 v2");
        chk.setState(grabarID3v2);

        final LabelTextField txtContFotosDesde = new LabelTextField("Contador fotos desde");


        JButton b = new JButton("Aceptar");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabarID3v2 = chk.getState();
                String cont = txtContFotosDesde.getText().trim();
                int contInt = 0;
                try {
                    contInt = Integer.parseInt(cont);
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                    cont = "";
                }
                try {
                    control.grabarID3v2(grabarID3v2);
                    if (!cont.equals(""))
                        control.setContadorDeFotosDesde(contInt);
                    frame.dispose();
                } catch (Exception e1) {
                    mostrarError(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        panel.add(chk);
        panel.add(txtContFotosDesde);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(b, BorderLayout.SOUTH);
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
    * Parsea el nombre del archivo para generar el título y el artista.
    */
   private void armarPanelArchivo2TitArt(){
       final JFrame frame = new JFrame("Archivo -> Título|Artista");
       frame.getContentPane().setLayout(new GridLayout(0,2));

       JButton b = new JButton("Asignar");

       final JLabel lblDesde = new JLabel("Desde");
       final JLabel lblHasta = new JLabel("Hasta");
       final JLabel lblSeparador = new JLabel("Separador");
       final TextField txtDesde = new TextField("_");
       final TextField txtHasta = new TextField(".mp3");
       final TextField txtSeparador = new TextField(" - ");
       final JCheckBox chkPrimeroArtista = new JCheckBox("Primero artista");
       b.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   GUI.startWaitCursor(dis.getRootPane());
                   String d = txtDesde.getText();
                   String h = txtHasta.getText();
                   String s = txtSeparador.getText();
                   boolean pa = chkPrimeroArtista.isSelected();
                   control.archivo2TitArt(d, h, s, pa);
                   frame.dispose();
                   GUI.stopWaitCursor(dis.getRootPane());
               } catch (Exception e1) {
                   mostrarError(e1.getMessage());
                   e1.printStackTrace();
               }
           }
       });
       frame.getContentPane().add(lblDesde);
       frame.getContentPane().add(txtDesde);
       frame.getContentPane().add(lblHasta);
       frame.getContentPane().add(txtHasta);
       frame.getContentPane().add(lblSeparador);
       frame.getContentPane().add(txtSeparador);
       frame.getContentPane().add(chkPrimeroArtista);
       frame.getContentPane().add(new JLabel(""));
       frame.getContentPane().add(b);
       frame.setSize(300,150);
       frame.setVisible(true);
       frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   }

    private void usarEditor(){
        p.aplic.exportar.GUI pEd = new p.aplic.exportar.GUI(false);
        pEd.ejecutar(null);
        pEd.setFile(new File(getDirectorio()));

        //armo y seteo el contenido del editor
//        int count = tablaModel.getRowCount();
//        StringBuffer sb = new StringBuffer();
//        for(int i = 0; i < count; i++) {
//            sb.append(tablaModel.getValueAt(i, RenTableModel.COLUMNA_ARCHIVO));
//            sb.append(Constantes.NUEVA_LINEA);
//        }
//        pEd.setText(sb);
    }

    private void armarPanelBorrarID3v2(){
        final JFrame frame = new JFrame("BorrarID3v2");
        frame.getContentPane().setLayout(new BorderLayout());

        final JTextArea textArea = new JTextArea();

        List<Fila> filas = tablaModel.getFilas();
        int tam = filas.size();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < tam; i++) {
            Fila fila = filas.get(i);
            if (fila.esDir() || fila.esArchivoNoMP3())
                continue;
            Boolean b = (Boolean) tablaModel.getValueAt(
                    i, RenTableModel.COLUMNA_TIENE_ID3_V2);
            if (b == null || b.equals(Boolean.FALSE))
                continue;
            sb.append(fila.getFileNameCompleto());
            sb.append(Constantes.NUEVA_LINEA);
        }
        textArea.setText(sb.toString());

        JButton b = new JButton("Borrar");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String txt = textArea.getText();
                try {
                    GUI.startWaitCursor(dis.getRootPane());
                    control.borrarID3v2(txt);
                    frame.dispose();
                } catch (Exception e1) {
                    mostrarError(e1.getMessage());
                    e1.printStackTrace();
                }
                finally{
                    GUI.stopWaitCursor(dis.getRootPane());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(b, BorderLayout.SOUTH);
        frame.setSize(600,600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void armarPanelImportar(){
        final JFrame frame = new JFrame("Importar");
        frame.getContentPane().setLayout(new BorderLayout());

        final JTextArea textArea = new JTextArea();
        JButton b = new JButton("Importar");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String txt = textArea.getText();
                control.importarNombres(txt);
                frame.dispose();
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(b, BorderLayout.SOUTH);
        frame.setSize(600,600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void armarCombos(){
        //Si se modifica el orden de los ítems se deben modificar
        //las constantes de arriba

        //combo cmbFiltro
        cmbFiltro.addItem(new ComboItem(ITEM_FILTRO_MP3_LBL, "MP3"));
        cmbFiltro.addItem(new ComboItem(ITEM_FILTRO_TODO_LBL, "Todo"));
        cmbFiltro.addItem(new ComboItem(ITEM_FILTRO_DIRECTORIOS, "Directorios"));
        cmbFiltro.addItem(ITEM_FILTRO_FOTOS);
        cmbFiltro.addItem(TEXTO_FILTRO_EDITABLE_LBL);
        //fn combo cmbFiltro


        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_PRINCICIO, "Al principio"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_FINAL, "Al final"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_FINAL_DE_EXT, "Al final de la ext"));
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

        //combo cmbInsertarNombres
        cmbInsertarNombres.addItem(ITEM_VACIO);
        String nombres = System.getProperty("NOMBRES");
        if (nombres != null && !nombres.trim().equals("")){
            StringTokenizer tok = new StringTokenizer(nombres, ";", false);
            while (tok.hasMoreTokens()){
                String n = tok.nextToken().trim();
                ComboItem item = new ComboItem(n, n);
                cmbInsertarNombres.addItem(item);
            }
        }
        //fin combo cmbInsertarNombres
    }

    private void setPropiedades(){
        splitPane.setDividerLocation(0.015);  //queda casi oculto
//        splitPane.setDividerLocation(0.15); así se ve bien

        cmbDirectorio.setEditable(true);
        cmbFiltro.setEditable(false);

        chkRecursivo.setSelected(true);
        chkSoloSeleccionado.setSelected(false);

        setFonts();

        //valores iniciales
        txtInsertarEn.setEnabled(false);

        boolean verMP3 = true;
        String s = System.getProperty("MODO");
        if (MODO_FOTOS.equals("s")){
            verMP3 = false;
        }
        verPanelMP3(verMP3);

        //se habilita uno solo
        txtEliminarCant.setEnabled(false);
        txtEliminarHasta.setEnabled(true);
    }

    private void setFonts(){
        Map map = new HashMap();
        map.put(TextAttribute.SIZE, new Float(11 * ampliar));
        Font f = new Font(map);
        txtViejo.setFont(f);
        txtNuevo.setFont(f);
        txtArtista.setFont(f);
        txtAlbum.setFont(f);
        txtAnnus.setFont(f);
        txtRenombrarFotos.setFont(f);
        txtComen.setFont(f);
        txtEliminarHasta.setFont(f);
        txtEliminarCant.setFont(f);

        btnRenombrar.setFont(f);
        btnSeparador.setFont(f);
        btnGrabar.setFont(f);
        btnMinuscula.setFont(f);
        btnNumerar.setFont(f);
        btnCopiarPrimeroASegundo.setFont(f);
        txtAgregarAlFinal.setFont(f);
        btnAAAAMMDD2AAAA_MM_DD.setFont(f);
        btnArch2Tit.setFont(f);
        btnSepMasArchATit.setFont(f);
        btnLlenarConDirs.setFont(f);
        btnTodo.setFont(f);
        btnTit2Arch.setFont(f);
        btnMP3.setFont(f);
        btnPegar.setFont(f);
        btnActualizar.setFont(f);
        btnEliminarDer.setFont(f);
        btnEliminarIzq.setFont(f);
        btnLimpiar.setFont(f);
        chkLoguear.setFont(f);
        btnInsertarExif.setFont(f);
        chkRecursivo.setFont(f);
        btnInsertarFecha.setFont(f);

        chkInsertarClic.setFont(f);
        chkSoloSeleccionado.setFont(f);
        chkArtista.setFont(f);
        chkAlbum.setFont(f);
        chkAnnus.setFont(f);
        chkComen.setFont(f);
        lblMP3.setFont(f);
        lblFotos.setFont(f);
        lblRenombrar.setFont(f);
        lblEliminarHasta.setFont(f);
        lblEliminarCant.setFont(f);

        cmbDirectorio.setFont(f);
        cmbFiltro.setFont(f);
        cmbInsertar.setFont(f);
        cmbInsertarFechas.setFont(f);
        cmbInsertarNombres.setFont(f);
    }

    private void verPanelMP3(boolean verMP3) {
        panelMP3.setVisible(verMP3);
        panelFotos.setVisible(!verMP3);
        chkInsertarClic.setSelected(false);

        if (!verMP3){
            cmbFiltro.setSelectedItem(ITEM_FILTRO_FOTOS);
        }
    }

    private void addListeners() {
        txtEliminarHasta.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                txtEliminarCant.setEnabled(false);
                txtEliminarHasta.setEnabled(true);
            }
        });

        txtEliminarCant.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                txtEliminarCant.setEnabled(true);
                txtEliminarHasta.setEnabled(false);
            }
        });

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e))
                    mostrarPopupMenu(e);
            }
        });

        lblMP3.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                verPanelMP3(false);
            }
        });

        lblFotos.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                verPanelMP3(true);
            }
        });

        txtArtista.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e)
                        || e.isControlDown()
                        || e.isAltDown()
                        || e.isShiftDown()){
                    String tem = txtArtista.getText();
                    tem = UtilString.mayusMinus(tem);
                    txtArtista.setText(tem);
                }
            }
        });

        txtAlbum.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e)
                        || e.isControlDown()
                        || e.isAltDown()
                        || e.isShiftDown()){
                    String tem = txtAlbum.getText();
                    tem = UtilString.mayusMinus(tem);
                    txtAlbum.setText(tem);
                }
            }
        });

        txtComen.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e)
                        || e.isControlDown()
                        || e.isAltDown()
                        || e.isShiftDown()){
                    String tem = txtComen.getText();
                    tem = UtilString.mayusMinus(tem);
                    txtComen.setText(tem);
                }
            }
        });

        cmbInsertarFechas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignorarEvento)
                    return;
                actualizarRenombrarFotos();
            }
        });

        cmbInsertarNombres.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignorarEvento)
                    return;
                //copio el nombre del combo al campo 'InsertarAlFinal'
                String n = cmbInsertarNombres.getSelectedItem().toString();
                txtAgregarAlFinal.setText(" " + n);

                actualizarRenombrarFotos();
            }
        });

        chkRecursivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignorarEvento)
                    return;
                actualizarTabla();
            }
        });

        cmbFiltro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignorarEvento)
                    return;
                Object o = cmbFiltro.getSelectedItem();
                if (o instanceof ComboItem)
                    cmbFiltro.setEditable(false);
                else{
                    cmbFiltro.setEditable(true);
                    if (TEXTO_FILTRO_EDITABLE_LBL.equals(o.toString()))
                        return;
                }

                actualizarTabla();
            }
        });

        cmbInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();

                if (item.getClave().equals(ITEM_INS_AL_PRINCICIO) || item.getClave().equals(ITEM_INS_AL_FINAL) ||
                        item.getClave().equals(ITEM_INS_AL_FINAL_DE_EXT)){
                    txtInsertarEn.setText("");
                    txtInsertarEn.setEnabled(false);
                }
                else
                    txtInsertarEn.setEnabled(true);

            }
        });

        cmbDirectorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarTabla();
            }
        });

        btnSeparador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabla.editingCanceled(new ChangeEvent(this));
                control.ponerSeparador();
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

        btnMinuscula.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabla.editingCanceled(new ChangeEvent(this));
                control.mayusculaMinuscula();
            }
        });


        btnCopiarPrimeroASegundo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copiarPrimeroASegundo = !copiarPrimeroASegundo;
				textoDeCopiarPrimeroASegundo = null;
                if (copiarPrimeroASegundo){
                    btnCopiarPrimeroASegundo.setText("Fin Copiar 1º a 2º");
                }
                else
                    btnCopiarPrimeroASegundo.setText("Copiar 1º a 2º");
            }
        });

        btnNumerar.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isRightMouseButton(e)){
                    control.numerar();
                }
            }
        });


        btnNumerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numerar = !numerar;
                if (numerar){
                    btnNumerar.setText("Fin numerar");
                    contNumerar = 1;
                }
                else
                    btnNumerar.setText("Numerar");
            }
        });

        btnEliminarDer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtEliminarHasta.isEnabled())
                    control.eliminarDesdeLaDerecha(txtEliminarHasta.getText());
                else
                    control.eliminarCantDesdeLaDerecha(txtEliminarCant.getText());
            }
        });
        btnEliminarIzq.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtEliminarHasta.isEnabled())
                    control.eliminarDesdeLaIzquierda(txtEliminarHasta.getText());
                else
                    control.eliminarCantDesdeLaIzquierda(txtEliminarCant.getText());
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int c = tabla.columnAtPoint(p);
                int f = tabla.rowAtPoint(p);
                Fila fila = tablaModel.getFila(f);
                filaClic = null;
                if (SwingUtilities.isRightMouseButton(e)){
                    filaClic = fila;
                    mostrarPopupMenu(e,true);
                }
                else{
                    if (e.isShiftDown() && fila.esDir()){
                        //subo un nivel   y muestro sólo los directorios
                        String dir = fila.getDirCompleto();
                        File file = new File(dir);
                        int anterior = cmbFiltro.getSelectedIndex();
                        ignorarEvento = true;
                        cmbFiltro.setSelectedIndex(ITEM_DIRECTORIOS_POS);
                        String nuevo = file.getParent();
                        setDirectorio(nuevo);
                        cmbFiltro.setSelectedIndex(anterior);
                        ignorarEvento = false;
                    }
                    else if (e.isControlDown() && fila.esDir()){
                        //bajo un nivel
                        String dir = fila.getDirCompleto();
                        setDirectorio(dir);
                    }
                    else if (numerar){
                        if (c == RenTableModel.COLUMNA_ARCHIVO){
                            String pre = Integer.toString(contNumerar);
                            if (pre.length() == 1)
                                pre = "0" + pre;
                            String viejo = (String) tabla.getValueAt(f, c);
                            String nuevo = pre + Renombrar.SEPARADOR + viejo;
                            tablaModel.setValueAt(nuevo, f, c);
                            tablaModel.fireTableDataChanged();
                            contNumerar++;
                        }
                    }
                    else if (chkInsertarClic.isSelected()){
                        if (c == RenTableModel.COLUMNA_ARCHIVO){
                            renombrarClic(f, c);
                        }
                    }
                    else if (copiarPrimeroASegundo){
                        if (c == RenTableModel.COLUMNA_ARCHIVO){
                            String viejo = (String) tabla.getValueAt(f, c);
                            if (textoDeCopiarPrimeroASegundo == null){
                                viejo = sacarExtension(viejo);
                                textoDeCopiarPrimeroASegundo = viejo;
                            }
                            else{
                                String ext = getExtension(viejo);
                                String nuevo = textoDeCopiarPrimeroASegundo + "." + ext;
                                textoDeCopiarPrimeroASegundo = null;
                                tablaModel.setValueAt(nuevo, f, c);
                                tablaModel.fireTableDataChanged();
                            }
                        }
                    }
                    else{
                        Object valueAt = tablaModel.getValueAt(f, c);
                        if (!(valueAt instanceof String))
                            return ;
                        String tem = (String)valueAt;
                        if (c == RenTableModel.COLUMNA_ARCHIVO){
                            robot.keyPress(KeyEvent.VK_F2);
                        }
                        else if (c == RenTableModel.COLUMNA_TITULO){
                            robot.keyPress(KeyEvent.VK_F2);
                        }
                        else if (c == RenTableModel.COLUMNA_ALBUM){
                            txtAlbum.setText(tem);
                            txtAlbum.requestFocus();
                        }
                        else if (c == RenTableModel.COLUMNA_ANNUS){
                            txtAnnus.setText(tem);
                            txtAnnus.requestFocus();
                        }
                        else if (c == RenTableModel.COLUMNA_ARTISTA){
                            txtArtista.setText(tem);
                            txtArtista.requestFocus();
                        }
                        else if (c == RenTableModel.COLUMNA_COMENTARIO){
                            txtComen.setText(tem);
                            txtComen.requestFocus();
                        }
                    }
                }
            }
        });

        btnArch2Tit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.arch2Tit();
            }
        });

        btnSepMasArchATit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.ponerSeparador();
                control.arch2Tit();
            }
        });

        btnTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //1) llena campos con directorio  y
                //      y modifica la tabla
                //2) Separador
                //3) Archivo a título
                //4) Graba
                try{
                    llenarCamposConDirectorio();
                    cambiarMP3();
                    control.ponerSeparador();
                    control.arch2Tit();
                    grabar();
                }catch(Exception er){
                    mostrarDialogo(er.getMessage());
                }
            }
        });


        btnLlenarConDirs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                llenarCamposConDirectorio();
            }
        });



        btnTit2Arch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.tit2Arch();
            }
        });

        btnMP3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarMP3();
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

        btnAAAAMMDD2AAAA_MM_DD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.AAAAMMDD2AAAA_MM_DD();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarTabla();
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

        if (tree != null){
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    Nodo n = (Nodo) tree.getLastSelectedPathComponent();

                    if (n == null)
                        return;
                    String x = n.getFile().getAbsolutePath();
                    actualizarTabla(x);
                }
            });
        }

        //Foco
        txtAlbum.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                chkAlbum.setSelected(true);
            }

            public void focusLost(FocusEvent e) {
            }
        });

        txtAnnus.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                chkAnnus.setSelected(true);
            }

            public void focusLost(FocusEvent e) {
            }
        });

        txtArtista.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                chkArtista.setSelected(true);
            }

            public void focusLost(FocusEvent e) {
            }
        });

        txtComen.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                chkComen.setSelected(true);
            }

            public void focusLost(FocusEvent e) {
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

        txtAlbum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarMP3();
            }
        });

        txtAnnus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarMP3();
            }
        });
        txtRenombrarFotos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renombrarFotos();
            }
        });

        txtArtista.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarMP3();
            }
        });
        txtComen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarMP3();
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

    private void renombrarClic(int f, int c) {
        String viejo = (String) tabla.getValueAt(f, c);
        if (!formatoValidoParaRenombrarClic(viejo))
            return ;
        String aInsertar = " " + txtRenombrarFotos.getText();
        //formato: AAAA-MM-DD_[contador] [nombres*].jpg
        int indEspacio = viejo.indexOf(" ");
        int indJPG = viejo.toLowerCase().indexOf(".jpg");
        if (indEspacio > 0 && indEspacio +1 != indJPG)//si ya hay otro nombre -> agrego guíon
            aInsertar = " -" + aInsertar;
        String nuevo = viejo.substring(0, indJPG) + aInsertar + ".jpg";
        tablaModel.setValueAt(nuevo, f, c);
        tablaModel.fireTableDataChanged();
    }

    private boolean formatoValidoParaRenombrarClic(String txt) {
        //AAAA-MM-DD_[contador] [nombres*].jpg
        if (txt.length() < 11){
            mostrarDialogo("Para renombrar se necesita formato: AAAA-MM-DD_[contador] [nombres*].jpg"  );
            return false;
        }
        else
            return true;
    }
    
    private void grabar() {
        numerar = false;
        btnNumerar.setText("Numerar");
        GUI.startWaitCursor(dis.getRootPane());
        try {
            control.grabar(chkLoguear.isSelected());
            actualizarTabla();
        }
        catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
        finally{
            GUI.stopWaitCursor(dis.getRootPane());
        }
    }

    private void llenarCamposConDirectorio() {
        //Si el directorio es de la forma:
        // [cualquier cosa]\ARTISTA\AÑO - DISCO
        //  (ej: C:\P\m\_129\Johnny Winter\1969 - Johnny Winter)
        //se llenan los campos Artista, año y álbum
        txtArtista.setText("");
        txtAnnus.setText("");
        txtAlbum.setText("");
        String dir = getDirectorio();
        try {
            if (dir.endsWith("\\"))
                dir = dir.substring(0, dir.length() - 1);
            int ind = dir.lastIndexOf("\\");
            String tem = dir.substring(ind + 1);
            String ann = tem.substring(0, 4);
            String alb = tem.substring(7);
            tem = dir.substring(0, ind);
            ind = tem.lastIndexOf("\\");
            String art = tem.substring(ind + 1);
            txtArtista.setText(art);
            txtAnnus.setText(ann);
            txtAlbum.setText(alb);

            chkArtista.setSelected(true);
            chkAnnus.setSelected(true);
            chkAlbum.setSelected(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarPopupMenu(MouseEvent e)
    {
        mostrarPopupMenu(e, false);
    }

    private void mostrarPopupMenu(MouseEvent e, boolean clicEnTabla)
    {
        itemVerID3v2.setEnabled(clicEnTabla);
        popupMenu.show(this, e.getX(),  e.getY());
    }

    private void actualizarRenombrarFotos() {
        String mostrar = "";
        String f = cmbInsertarFechas.getSelectedItem().toString();
        if (f != null && f.length() > 9){
            mostrar = f.substring(0, 10);
        }
        String n = cmbInsertarNombres.getSelectedItem().toString();
        if (mostrar.equals(""))
            mostrar = n;
        else if (n != null && !n.equals(""))
            mostrar += "_" + n;

        txtRenombrarFotos.setText(mostrar);
    }

    private void limpiar() {
        txtAlbum.setText("");
        txtAnnus.setText("");
        txtRenombrarFotos.setText("");
        txtArtista.setText("");
        txtComen.setText("");
        txtEliminarHasta.setText("");
        txtEliminarCant.setText("");
        txtInsertar.setText("");
        txtInsertarEn.setText("");
        txtNuevo.setText("");
        txtViejo.setText("");

        chkAlbum.setSelected(false);
        chkAnnus.setSelected(false);
        chkArtista.setSelected(false);
        chkComen.setSelected(false);
    }

    private void insertar(){
        String nue = txtInsertar.getText();
        String cond = txtInsertarEn.getText();
        ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();
        if (item.getClave().equals(ITEM_INS_AL_FINAL))
            control.insertarAlFinal(nue);
        else if (item.getClave().equals(ITEM_INS_AL_FINAL_DE_EXT))
            control.insertarAlFinal(nue, false);
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
        getContentPane().add(cmbFiltro, null);
        getContentPane().add(chkRecursivo, null);
        getContentPane().add(chkSoloSeleccionado, null);
        getContentPane().add(btnGrabar, null);
        getContentPane().add(btnLimpiar, null);
        getContentPane().add(chkLoguear, null);
        getContentPane().add(btnRenombrar, null);
        getContentPane().add(btnMinuscula, null);

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

        getContentPane().add(btnEliminarDer, null);
        getContentPane().add(btnEliminarIzq, null);

        //panel MP3
        panelMP3.add(lblMP3, null);
        panelMP3.add(btnSeparador, null);
        panelMP3.add(btnNumerar, null);
        panelMP3.add(btnArch2Tit, null);
        panelMP3.add(btnTit2Arch, null);
        panelMP3.add(btnSepMasArchATit, null);
        panelMP3.add(btnLlenarConDirs, null);
        panelMP3.add(btnTodo, null);
        panelMP3.add(chkArtista, null);
        panelMP3.add(txtArtista, null);
        panelMP3.add(chkAlbum, null);
        panelMP3.add(txtAlbum, null);
        panelMP3.add(chkAnnus, null);
        panelMP3.add(txtAnnus, null);
        panelMP3.add(chkComen, null);
        panelMP3.add(txtComen, null);
        panelMP3.add(btnMP3, null);
        panelMP3.add(btnCopiarPrimeroASegundo, null);
        getContentPane().add(panelMP3, null);

        //panel fotos
        panelFotos.add(lblFotos, null);
        panelFotos.add(cmbInsertarFechas, null);
        panelFotos.add(cmbInsertarNombres, null);
        panelFotos.add(txtRenombrarFotos, null);
        panelFotos.add(chkInsertarClic, null);
        panelFotos.add(btnInsertarExif, null);
        panelFotos.add(btnInsertarFecha, null);
        panelFotos.add(txtAgregarAlFinal, null);
        panelFotos.add(btnAAAAMMDD2AAAA_MM_DD, null);

        getContentPane().add(panelFotos, null);
    }

    private void setBounds(){
        chkSoloSeleccionado.setMargin(new Insets(0,0,0,0));
//        int anchoTabla = 500;// monitor de 15"
        int anchoTabla = (int)Math.round(1200 * ampliar); //monitor de 19" - Default
        String tamMonTemp = System.getProperty("TAM_MONITOR");
        if (tamMonTemp != null){
            try{
                int tamMon = Integer.parseInt(tamMonTemp);
                //Hago la regla de 3, sabiendo que
                //anchoTabla = 845 es para monitor de 19"
                anchoTabla = (1200 * tamMon) / 19;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        int anchoCombo = (int)Math.round(470 * ampliar);
        int altTabla = (int)Math.round(800 * ampliar);
        int alt2 = (int)Math.round(18 * ampliar);
        int alt3 = (int)Math.round(40 * ampliar);
        int sep = (int)Math.round(1 * ampliar);
        int ancChk = (int)Math.round(110 * ampliar);
        int ancIc = (int)Math.round(40 * ampliar);
        int anc2 = (int)Math.round(120 * ampliar);
        int anc3 = (int)Math.round(70 * ampliar);
        int ancFiltro = (int)Math.round(80 * ampliar);
        int sepVertical = (int)Math.round(20 * ampliar);


        TableColumnModel cm = tabla.getColumnModel();
        int colAn = (int)Math.round(40 * ampliar);
        cm.getColumn(RenTableModel.COLUMNA_ANNUS).setMaxWidth(colAn);//80 = 10car
        cm.getColumn(RenTableModel.COLUMNA_TIENE_ID3_V2).setMaxWidth(15);
        cm.getColumn(RenTableModel.COLUMNA_BITRATE).setMaxWidth(45);
        if (soloNombres){
            cm.getColumn(RenTableModel.COLUMNA_ARCHIVO).setMinWidth(600);
            anc2 = anc2 + 60; //agrando el ancho de los campos que están  a la
            //der. de la tabla
        }

        scrollPane1 = new JScrollPane(tabla);
        scrollPaneTree = new JScrollPane(tree,
                                          JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setBounds( sep, sep * 2 + alt2, anchoTabla - 30, altTabla);
        cmbDirectorio.setBounds(sep, sep, anchoCombo, alt2);
        splitPane.setBounds(sep, sep * 2 + alt2, anchoTabla, altTabla);

        int y2 = sep;
        int x1 = anchoCombo + sep;
        btnActualizar.setBounds(x1, y2, ancIc, alt2);
        x1 += sep + ancIc;
        btnPegar.setBounds(x1, y2, anc3, alt2);
        x1 += sep + anc3;
        cmbFiltro.setBounds(x1, y2, ancFiltro, alt2);
        x1 += sep + ancFiltro;
        chkRecursivo.setBounds(x1, y2, ancChk, alt2);
        x1 += sep + ancChk;
        chkSoloSeleccionado.setBounds(x1, y2, ancChk, alt2);

        int x2 = sep * 2 + anchoTabla;

        y2 += alt2 + sep;
        btnGrabar.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        btnMinuscula.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        btnLimpiar.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep ;
        chkLoguear.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;


        /****    renombrar    ****/
        y2 += sepVertical;
        lblRenombrar.setBounds(x2 ,y2 , anc2, alt2);
        y2 += alt2;
        txtViejo.setBounds(x2 , y2, anc2, alt2);
        int ancBR = anc2;
        y2 += alt2;
        btnRenombrar.setBounds(x2, y2 , ancBR, alt2);
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


        y2 += alt2;
        btnEliminarIzq.setBounds(x2, y2 , anc2, alt2);
        y2 += alt2 + sep;
        btnEliminarDer.setBounds(x2, y2 , anc2, alt2);

        btnEliminarDer.setMargin(new Insets(0, 0, 0, 0));
        btnEliminarIzq.setMargin(new Insets(0, 0, 0, 0));

        y2 += alt2 + sep * 4 + sepVertical;
        /****   fin eliminar   ****/

        /****   FOTOS  ***/
        int yF = 1;
        int xF = 0;
        lblFotos.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep;
        cmbInsertarFechas.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep;
        cmbInsertarNombres.setBounds(xF, yF, anc2, alt2);
        yF += alt2 + sep;
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
        btnAAAAMMDD2AAAA_MM_DD.setBounds(xF, yF, anc2, alt2);
        yF += alt3 + sep * 2;

        panelFotos.setBounds(x2, y2, anc2, yF);
        /****   FIN FOTOS  ***/

        /****   MP3    ***/
        int yPM = y2;
        y2 = 1;
        int x2Tem = x2;
        x2 = 0;
        lblMP3.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnSeparador.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        btnNumerar.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        btnArch2Tit.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnTit2Arch.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnSepMasArchATit.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnLlenarConDirs.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnTodo.setBounds(x2, y2, anc2, alt2);

        y2 += alt2 + sep * 8;

        chkArtista.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        txtArtista.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        chkAlbum.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        txtAlbum.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        chkAnnus.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        txtAnnus.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        chkComen.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        txtComen.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        btnMP3.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep * 2;
        btnCopiarPrimeroASegundo.setBounds(x2, y2, anc2, alt2);
        y2 += alt2 + sep;
        panelMP3.setBounds(x2Tem, yPM, anc2, y2);

        Dimension dim = new Dimension(x2Tem + anc2 + sep ,
                                      sep * 4 + altTabla + 45);
        setBounds(1, 1, (int) dim.getWidth()+20, (int) dim.getHeight()+20);
    }

    /**
     * Actualiza la tabla
     */
    public void actualizarTabla(){
        String str = getDirectorio();
        actualizarTabla(str);
    }
/*****************************************************************************/
/*                               Eventos                                     */
/*****************************************************************************/

    private void actualizarTabla(String pDir){
        File f = new File (pDir);
        if (!f.exists()){
            setTitle("No existe el directorio: " + pDir);
            return ;
        }else
            setTitle("");

        GUI.startWaitCursor(dis.getRootPane());

        tablaModel.clear();

        if (pDir == null || pDir.trim().equals(""))
            return;
        File dir = new File(pDir);
        if (!dir.exists() || !dir.isDirectory())
            return;

        try {
            List<String> directorios;
            if (chkRecursivo.isSelected())
                directorios = UtilFile.directorios(dir);
            else{
                directorios = new ArrayList<String>();
                directorios.add(pDir);
            }

            //Armo el modelo de la tabla
            Fila fila;

            String ext = "";
            String[] exts = null;
            boolean soloDir = false;
            ComboItem item = null;
            Object o = cmbFiltro.getSelectedItem();

            if ( o instanceof ComboItem){
                item = (ComboItem) cmbFiltro.getSelectedItem();
                if (item.getClave().equals(ITEM_FILTRO_MP3_LBL)){
                    ext = ".MP3";
                }
                else if (item.getClave().equals(ITEM_FILTRO_FOTOS_LBL)){
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
                else if (item.getClave().equals(ITEM_FILTRO_DIRECTORIOS))
                    soloDir  = true;
            }
            else
                ext = "." + o.toString();

            if (exts == null){
                exts = new String[1];
                exts[0] = ext;
            }

            DirFileFilter filtro = new DirFileFilter(soloDir, !soloDir, exts, true);
            for (int i = 0; i < directorios.size(); i++) {
                String d = directorios.get(i);
                File dd = new File(d);
                File[] archivos = dd.listFiles(filtro);
                ordenarArchivos(archivos);
                if (soloDir && i > 0)
                    fila = new Fila(d, false);
                else
                    fila = new Fila(d, true);

                tablaModel.addFila(fila);
                if (item != null
                        && ( item.getClave().equals(ITEM_FILTRO_MP3_LBL)
                            || item.getClave().equals(ITEM_FILTRO_TODO_LBL)
                            || item.getClave().equals(ITEM_FILTRO_FOTOS_LBL))){
                    for (File ff : archivos) {
                        String name = ff.getName();
                        Fila filaX;
                        if (name.length() > 4 &&
                                name.toLowerCase().endsWith(".mp3")) {
                            MP3File mp3 = new MP3File(ff, grabarID3v2);
                            filaX = new Fila(ff, mp3);
                        } else
                            filaX = new Fila(ff);
                        tablaModel.addFila(filaX);
                    }
                }
                else if (ext != null && !ext.trim().equals("")){
                    for(File ff : archivos) {
                        Fila filaX = new Fila(ff);
                        tablaModel.addFila(filaX);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            tablaModel.fireTableDataChanged();
            GUI.stopWaitCursor(dis.getRootPane());
        }
    }

    private void actualizarTabla(List<File> files){
        GUI.startWaitCursor(dis.getRootPane());

        tablaModel.clear();

        try {
            //Armo el modelo de la tabla
            Fila fila;
            for(File file : files) {
                fila = new Fila(file);
                tablaModel.addFila(fila);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            tablaModel.fireTableDataChanged();
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

    public boolean isRowSelected(int f){
        return tabla.isRowSelected(f);
    }

    public boolean soloSeleccionado(){
        return chkSoloSeleccionado.isSelected();
    }

    private void renombrarFotos() {
        tabla.editingCanceled(new ChangeEvent(this));
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

    /**
     * Asigna a los mp3 los datos cargados en los textfields
     */
    private void cambiarMP3(){
        List<Fila> filas = tablaModel.getFilas();
        for (int i = 0; i < filas.size(); i++) {
            Fila fila = filas.get(i);
            if (fila.esDir() ||
                (soloSeleccionado() && !tabla.isRowSelected(i)))
                continue;

            MP3File mp3 = fila.getMP3File();
            if (chkAlbum.isSelected())
                mp3.setAlbum(txtAlbum.getText());
            if (chkAnnus.isSelected())
                mp3.setAnnus(txtAnnus.getText());
            if (chkArtista.isSelected())
                mp3.setArtista(txtArtista.getText());
            if (chkComen.isSelected())
                mp3.setComentario(txtComen.getText());
        }
        tablaModel.fireTableDataChanged();
    }

    private void mostrarDialogo(String x){
        JOptionPane.showMessageDialog(this, x);
    }

    private void mostrarError(String x){
        mostrarDialogo(x);
    }


    private void renombrar() {
        tabla.editingCanceled(new ChangeEvent(this));
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
}