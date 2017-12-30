package p.aplic.verfotos.estrVisul;

/**
 * User: JPB
 * Date: Jul 16, 2008
 * Time: 1:43:05 PM
 */
public class AjustarAPantalla extends Basica {


    /**
     * Calcula el tamaño como para que se muestre la foto entera ajustada a la pantalla
     */
    public void calcularTamaño(){
        super.calcularSize();
    }


    /**
     * Intervalo de tiempo entre frame y frame en ms
     * @return
     */
    public long getIntervaloDeTiempo(){
        return config.getTiempoPorFoto();
    }
}
