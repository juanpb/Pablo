package p.aplic.exportar;

import org.apache.log4j.Logger;
import p.gui.ComboItem;
import p.gui.JTextArea_;
import p.gui.LabelTextField;
import p.gui.TextField;
import p.gui.dd.FileTransfer;
import p.gui.dd.FileTransferHandler;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Apr 1, 2009
 * Time: 10:02:23 AM
 */
public class GUI implements ActionListener, FileTransfer, Aplicacion {
    private String titulo = "Exportar";
    private JFrame frame = new JFrame(titulo);
    private JTextArea_ textArea = new JTextArea_();
    private TextField txtPath = new TextField();
    private JComboBox cmbExtensiones = new JComboBox();
    private TextField txtExtensiones = new TextField();
    private JCheckBox chkRutaCompleta = new JCheckBox("Ruta completa");
    private JCheckBox chkTabular = new JCheckBox("Tabular");
    private JCheckBox chkTabularParaDiscos = new JCheckBox("Tab.Disco");
    private JCheckBox chkTamano = new JCheckBox("Tamaño");
    private JCheckBox chkMostrarExtension = new JCheckBox("Mostrar extensión");
    private JCheckBox chkRecursivo = new JCheckBox("Recursivo");
    private JCheckBox chkSoloDirectorios = new JCheckBox("Solo Directorios");
    private JCheckBox chkPegarEnPortapapeles = new JCheckBox("Pegar Portap.", true);

    private String ITEM_TODO = "Todo";
    private String ITEM_INCLUIR = "Con extensiones:";
    private String ITEM_EXCLUIR = "Sin extensiones:";


    private JButton btnInvertir = new JButton("Invertir");
    private JButton btnInvertirLinea = new JButton("Invertir línea");
    private JButton btnReemplazarNLPorTab = new JButton("Reemplazar NL x Tab");
    private JButton btnOrdenar = new JButton("Ordenar");
    private JButton btnTrim = new JButton("Trim");
    private JButton btnSacarRepetipos = new JButton("Sacar repetidos");

    private JLabel lblRenombrar = new JLabel("Renombrar");
    private JLabel lblInsertar = new JLabel("Insertar");

    private JCheckBox chkSi = new JCheckBox("Si");
    private JComboBox cmbCondiciones = new JComboBox();
    private TextField txtCondicion = new TextField();


    private JLabel lblEliminarHasta = new JLabel("Eliminar hasta: ");
    private JLabel lblEliminarCant = new JLabel("Eliminar cant.: ");
    private JLabel lblReemplazarNL = new JLabel("Reemplazar NL: ");

    private JButton btnEliminarDer = new JButton("Desde la der.");
    private JButton btnEliminarIzq = new JButton("Desde la izq.");
    private JButton btnEliminarFila = new JButton("Eliminar fila");

    private JComboBox cmbInsertar = new JComboBox();
    private JPopupMenu popupMenu = new JPopupMenu();

    private TextField txtRenViejo = new p.gui.TextField();
    private TextField txtRenNuevo = new TextField();
    private TextField txtEliminarHasta = new TextField();
    private TextField txtEliminarCant = new TextField();
    private TextField txtReemmplazarNL = new TextField();
    private TextField txtInsertar = new TextField();
    private TextField txtInsertarEn = new TextField();

    private LabelTextField txtAgregarTabs = new LabelTextField("Agregar TAB");
    private JButton btnJumbo= new JButton("Jumbo");
    private JButton btnVea= new JButton("Vea");
    private JButton btnCoto= new JButton("Coto");
    private JButton btnMayMin= new JButton("MayMin");


