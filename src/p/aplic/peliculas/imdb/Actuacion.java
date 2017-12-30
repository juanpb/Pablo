package p.aplic.peliculas.imdb;

import p.aplic.peliculas.imdb.loaders.Util;

/**
 * User: JPB
 * Date: Apr 21, 2009
 * Time: 7:48:30 AM
 */
public class Actuacion {
    private Peli peli;
    private int puesto;
    private String papel;


    public Actuacion(Peli peli, int puesto) {
        this(peli, puesto, null);
    }

    public Actuacion(Peli peli, int puesto, String papel) {
        this.peli = peli;
        this.puesto = puesto;
        setPapel(papel);
    }

    public Peli getPeli() {
        return peli;
    }

    public void setPeli(Peli peli) {
        this.peli = peli;
    }

    public int getPuesto() {
        return puesto;
    }

    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = Util.truncar(papel, 200);
    }
}
