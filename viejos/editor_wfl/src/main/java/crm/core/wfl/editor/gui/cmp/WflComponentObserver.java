package crm.core.wfl.editor.gui.cmp;

import crm.core.wfl.editor.gui.cmp.WflComponent;

/**
 * Implementado por aquellos que observan un componente.
 * @author Cm
 */
public interface WflComponentObserver {

    /**
     * Llamada cuando el componente tuvo un cambio.
     * @param o Componente que tuvo el cambio.
     */
    void update(WflComponent o);
}
