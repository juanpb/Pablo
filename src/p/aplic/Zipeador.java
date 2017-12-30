package p.aplic;

import p.gui.JTextArea_;
import p.gui.TextField;
import p.util.Constantes;
import p.util.DirFileFilter;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class Zipeador extends JFrame implements Constantes{
    TextField _txtDirEntrada = new TextField();
    TextField _txtDirSalida = new TextField();
    JTextArea_   _txtArchivos = new JTextArea_();
    JCheckBox   _chkIncluirDirs = new JCheckBox("Incluir directorios");
    JButton     _btnHacer = new JButton("Hacer");
    JScrollPane _scrollPane = new JScrollPane(_txtArchivos);


    public static void main(String[] args) {
        new Zipeador();
    }

    public Zipeador() {
        super.getContentPane().setLayout(null);
        setBounds();
        addComponentes();
        setPropiedades();

        addListeners();
        setVisible(true);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addListeners() {
        _txtDirEntrada.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarArchivos();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        _btnHacer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    hacerZips();
                    mostrarMsg("Listo");
                } catch (IOException e1) {
                    mostrarMsg("Pinchose: "+ e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
    }

    private void hacerZips() throws IOException {
        String text = _txtArchivos.getText();
        StringTokenizer st = new StringTokenizer(text, NUEVA_LINEA, false);
        String dir = _txtDirEntrada.getText();
        while (st.hasMoreTokens()){
            String s = st.nextToken();
            File f = new File(dir, s);
//            if (f.isFile()){
                String in = f.getAbsolutePath();
                String out = in + ".zip";
                UtilFile.zipear(in, out);
//            }
        }
    }

    private void actualizarArchivos() throws Exception {
        String dir = _txtDirEntrada.getText();
        File dirF = new File(dir);
        if (!dirF.exists()){
            mostrarMsg("No existe el directorio " + dir);
            return ;
        }
        DirFileFilter filtro = null;

        if (_chkIncluirDirs.isSelected())
            filtro = new DirFileFilter(false, false, false);
        else
            filtro = new DirFileFilter(false, true, false);
        String archs = "";
        File[] files = dirF.listFiles(filtro);
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            archs += f.getName() + NUEVA_LINEA;
        }
        _txtArchivos.setText(archs);
    }

    private void setPropiedades() {

    }

    private void addComponentes() {
        getContentPane().add(_txtDirEntrada);
        getContentPane().add(_chkIncluirDirs);
        getContentPane().add(_scrollPane);
        getContentPane().add(_txtDirSalida);
        getContentPane().add(_btnHacer);
    }

    private void setBounds() {
        int x = 15, y = 15, xTem;
        int altTxt = 20;
        int ancTxt = 280;
        int ancBtn = 100;
        int sep = 20;
        int altArch = 300;

        _txtDirEntrada.setBounds(x, y, ancTxt, altTxt);
        xTem = x;
        x += sep + ancTxt;
        _chkIncluirDirs.setBounds(x, y, 150, altTxt);
        x = xTem;
        y += altTxt + sep;
        _scrollPane.setBounds(x, y, ancTxt, altArch);
        y += altArch + sep;
        _txtDirSalida.setBounds(x, y, ancTxt, altTxt);
        y += sep + altTxt;
        _btnHacer.setBounds(x, y , ancBtn, altTxt);

        setBounds(1,1,550, 500);
    }

    private void mostrarMsg(String x){
        JOptionPane.showMessageDialog(this, x);
    }
}
