package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:56:11 PM
 */
public abstract class WflDlg extends JDialog implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
    private JPanel panel = null;
    protected JPanel botonera = new JPanel(new GridBagLayout());
    private ImageIcon iconAc = WflUtil.createImageIcon(WflConstants.ICON_ACCEPT);
    private ImageIcon iconCa = WflUtil.createImageIcon(WflConstants.ICON_CANCEL);
    private JButton btnAccept = new JButton("Aceptar", iconAc);
    private JButton btnCancel = new JButton("Cancelar", iconCa);
    private WflBObj bo;
    protected boolean wasCancel = false;

    private boolean ignoreEnter = false;

    protected WflDlg(){
        init();
    }

    protected WflDlg(Dialog owner){
    	super(owner);
        init();
    }

    private void init(){
        setModal(true);
        doButtonBar();
        addListeners();
    }

    private void doButtonBar() {
        int w = 110;
        int h = 20;
        Dimension dim = new Dimension(w, h);
        btnAccept.setPreferredSize(dim);
        btnCancel.setPreferredSize(dim);

        botonera.add(btnAccept, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0));
        botonera.add(btnCancel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0));
    }

    private void addListeners() {
        btnAccept.addActionListener(this);
        btnCancel.addActionListener(this);
    }

    protected void setPanel(JPanel panel){
        this.panel = panel;
    }

    abstract public boolean wasModified();

    public void showMe() {

        //Agrega Listeners para poder cerrar el diálogo cuando se aprieta
        //scape o enter
        Component[] cmps = super.getComponents();

        for (int i = 0; i < cmps.length; i++) {
            Component cmp = cmps[i];
            cmp.addKeyListener(this);
        }
        cmps = panel.getComponents();

        for (int i = 0; i < cmps.length; i++) {
            Component cmp = cmps[i];
            cmp.addKeyListener(this);
            if (cmp instanceof JScrollPane){
                JScrollPane x = (JScrollPane)cmp;
                Component[] cs = x.getViewport().getComponents();
                for (int j = 0; j < cs.length; j++) {
                    cmp = cs[j];
                    cmp.addKeyListener(this);
                }
            }
            else if (cmp instanceof JTabbedPane){
                JTabbedPane x = (JTabbedPane)cmp;
                Component[] cs = x.getComponents();
                for (int j = 0; j < cs.length; j++) {
                    cmp = cs[j];
                    cmp.addKeyListener(this);
                    if (cmp instanceof JPanel){
                        JPanel p = (JPanel)cmp;
                        Component[] c2 = p.getComponents();
                        for (int k = 0; k < c2.length; k++) {
                            cmp = c2[k];
                            cmp.addKeyListener(this);
                        }
                    }
                }
            }
        }

        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(botonera, BorderLayout.SOUTH);
        super.pack();

        Dimension dim = getSize();
        Point p = WflUtil.getCenter(dim);
        setLocation(p);

        super.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel)
            cancel();
        else if (e.getSource() == btnAccept)
            accept();
    }

    protected void accept()
    {
        if (isOK()){
            updateBObj();
            setVisible(false);
        }
    }

    /**
     * Método que se puede sobreescribir para validar si se cargaron los todos
     * los datos requeridos por el diálogo.
     *
     * @return true si se cargaron los todos
     * los datos requeridos por el diálogo.
     */
    protected boolean isOK() {
        return true;
    }

    public void setBObj(WflBObj bo)
    {
        this.bo = bo;
        updateDlg();
    }
    
    public WflBObj getBObj()
    {
        return this.bo;
    }
    
    abstract protected void updateBObj();
    abstract protected void updateDlg();

    protected void cancel(){
        wasCancel = true;
        setVisible(false);
    }

    public boolean wasCancel(){
        return wasCancel;
    }

    public void keyTyped(KeyEvent e) {
        int k = e.getKeyChar();
        if(k == KeyEvent.VK_ESCAPE)
            cancel();
        else if(k == KeyEvent.VK_ENTER){
            if (ignoreEnter)
                return ;
            accept();
            e.consume();
        }
    }

    public boolean isIgnoreEnter() {
        return ignoreEnter;
    }

    public void setIgnoreEnter(boolean ignoreEnter) {
        this.ignoreEnter = ignoreEnter;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
