package p.pruebas.subclases;

/**
 * User: JPB
 * Date: 06/09/13
 * Time: 15:07
 */
public class Hijo extends Padre {
    public static void main(String[] args) {

        Padre p = new Hijo();
        p.m();

        Hijo h = new Hijo();
        h.m();
    }

    static public void m(){
        System.out.println("Hijo.m");
    }
}
