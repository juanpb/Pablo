package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflProcessDefinitionPrinter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflPrintAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "PRINT";
    private static final String title = "Imprimir...";

    private WflContainerControl control = null;


    public WflPrintAction(WflContainerControl control) {
        this(control, true);
    }

    public WflPrintAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_PRINT));
        this.control = control;
        String name = showText ? title : null;
        putValue(NAME, name);
        if (showText == false)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, WflShortcuts.PRINT);
    }

    public void actionPerformed(ActionEvent e) {
        print();
   }

    private void print() {
        final PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new WflProcessDefinitionPrinter(control.getContainer()));
        if (job.printDialog()) {
            try {
                new Thread(){
                    public void run(){
                        try {
                            job.print();
                        } catch (PrinterException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}