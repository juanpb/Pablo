package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflState;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflParallelismAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "CREATE_PARALLELISM";

    public static final int TYPE_CREATE = 0;
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_REMOVE = 2;

    private WflContainerControl control = null;
    private WflState stateInitial = null;
    private int type = -1;


    /**
     *
     * @param control
     * @param type indica si es un alta, baja o modificación.
     *  Valores posibles: TYPE_CREATE, TYPE_EDIT, TYPE_REMOVE
     * @param showText
     */
    public WflParallelismAction(WflContainerControl control, int type,
                                boolean showText) {
        this(control, type, showText, null);
    }

    /**
     *
     * @param control
     * @param type indica si es un alta, baja o modificación.
     *  Valores posibles: TYPE_CREATE, TYPE_EDIT, TYPE_REMOVE
     * @param showText si es true muestra el texto siempre, si es false sólo lo
     * muestra como tooltip.
     * @param comp
     */
    public WflParallelismAction(WflContainerControl control, int type,
                                      boolean showText, WflComponent comp) {
        super(name);
        List p = control.getParallelism(comp);
        WflState si = null;
        if (p != null){
            si = (WflState)p.get(0);
        }
        this.stateInitial = si;
        this.control = control;
        this.type = type;
        init(showText);
    }

    /**
     *
     * @param control
     * @param type indica si es un alta, baja o modificación.
     *  Valores posibles: TYPE_CREATE, TYPE_EDIT, TYPE_REMOVE
     * @param showText si es true muestra el texto siempre, si es false sólo lo
     * muestra como tooltip.
     * @param stateInitial
     */
    public WflParallelismAction(WflContainerControl control, int type,
                                      boolean showText, WflState stateInitial) {
        super(name);
        this.stateInitial = stateInitial;
        this.control = control;
        this.type = type;
        init(showText);
    }

    private void init(boolean showText) {
        String tit = getTitle();
        String name = showText ? tit : null;
        putValue(Action.SMALL_ICON, getIcon());
        putValue(NAME, name);
        if (showText ==false)
            putValue(SHORT_DESCRIPTION, tit);
        KeyStroke ks = getKeyStroke();
        putValue(ACCELERATOR_KEY, ks);
    }

    private Icon getIcon() {
        String name = null;

        if (type == TYPE_CREATE)
            name = WflConstants.ICON_PARALLELISM_CREATE;
        else if (type == TYPE_EDIT)
            name = WflConstants.ICON_PARALLELISM_EDIT;
        else if (type == TYPE_REMOVE)
            name = WflConstants.ICON_PARALLELISM_REMOVE;

        return WflUtil.createImageIcon(name);
    }

    /**
     * Devuelve el KeyStroke dependiendo del tipo de la acción.
     * @return
     */
    private KeyStroke getKeyStroke() {
        if (type == TYPE_CREATE)
            return WflShortcuts.CREATE_PARALLELISM;
        else if (type == TYPE_EDIT)
            return WflShortcuts.EDIT_PARALLELISM;
        else if (type == TYPE_REMOVE)
            return WflShortcuts.REMOVE_PARALLELISM;
        return null;
    }

    /**
     * Devuelve el título dependiendo del tipo de la acción.
     * @return
     */
    private String getTitle() {
        if (TYPE_CREATE == type)
            return "Crear paralelismo";
        else if (TYPE_EDIT == type)
            return "Editar paralelismo";
        else if (TYPE_REMOVE == type)
            return "Quitar paralelismo";
        return null;
    }

    public void setStateInitial(WflState stateInitial) {
        this.stateInitial = stateInitial;
    }

    public void actionPerformed(ActionEvent e) {
        if (TYPE_CREATE == type)
            control.createParallelism(stateInitial);
        else if (TYPE_EDIT == type)
            control.editParallelism(stateInitial);
        else if (TYPE_REMOVE == type)
            control.removeParallelism(stateInitial);
   }
}