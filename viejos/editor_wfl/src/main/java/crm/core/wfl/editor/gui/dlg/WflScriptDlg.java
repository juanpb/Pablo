package crm.core.wfl.editor.gui.dlg;

import javax.swing.*;

import crm.core.wfl.editor.function.WflScriptCompilerItf;
import crm.core.wfl.editor.function.WflScriptEditor;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class WflScriptDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(new BorderLayout());

    private WflScriptEditor txtScript; 
    private JScrollPane sp = null;

    private String script = null;
    
    private ImageIcon iconCompile = WflUtil.createImageIcon(WflConstants.ICON_COMPILE);    
    private JButton btnCompile = new JButton("Compilar", iconCompile);
    
    private WflScriptCompilerItf compiler;

    public WflScriptDlg(Dialog owner, 
    					String script, 
    					WflScriptCompilerItf compiler,
    					WflScriptEditor editor) {
        super(owner);
        this.script = script;
        this.compiler = compiler;
        this.txtScript = editor;
        init();
        if (script != null && txtScript != null) {
            txtScript.setText(script);
            txtScript.setCaretPosition(0);
        }
        txtScript.requestFocus();
    }

    private void init(){
        sp = new JScrollPane (txtScript);
        doButtonBar();
    	addListeners();
        setBounds();
        addComponents();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Script");
        updateDlg();
        super.setIgnoreEnter(true);
        this.setResizable(true);
    }

    private void setProperties() {

    }

    private void addComponents() {
    	panel.add(sp, BorderLayout.CENTER);
    }

    private void setBounds() {
        Dimension dim = new Dimension(800, 600);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }

    /**
     * Llena los campos del panel
     */
    public void updateDlg() {
    }

    public boolean wasModified(){
        return !(((getScript() == null && script == null)
                || (getScript() != null && getScript().equals(script))));
    }

    public String getScript() {
        String res = txtScript.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }

    protected void updateBObj() {

    }

    public boolean isOK() {
        return !wasCancel;
    }

    private void doButtonBar() {
        int w = 110;
        int h = 20;
        Dimension dim = new Dimension(w, h);
        btnCompile.setPreferredSize(dim);

        botonera.add(btnCompile, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0));
    }  
    
    private void addListeners() {
    	btnCompile.addActionListener(this);
    }    
    
    public void actionPerformed(ActionEvent e) {
    	super.actionPerformed(e);
        if (e.getSource() == btnCompile) {
        	if(compiler!=null) {
    	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	    	if(!compiler.compile(txtScript.getText(), new PrintWriter(baos))) {
    	            WflUtil.showErrorMessage(baos.toString(), "Compilador de Script");        		
    	    	}
        	}
        }
    }    
}
