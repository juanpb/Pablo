package p.aplic.inicio;

import javax.swing.*;
import java.awt.*;

/**
 * User: JP
 * Date: 13/05/2005
 * Time: 11:08:46
 */
public class ProgramaPanel extends JPanel{

    long _demora = -1;
    String _exe = null;
    int _posicion = -1;


    JButton _btnSubir = new JButton("/\\");
    JButton _btnBajar = new JButton("\\/");
    JCheckBox _chkEjecutar = new JCheckBox("Ejecutar");
    JButton _btnEjecutar = new JButton();
    JLabel _lblDemora = new JLabel("Demora");
    JTextField _txtDemora = new JTextField();

    public ProgramaPanel() {
        this(-1,null, -1);
    }

    public ProgramaPanel(long demoraEnMS, String exe, int posicion) {
        _demora = demoraEnMS;
        _exe = exe;
        _posicion = posicion;
        init();
    }

    private void init() {
        setPropiedades();
        setSizes();
        addComponentes();
    }

    private void addComponentes() {
        add(_btnEjecutar);
        add(_btnSubir);
        add(_btnBajar);
        add(_lblDemora);
        add(_txtDemora);
        add(_chkEjecutar);
    }

    private void setSizes() {
        int x = 20, y = 10;
        int alt = 20;

        _btnEjecutar.setBounds(x, y, 200, alt * 2);
        _lblDemora.setBounds(x, 60, 100, alt);
        _txtDemora.setBounds(x, 80, 50, alt);
        _chkEjecutar.setBounds(90, 80, 100, alt);

        x = _btnEjecutar.getX() + _btnEjecutar.getWidth() + 20;
        _btnSubir.setBounds(x, 20, 30, alt);
        _btnBajar.setBounds(x, 60, 30, alt);

        Dimension dim = new Dimension(_btnBajar.getX() + _btnBajar.getWidth(), 100);
        setSize(dim);
    }

    private void setPropiedades() {
        setLayout(null);
        Insets ins = new Insets(0,0,0,0);
        _btnBajar.setMargin(ins);
        _btnSubir.setMargin(ins);
    }


    public void setDemora(long p) {
        _demora = p;
    }
    public long getDemora() {
        return _demora;
    }

    public void setExe(String p) {
        _exe = p;
    }
    public String getExe() {
        return _exe;
    }

    public void setPosicion(int p) {
        _posicion = p;
    }
    public int getPosicion() {
        return _posicion;
    }
}
