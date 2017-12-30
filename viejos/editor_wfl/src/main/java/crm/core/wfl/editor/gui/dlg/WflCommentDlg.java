package crm.core.wfl.editor.gui.dlg;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:52:42 PM
 */
public class WflCommentDlg extends WflDlg{
	private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel(new BorderLayout());


    private JTextArea txtComment = new JTextArea ();
    private JScrollPane sp = new JScrollPane (txtComment);

    private String comment = null;

    public WflCommentDlg() {
        this(null);
    }
    public WflCommentDlg(String comment) {
        this(comment, false);
    }

    public WflCommentDlg(String comment, boolean onlyRead) {
        super();
        init();
        this.comment = comment;
        if (comment != null)
            txtComment.setText(comment);
        txtComment.setEditable(!onlyRead);
    }

    private void init(){
        setBounds();
        addComponents();
        setProperties();
        super.setPanel(panel);
        super.setTitle("Comentario");
        updateDlg();
        super.setIgnoreEnter(true);
        this.setResizable(true);        
    }

    private void setProperties() {
        txtComment.setWrapStyleWord(true);
        txtComment.setLineWrap(true);
    }

    private void addComponents() {
    	panel.add(sp, BorderLayout.CENTER);        
    }

    private void setBounds() {
//        int x1 = 10;
//        int sep = 10;
//        int y = 10;
//        int w = 400;
//        int h = 90;
//
//        y += 20;
//        sp.setBounds(x1, y, w, h);
//        y += h + sep;
//        Dimension dim = new Dimension(x1 + w + sep, y + sep);
//        panel.setSize(dim);
//        panel.setPreferredSize(dim);
        
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
        return !(((getComment() == null && comment == null)
                || (getComment() != null && getComment().equals(comment))));
    }

    public String getComment() {
        String res = txtComment.getText();
        if (res.trim().equals(""))
            res = null;
        return res;
    }

    protected void updateBObj() {

    }

    public boolean isOK() {
        return !wasCancel;
    }

}
