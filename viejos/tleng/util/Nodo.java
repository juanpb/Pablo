package tleng.util;


public class Nodo{

    private int _id;
    private Object _contenido;

    public Nodo(){}

    public Nodo(int id){
        this(id, null);
    }

    public Nodo(int id, Object contenido){
        _id = id;
        _contenido = contenido;
    }

    public void setContenido(Object contenido){
        _contenido = contenido;
    }

    public void setId(int id){
        _id = id;
    }

    public Object getContenido(){
        return _contenido;
    }

    public int getId(){
        return _id;
    }

}

