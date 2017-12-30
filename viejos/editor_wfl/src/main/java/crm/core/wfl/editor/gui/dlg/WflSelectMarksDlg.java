package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.bobj.WflStateMarkBObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 22, 2006
 * Time: 12:02:23 PM
 */
public class WflSelectMarksDlg {
    private static JDialog parent = null;
    private static List<WflStateMarkBObj> selected = null;
    private static WflMarksTable table = new WflMarksTable();
    private static JDialog dlg = null;
    private static List<WflStateMarkBObj> marks = null;

    /**
     * A partir de la lista de WflMarkBObj, permite elegir entre ellos,
     * seleccionando además la acción. Devuelve una lista de
     * WflStateMarkBObj.
     *
     * @param parent
     * @param marks lista de WflMarkBObj
     * @return una lista de WflStateMarkBObj.
     */
    public static List openSelectMarks(JDialog parent, List marks) {
        WflSelectMarksDlg.parent = parent;


        List<WflStateMarkBObj> ms = new ArrayList<WflStateMarkBObj>(marks.size());
        for (int i = 0; i < marks.size(); i++) {
            WflMarkBObj bo = (WflMarkBObj) marks.get(i);
            ms.add(new WflStateMarkBObj(WflStateMarkBObj.ALTA, bo));
        }
        table.setStateMarks(ms);
        WflSelectMarksDlg.marks = ms;

        return  showTable(table);
    }

    private static List showTable(JTable table) {
        dlg = new JDialog(parent);
        dlg.setTitle("Seleccionar Hitos");
        Container cp = dlg.getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridBagLayout());
        JButton btnAccept = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");

        btnAccept.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                accept();
            }
        });
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                cancel();
            }
        });
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (MouseEvent.BUTTON1 == e.getButton()
                        && e.getClickCount() ==2)
                    accept();
            }
        });
        p.add(btnAccept, new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5,5,5,10), 0,0));
        p.add(btnCancel, new GridBagConstraints(1,0,1,1,0,0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(5,5,5,10), 0,0));
        JScrollPane sp = new JScrollPane(table);
        cp.add(sp, BorderLayout.CENTER);
        cp.add(p, BorderLayout.SOUTH);

        dlg.setSize(300, 150);
        Point center = WflUtil.getCenter(dlg.getSize());
        dlg.setLocation(center);
        dlg.setModal(true);
        dlg.setVisible(true);

        return selected;
    }

    private static void accept() {
        selected = new ArrayList<WflStateMarkBObj>();
        int[] sr = table.getSelectedRows();
        if (sr.length < 0)
            return ;

        for (int i = 0; i < sr.length; i++) {
            int i1 = sr[i];
            selected.add(marks.get(i1));
        }
        dlg.setVisible(false);
    }

    private static void cancel() {
        selected = null;
        dlg.setVisible(false);
    }

}
