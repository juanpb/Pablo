package crm.core.wfl.editor.gui.dlg;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * Dialogo para testeo de valores de punto.
 */
public class WflTestTokenValueDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblTokenValue = new JLabel ("Valor");
    private JTextField txtTokenValue = new JTextField ();

    public WflTestTokenValueDlg() {
        super();
        init();
        setProperties();
    }
    
    /**
     * Setea el valor de token a mostrar.
     * @param value Valor de token.
     */
    public void setTokenValue(long value) {
    	this.txtTokenValue.setText(String.valueOf(value));
    }
    
    /**
     * Obtiene el valor de token ingresado.
     * @return Valor de token ingresado.
     */
    public long getTokenValue() {
    	return Long.parseLong(txtTokenValue.getText());
    }

    private void setProperties() {
    }

    private void init(){
        addComponents();
        setBounds();
        super.setPanel(panel);
        super.setTitle("Test de Valor de Punto");
    }

    public boolean wasModified() {
            return false;
    }

    private void addComponents() {
        panel.add(lblTokenValue);
        panel.add(txtTokenValue);
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 135;
        int h = 20;

        lblTokenValue.setBounds(x1, y, w, h);
        y += h;
        txtTokenValue.setBounds(x1, y, w, h);
        y += h;
        Dimension dim = new Dimension(x1 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }

    protected void updateDlg() {
    }

    protected void updateBObj() {
    }

    protected boolean isOK() {
        String msg = null;
     
        if(txtTokenValue.getText().trim().equals(""))
            msg = "Falta ingresar el valor de punto";
        else {
        	try {
        		Long.parseLong(txtTokenValue.getText());
	        }
        	catch(NumberFormatException e) {
        		msg = "Debe ingresar un valor numérico de punto";
        	}
        }

        if (msg != null)
            WflUtil.showErrorMessage(msg, "Testeo de Valor de Punto");
        return msg == null;
    }
}
