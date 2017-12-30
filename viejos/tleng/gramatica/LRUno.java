package tleng.gramatica;

import tleng.util.*;
import java.util.*;

public class LRUno {

    public static final String REDUCIR   = "Reducir";
    public static final String DESPLAZAR = "Desplazar";
    public static final String ACEPTAR   = "Aceptar";
    public static final String RECHAZAR  = "Rechazar";

    GLC     _gram;
    Grafo   _grafo;
    Vector  _estados;
    int     _tieneConflicto = 0; // es igual a 0 si aún no se verificó
    boolean _tieneConflictoB;


    Vector  _conflictos = new Vector();

    public LRUno(GLC gram) throws Exception {

        _gram = gram;
        Produccion pi = _gram._prodInicial;
        String der = pi.getDer();
        int z = der.indexOf(GLC.PESOS);
        if ( z >= 0)
            pi.setDer(der.substring(0, z));
        Item ii = new Item(pi, GLC.PESOS);
        hacerAD(ii);
        hacerTabla();

    }

    private void hacerAD(Item itemInicial) throws Exception{
        //terminales + no terminales
        Vector vPrima  = new Vector();
        vPrima.addAll(_gram.getNoTerminales());
        vPrima.addAll(_gram.getTerminales());


        Stack pila = new Stack();

        Vector v = new Vector();
        v.add(itemInicial);
        Vector q0 = clausura( v );

        int idNodo = 0;
        Nodo n = new Nodo(idNodo++, copiarItems(q0));
        _grafo = new Grafo(n);

        pila.push(new Tupla(q0, 0)); //q0 = conj. de ítems

        while (!pila.empty()){//mientras haya cjto de ítems sin marcar
            Tupla  t = (Tupla)pila.pop();
            Vector I = t._v;    // 'I'  =conjunto de ítems

            for (int i = 0; i < vPrima.size() ; i++){//para cada x de V'
                String x = (String) vPrima.get(i);
                Vector v2 = goTo(I, x);
                if (v2 != null && v2.size() > 0){
                    int z = pertenece(v2, _grafo);
                    if (z < 0){//=> v2 es un nuevo conjunto de ítems (nuevo nodo)
                        pila.push(new Tupla(v2,idNodo));
                        n = new Nodo(idNodo++, copiarItems(v2));
                        _grafo.addNodo(n);
                        _grafo.addEje(t._i, idNodo - 1, x);
                    } else //si estado (v2) ya existía => sólo
                           //agrego el eje (la transición)
                        _grafo.addEje(t._i, z, x);
                }
            }
        }
    }



    private void hacerTabla(){

        initTabla();
        Vector nodos   = _grafo.getNodos();
        Hashtable ejes = _grafo.getEjes();

        /**
         * El contenido de cada nodo del grafo es un conjunto de ítems.
         * Con cada uno de estos conjuntos, recorro todos sus ítems y me fijo si
         * alguno de ellos genera un Reduce o un Accept y lo agrego a la tabla.
         */
        for (int i = 0 ; i < nodos.size() ; i++){           //para cada nodo
            Nodo    n      = (Nodo)   nodos.get(i);         //n representa "I"
            Vector  items  = (Vector) n.getContenido();

            for (int j = 0 ;  j < items.size(); j++){       //para cada ítem
                Item   item = (Item)items.get(j);
                boolean reducir  = reduce((Item)item.clone());
                int np = item.getProduccion().getNumeroDeProduccion();
                if ( reducir){
                //hay que agregar 'Reduce'
                //consigo el look ahead y en esa columna de la tabla pongo
                //el reduce con el valor de la producción.
                    String la = item.getLookAhead();
                    Hashtable ht;
                    ht = (Hashtable)_estados.get(n.getId()); //consigo la fila
                    Vector valor = (Vector)ht.get(la);
                    valor.add(REDUCIR + " " + Integer.toString(np));
                }

                //busco [S'-> S. ]
                Produccion pr = item.getProduccion();
                int punto = item.getIndice();
                String iz = pr.getIzq();
                String de = pr.getDer();
                if (iz.equals(_gram.SPRIMA) &&
                    de.startsWith(_gram.getInicial()) &&
                    punto == 1){
                    Hashtable ht = (Hashtable)_estados.get(n.getId()); //consigo la fila
                    Vector valor = (Vector) ht.get(_gram.PESOS);
                    valor.add(ACEPTAR + " " + Integer.toString(np));
                }
            }
        }
        /**
         * Con los ejes hago los Shift.
         * Los ejes están en una Hashtable,
         *      key = "nodoPadre;ejeId" value = nodoHijo
         * => debe quedar [nodoPadre, ejeId] = nodoHijo {si ejeId es Vt}
         *              [nodoPadre, ejeId] = DESPLAZAR nodoHijo {si ejeId es Vn}
         */
        Hashtable ejesHT = (Hashtable)_grafo.getEjes();
        Enumeration keys = ejesHT.keys();
        while (keys.hasMoreElements()){
            String key   = (String)keys.nextElement();
            String padre = getNodo(key);
            String hijo  = (String)ejes.get(key);
            String eje   = getEje(key);
            Hashtable ht = (Hashtable) _estados.get(Integer.parseInt(padre));
            Vector valor = (Vector) ht.get(eje);
            if (_gram.esTerminal(eje))
                valor.add(DESPLAZAR + " " + hijo);
            else
                valor.add(hijo);
        }

        imprimirTabla();
    }

