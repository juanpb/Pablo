package p.aplic.verfotos;

/**
 * User: JPB
 * Date: Jul 3, 2008
 * Time: 11:43:36 AM
 */
public class Offset {
    private double zoom = 1.0;
    private double offsetHorizontal = 0;
    private double offsetVertical = 0;
    private static final double INCREMENTO_ZOOM = 0.1;
    private static final double INCREMENTO_HORIZONTAL = 20;
    private static final double INCREMENTO_VERTICAL = INCREMENTO_HORIZONTAL;

    public double getZoom() {
        return zoom;
    }

    public void setZoom(boolean incrementar) {
        zoom += incrementar ? INCREMENTO_ZOOM : -INCREMENTO_ZOOM;

        //el mínimo es INCREMENTO_ZOOM
        if (zoom < INCREMENTO_ZOOM)
            zoom = INCREMENTO_ZOOM; 
    }

    public double getOffsetHorizontal() {
        return offsetHorizontal;
    }

    public void setOffsetHorizontal(boolean moverALaDerecha) {
        offsetHorizontal += moverALaDerecha ?
                INCREMENTO_HORIZONTAL : -INCREMENTO_HORIZONTAL;
    }

    public double getOffsetVertical() {
        return offsetVertical;
    }

    public void setOffsetVertical(boolean bajar) {
        offsetVertical += bajar ? INCREMENTO_VERTICAL : -INCREMENTO_VERTICAL;
    }

    public void reset(){
        offsetHorizontal = 0;
        offsetVertical = 0;
        zoom = 1;
    }
}