    static final String ITEM_INS_AL_PRINCICIO = "ITEM_INS_AL_PRINCICIO";
    static final String ITEM_INS_AL_FINAL = "ITEM_INS_AL_FINAL";
    static final String ITEM_INS_ANTES_DE = "ITEM_INS_ANTES_DE";
    static final String ITEM_INS_DESPUES_DE = "ITEM_INS_DESPUES_DE";
    private static final String NOMBRE = "Editor/Exportar";
    private boolean disposeOnClose;
    private String ITEM_COND_EMPIEZA_CON = "Empieza con ";
    private String ITEM_COND_NO_EMPIEZA_CON = "No empieza con ";
    private String ITEM_COND_TERMINA_CON = "Termina con ";
    private String ITEM_COND_NO_TERMINA_CON = "TNo termina con ";
    private String ITEM_COND_CONTIENE = "Contiene";
    private String ITEM_COND_NO_CONTIENE = "No contiene";
    private static final Logger logger = Logger.getLogger(GUI.class);


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 final GUI gui = new GUI();
                 gui.ejecutar(null);
             }
        });
    }

    public GUI(boolean disposeOnClose) {
        this.disposeOnClose = disposeOnClose;
    }

    public GUI() {
        this(true);
    }

    public void setText(StringBuffer sb){
        textArea.setText(sb.toString());
    }



    private void initGUI(){
        cmbExtensiones.addItem(ITEM_TODO);
        cmbExtensiones.addItem(ITEM_INCLUIR);
        cmbExtensiones.addItem(ITEM_EXCLUIR);

        cmbCondiciones.addItem(ITEM_COND_CONTIENE);
        cmbCondiciones.addItem(ITEM_COND_NO_CONTIENE);
        cmbCondiciones.addItem(ITEM_COND_EMPIEZA_CON);
        cmbCondiciones.addItem(ITEM_COND_NO_EMPIEZA_CON);
        cmbCondiciones.addItem(ITEM_COND_TERMINA_CON);
        cmbCondiciones.addItem(ITEM_COND_NO_TERMINA_CON);


        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_PRINCICIO, "Al principio"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_AL_FINAL, "Al final"));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_ANTES_DE, "Antes de..."));
        cmbInsertar.addItem(new ComboItem(ITEM_INS_DESPUES_DE, "Después de..."));

        frame.setLayout(new BorderLayout());
        addListeners();
        valoresIniciales();
        addComponentes();
        setPropiedades();

        frame.setSize(1000,850);
        p.util.GUI.centrar(frame);
        frame.setVisible(true);
        if (disposeOnClose)
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //DD
        FileTransferHandler fth = new FileTransferHandler(this);
        txtPath.setTransferHandler(fth);

        textArea.setDragEnabled(true);
        chkTamano.setEnabled(true);

        armarPopupMenu();
    }

    private void armarPopupMenu(){
        JMenuItem item = new JMenuItem("DD-MM-AAAA->AAAA-MM-DD");
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DD_MM_AAAA_2_AAAA_MM_DD();
            }
        });
        popupMenu.add(item);

        JMenuItem item2 = new JMenuItem("Reemplazar c/valores");
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reemplazarPorListaValores();
            }
        });
        popupMenu.add(item2);
    }

    private void reemplazarPorListaValores() {
        final JFrame frame = new JFrame("Reemplazar por lista de valores");
        frame.getContentPane().setLayout(new GridBagLayout());

        JButton b = new JButton("Reemplazar");

        final JLabel lblClave = new JLabel("Clave a reemplazar");
        final TextField txtClave = new TextField();
        final JCheckBox chkDejarLineaVacia = new JCheckBox("Dejar línea vacía");
        final JLabel lblValores = new JLabel("Valores");
        final TextArea txtValores = new TextArea();
        final JFrame dis = frame;
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    p.util.GUI.startWaitCursor(dis.getRootPane());
                    String c = txtClave.getText();
                    String v = txtValores.getText();
                    boolean dlv = chkDejarLineaVacia.isSelected();
                    reemplazarPorListaValores(c, v, dlv);
                    frame.dispose();
                    p.util.GUI.stopWaitCursor(dis.getRootPane());
                } catch (Exception e1) {
                    JOptionPane.showInternalMessageDialog(null, e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        lblClave.setPreferredSize(new Dimension(150, 20));
        txtClave.setPreferredSize(new Dimension(250, 20));
        txtValores.setPreferredSize(new Dimension(250, 300));

        Insets insets = new Insets(10, 10, 10, 10);
        frame.getContentPane().add(lblClave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                insets, 0, 0));
        frame.getContentPane().add(txtClave, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                insets, 0, 0));
        frame.getContentPane().add(lblValores, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                insets, 0, 0));
        frame.getContentPane().add(txtValores, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                insets, 0, 0));
        frame.getContentPane().add(chkDejarLineaVacia, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                insets, 0, 0));
        frame.getContentPane().add(b, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                insets, 0, 0));
        frame.setSize(500,550);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void reemplazarPorListaValores(String clave, String valores, boolean dejarLineaVacia) {
        List<String> list = UtilString.getTextAsList(valores);
        String original = textArea.getText();
        StringBuffer sb = new StringBuffer();
        for (String nuevoValor : list) {
            String nuevo = UtilString.reemplazarTodo(original, clave, nuevoValor);
            sb.append(nuevo).append(Constantes.NUEVA_LINEA);
            if (dejarLineaVacia)
                sb.append(Constantes.NUEVA_LINEA);
        }
        mostrar(sb);
    }

    private void DD_MM_AAAA_2_AAAA_MM_DD() {
        List<String> list = getTextAsList();
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(Fechas.DD_MM_AAAA_2_AAAA_MM_DD(s)).append(Constantes.NUEVA_LINEA);
        }
        mostrar(sb);
    }


    private void setPropiedades() {
        txtPath.setToolTipText("Path");
        txtAgregarTabs.setToolTipText("Agrega TAB en las posiciones indicadas. Ej:1,4,7");
        btnJumbo.setToolTipText("arma una fila cada 4 y tabula");
        btnVea.setToolTipText("arma una fila cada 3 y tabula");
        chkSi.setToolTipText("Si está seleccionado, solo se aplican los cambios en las líneas que cumplan la condición");
    }

    private void addComponentes() {
        JPanel panelAuxArriba = new JPanel(new GridBagLayout());
        JPanel panelAuxArriba1 = new JPanel(new GridBagLayout());
        txtPath.setPreferredSize(new Dimension(70, 20));
        cmbExtensiones.setPreferredSize(new Dimension(130, 20));
        txtExtensiones.setPreferredSize(new Dimension(80, 20));
        chkPegarEnPortapapeles.setPreferredSize(new Dimension(110, 20));
        panelAuxArriba1.add(txtPath, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxArriba1.add(cmbExtensiones, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxArriba1.add(txtExtensiones, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxArriba1.add(chkPegarEnPortapapeles, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        JPanel panelAuxArriba2 = new JPanel(new GridLayout(1,0));
        panelAuxArriba2.add(chkRutaCompleta);
        panelAuxArriba2.add(chkTabular);
        panelAuxArriba2.add(chkTabularParaDiscos);
        panelAuxArriba2.add(chkTamano);
        panelAuxArriba2.add(chkMostrarExtension);
        panelAuxArriba2.add(chkRecursivo);
        panelAuxArriba2.add(chkSoloDirectorios);

        panelAuxArriba.add(panelAuxArriba1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxArriba.add(panelAuxArriba2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));


        //panel edición
        JPanel panelAuxDer = new JPanel(new GridBagLayout());
        int y = 0;
        panelAuxDer.add(btnInvertir, new GridBagConstraints(0, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnInvertirLinea, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnSacarRepetipos, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnOrdenar, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnTrim, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnReemplazarNLPorTab, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(lblReemplazarNL, new GridBagConstraints(0, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtReemmplazarNL, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        panelAuxDer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e))
                    mostrarPopupMenu(e);
            }
        });

        //renombrar
        panelAuxDer.add(lblRenombrar, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtRenViejo, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtRenNuevo, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        //insertar
        panelAuxDer.add(lblInsertar, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtInsertar, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(cmbInsertar, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtInsertarEn, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        //eliminar
        //insertar
        panelAuxDer.add(lblEliminarHasta, new GridBagConstraints(0, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtEliminarHasta, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));
        panelAuxDer.add(lblEliminarCant, new GridBagConstraints(0, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtEliminarCant, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        panelAuxDer.add(btnEliminarIzq, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnEliminarDer, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnEliminarFila, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //si
        panelAuxDer.add(chkSi, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(cmbCondiciones, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(txtCondicion, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));



        panelAuxDer.add(txtAgregarTabs, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));

        panelAuxDer.add(btnJumbo, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnVea, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelAuxDer.add(btnCoto, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        panelAuxDer.add(btnMayMin, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(20, 0, 0, 0), 0, 0));


        //panel dummy
        JPanel pnlD = new JPanel();
        panelAuxDer.add(pnlD, new GridBagConstraints(0, y, 2, 1, 0.1, 0.1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));


        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(panelAuxArriba, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelAuxDer, BorderLayout.EAST);
    }

    private void mostrarPopupMenu(MouseEvent e){
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void valoresIniciales() {
        //valores default
        chkMostrarExtension.setSelected(false);
        chkRecursivo.setSelected(false);
        chkSoloDirectorios.setSelected(false);
        chkRutaCompleta.setSelected(false);
        chkTabular.setSelected(true);

        txtInsertarEn.setEnabled(false);

        //se habilita uno solo
        txtEliminarCant.setEnabled(false);
        txtEliminarHasta.setEnabled(true);


        cmbExtensiones.setSelectedItem(ITEM_TODO);
    }

    private void addListeners() {
        txtPath.addActionListener(this);
        txtExtensiones.addActionListener(this);
        chkMostrarExtension.addActionListener(this);
        chkRutaCompleta.addActionListener(this);
        chkTabular.addActionListener(this);
        chkTabularParaDiscos.addActionListener(this);
        chkTamano.addActionListener(this);
        chkTabular.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if (chkTabular.isSelected()){
                        chkTabularParaDiscos.setSelected(false);
                    }
                }
        });
        chkTabularParaDiscos.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if (chkTabularParaDiscos.isSelected()){
                        chkTabular.setSelected(false);
                    }
                }
        });
        chkRecursivo.addActionListener(this);
        chkSoloDirectorios.addActionListener(this);
        cmbExtensiones.addActionListener(this);

        /*******************************************************/
        //Edición
        /*******************************************************/
        btnInvertirLinea.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                invertirLinea();
            }
        });
        btnInvertir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                invertir();
            }
        });
        btnSacarRepetipos.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sacarRepetidos();
            }
        });
        btnOrdenar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ordernar();
            }
        });
        btnTrim.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                trim();
            }
        });
        txtRenViejo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                renombrarViejoNuevo();
            }
        });
        txtRenNuevo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                renombrarViejoNuevo();
            }
        });


        txtReemmplazarNL.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reemplazarNLPor(txtReemmplazarNL.getText());
            }
        });


        txtInsertar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                renombrarViejoNuevo();
            }
        });

        txtAgregarTabs.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                agregarTABEnPosición(txtAgregarTabs.getText());
            }
        });

        btnJumbo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                jumbo();
            }
        });
        btnVea.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                vea();
            }
        });
        btnCoto.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                coto();
            }
        });
        btnMayMin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mayMin();
            }
        });

        btnReemplazarNLPorTab.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reemplazarNLPorTab();
            }
        });


        cmbInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();
                if (item.getClave().equals(ITEM_INS_AL_PRINCICIO)
                        || item.getClave().equals(ITEM_INS_AL_FINAL)){
                    txtInsertarEn.setText("");
                    txtInsertarEn.setEnabled(false);
                }else
                    txtInsertarEn.setEnabled(true);
            }
        });
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

        btnEliminarDer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtEliminarHasta.isEnabled())
                    eliminarDesdeLaDerecha(txtEliminarHasta.getText());
                else
                    eliminarCantDesdeLaDerecha(txtEliminarCant.getText());
            }
        });
        btnEliminarIzq.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtEliminarHasta.isEnabled())
                    eliminarDesdeLaIzquierda(txtEliminarHasta.getText());
                else
                    eliminarCantDesdeLaIzquierda(txtEliminarCant.getText());
            }
        });
        btnEliminarFila.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarFilas();
            }
        });
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
    }

    private void mayMin() {
        List<String> list = getTextAsList();
        List<String> sal = new ArrayList<String>();
        int pos = 0;
        for(String s : list) {
            String x = UtilString.minusculaSalvoPrimeraLetra(s);
            sal.add(x);
        }
        mostrar(sal);
    }

    private void actualizarTexto(){
        String path = txtPath.getText();
        File f = new File(path);
        if (!f.exists()){
            if (path.length() > 0)
                mostrarDlg("No existe el directorio: '" + path + "'");
            return ;
        }

        DirFileFilter filtro = new DirFileFilter(chkSoloDirectorios.isSelected(), false, true);
        Object selectedItem = cmbExtensiones.getSelectedItem();
        if (txtExtensiones.getText().length() > 0 && !selectedItem.equals(ITEM_TODO)){
            String[] ext = UtilString.separarAsArray(txtExtensiones.getText(), ",");
            filtro.setExtensiones(ext);
            filtro.setExcluirExtensiones(selectedItem.equals(ITEM_EXCLUIR));
        }

        if (chkTabularParaDiscos.isSelected()){
            //excluyo carpeta web y Disco i
            filtro.addExcluirDirsCaseUnsensitive("web");
            filtro.addExcluirDirsEmpiezanCon("Disco ");
            filtro.addExcluirDirsEmpiezanCon("Disc ");
        }

        try {
            java.util.List<File> files = UtilFile.archivos(f, filtro, chkRecursivo.isSelected());
            listar(files);
        } catch (Exception e) {
            mostrarDlg(e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private void listar(List<File> files) {
        long ini = System.currentTimeMillis();

        StringBuffer salida = new StringBuffer();
        for(File f : files) {
            salida.append(asString(f)).append(Constantes.NUEVA_LINEA);
        }

        mostrar(salida);

        long fin = System.currentTimeMillis();
        long ms = (fin - ini);
        String dem;
        if (ms < 1000)
            dem = ms + " milisegundos";
        else
            dem = ms/1000 + " segundos";
        frame.setTitle(titulo + ". Demoró " + dem);
    }

    private String asString(File f) {
        String salida = "";

        if (chkRutaCompleta.isSelected())
            salida += f.getAbsolutePath();
        else
            salida += f.getName();

        if (!chkMostrarExtension.isSelected()
                && f.isFile()/*no se saca nada si es un directorio*/){
            int i = salida.lastIndexOf(".");
            if (i > 0)
                salida = salida.substring(0, i);
        }

        if (chkTamano.isSelected() && !chkTabularParaDiscos.isSelected()){
            //el caso chkTabularParaDiscos.isSelected() es particular y se maneja más abajo
            long tam = 0;//lo paso a megas
            try {
                tam = UtilFile.getSize(f) / 1024 / 1024;
                salida += " ["+ tam + " MB]";
            } catch (Exception e) {
                salida += " [ problemas ]";
                mostrarDlg("Error al calcular el tamaño de " + f.getAbsolutePath());
                e.printStackTrace();
            }

        }



        if (chkTabular.isSelected()){
            //Para saber la profundidad, comparo el path original con el que estoy
            //imprimiendo ahora y a partir de ahí calculo el tab.
            String s = txtPath.getText();
            String sep = "\\";
            if (!s.endsWith(sep))
                s += sep;

            int x = UtilString.cantidadApariciones(s, sep);
            int y = UtilString.cantidadApariciones(f.getAbsolutePath(), sep);
            for(int i = 0; i < (y-x); i++) {
                salida = Constantes.TAB + salida;
            }
        }else if (chkTabularParaDiscos.isSelected()){
            //solo se tabula el primer nivel (Artista)
            String s = txtPath.getText();
            String sep = "\\";
            if (!s.endsWith(sep))
                s += sep;

            //para saber si corresponde al Artista o al álbum
            int x = UtilString.cantidadApariciones(s, sep);
            String s2 = f.getPath();
            int y = UtilString.cantidadApariciones(s2, sep);
            if(x == y) { //es artista
                salida = Constantes.TAB + salida;
            }
            else{//es álbum
                //calculo tamaño
                if (chkTamano.isSelected()){
                    long tam = UtilFile.getSize(f) / 1024 / 1024;//lo paso a megas
                    salida += " ["+ tam + " MB]";
                }
            }
        }

        return salida;
    }


    private void mostrar(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for(String s : list) {
            sb.append(s).append(Constantes.NUEVA_LINEA);
        }
        mostrar(sb);
    }

    private void mostrar(StringBuffer txt){
        mostrar(txt.toString());
    }
    private void mostrar(String txt){
        textArea.setText(txt);
        if (chkPegarEnPortapapeles.isSelected())
            Util.pegarEnElPortapapeles(txt);
    }
    private void mostrarDlg(String txt){
        JOptionPane.showMessageDialog(frame, txt);
    }

    public void actionPerformed(ActionEvent e) {
        actualizarTexto();
    }

    public void setFile(File f) {
        txtPath.setText(f.getAbsolutePath());
        actualizarTexto();
    }

    public void setFiles(List<File> files) {

    }
    /*********************************************************************************************/
    //Edición texto
    /*********************************************************************************************/
    private List<String> getTextAsList(){
        String s = textArea.getText().trim();
        return UtilString.getTextAsList(s);
    }

    private void invertir(){
        List<String> list = getTextAsList();
        StringBuffer sb = new StringBuffer();
        for(int i = list.size()-1; i >= 0; i--) {
            sb.append(list.get(i)).append(Constantes.NUEVA_LINEA);
        }
        mostrar(sb);
    }

    private void invertirLinea(){
        List<String> list = getTextAsList();
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            StringBuilder x = new StringBuilder(s);
            sb.append(x.reverse()).append(Constantes.NUEVA_LINEA);

        }
        mostrar(sb);
    }

    private void sacarRepetidos(){
        List<String> list = getTextAsList();
        list = UtilString.sacarRpepetidos(list);
        mostrar(list);
    }

    private void ordernar(){
        List<String> list = getTextAsList();
        list = Util.ordenarList(list);
        mostrar(list);
    }

    private void trim(){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                list.set(pos++, s.trim());
        }
        mostrar(list);
    }

    private boolean cumpleCondicion(String s){
        if (chkSi.isSelected()){
            String cond = (String) cmbCondiciones.getSelectedItem();
            final String c = txtCondicion.getText();
            if(cond.equals(ITEM_COND_CONTIENE))
                return s.contains(c);
            else if(cond.equals(ITEM_COND_NO_CONTIENE))
                return !s.contains(c);
            else if(cond.equals(ITEM_COND_EMPIEZA_CON))
                return s.startsWith(c);
            else if(cond.equals(ITEM_COND_NO_EMPIEZA_CON))
                return !s.startsWith(c);
            else if(cond.equals(ITEM_COND_TERMINA_CON))
                return s.endsWith(c);
            else if(cond.equals(ITEM_COND_NO_TERMINA_CON))
                return !s.endsWith(c);
            else
                throw new RuntimeException("no implementado 654");
        }
        else
            return true;
    }

    private void renombrarViejoNuevo(){
        String v = txtRenViejo.getText();
        String n = txtRenNuevo.getText();
        if (v.length() == 0)
            return ;

        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.reemplazarTodo(s, v, n);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    private void insertar(){
        String nue = txtInsertar.getText();
        String cond = txtInsertarEn.getText();
        ComboItem item = (ComboItem) cmbInsertar.getSelectedItem();
        if (item.getClave().equals(ITEM_INS_AL_FINAL))
            insertarAlFinal(nue);
        else if (item.getClave().equals(ITEM_INS_AL_PRINCICIO))
            insertarAlPrincipio(nue);
        else if (item.getClave().equals(ITEM_INS_ANTES_DE))
            insertarAntesDe(nue, cond);
        else if (item.getClave().equals(ITEM_INS_DESPUES_DE))
            insertarDespuesDe(nue, cond);
    }


    private void insertarDespuesDe(String nuevo, String cond){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.insertarDespuesDe(s, nuevo, cond);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    private void insertarAntesDe(String nuevo, String cond){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.insertarAntesDe(s, nuevo, cond);
            list.set(pos++, s);
        }
        mostrar(list);;
    }

    private void insertarAlPrincipio(String nuevo){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = nuevo + s;
            list.set(pos++, s);
        }
        mostrar(list);
    }

    private void insertarAlFinal(String nuevo){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.insertarAlFinal(s, nuevo, false);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    /**
     * Si no encuentra 'hasta' no hace nada
     */
    private void eliminarDesdeLaIzquierda(String hasta){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.eliminarDesdeLaIzquierda(s, hasta);
            list.set(pos++, s);
        }
        mostrar(list);
    }


    /*
    agrega el caracter TAB en las posiciones -1 recibidas como parametros.
    Ej:
    txt = "0123456789"
    posiciones: "2,4,8,10"
    salida = 01TAB23TAB4567TAB89
     */
    private void agregarTABEnPosición(String posiciones){
        StringTokenizer st = new StringTokenizer(posiciones, ",");
        List<Integer> indices = parsearPosiciones(posiciones);

        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = agregarTabs(indices, s);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    /*
    genera una linea x cada 3, separando por tab
     */
    private void jumboPreOctubre18(){

        List<String> list = getTextAsList();
        int pos = 0;

        StringBuilder sb = new StringBuilder();
        //agrego cabecera
        sb.append("Producto").append(Constantes.TAB_CHARACTER).append("Cant").append(Constantes.TAB_CHARACTER).
                append("Enviado").append(Constantes.TAB_CHARACTER).
                append("Precio unidad").append(Constantes.TAB_CHARACTER)./*.append("Precio total").append(Constantes.TAB_CHARACTER).
                append("Precio unidad sin iva").append(Constantes.TAB_CHARACTER).append("Precio total sin iva").append(Constantes.TAB_CHARACTER).
                */
                append(Constantes.NUEVA_LINEA) ;

        double acumSinIva = 0;
        double acumConIva = 0;
        DecimalFormat df = new DecimalFormat("#.##");

        for(int i = 0; i< list.size()-2; i=i+3) {
            String prod = list.get(i);
            String cant = list.get(i+1); //ej:6.00
//            cant = cant.replace(".", ","); //cambio separador de decimales
            String prec = list.get(i+2);
//            prec = prec.replace(".", ","); //cambio separador de decimales
            prec = prec.replace("$", ""); //cambio separador de decimales

            double cantDouble = Double.parseDouble(cant);
            double precioDouble = Double.parseDouble(prec);
            double precioPorUnidad = precioDouble / cantDouble;
            double precioPorUnidadSinIva = precioPorUnidad / 1.21; //asume iva 21%
            double precioTotalSinIva = precioPorUnidadSinIva * cantDouble; //asume iva 21%

            acumConIva +=precioDouble;
            acumSinIva +=precioTotalSinIva;

            sb.append(prod).append(Constantes.TAB_CHARACTER).append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER).
                    append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER). //enviado
                    append(df.format(precioPorUnidad))./*.append(Constantes.TAB_CHARACTER).append(df.format(precioDouble)).append(Constantes.TAB_CHARACTER).
                    append(df.format(precioPorUnidadSinIva)).append(Constantes.TAB_CHARACTER).append(df.format(precioTotalSinIva)).append(Constantes.TAB_CHARACTER).
                    */
                    append(Constantes.NUEVA_LINEA) ;

        }
        /*
        sb.append("Total").append(Constantes.TAB_CHARACTER).append(Constantes.TAB_CHARACTER).
                append(Constantes.TAB_CHARACTER).append(df.format(acumConIva)).append(Constantes.TAB_CHARACTER).
                append(Constantes.TAB_CHARACTER).append(df.format(acumSinIva)).append(Constantes.TAB_CHARACTER).
                append(Constantes.NUEVA_LINEA) ;      */
        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }

    private void jumbo(){

        List<String> list = getTextAsList();
        int pos = 0;

        StringBuilder sb = new StringBuilder();
        //agrego cabecera
        sb.append("Producto").append(Constantes.TAB_CHARACTER).append("Cant").append(Constantes.TAB_CHARACTER).
                append("Enviado").append(Constantes.TAB_CHARACTER).
                append("Precio unidad").append(Constantes.TAB_CHARACTER)./*.append("Precio total").append(Constantes.TAB_CHARACTER).
                append("Precio unidad sin iva").append(Constantes.TAB_CHARACTER).append("Precio total sin iva").append(Constantes.TAB_CHARACTER).
                */
                append(Constantes.NUEVA_LINEA).append(Constantes.NUEVA_LINEA) ;

        double acumSinIva = 0;
        double acumConIva = 0;
        DecimalFormat df = new DecimalFormat("#.##");

        int cantProds = 0;
        for(int i = 0; i< list.size()-2; ) {
            try {
                String prod = list.get(i++);
                list.get(i++);//producto repetido
                prod += ", " + list.get(i++);//marca
                String precTotal = list.get(i++); //$592,20
                String cantOXKg = list.get(i++); //ej:6.00 ; si el prod es x kilo, acá pone "$179,00 x kg"
                if (cantOXKg.startsWith("$")) {
                    cantOXKg = list.get(i++);//ahora sí es la cantidad, ej: 1.5kg
                    cantOXKg = cantOXKg.replace("kg", "");
                }
                list.get(i++);//precTotal de nuevo
                list.get(i++);//comentario
                list.get(i++);//no sustituir

                precTotal = precTotal.replace(".", ""); //saco separador de miles
                precTotal = precTotal.replace(",", "."); //cambio separador de decimales
                precTotal = precTotal.replace("$", ""); //saco pesos

                double cantDouble = Double.parseDouble(cantOXKg);
                double precioTDouble = Double.parseDouble(precTotal);
                double precioPorUnidad = precioTDouble / cantDouble;
                //double precioPorUnidadSinIva = precioPorUnidad / 1.21; //asume iva 21%
                //double precioTotalSinIva = precioPorUnidadSinIva * cantDouble; //asume iva 21%

                //acumConIva +=precioDouble;
                //acumSinIva +=precioTotalSinIva;

                sb.append(prod).append(Constantes.TAB_CHARACTER).append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER).
                        append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER). //enviado
                        append(df.format(precioPorUnidad))./*.append(Constantes.TAB_CHARACTER).append(df.format(precioDouble)).append(Constantes.TAB_CHARACTER).
                        append(df.format(precioPorUnidadSinIva)).append(Constantes.TAB_CHARACTER).append(df.format(precioTotalSinIva)).append(Constantes.TAB_CHARACTER).
                        */
                        append(Constantes.NUEVA_LINEA) ;
                cantProds++;
            } catch (Exception e) {
                sb.append(("list.get(i) = " + list.get(i))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+1) = " + list.get(i + 1))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+2) = " + list.get(i + 2))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+3) = " + list.get(i + 3))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+4) = " + list.get(i + 4))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+5) = " + list.get(i + 5))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+6) = " + list.get(i + 6))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+7) = " + list.get(i + 7))).append(Constantes.NUEVA_LINEA);
                sb.append(e.getLocalizedMessage());
                e.printStackTrace();
                break;
            }
        }
        agregarPie(sb, cantProds);

        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }
    private void vea(){

        List<String> list = getTextAsList();
        int pos = 0;

        StringBuilder sb = new StringBuilder();
        //agrego cabecera
        sb.append("Producto").append(Constantes.TAB_CHARACTER).append("Cant").append(Constantes.TAB_CHARACTER).
                append("Enviado").append(Constantes.TAB_CHARACTER).
                append("Precio unidad").append(Constantes.TAB_CHARACTER)./*.append("Precio total").append(Constantes.TAB_CHARACTER).
                append("Precio unidad sin iva").append(Constantes.TAB_CHARACTER).append("Precio total sin iva").append(Constantes.TAB_CHARACTER).
                */
                append(Constantes.NUEVA_LINEA).append(Constantes.NUEVA_LINEA) ;

        double acumSinIva = 0;
        double acumConIva = 0;
        DecimalFormat df = new DecimalFormat("#.##");

        int cantProds = 0;
        for(int i = 0; i< list.size()-2; ) {
            try {
                String prod = list.get(i++);
                String cantOXKg = list.get(i++); //ej:6.00 ; si el prod es x kilo, acá pone "$179,00 x kg"
                if (cantOXKg.startsWith("$")) {
                    cantOXKg = list.get(i++);//ahora sí es la cantidad, ej: 1.5kg
                    cantOXKg = cantOXKg.replace("kg", "");
                }
                String precTotal = list.get(i++); //$592,20

                precTotal = precTotal.replace("$", ""); //saco pesos

                double cantDouble = Double.parseDouble(cantOXKg);
                double precioTDouble = Double.parseDouble(precTotal);
                double precioPorUnidad = precioTDouble / cantDouble;
                //double precioPorUnidadSinIva = precioPorUnidad / 1.21; //asume iva 21%
                //double precioTotalSinIva = precioPorUnidadSinIva * cantDouble; //asume iva 21%

                //acumConIva +=precioDouble;
                //acumSinIva +=precioTotalSinIva;

                sb.append(prod).append(Constantes.TAB_CHARACTER).append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER).
                        append(df.format(cantDouble)).append(Constantes.TAB_CHARACTER). //enviado
                        append(df.format(precioPorUnidad))./*.append(Constantes.TAB_CHARACTER).append(df.format(precioDouble)).append(Constantes.TAB_CHARACTER).
                        append(df.format(precioPorUnidadSinIva)).append(Constantes.TAB_CHARACTER).append(df.format(precioTotalSinIva)).append(Constantes.TAB_CHARACTER).
                        */
                        append(Constantes.NUEVA_LINEA) ;
                cantProds++;
            } catch (Exception e) {
                sb.append(("list.get(i) = " + list.get(i))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+1) = " + list.get(i + 1))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+2) = " + list.get(i + 2))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+3) = " + list.get(i + 3))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+4) = " + list.get(i + 4))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+5) = " + list.get(i + 5))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+6) = " + list.get(i + 6))).append(Constantes.NUEVA_LINEA);
                sb.append(("list.get(i+7) = " + list.get(i + 7))).append(Constantes.NUEVA_LINEA);
                sb.append(e.getLocalizedMessage());
                e.printStackTrace();
                break;
            }
        }
        agregarPie(sb, cantProds);

        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }


    private void coto(){

        List<String> list = getTextAsList();
        int pos = 0;

        StringBuilder sb = new StringBuilder();
        //agrego cabecera
        sb.append("Producto").append(Constantes.TAB_CHARACTER).append("Cant").append(Constantes.TAB_CHARACTER).
                append("Enviado").append(Constantes.TAB_CHARACTER).
                append("Precio unidad").append(Constantes.TAB_CHARACTER).
                append(Constantes.NUEVA_LINEA).append(Constantes.NUEVA_LINEA) ;

        double acumSinIva = 0;
        double acumConIva = 0;
        DecimalFormat df = new DecimalFormat("#.##");

        int cantProds = 0;

        for(int i = 0; i< list.size(); i++) {
            try {
                //ej: Pure De Tomate Campagnola 520 Gr		$32.09		4.0		$128.36		Llevando 2:	80% 2da

                String linea = list.get(i);
                String[] split = linea.split(Constantes.TAB_CHARACTER+"");
                String prod = split[0];
                String precXUnidadTmp = split[2].replace("$", ""); //saco pesos;
                double precioPorUnidad = Double.parseDouble(precXUnidadTmp);

                String cantTmp = split[4];
                double cant = Double.parseDouble(cantTmp);
                String promoTmp = "";
                if (split.length > 8){
                    promoTmp = split[8];
                    if (split.length == 10)
                        promoTmp += split[9];
                }
                double promo = getPromoCoto(promoTmp);

                sb.append(prod).append(Constantes.TAB_CHARACTER).append(df.format(cant)).append(Constantes.TAB_CHARACTER).
                        append(df.format(cant)).append(Constantes.TAB_CHARACTER). //enviado
                        append(df.format(precioPorUnidad)).append(Constantes.TAB_CHARACTER).
                        append(df.format(promo)).
                        append(Constantes.NUEVA_LINEA) ;

                cantProds++;

            } catch (Exception e) {
                sb.append(("list.get(i) = " + list.get(i))).append(Constantes.NUEVA_LINEA);
                sb.append(e.getLocalizedMessage());
                e.printStackTrace();
                break;
            }
        }
        agregarPie(sb, cantProds);

        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }

    private double getPromoCoto(String promoTmp) {
        if ("".equals(promoTmp))
            return 1;
        if ("Llevando 2:80% 2da".equals(promoTmp))
            return 0.6;
        if ("Llevando 2:70% 2da".equals(promoTmp))
            return 0.65;
        if (promoTmp.startsWith("20%Dto"))
            return 0.8;
        if (promoTmp.startsWith("25%Dto"))
            return 0.75;
        if ("Llevando 2:30%Dto".equals(promoTmp))
            return 0.7;
        if ("-".equals(promoTmp))
            return 1;

        JOptionPane.showMessageDialog(null, "Revisar promo desconocida: '" + promoTmp + "'");

        return 1;
    }

    private void agregarPie(StringBuilder sb, int cantProds) {
        sb.append(Constantes.NUEVA_LINEA) ;
        sb.append("Cantidad de productos: " + cantProds);
        sb.append(Constantes.NUEVA_LINEA) ;
    }

    private void reemplazarNLPor(String nuevo){
        List<String> list = getTextAsList();
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        for(String s : list) {
            sb.append(s).append(nuevo) ;
        }
        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }

    private void reemplazarNLPorTab(){
        List<String> list = getTextAsList();
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        for(String s : list) {
            sb.append(s).append(Constantes.TAB_CHARACTER) ;

        }
        list.clear();
        list.add(sb.toString());
        mostrar(list);
    }

