package p.aplic.verfotos.estrVisul;

/**
 * User: JPB
 * Date: Jul 16, 2008
 * Time: 1:43:05 PM
 */
public class AjustarAPantalla extends Basica {


    /**
     * Calcula el tama�o como para que se muestre la foto entera ajustada a la pantalla
     */
    public void calcularTama�o(){
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
