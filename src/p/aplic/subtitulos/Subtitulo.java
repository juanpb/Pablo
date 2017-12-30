package p.aplic.subtitulos;

import p.util.Hora;

/**
 * User: JPB
 * Date: Apr 1, 2008
 * Time: 1:26:26 PM
 */
public class Subtitulo {
    private Hora inicio;
    private Hora fin;
    private String texto;


    public Subtitulo(Hora inicio, String texto) {
        this(inicio, null, texto);
    }

    public Subtitulo(Hora inicio, Hora fin, String texto) {
        this.inicio = inicio;
        this.fin = fin;
        this.texto = texto;
    }


    /**
     * Devuelve la duración en milis
     * @return
     */
    public long getDuracion() {
        return fin.diferencia(inicio);
    }

    public Hora getInicio() {
        return inicio;
    }

    public void setInicio(Hora inicio) {
        this.inicio = inicio;
    }

    public Hora getFin() {
        return fin;
    }

    public void setFin(Hora fin) {
        this.fin = fin;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public String toString() {
        return getInicio().toString() + " -> " + getFin().toString() + ": " +
                getTexto();
    }

    public boolean esAnteriorA(Subtitulo sub2) {
        // this < sub2 sii sub2 > this
        return sub2.getInicio().esMayor(getInicio());
    }
}