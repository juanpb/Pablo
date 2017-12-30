package p.aplic.verfotos.estrVisul;

import p.aplic.verfotos.Config;
import p.aplic.verfotos.Offset;

import java.awt.*;
import java.math.BigDecimal;

/**
 * User: JPB
 * Date: Jun 19, 2008
 * Time: 1:58:47 PM
 */
public class Basica {
    protected int alturaAMostrar;
    protected  int anchoAMostrar;
    protected int alturaAMostrarConZoom;
    protected  int anchoAMostrarConZoom;
    protected  int posX = 0;
    protected  int posY = 0;
    protected  boolean mostrarInfo = false;

    protected  Dimension screenSize =
            Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Si porcentaje = 0 => la foto se ajusta a la pantalla.
     * Si porcentaje = 1 => la foto se muestra con su tamaño real.
     */
    protected double porcentaje = 0.0;

    //Esta proporción es la que habría que multiplicar al tamaño de la
    //foto para que entre completa en la pantalla (aprovechando al
    // máximo la pantalla sin cortar la foto)
    private double proporcion;    

    protected long altoFoto;
    protected long anchoFoto;
    private Image foto;
    protected Offset offset;
    protected Config config;
    private String info;

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

//todo debe ser abstracta
    /**
     * Intervalo de tiempo entre frame y frame en ms
     * @return
     */
    public long getIntervaloDeTiempo(){
        return 1000 / config.getFramesPorSegundo();
    }

    public void setFoto(Image foto) {
        this.foto = foto;
        anchoFoto = foto.getWidth(null);
        altoFoto = foto.getHeight(null);
    }

    public void pintar(Graphics graphics) {
        //si la imagen no ocupa toda la pantalla =>
        // primero pinto rectángulos de negro donde corresponda.
        double scW = screenSize.getWidth();
        double scH = screenSize.getHeight();
        graphics.setColor(Color.black);

        if (posY > 0){
            //pinto rectángulo arriba
            graphics.fillRect(0,0, (int) scW, posY);
        }

        //si hay espacio abajo
        if (posY + alturaAMostrarConZoom  < scH){
            //pinto rectángulo abajo
            graphics.fillRect(0,posY + alturaAMostrarConZoom, (int) scW,
                    (int) (scH - posY - alturaAMostrarConZoom));
        }

        if (posX > 0){
            //pinto rectángulo a la izq
            graphics.fillRect(0,0, posX, (int) scH);
        }

        if (posX + anchoAMostrarConZoom < scW){
            //pinto rectángulo a la der
            graphics.fillRect(posX + anchoAMostrarConZoom, 0,
                    (int) (scW -  posX - anchoAMostrarConZoom), (int) scH);
        }

        //fin pintada de recuadros negros

        graphics.drawImage(foto, posX, posY,
                anchoAMostrarConZoom, alturaAMostrarConZoom, null);
        mostrarInfo(graphics);
        graphics.dispose();
    }

    private void mostrarInfo(Graphics g){
        if (!mostrarInfo)
            return ;
        String msg = info + "   |   " +
                new BigDecimal(proporcion*100*offset.getZoom()).intValue() + "%";
        double y = screenSize.getHeight();
        double w = screenSize.getWidth();

        g.setColor(Color.WHITE);
        int alt = 15;
        g.fillRect(0, (int)y-alt, (int)w, alt);
        g.setColor(Color.BLACK);
        g.drawLine(0, (int)y-alt, (int)w, (int)y-alt);        
        g.drawString(msg, 10, (int) y-3);
    }

    public void calcularSize(){
        double scH = screenSize.getHeight();
        double scW = screenSize.getWidth();
        double proporcionH = scH / altoFoto;
        double proporcionW = scW / anchoFoto;

        //Calculo la proporción entre el tamaño de la pantalla y la foto
        if (proporcionW > 1 && proporcionH > 1){//si la pantalla es más grande que la foto
            //dejo el tamaño original de la foto
            proporcion = 1;
        }
        else{
            proporcion = Math.min(proporcionW, proporcionH);
        }

        proporcion += (1 - proporcion) * porcentaje;
        //Si la proporción es = a 1 => la foto no se achica
        //Si la proporción es = a Math.min(proporcionW, proporcionH)
        //  => la foto se ajusta a la pantalla

        anchoAMostrar = (int) (anchoFoto * proporcion);
        alturaAMostrar = (int) (altoFoto * proporcion);

        //si la imagen es más grande que la pantalla
        if (anchoFoto > scW || altoFoto > scH){
            //y no se está aprovechando la altura ni el ancho
            if (anchoAMostrar < scW && alturaAMostrar < scH){
                //busco el menor de los ajustes que puedo hacer
                double ajusteW = scW / anchoAMostrar;
                double ajusteH = scH / alturaAMostrar;
                double ajuste = Math.min(ajusteH, ajusteW);
                anchoAMostrar *= ajuste;
                alturaAMostrar *= ajuste;
            }
        }

        anchoAMostrarConZoom = (int) (anchoAMostrar * offset.getZoom());
        alturaAMostrarConZoom = (int) (alturaAMostrar * offset.getZoom());

        //centrado
        posX = (int) ((scW - anchoAMostrarConZoom)/2);
        posY = (int) ((scH - alturaAMostrarConZoom)/2);
        ajustarXYSegunOffset();
    }

    protected void ajustarXYSegunOffset(){
        posX += offset.getOffsetHorizontal();
        posY += offset.getOffsetVertical();
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    public void setMostrarInfo(boolean b) {
        this.mostrarInfo = b;
    }
    public boolean getMostrarInfo() {
        return mostrarInfo;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
