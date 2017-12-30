package crm.core.wfl.editor.gui.dlg;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import crm.core.wfl.editor.bobj.WflObjectConstraintBObj;
import crm.core.wfl.editor.bobj.WflPropertyBObj;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflProcessDefinitionDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblId = new JLabel ("Id");
    private JTextField txtId = new JTextField ();

    private JLabel lblVersion = new JLabel ("Versión");
    private JTextField txtVersion = new JTextField ();

    private JLabel lblName = new JLabel ("Nombre");
    private JTextField txtName = new JTextField ();

    private JLabel lblTrace = new JLabel ("Seguimiento");
    private JRadioButton rdbTraceYes = new JRadioButton("Generar seguimiento");
    private JRadioButton rdbTraceNo = new JRadioButton("No generar seguimiento");
    private JRadioButton rdbTraceYesSeq = new JRadioButton("En archivo secuencial");

    private JLabel lblSecurityObject = new JLabel ("Objeto de seguridad");
    private JTextField txtSecurityObject = new JTextField ();
    private JLabel lblComment = new JLabel ("Comentario");
    private JTextArea txtComment = new JTextArea ();
    private Icon ic = WflUtil.createImageIcon(WflConstants.ICON_EXPAND);
    private JButton btnComment = new JButton(ic);
    private JScrollPane spComment = new JScrollPane(txtComment);
    
    private JLabel lblProperties = new JLabel ("Properties del proceso");
    private WflPropertiesTable tblProperties = new WflPropertiesTable();
    private JScrollPane spProperties = new JScrollPane(tblProperties);
    private JButton btnRemoveProperty = new JButton(WflUtil.createImageIcon(WflConstants.ICON_PROPERTIES_REMOVE));
    private JButton btnAddProperty = new JButton(WflUtil.createImageIcon(WflConstants.ICON_PROPERTIES_ADD));    
    
    private JLabel lblObjectConstraints = new JLabel ("Objetos");
    private WflObjectConstraintsTable tblObjectConstraints = new WflObjectConstraintsTable();
    private JScrollPane spObjectConstraints= new JScrollPane(tblObjectConstraints);
    private JButton btnRemoveObjectConstraint = new JButton(WflUtil.createImageIcon(WflConstants.ICON_OBJECT_CONSTRAINT_REMOVE));
    private JButton btnAddObjectConstraint = new JButton(WflUtil.createImageIcon(WflConstants.ICON_OBJECT_CONSTRAINT_ADD));    


    private String id = null;
    private String name = null;
    private String version = null;
    private String trace = null;
    private String securityObject = null;
    private String comment = null;
    private java.util.List<WflPropertyBObj> properties = null;
    private java.util.List constraints = null;    


    public WflProcessDefinitionDlg() {
        this(null, null, null, null, null, null, new ArrayList<WflPropertyBObj>(), new ArrayList());
    }
    public WflProcessDefinitionDlg(String id, String name, String version,
                                   String trace, String securityObj, 
                                   String comment, 
                                   java.util.List<WflPropertyBObj> properties,
                                   java.util.List constraints) {
        super();
        this.id = id;
        this.version = version;
        this.name = name ;
        this.trace = trace ;
        this.securityObject = securityObj ;
        this.comment = comment ;
        this.properties = properties;
        this.constraints = constraints;
        init();
    }

	private void init(){
        addComponents();
        setBounds();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Definición del proceso");
        updateDlg();
    }


    private void setProperties() {
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbTraceNo);
        bg.add(rdbTraceYes);
        bg.add(rdbTraceYesSeq);

        btnComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComment();
            }
        });
        
        btnAddProperty.setToolTipText("Agregar Property");
        btnAddProperty.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	addProperty();
            }
        });
        btnRemoveProperty.setToolTipText("Borrar Property");
        btnRemoveProperty.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeProperty();
            }
        });
        
        btnAddObjectConstraint.setToolTipText("Agregar Restricción");
        btnAddObjectConstraint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	addObjectConstraint();
            }
        });
        btnRemoveObjectConstraint.setToolTipText("Borrar Restricción");
        btnRemoveObjectConstraint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeObjectConstraint();
            }
        });
    }
    
    /**
     * Borra los las properties que estan seleccionados.
     */
    private void removeProperty() {
        java.util.List sv = tblProperties.getSelected();
        if (sv.size() == 0){
            String msg = "No se han elegido properties para borrar.";
            WflUtil.showMessage(msg, "Borrar properties");
            return ;
        }

        //Del objeto los borro si se aprieta "Aceptar"

        tblProperties.removeSelected();
        // TODO marksModified = true ;
    }
    
    /**
     * Agrega una property en blanco a la lista.
     */
    private void addProperty() {
        java.util.List<WflPropertyBObj> properties = tblProperties.getProperties();
        properties.add(new WflPropertyBObj("Nueva Property"));
        tblProperties.setProperties(properties);
    }    

    private void showComment() {
        String c = getComment();
        WflCommentDlg dlg = new WflCommentDlg(c);
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
            txtComment.setText(dlg.getComment());
    }

    /**
     * Agrega una restriccion en blanco a la lista.
     */
    private void addObjectConstraint() {
        java.util.List<WflObjectConstraintBObj> constraints = tblObjectConstraints.getConstraints();
        constraints.add(new WflObjectConstraintBObj());
        tblObjectConstraints.setConstraints(constraints);
    }
    
    /**
     * Borra los las restricciones que estan seleccionados.
     */
    private void removeObjectConstraint() {
        java.util.List sv = tblObjectConstraints.getSelected();
        if (sv.size() == 0){
            String msg = "No se han elegido restricciones para borrar.";
            WflUtil.showMessage(msg, "Borrar restricciones");
            return ;
        }

        //Del objeto los borro si se aprieta "Aceptar"
        tblObjectConstraints.removeSelected();
        // TODO marksModified = true ;
    }
    

    private void addComponents() {
        panel.add(lblId);
        panel.add(txtId);

        panel.add(lblVersion);
        panel.add(txtVersion);

        panel.add(lblName);
        panel.add(txtName);

        panel.add(lblTrace);
        panel.add(rdbTraceNo);
        panel.add(rdbTraceYes);
        panel.add(rdbTraceYesSeq);

        panel.add(lblSecurityObject);
        panel.add(txtSecurityObject);

        panel.add(lblComment);
        panel.add(spComment);
        panel.add(btnComment);
        panel.add(lblProperties);        
        panel.add(spProperties);
        panel.add(btnRemoveProperty);
        panel.add(btnAddProperty);        
        
        panel.add(lblObjectConstraints);        
        panel.add(spObjectConstraints);
        panel.add(btnRemoveObjectConstraint);
        panel.add(btnAddObjectConstraint);        
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 135;
        int w2 = 165;
        int w3 = 250;
        int w4 = 570;        
        int x2 = x1 + w + sep;
        int x3 = x2 + w2 + sep;        
        int h = 20;

        lblId.setBounds(x1, y, w, h);
        lblName.setBounds(x2, y, w, h);
        lblVersion.setBounds(x3, y, w, h);
        y += h;
        txtId.setBounds(x1, y, w, h);
        txtName.setBounds(x2, y, w, h);
        txtVersion.setBounds(x3, y, w, h);
        
        y += h + sep;
        lblSecurityObject.setBounds(x1, y, w, h);
        lblTrace.setBounds(x2, y, w, h);
        lblComment.setBounds(x3, y, w, h);
        
        y += h;        
        txtSecurityObject.setBounds(x1, y, w, h);
        rdbTraceYes.setBounds(x2, y, w2, h);
        spComment.setBounds(x3, y, w3, h *2);
        btnComment.setBounds(x3 + w3, y, 30, h);
        y += h;
        rdbTraceYesSeq.setBounds(x2, y, w2, h);
        y += h;
        rdbTraceNo.setBounds(x2, y, w2, h);
        
        y += h + sep;
        lblProperties.setBounds(x1, y, w, h);
        y += h;        
        spProperties.setBounds(x1, y, w4, h*5);
        
        btnAddProperty.setBounds(x3 + w3 + sep, y, 20, h);
        y += h + sep;
        btnRemoveProperty.setBounds(x3 + w3 + sep, y, 20, h);
        
        y += h*4 + sep;
        lblObjectConstraints.setBounds(x1, y, w, h);
        y += h;        
        spObjectConstraints.setBounds(x1, y, w4, h*5);
        
        btnAddObjectConstraint.setBounds(x3 + w3 + sep, y, 20, h);
        y += h + sep;
        btnRemoveObjectConstraint.setBounds(x3 + w3 + sep, y, 20, h);

        y += h*4 + sep;
        
        Dimension dim = new Dimension(x3 + w3 + 30 + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }


    /**
     * Llena los campos del panel
     */
    public void updateDlg() {
        txtId.setText(id);
        txtName.setText(name);
        txtVersion.setText(version);
        txtSecurityObject.setText(securityObject);
        txtComment.setText(comment);
        tblProperties.setProperties(copyProperties(properties));
        tblObjectConstraints.setConstraints(copyObjectConstraints(constraints));        
        setTrace();
    }
    
    /**
     * Copia una lista de properties.
     * @param properties Properties a copiar.
     * @return Copia de todos las properties.
     */
    private List<WflPropertyBObj> copyProperties(List<WflPropertyBObj> properties) {
    	List<WflPropertyBObj> newProperties = new ArrayList<WflPropertyBObj>(properties.size());
        for(int i=0; i<properties.size(); i++) {
        	WflPropertyBObj p = properties.get(i);
        	WflPropertyBObj newP = new WflPropertyBObj(p.getName());
        	newProperties.add(newP); 
        }
        return newProperties;
    }
    
    /**
     * Copia una lista de restricciones.
     * @param constraints Restricciones a copiar.
     * @return Copia de todos las restricciones.
     */
    private List<WflObjectConstraintBObj> copyObjectConstraints(List constraints) {
    	List<WflObjectConstraintBObj> newConstraints= new ArrayList<WflObjectConstraintBObj>(constraints.size());
        for(int i=0; i<constraints.size(); i++) {
        	WflObjectConstraintBObj p = (WflObjectConstraintBObj)constraints.get(i);
        	WflObjectConstraintBObj newP = new WflObjectConstraintBObj(p.getLabel(),
        														       p.isNotNull(),
        														       p.isUnique(),
        														       p.getTargetProcessDefinitionId());
        	newConstraints.add(newP); 
        }
        return newConstraints;
    }

    /**
     * Compara dos listas de properties.
     * @param pA Primera lista.
     * @param pB Segunda lista.
     * @return true si las dos listas son iguales en cantidad, orden y contenido.
     */
    private boolean compareProperties(List pA, List pB) {
    	if(pA.size()!=pB.size())
    		return false;
    	 
        for(int i=0; i<pA.size(); i++) {
        	WflPropertyBObj aliasA = (WflPropertyBObj)pA.get(i);
        	WflPropertyBObj aliasB = (WflPropertyBObj)pB.get(i);
        	if(!aliasA.getName().equals(aliasB.getName()))
        		return false;
        }    	
        return true;
    }

    private void setTrace(){
        if (trace == null || trace.trim().equals(""))
            rdbTraceYes.setSelected(true);
        else if (trace.equalsIgnoreCase("s"))
            rdbTraceYesSeq.setSelected(true);
        else if(trace.equalsIgnoreCase("n"))
            rdbTraceNo.setSelected(true);
        else{
            System.out.println("Problemas en WflProcessDefinitionDlg.setTrace()");
        }
    }

    public String getTrace() {
        if (rdbTraceNo.isSelected())
            return "N";
        else if(rdbTraceYes.isSelected())
            return " ";
        //if (rdbTraceYesSeq.isSelected())
        return "S";
    }


    public boolean wasModified(){
        return !(getId().equals(id)
                && getVersion().equals(version)
                && getName().equals(name)
                && ((getSecurityObject() == null && securityObject == null)
                    ||(getSecurityObject() != null && getSecurityObject().equals(securityObject)))
                && ((getComment() == null && comment == null)
                    ||(getComment() != null && getComment().equals(comment)))
                && ((getTrace() == null && trace == null)
                || (getTrace() != null && getTrace().equals(trace)))) || !compareProperties(this.properties, getProperties());
    }

    public String getSecurityObject(){
        String c = txtSecurityObject.getText();
        if (c.trim().equals(""))
            c = null;
        return c;
    }

    public String getComment(){
        String c = txtComment.getText();
        if (c.trim().equals(""))
            c = null;
        return c;
    }

    protected void updateBObj() {
        //de la actualizacion de objetos se ocupa el que invoca al dlg
    }

    public boolean isOK() {
        if (wasCancel)
            return false;
        String msg = null;

        if (getName().trim().equals(""))
            msg = "Se debe ingresar el nombre";

        if (getId().trim().equals(""))
            msg = "Se debe ingresar el id";

        if (getVersion().trim().equals(""))
            msg = "Se debe ingresar la versión";

        // Debo validar la tabla de properties
        List properties = tblProperties.getProperties();
        for(int i=0; i<properties.size(); i++) {
        	WflPropertyBObj property = (WflPropertyBObj)properties.get(i);
        	
        	if(property.getName()==null || property.getName().trim().length()==0)
        		msg = "Existen properties que no tienen asignado un nombre.";

        	// Chequeo nombres duplicados.
        	for(int j=0; j<properties.size(); j++) {
        		if(j!=i) {
                	WflPropertyBObj propertyB = (WflPropertyBObj)properties.get(j);
                	if(property.getName().equals(propertyB.getName()))
                		msg = "Existen properties que tiene el nombre repetido.";
        		}
        	}
        }
        
        List constraints = tblObjectConstraints.getConstraints();
        for(int i=0; i<constraints.size(); i++) {
        	WflObjectConstraintBObj constraint = (WflObjectConstraintBObj)constraints.get(i);
        	
        	if(constraint.getLabel()==null || constraint.getLabel().trim().length()==0)
        		msg = "Existen objetos que no tienen asignado una etiqueta.";

        	if(constraint.isUnique() && 
        			(constraint.getTargetProcessDefinitionId()==null || constraint.getTargetProcessDefinitionId().trim().length()==0))
        		msg = "Si se especificó que un objeto es unico, debe indicarse contra que tipo de proceso se chequea unicidad.";
        }

        if (msg != null){
            WflUtil.showErrorMessage(msg, "Definición de proceso");
            return false;
        }
        else
            return true;

    }

    public String getName() {
        return txtName.getText();
    }

    public String getId() {
        return txtId.getText();
    }

    public String getVersion() {
        return txtVersion.getText();
    }
    
    /**
	 * Obtiene las properties.
	 * @return Retorna las properties.
	 */
	public java.util.List<WflPropertyBObj> getProperties() {
		return tblProperties.getProperties();
	}
    
	public java.util.List getConstraints() {
		return tblObjectConstraints.getConstraints();
	}
}
