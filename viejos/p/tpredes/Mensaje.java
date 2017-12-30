package p.tpredes;

public class Mensaje implements java.io.Serializable
 {

    double _idTx = 0;
    int _offeset = 0;
    byte[] _datos = null;

    public Mensaje() {
    }
    public Mensaje( double _idTx, int _offeset, byte[] _datos){
        this._idTx = _idTx;
        this._offeset = _offeset;
        this._datos = _datos;
    }

    public void setIdTx(double p) {
        this._idTx = p;
    }
    public double getIdTx() {
        return this._idTx;
    }

    public void setOffeset(int p) {
        this._offeset = p;
    }
    public int getOffeset() {
        return this._offeset;
    }

    public void setDatos(byte[] p) {
        this._datos = p;
    }
    public byte[] getDatos() {
        return this._datos;
    }
}