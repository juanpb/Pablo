package p.pruebas.thumb;

import p.fotos.Exif;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Feb 22, 2010
 * Time: 11:15:19 PM
 */
public class Icono extends ImageIcon {
    private int tam = 120;
    private String filename;

    public Icono(String f) {
        super(f);
        filename = f;
    }

    public int getIconHeight() {
      return tam;
    }

    public int getIconWidth() {
      return tam;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Exif ex = new Exif(filename);
        final Image image = ex.getThumbnail();
//        javax.imageio.ImageIO.read(file);
        ex = null;
//        final Image image = getImage();
        g.drawImage(image, x, y, tam, tam, null);
    }
}


