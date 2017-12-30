package tleng.gramatica;


public class Produccion{

    private String _izq = "";
    private String _der = "";
    private int    _num;    //es el número que tiene asociada la produccion

    public static final String SEPARADOR = ":";
    public static final String FIN_PRODUCCION = ";";


/*
    public Produccion(String str) throws Exception{
        try {
            int d,h;
            str = str.trim();
            h = str.indexOf(SEPARADOR);
            d = str.indexOf(FIN_PRODUCCION);
            _izq = str.substring(0, h);
            _der = str.substring(h + 1 , d);
        }
        catch (Exception ex) {
            throw new Exception("La producción es inválida.");
        }
        System.out.println("PRODUCCION: "  );
    }*/

    //asuma que cada símbolo es de un caracter
    public int tamanoParteDerecha(){
        return _der.trim().length();
    }

    public Produccion(String str, int numProduccion) throws Exception{

        try {
            int d,h;
            str = str.trim();
            h = str.indexOf(SEPARADOR);
            if (h < 0)
                throw new Exception("La producción es inválida.");
            d = str.indexOf(FIN_PRODUCCION);
            if (d < 0)
                throw new Exception("La producción es inválida.");
            _izq = str.substring(0, h);
            _der = str.substring(h + 1 , d);
            _num = numProduccion;
        }
        catch (Exception ex) {
            throw new Exception("La producción es inválida.");
        }
    }

    //no se fija el número de producción
    public boolean equals(Produccion p){

        return _der.equals(p.getDer()) && _izq.equals(p.getIzq());
    }
    public Object clone(){
        Produccion p = null;
        try{
            p = new Produccion(_izq + SEPARADOR + _der + FIN_PRODUCCION,
                                _num);
        }catch(Exception e){
            e.printStackTrace();
        }
        return p;
    }

    public int getNumeroDeProduccion(){
        return _num;
    }
    public String getIzq(){
        return _izq.trim();
    }

    public String getDer(){
        return _der.trim();
    }
    public void setDer(String der){
        _der = der;
    }
    public String toString(){
        return _izq + " --> " + _der + FIN_PRODUCCION;
    }
}
