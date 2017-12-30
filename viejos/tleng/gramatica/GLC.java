package tleng.gramatica;

import tleng.util.*;

import java.io.*;
import java.util.*;

public class GLC{

    public static final String SPRIMA = "S'";
    public static final String PESOS  = "$";

    public Produccion _prodInicial;

    private StringBuffer   _buffer;
    private String         _cadena;
    private BufferedReader _br;

    private Vector _terminales;
    private Vector _noTerminales;
    private String _inicial;
    private Vector _producciones;
    private Vector _anulables = null;

    public GLC(String gramatica) throws Exception{
        _cadena = gramatica.trim();
        parsear();
    }

    public GLC(File file) throws Exception{

        try {
            FileInputStream fis = new FileInputStream(file);
            _br = new BufferedReader(new InputStreamReader(fis));
            _buffer = new StringBuffer();
            while(_br.ready()){
                _buffer.append(_br.readLine());
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        _cadena = _buffer.toString().trim();
        parsear();
    }

    public void imprimirGramatica(){
        System.out.println("Simbolos no terminales:");
        Vector noT = getNoTerminales();
        for (int i = 0 ; i < noT.size(); i++ ){
            System.out.println("    " + (String) noT.get(i));
        }

        System.out.println("Simbolos terminales:");
        Vector t = getTerminales();
        for (int i = 0 ; i < t.size(); i++ ){
            System.out.println("    " + (String) t.get(i));
        }

        System.out.println("Simbolo distinguido:");
        System.out.println("    " + getInicial());

        System.out.println("Producciones:");
        Vector prod = getProducciones();
        for (int i = 0 ; i < prod.size(); i++ ){
            Produccion p = (Produccion)prod.get(i);
            System.out.println("    " + p.toString() + " ( " + p.getNumeroDeProduccion() + " )");
        }

    }

    public Vector getTerminales(){
        return _terminales;
    }

    public Vector getNoTerminales(){
        return _noTerminales;
    }

    public String getInicial(){
        return _inicial;
    }

    public Vector getProducciones(){
        return _producciones;
    }

    /**
     * Devuelve copia
     */
    public Vector getProducciones(String parteIzquierda){
        Vector res = new Vector();
        Produccion p;
        if (esTerminal(parteIzquierda))
            return res;
        for (int i = 0 ; i < getProducciones().size(); i++){
            p = (Produccion) getProducciones().get(i);
            if (p.getIzq().equals(parteIzquierda)){
                res.add(p.clone());
            }
        }
        return res;
    }

    public Produccion getProduccion(int numProduccion){
        Vector res = new Vector();
        Produccion p;

        for (int i = 0 ; i < getProducciones().size(); i++){
            p = (Produccion) getProducciones().get(i);
            if (p.getNumeroDeProduccion()== numProduccion)
                return p;
        }
        return null;
    }

    public boolean esAnulable(String noTerminal){
        if (_anulables == null)
            calcularAnulables();
        return _anulables.contains(noTerminal);
    }


    /**
     * Un no terminal es anulables si todos los símbolos que están a la
     * derecha de él son anulables.
     */
    private void calcularAnulables(){
        String      der;
        String      izq;
        String      aux;
        boolean     sigo    = true;
        boolean     anul    = false;
        Produccion  p;

        _anulables = new Vector();
        _anulables.add("");//primer parte derecha de un anulable

        while (sigo){
            sigo = false;

            for (int i = 0 ; i < _producciones.size() ; i++){
                p   = (Produccion) _producciones.get(i);

                der = p.getDer();
                izq = p.getIzq();
                if (! _anulables.contains(izq)){
                    anul = true;
                    for (int j = 0; j < der.length(); j++){
                        aux = der.substring(j , j + 1);
                        if (!_anulables.contains(aux))
                            anul = false;
                    }
                    if (anul){
                        sigo = true;
                        _anulables.add(izq);
                    }
                }
            }
        }
    }

    //en pila2 están las partes izq
    private boolean esAnulableP(Stack pila2, String sim){
        if (!esNoTerminal(sim))
            return false; //throws new Exception

        if (pila2.contains(sim))
            return false;

        Vector v = getProducciones(sim);
        Stack pila = new Stack();
        pila.addAll(v);

        Produccion  p;
        String      der;
        String      prox;
        String      izq;
        boolean     res;

        while (!pila.empty()){//es anulable si alguno de los elememntos de la pila es anulable
            p = (Produccion) pila.pop();
            der = p.getDer().trim();
            izq = p.getIzq().trim();

            res = true; //es anulable si toda la parte derecha es anulable
            for (int i = 0 ; i < der.length(); i++){ //si hay ciclo, esa parte no es anulable
                String s = der.substring(i, i + 1);
                if (pila2.contains(s))
                    res = false;
                else{
                    pila2.push(izq);
                    boolean b = esAnulableP(pila2, s);
                    res = res && b;
                }
            }
            if (res)
                return true;

/*
            if (der.equals(""))
                return true;
            prox = der.substring(0, 1);
            if (esNoTerminal(prox))
                pila.addAll(getProducciones(prox));*/
        }
        return false;
    }

    public Vector primeros(String sim){
        Vector  res     = new Vector();
        Stack   pila    = new Stack();
        Vector  v       = null;
        Vector  v1      = null;
        String  prim    = "";
        String  der     = "";
        Produccion p    = null;
        Vector  yaVistas = new Vector();


        if (esTerminal(sim)){
            res.add(sim);
            return res;
        }

        v = getProducciones(sim);
        pila.addAll(v);
        for (int i = 0 ; i < v.size() ; i++){
            p = (Produccion)v.get(i);
            int j = p.getNumeroDeProduccion();
            yaVistas.add(new Integer(j));
        }

        while (! pila.empty()){
            p = (Produccion) pila.pop();
            der = p.getDer();
            prim = (p.tamanoParteDerecha() == 0) ? "" : der.substring(0,1);
            if (esTerminal(prim)){
                if (!res.contains(prim))
                    res.add(prim);
            }
            else{
                v1 = getProducciones(prim);
                if (v1.size() > 0){
                    for (int y = 0 ; y < v1.size() ; y++){
                        p = (Produccion)v1.get(y);
                        int j = p.getNumeroDeProduccion();
                        Integer zz = new Integer(j);
                        if (! yaVistas.contains(zz)){
                            yaVistas.add(zz);
                            pila.add(p);
                        }
                    }
                }

                //mientras haya anulables agrego los primeros de los que sigen a los anulables
                boolean sigo = true;
                int in = 0;
                while (sigo){
                    int tam = der.length() - 1;
                    if ((!esAnulable(prim)) || !(in < tam) )
                        break;
                    prim = der.substring(in + 1, in + 2);
                    if (esTerminal(prim) && !res.contains(prim))
                        res.add(prim);
                    else{
                        v1 = getProducciones(prim);
                        if (v1.size() > 0){
                           for (int y = 0 ; y < v1.size() ; y++){
                                p = (Produccion)v1.get(y);
                                int j = p.getNumeroDeProduccion();
                                Integer zz = new Integer(j);
                                if (! yaVistas.contains(zz)){
                                    yaVistas.add(zz);
                                    pila.add(p);
                                }
                            }
                        }
                    }
                    in++;
                }
            }
        }
        return res;
    }

    public Vector siguientes(String sim){
        Vector      v       = new Vector();
        Stack       pila    = new Stack();
        Produccion  p       = null;
        String      der     = "";
        int         pos     = -1;
        Vector      res     = new Vector();
        String      aux;
        Vector      yaVistas = new Vector();

        //si 'sim' es S => pongo $ como siguiente
        if (sim.equals(getInicial()))
            res.add(PESOS);

        if (sim.equals(SPRIMA))
            return res;

        v = getProducciones(getInicial());
        pila.addAll(v);
        for (int i = 0 ; i < v.size() ; i++){
            p = (Produccion)v.get(i);
            int j = p.getNumeroDeProduccion();
            yaVistas.add(new Integer(j));
        }

        while (! pila.empty()){
            p = (Produccion)pila.pop();
            der = p.getDer();
            pos = 0;
            pos = der.indexOf(sim, pos);
            while (pos >= 0 && der.length() > pos + 1){
            // si existe el símbolo en la producción y
            // hay un símbolo a su derecha
                aux = der.substring(pos + 1 , pos + 2);
                v = primeros(aux);
                for (int u = 0 ; u < v.size() ; u++){
                    String g = (String) v.get(u);
                    if (!res.contains(g))
                        res.add(g);
                }

                //S->'sim'ABC y A es anulable => busco primeros(B)
                //y si además B es anulable =>  busco primeros(C)  etc
                int e = pos + 2;
                while (esAnulable(aux) && der.length() > e){
                    aux = der.substring(e , e + 1);
                    e++;
                    v = primeros(aux);
                    for (int u = 0 ; u < v.size() ; u++){
                        String g = (String) v.get(u);
                        if (!res.contains(g))
                            res.add(g);
                    }
                }
                pos = der.indexOf(sim, pos + 1 );
            }

            //S->ABCD y 'sim' pertenece a los ultimos(B) => agrego los primeros de
            //C. Y si C en anulable => agrego los primeros de D, etc
            for (int j = 0 ; j < der.length() - 1; j++){
                String z  = der.substring(j, j + 1);
                Vector q = ultimos(z);
                if (q.contains(sim)){
                    String y = der.substring(j + 1, j + 2 );
                    q = primeros(y);
                    for (int t = 0 ; t < q.size() ; t++){
                        String w = (String)q.get(t);
                        if (!res.contains(w))
                            res.add(w);
                    }
                    int k = j + 1;
                    while (esAnulable(y) && der.length() > k ){
                        y = der.substring(k, k + 1);
                        q = primeros(y);
                        for (int t = 0 ; t < q.size() ; t++){
                            String w = (String)q.get(t);
                            if (!res.contains(w))
                                res.add(w);
                        }
                        k++;
                    }
                }

            }


            //Busco los siguientes de 'sim' en todas las produciones generedas
            //con los no terminales que están en 'der' (y que no fueron
            //controladas, o sea, las que no están en yaVistas )
            for (int i = 0 ; i < der.length() ; i++){
                aux = der.substring(i, i + 1 );
                if (esNoTerminal(aux)){
                    v = getProducciones(aux);
                    for (int y = 0 ; y < v.size() ; y++){
                        p = (Produccion)v.get(y);
                        int j = p.getNumeroDeProduccion();
                        Integer zz = new Integer(j);
                        if (! yaVistas.contains(zz)){
                            yaVistas.add(zz);
                            pila.add(p);
                        }
                    }
                }
            }

        }
        return res;
    }

    /**
     * Devuelve los terminales y los no terminales que también son últimos
     */
    public Vector ultimos (String sim){
        Produccion  p ;
        Vector      res     = new Vector();
        Vector      v;
        Vector      yaVistas = new Vector();
        Stack       pila    = new Stack();
        String      ult;
        String      der;

        if (esTerminal(sim)){
            res.add(sim);
            return res;
        }

        //últimos(X) con X-> A|B|C son los últimos de A U ult(B) U ult(C) y si
        //B->XY y Y es anulable => hay que agregar los últimos de X
        //y si X es anulable....
        //También devuelvo A, B y C como últimos de X
        v = getProducciones(sim);
        pila.addAll(v);
        for (int i = 0 ; i < v.size() ; i++){
            p = (Produccion)v.get(i);
            int j = p.getNumeroDeProduccion();
            yaVistas.add(new Integer(j));
        }

        while (! pila.empty()){
            p = (Produccion)pila.pop();
            der = p.getDer();
            ult = ultimo(der);
            der = quitarUltimo(der);

            if (esTerminal(ult) && !res.contains(ult))
                res.add(ult);
            else
                if (esNoTerminal(ult)){
                    if (!res.contains(ult))
                        res.add(ult);
                    Vector o = getProducciones(ult);
                    for (int y = 0 ; y < o.size() ; y++){
                        p = (Produccion)o.get(y);
                        int j = p.getNumeroDeProduccion();
                        Integer zz = new Integer(j);
                        if (! yaVistas.contains(zz)){
                            yaVistas.add(zz);
                            pila.add(p);
                        }
                    }
                    //si es anulable, busco los últimos de los que están a la izquierda
                    int h = 1;
                    while (esAnulable(ult) && der.length() > 1){
                        ult = ultimo (der);
                        der = quitarUltimo(der);
                        if (esTerminal(ult) && !res.contains(ult))
                            res.add(ult);
                        else
                            if (esNoTerminal(ult)){
                                Vector r = getProducciones(ult);
                                for (int y = 0 ; y < r.size() ; y++){
                                    p = (Produccion)r.get(y);
                                    int j = p.getNumeroDeProduccion();
                                    Integer zz = new Integer(j);
                                    if (! yaVistas.contains(zz)){
                                        yaVistas.add(zz);
                                        pila.add(p);
                                    }
                                }
                            }
                    }
                }
         }
         return res;
    }

    private String ultimo(String x){
        x = x.trim();
        int i = x.length();
        if (i < 1)
            return "";

        return x.substring(i - 1);
    }

    private String quitarUltimo(String x){
        x = x.trim();
        int i = x.length();
        if (i < 1)
            return "";

        return x.substring(0, i - 1);
    }

    public boolean esTerminal(String x){
        if (x == null || x.trim().equals(""))
            return false;
        return _terminales.contains(x);
    }

    public boolean esNoTerminal(String x){
        if (x == null || x.trim().equals(""))
            return false;
        for (int i = 0 ; i < _noTerminales.size() ; i++){
            String xx = (String)_noTerminales.get(i);
            if (xx.equals(x))
                return true;
        }
        return false;
    }

    private void parsear() throws Exception{
        terminales();
        noTerminales();
        inicial();
        producciones();
    }

    private void noTerminales() throws Exception{
        try{
            int d,h;
            d = _cadena.indexOf("{");
            h = _cadena.indexOf("}", d);
            String ter = _cadena.substring(d + 1, h);

            _noTerminales = new Vector();
            for (int i = 0; i < ter.length() ; i++){
                char ch = ter.charAt(i);
                if (!(Character.isLetter(ch) &&
                      Character.isUpperCase(ch)))
                    throw new Exception("Entre los no terminales hay un " +
                                        "símbolo incorrecto." );

                _noTerminales.add("" + ter.charAt(i));
            }
            _noTerminales.add(SPRIMA);

            _cadena = _cadena.substring(h + 1).trim();
        }catch(Exception e){
            throw new Exception("Los símbolos no terminales no son correctos.");
        }
    }

    private void terminales() throws Exception{
        try{
            int d,h;
            d = _cadena.indexOf("{");
            h = _cadena.indexOf("}", d);
            String ter = _cadena.substring(d + 1, h);

            _terminales = new Vector();
            for (int i = 0; i < ter.length() ; i++){
                if (!(Character.isLetter(ter.charAt(i)) &&
                      Character.isLowerCase(ter.charAt(i))))
                    throw new Exception("Entre los terminales hay un " +
                                        "símbolo incorrecto." );
                _terminales.add("" + ter.charAt(i));
            }
            _terminales.add(PESOS);

            _cadena = _cadena.substring(h + 1).trim();
        }catch(Exception e){
            throw new Exception("Los símbolos terminales no son correctos.");
        }
    }

    private void inicial() throws Exception{
        _inicial = _cadena.substring(0,1);
        _cadena = _cadena.substring(1).trim();
        if (!_noTerminales.contains(_inicial))
            throw new Exception ("El símbolo distinguido no es un no terminal.");
    }

    private void producciones() throws Exception{
        boolean sigo = true;
        int d,h;
        int numProd = 0;
        String pro;
        Produccion p;
        _producciones = new Vector();

        p = new Produccion(SPRIMA + Produccion.SEPARADOR + getInicial() +
                           PESOS  + Produccion.FIN_PRODUCCION, numProd++);
        _producciones.add(p);
        _prodInicial = (Produccion)p.clone();
        while (sigo){
            h = _cadena.indexOf(";");
            if (h > 1){
                pro = _cadena.substring(0, h + 1);
                p = new Produccion(pro, numProd++);
                _producciones.add(p);
                _cadena = _cadena.substring(h + 1).trim();
            }else
                sigo = false;
        }
    }

    public static void main(String args[]) throws Exception{

        GLC g = null;
        try{
           /* g = new GLC("{abcde}" + "{SABCDE}" + "S " +
                          "S:Sa; S:Ae; C:; S:aSa; " +
                        "A:abC; A:B; B:CCabc;");
            */
          /*  g = new GLC("{ab} {SA} S " +
                        "S:aA; S:b;" +
                        "A:b; ");
            */
            //g = new GLC("{ab} {S} S S:aS; S:b;");
            //g = new GLC("{abcdexwkz} {SABCX} S S:aS; S:b; S:ACX; A:C; C:da;" +
              //          "S:B; B:BwCBk; X:x; C:; B:; B:z;");
            //g = new GLC ("{abc} {SABC} S S:ABC; A:a; B:b; C:c;");

            g = new GLC("{ab} {AS} S S:Sa; S:Ab; A:S; A:b;  ");
            //g = new GLC ("{cdsi} {EL} E E:csLd; E:i; L:EL; L:E;   ");

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Símbolos no terminales:");
        Vector noT = g.getNoTerminales();
        for (int i = 0 ; i < noT.size(); i++ ){
            System.out.println("    " + (String) noT.get(i));
        }

        System.out.println("Símbolos terminales:");
        Vector t = g.getTerminales();
        for (int i = 0 ; i < t.size(); i++ ){
            System.out.println("    " + (String) t.get(i));
        }

        System.out.println("Símbolo distinguido:");
        System.out.println("    " + g.getInicial());

        System.out.println("Producciones:");
        Vector prod = g.getProducciones();
        for (int i = 0 ; i < prod.size(); i++ ){
            Produccion p = (Produccion)prod.get(i);
            System.out.println("    " + p.toString() + " ( " + p.getNumeroDeProduccion() + " )");
        }

        ////////////////////
        boolean b;
        String p;
        System.out.println("anulable:");
        prod = g.getTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            b = g.esAnulable(p);
            System.out.println("    " + p  + " : " + b);
        }
        prod = g.getNoTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            b = g.esAnulable(p);
            System.out.println("    " + p + " : " + b);
        }


        System.out.println("Primeros ");
        prod = g.getTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.primeros(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx + " , ");
            }
        }
        System.out.println("");
        prod = g.getNoTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.primeros(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx + " , ");
            }
        }
        System.out.println("");

        //no term
        System.out.println("Siguientes: ");
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.siguientes(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx  + " , ");
            }
        }
        //term
        prod  = g.getTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.siguientes(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx  + " , ");
            }
        }


        //últimos
        //no term
        prod  = g.getNoTerminales();
        System.out.println("");
        System.out.println("Últimos: ");
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.ultimos(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx  + " , ");
            }
        }
        //term
        prod  = g.getTerminales();
        for (int i = 0 ; i < prod.size(); i++ ){
            p = (String)prod.get(i);
            Vector d = g.ultimos(p);
            System.out.println("");
            System.out.print("  " + p + " : ");
            for (int z = 0 ;z<d.size(); z++){
                String xx = (String)d.get(z);
                System.out.print(xx  + " , ");
            }
        }

        ////////////////////////////////
        SLR slr = new SLR(g);
        String cad = "bb";
        b = slr.tieneConflicto();
        System.out.println("¿tiene conflico SLR? " + b);
        boolean bb = slr.pertenece(cad);
        System.out.println("¿pertenece " + cad  + " ? " + bb);
/*
        LRCero lr0 = new LRCero(g);
        b = lr0.tieneConflicto();
        System.out.println("¿tiene conflico LRCero? " + b);
*/
    }
}