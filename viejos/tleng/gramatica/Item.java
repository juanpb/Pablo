package tleng.gramatica;


public class Item {

    private int _indice = 0;
    private Produccion  _produccion;
    private String _der;
    private String _lookAhead = "";

    public Item(){}

    public Item(Produccion produccion){
        _produccion = produccion;
        _der = produccion.getDer();
    }
     public Item(Produccion produccion, String lookAhead){
        _produccion = produccion;
        _der = produccion.getDer();
        _lookAhead = lookAhead;
    }

    public String toString(){
        String res, adp;
        adp  = _produccion.getDer().substring(0, _indice);
        res = "[ " + _produccion.getIzq() + " --> " + adp + "." +
              _der + Produccion.FIN_PRODUCCION + " ]";

        return res;
    }

    public boolean hayMas(){
        return !_der.equals("");
    }

    public Produccion getProduccion(){
        return _produccion;
    }

    public String getLookAhead(){
        return _lookAhead;
    }

    public Object clone(){
        Item  it = null;
        try {
            it = new Item((Produccion)_produccion.clone());
            it._indice = _indice;
            it._der = _der;
            it._lookAhead = _lookAhead;
        }catch(Exception e ){
            e.printStackTrace();
        }

        return it;
    }

    public int getIndice(){
        return _indice;
    }

    public String proximo(){
        String res = "";
        if (! hayMas())
            return res;
        res = _der.substring(0,1);
        try{
            _der = _der.substring(1);
        }
        catch(Exception e){
            _der  = "";
        }
        _indice++;
        if (res.equals(GLC.PESOS)){
            res = "";
            _der = "";
        }
        return res;
    }

    public boolean equals(Item item){
        return  _indice == item._indice &&
                _produccion.toString().equals(item._produccion.toString()) &&
                _lookAhead.equals(item.getLookAhead());
    }
}