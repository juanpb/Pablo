package p.aplic.peliculas.util;

/**
 * User: JPB
 * Date: Jul 17, 2009
 * Time: 8:28:21 AM
 */
public class ExportarConfig {
    private boolean mostrarId = false;
    private boolean mostrarNombre = false;
    private boolean mostrarAnnus = false;
    private boolean mostrarIMDB = false;
    private boolean mostrarPais = false;
    private boolean mostrarComentarios = false;
    private boolean mostrarProblemas = false;
    private boolean mostrarDuracion = false;
    private boolean mostrarPosters = false;
    private boolean mostrarTags = false;
    private boolean mostrarCritica = false;
    private boolean mostrarDatosTecnicos = false;
    private boolean soloHD = false;

    public ExportarConfig() {
    }

    public ExportarConfig(boolean mostrarTodo) {
        setMostrarId(mostrarTodo);
        setMostrarNombre(mostrarTodo);
        setMostrarAnnus(mostrarTodo);
        setMostrarIMDB(mostrarTodo);
        setMostrarPais(mostrarTodo);
        setMostrarPosters(mostrarTodo);
        setMostrarComentarios(mostrarTodo);
        setMostrarProblemas(mostrarTodo);
        setMostrarDuracion(mostrarTodo);
        setMostrarPais(mostrarTodo);
        setMostrarCritica(mostrarTodo);
        setMostrarDatosTecnicos(mostrarTodo);
        setMostrarTags(mostrarTodo);
    }

    public boolean isMostrarId() {
        return mostrarId;
    }

    public void setMostrarId(boolean mostrarId) {
        this.mostrarId = mostrarId;
    }

    public boolean isMostrarNombre() {
        return mostrarNombre;
    }

    public void setMostrarNombre(boolean mostrarNombre) {
        this.mostrarNombre = mostrarNombre;
    }

    public boolean isMostrarAnnus() {
        return mostrarAnnus;
    }

    public void setMostrarAnnus(boolean mostrarAnnus) {
        this.mostrarAnnus = mostrarAnnus;
    }

    public boolean isMostrarIMDB() {
        return mostrarIMDB;
    }

    public void setMostrarIMDB(boolean mostrarIMDB) {
        this.mostrarIMDB = mostrarIMDB;
    }

    public boolean isMostrarPais() {
        return mostrarPais;
    }

    public void setMostrarPais(boolean mostrarPais) {
        this.mostrarPais = mostrarPais;
    }

    public boolean isMostrarComentarios() {
        return mostrarComentarios;
    }

    public void setMostrarComentarios(boolean mostrarComentarios) {
        this.mostrarComentarios = mostrarComentarios;
    }

    public boolean isMostrarProblemas() {
        return mostrarProblemas;
    }

    public void setMostrarProblemas(boolean mostrarProblemas) {
        this.mostrarProblemas = mostrarProblemas;
    }

    public boolean isMostrarDuracion() {
        return mostrarDuracion;
    }

    public void setMostrarDuracion(boolean mostrarDuracion) {
        this.mostrarDuracion = mostrarDuracion;
    }

    public boolean isMostrarPosters() {
        return mostrarPosters;
    }

    public void setMostrarPosters(boolean mostrarPosters) {
        this.mostrarPosters = mostrarPosters;
    }

    public boolean isMostrarCritica() {
        return mostrarCritica;
    }

    public void setMostrarCritica(boolean mostrarCritica) {
        this.mostrarCritica = mostrarCritica;
    }

    public boolean isMostrarDatosTecnicos() {
        return mostrarDatosTecnicos;
    }

    public void setMostrarDatosTecnicos(boolean mostrarDatosTecnicos) {
        this.mostrarDatosTecnicos = mostrarDatosTecnicos;
    }

    public boolean isMostrarTags() {
        return mostrarTags;
    }

    public void setMostrarTags(boolean mostrarTags) {
        this.mostrarTags = mostrarTags;
    }

    public void setSoloHD(boolean b) {
        soloHD = b;
    }
}
