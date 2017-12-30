package p.aplic.verfotos.estrVisul;

import p.aplic.verfotos.Config;
import p.aplic.verfotos.Paint;

import java.awt.*;

/**
 * User: JPB
 * Date: Jun 20, 2008
 * Time: 11:59:37 AM
 */
public class RealAPantalla extends Basica{
    private Image foto;
    private Config config;
    private Paint paint = new Paint();
    private Boolean sePinto = Boolean.FALSE;

//    public RealAPantalla(File f, Config config, Container comp) {
//        try {
//            foto = javax.imageio.ImageIO.read(f);
//            comp.add(paint);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        this.config = config;
//    }

    public RealAPantalla(Config config, Container frame) {
        this.config = config;
        frame.add(paint);
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public void mostrar(){
        porcentaje = 1; //arrancamos con el tamaño real
        paint.setFoto(foto);

        long cantVistas = config.getFramesPorSegundo() * config.getTiempoPorFoto() / 1000;
        for(int i = 0; i<= cantVistas; i++){
            calcularSize();
            paint.pintar();
            porcentaje = 1.0d - (1.0d / cantVistas) * i; //porcentaje va de 1 hasta 0
        }
    }
}
