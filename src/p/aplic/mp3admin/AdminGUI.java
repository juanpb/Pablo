package p.aplic.mp3admin;

import org.apache.log4j.Logger;
import p.aplic.renombrar.Renombrar;
import p.gui.JTextArea_;
import p.gui.TextField;
import p.util.Constantes;
import p.util.DirFileFilter;
import p.util.UtilString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;


public class AdminGUI extends JFrame implements Constantes{
    private static final Logger logger = Logger.getLogger(AdminGUI.class);
    private JPanel      _pnlPrincipal = new JPanel(null);
    private JPanel      _pnlBotonera = new JPanel(null);
    private JTextArea_   _textAreaIn = new JTextArea_();
    private JScrollPane _scrollPaneIn = new JScrollPane(_textAreaIn);
    private JTextArea_   _textAreaOut = new JTextArea_();
    private JScrollPane _scrollPaneOut = new JScrollPane(_textAreaOut);
    private JLabel      _lblDirSalida = new JLabel("Dir. salida");
    private JLabel      _lblKBPS = new JLabel("kbps");
    private JLabel      _lblEstado = new JLabel("");
    private JComboBox   _cmbKBPS = new JComboBox();
    private JCheckBox   _chkCopiarArchivos =
            new JCheckBox("Copiar archivos", true);
    private p.gui.TextField _txtDirSalida = new TextField();

    private JButton     _btnConvertir = new JButton("Convertir");
    private JButton     _btnCancelar = new JButton("Cancelar");
    private JButton     _btnHelp = new JButton("?");
    private JButton     _btnInsertarLinea = new JButton("Insertar línea");

    private String LAME = "C:\\P\\_env\\lame-3.96.1\\lame.exe";
    private static final String C = "\"";

    private static final String _linea = " ;--ta ; --tl ; --ty ; --tc ; " +
                        "-elim=\"\"; -elimH=\"\"; -b;";
    static private DirFileFilter _filtro = new DirFileFilter(false, "mp3");
//    private static final String _sep = "_"; //separador entre nro_track y título


//    public static void main(String[] args) throws Exception {
//        String dir = "C:\\P\\m\\_129\\_borrar";
//        File f = new File(dir);
//        File[] files = f.listFiles(_filtro);
//        System.out.println("files = " + files);
//        if (files != null){
//            System.out.println("files.length = " + files.length);
//        }
//    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminGUI();
            }
        });
