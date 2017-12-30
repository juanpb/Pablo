package crm.core.wfl.editor.gui.dlg;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import crm.core.wfl.editor.bobj.WflFunctionBObj;
import crm.core.wfl.editor.function.WflScriptCompilerItf;
import crm.core.wfl.editor.function.WflScriptEditor;
import crm.core.wfl.editor.function.WflServiceTypeDefinition;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflFunctionDlg extends WflDlg{

	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblId = new JLabel ("Id");
    private JTextField txtId = new JTextField ();

    private JLabel lblName = new JLabel ("Nombre");
    private JTextField txtName = new JTextField ();

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JLabel lblExecType = new JLabel ("Tipo");
    private JRadioButton rdbExecTypeSS = new JRadioButton ("Server sincrónica");
    private JRadioButton rdbExecTypeSA = new JRadioButton ("Server asincrónica");
    private JRadioButton rdbExecTypeCI = new JRadioButton ("Cliente interactiva");
    private JRadioButton rdbExecTypePE = new JRadioButton ("Proceso especial");
    private ButtonGroup bgExecType = new ButtonGroup();

    private JLabel lblExecTypeRB = new JLabel ("Tipo");
    private JRadioButton rdbExecTypeRBSS = new JRadioButton ("Server sincrónica");
    private JRadioButton rdbExecTypeRBSA = new JRadioButton ("Server asincrónica");
    private JRadioButton rdbExecTypeRBCI = new JRadioButton ("Cliente interactiva");
    private JRadioButton rdbExecTypeRBPE = new JRadioButton ("Proceso especial");
    private ButtonGroup bgExecTypeRB = new ButtonGroup();

    private JLabel lblExecutionName = new JLabel ("Nombre de la ejecución");
    private JTextField txtExecutionName = new JTextField ("Nombre de la ejecución");

    private JLabel lblExecutionNameRB = new JLabel ("Nombre de la ejecución");
    private JTextField txtExecutionNameRB = new JTextField ("Nombre de la ejecución");
    
    private JLabel lblRole = new JLabel ("Rol");
    private JTextField txtRole = new JTextField ("Rol");
    private JLabel lblRoleParametricObjectId = new JLabel ("Id Obj Parametrico");
    private JTextField txtRoleParametricObjectId = new JTextField ("Id Obj Parametrico");

    private JLabel lblRoleRB = new JLabel ("Rol");
    private JTextField txtRoleRB = new JTextField ("Rol");
    private JLabel lblRoleParametricObjectIdRB = new JLabel ("Id Obj Parametrico");
    private JTextField txtRoleParametricObjectIdRB = new JTextField ("Id Obj Parametrico");
    
    private JLabel lblExecution = new JLabel ("Ejecución");
    private JRadioButton rdbExeSinc = new JRadioButton ("Sincrónica");
    private JRadioButton rdbExeAsinc = new JRadioButton ("Asincrónica");
    private ButtonGroup bgExe = new ButtonGroup();

    private JLabel lblExecutionRB = new JLabel ("Ejecución");
    private JRadioButton rdbExeRBSinc = new JRadioButton ("Sincrónica");
    private JRadioButton rdbExeRBAsinc = new JRadioButton ("Asincrónica");
    private ButtonGroup bgExeRB = new ButtonGroup();

    private JLabel lblServiceType = new JLabel("Tipo Servicio");
    private JComboBox cmbServiceType = new JComboBox ();

    private JLabel lblServiceTypeRB = new JLabel("Tipo Servicio");
    private JComboBox cmbServiceTypeRB = new JComboBox ();

    private JLabel lblClassName = new JLabel ("Clase");
    private JTextField txtClassName = new JTextField ();
    Icon ic = WflUtil.createImageIcon(WflConstants.ICON_EXPLORER);
    private JButton btnClassName = new JButton (ic);
    private String txtScript = "";
    private JButton btnScriptEditor = new JButton (ic);    
    
    private JLabel lblClassNameRB = new JLabel ("Clase");
    private JTextField txtClassNameRB = new JTextField ();
    private JButton btnClassNameRB = new JButton (ic);
    private String txtScriptRB = "";

    private JButton btnScriptEditorRB = new JButton (ic);    
    
    private JLabel lblPriority = new JLabel ("Prioridad");
    private JComboBox cmbPriority = new JComboBox ();

    private JCheckBox chkCarryContext = new JCheckBox ("Lleva contexto");

    private JLabel lblType = new JLabel ("Tipo");
    private JRadioButton rdbTypeNormal = new JRadioButton ("Normal");
    private JRadioButton rdbTypeIterative = new JRadioButton ("Iterativa");
    private ButtonGroup bgType = new ButtonGroup();

    private JLabel lblSecurityObject = new JLabel ("Objeto de seguridad");
    private JTextField txtSecurityObject = new JTextField ();

    private JLabel lblComment = new JLabel ("Comentario");
    private JTextArea txtComment = new JTextArea ();
    private JScrollPane spComment = new JScrollPane (txtComment);
    private Icon ic2 = WflUtil.createImageIcon(WflConstants.ICON_EXPAND);
    private JButton btnComment = new JButton(ic2);

    private String id = null;
    private String name = null;
    private String executionType = null;
    private String executionSynchro = null;
    private String functionClass = null;
    private String executionName = null;
    private String serviceType = null;
    private String script = null;
    private String role = null;
    private String roleParametricObjectId = null;
    private String executionTypeRB = null;
    private String executionSynchroRB = null;
    private String functionClassRB = null;
    private String executionNameRB = null;
    private String serviceTypeRB = null;
    private String scriptRB = null;
    private String roleRB = null;
    private String roleParametricObjectIdRB = null;
    private int priority;
    private boolean carryContext = false;
    private String type = null;
    private String securityObject = null;
    private String comment = null;

    private JPanel forwardPanel;
    private JPanel rollbackPanel;


    public WflFunctionDlg() {
        super();
        init();
    }

    private void init(){
        addComponents();
        setBounds();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Función");

        for (int i = 1; i < 10; i++) {
            cmbPriority.addItem(new Integer(i));
        }

        // Al de rollback se le agrega la el tipo de funcion nula.
        cmbServiceTypeRB.addItem("");

        for (int i = 0; i<WflServiceTypeDefinition.SERVICE_TYPES.length; i++) {
        	cmbServiceType.addItem(WflServiceTypeDefinition.SERVICE_TYPES[i].getDescription());
        	cmbServiceTypeRB.addItem(WflServiceTypeDefinition.SERVICE_TYPES[i].getDescription());
        }

        cmbServiceType.setSelectedIndex(0);
        cmbServiceTypeRB.setSelectedIndex(0);

    }
    
    

    public boolean wasModified() {
        String es = getExecutionSynchro();
        String esRB = getExecutionSynchroRB();
        String etRB = getExecutionTypeRB();
        String enRB = getExecutionNameRB();
        String cnRB = getClassNameRB();
        String so = getSecurityObject();
        String c = getComment();
        String t = getType2();
        boolean b1 = txtId.getText().equals(id);
        boolean b2 = txtName.getText().equals(name);
        boolean b3 = ((es == null && executionSynchro == null)
                || (es != null && es.equals(executionSynchro)));
        boolean b4 = ((esRB == null && executionSynchroRB == null)
                || (esRB != null && esRB.equals(executionSynchroRB)));
        boolean b5 = ((etRB == null && executionTypeRB == null)
                || (etRB != null && etRB.equals(executionTypeRB)));
        boolean b6 = getExecutionType().equals(executionType);
        boolean b7 = txtClassName.getText().equals(functionClass);
        boolean b8 = (cnRB == null && functionClassRB == null)
                || (cnRB != null && cnRB.equals(functionClassRB));
        boolean b9 = getPriority() == priority;
        boolean b10 = ((enRB == null && executionNameRB == null)
                || (enRB != null && enRB.equals(securityObject)));
        boolean b11 = ((so == null && securityObject == null)
                || (so != null && so.equals(securityObject)));
        boolean b12 = ((c == null && comment == null)
                || (c != null && c.equals(comment)));
        boolean b13 = ((t == null && type == null)
                || (getType2() != null && getType2().equals(type)));
        boolean b14 = (carryContext == chkCarryContext.isSelected());
        boolean b15 = (txtExecutionName.getText().equals(executionName));
        boolean b16 = (cmbServiceType.getSelectedItem().equals(this.serviceType));
        boolean b17 = (cmbServiceTypeRB.getSelectedItem().equals(this.serviceTypeRB));
        boolean b18 = (txtScript.equals(this.script));
        boolean b19 = (txtScriptRB.equals(this.scriptRB));
        return !(b1
                && b2
                && b3
                && b4
                && b5
                && b6
                && b7
                && b8
                && b9
                && b10
                && b11
                && b12
                && b13
                && b14
                && b15 
                && b16
                && b17
                && b18
                && b19);
    }

    private String getClassNameRB() {
        String r = txtClassNameRB.getText();
        if (r.trim().equals(""))
            r = null;
        return r;
    }

    private String getExecutionNameRB() {
        if (getFunctionClassRB() == null)
            return null;

        String res = txtExecutionNameRB.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }
    private String getSecurityObject() {
        String res = txtSecurityObject.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }

    private String getType2() {
        if (rdbTypeIterative.isSelected())
            return WflFunctionBObj.TYPE_ITERATIVE;
        else
            return WflFunctionBObj.TYPE_NORMAL;
    }

    private int getPriority() {
        Integer i = (Integer)cmbPriority.getSelectedItem();
        return i.intValue();
    }

    protected void updateDlg() {
        WflFunctionBObj bobj = (WflFunctionBObj)getBObj();
        id = bobj.getId();
        name = bobj.getName();
        executionType = bobj.getExecutionType();
        executionSynchro = bobj.getExecutionSynchro();
        functionClass = bobj.getFunctionClass();
        serviceType = bobj.getServiceType();
        script = bobj.getScript();
        executionTypeRB = bobj.getExecutionTypeRB();
        executionSynchroRB = bobj.getExecutionSynchroRB();
        functionClassRB = bobj.getFunctionClassRB();
        serviceTypeRB = bobj.getServiceTypeRB();
        scriptRB = bobj.getScriptRB();
        executionNameRB = bobj.getExecutionNameRB();
        type = bobj.getType();
        carryContext = bobj.isCarryContext();
        executionName = bobj.getExecutionName();
        priority = bobj.getPriority();
        securityObject = bobj.getSecurityObject();
        comment = bobj.getComment();
        role = bobj.getRole();
        roleParametricObjectId = bobj.getRoleParametricObjectId();
        roleRB = bobj.getRoleRB();
        roleParametricObjectIdRB = bobj.getRoleParametricObjectIdRB();
        
        txtId.setText(id);
        txtName.setText(name);
        setExecutionType(executionType);
        setExecutionSynchro(executionSynchro);
        setServiceType(serviceType);
        setScript(script);

        setExecutionTypeRB(executionTypeRB);
        setExecutionSynchroRB(executionSynchroRB);
        setServiceTypeRB(serviceTypeRB);
        setScriptRB(scriptRB);
        txtExecutionNameRB.setText(executionNameRB);

        setType(type);
        setCarryContext(carryContext);
        txtExecutionName.setText(executionName);
        txtSecurityObject.setText(securityObject);
        txtComment.setText(comment);
        setPriority(priority);
        
        txtRole.setText(role);
        txtRoleParametricObjectId.setText(roleParametricObjectId);
        txtRoleRB.setText(roleRB);
        txtRoleParametricObjectIdRB.setText(roleParametricObjectIdRB);
        
        setupServiceTypeEditor(functionClass);
        setupServiceTypeRBEditor(functionClassRB);
    }

    private void setPriority(int priority) {
        if (priority < 1 || priority >9 )
            priority = 5 ;
        cmbPriority.setSelectedItem(new Integer(priority));
    }

    private void setCarryContext(boolean carryContext) {
        chkCarryContext.setSelected(carryContext);
    }

    private void setType(String type) {
        if (WflFunctionBObj.TYPE_NORMAL.equals(type))            
            rdbTypeNormal.setSelected(true);
        else
            rdbTypeIterative.setSelected(true);
    }

    private void setExecutionSynchroRB(String t) {
        if (t == null)
            rdbExeRBSinc.setSelected(true); //valor default
        else if (WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC.equals(t))
            rdbExeRBSinc.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_SYNCHRO_ASYNCHRONOUS.equals(t) )
            rdbExeRBAsinc.setSelected(true);
        else
            System.out.println("executionSyncro desconocido = " + t);
    }

    private void setExecutionSynchro(String t) {
        if (t == null)
            rdbExeSinc.setSelected(true); //valor default
        else if (WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC.equals(t))
            rdbExeSinc.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_SYNCHRO_ASYNCHRONOUS.equals(t) )
            rdbExeAsinc.setSelected(true);
        else
            System.out.println("executionSyncro desconocido = " + t);
    }
    
    private void setServiceType(String serviceTypeCode) {
    	for(int i=0; i<WflServiceTypeDefinition.SERVICE_TYPES.length; i++) {
    		if(WflServiceTypeDefinition.SERVICE_TYPES[i].getId().equals(serviceTypeCode))
    			this.cmbServiceType.setSelectedIndex(i);
    	}
    }

    private void setServiceTypeRB(String serviceTypeCode) {
    	for(int i=0; i<WflServiceTypeDefinition.SERVICE_TYPES.length; i++) {
    		if(WflServiceTypeDefinition.SERVICE_TYPES[i].getId().equals(serviceTypeCode)) {
    			this.cmbServiceTypeRB.setSelectedIndex(i+1);
    			return;
    		}
    	}
    	this.cmbServiceTypeRB.setSelectedIndex(0);
    }
    
    private void setScript(String script) {
    	if(script!=null)
    		this.txtScript = script;
    	else
    		this.txtScript = "";
    }

    private void setScriptRB(String script) {
    	if(script!=null)
    		this.txtScriptRB = script;
    	else
    		this.txtScriptRB = "";
    }
    
    private String getScript() {
    	return this.txtScript.trim().length() == 0 ? null : this.txtScript;
    }

    private String getScriptRB() {
    	return this.txtScriptRB.trim().length() == 0 ? null : this.txtScriptRB;
    }
    
    private void setExecutionTypeRB(String type) {
        if (type == null )
            rdbExecTypeRBSS.setSelected(true); //valor default
        else if (WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(type))
            rdbExecTypeRBCI.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS.equals(type) )
            rdbExecTypeRBPE.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(type) )
            rdbExecTypeRBSA.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(type) )
            rdbExecTypeRBSS.setSelected(true);
        else{
            System.out.println("executionType desconocido = " + type);
        }
    }
    private void setExecutionType(String type) {
        if (type == null )
            rdbExecTypeSS.setSelected(true); //valor default
        else if (WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(type))
            rdbExecTypeCI.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS.equals(type))
            rdbExecTypePE.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(type) )
            rdbExecTypeSA.setSelected(true);
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(type) )
            rdbExecTypeSS.setSelected(true);
        else{
            System.out.println("executionType desconocido = " + type);
        }
    }

    private void setProperties() {
        btnClassName.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFinderClass();
            }
        });
        btnClassNameRB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFinderClassRB();
            }
        });
        btnComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComment();
            }
        });
        
        cmbServiceType.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			setupServiceTypeEditor(null);
        		}
           });

        cmbServiceTypeRB.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			setupServiceTypeRBEditor(null);
    		}
       });
    
        btnScriptEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showScriptEditor();
            }
        });

        btnScriptEditorRB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showScriptEditorRB();
            }
        });
    
    }
    
    private void showScriptEditor() {
        String c = getScript();
        WflServiceTypeDefinition def = WflServiceTypeDefinition.SERVICE_TYPES[cmbServiceType.getSelectedIndex()];
        WflScriptDlg dlg = new WflScriptDlg(this, c, getCompiler(def), getEditor(def));
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified()) {
        	// Tengo que compilar el script
        	checkCompileErrors(dlg.getScript(), def);
            setScript(dlg.getScript());
        }
    }
    
    /**
     * Obtiene el compilador de script.
     * @param def Definicion a tomar.
     * @return Compilador de script asociado. Null si hay un error obteniendo el compiler.
     */
    private WflScriptCompilerItf getCompiler(WflServiceTypeDefinition def) {
    	try {
    		if(def.getScriptCompiler()!=null && def.getScriptCompiler().trim().length()>0) {
	        	Class compilerClass = Class.forName(def.getScriptCompiler());
	        	return (WflScriptCompilerItf)compilerClass.newInstance();
    		}
    		else {
    			WflUtil.showErrorMessage("El tipo de servicio no tiene asociado compilador de script.", "Compilador de Script");
    		}
		} catch (Exception e) {
            WflUtil.showErrorMessage(e.getClass().getName() + ": " + e.getMessage(), "Compilador de Script");
		}
        return null;
    }

    /**
     * Obtiene el editor de script.
     * @param def Definicion a tomar.
     * @return Compilador de script asociado. Null si hay un error obteniendo el compiler.
     */
    private WflScriptEditor getEditor(WflServiceTypeDefinition def) {
    	try {
    		if(def.getScriptEditor()!=null && def.getScriptEditor().trim().length()>0) {
	        	Class compilerClass = Class.forName(def.getScriptEditor());
	        	return (WflScriptEditor)compilerClass.newInstance();
    		}
    		else {
    			WflUtil.showErrorMessage("El tipo de servicio no tiene asociado editor de script.", "Editor de Script");
    		}
		} catch (Exception e) {
            WflUtil.showErrorMessage(e.getClass().getName() + ": " + e.getMessage(), "Editor de Script");
		}
		return null;
    }

    /**
     * Chequea si un script compila.
     * Si no compila muestra el error por pantalla.
     * @param def Definicion a usar. 
     * @param script Script a compilar.
     */
    private void checkCompileErrors(String script, WflServiceTypeDefinition def) {
    	WflScriptCompilerItf compiler = getCompiler(def);
    	if(compiler!=null) {
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	if(!compiler.compile(script, new PrintWriter(baos))) {
	            WflUtil.showErrorMessage(baos.toString(), "Compilador de Script");        		
	    	}
    	}
    }
    
    private void showScriptEditorRB() {
    	WflServiceTypeDefinition def = WflServiceTypeDefinition.SERVICE_TYPES[cmbServiceTypeRB.getSelectedIndex()-1];    	
        String c = getScriptRB();
        WflScriptDlg dlg = new WflScriptDlg(this, c, getCompiler(def), getEditor(def));
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
        	// Tengo que compilar el script
        	checkCompileErrors(dlg.getScript(), def);
            setScriptRB(dlg.getScript());   	
    }
    
    private void setupServiceTypeEditor(String overrideDefault) {
    	WflServiceTypeDefinition def = WflServiceTypeDefinition.SERVICE_TYPES[cmbServiceType.getSelectedIndex()];
		this.txtClassName.setEnabled(!def.isScriptable());
		this.lblClassName.setEnabled(!def.isScriptable());
		this.btnClassName.setEnabled(!def.isScriptable());
		if(overrideDefault!=null)
			this.txtClassName.setText(overrideDefault);
		else
			this.txtClassName.setText(def.getDefaultFunction());
		this.btnScriptEditor.setEnabled(def.isScriptable());
		if(!def.isScriptable())
			setScript("");
		else {
			if(getScript()==null || getScript().trim().length()==0) {
				setScript(def.getDefaultScript());
			}
		}
    }

    private void setupServiceTypeRBEditor(String overrideDefault) {
    	// Si es el primero es el que esta en blanco.
    	if(cmbServiceTypeRB.getSelectedIndex()==0) {
			this.txtClassNameRB.setEnabled(false);
			this.lblClassNameRB.setEnabled(false);
			this.btnClassNameRB.setEnabled(false);
			this.txtClassNameRB.setText("");
			this.btnScriptEditorRB.setEnabled(false);			
			setScriptRB("");
    	}
    	else {
	    	WflServiceTypeDefinition def = WflServiceTypeDefinition.SERVICE_TYPES[cmbServiceTypeRB.getSelectedIndex()-1];
			this.txtClassNameRB.setEnabled(!def.isScriptable());
			this.lblClassNameRB.setEnabled(!def.isScriptable());
			this.btnClassNameRB.setEnabled(!def.isScriptable());
			if(overrideDefault!=null)
				this.txtClassNameRB.setText(overrideDefault);
			else
				this.txtClassNameRB.setText(def.getDefaultFunction());
			this.btnScriptEditorRB.setEnabled(def.isScriptable());
			if(!def.isScriptable())
				setScriptRB("");
			else {
				if(getScriptRB()==null || getScriptRB().trim().length()==0) {
					setScriptRB(def.getDefaultScript());
				}
			}
    	}
    }

    
    private void showComment() {
        String c = getComment();
        WflCommentDlg dlg = new WflCommentDlg(c);
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
            txtComment.setText(dlg.getComment());
    }

    private void openFinderClassRB() {
        String s = WflSelectClassDlg.openFinderClass(this);
        if (s != null)
            txtClassNameRB.setText(s);
    }
    private void openFinderClass() {
        String s = WflSelectClassDlg.openFinderClass(this);
        if (s != null)
            txtClassName.setText(s);
    }

    private void addComponents() {
        panel.add(lblId);
        panel.add(txtId);
        panel.add(lblName);
        panel.add(txtName);

        JPanel r = getTabRollback();
        JPanel f = getTabForward();

        tabbedPane.add("Forward", f);
        tabbedPane.add("Rollback", r);
        panel.add(tabbedPane);

        panel.add(lblPriority);
        panel.add(cmbPriority);

        panel.add(chkCarryContext);

        panel.add(lblType);
        panel.add(rdbTypeNormal);
        panel.add(rdbTypeIterative);

        bgType.add(rdbTypeIterative);
        bgType.add(rdbTypeNormal);

        panel.add(lblSecurityObject);
        panel.add(txtSecurityObject);

        panel.add(lblComment);
        panel.add(spComment);
        panel.add(btnComment);
    }

    private JPanel getTabRollback() {
        rollbackPanel = new JPanel(null);
        rollbackPanel.add(lblExecTypeRB);
        rollbackPanel.add(rdbExecTypeRBSS);
        rollbackPanel.add(rdbExecTypeRBSA);
        rollbackPanel.add(rdbExecTypeRBCI);
        rollbackPanel.add(rdbExecTypeRBPE);

        bgExecTypeRB.add(rdbExecTypeRBCI);
        bgExecTypeRB.add(rdbExecTypeRBPE);
        bgExecTypeRB.add(rdbExecTypeRBSA);
        bgExecTypeRB.add(rdbExecTypeRBSS);

        rollbackPanel.add(lblExecutionRB);
        rollbackPanel.add(rdbExeRBSinc);
        rollbackPanel.add(rdbExeRBAsinc);

        bgExeRB.add(rdbExeRBSinc);
        bgExeRB.add(rdbExeRBAsinc);

        rollbackPanel.add(lblExecutionNameRB);
        rollbackPanel.add(txtExecutionNameRB);

        rollbackPanel.add(lblServiceTypeRB);
        rollbackPanel.add(cmbServiceTypeRB);
        rollbackPanel.add(btnScriptEditorRB);
    
        rollbackPanel.add(lblClassNameRB);
        rollbackPanel.add(txtClassNameRB);
        rollbackPanel.add(btnClassNameRB);
        
        rollbackPanel.add(lblRoleRB);
        rollbackPanel.add(txtRoleRB);
        rollbackPanel.add(lblRoleParametricObjectIdRB);
        rollbackPanel.add(txtRoleParametricObjectIdRB);

        return rollbackPanel;
    }

    private JPanel getTabForward() {
        forwardPanel = new JPanel(null);
        forwardPanel.add(lblExecType);
        forwardPanel.add(rdbExecTypeSS);
        forwardPanel.add(rdbExecTypeSA);
        forwardPanel.add(rdbExecTypeCI);
        forwardPanel.add(rdbExecTypePE);

        bgExecType.add(rdbExecTypeCI);
        bgExecType.add(rdbExecTypePE);
        bgExecType.add(rdbExecTypeSA);
        bgExecType.add(rdbExecTypeSS);

        forwardPanel.add(lblExecution);
        forwardPanel.add(rdbExeSinc);
        forwardPanel.add(rdbExeAsinc);

        bgExe.add(rdbExeSinc);
        bgExe.add(rdbExeAsinc);

        forwardPanel.add(lblExecutionName);
        forwardPanel.add(txtExecutionName);
        
        forwardPanel.add(lblServiceType);
        forwardPanel.add(cmbServiceType);
        forwardPanel.add(btnScriptEditor);
        
        forwardPanel.add(lblClassName);
        forwardPanel.add(txtClassName);
        forwardPanel.add(btnClassName);
        
        forwardPanel.add(lblRole);
        forwardPanel.add(txtRole);
        forwardPanel.add(lblRoleParametricObjectId);
        forwardPanel.add(txtRoleParametricObjectId);

        return forwardPanel;
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 145;
        int x2 = x1 + w + sep;
        int h = 20;

        lblId.setBounds(x1, y, w, h);
        lblName.setBounds(x2, y, w, h);
        y += h;
        txtId.setBounds(x1, y, w, h);
        txtName.setBounds(x2, y, w, h);
        y += h + sep;

        setBoundsTabs(w, h, sep);
        int hTab = rollbackPanel.getHeight() + 30;
        tabbedPane.setBounds(x1, y, rollbackPanel.getWidth(), hTab);
        y += hTab + sep;

        lblPriority.setBounds(x1, y, w, h);
        chkCarryContext.setBounds(x2, y, w, h);
        y += h;
        cmbPriority.setBounds(x1, y, w, h);
        y += h + sep;

        lblType.setBounds(x1, y, w, h);
        lblSecurityObject.setBounds(x2, y, w, h);
        y += h;
        rdbTypeIterative.setBounds(x1, y, w, h);
        txtSecurityObject.setBounds(x2, y, w, h);
        y += h;
        rdbTypeNormal.setBounds(x1, y, w, h);
        y += h + sep;

        lblComment.setBounds(x1, y, w *2 + sep, h);
        y += h;
        int w2 = w * 2 + sep - 30;
        spComment.setBounds(x1, y, w2, h*2);
        btnComment.setBounds(w2+10, y, 30, h);

        y += h*2 + sep;

        Dimension dim = new Dimension(x2 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }

    private void setBoundsTabs(int w, int h, int sep) {
        int x1 = 5;
        int x2 = x1 + w;
        int w2 = w * 2 + sep;
        int y = 0;

        lblExecType.setBounds(x1, y, w, h);
        lblExecTypeRB.setBounds(x1, y, w, h);
        lblExecution.setBounds(x2, y, w, h);
        lblExecutionRB.setBounds(x2, y, w, h);
        y += h;
        rdbExecTypeSS.setBounds(x1, y, w, h);
        rdbExecTypeRBSS.setBounds(x1, y, w, h);
        rdbExeSinc.setBounds(x2, y, w, h);
        rdbExeRBSinc.setBounds(x2, y, w, h);
        y += h;
        rdbExecTypeSA.setBounds(x1, y, w, h);
        rdbExecTypeRBSA.setBounds(x1, y, w, h);
        rdbExeAsinc.setBounds(x2, y, w, h);
        rdbExeRBAsinc.setBounds(x2, y, w, h);
        y += h;
        int y2 = y + sep;
        rdbExecTypeCI.setBounds(x1, y, w, h);
        rdbExecTypeRBCI.setBounds(x1, y, w, h);
        y += h;
        rdbExecTypePE.setBounds(x1, y, w, h);
        rdbExecTypeRBPE.setBounds(x1, y, w, h);
        y += h + sep;

        lblExecutionName.setBounds(x2, y2, w, h);
        lblExecutionNameRB.setBounds(x2, y2, w, h);
        y2 += h;
        txtExecutionName.setBounds(x2, y2, w, h);
        txtExecutionNameRB.setBounds(x2, y2, w, h);

        lblRole.setBounds(x1, y, w, h);
        lblRoleParametricObjectId.setBounds(x2, y, w, h);
        lblRoleRB.setBounds(x1, y, w, h);
        lblRoleParametricObjectIdRB.setBounds(x2, y, w, h);
        
        y += h;
        
        txtRole.setBounds(x1, y, w, h);
        txtRoleParametricObjectId.setBounds(x2, y, w, h);
        txtRoleRB.setBounds(x1, y, w, h);
        txtRoleParametricObjectIdRB.setBounds(x2, y, w, h);
        
        y += h;
        
        lblServiceType.setBounds(x1, y, w, h);
        lblServiceTypeRB.setBounds(x1, y, w, h);
 
        y += h;

        cmbServiceType.setBounds(x1, y, w, h);
        cmbServiceTypeRB.setBounds(x1, y, w, h);
        btnScriptEditor.setBounds(x1 + w + sep, y, 20, h);
        btnScriptEditorRB.setBounds(x1 + w + sep, y, 20, h);
        
        y += h;

        lblClassName.setBounds(x1, y, w, h);
        lblClassNameRB.setBounds(x1, y, w, h);
        
        y += h;

        txtClassName.setBounds(x1, y, w2-30, h);
        txtClassNameRB.setBounds(x1, y, w2-30, h);
        btnClassName.setBounds(x1 + w2-30, y, 20, h);
        btnClassNameRB.setBounds(x1 + w2-30, y, 20, h);
        y += h + sep;

        forwardPanel.setSize(w2 + 3, y);
        rollbackPanel.setSize(w2+ 3, y);
    }

    protected void updateBObj() {
        WflFunctionBObj bobj = (WflFunctionBObj)getBObj();
        bobj.setId(txtId.getText());
        bobj.setName(txtName.getText());
        bobj.setExecutionSynchro(getExecutionSynchro());
        bobj.setExecutionType(getExecutionType());
        bobj.setServiceType(getServiceType());
        bobj.setFunctionClass(txtClassName.getText());
        bobj.setScript(getScript());
        
        bobj.setExecutionSynchroRB(getExecutionSynchroRB());
        bobj.setExecutionTypeRB(getExecutionTypeRB());
        bobj.setServiceTypeRB(getServiceTypeRB());
        bobj.setFunctionClassRB(getFunctionClassRB());
        bobj.setExecutionNameRB(getExecutionNameRB());
        bobj.setScriptRB(getScriptRB());
        
        bobj.setPriority(getPriority());
        bobj.setCarryContext(chkCarryContext.isSelected());
        bobj.setType(getType2());
        bobj.setExecutionName(txtExecutionName.getText());
        bobj.setSecurityObject(getSecurityObject());
        bobj.setComment(getComment());
        
        bobj.setRole(getRole());
        bobj.setRoleParametricObjectId(getRoleParametricObjectId());
        bobj.setRoleRB(getRoleRB());
        bobj.setRoleParametricObjectIdRB(getRoleParametricObjectIdRB());
    }

    private String getRoleParametricObjectIdRB() {
    	 String res = txtRoleParametricObjectIdRB.getText();
         if (res.trim().equals(""))
             res = null;

         return res;
	}

	private String getRoleRB() {
		 String res = txtRoleRB.getText();
	        if (res.trim().equals(""))
	            res = null;

	        return res;
	}

	private String getRoleParametricObjectId() {
		 String res = txtRoleParametricObjectId.getText();
	        if (res.trim().equals(""))
	            res = null;

	        return res;
	}

	private String getRole() {
		 String res = txtRole.getText();
	        if (res.trim().equals(""))
	            res = null;

	        return res;
	}

	private String getFunctionClassRB() {
        String res = txtClassNameRB.getText();
        if (res.trim().equals(""))
            res = null;

        return res;
    }

    private String getComment() {
        String res = txtComment.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }

    private String getExecutionSynchroRB() {
        if (getFunctionClassRB() == null)
            return null;

        if (rdbExeRBAsinc.isSelected())
            return WflFunctionBObj.EXECUTION_SYNCHRO_ASYNCHRONOUS;
        else if (rdbExeRBSinc.isSelected())
            return WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC;
        else
            return null;
    }
    private String getExecutionSynchro() {
        if (rdbExeAsinc.isSelected())
            return WflFunctionBObj.EXECUTION_SYNCHRO_ASYNCHRONOUS;
        else if (rdbExeSinc.isSelected())
            return WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC;
        else
            return null;
    }

    private String getExecutionTypeRB() {
        if (getFunctionClassRB() == null)
            return null;

        if (rdbExecTypeRBCI.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE;
        else if (rdbExecTypeRBSA.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS;
        else if (rdbExecTypeRBPE.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS;
        else if (rdbExecTypeRBSS.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC;
        else
            return null;
    }
    private String getExecutionType() {
        if (rdbExecTypeCI.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE;
        else if (rdbExecTypeSA.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS;
        else if (rdbExecTypePE.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS;
        else if (rdbExecTypeSS.isSelected())
            return WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC;
        else
            return null;
    }

    private String getServiceType() {
    	return WflServiceTypeDefinition.SERVICE_TYPES[this.cmbServiceType.getSelectedIndex()].getId();
    }
    
    private String getServiceTypeRB() {
    	if(this.cmbServiceTypeRB.getSelectedIndex()==0)
    		return "";
    	else
    		return WflServiceTypeDefinition.SERVICE_TYPES[this.cmbServiceTypeRB.getSelectedIndex()-1].getId();
    }

    protected boolean isOK() {
        String msg = null;
        if (txtId.getText().trim().equals(""))
            msg = "Falta ingresar el id";
        else if (txtName.getText().trim().equals(""))
            msg = "Falta ingresar el nombre";
        else if (txtClassName.getText().trim().equals(""))
            msg = "Falta ingresar la clase";
        else if(WflServiceTypeDefinition.SERVICE_TYPES[cmbServiceType.getSelectedIndex()].isScriptable() && 
        		(txtScript==null || this.txtScript.trim().equals("")))
        	msg = "Falta ingresar el script.";
        else if ( (txtRole.getText().trim().equals("")) && (!txtRoleParametricObjectId.getText().trim().equals("")) ){
        	msg = "Falta definir el rol (o quitar el id de rol parametrico)";
        }
        else if ( (txtRoleRB.getText().trim().equals("")) && (!txtRoleParametricObjectIdRB.getText().trim().equals("")) ){
        	msg = "Falta definir el rol de rollback (o quitar el id de rol parametrico)";
        }
        if (msg != null)
            WflUtil.showErrorMessage(msg, "Función");

        return msg == null;
    }
}
