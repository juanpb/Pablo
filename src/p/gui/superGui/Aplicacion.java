package p.gui.superGui;

import java.util.List;

/**
 * User: JPB
 * Date: 07/05/13
 * Time: 11:50
 */
public interface Aplicacion {
    String getName();
    List<Param> getParams();

    /**
     *
     * @param params
     * @return mensaje con resultado de la ejecución
     */
    String ejecutar(List<String> params);
}
