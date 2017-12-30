package p.aplic.sincro;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Feb 5, 2009
 * Time: 7:36:41 AM
 */
public class Log {

    private List<String> error = new ArrayList<String>();
    private List<String> info = new ArrayList<String>();
    private List<String> copiados = new ArrayList<String>();
    private List<String> noCopiados = new ArrayList<String>();
    private List<String> movidos = new ArrayList<String>();
    private List<String> noMovidos = new ArrayList<String>();
    private List<String> contenidoMovido = new ArrayList<String>();
    private List<String> contenidoNoMovido = new ArrayList<String>();
    private List<String> abierto = new ArrayList<String>();
    private List<String> noAbierto = new ArrayList<String>();

    public List<String> getInfo() {
        return info;
    }

    public List<String> getCopiados() {
        return copiados;
    }

    public List<String> getNoCopiados() {
        return noCopiados;
    }

    public List<String> getMovidos() {
        return movidos;
    }

    public List<String> getNoMovidos() {
        return noMovidos;
    }

    public List<String> getContenidoMovido() {
        return contenidoMovido;
    }

    public List<String> getContenidoNoMovido() {
        return contenidoNoMovido;
    }

    public List<String> getAbierto() {
        return abierto;
    }

    public List<String> getError() {
        return error;
    }

    public List<String> getNoAbierto() {
        return noAbierto;
    }

    public boolean addCopiado(String s) {
        return copiados.add(s);
    }
    public boolean addNoCopiado(String s) {
        return noCopiados.add(s);
    }

    public boolean addMovido(String s) {
        return movidos.add(s);
    }
    public boolean addNoMovido(String s) {
        return noMovidos.add(s);
    }

    public boolean addAbierto(String s) {
        return abierto.add(s);
    }
    public boolean addNoAbierto(String s) {
        return noAbierto.add(s);
    }

    public boolean addContenidoMovido(String s) {
        return contenidoMovido.add(s);
    }
    public boolean addContenidoNoMovido(String s) {
        return contenidoNoMovido.add(s);
    }

    public boolean addInfo(String s) {
        return info.add(s);
    }
    public boolean addError(String s) {
        return error.add(s);
    }


    public void mostrar() {
        if (getNoAbierto().size() > 0){
            System.out.println("No se abrieron:");
            toString(getNoAbierto());
        }
        if (getNoCopiados().size() > 0){
            System.out.println("No se copiaron:");
            toString(getNoCopiados());
        }
        if (getNoMovidos().size() > 0){
            System.out.println("No se movieron:");
            toString(getNoMovidos());
        }
        if (getContenidoNoMovido().size() > 0){
            System.out.println("No se movió el contenido:");
            toString(getNoMovidos());
        }

        if (getInfo().size() > 0){
            System.out.println("Info:");
            toString(getInfo());
        }




        if (getAbierto().size() > 0){
            System.out.println("Se abrieron:");
            toString(getAbierto());
        }
        if (getCopiados().size() > 0){
            System.out.println("Se copiaron:");
            toString(getCopiados());
        }
        if (getMovidos().size() > 0){
            System.out.println("Se movieron:");
            toString(getMovidos());
        }

        if (getContenidoMovido().size() > 0){
            System.out.println("Se movió el contenido:");
            toString(getMovidos());
        }

        if (getError().size() > 0){
            System.out.println("Errores:");
            toString(getError());
        }

    }

    private void toString(List<String> list) {
        for(String s : list) {
            System.out.println("    " + s);
        }

    }
}
