package p.gui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToolTipUI;
import java.awt.*;

/**
 * User: JPB
 * Date: Jan 28, 2008
 * Time: 11:56:48 AM
 */
public class ImageToolTipUI extends MetalToolTipUI {
    private Image m_image;
    private int tTWide = 150;
    private int tTWHeight = 150;

    public ImageToolTipUI(Image image) {
        m_image = image;
    }

    public ImageToolTipUI(Image image, Double proporcionHW) {
        m_image = image;
        //para mantener las proporciones
        if (proporcionHW != null)
            tTWHeight = (int) (proporcionHW * tTWide);
    }

    /**
     * This method is overriden from the MetalToolTipUI
     * to draw the given image and text
     */
    public void paint(Graphics g, JComponent c) {
//        FontMetrics metrics = c.getFontMetrics(g.getFont());
//        //Dimension size = c.getSize();
//        //g.setColor(c.getBackground());
//        //g.fillRect(0, 0, size.width, size.height);
//        g.setColor(c.getForeground());
//
//        g.drawString(((JToolTip) c).getTipText(), 3, 15);


        g.drawImage(m_image, 0, 0, tTWide, tTWHeight, c);
    }

    /**
     * This method is overriden from the MetalToolTipUI
     * to return the appropiate preferred size to size the
     * ToolTip to show both the text and image.
     */
    public Dimension getPreferredSize(JComponent c) {
//        FontMetrics metrics = c.getFontMetrics(c.getFont());
//        String tipText = ((JToolTip) c).getTipText();
//        if (tipText == null) {
//            tipText = "";
//        }
//
//        int width = SwingUtilities.computeStringWidth(metrics, tipText);
//        int height = metrics.getHeight() + m_image.getHeight(c) + 6;
//
//        if (width < m_image.getWidth(c)) {
//            width = m_image.getWidth(c);
//        }

        return new Dimension(tTWide, tTWHeight);
    }
}