//    private void girar(){
//        List<String> list = getTextAsList();
//        if (list.size() == 0)
//            return ;
//
//        List<String> listSal = new ArrayList<String>();
//        int i = 0;
//        for (String s : list) {
//            if (s.endsWith(Constantes.TAB_CHARACTER+""))
//                list.set(i, s + " ");
//            i++;
//        }
//
//        final String str = list.get(0);
//        StringTokenizer x = new StringTokenizer(str, Constantes.TAB_CHARACTER+"", true);
//        while (x.hasMoreTokens()){
//            x.nextToken();
//            listSal.add("");
//        }
//
//        for(String s : list) {
//            StringTokenizer st = new StringTokenizer(s, Constantes.TAB_CHARACTER+"", true);
//            int pos = 0;
//            while (st.hasMoreElements()) {
//                String nv = listSal.get(pos) + st.nextToken();
//                listSal.set(pos, nv);
//                pos++;
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for(String s : listSal) {
//            sb.append(s).append(Constantes.NUEVA_LINEA) ;
//        }
//
//        list.clear();
//        list.add(sb.toString());
//        mostrar(list);
//    }

    static private String agregarTabs(List<Integer> indices, String entrada) {
        StringBuilder sb = new StringBuilder();
        int posDesde = 0;
        for (final Integer posHasta : indices) {
            final String c = entrada.substring(posDesde, posHasta);
            posDesde = posHasta;
            sb.append("'").append(c).append(Constantes.TAB_CHARACTER);
        }
        return sb.toString();
    }

    static private List<Integer> parsearPosiciones(String posiciones){
        StringTokenizer st = new StringTokenizer(posiciones, ",");
        List<Integer> indices = new ArrayList<Integer>();
        while (st.hasMoreElements()){
            String s = st.nextToken();
            Integer i = Integer.parseInt(s.trim());
            indices.add(i);
        }
        return indices;
    }

    /**
     * Respeta la extensión
     */
    private void eliminarDesdeLaDerecha(String hasta){
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.eliminarDesdeLaDerecha(s, hasta);
            list.set(pos++, s);
        }
        mostrar(list);
    }


    private void eliminarCantDesdeLaDerecha(String text) {
        int cant;
        try{
            cant = Integer.parseInt(text);
        }
        catch(Exception e){
            return ;
        }
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.eliminarCantDesdeLaDerecha(s, cant);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    private void eliminarFilas() {
        StringBuilder sb = new StringBuilder();
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(!cumpleCondicion(s))
                sb.append(s).append(Constantes.NUEVA_LINEA);
        }
        mostrar(sb.toString());
    }

    private void eliminarCantDesdeLaIzquierda(String text) {
        int cant;
        try{
            cant = Integer.parseInt(text);
        }
        catch(Exception e){
            return ;
        }
        List<String> list = getTextAsList();
        int pos = 0;
        for(String s : list) {
            if(cumpleCondicion(s))
                s = UtilString.eliminarCantDesdeLaIzquierda(s, cant);
            list.set(pos++, s);
        }
        mostrar(list);
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        return null;
    }

    @Override
    public String ejecutar(List<String> params) {
        initGUI();
        return null;
    }
}
