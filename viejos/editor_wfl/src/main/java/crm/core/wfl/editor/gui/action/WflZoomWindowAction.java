package crm.core.wfl.editor.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;

import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * Action para activar o desactivar la venana de zoom.
 */
public class WflZoomWindowAction extends AbstractAction implements WindowListener {
	private static final long serialVersionUID = 1L;
	private static final String NOMBRE = "Ventana de Zoom";

    private WflContainerControl control = null;
    
    

    public WflZoomWindowAction(WflContainerControl control, boolean showText) {
    	super(NOMBRE, WflUtil.createImageIcon(WflConstants.ICON_ZOOM_WINDOW));
        this.control = control;

        if(!showText) {
        	putValue(NAME, null);
        	putValue(SHORT_DESCRIPTION, NOMBRE);
        }
        else {
        	putValue(SHORT_DESCRIPTION, NOMBRE);
        }
        putValue(ACCELERATOR_KEY, WflShortcuts.ZOOM);

        control.getZoomFrame().addWindowListener(this);
    }

    public void actionPerformed(ActionEvent e) {
    	if(!control.isZoomFrameActive()) {
    		control.setZoomFrameActive(true);
    	}
    	else {
    		control.setZoomFrameActive(false);
    	}
    }

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowActivated(WindowEvent e) {}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowClosed(WindowEvent e) {
		control.setZoomFrameActive(false);
	}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowClosing(WindowEvent e) {
		control.setZoomFrameActive(false);
	}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowDeactivated(WindowEvent e) {	}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowDeiconified(WindowEvent e) {}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowIconified(WindowEvent e) {}

    /**
     * Evento de windows listener.
     * @param e Evento.
     */
	public void windowOpened(WindowEvent e) {
	}
}
