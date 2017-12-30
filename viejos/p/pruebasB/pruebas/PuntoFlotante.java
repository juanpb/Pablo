package p.pruebas;

public class PuntoFlotante {


    public static void main(String[] args) {
            double val1 = 0.1;
            double val2 = 0.3;

            double d = val1 + val1 + val1;

            if (d != val2) {
                System.out.println("d != val2");
            }

            System.out.println(
               "d - val2 = " + (d - val2));
    }
}