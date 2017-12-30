package p.proc.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: 22/06/15
 * Time: 11:35
 */
public class AsigSevHs {

    private String asignadoA;
    private List<String> hs = new ArrayList<String>();

    AsigSevHs(String linea) {
        parsear(linea);
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public String getHs(int indice){
        return hs.get(indice);
    }

    public void parsear(String linea){
        StringTokenizer st = new StringTokenizer(linea, ",", false);
        asignadoA = st.nextToken().trim();
        while (st.hasMoreTokens()) {
            hs.add(st.nextToken().trim());
        }
    }

    public String toString() {
        return "AsigSevHs{" +
                "asignadoA='" + asignadoA + '\'' +
                ", hs=" + hs +
                '}';
    }
}