    private String getNodo(String k){
        return k.substring(0, k.indexOf(";"));
    }

    private String getEje(String k){
        return k.substring(k.indexOf(";") + 1 );
    }

    private boolean reduce(Item it){
        if (it.getProduccion().getIzq().equals(GLC.SPRIMA))
            return false;
        return !it.hayMas();
    }

    /**
     * Con los símbolos de la gramática y los estados del grafo inicializa
     * la tabla, ie, _estados y _simbolos
     */
    private void initTabla(){
        _estados = new Vector();
        Vector noT = _gram.getNoTerminales();
        Vector ter = _gram.getTerminales();

        for (int j = 0 ; j < _grafo.getCantNodos() ; j++ ){
            Hashtable ht = new Hashtable();
            //no terminales
            for (int i = 0; i < noT.size(); i++){
                String key = (String) noT.get(i);
                if (!key.equals(GLC.SPRIMA))
                    ht.put(key, new Vector());
            }

            //terminales
            for (int m = 0; m < ter.size(); m++){
                String key = (String) ter.get(m);
                ht.put(key, new Vector());
            }
            _estados.add(j,ht);
        }
    }

    private Vector clausura(Vector conjItems){
        Item    item;
        Vector  res     = new Vector();
        Stack   pila    = new Stack();
        Vector  lookA   = new Vector();
        String  aux;

        pila.addAll(conjItems);

        while (!pila.isEmpty()){
            item = (Item) pila.pop();
            boolean esta = false;
            for (int f = 0 ; f < res.size(); f++){
                Item tt = (Item) res.get(f);
                if (tt.equals(item))
                    esta = true;
            }
            if (!esta)
                res.add(item);

            item = (Item)item.clone();
            if (item.hayMas()){
                String simbolo = item.proximo();
                if (_gram.esNoTerminal( simbolo )){

                    Vector x = _gram.getProducciones(simbolo);
                    boolean b = item.hayMas();
                    if (b){
                        aux = item.proximo();
                        lookA = _gram.primeros(aux);
                    }else
                        aux = "";
                    if (_gram.esAnulable(aux)){
                        String ss = item.getLookAhead();
                        lookA.add(ss);
                    }

                    if (x != null)
                        agregarSinRepetidos(pila, res, x, lookA);
                }
            }
        }

        return res;
    }

        /**
     * Devuelve el número de nodo (su id) que tiene como contenido al vector
     * pasado como primer parámetro. -1 si no pertenece
     */
    private int pertenece(Vector v, Grafo g){
        Vector nodos = g.getNodos();
        for (int i = 0; i < nodos.size() ; i++){
            Nodo n = (Nodo)nodos.get(i);
            Vector cont  = (Vector)n.getContenido();
            if (conjuntosDeItemsIguales(cont, v))
                return n.getId();
        }
        return -1;
    }

    private boolean conjuntosDeItemsIguales(Vector v1, Vector v2){
        if (v1 == null || v2 == null)
            return false;
        if (v1.size() != v2.size())
            return false;
        for (int i = 0; i < v1.size(); i++){
            Item item = (Item)v1.get(i);
            if (!perteneceItemAItems(item, v2))
                return false;
        }
        return true;
    }

    private boolean perteneceItemAItems(Item item, Vector v2){
        for (int i = 0 ; i < v2.size(); i++){
            Item it = (Item)v2.get(i);
            if (item.equals(it))
                return true;
        }
        return false;
    }

