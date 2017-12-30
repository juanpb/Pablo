package crm.core.wfl.editor.gui.util;

import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * User: JPB
 * Date: Feb 6, 2006
 * Time: 1:26:07 PM
 */
public class WflProcessDefinitionPrinter implements Printable{

    private WflProcessDefinitionContainer container = null;
    public WflProcessDefinitionPrinter(WflProcessDefinitionContainer container)
    {
        this.container = container;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex)
        throws PrinterException
    {

        if (pageIndex > 0) {
          return(NO_SUCH_PAGE);
        } else {
          Graphics2D g2d = (Graphics2D)g;
          g2d.scale(0.5, 0.5);
          g2d.translate(pf.getImageableX(), pf.getImageableY());
          boolean wasBuffered = disableDoubleBuffering(container);
          container.paint(g2d);
          restoreDoubleBuffering(container, wasBuffered);
          return(PAGE_EXISTS);
        }
    }
    private boolean disableDoubleBuffering(Component c) {
        if (c instanceof JComponent == false) return false;
        JComponent jc = (JComponent)c;
        boolean wasBuffered = jc.isDoubleBuffered();
        jc.setDoubleBuffered(false);
        return wasBuffered;
    }

    private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
        if (c instanceof JComponent)
            ((JComponent)c).setDoubleBuffered(wasBuffered);
    }
}
