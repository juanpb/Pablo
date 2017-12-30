package p.aplic.verfotos.v3;

import java.awt.*;

/**
 * User: JPB
 * Date: Aug 11, 2009
 * Time: 3:09:22 PM
 */
public class ControlObj {
    private Image foto;
    private boolean pausar = false;
    private boolean salir = false;
    private boolean mostrarYPausar = false;

    private Offset offset = new Offset();
    private boolean mostrarProxima;
    private boolean mostrarPrevia;
    private boolean actualizar;

    public void setSalir(boolean b) {
        salir = b;
    }

    public boolean isSalir() {
        return salir;
    }

    public boolean isPausar() {
        return pausar;
    }

    public void setPausar(boolean pausar) {
        this.pausar = pausar;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public boolean isMostrarYPausar() {
        return mostrarYPausar;
    }

    public void setMostrarYPausar(boolean mostrarYPausar) {
        this.mostrarYPausar = mostrarYPausar;
    }

    public void setMostrarProxima(boolean b) {
        mostrarProxima = b;
    }

    public boolean isMostrarProxima() {
        return mostrarProxima;
    }

    public boolean isActualizar() {
        return actualizar;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
        setActualizar(true);
    }

    public boolean isMostrarPrevia() {
        return mostrarPrevia;
    }

    public void setMostrarPrevia(boolean mostrarPrevia) {
        this.mostrarPrevia = mostrarPrevia;
    }
}
