package crm.core.wfl.editor.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;
import crm.core.wfl.editor.gui.dlg.WflZoomFrame;

/**
 * Componente encargado de escuchar el mouse para realizar
 * el redibujo de zoom si estuviera activado.
 * @author cm
 *
 */
public class WflZoomMouseListener implements MouseListener, MouseMotionListener {

	private WflProcessDefinitionContainer container;
	
	private WflZoomFrame zoomFrame;
	
	/**
	 * Crea el listener.
	 * @param container Container.
	 */
	public WflZoomMouseListener(WflProcessDefinitionContainer container, WflZoomFrame zoomFrame) {
		this.container = container;
		this.zoomFrame = zoomFrame;
	}
	/**
	 * Mouse click.
	 * @param e Evento de mouse.
	 */
	public void mouseClicked(MouseEvent e) {	}

	/**
	 * Mouse enter.
	 * @param e Evento de mouse.
	 */
	public void mouseEntered(MouseEvent e) {	}

	/**
	 * Mouse exit.
	 * @param e Evento de mouse.
	 */
	
	public void mouseExited(MouseEvent e) {	}

	/**
	 * Mouse presed.
	 * 
	 * 
	 * @param e Evento de mouse.
	 */
	public void mousePressed(MouseEvent e) {	}

	/**
	 * Mouse released.
	 * @param e Evento de mouse.
	 */
	public void mouseReleased(MouseEvent e) {	}

	/**
	 * Mouse dragged.
	 * @param e Evento de mouse.
	 */
	public void mouseDragged(MouseEvent e) {	}

	/**
	 * Mouse moved..
	 * @param e Evento de mouse.
	 */
	public void mouseMoved(MouseEvent e) {
		if(zoomFrame.isVisible()) { 
	    	double currentZoom = container.getControl().getZoom();
	    	
	        // Debo obtener las cordenadas del mouse absolutas desde el 0,0
	        int mouseX = e.getX();
	        int mouseY = e.getY();
	        if(e.getComponent()!=container) {
	        	mouseX += e.getComponent().getX();
	        	mouseY += e.getComponent().getY();
	        }
	
	        // Llevo las coordenadas a coordenadas con zoom al 100% 
	        int x = (int)(mouseX/currentZoom);
	        int y = (int)(mouseY/currentZoom);
	
	        // Ahora debo obtener un sub grafico.
	        int zoomWidth = zoomFrame.getWidth();
	        int zoomHeight = zoomFrame.getHeight();
	        
	        // Debo tomar el punto como centro y obtener la esquina superior izq.
	        int xArea = x-(zoomWidth/2);
	        int yArea = y-(zoomHeight/2);
	        
	        // Controlo de no irme de la pantalla
	        xArea = Math.max(0, xArea);
	        yArea = Math.max(0, yArea);
	        
	        // Redefino el ancho y alto en caso de irme de la pantalla
	    	int containerWidth = (int)(container.getWidth()/currentZoom);
	    	int containerHeight = (int)(container.getHeight()/currentZoom);    	
	    	if(xArea + zoomWidth > containerWidth)
	        	xArea = containerWidth - zoomWidth; 
	        
	        if(yArea + zoomHeight > containerHeight)
	        	yArea = containerHeight - zoomHeight; 
	
	    	// Redibujo al 100%
	    	container.getControl().setZoom(1);
	    	
	    	Rectangle currentBounds = container.getBounds();
	    	container.setBounds(0, 0, containerWidth, containerHeight);
	       
	    	// Creo el grafico completo
	        BufferedImage bi = new BufferedImage(containerWidth, containerHeight, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = bi.createGraphics();
	        container.paint(g2d);
	
	        // Corto el area que me interesa
	        BufferedImage bi2 = bi.getSubimage(xArea, yArea, zoomWidth, zoomHeight);
	        g2d.dispose();
	        
	        // Seteo la imagen
	        zoomFrame.setZoomView(bi2);
	
	        // Vuelvo el zoom al valor anterior
	        container.getControl().setZoom(currentZoom);
	        container.setBounds(currentBounds);
	        container.invalidate();
	        container.repaint();
		}
	}

	
	
}
