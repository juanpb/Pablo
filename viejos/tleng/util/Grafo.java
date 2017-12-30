package tleng.util;

import java.util.*;

public class Grafo{

    private Nodo   _raiz;
    private Vector _nodos;
    private Vector _ides;

    /*
        En la hashtable se guarda, dado un nodo1Padre, nodoHijo, ejeId;
            key = "nodoPadre;ejeId" value = nodoHijo
    */
    private Hashtable _ejes = new Hashtable();

    public Grafo(Nodo raiz){
        _nodos = new Vector();
        _ides  = new Vector();

        _nodos.add(raiz);
        _ides.add(new Integer (raiz.getId()));
        _raiz = raiz;
    }

    /**
     * Si el id del nodo ya existe en el grafo => Exception
     */
    public void addNodo(Nodo nodo) throws Exception{
        if (_ides.contains(new Integer (nodo.getId())))
            throw new Exception("id repetido");

        _nodos.add(nodo);
        _ides.add(new Integer(nodo.getId()));
    }

    /**
     * El eje va (apunta) de el nodo padre al hijo.
     */
    public void addEje(int idNodoPadre, int idNodoHijo, String ejeId)
                            throws Exception{
        if (!(_ides.contains(new Integer(idNodoPadre)) &&
              _ides.contains(new Integer(idNodoHijo))))
            throw new Exception("Alguno de los id no corresponde a un nodo" +
                                " del grafo");
        _ejes.put(Integer.toString(idNodoPadre) + ";" + ejeId,
                  Integer.toString(idNodoHijo));
    }

    public Nodo getNodo(int nodoId, String ejeId){
        Integer nodoIdRes = (Integer) _ejes.get(nodoId + ";" + ejeId);
        Nodo nodo;
        for (int i = 0 ; i < _nodos.size() ; i++){
            nodo = (Nodo) _nodos.get(i);
            if (nodoIdRes.equals(new Integer (nodo.getId())))
                return nodo;
        }
        return null;
    }

    public Nodo getNodo(int nodoId){
        Nodo nodo;
        for (int i = 0 ; i < _nodos.size() ; i++){
            nodo = (Nodo) _nodos.get(i);
            if (new Integer(nodoId).equals(new Integer (nodo.getId())))
                return nodo;
        }
        return null;
    }

    public int getCantNodos(){
        return _nodos.size();
    }

    public Nodo getRaiz(){
        return _raiz;
    }

    public Vector getNodos(){
        return _nodos;
    }

    public Hashtable getEjes(){
        return _ejes;
    }

}