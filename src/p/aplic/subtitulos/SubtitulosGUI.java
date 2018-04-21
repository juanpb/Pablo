package p.aplic.subtitulos;

import org.apache.log4j.Logger;
import p.gui.LabelTextField;
import p.util.Constantes;
import p.util.Hora;
import p.util.UtilFile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SubtitulosGUI extends JFrame implements Constantes{
    private static final Logger logger = Logger.getLogger(SubtitulosGUI.class);

    private LabelTextField txtOffsetHH = new LabelTextField("Horas");
    private LabelTextField txtOffsetMM = new LabelTextField("Min");
    private LabelTextField txtOffsetSS = new LabelTextField("Seg");
    private LabelTextField txtOffsetMili = new LabelTextField("Mili");

    private LabelTextField txtDesdeHH = new LabelTextField("Horas");
    private LabelTextField txtDesdeMM = new LabelTextField("Min");
    private LabelTextField txtDesdeSS = new LabelTextField("Seg");
    private LabelTextField txtDesdeMili = new LabelTextField("Mili");
    private JRadioButton rbAdelantar = new JRadioButton("Adelantar (resta)");
    private JRadioButton rbAtrazar = new JRadioButton("Atrazar (suma)");
    private JButton btnCambiar = new JButton("Cambiar");


    private LabelTextField txtDesde = new LabelTextField("Desde (hh:mm:ss:mss)");
    private LabelTextField txtHasta = new LabelTextField("Hasta (hh:mm:ss:mss)");

    private LabelTextField txtArchivo = new LabelTextField("Archivo");
    private LabelTextField txtArchivo2 = new LabelTextField("Archivo");

    private JButton btnEstirar = new JButton("Estirar");
    private JButton btnArreglarContador = new JButton("Arreglar contador");
    private JButton btnTextoEnPP = new JButton("Texto al PP");
    private JButton btnCortarHasta = new JButton("Cortar Hasta");
    private JButton btnEliminarSubsVacios = new JButton("Eliminar Subs.Vacíos");
    private JButton btnArchivo = new JButton("Abrir...");
    private JButton btnArchivo2 = new JButton("Abrir...");
    private JButton btnUnir = new JButton("Unir");

    private JCheckBox chkSobreescribir = new JCheckBox("Sobreescribir", true);
    private JCheckBox chkOffsetDesde = new JCheckBox("Modificar desde", false);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new SubtitulosGUI();
             }
        });
    }

    public SubtitulosGUI() {
        initGUI();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    private void initGUI(){
        setLayout(null);
        setBounds();
        addComponentes();
        setPropiedades();
        setVisible(true);
  }

    private void setPropiedades(){
        final Insets ins = new Insets(0, 0, 0, 0);
        btnArreglarContador.setMargin(ins);
        btnTextoEnPP.setMargin(ins);
        btnCortarHasta.setMargin(ins);
        btnEliminarSubsVacios.setMargin(ins);

        btnArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = abrirArchivo();
                if (s == null)
                    s = "";
                txtArchivo.setText(s);
            }
        });

        btnArchivo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = abrirArchivo();
                if (s == null)
                    s = "";
                txtArchivo2.setText(s);
            }
        });

        btnUnir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unir();
            }
        });


        btnEstirar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                estirar();
            }
        });

        btnCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                offset();
            }
        });

        rbAtrazar.setSelected(true);
        habilitarDesde(false);

        chkOffsetDesde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                habilitarDesde(chkOffsetDesde.isSelected());
            }
        });

        btnArreglarContador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                arreglarContador();
            }
        });
        btnTextoEnPP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textoAlPortapapeles();
            }
        });
        btnCortarHasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cortarHasta();
            }
        });

        btnEliminarSubsVacios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarSubsVacios();
            }
        });
    }

    private void unir() {
        try {
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            List<Subtitulo> subs2 = Util.getSubtitulos(txtArchivo2.getText());
            subs = Util.unir(subs, subs2);
            grabar(subs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }


    private void habilitarDesde(boolean b){
        txtDesdeHH.setEditable(b);
        txtDesdeMM.setEditable(b);
        txtDesdeSS.setEditable(b);
        txtDesdeMili.setEditable(b);
    }

    private void addComponentes(){
        add(txtArchivo);
        add(btnArchivo);
        add(txtArchivo2);
        add(btnArchivo2);
        add(btnUnir);
        add(chkSobreescribir);
        add(txtDesde);
        add(txtHasta);
        add(btnEstirar);

        add(txtOffsetHH);
        add(txtOffsetMili);
        add(txtOffsetMM);
        add(txtOffsetSS);
        add(txtDesdeHH);
        add(txtDesdeMili);
        add(txtDesdeMM);
        add(txtDesdeSS);
        final ButtonGroup bg = new ButtonGroup();
        bg.add(rbAdelantar);
        bg.add(rbAtrazar);
        add(rbAdelantar);
        add(rbAtrazar);
        add(btnCambiar);
        add(chkOffsetDesde);
        add(btnArreglarContador);
        add(btnTextoEnPP);
        add(btnCortarHasta);
        add(btnEliminarSubsVacios);
    }

    private void setBounds()
    {
        int x1 = 10;
        int y = 10;
        int alt = 20;
        int altD = 44;
        int anc1 = 130;
        int ancB = 80;
        int sep = 10;
        int x2 = x1 + anc1 + sep;

        txtArchivo.setBounds(x1, y, 280, altD);
        btnArchivo.setBounds(txtArchivo.getX() + txtArchivo.getWidth() + 10,
                y+23, ancB, alt);
        y += altD + sep * 2;

        chkSobreescribir.setBounds(x1, y, 280, alt);

        y += alt + sep ;
        txtArchivo2.setBounds(x1, y, 280, altD);
        btnArchivo2.setBounds(txtArchivo.getX() + txtArchivo.getWidth() + 10,
                y+23, ancB, alt);
        btnUnir.setBounds(btnArchivo2.getX() + btnArchivo2.getWidth() + 10,
                y+23, ancB, alt);

        y += altD * 2 + sep ;


        txtDesde.setBounds(x1, y, anc1, altD);
        txtHasta.setBounds(x2, y, anc1, altD);
        btnEstirar.setBounds(txtHasta.getX() + txtHasta.getWidth() + 10,
                y+23, ancB, alt);
        y += altD + sep * 2;

        btnArreglarContador.setBounds(x1, y, anc1, alt);
        btnEliminarSubsVacios.setBounds(x2, y, anc1, alt);
        y += alt + sep * 2;
        btnTextoEnPP.setBounds(x1, y, anc1, alt);
        btnCortarHasta.setBounds(x2, y, anc1, alt);
        y += altD + sep * 2;

        int xp1 = 10;
        int sepP = 10;
        int ancP = 35;
        int xp2 = xp1 + sepP + ancP;
        int xp3 = xp2 + sepP + ancP;
        int xp4 = xp3 + sepP + ancP;
        int xp5 = xp4 + sepP + ancP;


        txtOffsetHH.setBounds(xp1, y, ancP, altD);
        txtOffsetMM.setBounds(xp2, y, ancP, altD);
        txtOffsetSS.setBounds(xp3, y, ancP, altD);
        txtOffsetMili.setBounds(xp4, y, ancP, altD);
        rbAtrazar.setBounds(xp5, y, 200, alt);
        rbAdelantar.setBounds(xp5, y+20, 200, alt);
        y += altD + sep;
        chkOffsetDesde.setBounds(xp1, y, anc1, alt);
        btnCambiar.setBounds(xp5, y, anc1, alt);
        y += alt + sep;
        txtDesdeHH.setBounds(xp1, y, ancP, altD);
        txtDesdeMM.setBounds(xp2, y, ancP, altD);
        txtDesdeSS.setBounds(xp3, y, ancP, altD);
        txtDesdeMili.setBounds(xp4, y, ancP, altD);
        y += altD + sep*3 + 100;

        setSize(500,y);
    }

///////////////////////////////////////////////////////////////////////////////////////////


    private void cortarHasta(){
        try {
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            Util.cortarHasta(subs);
            grabar(subs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private void eliminarSubsVacios(){
        try {
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            subs = Util.eliminarSubsVacios(subs);
            grabar(subs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private void textoAlPortapapeles(){
        try {
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            StringBuffer textoAsString = Util.getTextoAsString(subs);
            p.util.Util.pegarEnElPortapapeles((textoAsString.toString()));
            JOptionPane.showMessageDialog(this, "Texto pegado al PP");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }


    private void arreglarContador(){
        try {
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            grabar(subs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private void offset(){
        try {
            int hh = getInt(txtOffsetHH.getText());
            int mm = getInt(txtOffsetMM.getText());
            int ss = getInt(txtOffsetSS.getText());
            int mili = getInt(txtOffsetMili.getText());
            Hora offset = new Hora(hh, mm, ss, mili);
            Hora horaDesde = null;
            if (chkOffsetDesde.isSelected()){
                hh = getInt(txtDesdeHH.getText());
                mm = getInt(txtDesdeMM.getText());
                ss = getInt(txtDesdeSS.getText());
                mili = getInt(txtDesdeMili.getText());
                horaDesde = new Hora(hh, mm, ss, mili);
            }
            List<Subtitulo> subs = Util.getSubtitulos(txtArchivo.getText());
            Util.offset(offset, horaDesde, subs, rbAtrazar.isSelected());
            grabar(subs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

    private int getInt(String text) {
        if (text.trim().equals(""))
            return 0;
        else
            return Integer.parseInt(text);
    }

    private String abrirArchivo(){
      File f = new File("F:\\películas\\subtítulos");
      JFileChooser chooser;
      if (f.exists())
          chooser = new JFileChooser(f.getAbsolutePath());
      else
          chooser = new JFileChooser("C:\\");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Subtítulos", "srt", "sub", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            return file.getAbsolutePath();
        }
        else
            return null;
    }

    private void estirar(){
        try {
            String str = txtDesde.getText();
            Hora horaDesde = new Hora(str);
            str = txtHasta.getText();
            Hora horaHasta = new Hora(str);
            String nombreArchOrig = txtArchivo.getText();
            File fileOriginal = new File(nombreArchOrig);
            java.util.List<Subtitulo> subs = Util.estirar(fileOriginal, horaDesde, horaHasta);
            grabar(subs);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Listo");
    }

    private void grabar(List<Subtitulo> subs) throws IOException {
        File fileSal;
        String nombreArchOrig = txtArchivo.getText();
        File fileOriginal = new File(nombreArchOrig);
        if (chkSobreescribir.isSelected()){
            File backupFile = new File(nombreArchOrig + ".orig");
            int contBack = 1;
            while(backupFile.exists()){
                backupFile = new File(nombreArchOrig + ".orig_" + contBack++);
            }
            boolean seCreo = fileOriginal.renameTo(backupFile);//backup
            if (!seCreo){
                backupFile.createNewFile();
                UtilFile.copiar(fileOriginal, backupFile);
            }
            fileSal = new File(nombreArchOrig);
        }
        else{
            fileSal = new File(nombreArchOrig + ".nuevo");
        }

        Util.guardarFormatoSRT(fileSal, subs);
        String msg = "Se grabó la salida en " + fileSal;
        JOptionPane.showMessageDialog(null, msg);
    }


}