    private Vector copiarItems(Vector v){
        Vector res = new Vector();
        Item item;
        for (int i = 0; i < v.size(); i++){
            item = (Item) v.get(i);
            res.add(item.clone());
        }
        return res;
    }


    /**
     * Transforma las producciones recibidas en ítems y si no están en res (ni
     * en la pila ) las agrega en la pila (una por cada simbolo de lookA).
     */
    private void agregarSinRepetidos(Stack pila, Vector res,
                                     Vector prod, Vector lookA){
        for (int i = 0; i < prod.size(); i++ ){
            Produccion p = (Produccion)prod.get(i);

            for (int j = 0 ; j < lookA.size(); j++){
                String la = (String) lookA.get(j);
                Item niuItem = new Item(p, la);

                Enumeration enum = res.elements();
                boolean esta = false;
                while (enum.hasMoreElements() && !esta){
                    Item item = (Item) enum.nextElement();
                    if (item.equals(niuItem)){
                        esta = true;
                    }
                }
                //busco en la pila
                enum = pila.elements();
                while (enum.hasMoreElements() && !esta){
                    Item item = (Item) enum.nextElement();
                    if (item.equals(niuItem)){
                        esta = true;
                    }
                }
                if (! esta)
                    pila.push(niuItem);
            }
        }
    }

    // el vector que devuelve tiene copias de los ítems
    private Vector goTo(Vector items, String simbolo){
        Vector tem = new Vector();
        Item item;
        for (int i = 0; i < items.size(); i++ ){
            item = (Item) items.get(i);
            item = (Item)item.clone();
            if (item.hayMas()){
                String x = item.proximo();
                if (x != null && x.equals(simbolo))
                    tem.add(item);
            }
        }
        return clausura(tem);
    }


    private void imprimirTabla(){
        Hashtable ht;
        Enumeration simb;
        String x;
        System.out.println("Valores de la tabla");
        for (int i = 0 ; i < _estados.size() ; i++ ){
        // 'i' es la fila de la tabla
            ht = (Hashtable) _estados.get(i);
            simb = ht.keys();
            //cada 'simb' es una columna de la tabla
            while (simb.hasMoreElements()){
                x = (String)simb.nextElement();
                Vector contenido = (Vector)ht.get(x);
                imprimirValores(i, x, contenido);
            }
        }
    }

    private void imprimirValores(int fila, String columna, Vector contenido){
        System.out.print("[ " + fila + " , " + columna + " ] = ");
        if (contenido.size() == 0)
            System.out.println(RECHAZAR);
        else{
            boolean primero = true;
            for (int i = 0; i < contenido.size() ; i++){
                String x = (String)contenido.get(i);
                if (primero){
                    primero = false;
                    System.out.print(x);
                }
                else
                    System.out.print(", " + x);
            }
            System.out.println(" ");
        }
    }

