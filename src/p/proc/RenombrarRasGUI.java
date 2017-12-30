package p.proc;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.Util;
import p.util.UtilFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: JPB
 * Date: Sep 30, 2005
 * Time: 1:37:52 PM
 */
public class RenombrarRasGUI extends JFrame{
    private static final Logger logger = Logger.getLogger(RenombrarRasGUI.class);
    private JCheckBox _chkIncluirSubdirectorios = new JCheckBox("Incluir subdirectorios");
    private JButton _btnHacer = new JButton("Hacer");

    DirFileFilter _filterRas = new DirFileFilter(false, true, ".ras", true);
    private int _cantArchMod = 0;

    public static void main(String[] args) {
        new RenombrarRasGUI();
    }
    public RenombrarRasGUI(){
        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_btnHacer, BorderLayout.CENTER);
        getContentPane().add(_chkIncluirSubdirectorios, BorderLayout.SOUTH);

        _btnHacer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                renombrar();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void renombrar() {
        setTitle("");
        _cantArchMod = 0;
        try {
            String s = Util.copiarDesdeElPortapapeles();
            renombrar(s);
        } catch (Exception e) {
            mostrarMsg(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        setTitle("Modificados: " + _cantArchMod);
    }

    private void mostrarMsg(String m) {
        JOptionPane.showMessageDialog(this, m);
    }

    private void renombrar(String s) throws Exception {
        System.out.println("s = " + s);
        File f = new File (s);
        if (!f.isDirectory()){
            String x = s + " no es un directorio";
            mostrarMsg(x);
            return ;
        }
        renombrarDir(f);
        if (_chkIncluirSubdirectorios.isSelected()){
            java.util.List vector = UtilFile.directorios(f);
            if (vector.size() < 2)
                return ;
            for (int i = 1; i < vector.size(); i++) {
                String o = (String) vector.get(i);
                System.out.println("o = " + o);
                renombrar(o);
            }
        }
    }

    private void renombrarDir(File f) {
        File[] files = f.listFiles(_filterRas);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            File fd = new File(file.getAbsolutePath() + ".jpg");
            file.renameTo(fd);
            _cantArchMod++;
        }
    }
}
