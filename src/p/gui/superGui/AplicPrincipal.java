package p.gui.superGui  ;

import org.apache.log4j.Logger;
import p.gui.TextField;
import p.gui.dd.FileTransfer;
import p.gui.dd.FileTransferHandler;
import p.util.GUI;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: 07/05/13
 * Time: 11:56
 */
public class AplicPrincipal extends JFrame implements FileTransfer {
    private static final Logger logger = Logger.getLogger(AplicPrincipal.class);
    private JComboBox<String> list = new JComboBox<String>();
    private JButton btnAbrir = new JButton("Abrir");
    private List aplics = new ArrayList();//espera objetos de tipo Aplicacion o String

    private FileTransferHandler fileTransferHandler = new p.gui.dd.FileTransferHandler(this);
    private List<JComponent> cmps;

    public static void main(String[] args) throws Exception {
        if (args.length != 1 ){
            System.out.println("Modo de uso: java AplicPrincipal archIni");
            System.exit(-1);
        }
        new AplicPrincipal(args[0]);
    }

    private AplicPrincipal(String ini) throws Exception {
        addListeners();
        init(ini);
    }

    private void addListeners() throws Exception {
        btnAbrir.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                try {
                    abrirSeleccionado();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                }
            }
        }
        );
    }

    /**
     * Abre panel pidiendo parámetros para abrir la aplicación seleccionada  si corresponde o obre directamente la aplicación
     */
    private void abrirSeleccionado() throws Exception {
        final int si = list.getSelectedIndex();
        final Object o = aplics.get(si);
        if (o instanceof String)
            abrirAplic((String)o);
        else{
            Aplicacion ap = (Aplicacion )o;

            if (ap.getParams() != null){
                JPanel pnl = getPanel(ap);

                JDialog jd = new JDialog(this, "Parámetros - " + ap.getName());
                jd.add(pnl);
                jd.setSize(new Dimension(360, 60 * (cmps.size()+1)));
                GUI.centrar(jd);

                jd.setVisible(true);
            }
            else{
                //si no necesita paráms se ejecuta directamente
                final String msj = ap.ejecutar(null);
                if (msj != null)
                    JOptionPane.showMessageDialog(this, msj);
            }
        }
    }

    /**
     *
     * @param linea clase "Nombre" "Archivo param"(opcional)
     */
    private void abrirAplic(String linea) throws Exception{
        StringTokenizer st = new StringTokenizer(linea.trim(), "\"", false);
//        StringTokenizer st = new StringTokenizer(linea.trim(), " ", false);

        final String className = st.nextToken().trim();
        System.out.println("className = " + className);
        final Class aClass = Class.forName(className);
        st.nextToken();//nombre
        String param = null;
        if (st.hasMoreTokens()){
            //como "Renombrar" "D:\P\jav" se pasa a 3 tokens ("Renombrar",  , "D:\P\jav")
            st.nextToken();//este caso sería el espacio entre "
        }
        if (st.hasMoreTokens()){
            param = st.nextToken().trim();
        }

        final Constructor constructor = aClass.getConstructor();
        if(param == null)
            constructor.newInstance();
        else{
//            param = param.substring(1, param.length() - 1);//saco las comillas
            Class[] argTypes = new Class[] { String[].class };

            final Method main = aClass.getDeclaredMethod("main", argTypes);
            final String[] mainArgs = new String[]{param};
            System.out.format("invoking %s.main()%n", aClass.getName());
            System.out.println("param = " + param);
            new Thread(new Runnable(){
                public void run() {
                    try {
                        main.invoke(null, (Object)mainArgs);
                    } catch (IllegalAccessException e) {
                        logger.error(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }).start();
        }
    }



    /**
     * Recupera los parámetros cargados, abre la aplicación seleccionada y muestra msj de finalización (si vino)
     */
    private void abrirAplic(Aplicacion ap)  {
        List<String> params = new ArrayList<String>();
        for (JComponent cmp : cmps) {
            if (cmp instanceof JTextField)
                params.add(((JTextField)cmp).getText());
            else if (cmp instanceof JComboBox)
                params.add(((JComboBox)cmp).getSelectedItem().toString());
            else if (cmp instanceof JCheckBox){
                final boolean b = ((JCheckBox) cmp).isSelected();
                params.add((Boolean.toString(b)));
            }
        }
        final String msj = ap.ejecutar(params);
        if (msj != null)
            JOptionPane.showMessageDialog(this, msj);
    }


    private JPanel getPanel(final Aplicacion ap) {
        List<Param> params = ap.getParams();

        JPanel panel = new JPanel(new GridBagLayout());
        cmps = new ArrayList<JComponent>();

        int fila = 0;
        for (Param pa : params) {
            JComponent cmp;
            panel.add(new JLabel(pa.getNombre()), new GridBagConstraints(0, fila, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 10, 10), 0, 0));
            final boolean esCheckbox = pa.isEsCheckbox();
            if (esCheckbox){
                cmp = new JCheckBox();
            }
            else {
                final List<String> vp = pa.getValoresPosibles();
                if (vp == null){
                    cmp = new TextField();
                    cmp.setTransferHandler(fileTransferHandler);
                }
                else{
                    cmp = new JComboBox<String>();
                    for (String s : vp) {
                        ((JComboBox)cmp).addItem(s);
                    }
                }
            }
            if (pa.getTooltip() != null)
                cmp.setToolTipText(pa.getTooltip());
            panel.add(cmp, new GridBagConstraints(1, fila, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 10, 10), 0, 0));
            cmps.add(cmp);
            fila++;
        }

        JButton btnCorrer = new JButton("Correr");
        panel.add(btnCorrer, new GridBagConstraints(0, fila, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0));
        btnCorrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirAplic(ap);
            }
        });


        return panel;
    }


    private void init(String ini) throws Exception {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(ini);
        for (String s : archivoPorLinea) {
            if (!s.trim().startsWith("!")){
                StringTokenizer st = new StringTokenizer(s.trim(), "\"", false);
                if (st.countTokens() == 1){// es de tipo Aplicación
                    final Class aClass = Class.forName(s);
                    final Constructor constructor = aClass.getConstructor();
                    Aplicacion ap = (Aplicacion ) constructor.newInstance();
                    list.addItem(ap.getName());
                    aplics.add(ap);
                }else{ //clase común
                    st.nextToken();//clase
                    String nombre = st.nextToken();
                    list.addItem(nombre);//nombre
                    aplics.add(s);
                }
            }
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(list, BorderLayout.CENTER);
        getContentPane().add(btnAbrir, BorderLayout.EAST );

        setSize(230, 80);
        GUI.centrar(this);
        setVisible(true);
    }

    @Override
    public void setFile(File f) {
        ((TextField)fileTransferHandler.getComponente()).setText(f.getAbsolutePath());
    }

    @Override
    public void setFiles(List<File> files) {
        throw new RuntimeException("No implementado");
    }
}
