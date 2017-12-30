package p.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: JPB
 * Date: Jun 29, 2005
 * Time: 2:31:53 PM
 */
public class BuscadorDlg  extends JDialog{
    boolean _habilitarEnter = true;

        JLabel    _lblBuscar = new JLabel("Texto a buscar:");
        TextField _txtBuscar = new TextField();
        JButton   _btnBuscar = new JButton("Buscar");
        JButton   _btnCancelar = new JButton("Cancelar");

        JCheckBox _chkCaseSensitive = new JCheckBox("Coincidir mayúsculas y minúsculas");
//    JLabel    _lblAlcance = new JLabel("Alcance");
//    CrmRadioButton  _rbtAlcGlobal = new CrmRadioButton("Global");
//    CrmRadioButton  _rbtAlcSeleccionado = new CrmRadioButton("Seleccionado");
//    ButtonGroup _grpAlcance = new ButtonGroup();
//
//    JLabel    _lblDireccion = new JLabel("Direccion");
//    CrmRadioButton  _rbtDirUp = new CrmRadioButton("Atrás");
//    CrmRadioButton  _rbtDirDown = new CrmRadioButton("Adelante");
//    ButtonGroup _grpDireccion = new ButtonGroup();

        BuscadorTextoItf _buscador;

        public BuscadorDlg(Frame owner, BuscadorTextoItf f) throws HeadlessException {
            super(owner);
            init(f);
        }

        public BuscadorDlg(BuscadorTextoItf f) {
            super();
            init(f);
        }

        public BuscadorDlg(Frame owner, BuscadorTextoItf f, boolean modal) {
            super(owner, modal);
            init(f);
        }

        public BuscadorDlg(Frame owner, BuscadorTextoItf f, String title) {
            super(owner, title);
            init(f);
        }

        public BuscadorDlg(Frame owner, BuscadorTextoItf f,
                              String title, boolean modal) {
            super(owner, title, modal);
            init(f);
        }

        public BuscadorDlg(Dialog owner, BuscadorTextoItf f) {
            super(owner);
            init(f);
        }

        public BuscadorDlg(Dialog owner, BuscadorTextoItf f, boolean modal) {
            super(owner, modal);
            init(f);
        }

        public BuscadorDlg(Dialog owner, BuscadorTextoItf f, String title) {
            super(owner, title);
            init(f);
        }

        public BuscadorDlg(Dialog owner, BuscadorTextoItf f,
                              String title, boolean modal) {
            super(owner, title, modal);
            init(f);
        }

        private void init(BuscadorTextoItf f){
            _buscador = f;
            setTitle("Buscador de texto");
            getContentPane().setLayout(null);
            setSizes();
            addComponentes();
            addListeners();
        }

        public void habilitarEnter(boolean b){
            _habilitarEnter = b;
        }

        private void addListeners() {
//        getRootPane().setDefaultButton(_btnBuscar);
            _txtBuscar.addKeyListener(new KeyAdapter(){
                 public void keyTyped(KeyEvent e) {
                     if (_habilitarEnter && e.getKeyChar() == KeyEvent.VK_ENTER){
                         buscar();
                     }
                 }
            });
            _btnBuscar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    buscar();
                }
            });

            _btnCancelar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    cancelar();
                }
            });
        }

        private void buscar() {
            String txt = _txtBuscar.getText();
            if (txt.equals(""))
                return ;
            boolean caseSen = _chkCaseSensitive.isSelected();
            setVisible(false);
            _buscador.buscar(txt, true, caseSen, false, false);
        }

        private void cancelar() {
            this.setVisible(false);
        }

        private void addComponentes() {
            getContentPane().add(_lblBuscar);
            getContentPane().add(_txtBuscar);
            getContentPane().add(_btnBuscar);
            getContentPane().add(_btnCancelar);
            getContentPane().add(_chkCaseSensitive);
        }

        private void setSizes() {
            int x = 10, y = 10;
            int alt = 20;

            _lblBuscar.setBounds(x, y, 100, alt);
            x += _lblBuscar.getWidth() + 5;
            _txtBuscar.setBounds(x, y, 180, alt);

            _chkCaseSensitive.setBounds(10, 40, 240, alt);
            y = 70;
            _btnBuscar.setBounds(100, y, 90, alt);
            x = _btnBuscar.getX() + _btnBuscar.getWidth() + 5;
            _btnCancelar.setBounds(x, y, 90, alt);

            int w = _txtBuscar.getX() + _txtBuscar.getWidth() + 20;
//        int h = _btnBuscar.getY() + _btnBuscar.getHeight() + 20;
            setSize(w, 130);
        }

}
