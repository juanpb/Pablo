package p.aplic.peliculas;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:04:50 PM
 */
public class Pelicula {
    public static final String IMDB_NO_ENCONTRADO = "-";
    private String nombre;
    private String aka;
    private String annus;
    private String pais;
    private List<String> posters = new ArrayList<String>(1);
    private String imdbPuntaje;
    private String imdbId;
    private Estado estado;
    private String id;
    private Integer uniqueId;

    /**
     * Contador usado para estado 'visto' y para estado 'sin_ver' 
     */
    private Integer contador = 0;
    private String critica;
    private String duracion;
    private String comentario;
    private String datosTecnicos;
    private String problemas;

    public Pelicula clonar(){
        Pelicula res = new Pelicula();
        res.cargar(this);
        return res;
    }

    /**
     * Carga la película pasado como parámetro a this
     */
    public void cargar(Pelicula pel){
        setAka(pel.getAka());
        setAnnus(pel.getAnnus());
        setComentario(pel.getComentario());
        setProblemas(pel.getProblemas());
        setCritica(pel.getCritica());
        setDatosTecnicos(pel.getDatosTecnicos());
        setDuracion(pel.getDuracion());
        setEstado(pel.getEstado());
        setId(pel.getId());
        setUniqueId(pel.getUniqueId());
        setContador(pel.getContador());
        setImdbId(pel.getImdbId());
        setImdbPuntaje(pel.getImdbPuntaje());
        setNombre(pel.getNombre());
        setPais(pel.getPais());
        setPosters(pel.getPosters());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pelicula) )
            return false;

        Pelicula pel = (Pelicula) obj;

        boolean res = sonIguales(getAka(), pel.getAka());
        res &= sonIguales( getAnnus(), pel.getAnnus());
        res &= sonIguales( getComentario(), pel.getComentario());
        res &= sonIguales( getProblemas(), pel.getProblemas());
        res &= sonIguales( getCritica(), pel.getCritica());
        res &= sonIguales( getDatosTecnicos(), pel.getDatosTecnicos());
        res &= sonIguales( getDuracion(), pel.getDuracion());
        res &= sonIguales( getEstado(), pel.getEstado());
        res &= sonIguales( getId(), pel.getId());
        res &= sonIguales( getUniqueId(), pel.getUniqueId());
        res &= sonIguales( getContador(), pel.getContador());
        res &= sonIguales( getImdbId(), pel.getImdbId());
        res &= sonIguales( getImdbPuntaje(), pel.getImdbPuntaje());
        res &= sonIguales( getNombre(), pel.getNombre());
        res &= sonIguales( getPais(), pel.getPais());
        res &= sonIguales( getPosters(), pel.getPosters());

        return res;
    }

    private boolean sonIguales(Object a, Object b) {
        if (a == null)
            return b == null;

        return a.equals(b);
    }

    public boolean isHD() {
        final String id1 = getId();
        if (id1 == null)
            return false;
        else{
            final String s = id1.toLowerCase();
            final boolean id2 = s.contains("hd");
            return id2;
        }
    }

    public enum Estado {
        ESPERA, BAJANDO, SIN_VER, VISTO, ARCHIVADO_SIN_VER;

        public static Estado parse(String est){
            if (ESPERA.toString().equals(est))
                return ESPERA;
            else if (BAJANDO.toString().equals(est))
                return BAJANDO;
            else if (SIN_VER.toString().equals(est))
                return SIN_VER;
            else if (VISTO.toString().equals(est))
                return VISTO;
            else if (ARCHIVADO_SIN_VER.toString().equals(est))
                return ARCHIVADO_SIN_VER;
            else
                return null;
        }

        public String toString(){
            switch(this) {
                case ESPERA:   return "Espera";
                case BAJANDO:  return "Bajando";
                case SIN_VER:  return "Sin ver";
                case VISTO: return "Visto";
                case ARCHIVADO_SIN_VER: return "Archivado";
            }
            throw new AssertionError("Unknown op: " + this);
        }
    }

    public static List<Estado> getEstados(){
        List<Estado> res = new ArrayList<Estado>();

        res.add(Estado.BAJANDO);
        res.add(Estado.ESPERA);
        res.add(Estado.SIN_VER);
        res.add(Estado.VISTO);
        res.add(Estado.ARCHIVADO_SIN_VER);

        return res;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAnnus() {
        return annus;
    }

    public void setAnnus(String annus) {
        this.annus = annus;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<String> getPosters() {
        return posters;
    }

    public void setPosters(List<String> posters) {
        this.posters = posters;
    }

    public void addPoster(String nombre){
        posters.add(nombre);
    }

    public String getImdbPuntaje() {
        return imdbPuntaje;
    }

    public void setImdbPuntaje(String imdbPuntaje) {
        this.imdbPuntaje = imdbPuntaje;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCritica() {
        return critica;
    }

    public void setCritica(String critica) {
        this.critica = critica;
    }


    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }


    public String toString() {
        return getNombre();
    }


    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    public String getDatosTecnicos() {
        return datosTecnicos;
    }

    public void setDatosTecnicos(String datosTecnicos) {
        this.datosTecnicos = datosTecnicos;
    }

    public String getProblemas() {
        return problemas;
    }

    public void setProblemas(String problemas) {
        this.problemas = problemas;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getAka() {
        return aka;
    }

    public void setAka(String aka) {
        this.aka = aka;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public boolean esHD(){
        final String id1 = getId();
        return (id1 != null) && id1.contains("HD");
    }
}
