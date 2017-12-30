package p.aplic.libretadirecciones.gui.abm;

import p.aplic.libretadirecciones.bobj.Direccion;
import p.gui.JTextArea_;

import javax.swing.*;

/**
 * User: JPB
 * Date: Dec 3, 2007
 * Time: 10:06:05 AM
 */
class TabNotas extends JPanel {
    private JTextArea_ _txt = new JTextArea_();
    private JScrollPane _sp = new JScrollPane(_txt);

    public TabNotas(){
        init();
    }

    public String getNotas(){
        return _txt.getText();
    }
    private void init() {
        setLayout(null);
        _sp.setBounds(10, 10, 480, 355);
        add(_sp);
        setSize(450,450);
    }

    public void setDireccion(Direccion dir) {
        _txt.setText(dir.getNotas());
    }

}
