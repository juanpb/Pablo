package p.gui.superGui;


import java.util.List;

/**
 * User: JPB
 * Date: 07/05/13
 * Time: 14:06
 */
public class Param {
    private List<String> valoresPosibles;
    private String nombre;
    private String tooltip;
    private boolean esCheckbox;

    public Param() {
        this(null);
    }

    public Param(String nombre) {
        this(nombre, null);
    }

    public Param(String nombre, boolean esCheckbox) {
        this(null, nombre, null, esCheckbox);
    }

    public Param(String nombre, String tooltip) {
        this(null, nombre, tooltip);
    }
    public Param(List<String> valoresPosibles, String nombre) {
        this(valoresPosibles, nombre, null);
    }

    public Param(List<String> valoresPosibles, String nombre, String tooltip) {
        this(valoresPosibles, nombre, tooltip, false);
    }

    public Param(List<String> valoresPosibles, String nombre, String tooltip, boolean esCheckbox) {
        this.esCheckbox = esCheckbox;
        this.valoresPosibles = valoresPosibles;
        this.nombre = nombre;
        this.tooltip = tooltip;
    }

    public List<String> getValoresPosibles() {
        return valoresPosibles;
    }

    public void setValoresPosibles(List<String> valoresPosibles) {
        this.valoresPosibles = valoresPosibles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }


    public boolean isEsCheckbox() {
        return esCheckbox;
    }

    public void setEsCheckbox(boolean esCheckbox) {
        this.esCheckbox = esCheckbox;
    }
}
