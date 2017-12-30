package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.bobj.WflStateMarkBObj;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtility;
import crm.core.wfl.editor.gui.mnu.WflMarksBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflStateDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblId = new JLabel ("Id");
    private JTextField txtId = new JTextField ();

    private JLabel lblName = new JLabel ("Nombre");
    private JTextField txtName = new JTextField ();

    private JLabel lblMaskType = new JLabel ();
    private JRadioButton rdbMTAnd = new JRadioButton ("AND BINARIO");
    private JRadioButton rdbMTOr = new JRadioButton ("OR BINARIO");
    private JRadioButton rdbMTNone = new JRadioButton ("Ninguna");
    private ButtonGroup bgMT = new ButtonGroup();

    private JLabel lblMaskValue = new JLabel ("Valor");
    private JTextField txtMaskValue = new JTextField ();

    private JLabel lblParallelism = new JLabel ("Paralelismo dinámico");
    private JRadioButton rdbDPStart = new JRadioButton ("Comienzo");
    private JRadioButton rdbDPEnd = new JRadioButton ("Fin");
    private JRadioButton rdbDPNone = new JRadioButton ("Ninguno");
    private ButtonGroup bgDP = new ButtonGroup();
    
    private JCheckBox chkCancelState = new JCheckBox("Cancela Proceso");

    private JLabel lblClassName = new JLabel ("Clase");
    private JTextField txtClassName = new JTextField ();

    private JLabel lblFinalState = new JLabel ("Estado final");
    private JComboBox cmbFinalState = new JComboBox ();

    private JLabel lblComment = new JLabel ("Comentario");
    private JTextArea txtComment = new JTextArea ();
    private JScrollPane spComment = new JScrollPane (txtComment);
    private Icon ic = WflUtil.createImageIcon(WflConstants.ICON_EXPAND);
    private JButton btnComment = new JButton(ic);

    private JLabel lblMarks = new JLabel ("Hitos");
    WflMarksTable marksTable = new WflMarksTable();
    private JScrollPane spMarks = new JScrollPane(marksTable);
    Icon icRem = WflUtil.createImageIcon(WflConstants.ICON_MARK_STATE_REMOVE);
    Icon icAdd = WflUtil.createImageIcon(WflConstants.ICON_MARK_STATE_ADD);
    private JButton btnRemoveMarks = new JButton(icRem);
    private JButton btnAddMarkss = new JButton(icAdd);


    //Atributos originales del estado (antes de que se modifiquen en est dlg)
    private String id = null;
    private String name = null;
    private String maskType = null;
    private String maskValue = null;
    private String comment = null;
    private List<WflStateMarkBObj> stateMarks = null;
    private boolean cancelState;

    private boolean marksModified = false;

    public WflStateDlg() {
        super();
        init();
    }

    private void init(){
        addComponents();
        setBounds();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Estado");
    }

    public boolean wasModified() {
        return !(txtId.getText().equals(id)
                && txtName.getText().equals(name)
                && (((getMaskType() == null && maskType == null)
                || getMaskType() != null && getMaskType().equals(maskType)
                && txtMaskValue.getText().equals(maskValue)))
                && ((getComment() == null && comment == null)
                    || (getComment() != null && getComment().equals(comment)))
                && !marksModified &&
                chkCancelState.isSelected() != cancelState
        	    );
    }


    private String getComment() {
        String res = txtComment.getText();
        if (res.trim().equals(""))
            res = null;

        return res;
    }

    private void setProperties() {
        cmbFinalState.setEditable(true);
        //El campo MaskValue no está visible si rdbMTNone está seleccionado
        rdbMTAnd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });
        rdbMTNone.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });
        rdbMTOr.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setMaskValueVisible(!rdbMTNone.isSelected());
            }
        });

        btnRemoveMarks.setToolTipText("Borrar hitos");
        btnRemoveMarks.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeMarks();
            }
        });
        btnAddMarkss.setToolTipText("Agregar hitos");
        btnAddMarkss.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addMark();
            }
        });
        //sólo lectura
        rdbDPEnd.setEnabled(false);
        rdbDPNone.setEnabled(false);
        rdbDPStart.setEnabled(false);
        txtClassName.setEnabled(false);
        cmbFinalState.setEnabled(false);
        btnComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComment();
            }
        });

        //tooltip
        rdbMTAnd.setToolTipText("Comparación a nivel de bits (AND)");
        rdbMTOr.setToolTipText("Comparación a nivel de bits (OR)");
    }

    private void showComment() {
        String c = getComment();
        WflCommentDlg dlg = new WflCommentDlg(c);
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
            txtComment.setText(dlg.getComment());
    }

    /**
     * Borra los Hitos que están seleccionados en la lista.
     */
    private void removeMarks() {
        List sv = marksTable.getSelected();
        if (sv.size() == 0){
            String msg = "No se han elegido hitos para borrar.";
            WflUtil.showMessage(msg, "Borrar hitos");
            return ;
        }

        //Del objeto los borro si se aprieta "Aceptar"

        marksTable.removeSelected();
        marksModified = true ;
    }

    private void addMark() {
        //Excluyo de la lista a mostrar los hitos que ya se agregaron.

        List<WflStateMarkBObj> currentSM = marksTable.getStateMarks();
        List<WflMarkBObj> currentMarks = new ArrayList<WflMarkBObj>(currentSM.size());
        for (int i = 0; i < currentSM.size(); i++) {
            WflStateMarkBObj sm = (WflStateMarkBObj) currentSM.get(i);
            currentMarks.add(sm.getMark());
        }

        WflMarksBar mb = WflUtility.getMarksBar();
        List allMarks = mb.getMarks();

        List<WflMarkBObj> showMarks = new ArrayList<WflMarkBObj>();
        for (int i = 0; i < allMarks.size(); i++) {
            WflMarkBObj bo = (WflMarkBObj) allMarks.get(i);
            if (!currentMarks.contains(bo))
                showMarks.add(bo);
        }

        //Abro el diálogo para seleccionar hitos
        List toAdd = WflSelectMarksDlg.openSelectMarks(this, showMarks);

        //agrago los hitos seleccionados a la lista de hitos del estado
        //El objeto se modifica cuando se aprieta "Aceptar"
        if (toAdd != null && toAdd.size() > 0){
            marksModified = true ;
            List<WflStateMarkBObj> sm = marksTable.getStateMarks();
            for (int i = 0; i < toAdd.size(); i++) {
                WflStateMarkBObj bo = (WflStateMarkBObj)toAdd.get(i);
                sm.add(bo);
            }
            marksTable.setStateMarks(sm);
        }
    }

    private void setMaskValueVisible(boolean b) {
        lblMaskValue.setVisible(b);
        txtMaskValue.setVisible(b);
    }

    /**
     * Hace visible/invisible los campos que dependen del paralelismo dinámico
     * @param b
     */
    private void setStartParVisible(boolean b) {
        lblClassName.setVisible(b);
        txtClassName.setVisible(b);
        lblFinalState.setVisible(b);
        cmbFinalState.setVisible(b);
    }

    private void addComponents() {
        panel.add(lblId);
        panel.add(txtId);

        panel.add(lblName);
        panel.add(txtName);

        panel.add(lblMaskType);
        panel.add(rdbMTNone);
        panel.add(rdbMTAnd);
        panel.add(rdbMTOr);

        bgMT.add(rdbMTNone);
        bgMT.add(rdbMTAnd);
        bgMT.add(rdbMTOr);

        panel.add(lblMaskValue);
        panel.add(txtMaskValue);

        panel.add(lblParallelism);
        panel.add(rdbDPNone);
        panel.add(rdbDPStart);
        panel.add(rdbDPEnd);
        
        panel.add(chkCancelState);

        bgDP.add(rdbDPEnd);
        bgDP.add(rdbDPStart);
        bgDP.add(rdbDPNone);

        panel.add(lblClassName);
        panel.add(txtClassName);

        panel.add(lblComment);
        panel.add(spComment);
        panel.add(btnComment);

        panel.add(lblFinalState);
        panel.add(cmbFinalState);

        panel.add(lblMarks);
        panel.add(spMarks);
        panel.add(btnAddMarkss);
        panel.add(btnRemoveMarks);
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

        int wc = x2 + w - 10;
        lblComment.setBounds(x1, y, wc, h);
        y += h;
        spComment.setBounds(x1, y, wc-30, h*2);
        btnComment.setBounds(x1 + wc - 30, y, 30, h);

        y += h *2+ sep;

        lblParallelism.setBounds(x1, y, w, h);
        
        chkCancelState.setBounds(x2, y, w, h);
        
        y += h;
        rdbDPNone.setBounds(x1, y, w, h);
        lblClassName.setBounds(x2, y, w, h);
        y += h;
        rdbDPStart.setBounds(x1, y, w, h);
        txtClassName.setBounds(x2, y, w, h);
        rdbDPEnd.setBounds(x1, y + h, w, h);
        y += h + sep;
        lblFinalState.setBounds(x2, y, w, h);
        y += h;
        cmbFinalState.setBounds(x2, y, w, h);
        y += h + sep;
        lblMarks.setBounds(x1, y, w, h);
        y += h;
        int w2 = x2 + w - 40;
        spMarks.setBounds(x1, y, w2, h * 3);
        btnAddMarkss.setBounds(x1 + w2 + 10, y + 5, 20, h);
        btnRemoveMarks.setBounds(x1 + w2 + 10, y + 35, 20, h);

        y += h  * 3 + sep;

        Dimension dim = new Dimension(x2 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }


    /**
     * Llena los campos del panel
     */
    protected void updateDlg() {

        WflStateBObj obj = (WflStateBObj)getBObj();
        if (WflStateBObj.POSITION_INITIAL.equals(obj.getPosition()))
            lblMaskType.setText("Estado inicial condicional");
        else
            lblMaskType.setText("Estado final condicional");

        id = obj.getId();
        name = obj.getName();
        maskType = obj.getMaskType();
        maskValue = obj.getMaskValue();
        stateMarks = obj.getStateMarks();
        comment = obj.getComment();
        cancelState = obj.getCancelState().equals(WflStateBObj.CANCEL_STATE); 
        
        chkCancelState.setSelected(cancelState);
        marksTable.setStateMarks(stateMarks);
        txtId.setText(id);
        txtName.setText(name);
        setMaskType(maskType);
        txtMaskValue.setText(maskValue);
        txtComment.setText(comment);
        setParallelism(obj.getParallelism());
        if (WflStateBObj.PARALLELISM_START.equals(obj.getParallelism())){
            txtClassName.setText(obj.getClassName());
            setFinalState(obj.getFinalState());
        }
        setStartParVisible(rdbDPStart.isSelected());
        setMaskValueVisible (! (maskType == null));
    }

    private void setFinalState(String finalState) {
        for (int i = 0; i < cmbFinalState.getItemCount(); i++) {
            if (cmbFinalState.getItemAt(i).equals(finalState)){
                cmbFinalState.setSelectedIndex(i);
                return ;
            }
        }
        cmbFinalState.addItem(finalState);
    }

    private void setParallelism(String p) {
        if (WflStateBObj.PARALLELISM_START.equals(p))
            rdbDPStart.setSelected(true);
        else if (WflStateBObj.PARALLELISM_END.equals(p) )
            rdbDPEnd.setSelected(true);
        else
            rdbDPNone.setSelected(true);
    }

    private void setMaskType(String maskType) {
        if (WflStateBObj.MASK_TYPE_AND.equals(maskType))
            rdbMTAnd.setSelected(true);
        else if (WflStateBObj.MASK_TYPE_OR.equals(maskType) )
            rdbMTOr.setSelected(true);
        else{
            rdbMTNone.setSelected(true);
        }
    }

    protected void updateBObj() {
        WflStateBObj bobj = (WflStateBObj)getBObj();
        bobj.setId(txtId.getText());
        bobj.setName(txtName.getText());
        String mt = getMaskType();

        if (mt == null ){
            bobj.setMaskType(null);
            bobj.setMaskValue(null);
        }
        else{
            bobj.setMaskType(mt);
            bobj.setMaskValue(txtMaskValue.getText());
        }
        bobj.setParallelism(getParallelism());
        bobj.setClassName(getClassName());
        bobj.setFinalState(getFinalState());
        bobj.setStateMarks(stateMarks);
        bobj.setComment(getComment());

        if(this.chkCancelState.isSelected())
        	bobj.setCancelState(WflStateBObj.CANCEL_STATE);
        else
        	bobj.setCancelState(WflStateBObj.NO_CANCEL_STATE);

        if (marksModified)
            bobj.setStateMarks(marksTable.getStateMarks());
    }

    private String getClassName() {
        if (WflStateBObj.PARALLELISM_START.equals(getParallelism())){
            return txtClassName.getText();
        }
        return null;
    }

    private String getFinalState() {
        if (WflStateBObj.PARALLELISM_START.equals(getParallelism())){
            Object si = cmbFinalState.getSelectedItem();
            if (si != null)
                return si.toString();
        }
        return null;
    }

    private String getParallelism() {
        if (rdbDPStart.isSelected())
            return WflStateBObj.PARALLELISM_START;
        else if (rdbDPEnd.isSelected())
            return WflStateBObj.PARALLELISM_END;
        else
            return WflStateBObj.PARALLELISM_NONE;
    }

    private String getMaskType() {
        if (rdbMTAnd.isSelected())
            return WflStateBObj.MASK_TYPE_AND;
        else if (rdbMTOr.isSelected())
            return WflStateBObj.MASK_TYPE_OR;
        else
            return null;
    }

    protected boolean isOK() {
        String msg = null;
        if (txtId.getText().trim().equals(""))
            msg = "Falta ingresar el id";
        else if (txtName.getText().trim().equals(""))
            msg = "Falta ingresar el nombre";
        else if (getMaskType() != null && txtMaskValue.getText().trim().equals(""))
            msg = "Falta ingresar el valor de la máscara";

        if (msg != null)
            WflUtil.showErrorMessage(msg, "Estado");
        return msg == null;
    }
}