        public boolean pertenece(String cadena){
        boolean res   = false;
        Stack   pila  = new Stack();
        int     i     = 0;
        String  valor = "";
        String  ch    = "";
        Vector  v     = new Vector();
        String  actual;
        String  copiaCadena;

        if (tieneConflicto() ){
            System.out.println("Se está usando un parser con conflictos");
            return false;
        }

        cadena = cadena.trim();
        if (cadena.length() == 0)
            return true;
        //verifico que todos los símbolos de la cadena pertenezcan a Vt
        for (int d = 0 ; d < cadena.length() ; d++ ){
            String cha = cadena.substring(d, d + 1 );
            if (!_gram.esTerminal(cha)){
                System.out.println("El caracter '" + cha + "' de la cadena " +
                cadena + " no pertenece a los terminales de la gramática.");
                return false;
            }
        }

        //si no termina en $ => lo agrego
        if (!cadena.endsWith("$"))
            cadena = cadena + "$";
        copiaCadena = cadena;

        ch = cadena.substring(i, ++i);
        actual = ch;
        pila.add(new Integer(0));


        while (true){
            Integer ii  = (Integer) pila.peek();
            v = valorTabla(ii, actual);
            if (v == null || v.size() == 0){
                imprimirAccion(pila, copiaCadena, "");
                System.out.println("Nueva accion: ");
                System.out.println("    Rechazar la cadena");
                return false;
            }
            valor = (String)v.get(0);

            if (valor.startsWith(ACEPTAR) ){
                System.out.println("Nueva accion:");
                System.out.println("    Aceptar la cadena");
                return true;
            }

            if (valor.startsWith(DESPLAZAR)){
                String x = valor.substring(valor.indexOf(" ")).trim();

                //para log
                imprimirAccion(pila, copiaCadena, DESPLAZAR + " " + x);

                pila.push(actual);
                Integer z = new Integer(x);
                pila.push(z);
                if (i >= cadena.length() ){
                    imprimirAccion(pila, copiaCadena, "");
                    System.out.println("Nueva accion: ");
                    System.out.println("    Rechazar la cadena");
                    return false;
                }
                copiaCadena = cadena.substring(i );
                actual = cadena.substring(i, ++i);
            }
            else if(valor.startsWith(REDUCIR)){
                String x = valor.substring(valor.indexOf(" ")).trim();
                Integer z = new Integer(x);
                Produccion p = _gram.getProduccion(z.intValue());

                //para log
                imprimirAccion(pila, copiaCadena, REDUCIR + " " + x);

                //desaplilo 2 * |parte derecha| veces
                int tamPD = p.tamanoParteDerecha();
                for (int f = 0 ; f < tamPD * 2 ; f++)
                    pila.pop();

                Integer ss = (Integer) pila.peek(); //levanto el estado
                Vector vv = valorTabla(ss, p.getIzq());
                if (vv.size() == 0){
                    System.out.println("Nueva accion: ");
                    System.out.println("    Rechazar la cadena");
                    return false;
                }
                valor = (String) vv.get(0);
                pila.push(p.getIzq());
                pila.push(new Integer(valor));
            }
            else{
                System.out.println("Nueva accion: ");
                System.out.println("    Rechazar la cadena");

                return false;
            }
        }
    }

    public boolean tieneConflicto(){
        Vector v = null;

        if (_tieneConflicto != 0)
            return _tieneConflictoB;
        _tieneConflicto = 1; //para que no se vuelva a verificar
        _tieneConflictoB = false;

        for (int i = 0; i < _estados.size() ; i++){

            Hashtable ht = (Hashtable)_estados.get(i);
            //verifico no terminales
            for (int j = 0 ; j < _gram.getNoTerminales().size() ; j++){
                String str = (String)_gram.getNoTerminales().get(j);
                if (! str.equals(GLC.SPRIMA)){
                    v = (Vector)valorTabla(i, str);
                    if (v.size() > 1){
                        String e = "Conflico en [" + i + ", " + str + "]";
                        _conflictos.add(e);
                        _tieneConflictoB = true;
                    }
                }
            }

            //verifico terminales
            for (int j = 0 ; j < _gram.getTerminales().size() ; j++){
                String str = (String)_gram.getTerminales().get(j);
                if (! str.equals(GLC.SPRIMA)){
                    v = (Vector)valorTabla(i, str);
                    if (v.size() > 1){
                        String e = "Conflico en [" + i + ", " + str + "]";
                        _conflictos.add(e);
                        _tieneConflictoB = true;
                    }
                }
            }
        }
        if (_tieneConflicto == 0)
            _tieneConflicto = -1;

        for (int j = 0 ; j < _conflictos.size() ; j++ ){
            String s = (String) _conflictos.get(j);
            System.out.println(s);
        }
        return _tieneConflictoB;
    }

        //si el estado o el símbolo no corresponden a la tabla, devuelve nulo
    private Vector valorTabla (int estado, String simbolo){
        Vector valor = null;
        try{
            Hashtable ht = (Hashtable) _estados.get(estado);
            valor = (Vector) ht.get(simbolo);
        }catch(Exception e){}

        return valor;
    }

    private Vector valorTabla (Integer estado, String simbolo){
        return valorTabla(estado.intValue(), simbolo);
    }


    private Vector valorTabla (int estado, char simbolo){
        char[] sim = new char[1];
        sim[0] = simbolo;
        return valorTabla(estado, new String (sim));
    }

    private void imprimirAccion(Stack pila, String cadena, String accion){
        Stack   pila2   = new Stack();
        System.out.println("Nueva accion: ");
        while (!pila.empty()){ //hago una copia de la pila al revés
            Object o = pila.pop();
            pila2.push(o);
        }

        System.out.print("    Pila: ");
        while (!pila2.empty()){
            Object o = pila2.pop();
            pila.push(o);
            System.out.print(o.toString());
        }
        System.out.println("");
        System.out.println("    Cadena: " + cadena);
        System.out.println("    accion: " + accion);
    }
}


