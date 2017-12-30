package p.gui;

import javax.swing.*;
import java.awt.*;

/**
 * User: JPB
 * Date: Jan 28, 2008
 * Time: 11:58:50 AM
 */
public class ImageToolTip extends JToolTip {
    private Double proporcionHW;

    public ImageToolTip(Image image) {
        setUI(new ImageToolTipUI(image));
    }

    public ImageToolTip(Image image, Double proporcionHW) {
        setUI(new ImageToolTipUI(image, proporcionHW));
        this.proporcionHW = proporcionHW;
    }


    public Double getProporcionHW() {
        return proporcionHW;
    }

    public void setProporcionHW(Double proporcionHW) {
        this.proporcionHW = proporcionHW;
    }
}