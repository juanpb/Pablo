package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.bobj.WflMarkBObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflMarkDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(null);

    private JLabel lblId = new JLabel ("Id");
    private JTextField txtId = new JTextField ();

    private JLabel lblName = new JLabel ("Nombre");
    private JTextField txtName = new JTextField ();

    private JCheckBox chkDeletable = new JCheckBox ("Borrable");

    private JLabel lblComment = new JLabel ("Comentario");
    private JTextArea txtComment = new JTextArea ();
    private JScrollPane spComment = new JScrollPane (txtComment);
    private Icon ic = WflUtil.createImageIcon(WflConstants.ICON_EXPAND);
    private JButton btnComment = new JButton (ic);

    private String id = null;
    private String name = null;
    private String comment = null;
    private boolean deletable = false;

    public WflMarkDlg() {
        this(null);
    }
    public WflMarkDlg(WflMarkBObj bo) {
        super();
        super.setBObj(bo);
        init();
    }

    private void init(){
        addComponents();
        setBounds();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Hito");
        updateDlg();
    }

    private void setProperties() {
        btnComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComment();
            }
        });
    }

    private void showComment() {
        String c = getComment();
        WflCommentDlg dlg = new WflCommentDlg(c);
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
            txtComment.setText(dlg.getComment());
    }

    private void addComponents() {
        panel.add(lblId);
        panel.add(txtId);

        panel.add(lblName);
        panel.add(txtName);

        panel.add(chkDeletable);

        panel.add(lblComment);
        panel.add(spComment);
        panel.add(btnComment);
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 135;
        int x2 = x1 + w + sep;
        int h = 20;

        lblId.setBounds(x1, y, w, h);
        lblName.setBounds(x2, y, w, h);
        y += h;
        txtId.setBounds(x1, y, w, h);
        txtName.setBounds(x2, y, w, h);
        y += h + sep;

        chkDeletable.setBounds(x1, y, w, h);
        y += h + sep;
        lblComment.setBounds(x1, y, w, h);
        y += h;
        spComment.setBounds(x1, y, w*2+sep-30, h*2);
        btnComment.setBounds(x1 + w*2+sep-30, y, 30, h);
        y += h*2 + sep;

        Dimension dim = new Dimension(x2 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }


    /**
     * Llena los campos del panel
     */
    public void updateDlg() {
        WflMarkBObj markBObj = (WflMarkBObj)getBObj();
        if (markBObj != null){
            id = markBObj.getId();
            name = markBObj.getName();
            deletable = markBObj.isDeletable();
            comment = markBObj.getCommnet();

            txtId.setText(id);
            txtName.setText(name);
            chkDeletable.setSelected(deletable);
            txtComment.setText(comment);
        }
        else{
            txtId.setText("");
            txtName.setText("");
            txtComment.setText("");
            chkDeletable.setSelected(false);
            id = null;
            name = null;
            comment = null;
            deletable = false;
        }
    }

    public boolean wasModified(){
        return !(getId().equals(id)
                && isDeletable() == deletable
                && ((getComment() == null && comment == null)
                || (getComment() != null && getComment().equals(comment)))
                && getMarkName().equals(name));
    }

    private String getComment() {
        String res = txtComment.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }

    protected void updateBObj() {
        WflMarkBObj bo = (WflMarkBObj)getBObj();
        bo.setDeletable(isDeletable());
        bo.setId(getId());
        bo.setName(getMarkName());
        bo.setCommnet(getComment());
    }

    public boolean isOK() {
        if (wasCancel)
            return false;
        String msg = null;

        if (getMarkName().trim().equals(""))
            msg = "Se debe ingresar el nombre";

        if (getId().trim().equals(""))
            msg = "Se debe ingresar el id";

        if (msg != null){
            WflUtil.showErrorMessage(msg);
            return false;
        }
        else
            return true;

    }

    private String getMarkName() {
        return txtName.getText();
    }

    private String getId() {
        return txtId.getText();
    }

    private boolean isDeletable() {
        return chkDeletable.isSelected();
    }

    public void showForEdit() {
        showForEdit(false);
    }
    public void showForEdit(boolean disableDelete) {
        setTitle("Editar Hito");
        if (disableDelete){
            chkDeletable.setEnabled(false);
            String s = "No puede ser borrable porque hay estados que lo " +
                    "tienen como 'Baja'";
            chkDeletable.setToolTipText(s);
        }
        else{
            chkDeletable.setEnabled(true);
            chkDeletable.setToolTipText(null);
        }
        super.showMe();
    }

    public void showForDelete() {
        setTitle("Borrar Hito");
        disableAll();
        super.showMe();
    }

    private void disableAll() {
        txtId.setEnabled(false);
        txtName.setEnabled(false);
        txtComment.setEnabled(false);
        chkDeletable.setEnabled(false);
        btnComment.setEnabled(false);
    }

    public boolean wasOK() {
        return !(wasCancel());
    }
}