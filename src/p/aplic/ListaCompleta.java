package p.aplic;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.GUI;
import p.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class ListaCompleta implements p.util.Constantes{
    private static final Logger logger = Logger.getLogger(ListaCompleta.class);

    //    String _guardarEn = "C:\\Documents and Settings\\Pablo\\Escritorio\\Música.txt";//wxp
//    String _guardarEnWinamp = "C:\\Documents and Settings\\Pablo\\Escritorio\\Música.m3u";//wxp
    private String ESCRITORIO = "C:\\Documents and Settings\\Administrador\\Escritorio\\";
    private BufferedWriter _bw;
    //_guardarEn = "C:\WINDOWS\Escritorio\" 'para win98
    //_guardarEn = "C:\Documents and Settings\Administrador\Escritorio\" 'para win 2000
    private boolean _winamp = false;

    private JFrame frame = new JFrame();
    private JTextArea txtDirectorios = new JTextArea();
    private JLabel lblCantNiv = new JLabel();
    private JLabel lblArchConExt = new JLabel();
    private JCheckBox chkSoloCarpetas = new JCheckBox();
    private boolean soloCarpetas = true;
    private JTextField txtCantNiv = new JTextField();
    private JTextField txtExt = new JTextField();
    private JCheckBox chkSinTab = new JCheckBox();
    private JButton btnHacer = new JButton();
    private JCheckBox chkImprimirExtension = new JCheckBox();
    private JButton btnWinamp = new JButton();
    private JButton btnVerif = new JButton();
    private static final String listaDefault = "V:\n";
    private JTextArea txtViejo = new JTextArea();
    private JTextArea txtNuevo = new JTextArea();
    private List<String> excluir = null;
//    private static String excluirDefault = "F:\\Mis documentos\\Exe\\" +
//            "Mis programas\\ListaCompretaExcluir.txt";

    public static void main(String[] args)throws Exception{
        String excluir = null;
        if (args.length > 0)
            excluir = args[0];

        new ListaCompleta(excluir);
    }

    public ListaCompleta() {
        excluir(null);
    }

    public ListaCompleta(String excluir) {
        excluir(excluir);
        try {
            jbInit();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void discos(File guardarEn, String raizDiscos) throws Exception {
        soloCarpetas = true;
        List<String> raices = new ArrayList<String>();
        raices.add(raizDiscos);
        excluir.add("web");
        hacer(guardarEn, raices);
    }

    private void excluir(String file){
        excluir = new ArrayList<String>();
        if (file == null)
            return ;
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while(lnr.ready()){
                String linea = lnr.readLine();
                if (linea != null && !linea.trim().equals(""))
                    excluir.add(linea.trim());
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void jbInit() throws Exception {
        frame.getContentPane().setLayout(null);
        lblCantNiv.setText("Cantidad de niveles:");
        lblCantNiv.setBounds(new Rectangle(35, 145, 126, 19));
        lblCantNiv.setPreferredSize(new Dimension(126, 19));
        lblCantNiv.setMinimumSize(new Dimension(126, 19));
        lblArchConExt.setEnabled(false);
        lblArchConExt.setText("Sólo archivos con ext.:");
        lblArchConExt.setBounds(new Rectangle(35, 238, 126, 19));
        lblArchConExt.setPreferredSize(new Dimension( 126, 19));
        lblArchConExt.setMinimumSize(new Dimension( 126, 19));
        chkSoloCarpetas.setSelected(soloCarpetas);
        chkSoloCarpetas.setText("Sólo carpetas");
        chkSoloCarpetas.setBounds(new Rectangle(35, 196, 129, 19));
        chkSoloCarpetas.setPreferredSize(new Dimension(129, 19));
        chkSoloCarpetas.setMinimumSize(new Dimension(129, 19));
        chkSoloCarpetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chkSoloCarpetas_actionPerformed(e);
            }
        });
        txtExt.setEnabled(false);
        txtExt.setBounds(new Rectangle(180, 238, 48, 19));
        txtExt.setPreferredSize(new Dimension(48, 19));
        txtExt.setMinimumSize(new Dimension(48, 19));
        chkSinTab.setText("Sin tabular");
        chkSinTab.setBounds(new Rectangle(35, 165, 129, 19));
        chkSinTab.setPreferredSize(new Dimension(129, 19));
        chkSinTab.setMinimumSize(new Dimension(129, 19));
        chkSinTab.setSelected(false);
        btnHacer.setBounds(new Rectangle(238, 140, 114, 26));
        btnHacer.setPreferredSize(new Dimension(110, 26));
        btnHacer.setMinimumSize(new Dimension(110, 26));
        btnHacer.setText("Hacer");
        btnHacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _btnHacer_actionPerformed(e);
            }
        });
        chkImprimirExtension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _chkImprimirExtension_actionPerformed(e);
            }
        });
        chkImprimirExtension.setEnabled(false);
        chkImprimirExtension.setText("Imprimir extensión");
        chkImprimirExtension.setBounds(new Rectangle(35, 219, 129, 19));
        chkImprimirExtension.setPreferredSize(new Dimension(129, 19));
        chkImprimirExtension.setMinimumSize(new Dimension(129, 19));
        btnWinamp.setBounds(new Rectangle(238, 171, 114, 27));
        btnWinamp.setPreferredSize(new Dimension( 110, 27));
        btnWinamp.setMinimumSize(new Dimension( 110, 27));
        btnWinamp.setText("Lista Winamp");
        btnWinamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _btnWinamp_actionPerformed(e);
            }
        });
        btnVerif.setBounds(new Rectangle(239, 202, 113, 27));
        btnVerif.setPreferredSize(new Dimension(110, 27));
        btnVerif.setMinimumSize(new Dimension(110, 27));
        btnVerif.setText("Verificar LW");
        btnVerif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _btnVerif_actionPerformed(e);
            }
        });
        txtViejo.setToolTipText("");
        txtViejo.setBounds(new Rectangle(20, 20, 0, 17));
        txtViejo.setPreferredSize(new Dimension(0, 17));
        txtViejo.setMinimumSize(new Dimension(0, 17));
        txtCantNiv.setBounds(new Rectangle(181, 145, 47, 19));
        txtCantNiv.setPreferredSize(new Dimension( 47, 19));
        txtCantNiv.setMinimumSize(new Dimension(47, 19));
        txtDirectorios.setBounds(new Rectangle(35, 13, 297, 116));
        txtDirectorios.setPreferredSize(new Dimension(297, 116));
        txtDirectorios.setMinimumSize(new Dimension(297, 116));
        txtNuevo.setBounds(new Rectangle(0, 0, 0, 17));
        txtNuevo.setPreferredSize(new Dimension(0, 17));
        txtNuevo.setMinimumSize(new Dimension(0, 17));
        frame.getContentPane().add(txtDirectorios, null);
        frame.getContentPane().add(lblCantNiv, null);
        frame.getContentPane().add(txtCantNiv, null);
        frame.getContentPane().add(chkSoloCarpetas, null);
        frame.getContentPane().add(chkSinTab, null);
        frame.getContentPane().add(lblArchConExt, null);
        frame.getContentPane().add(txtExt, null);
        frame.getContentPane().add(chkImprimirExtension, null);
        frame.getContentPane().add(txtViejo, null);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        txtDirectorios.setText(listaDefault);
        txtDirectorios.selectAll();

        frame.getContentPane().add(txtNuevo, null);
        frame.getContentPane().add(btnHacer, null);
        frame.getContentPane().add(btnVerif, null);
        frame.getContentPane().add(btnWinamp, null);

        frame.setSize(400,300);
        GUI.centrar(frame);
        frame.setVisible(true);
