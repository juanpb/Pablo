package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflPosFunctionDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblMaskType = new JLabel ("Tipo de máscara");
    private JRadioButton rdbMTAnd = new JRadioButton ("AND BINARIO");
    private JRadioButton rdbMTOr = new JRadioButton ("OR BINARIO");
    private JRadioButton rdbMTNone = new JRadioButton ("Ninguna");
    private ButtonGroup bgMT = new ButtonGroup();

    private JLabel lblMaskValue = new JLabel ("Valor");
    private JTextField txtMaskValue = new JTextField ();

    private String maskType = null;
    private String maskValue = null;

    public WflPosFunctionDlg() {
        super();
        init();
        setProperties();
    }


    private void setProperties() {
                //El campo MaskValue no está visible si rdbMTNone está seleccionado
        rdbMTAnd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });
        rdbMTOr.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });
        rdbMTNone.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });

        //tooltip
        rdbMTAnd.setToolTipText("Comparación a nivel de bits (AND)");
        rdbMTOr.setToolTipText("Comparación a nivel de bits (OR)");
    }

    private void setMaskValueVisible(boolean b) {
         lblMaskValue.setVisible(b);
        txtMaskValue.setVisible(b);
    }

    private void init(){
        addComponents();
        setBounds();
        super.setPanel(panel);
        super.setTitle("PosFunción");
    }

    public boolean wasModified() {
        if (((getMaskType() == null && maskType == null)
                || getMaskType() != null && getMaskType().equals(maskType))
                && txtMaskValue.getText().equals(maskValue))
            return false;
        else
            return true;
    }

    private void addComponents() {
        panel.add(lblMaskType);
        panel.add(rdbMTAnd);
        panel.add(rdbMTOr);
        panel.add(rdbMTNone);

        bgMT.add(rdbMTAnd);
        bgMT.add(rdbMTOr);
        bgMT.add(rdbMTNone);

        panel.add(lblMaskValue);
        panel.add(txtMaskValue);
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 135;
        int x2 = x1 + w + sep;
        int h = 20;

        lblMaskType.setBounds(x1, y, w, h);
        lblMaskValue.setBounds(x2, y, w, h);
        y += h;
        rdbMTNone.setBounds(x1, y, w, h);
        txtMaskValue.setBounds(x2, y, w, h);
        y += h;
        rdbMTAnd.setBounds(x1, y, w, h);
        y += h;
        rdbMTOr.setBounds(x1, y, w, h);
        y += h + sep;

        Dimension dim = new Dimension(x2 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }

    protected void updateDlg() {
        WflPosFunctionBObj obj = (WflPosFunctionBObj)getBObj();
        maskType = obj.getMaskType();
        maskValue = obj.getMaskValue();

        setMaskType(maskType);
        txtMaskValue.setText(maskValue);

        setMaskValueVisible(maskType != null);
    }

    private void setMaskType(String maskType) {
        if (WflPosFunctionBObj.MASK_TYPE_AND.equals(maskType))
            rdbMTAnd.setSelected(true);
        else if (WflPosFunctionBObj.MASK_TYPE_OR.equals(maskType) )
            rdbMTOr.setSelected(true);
        else{
            rdbMTNone.setSelected(true);
        }
    }

    protected void updateBObj() {
        WflPosFunctionBObj obj = (WflPosFunctionBObj)getBObj();
        String mt = getMaskType();
        obj.setMaskType(mt);
        if (mt == null )
            obj.setMaskValue(null);
        else
            obj.setMaskValue(txtMaskValue.getText());
    }

    private String getMaskType() {
        if (rdbMTAnd.isSelected())
            return WflPosFunctionBObj.MASK_TYPE_AND;
        else if (rdbMTOr.isSelected())
            return WflPosFunctionBObj.MASK_TYPE_OR;
        else
            return null;
    }


    protected boolean isOK() {
        String msg = null;
        if (getMaskType() != null && txtMaskValue.getText().trim().equals(""))
            msg = "Falta ingresar el valor de la máscara";

        if (msg != null)
            WflUtil.showErrorMessage(msg, "Posfunción");
        return msg == null;
    }

}
