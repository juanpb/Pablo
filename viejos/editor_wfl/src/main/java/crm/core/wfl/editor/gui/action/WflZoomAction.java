package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflZoomAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "ZOOM";
    private static final String title = "Zoom";

    private double lastValue = 100;
    private WflContainerControl control = null;
    private JComboBox combo;


    public WflZoomAction(WflContainerControl control, JComboBox combo,
                         boolean showText) {
        super(WflZoomAction.name);
        this.control = control;
        this.combo = combo;
        String name = showText ? title : null;
        putValue(NAME, name);
        if (!showText)
            putValue(SHORT_DESCRIPTION, WflZoomAction.title);
    }

    public void actionPerformed(ActionEvent e) {
        if (!"comboBoxChanged".equals(e.getActionCommand()))
            return ;

        Object si = combo.getSelectedItem();
        double d, v;
        try {
            String s = si.toString();
            s = s.replaceAll("%", "");
            v = Double.parseDouble(s);
            if (v == lastValue)
                return ;
            if (v <= 0 ){
                String msg = "El valor tiene que ser mayor que cero";
                WflUtil.showMessage(msg, "Zoom");
                return ;
            }
            lastValue = v;
            d = v/100;
        }
        catch (Exception ex) {
            return ;
        }
        finally{
            String x = value2String(lastValue);
            combo.setSelectedItem(x);
        }
        control.setZoom(d);
    }

    /**
     * @param v
     * @return un String con decimales sólo si son distintos de cero. Incluye %
     * al final del valor.
     */
    private String value2String(double v){
        String res = Double.toString(v);
        while (res.endsWith("0")) {
            res = res.substring(0, res.length() - 1);
        }
        if (res.endsWith("."))
            res = res.substring(0, res.length() - 1);

        return res+"%";
    }
}
