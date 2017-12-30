package tleng.gramatica;

import java.io.*;

public class AS {

    public static void main(String[] args){
        String  parser;
        String  gram;
        String  cadena = "";
        GLC     g   = null;
        LRCero  lr  = null;
        SLR     slr = null;
        LRUno   lru = null;


        boolean b;

        if (args.length < 2)
            mensajeError();
        parser  = args[1];
        gram    = args[0];
        if (args.length > 2)
            cadena = args[2];
        else
            System.out.println("tam arg = " + args.length);
        try{
            g = new GLC(new File(gram));
        }catch(Exception e){
            System.out.println("No se encuentra el archivo " + gram);
            e.printStackTrace();
            System.exit(0);
        }
        g.imprimirGramatica();
        if (parser.equals("1")){//LR0
            try{
                lr = new LRCero(g);
            }catch(Exception e){
                System.out.println("La sintáxis de la gramática del archivo " +
                gram + " no es correcta.");
                e.printStackTrace();
                System.exit(0);
            }
            b = lr.tieneConflicto();
            if(b)
                System.out.println("La gramatica tiene conflictos.");
            else
                System.out.println("La gramatica no tiene conflictos.");

            if (!cadena.trim().equals("")){//si se ingresó una cadena, la verifico
                System.out.println("Acciones al parsear la cadena");
                b = lr.pertenece(cadena);
                if (b)
                    System.out.println("La cadena pertenece a la gramatica.");
                else
                    System.out.println("La cadena no pertenece a la gramatica.");
            }
        }
        else if (parser.equals("2")){//SLR
            try{
                slr = new SLR(g);
            }catch(Exception e){
                System.out.println("La sintáxis de la gramática del archivo " +
                gram + " no es correcta.");
                e.printStackTrace();
                System.exit(0);
            }
            b = slr.tieneConflicto();
            if(b)
                System.out.println("La gramatica tiene conflictos.");
            else
                System.out.println("La gramatica no tiene conflictos.");


            if (!cadena.trim().equals("")){//si se ingresó una cadena, la verifico
                System.out.println("Acciones al parsear la cadena");
                b = slr.pertenece(cadena);
                if (b)
                    System.out.println("La cadena pertenece a la gramatica.");
                else
                    System.out.println("La cadena no pertenece a la gramatica.");
            }
        }
        else if (parser.equals("3")){//LR1
            System.out.println("LR1");
            System.out.println("cadena: " + cadena);
            try{
                lru = new LRUno(g);
            }catch(Exception e){
                System.out.println("La sintáxis de la gramática del archivo " +
                gram + " no es correcta.");
                e.printStackTrace();
                System.exit(0);
            }
            b = lru.tieneConflicto();
            if(b)
                System.out.println("La gramatica tiene conflictos.");
            else
                System.out.println("La gramatica no tiene conflictos.");


            if (!cadena.trim().equals("")){//si se ingresó una cadena, la verifico
                System.out.println("Acciones al parsear la cadena");
                b = lru.pertenece(cadena);
                if (b)
                    System.out.println("La cadena pertenece a la gramatica.");
                else
                    System.out.println("La cadena no pertenece a la gramatica.");
            }
        }
    }

    private static void mensajeError(){
        System.out.println("Error en la sintaxis.");
        System.out.println("Para ejecutar: ");
        System.out.println("tleng.gramatica.AS [path y nombre del archivo]" +
                           " [1|2|3] (LR0|SLR|LR1) "   );
        System.out.println("Para saber si una cadena pertenece a la gramatica: " +
                            "ademas de lo anterior agregar la cadena ");
        System.out.println("Ejemblo: ");
        System.out.println("    tleng.gramatica.AS 2 C:\\prueba.txt abcd ");
        System.out.println("    Esto usa el parser SLR para verificar si la "+
        "cadena abcd pertenece a la gramatica del archivo C:\\prueba.txt");
        System.exit(0);

    }

}

