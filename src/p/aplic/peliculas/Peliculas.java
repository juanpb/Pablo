package p.aplic.peliculas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:08:20 PM
 */
public class Peliculas {

    private List<Pelicula> peliculas = new ArrayList<Pelicula>();
    private Integer contadorDVD;
    private Integer contadorUniqueID;
    private Integer contadorVistos;
    private Integer contadorSinVer;
    private Integer contadorIMDBId;
    private long timestamp;

    public void removePelicula(Pelicula p){
        peliculas.remove(p);
    }
    public void addPelicula(Pelicula p){
        if (p.getUniqueId() == null){
            p.setUniqueId(getContadorUniqueID());
            incrementarUniqueID();
        }
        peliculas.add(p);
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Devuelve las películas que correspondan al estado recibido como
     * parámetro.
     */
    public List<Pelicula> getPeliculas(Pelicula.Estado estado) {
        List<Pelicula> res = new ArrayList<Pelicula>();
        for (Pelicula pelicula : getPeliculas()) {
            if (estado.equals(pelicula.getEstado())){
                res.add(pelicula);
            }
        }
        return res;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public void setContadorDVD(Integer contadorDVD) {
        this.contadorDVD = contadorDVD;
    }


    public Integer getContadorDVD() {
        return contadorDVD;
    }

    public void incrementarDVD() {
        contadorDVD++;
    }
    public void incrementarContadorVistos() {
        contadorVistos++;
    }
    public void incrementarContadorSinVer() {
        contadorSinVer++;
    }

    public void setContadorUniqueID(Integer contadorUniqueID) {
        this.contadorUniqueID = contadorUniqueID;
    }

    public Integer getContadorUniqueID() {
        return contadorUniqueID;
    }

    public void incrementarUniqueID() {
        contadorUniqueID++;
    }

//    public void ordenarPorEstado(){
//        //Visto, sin ver, bajando, espera
//        List<Pelicula> ordenado = getPeliculas(Pelicula.Estado.VISTO);
//        ordenado.addAll(getPeliculas(Pelicula.Estado.SIN_VER));
//        ordenado.addAll(getPeliculas(Pelicula.Estado.BAJANDO));
//        ordenado.addAll(getPeliculas(Pelicula.Estado.ESPERA));
//        ordenado.addAll(getPeliculas(Pelicula.Estado.ARCHIVADO_SIN_VER));
//        setPeliculas(ordenado);
//    }

    public void ordenarPorUniqueID(){
        Collections.sort(peliculas, new Comparator<Pelicula>(){
            public int compare(Pelicula p1, Pelicula p2) {
                Integer id1 = p1.getUniqueId();
                Integer id2 = p2.getUniqueId();
                return id1 - id2;
            }
        });
    }

    public void setContadorIMDBId(Integer con) {
        contadorIMDBId = con;
    }

    public Integer getContadorIMDBId() {
        return contadorIMDBId;
    }

    public Integer getContadorSinVer() {
        return contadorSinVer;
    }

    public void setContadorSinVer(Integer contadorSinVer) {
        this.contadorSinVer = contadorSinVer;
    }

    public Integer getContadorVistos() {
        return contadorVistos;
    }

    public void setContadorVistos(Integer contadorVistos) {
        this.contadorVistos = contadorVistos;
    }
}
