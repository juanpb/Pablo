package p.aplic.peliculas.util;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.PeliculasFrame;
import p.gui.LabelTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ExportarGUI extends JDialog {

    private JPanel contentPane = new JPanel();
    private JLabel lblAExportar = new JLabel("A exportar");
    private JLabel lblInfoAExportar = new JLabel("Info a exportar");
    private JButton btnGenerarTXT = new JButton("Generar txt");
    private JButton btnGenerarHTML = new JButton("Generar html");

    private JButton btnCancel = new JButton("Salir");
    private JCheckBox chkVisto = new JCheckBox("Visto");
    private JCheckBox chkSinVer = new JCheckBox("Sin ver");
    private JCheckBox chkBajando = new JCheckBox("Bajando");
    private JCheckBox chkEspera = new JCheckBox("Espera");
    private JCheckBox chkTodosNinguno = new JCheckBox("Todos / Ninguno");
    private JCheckBox chkTodosNingunoInfo = new JCheckBox("Todos / Ninguno");

    private JCheckBox chkId = new JCheckBox("Id");
    private JCheckBox chkNombre = new JCheckBox("Nombre");    
    private JCheckBox chkAño = new JCheckBox("Año");
    private JCheckBox chkIMDB = new JCheckBox("IMDB");
    private JCheckBox chkPais = new JCheckBox("País");
    private JCheckBox chkComentarios = new JCheckBox("Comentarios");
    private JCheckBox chkProblemas = new JCheckBox("Problemas");
    private JCheckBox chkDuracion = new JCheckBox("Duración");
    private JCheckBox chkPosters = new JCheckBox("Posters");
    private JCheckBox chkCriticas = new JCheckBox("Críticas");
    private JCheckBox chkDatosTecnicos = new JCheckBox("Datos Técnicos");

    private p.gui.List lstOrdenarPor = new p.gui.List();
    private JScrollPane listScrollerOrd;
    private p.gui.TextArea textArea = new p.gui.TextArea();
    private LabelTextField txtDVDDesde = new LabelTextField("DVD desde");
    private LabelTextField txtDVDHasta = new LabelTextField("DVD hasta");

    private JCheckBox chkSoloHD = new JCheckBox("Sólo HD");

    public ExportarGUI() {
        armarPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnGenerarTXT);

        btnGenerarTXT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarTXT();
            }
        });

        btnGenerarHTML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarHTML();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        chkTodosNinguno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean b = chkTodosNinguno.isSelected();
                chkVisto.setSelected(b);
                chkSinVer.setSelected(b);
                chkBajando.setSelected(b);
                chkEspera.setSelected(b);
            }
        });

        chkTodosNingunoInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean b = chkTodosNingunoInfo.isSelected();
                chkAño.setSelected(b);
                chkNombre.setSelected(b);
                chkId.setSelected(b);
                chkIMDB.setSelected(b);
                chkPais.setSelected(b);
                chkDuracion.setSelected(b);
                chkPosters.setSelected(b);
                chkCriticas.setSelected(b);
                chkComentarios.setSelected(b);
                chkProblemas.setSelected(b);
                chkDatosTecnicos.setSelected(b);
            }
        });
    }

    private void armarPanel() {
        contentPane.setLayout(null);
        armarListaOrdenar();
        setBounds();
        addComponentes();
        pack();
    }

    private void armarListaOrdenar(){
        DefaultListModel listModelOrd = new DefaultListModel();
        listModelOrd.addElement(PeliculasFrame.IT_OR_AÑO);
        listModelOrd.addElement(PeliculasFrame.IT_OR_NOMBRE);
        listModelOrd.addElement(PeliculasFrame.IT_OR_ESTADO);
        listModelOrd.addElement(PeliculasFrame.IT_OR_ID);

        lstOrdenarPor = new p.gui.List(listModelOrd);
        listScrollerOrd = new JScrollPane(lstOrdenarPor);
    }
    
    private void addComponentes(){
        contentPane.add(lblAExportar);
        contentPane.add(lblInfoAExportar);
        contentPane.add(chkVisto);
        contentPane.add(chkAño);
        contentPane.add(chkSinVer);

        contentPane.add(chkNombre);
        contentPane.add(chkIMDB);
        contentPane.add(chkPais);
        contentPane.add(chkComentarios);
        contentPane.add(chkProblemas);
        contentPane.add(chkDuracion);
        contentPane.add(chkPosters);
        contentPane.add(chkCriticas);
        contentPane.add(chkDatosTecnicos);

        contentPane.add(chkBajando);
        contentPane.add(chkId);
        contentPane.add(chkEspera);
        contentPane.add(chkTodosNinguno);
        contentPane.add(chkTodosNingunoInfo);
        contentPane.add(textArea);
        contentPane.add(btnCancel);
        contentPane.add(btnGenerarTXT);
        contentPane.add(btnGenerarHTML);

        contentPane.add(listScrollerOrd);
        contentPane.add(txtDVDDesde);
        contentPane.add(txtDVDHasta);
        contentPane.add(chkSoloHD);
    }

    private void setBounds(){
        int anc = 140;
        int ancBoton = 120;
        int alt = 20;
        int y = 20;
        int sep = 10;

        int x1 = 10;
        int x2 = 200;
        int x3 = 360;

        lblAExportar.setBounds(x1, y, anc, alt);
        lblInfoAExportar.setBounds(x2, y, anc, alt);
        y += alt+sep;
        chkVisto.setBounds(x1, y, anc, alt);
        chkAño.setBounds(x2, y, anc, alt);
        chkComentarios.setBounds(x3, y, anc, alt);
        y += alt+sep;
        chkSinVer.setBounds(x1, y, anc, alt);
        chkNombre.setBounds(x2, y, anc, alt);
        chkDuracion.setBounds(x3, y, anc, alt);
        y += alt+sep;
        chkBajando.setBounds(x1, y, anc, alt);
        chkId.setBounds(x2, y, anc, alt);
        chkPosters.setBounds(x3, y, anc, alt);
        y += alt+sep;
        chkEspera.setBounds(x1, y, anc, alt);
        chkIMDB.setBounds(x2, y, anc, alt);
        chkCriticas.setBounds(x3, y, anc, alt);
        y += alt+sep;
        chkTodosNinguno.setBounds(x1, y, anc, alt);
        chkPais.setBounds(x2, y, anc, alt);
        chkDatosTecnicos.setBounds(x3, y, anc, alt);
        y += alt+sep;
        chkProblemas.setBounds(x3, y, anc, alt);
        chkTodosNingunoInfo.setBounds(x2, y, anc, alt);
        y += alt+sep;


        txtDVDDesde.setBounds(x1, y, 80, 40);
        txtDVDHasta.setBounds(x1 + 100, y, 100, 40);
        chkSoloHD.setBounds(x1 + 220, y+20, 100, 20);

        y +=sep + 60;
        textArea.setBounds(x1, y, 330, 200);
        listScrollerOrd.setBounds(x1 + textArea.getWidth() + 20, y, anc, 170);
        y += textArea.getHeight();

        y +=sep*3;
        btnGenerarTXT.setBounds(x1, y, ancBoton, alt);
        btnGenerarHTML.setBounds(x1+anc+sep, y, ancBoton, alt);
        btnCancel.setBounds(x1+anc*2+sep*2, y, ancBoton, alt);
        Dimension d = new Dimension(x3 + anc + sep * 3,
                btnCancel.getY() + btnCancel.getHeight() + sep * 3);
        setPreferredSize(d);
    }

    private void generarTXT() {
        java.util.List<Pelicula> ps = getPeliculas();
        StringBuffer sb = Exportar.toString(ps, getConfig());
        textArea.setText(sb.toString());
    }

    private void generarHTML() {
        List<Pelicula> ps = getPeliculas();
        StringBuffer sb = HTML.getContenidoHTML(ps, getConfig());
        textArea.setText(sb.toString());
    }

    private List<Pelicula> getPeliculas() {
        Object[] orX = ((DefaultListModel) lstOrdenarPor.getModel()).toArray();
        java.util.List<Pelicula> res = Exportar.getPeliculas(orX, estadosSeleccionados());
        if (chkSoloHD.isSelected())
            return filtrarSoloHD(res);
        else
            return filtrarPorDVD(res);
    }

    private List<Pelicula> filtrarSoloHD(List<Pelicula> ps){
        List<Pelicula> res = new ArrayList<Pelicula>();
        for(Pelicula p : ps) {
            if (p.isHD())
                res.add(p);
        }
        return res;
    }

    private List<Pelicula> filtrarPorDVD(List<Pelicula> ps){
        Integer d = null;
        Integer h = null;
        if (txtDVDDesde.getText().length() >0){
            //noinspection EmptyCatchBlock
            try {
                d = Integer.parseInt(txtDVDDesde.getText());
            }
            catch (Exception ex) {}
        }
        if (txtDVDHasta.getText().length() >0){
            //noinspection EmptyCatchBlock
            try {
                h = Integer.parseInt(txtDVDHasta.getText());
            }
            catch (Exception ex) {}
        }
        if (d == null && h == null)
            return ps;
        else{
            List<Pelicula> res = new ArrayList<Pelicula>();
            for(Pelicula p : ps) {
                if (pasa(p,d,h))
                    res.add(p);
            }
            return res;
        }
    }

    private boolean pasa(Pelicula p , Integer d, Integer h) {
        String id = p.getId();
        if (id == null)
            return false;
        if (!id.startsWith("DVD"))
            return false;

        try {
            //id puede ser de la forma: DVD 2 - DIVX 22
            id = id.substring(4).trim();
            if (id.contains("-"))
                id = id.substring(0, id.indexOf("-")).trim();
            final int numId = Integer.parseInt(id);
            if (d == null)
                d = -1;// no filtro por este campo
            if (h == null)
                h = Integer.MAX_VALUE;// no filtro por este campo
            return d <= numId && numId <= h;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private ExportarConfig getConfig() {
        ExportarConfig conf = new ExportarConfig();
        conf.setMostrarAnnus(chkAño.isSelected());
        conf.setMostrarComentarios(chkComentarios.isSelected());
        conf.setMostrarProblemas(chkProblemas.isSelected());
        conf.setMostrarCritica(chkCriticas.isSelected());
        conf.setMostrarDatosTecnicos(chkDatosTecnicos.isSelected());
        conf.setMostrarPosters(chkPosters.isSelected());
        conf.setMostrarDuracion(chkDuracion.isSelected());
        conf.setMostrarId(chkId.isSelected());
        conf.setMostrarIMDB(chkIMDB.isSelected());
        conf.setMostrarNombre(chkNombre.isSelected());
        conf.setMostrarPais(chkPais.isSelected());
        conf.setSoloHD(chkSoloHD.isSelected());

        return conf;
    }

    private Pelicula.Estado[] estadosSeleccionados() {
        java.util.List al = new ArrayList();
        if (chkBajando.isSelected())
            al.add(Pelicula.Estado.BAJANDO);
        if (chkEspera.isSelected())
            al.add(Pelicula.Estado.ESPERA);
        if (chkSinVer.isSelected())
            al.add(Pelicula.Estado.SIN_VER);
        if (chkVisto.isSelected())
            al.add(Pelicula.Estado.VISTO);

        Pelicula.Estado[] res = new Pelicula.Estado[al.size()];
        int i = 0;
        for (Object re : al) {
            res[i++] = (Pelicula.Estado) re;
        }
        return res;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ExportarGUI dialog = new ExportarGUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