/*
        Runtime rt = Runtime.getRuntime();
        String cmd = "cmd.exe /c C:\\P\\_env\\lame-3.96.1\\lame -q9 -b 192 C:\\P\\_env\\lame-3.96.1\\a.mp3 C:\\P\\_env\\z.mp3";
        File dir = new File("C:\\P\\_env\\lame-3.96.1");
        Process p = rt.exec(cmd, null, null);
        BufferedReader stdout = new BufferedReader( new InputStreamReader(p.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        int lExitValue = -1;
        do {
            if (stdout.ready())
              System.out.println(stdout.readLine());
            if (stderr.ready())
              System.out.println("Error: " + stderr.readLine());
            try {
                lExitValue = p.exitValue();
                System.out.println("lExitValue = " + lExitValue);
            }
            catch (IllegalThreadStateException ex) {
            }
        } while (lExitValue == -1);
*/
    }

    public AdminGUI(){
        try {
            jbInit();
        }
        catch(Exception e) {
          logger.error(e.getMessage(), e);
        }
    }

    private void jbInit() throws Exception {
        super.getContentPane().setLayout(null);
        armarCombos();
        setBounds();
        addComponentes();
        setPropiedades();

        addListeners();
        setVisible(true);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void armarCombos() {
        _cmbKBPS.addItem("32");
        _cmbKBPS.addItem("40");
        _cmbKBPS.addItem("48");
        _cmbKBPS.addItem("56");
        _cmbKBPS.addItem("64");
        _cmbKBPS.addItem("80");
        _cmbKBPS.addItem("96");
        _cmbKBPS.addItem("112");
        _cmbKBPS.addItem("128");
        _cmbKBPS.addItem("160");
        _cmbKBPS.addItem("192");
        _cmbKBPS.addItem("224");
        _cmbKBPS.addItem("256");
        _cmbKBPS.addItem("320");
    }

    private void setBounds() {
        int anBot = 140;
        int altBot = 20;
        int x = 5, y = 5;
        int anT = 550;
        int alT = 300;
        int sep = 10;

        _scrollPaneIn.setBounds(x, y, anT, alT);
        _lblEstado.setBounds(x, y + sep + alT, anT, altBot);
        _scrollPaneOut.setBounds(x, y + sep + alT + sep + altBot, anT, alT);
        int finTxtOut = y + sep + alT + alT + sep + altBot;
        x += anT + sep;
        _lblDirSalida.setBounds(x, y, anBot, altBot);
        y += altBot;
        _txtDirSalida.setBounds(x, y, anBot, altBot);
        y += altBot + sep;
        _lblKBPS.setBounds(x, y, anBot, altBot);
        y += altBot ;
        _cmbKBPS.setBounds(x, y, anBot, altBot);
        y += altBot + sep;
        _chkCopiarArchivos.setBounds(x, y, anBot, altBot);
        y += altBot + sep;
        _btnInsertarLinea.setBounds(x, y, anBot, altBot);
        y += altBot + sep;

        int anPnlP = x + anBot + sep;
        int altPnlP = finTxtOut;

        _pnlPrincipal.setBounds(0, 0, anPnlP, altPnlP);

        //botonera
        int despl = 250;
        _btnHelp.setBounds(despl,5 , anBot, altBot);
        despl += anBot + sep;
        _btnCancelar.setBounds(despl,5 , anBot, altBot);
        despl += anBot + sep;
        _btnConvertir.setBounds(despl, 5, anBot, altBot);

        int altPnlB = 40;
        _pnlBotonera.setBounds(0, altPnlP, anPnlP, altPnlB);

        setBounds(0,0,anPnlP, altPnlP + altPnlB + 20);
        _pnlBotonera.setBorder(BorderFactory.createLineBorder(Color.RED));
        _pnlPrincipal.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    }

    private void addComponentes() {
        _pnlPrincipal.add(_scrollPaneIn);
        _pnlPrincipal.add(_lblEstado);
        _pnlPrincipal.add(_scrollPaneOut);
        _pnlPrincipal.add(_lblDirSalida);
        _pnlPrincipal.add(_txtDirSalida);
        _pnlPrincipal.add(_lblKBPS);
        _pnlPrincipal.add(_cmbKBPS);
        _pnlPrincipal.add(_chkCopiarArchivos);
        _pnlPrincipal.add(_btnInsertarLinea);

        _pnlBotonera.add(_btnHelp);
        _pnlBotonera.add(_btnCancelar);
        _pnlBotonera.add(_btnConvertir);

        getContentPane().add(_pnlPrincipal);
        getContentPane().add(_pnlBotonera);
    }

    private void addListeners() {
        _btnHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAyuda();
            }
        });

        _btnInsertarLinea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String x = getTxtIn() + Constantes.NUEVA_LINEA + _linea;
                setTxtIn(x);
            }
        });

        _btnConvertir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertir();
            }
        });
    }

    private void convertir() {
        int bitrate = Integer.parseInt(_cmbKBPS.getSelectedItem().toString());
        String dirSal = _txtDirSalida.getText();

        if (dirSal == null || dirSal.trim().equals("")){
            mostrarDialogo("Falta ingresar direcotorio de salida");
            return ;
        }

        String txt = getTxtIn();
        StringTokenizer st = new StringTokenizer(txt, Constantes.NUEVA_LINEA, false);
        Vector tareas = new Vector();

        while(st.hasMoreTokens()){
            String s = st.nextToken();
            if (s != null){
                s = s.trim();
                if (!s.equals(""))
                    tareas.add(s);
            }
        }
        for (int i = 0; i < tareas.size(); i++) {
            String str = (String)tareas.get(i);
            ejecutarLinea(str, bitrate, dirSal);
            mostrarHecho(i, tareas);
        }
    }

    private void mostrarHecho(int i, Vector tareas) {
        String msg = "--------------------Pendientes--------------------" +
                NUEVA_LINEA;
        for (int j = i + 1; j < tareas.size(); j++) {
            String s = (String) tareas.elementAt(j);
            msg += s + NUEVA_LINEA;
        }
        msg += "--------------------Listos--------------------";
        for (int j = 0; j < i; j++) {
            String s = (String) tareas.elementAt(j);
            msg += s + NUEVA_LINEA;
        }
        setTxtIn(msg);
    }

    private void ejecutarLinea(String str, int bitrate, String dirSal) {
        StringTokenizer st = new StringTokenizer(str, ";", false);
        try {
            String art = null;
            String alb = null;
            String ann = null;
            int br = bitrate;
            String com = null;
            boolean id3v1Only = false;
            String elim = null;
            String elimH = null;
            String dirEnt = st.nextToken().trim();
            while (st.hasMoreTokens()){
                String tok = st.nextToken().trim();
                if (tok.startsWith("--ta")){
                    art = tok.substring(4).trim();
                }
                else if (tok.startsWith("--tl")){
                    alb = tok.substring(4).trim();
                }
                else if (tok.startsWith("--ty")){
                    ann = tok.substring(4).trim();
                }
                else if (tok.startsWith("-b")){
                    br = Integer.parseInt(tok.substring(2).trim());
                }
                else if (tok.startsWith("--tc")){
                    com = tok.substring(4).trim();
                }
                else if (tok.startsWith("--tl")){
                    alb = tok.substring(4).trim();
                }
                else if (tok.equals("--id3v1-only")){
                    id3v1Only = true;
                }
                else if (tok.startsWith("-elim=")){//comillas
                    elim = tok.substring(7, tok.length() - 1).trim();
                }
                else if (tok.startsWith("-elimH")){//comillas
                    elimH = tok.substring(8, tok.length() - 1).trim();
                }
            }
            convertir(dirEnt, dirSal, br, alb, art, ann, com, id3v1Only,
                        elim, elimH);
        } catch (Exception ex) {
            String msg = "'Cancelar' -> ignora " + NUEVA_LINEA +
                    "o nueva instr + 'Aceptar'";
            String res = JOptionPane.showInputDialog(msg, str);
            if (res == null) //se apretó cancelar
                ;
            else if (!res.trim().equals(""))
                //si se ingresó algo pruebo con la nueva inst
                ejecutarLinea(res, bitrate, dirSal);
        }
    }

    private void convertir(String dirEnt, String dirSal, int br, String alb,
                           String art, String ann, String com,
                           boolean id3v1Only, String elim, String elimH) throws IOException {
        System.out.println("dirEnt = '" + dirEnt + "'");
        File dirEntF = new File(dirEnt);
        if (!dirEntF.exists()){
            mostrarDialogo("No existe el directorio " + dirEntF);
            return ;
        }

        File[] files = dirEntF.listFiles();
//        File[] files = dirEntF.listFiles(_filtro);
        if (files == null){
            mostrarDialogo("El directorio " + dirEnt + " en listFiles dio null");
            return ;
        }
        if (files.length < 1){
            mostrarDialogo("El directorio " + dirEnt + " no tiene archivos");
            return ;
        }

        //Creo el directorio de salida
        String sal = dirSal + File.separator + art + File.separator;
        if (ann != null && !ann.trim().equals("")){
            sal += ann + " - ";
        }
        sal += alb;
        File dirSalF = new File(sal);
//        if (dirSalF.exists()){
//            String msg = "'Cancelar' -> ignora " + NUEVA_LINEA +
//                    "o nueva instr + 'Aceptar'";
//            int res = JOptionPane.showOptionDialog(this,msg, "",
//                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
//                    null, null, null);
//            if (res == null) //se apretó cancelar
//                ;
//            else if (!res.trim().equals(""))
//        }
        dirSalF.mkdirs();

        int cantArch = files.length;
        String msg = "Convirtiendo directorio " + dirEnt;
        for (int i = 0; i < cantArch; i++) {
            File file = files[i];
            String nn = getNuevoNombre(file, elim, elimH);
            _lblEstado.setText(msg + ". Archivo " + (i+1) + " / " + cantArch);
            ejecutar(file, nn, br, alb, art, ann, com, id3v1Only,
                    dirSalF.getAbsolutePath());
        }
        _lblEstado.setText("Listo el dirctorio: " + dirEnt);

    }

    private void ejecutar(File file, String nn, int br, String alb, String art,
                          String ann, String com,
                          boolean id3v1Only, String dirSal) throws IOException {

        String tit = nn.substring(nn.indexOf(Renombrar.SEPARADOR) + 1);
        if (tit.endsWith(".mp3")){
            tit = tit.substring(0, tit.length() - 4);
        }
        String cmd = "cmd.exe /c " + LAME +
                " -b " + br + //bitrate
                " --tt " + C + tit + C + //id3 título
                " --ta " + C + art + C + //id3 artista
                " --tl " + C + alb + C; //id3 álbum
        if (com != null)
            cmd += " --tc " + C + com + C; //id3 comentario
        if (ann != null)
            cmd += " --ty " + C + ann + C; //id3 año
        if (id3v1Only)
            cmd += " --id3v1-only";

        //nombres de entrada y de salida
        cmd += " " + C + file.getAbsolutePath() + C + " " +
                 C + dirSal + File.separator + nn + C;
        System.out.println("cmd = " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);

        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader stdout = new BufferedReader( isr);
        InputStreamReader isrError = new InputStreamReader(p.getErrorStream());
        BufferedReader stderr = new BufferedReader(isrError);
        int lExitValue = -1;

        /*
          Mientras corre tira por getErrorStream el sig. msg

          LAME version 3.96.1 (http://lame.sourceforge.net/)
          CPU features: MMX (ASM used), SSE, SSE2
          Using polyphase lowpass filter, transition band: 19383 Hz - 19916 Hz
          Encoding C:\P\_env\lame-3.96.1\a.mp3 to C:\P\_env\z.mp3
          Encoding as 44.1 kHz 192 kbps j-stereo MPEG-1 Layer III (7.3x) qval=9

              Frame          |  CPU time/estim | REAL time/estim | play/CPU |    ETA
               0/       ( 0%)|    0:00/     :  |    0:00/     :  |         x|     :
               0/3370   ( 0%)|    0:00/    0:00|    0:00/    0:00|   0.0000x|    0:00
              50/3370   ( 1%)|    0:00/    0:12|    0:00/    0:00|   7.2562x|    0:00
                 ...
            3381/3370  (100%)|    0:11/    0:11|    0:11/    0:10|   7.7980x|    0:00
          average: 192.0 kbps                    MS: 3384 (100.0%)

         Writing LAME Tag...done
         ReplayGain: -3.4dB

        */
        //manejar mensajes de salida--------------------------------------
        boolean cabecera = true;
        boolean pie = false;
        String msg = "";
        do {
            if (stdout.ready())
              System.out.println(stdout.readLine());
            if (stderr.ready()){
                String l = stderr.readLine();
                if (l == null || l.trim().equals(""))
                    continue;
                if (cabecera){
                    msg += NUEVA_LINEA + l;
                    if (l.indexOf("CPU time/estim") > 0){
                        cabecera = false;
                    }
                    _textAreaOut.setText(msg);
                }
                else if (pie){
                    msg += NUEVA_LINEA + l;
                    _textAreaOut.setText(msg);
                }
                else{
                    if (l.indexOf("average") > 0
                            || l.indexOf("Writing LAME") > 0){
                        pie = true;
                        msg += NUEVA_LINEA + l;
                        _textAreaOut.setText(msg);
                    }
                    else{
                        _textAreaIn.setText(msg + NUEVA_LINEA + l);
                    }
                }
            }
            try {
                lExitValue = p.exitValue();
            }
            catch (IllegalThreadStateException ex) {
            }
        } while (lExitValue == -1);

    }

    private String getNuevoNombre(File file, String elim, String elimH) {
        String name = file.getName();
        if (elim != null){
            name = UtilString.reemplazarTodo(name, elim, "");
        }
        if (elimH != null){
            name = UtilString.eliminarDesdeLaIzquierda(name, elimH);
        }
        name = Renombrar.ponerSeparador(name);
        name = Renombrar.mayMin(name);

        return name;
    }

    private void setTxtIn(String x){
        _textAreaIn.setText(x);
    }

    private String getTxtIn(){
        return _textAreaIn.getText();
    }
    private void mostrarAyuda() {
        String msg = "Dir.Entrada; --ta artista; --tl album;" +
                NUEVA_LINEA + "Opcionales: " + NUEVA_LINEA +
                "--ty año;"  + NUEVA_LINEA +
                "--tc comentario;"  + NUEVA_LINEA +
                "-b bitrate;"  + NUEVA_LINEA +
                "--id3v1-only;"  + NUEVA_LINEA +
                "-elim=\"string a eliminar\";"  + NUEVA_LINEA +
                "-elimH=\"eliminar hasta este string inclusive\";"  + NUEVA_LINEA +
                "";
        mostrarDialogo(msg);
    }

    private void setPropiedades() {
        //valores iniciales
        _cmbKBPS.setSelectedItem("160");
        _txtDirSalida.setText("C:\\P\\_env\\ZZ");
        setTxtIn(_linea);
    }
    private void mostrarDialogo(String x){
        JOptionPane.showMessageDialog(this, x);
    }
}
