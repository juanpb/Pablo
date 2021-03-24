package p.gui;

import p.aplic.peliculas.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * User:
 * Date: 21/11/2019
 * Time: 10:35
 */
public class ComboCheckBox extends JComboBox  {
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public ComboCheckBox() {
        init();
    }

    public ComboCheckBox(JCheckBox[] items) {
        super(items);
        init();
    }

    public ComboCheckBox(Vector items) {
        super();
        init();
    }

    public ComboCheckBox(ComboBoxModel aModel) {
        super(aModel);
        init();
    }
    public void setPopupVisible(boolean v) {
        if(v)
            super.setPopupVisible(v);
    }

    public List<String> getSelected(){
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < super.getItemCount(); i++) {
            if (((JCheckBox)getItemAt(i)).isSelected())
                res.add(((JCheckBox)getItemAt(i)).getText());
        }

      return res;
    }

    private void init() {
        setRenderer(new ComboBoxRenderer());
        super.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                itemSelected(ae);
            }
        });
    }

    //sobreescribo método para primero actualizar los checkbox y luego avisar arriba, xq si no se le avisa arriba antes
    //de que se actualice el estado seleccionado/deseleccionado del check
    public void addActionListener(ActionListener act) {
        listeners.add(act);
    }

    private void itemSelected(ActionEvent ae) {
        if (getSelectedItem() instanceof JCheckBox) {
            JCheckBox jcb = (JCheckBox)getSelectedItem();
            System.out.println("antes jcb.isSelected() = " + jcb.isSelected());
            jcb.setSelected(!jcb.isSelected());
            System.out.println("desp jcb.isSelected() = " + jcb.isSelected());
            avisarListeners(ae);
        }
    }

    private void avisarListeners(ActionEvent ae) {
        for (ActionListener listener : listeners) {
            listener.actionPerformed(ae);
        }
    }

    class ComboBoxRenderer implements ListCellRenderer {
        private JLabel label;

        public ComboBoxRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Component) {
                Component c = (Component)value;
                if (isSelected) {
                    c.setBackground(list.getSelectionBackground());
                    c.setForeground(list.getSelectionForeground());
                } else {
                    c.setBackground(list.getBackground());
                    c.setForeground(list.getForeground());
                }

                return c;
            } else {
                if (label ==null) {
                    if (value == null)
                        value = "null-VER";
                    label = new JLabel(value.toString());
                }
                else {
                    label.setText(value.toString());
                }

                return label;
            }
        }
    }
}
