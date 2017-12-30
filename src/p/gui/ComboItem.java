package p.gui;

public class ComboItem {
    private Object clave;
    private String valor;

    public ComboItem() {
    }

    public ComboItem( Object clave, String valor){
        this.clave = clave;
        this.valor = valor;
    }
    public void setClave(Object p) {
        this.clave = p;
    }
    public Object getClave() {
        return this.clave;
    }

    public void setValor(String p) {
        this.valor = p;
    }
    public String getValor() {
        return this.valor;
    }

    public String toString(){
        return valor;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ComboItem))
            return false;

        final ComboItem comboItem = (ComboItem) o;

        if (clave != null ? !clave.equals(comboItem.clave) : comboItem.clave != null)
            return false;
        if (valor != null ? !valor.equals(comboItem.valor) : comboItem.valor != null)
            return false;

        return true;
    }

}