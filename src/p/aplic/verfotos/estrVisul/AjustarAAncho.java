package p.aplic.verfotos.estrVisul;

/**
 * User: JPB
 * Date: Jul 16, 2008
 * Time: 1:43:05 PM
 */
public class AjustarAAncho extends Basica {

    public AjustarAAncho() {
    }

    //    public long getIntervaloDeTiempo(){
//        return config.getTiempoPorFoto();
//    }

    /**
     * Calcula el tama�o como para que ocupe _todo el ancho de la pantalla
     */
    public void calcularTama�o(){
        double scH = screenSize.getHeight();
        double scW = screenSize.getWidth();

        //Esta proporci�n es la que habr�a que multimplicar al tama�o de la
        //foto para que entre completa en la pantalla (aprovechando al
        // m�ximo la pantalla sin cortar la foto)
        double proporcion= scW / anchoFoto;
        if (proporcion > 1 //si es m�s grande el monitor que la foto
                && !config.isAgrandarParaLlenarPantalla()) //y no se quiere estirar
                proporcion =1;

        //Si la proporci�n es = a 1 => la foto no se achica

        anchoAMostrar = (int) (anchoFoto * proporcion);
        alturaAMostrar = (int) (altoFoto * proporcion);

        anchoAMostrarConZoom = (int) (anchoAMostrar * offset.getZoom());
        alturaAMostrarConZoom = (int) (alturaAMostrar * offset.getZoom());

        //centrado
        posX = (int) ((scW - anchoAMostrarConZoom)/2);
        posY = (int) ((scH - alturaAMostrarConZoom)/2);
        ajustarXYSegunOffset();
    }


    /**
     * Intervalo de tiempo entre frame y frame en ms
     * @return
     */
    public long getIntervaloDeTiempo(){
        return config.getTiempoPorFoto();
    }
}
