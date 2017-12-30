package p.aplic;

import p.util.UtilFile;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * User: JP
 * Date: 03/09/2004
 * Time: 17:33:25
 */
public class CopiarGUI extends JFrame{
    private JLabel _lblOrigen = new JLabel("Origen:");
    private JLabel _lblDetino = new JLabel("Destino:");
    private JTextField _txtOrigen = new JTextField() ;
    private JTextField _txtDestino = new JTextField("F:\\tem") ;

    private JButton _btnCopiar = new JButton("Copiar");
    private JButton _btnElegirArch = new JButton("...");

    public static void main(String[] args) {
        new CopiarGUI();
    }

    public CopiarGUI() {
        init();
    }

    private void init(){
        getContentPane().setLayout(null);
        add();
        setBounds();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        _btnCopiar.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                copiar();
            }
        }
        );
        _btnElegirArch.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                elegirArchivo();
            }
        }
        );
        setVisible(true);
    }

    private void copiar() {

        String origen = _txtOrigen.getText();
        String dest = _txtDestino.getText();
        File fo = new File(origen);
        File fd = new File(dest, fo.getName());

        if (fd.exists()){
            String msg = "Ya existe el archivo destino. ¿Sobreescribir?";
            int res = JOptionPane.showConfirmDialog(
                    this, msg,"",  JOptionPane.YES_NO_OPTION);
            if (res != JOptionPane.YES_OPTION){
                return ;
            }
        }

        
        try {
            if (fo.isFile()){
                setTitle("Copiando archivo...");
                UtilFile.copiar(fo, fd);
            }
            else if (fo.isDirectory()){
                setTitle("Copiando directorio...");                
                UtilFile.copiarDir(fo, fd);
            }

            setTitle("Listo " + fd.getName());
        } catch (Exception e1) {
            e1.printStackTrace();
            setTitle("Error: " + e1.getMessage());
        }
    }

    private void setBounds() {
        int anTxt = 250;
        int anLbl = 60;
        int al = 20;
        _lblOrigen.setBounds(0, 0, anLbl, al);
        _txtOrigen.setBounds(anLbl + 10, 0, anTxt, al);
        _btnElegirArch.setBounds(anLbl + 10 + anTxt + 2, 0, al, al);

        _lblDetino.setBounds(0, al + 10, anLbl, al);
        _txtDestino.setBounds(anLbl + 10, al + 10, anTxt, al);
        _btnCopiar.setBounds(60, al *2 +20, 100, al);
        this.setSize(350, 110);
    }

    private void add() {
        getContentPane().add(_lblOrigen);
        getContentPane().add(_lblDetino);
        getContentPane().add(_txtOrigen);
        getContentPane().add(_txtDestino);
        getContentPane().add(_btnCopiar);
        getContentPane().add(_btnElegirArch);
    }

    private void elegirArchivo() {
        JFileChooser chooser = new JFileChooser("D:\\");
        chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                _txtOrigen.setText(file.getAbsolutePath());
            }
}   }
