package p.aplic.libretadirecciones.bobj;

import p.util.UtilString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Direccion {
//    		<Tit_1>Apellidos</Tit_1>
//		<Tit_2>Nombre</Tit_2>
//		<Tit_5>Casa</Tit_5>
//		<Tit_6>Móvil</Tit_5>
//		<Tit_9>Correo</Tit_9>
//		<Tit_10>Direcciones</Tit_10>
//		<Tit_15>Personalizado 1</Tit_15>
//		<Tit_19>Nota</Tit_19>
//		<Tit_21>Categoría</Tit_21>

    private boolean historico = false;
    private String apellido = null;
    private String nombre = null;
    private String telCasa = null;
    private String telTrabajo = null;
    private String movil = null;
    private String correo = null;
    private String direc = null;
    private String notas = null;
    private List<String> categorias = null;
    private Map otros = null;


    public boolean isHistorico() {
        return historico;
    }

    public void setHistorico(boolean historico) {
        this.historico = historico;
    }

    public void setApellido(String p) {
        apellido = p;
    }
    public String getAppellido() {
        return apellido;
    }

    public void setNombre(String p) {
        nombre = p;
    }
    public String getNombre() {
        return nombre;
    }

    public void setTelCasa(String p) {
        telCasa = p;
    }
    public String getTelCasa() {
        return telCasa;
    }

    public void setTelTrabajo(String p) {
        telTrabajo = p;
    }
    public String getTelTrabajo() {
        return telTrabajo;
    }

    public void setMovil(String p) {
        movil = p;
    }
    public String getMovil() {
        return movil;
    }

    public void setCorreo(String p) {
        correo = p;
    }
    public String getCorreo() {
        return correo;
    }

    public void setDirec(String p) {
        direc = p;
    }
    public String getDirec() {
        return direc;
    }

    public void setNotas(String p) {
        notas = p;
    }
    public String getNotas() {
        return notas;
    }

    public void addCategoria(String p) {
        if (categorias == null)
            categorias = new ArrayList<String>();
        categorias.add(p);
    }

    public void setCategorias(List<String> p) {
        categorias = p;
    }
    public List<String> getCategorias() {
        return categorias;
    }

    public Map getOtros() {
        return otros;
    }


    public void addOtro(String clave, String valor) {
        if (otros == null)
            otros = new HashMap();

        otros.put(clave, valor);
    }

    public boolean mismaPersona(Direccion otraDir){
        String no = otraDir.getNombre();
        String ap = otraDir.getAppellido();

        boolean res = (no == null && nombre == null)
                || (no != null && no.equals(nombre));

        res &= (ap == null && apellido == null)
                || (ap != null && ap.equals(apellido));


        return res;
    }

    public String toString(){
        String res = apellido + "; " + nombre + "; " + direc + "; " + correo
                + "; " + movil + "; " + telCasa + "; " + telTrabajo
                + "; " + notas + "; ";

        res = UtilString.reemplazarTodo(res, "null; ", "; ");
        return res;
    }

    private boolean categoriasIguales(List<String> una, List<String> otra){
        if (una == null && otra == null)
            return true;
        else if (una != null && otra != null && una.size() == otra.size()){
            for(int i = 0; i < una.size(); i++) {
                if (!una.get(i).equals(otra.get(i)))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean equals(Object o ){
        if (!(o instanceof Direccion))
            return false;
        Direccion od = (Direccion)o;

        boolean res = true;

        res &= (apellido == null && od.getAppellido() == null)
                || (apellido != null && apellido.equals(od.getAppellido()));

        res &= categoriasIguales(categorias, od.getCategorias());

        res &= (correo == null && od.getCorreo() == null)
                || (correo != null && correo.equals(od.getCorreo()));

        res &= (direc == null && od.getDirec() == null)
                || (direc != null && direc.equals(od.getDirec()));

        res &= (movil == null && od.getMovil() == null)
                || (movil != null && movil.equals(od.getMovil()));

        res &= (nombre == null && od.getNombre() == null)
                || (nombre != null && nombre.equals(od.getNombre()));

        res &= (notas == null && od.getNotas() == null)
                || (notas != null && notas.equals(od.getNotas()));

        res &= (telCasa == null && od.getTelCasa() == null)
                || (telCasa != null && telCasa.equals(od.getTelCasa()));

        res &= (telTrabajo == null && od.getTelTrabajo() == null)
                || (telTrabajo != null && telTrabajo.equals(od.getTelTrabajo()));

        res &= isHistorico() == od.isHistorico();

        return res;
    }

    public void cargar(Direccion nueva) {

        apellido = nueva.getAppellido();
        nombre = nueva.getNombre();
        telCasa = nueva.getTelCasa();
        telTrabajo = nueva.getTelTrabajo();
        movil = nueva.getMovil();
        correo = nueva.getCorreo();
        direc = nueva.getDirec();
        notas = nueva.getNotas();
        categorias = nueva.getCategorias();
        otros = nueva.getOtros();
        historico = nueva.isHistorico();
    }
    public Direccion getCopia(){
        Direccion d = new Direccion();

        d.setApellido(getAppellido());
        d.setNombre(getNombre());
        d.setTelCasa(getTelCasa());
        d.setTelTrabajo(getTelTrabajo());
        d.setMovil(getMovil());
        d.setCorreo(getCorreo());
        d.setDirec(getDirec());
        d.setNotas(getNotas());
        d.setCategorias(getCategorias());
        d.setHistorico(isHistorico());

        return d;
    }
}
