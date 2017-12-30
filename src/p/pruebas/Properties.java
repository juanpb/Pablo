package p.pruebas;


/**
 * User: JPB
 * Date: 2/28/11
 * Time: 2:13 PM
 */
public class Properties {
    public static void main(String[] args) {
        java.util.Properties props = System.getProperties();
        for(Object o : props.keySet()) {
            System.out.println(o + " = " + props.get(o));
        }
    }
}
