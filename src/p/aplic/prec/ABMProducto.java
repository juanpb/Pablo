package p.aplic.prec;

import p.aplic.prec.domain.ProductoDO;
import p.gui.LabelTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * User: JPB
 * Date: May 4, 2005
 * Time: 6:58:51 PM
 */
public class ABMProducto extends JDialog {


    private LabelTextField txtId = new LabelTextField("Id");
    private LabelTextField txtDesc = new LabelTextField("Descripción");
    private Checkbox chkImprimir = new Checkbox("Imprimir");

    private JButton btnAceptar = new JButton("Aceptar");
    private JButton btnCancelar = new JButton("Cancelar");
    private ProductoDO prod;


    public ABMProducto(ProductoDO prd) {
        super();
        init();
        if (prd != null){
            setProducto(prd);
        }
    }


    public void setProducto(ProductoDO prd){
        prod = prd;
        if (prd != null){
            txtId.setText( prd.getId()+"");
            txtDesc.setText(prd.getDescripcion());
            chkImprimir.setState(prd.isImprimir());
        }
    }

    /**
     * Devuelve una nueva dirección
     * @return
     */
    public ProductoDO getProducto(){
        return prod;
    }

    private void init() {
        getContentPane().setLayout(null);
        addComponentes();
        setSizes();
        addListeners();

        txtId.setEditable(false);
    }

    private void addCancelByEscapeKey(){
        String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
//        String ACCEPT_ACTION_KEY = "ACCEPT_ACTION_KEY";
        int noModifiers = 0;
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, noModifiers, false);
//        KeyStroke accpetKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, noModifiers, false);
        InputMap inputMap =
          getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        ;
        inputMap.put(escapeKey, CANCEL_ACTION_KEY);
//        inputMap.put(accpetKey, ACCEPT_ACTION_KEY);
        
        AbstractAction cancelAction = new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                cancelar();
            }
        };
//        AbstractAction acceptAction = new AbstractAction(){
//            public void actionPerformed(ActionEvent e){
//                System.out.println("en ABMProd");
//                aceptar();
//            }
//        };
        getRootPane().getActionMap().put(CANCEL_ACTION_KEY, cancelAction);
//        getRootPane().getActionMap().put(ACCEPT_ACTION_KEY, acceptAction);
      }


    private void addListeners() {
        addCancelByEscapeKey();
        getRootPane().setDefaultButton(btnAceptar);

        addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent e) {
                prod = null;
             }
        });

        btnAceptar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });

        btnCancelar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

    }

    private void aceptar() {
        prod.setDescripcion(txtDesc.getText());
        prod.setImprimir(chkImprimir.getState());
        cerrar();
    }

    private void cancelar() {
        prod = null;
        cerrar();
    }

    private void cerrar() {
        dispose();
    }

    private void addComponentes() {
        getContentPane().add(btnAceptar);
        getContentPane().add(btnCancelar);
        getContentPane().add(txtId);
        getContentPane().add(txtDesc);
        getContentPane().add(chkImprimir);
    }

    private void setSizes() {
        setSize(510, 350);
        txtDesc.setBounds(10, 10, 320, 40);
        txtId.setBounds(10, 60, 120, 40);
        chkImprimir.setBounds(10, 110, 120, 20);

        btnAceptar.setBounds(10, 180, 120, 20);
        btnCancelar.setBounds(200, 180, 120, 20);
    }
}
