package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.gui.action.WflAddMarkAction;
import crm.core.wfl.editor.gui.action.WflCreateMarkAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * User: JPB
 * Date: Feb 10, 2006
 * Time: 10:52:15 AM
 */
public class WflMarksBar extends JToolBar implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private WflProcessDefinitionContainer container;
    private List<WflMarkBObj> marks = new ArrayList<WflMarkBObj>();
    private List<WflAddMarkAction> actions = new ArrayList<WflAddMarkAction>();
    private List<JButton> buttons = new ArrayList<JButton>();
    private int orientation = JToolBar.VERTICAL;

    public WflMarksBar(WflProcessDefinitionContainer container) {
        this.container = container;
        init();
    }

    private void init() {
        super.setOrientation(VERTICAL);
        super.setName("Hitos");
        super.addPropertyChangeListener("orientation", this);
        addButtons();
    }

    private void addButtons() {
        WflContainerControl control = getControl();
        super.setLayout(new GridBagLayout());
        JButton btn = new JButton(new WflCreateMarkAction(control, false));
        buttons.add(btn);
        updateLayout();
    }

    private void updateLayout() {
        super.removeAll();

        int anchor = 0;
        int fill = 0;
        Insets insets = null;
        int weighty = 0;
        int weightx = 0;
        if (orientation == JToolBar.VERTICAL){
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
            insets = new Insets(0, 2, 1, 2);
        }
        else{
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.NONE;
            insets = new Insets(2, 0, 2, 1);
        }

        int size = buttons.size();
        for (int i = 0; i < size; i++) {
            int x = 0;
            int y = 0;
            if (orientation == JToolBar.VERTICAL)
                y = i;
            else
                x = i;

            JButton b = (JButton) buttons.get(i);

            if (i == size-1)
                if (orientation == JToolBar.VERTICAL)
                    weighty = 1;
                else
                    weightx = 1;
            Insets ins = insets;
            if (i == 0)
                if (orientation == JToolBar.VERTICAL)
                    ins = new Insets(0, 2, 10, 2);
                else
                    ins = new Insets(2, 0, 2, 10);

            GridBagConstraints c = new GridBagConstraints(x, y, 1,1,
                    weightx, weighty, anchor, fill, ins, 0,0);
            super.add(b, c);
        }
    }

    private void addButton(WflAddMarkAction action) {
        final JButton btn = new JButton(action);
        final WflMarkBObj bo = ((WflAddMarkAction)action).getBObj();
        final WflMarksBar bar = this;
        btn.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3){
                    WflMarkMnu mnu = new WflMarkMnu(getControl(), bo);
                    mnu.show(bar, btn.getX(), btn.getY());
                }
            }
        });
        buttons.add(btn);
        updateLayout();
    }

    private WflContainerControl getControl() {
        return container.getControl();
    }

    public void addMark(WflMarkBObj bo) {
        marks.add(bo);
        WflContainerControl control = container.getControl();
        WflAddMarkAction action = new WflAddMarkAction(control, bo,
                true, false);
        actions.add(action);
        addButton(action);
        revalidate();
    }

    /**
     *
     * @return lista de WflMarkBObj
     */
    public List<WflMarkBObj> getMarks() {
        return marks;
    }

    /**
     * Se debe llamar cuando se modifican los atributos de los hitos.
     */
    public void update() {
        for (int i = 0; i < actions.size(); i++) {
            WflAddMarkAction act = (WflAddMarkAction) actions.get(i);
            act.update();
        }

        Component[] cs = super.getComponents();
        for (int i = 0; i < cs.length; i++) {
            Component c = cs[i];
            if (c instanceof JButton){
                JButton b = (JButton)c;
                b.revalidate();
            }
        }
    }

    public void remove(WflMarkBObj mark) {
        marks.remove(mark);
        String id = mark.getId();

        //Arranco desde 1 porque el primero es el de WflCreateMarkAction
        for (int i = 1; i < buttons.size(); i++) {
            JButton b = (JButton) buttons.get(i);
            WflAddMarkAction a = (WflAddMarkAction)b.getAction();
            WflAddMarkAction act = (WflAddMarkAction) a;
            WflMarkBObj bObj = act.getBObj();
            if (id.equals(bObj.getId())){
                actions.remove(act);
                buttons.remove(b);
                break;
            }

        }
        updateLayout();
        super.updateUI();
    }

    public void clean(){
        while (marks.size() > 0) {
            remove((WflMarkBObj)marks.get(0));
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        orientation = ((Integer)e.getNewValue()).intValue();
        updateLayout();
    }
}
