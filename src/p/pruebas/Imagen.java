package p.pruebas;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Imagen {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());

//        final File ff = new File ("D:\\P\\Fotos\\mdp\\_n\\2014-01-03_13 Juliana.jpg");
        final File ff = new File ("D:\\P\\Fotos\\mdp\\_n\\2014-01-03_13 Juliana.jpg");
        //JButton b = new JButton(new Icon() {
        JLabel b = new JLabel(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                try {
                    BufferedImage read = ImageIO.read(ff);
                    g.drawImage(read, 0,0, 111, 111,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getIconWidth() {
                return 110;
            }

            @Override
            public int getIconHeight() {
                return 110;
            }
        }  );
        //JLabel b = new JLabel( "<html><img height='" + 155 + "' width='150' src='" + x + "'>");

        JScrollPane pane = new JScrollPane(b);
        frame.getContentPane().add(BorderLayout.CENTER, pane);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}
