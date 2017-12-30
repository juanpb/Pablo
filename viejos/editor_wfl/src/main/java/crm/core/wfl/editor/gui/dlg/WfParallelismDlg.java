package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WfParallelismDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private WflStateBObj stateInitial = null;
    private WflStateBObj stateFinal = null;

    private JPanel panel = new JPanel(null);

    private JLabel lblInitial = new JLabel ("Estado inicial");
    private JTextField txtInitial = new JTextField ();

    private JLabel lblFinal = new JLabel ("Estado final");
    private JTextField txtFinal = new JTextField ();

    private JLabel lblClassName = new JLabel ("Clase");
    private JTextField txtClassName = new JTextField ();

    Icon ic = WflUtil.createImageIcon(WflConstants.ICON_EXPLORER);
    private JButton btnClassName = new JButton (ic);

    private JLabel lblComment = new JLabel ("Comentario");
    private JTextArea txtComment = new JTextArea ();
    private JScrollPane spComment = new JScrollPane(txtComment);

    Icon ic2 = WflUtil.createImageIcon(WflConstants.ICON_EXPAND);
    private JButton btnComment = new JButton(ic2);

    private String className = null;
    private String comment = null;

    private boolean isAdd = false;
    private boolean isRemove = false;
    private boolean isEdit = false;
    private boolean wasCancel = false;
    private boolean wasOK = true;

    public WfParallelismDlg(WflStateBObj stateInitial, WflStateBObj stateFinal) {
        super();
        this.stateInitial = stateInitial;
        this.stateFinal = stateFinal;
        init();
    }

    private void init(){
        addComponents();
        setBounds();
        setProperties();
        super.setPanel(panel);
        updateDlg();
    }

    public boolean wasModified() {
        return  !(getClassName().equals(className)
                && ((getComment() == null && comment == null)
                    || (getComment() != null && getComment().equals(comment))));
    }

    public String getComment() {
        String res = txtComment.getText();
        if (res.trim().equals(""))
            res = null;

        return res;
    }

    private void setProperties() {
        txtFinal.setEditable(false);
        txtInitial.setEditable(false);
        btnClassName.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFinderClass();
            }
        });

        btnComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComment();
            }
        });
    }


    private void showComment() {
        String c = getComment();

        WflCommentDlg dlg = new WflCommentDlg(c, isRemove);
        dlg.showMe();
        if (dlg.isOK() && dlg.wasModified())
            txtComment.setText(dlg.getComment());
    }

    private void openFinderClass() {
        String s = WflSelectClassDlg.openFinderClass(this);
        if (s != null)
            txtClassName.setText(s);
    }

    private void addComponents() {
        panel.add(lblInitial);
        panel.add(txtInitial);

        panel.add(lblFinal);
        panel.add(txtFinal);

        panel.add(lblClassName);
        panel.add(txtClassName);
        panel.add(btnClassName);

        panel.add(lblComment);
	    panel.add(spComment);
        panel.add(btnComment);
    }

    private void setBounds() {
        int sep = 10;
        int x1 = 10;
        int y = 10;
        int w = 135;
        int w2 = w * 2 + sep;
        int x2 = x1 + w + sep;
        int h = 20;

        lblInitial.setBounds(x1, y, w, h);
        lblFinal.setBounds(x2, y, w, h);
        y += h;
        txtInitial.setBounds(x1, y, w, h);
        txtFinal.setBounds(x2, y, w, h);
        y += h + sep;

        lblClassName.setBounds(x1, y, w2, h);
        y += h;
        txtClassName.setBounds(x1, y, w2-20, h);
        btnClassName.setBounds(x1 + w2-20, y, 20, h);
        y += h + sep;

        lblComment.setBounds(x1, y, w2, h);
        y += h;
        spComment.setBounds(x1, y, w2 - 32, h * 2);
        btnComment.setBounds(x1 + w2 - 30, y, 30, h);

        y += h*2 + sep ;

        Dimension dim = new Dimension(x2 + w + sep, y + sep);
        panel.setSize(dim);
        panel.setPreferredSize(dim);
    }


    /**
     * Llena los campos del panel
     */
    public void updateDlg() {
        String id = stateInitial.getId();
        if (id == null || id.equals(""))
            id = "Estado sin id";
        txtInitial.setText(id);

        if (stateFinal != null)
            id = stateFinal.getId();
        if (id == null || id.equals(""))
            id = "Estado sin id";
        txtFinal.setText(id);
        txtComment.setText(comment);
    }


    protected void updateBObj() {
        //de la actualizacion de objetos se ocupa el que invoca al dgl
    }

    protected void cancel() {
        super.cancel();
        wasCancel = true;
        wasOK = false;
    }

    /**
     * Valida que estén cargados los datos necesarios.
     * @return true si se está bien cargado.
     */
    public boolean isOK() {
        if (wasCancel)
            wasOK = false;
        else if (isRemove){
//            String msg = "¿Está seguro que desea eliminar el paralelismo?";
//            int res = WflUtil.showConfirmDialog(msg, "Borrar paralelismo");
//            wasOK = (JOptionPane.OK_OPTION == res);
        }
        else if (isAdd || isEdit){
            String cn = getClassName();
            wasOK = cn != null && !cn.trim().equals("");
            if (!wasOK){
                String msg = "Falta agregar el nombre de la clase";
                WflUtil.showErrorMessage(msg, "Paralelismo");
            }
        }
        return wasOK;
    }

    /**
     * Devuelve el valor de la última vez que se llamó a isOk(). No valida de
     * nuevo.
     *
     * @return  el valor de la última vez que se llamó a isOk()
     */
    public boolean wasOK(){
        return wasOK;
    }

    public String getClassName() {
        return txtClassName.getText();
    }

    public void setClassName(String className) {
        this.className = className;
        txtClassName.setText(className);
    }

    public void setComment(String comment) {
        this.comment = comment;
        txtComment.setText(comment);
    }

    public void showAdd() {
        super.setTitle("Agregar paralelismo dinámico");
        isAdd = true;
        isRemove = false;
        isEdit = false;
        super.showMe();
    }
    public void showEdit() {
        super.setTitle("Editar paralelismo dinámico");
        isAdd = false;
        isRemove = false;
        isEdit = true;
        super.showMe();
    }
    public void showRemove() {
        super.setTitle("Quitar paralelismo dinámico");
        txtClassName.setEditable(false);
        btnClassName.setEnabled(false);
        txtComment.setEditable(false);
        isAdd = false;
        isRemove = true;
        isEdit = false;
        super.showMe();
    }
}
