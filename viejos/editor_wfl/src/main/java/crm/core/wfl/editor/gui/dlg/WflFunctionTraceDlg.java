package crm.core.wfl.editor.gui.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import crm.core.wfl.editor.gui.cmp.WflFunctionTrace;

/**
 * Editor de seguimientos
 */
public class WflFunctionTraceDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel(new BorderLayout());
    private JTextArea txtTrace = new JTextArea ();
    private JScrollPane sp = new JScrollPane (txtTrace);
    private List<WflFunctionTrace> trace = null;

    /**
     * Constructor.
     * @param trace 
     */
    public WflFunctionTraceDlg(List<WflFunctionTrace> trace) {
        super();
        init();
        this.trace = trace;
        txtTrace.setText(traceToString());
    }

    private void init(){
        Dimension dim = new Dimension(800, 600);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    	panel.add(sp, BorderLayout.CENTER);        
        txtTrace.setWrapStyleWord(true);
        txtTrace.setLineWrap(true);
        super.setPanel(panel);
        super.setTitle("Seguimientos");
        updateDlg();
        super.setIgnoreEnter(true);
        this.setResizable(true);        
    }

    /**
     * Llena los campos del panel
     */
    public void updateDlg() {
    }

    private List<WflFunctionTrace> getTrace() {
        String res = txtTrace.getText();
        List<WflFunctionTrace> traces = new ArrayList<WflFunctionTrace>();
        BufferedReader br = new BufferedReader(new StringReader(res));
        
        String line;
        try {
			while((line=br.readLine())!=null) {
				String separator = ",";
				if(line.indexOf(separator)<0)
					separator = " ";

				String functionId = null;
				String tokenValueSt = null;
				int separatorPosition = line.indexOf(separator);
				if(separatorPosition>=0) {
					functionId = line.substring(0, separatorPosition).trim();
					tokenValueSt = line.substring(separatorPosition+1).trim();
				}
				else
					functionId = line;
				
				long tokenValue = 0;
				try {
					tokenValue = Long.valueOf(tokenValueSt);
				}
				catch(NumberFormatException e) {
					tokenValue = 0;
				}
				if(functionId!=null && functionId.trim().length()>0)
					traces.add(new WflFunctionTrace(functionId, tokenValue));
			}
		} catch (IOException e) {

		}
        
        return traces;
    }

    public boolean isOK() {
        return !wasCancel;
    }
    
    /**
     * Convierte el trace a un string para ser editado.
     * @return String con el formato functionId,token
     */
    private String traceToString() {
    	StringBuffer sb = new StringBuffer();
    	for(WflFunctionTrace t : trace) {
    		sb.append(t.getFunctionId());
    		sb.append(",");
    		sb.append(t.getTokenValue());
    		sb.append("\n");
    	}
    	return sb.toString();
    }

	protected void updateBObj() {
		this.trace.clear();
		this.trace.addAll(getTrace());
	}

	public boolean wasModified() {
		return true;
	}

}
