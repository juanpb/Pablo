package crm.core.wfl.editor.gui.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * Ventana de zoom.
 * @author cm
 *
 */
public class WflZoomFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Lugar a dejar libre al costado del frame.
	 */
	private static final int FRAME_INSET = 50;
	
	private JLabel zoomLabel = new JLabel("aa");
	
	/**
	 * Constructor vacio.
	 */
	public WflZoomFrame() {
		setAlwaysOnTop(true);
		setTitle("Zoom");
		setIconImage(WflUtil.createImageIcon(WflConstants.ICON_ZOOM_WINDOW).getImage());
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(500, 500);
		setLocation(screen.width-getWidth()-FRAME_INSET, screen.height-getHeight()-FRAME_INSET);
		getContentPane().setLayout(new BorderLayout());
		zoomLabel = new JLabel();
		getContentPane().add(zoomLabel, BorderLayout.CENTER);
	}
	
	/**
	 * Setea la nueva vista de zoom.
	 * @param zoomView Nueva Vista
	 */
	public void setZoomView(BufferedImage zoomView) {
	    // Seteo la imagen
	    zoomLabel.setIcon(new ImageIcon(zoomView));
	    zoomLabel.invalidate();
	    zoomLabel.repaint();
	}
}
