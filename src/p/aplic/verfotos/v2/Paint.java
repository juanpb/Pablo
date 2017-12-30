package p.aplic.verfotos.v2;

import java.awt.*;

/**
 * User: JPB
 * Date: Jun 19, 2008
 * Time: 1:58:47 PM
 */
public class Paint {
    protected int altura;
    protected  int ancho;
    protected  int posX = 0;
    protected  int posY = 0;
    protected  boolean mostrarInfo = false;

    public final  static Dimension screenSize =
            Toolkit.getDefaultToolkit().getScreenSize();

    protected long altoFoto;
    protected long anchoFoto;
    private Image foto;
    private String info;


    public void setFoto(Image foto) {
        this.foto = foto;
        anchoFoto = foto.getWidth(null);
        altoFoto = foto.getHeight(null);
    }

    public void setPosicion(int x, int y, int width, int height) {
        posX = x;
        posY = y ;
        ancho = width;
        altura = height;
    }

    public void pintarFondo(Graphics graphics) {
        double scW = screenSize.getWidth();
        double scH = screenSize.getHeight();

        //pinto rectángulo de negro
        graphics.fillRect(0,0, (int) scW, (int) scH);
    }
    public void pintar(Graphics graphics) {
        graphics.drawImage(foto, posX, posY, ancho, altura, null);
        mostrarInfo(graphics);
    }

    private void mostrarInfo(Graphics g){
//        if (!mostrarInfo)
//            return ;
//        String msg = info + "   |   " +
//                new BigDecimal(proporcion*100*offset.getZoom()).intValue() + "%";
//        double y = screenSize.getHeight();
//        double w = screenSize.getWidth();
//
//        g.setColor(Color.WHITE);
//        int alt = 15;
//        g.fillRect(0, (int)y-alt, (int)w, alt);
//        g.setColor(Color.BLACK);
//        g.drawLine(0, (int)y-alt, (int)w, (int)y-alt);
//        g.drawString(msg, 10, (int) y-3);
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