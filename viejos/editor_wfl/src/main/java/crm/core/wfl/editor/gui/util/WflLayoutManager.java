package crm.core.wfl.editor.gui.util;

import crm.core.wfl.editor.gui.cmp.*;
import crm.core.wfl.editor.bobj.WflStateBObj;

import java.util.*;

/**
 * Esta clase, dado un conjunto de WflComponentes que representan la definición
 * de un proceso, calcula y asigna un 'x' y un 'y' a cada uno de los
 * componentes.
 *
 * User: JPB
 * Date: Mar 13, 2006
 * Time: 3:10:23 PM
 */
public class WflLayoutManager {

    private static Set<WflConnector> notAssigned = new HashSet<WflConnector>();

    private static int x = 10;
//    private static int y = 10;
    private static int originalX = 10;
    private static int originalY = 10;

    /**
     * Distancia Y entre un componente y el de abajo.
     */
    private final static int distanceY = 30;

    /**
     * Distancia X entre un componente y el de la derecha.
     */
    private final static int distanceX = 30;

    /**
     * Calcula y asigna un 'x' y un 'y' a cada uno de los
     * componentes.
     * @param wflComponents lista de WflComponents
     */
    public static void assignLocations(List wflComponents){
        x = originalX;
        int y = originalY;
        for (int i = 0; i < wflComponents.size(); i++) {
            Object o = wflComponents.get(i);
            if (!(o instanceof WflConnector))
                notAssigned.add((WflConnector)o);
        }
        List initials = getInitials();
        if (initials.size() == 0){
            String msg = "No se encontraron estados iniciales (no se calcula el layout)";
            WflUtil.showErrorMessage(msg);
            return ;
        }
        notAssigned.removeAll(initials);
        for (int i = 0; i < initials.size(); i++) {
            WflComponent wc = (WflComponent) initials.get(i);
            wc.setPosition(x, y);
            y += wc.getHeight() + distanceY;
            assignBranch(wc);
            y = originalY;
            x += distanceX;
        }

    }

    /**
     * Asigna location a los componentes a los cuales llega el recibido como
     * parámetro (no a este).
     * @param wc un WflState o un WflFunction
     */
    private static void assignBranch(WflComponent wc) {
        List sons = getSons(wc);
        int localX = wc.getX();
        int y = wc.getY() + wc.getHeight() + distanceY;

        for (int i = 0; i < sons.size(); i++) {
            WflComponent c = (WflComponent) sons.get(i);
            if (notAssigned.contains(c)){
                notAssigned.remove(c);
                c.setPosition(localX, y);
                assignBranch(c);
                localX += distanceX + c.getWidth();
            }
        }
        if (x < localX)
            x = localX;
    }

    /**
     *
     * @param wc
     * @return los hijos del parámetro a los que aún no se le asignaron x e y.
     */
    private static List getSons(WflComponent wc) {
        List res = null;
        if (wc instanceof WflState)
            return getSons((WflState)wc);
        if (wc instanceof WflFunction)
            return getSons((WflFunction)wc);

        return res;
    }

    private static List<WflComponent> getSons(WflState wc){
        List<WflComponent> res = new ArrayList<WflComponent>();
        List<WflComponent> rcs = wc.getRelatedComponents(WflPreFunction.class);
        for (int i = 0; i < rcs.size(); i++) {
            WflPreFunction pf = (WflPreFunction) rcs.get(i);
            List<WflComponent> r = pf.getRelatedComponents(WflFunction.class);
            if (r.size() == 1){
            	WflComponent o = r.get(0);
                if (notAssigned.contains(o))
                    res.add(o);
            }
        }
        return res;
    }
    private static List getSons(WflFunction wc){
        List<WflComponent> res = new ArrayList<WflComponent>();
        List<WflComponent> rcs = wc.getRelatedComponents(WflPosFunction.class);
        for (int i = 0; i < rcs.size(); i++) {
            WflPosFunction pf = (WflPosFunction) rcs.get(i);
            List<WflComponent> r = pf.getRelatedComponents(WflState.class);
            if (r.size() == 1){
            	WflComponent o = r.get(0);
                if (notAssigned.contains(o))
                    res.add(o);
            }
        }
        return res;
    }

    /**
     * @return una lista de WflState que sean iniciales.
     */
    private static List<WflState> getInitials() {
        List<WflState> res = new ArrayList<WflState>();
        Iterator it = notAssigned.iterator();
        while (it.hasNext()) {
            WflComponent wc = (WflComponent) it.next();
            if (wc instanceof WflState){
                WflState s = (WflState)wc;
                String pos = ((WflStateBObj) s.getBObj()).getPosition();
                if (WflStateBObj.POSITION_INITIAL.equals(pos))
                    res.add(s);
            }
        }

        return res;
    }
}