//        frame.pack();
//        frame.validate();
    }

    void _btnHacer_actionPerformed(ActionEvent e) {
        try{
            frame.setTitle("Ejecutando...");
            hacer();
            frame.setTitle("Listo");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    void _chkSoloCarpetas_actionPerformed(ActionEvent e) {
        soloCarpetas = chkSoloCarpetas.isSelected();
        if (soloCarpetas){
            lblArchConExt.setEnabled(false);
            txtExt.setEnabled(false);
            chkImprimirExtension.setEnabled(false);
        }else{
            lblArchConExt.setEnabled(true);
            txtExt.setEnabled(true);
            chkImprimirExtension.setEnabled(true);
        }
    }

    private long hacer() throws Exception{
        String abrirEn;
        File fileGuardarEn;
        File f = new File(ESCRITORIO);
        if (f.exists())
            abrirEn = ESCRITORIO;
        else
            abrirEn = "C:\\";

        JFileChooser chooser = new JFileChooser(abrirEn);
        chooser.setDialogTitle("Guardar lista en ...");
        int returnVal = chooser.showOpenDialog(null);
        long res = 0;
        if(JFileChooser.APPROVE_OPTION == returnVal) {
            fileGuardarEn = chooser.getSelectedFile();
            fileGuardarEn.createNewFile(); //si no existe lo crea
            StringTokenizer st = new StringTokenizer(txtDirectorios.getText(),
                                                 "\n", false);
            List<String> raices = new ArrayList<String>();

            //levanto los directorios raíces desde donde se hará la recursión
            while(st.hasMoreElements()){
                String path = ((String)st.nextElement()).trim();
                if (!"".equals(path))
                    raices.add(path);
            }

            res = hacer(fileGuardarEn, raices);
        }
        return res;
    }

    private long hacer(File guardarEn, List<String> raices) throws Exception{
        long fin, ini, res;

        ini = System.currentTimeMillis();
        _bw =  new BufferedWriter(new FileWriter(guardarEn));


        //recupero los subdirectorios de 'raíces'
        List<Object> dir = new ArrayList<Object>();
        Map<String, String> ht = new HashMap<String, String>();
        for (String raiz : raices) {
//            imprimir(new File(raiz), 0);

            try {
                File file = new File(raiz.trim());
                String[] subD = file.list(new DirFileFilter(true));
                for (String d : subD) {
                    if (excluirDirectorio(d))
                        continue;

                    dir.add(d);
                    if (ht.containsKey(d))
                        System.out.println("Se complicó. Existe una carpeta repetida: " + d);
                    String t = file.getAbsolutePath();
                    if (t.endsWith("\\"))
                        t = t.substring(0, t.length() - 1);
                    ht.put(d, t);
                }
            } catch (Exception e) {
                System.out.println("tem = " + raiz);
                logger.error(e.getMessage(), e);
            }
        }
        Object[] dirx = Util.ordenar(dir);
        for (Object aDirx : dirx) {
            String x = (String) aDirx;
            String z = ht.get(x);
            //System.out.println(z + "\\" + x);
            imprimir(new File(z + "\\" + x), 0);
        }
        _bw.close();
        fin = System.currentTimeMillis();
        res = fin - ini;
        return res;
    }

    private boolean excluirDirectorio(String dir){
        if (dir == null || dir.trim().equals(""))
            return true;
        for (String x : excluir) {

            if (x.compareToIgnoreCase(dir.trim()) == 0)
                return true;
        }
        return false;
    }

    /**
     * Imprime el directorio actual y luego recursivamente todos los
     * subdirectorios y luego, si tiene que
     * imprimir archivos (no 'chkSoloCarpetas'), lo hace.
     */
    private void imprimir(File pFile, int pTab ){
        try {
            String d = pFile.getName();
            if (pFile.getAbsolutePath().length() == 3){
                // es una raíz
                imprimir(d, pTab); //imprime el nombre del directorio
                if (!soloCarpetas)
                    imprimirCarpeta(pFile, pTab);
                return ;
            }
            else if (excluirDirectorio(d))
                return;
            if (!_winamp)
                imprimir(d, pTab); //imprime el nombre del directorio

            String[] files = pFile.list(new DirFileFilter(true));
            if (files != null && files.length > 0){
                Util.ordenar(files);
                for (String file : files) {
                    imprimir(new File(pFile.getCanonicalPath() + "\\" + file), pTab + 1);
                }
            }
            if (!soloCarpetas)
                imprimirCarpeta(pFile, pTab);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void imprimirCarpeta(File pFile, int pTab) {
        String ext = txtExt.getText().trim();
        File[] f = pFile.listFiles(new DirFileFilter(false, ext));
        if (f != null){
            for (File aF : f) {
                String name;
                if (_winamp)
                    name = aF.getAbsolutePath();
                else
                    name = aF.getName();
                if (!chkImprimirExtension.isSelected()) {
                    if (name.length() > 4)
                        name = name.substring(0, name.length() - 4);
                }
                imprimir(name, pTab + 1);
            }
        }
    }

    private void imprimir(String pTxt, int pTab){

        try{
            if (!chkSinTab.isSelected()){
                for (int i = 0; i < pTab; i++){
                    pTxt = TAB + pTxt;
                }
            }
            _bw.write(pTxt);
            _bw.newLine();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    void _chkImprimirExtension_actionPerformed(ActionEvent e) {

    }

    void _btnWinamp_actionPerformed(ActionEvent e) {
        frame.setTitle("Ejecutando...");
        long seg = 0;
        try{
            chkSinTab.setSelected(true);
            soloCarpetas = false;
            chkSoloCarpetas.setSelected(soloCarpetas);
            txtExt.setText("mp3");
            chkImprimirExtension.setSelected(true);
            _winamp = true;
            long tiempo = hacer();
            seg = tiempo/1000;
            _winamp = false;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        frame.setTitle("Listo en " + seg + " segundos.");
    }

    void _btnVerif_actionPerformed(ActionEvent e) {
        String abrirEn;
        File m3u;
        File f = new File("M:\\");
        if (f.exists())
            abrirEn = "M:\\";
        else
            abrirEn = "C:\\";
        JFileChooser chooser = new JFileChooser(abrirEn);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            m3u = chooser.getSelectedFile();
        }
        else
            return;
        String path = m3u.getParent();
        if (!path.endsWith("\\"))
            path = path + "\\";

        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(m3u));
            //Asumo: primero viene una línea que se ignora y luego pares de líneas
            //tales que la que vale es la segunda.
            lnr.readLine();
            while(lnr.ready()){
                lnr.readLine();
                String linea = lnr.readLine();
                f = new File(linea);
                if (!f.exists()){
                    f = new File(path + linea);
                    if (!f.exists()){
                        String titulo = "No existe el archivo: ";
                        int opt = JOptionPane.showConfirmDialog(null, linea, titulo,
                                                      JOptionPane.OK_CANCEL_OPTION);
                        if (opt == JOptionPane.CANCEL_OPTION)
                            return;
                    }
                }
            }
            lnr.close();
            JOptionPane.showMessageDialog(null, "Listo");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}