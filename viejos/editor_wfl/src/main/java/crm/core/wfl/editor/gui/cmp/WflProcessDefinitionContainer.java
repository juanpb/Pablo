package crm.core.wfl.editor.gui.cmp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import crm.core.wfl.editor.gui.util.WflConstants;

/**
 * Contenedor de un proceso de negocio.
 * @author Cm
 */
public class WflProcessDefinitionContainer extends JPanel
        implements WflComponentObserver, MouseListener, MouseWheelListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private static final int MOUSE_WHEEL_ACELERATOR = 40;
	private static final int GOTO_COMPONENT_INSET = 50;
	
    /**
     * Punto usado cuando se aprieta el botón izquierdo del mouse y, sin
     * soltarlo, se mueve. Este es uno de los puntos que se usan para dibujar un
     * rectángulo, entre <code>startSelection</code>  y
     * <code>endSelection</code> y seleccionar todos los componentes que están
     * adentro del rectángulo.
     */
    private Point startSelection;
    private Point endSelection;
    
    /**
     * Creo una dotted stroke
     */
    protected Stroke dottedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 10, new float[] { 4, 4 }, 0);
    private WflContainerControl control = null;
    private double zoom = 1;

    /**
     * View port donde se muestra este panel.
     */
    private JViewport associatedViewport = null;
    
    private JScrollPane associatedScrollPane = null;
    
    /**
     * Constructor vacio.
     */
    public WflProcessDefinitionContainer()
    {
        super(null);
        init();
    }

    private void init() {
        control = new WflContainerControl();
        control.setContainer(this);
        addListener();

        // Requiero el foco para el manejo de teclado.
        setFocusable(true);
        this.requestFocusInWindow();

        WflConstants c = WflConstants.getInstance();
        Color col = c.getColorProperty(WflConstants.CONTAINER_BACKGROUND);
        if (col != null){
            setBackground(col);
        }
    }

    private void addListener() {
        addMouseListener(this);
        addMouseListener(control);
        addMouseMotionListener(this);        
        addMouseWheelListener(this);
    }

    /**
     * Marca todos los componentes como no seleccionados
     */
    private void cleanSelection() {
        Component[] components = super.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if (c instanceof WflComponent){
                ((WflComponent)c).setSelected(false);
            }
        }
    }

    /**
     * Agrega un componente al container
     * @param component Componente a agregar
     */
    public void add(WflComponent component)
    {
        internalAdd(component);
        cleanSelection();
        repaint();        
    }

    /**
     * Agrega un componente al container.
     * @param component Componente a agregar.
     */
    private void internalAdd(WflComponent component)
    {
        component.addObserver(this);
        component.addMouseListener(control);
        component.addMouseMotionListener(control);
        component.setZoom(zoom);
        super.add(component);
    }

    public WflComponent getWflComponent(String id){
        if (id == null)
            return null;
        List list = getWflComponents();
        for (int i = 0; i < list.size(); i++) {
            WflComponent c = (WflComponent) list.get(i);
            if (id.equals( c.getId()))
                return c;
        }
        return null;
    }

    public WflComponent getWflComponent(int volatileId){
        List list = getWflComponents();
        for (int i = 0; i < list.size(); i++) {
            WflComponent c = (WflComponent) list.get(i);
            if (volatileId ==  c.getVolatilId())
                return c;
        }
        return null;
    }

    /**
     * Agrega una lista de componentes.
     * @param list Lista de componentes a agregar.
     */
    public void add(List list)
    {
        Iterator i = list.iterator();
        while(i.hasNext())
            internalAdd((WflComponent)i.next());
        cleanSelection();
        repaint();
    }
    
    
    /**
     * Remueve todos los componentes que figuran en la lista.
     * @param components Lista de componente a remover
     */
    public void remove(List components)
    {
        Iterator i = components.iterator();
        while (i.hasNext())
            remove((WflComponent)i.next());
    }
    
    /**
     * Remueve un componente al container.
     * @param component Componente a remover
     */
    public void remove(WflComponent component)
    {
        // Borro los componentes en cascadas
        Stack<WflComponent> stack = new Stack<WflComponent>();
        stack.add(component);
        while(!stack.isEmpty())
        {
            WflComponent current = (WflComponent)stack.pop();
            stack.addAll(current.getCascadeDeleteComponents());
            current.removeObserver(this);
            current.beforeRemove();
            super.remove(current);
        }
        repaint();
    }

    public void repaint() {
        resetPreferredSize();
        super.repaint();
    }

    /**
     * Recalcula el tamaño del container.
     */
    private void resetPreferredSize()
    {
        List wc = getWflComponents();
        int width = 0;
        int height = 0;
        
        for(int i=0; i < wc.size(); i++){
            WflComponent c = (WflComponent) wc.get(i);
            if(c.getX() + c.getWidth() > width)
                width = c.getX() + c.getWidth();
            if(c.getY() + c.getHeight() > height)
                height = c.getY() + c.getHeight();
        }
        setPreferredSize(new Dimension(width+50, height+50));
        revalidate();
    }

    /**
     * Se invoca cada vez que se cambia un componente de lugar.
     */
    public void update(WflComponent o) {
        resetPreferredSize();
    }

    /**
     * Obtiene todos los componentes seleccionados de un tipo determinado.
     * @param type Tipo de componente a buscar
     * @return Lista de componentes de ese tipo que fueron seleccionado.
     */
    public List<WflComponent> getSelectedComponents(Class type)
    {
        List<WflComponent> r = new ArrayList<WflComponent>();
        Iterator i = getSelectedComponents().iterator();
        while(i.hasNext())
        {
            WflComponent c = (WflComponent)i.next();
            if(type.isInstance(c))
                r.add(c);
        }
        return r;
    }
    
    /**
     * Obtiene todos los componentes que fueron seleccionados.
     * @return Lista de WflComponent
     */
    public List<WflComponent> getSelectedComponents()
    {
        List<WflComponent> res = new ArrayList<WflComponent>();
        List<WflComponent> r = getWflComponents();
        for (int i = 0; i < r.size(); i++) {
            WflComponent wc = r.get(i);
            if(wc.isSelected())
                res.add(wc);
        }
        return res;
    }
    
    /**************************************************************************/
    /** manejo de eventos del mouese                                          */
    /**************************************************************************/
    
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            startSelection = e.getPoint();
    }
    
    /**
     * Hace que el componente indicado quede visible en el 
     * viewport.
     * El componente se centra en la pantalla.
     * @param component Componente que se quiere ver.
     */
    public void viewComponent(WflComponent component) {
    	int gapX = associatedScrollPane.getSize().width / 2 - component.getWidth() / 2;;
    	int gapY = associatedScrollPane.getSize().height / 2 - component.getHeight() / 2;;
    	int x = Math.max(component.getX()-gapX, GOTO_COMPONENT_INSET);
    	int y = Math.max(component.getY()-gapY, GOTO_COMPONENT_INSET);
		associatedViewport.setViewPosition(new Point(x,y));
		associatedViewport.invalidate();
		associatedViewport.repaint();
    }

    public void mouseClicked(MouseEvent e) {
  	
    	// Si estoy en modo zoom, con un zoom diferente de uno
    	// y da doble click, tengo que poner el zoom en 1 y moverme
    	// para mostrar esa zona.
    	
    	/* TODO -> QUEDA PENDIENTE VER COMO SE PUEDE HACER ESTO...
    	 * 
    	if(	e.getClickCount()==2 && 
    		control.isZoomFrameActive() && 
    		control.getZoom()!=1.0) {
    		
        	// Tengo que obtener las coordenadas del click sin el zoom.
    		int x = (int)(e.getX() / control.getZoom());
    		int y = (int)(e.getY() / control.getZoom());
    		
    		control.setZoomFrameActive(false);
    		control.setZoom(1);
    		invalidate();
    		associatedViewport.setViewPosition(new Point(x,y));
    		
    		while(associatedViewport.getViewPosition().x != x ||
    				associatedViewport.getViewPosition().y !=y) {
    			try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				associatedViewport.setViewPosition(new Point(x,y));
    		}
			associatedViewport.invalidate();
			associatedViewport.repaint();
    	}
    	*/
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1){

            //si se movió el mouse desde que se hizo mousePressed => marco
            //como seleccionado todos los componentes que quedaron adentro del
            //rectángulo formado
            if (endSelection != null){
                int[] ps = getRectangleForSelection();
                mark(ps[0], ps[1], ps[2], ps[3]);
            }
            startSelection = null;
            endSelection = null;
            repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    
    /**
     * Se llama cuando se draguea el mouse
     * @param e Evento de mouse
     */
    public void mouseDragged(MouseEvent e) {
        if (startSelection != null){
            endSelection = e.getPoint();
            repaint();
        }
    }


    /**************************************************************************/
    /** fin manejo de eventos del mouese                                      */
    /**************************************************************************/


    public void paint(Graphics g) {
        super.paint(g);
        if(startSelection!=null && endSelection!=null)
        {
            //Mientras se mueve el mouse con el botón izquierdo apretado
            //se va dibujando un rectángulo
            Graphics2D g2d = (Graphics2D)g;

            RenderingHints qualityHints = new
                                RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                               RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints(qualityHints);
            g2d.setPaint(Color.black);
            g2d.setStroke(dottedStroke);
            int[] ps = getRectangleForSelection();
            g2d.drawRect(ps[0], ps[1], ps[2], ps[3]);
        }
    }

    private void mark(int x1, int y1, int width, int height) {
        Iterator i = getWflComponents().iterator();
        while(i.hasNext())
        {
            WflComponent w = (WflComponent)i.next();
            if(w.isContainedIn(x1, y1, width, height))
                w.setSelected(true);
        }
    }

    /**
     * Calcula el ancho, el alto, el x y el y del punto de arriba a la izq. del
     * rectángulo que se está formando mientras se mueve el mouse con el
     * botón izq. apretado.
     * @return los valores necesarios para formar el rectángulo
     */
    private int[] getRectangleForSelection() {
        int[] res = new int[4];

        int sx = (int)startSelection.getX();
        int sy = (int)startSelection.getY();
        int ex = (int)endSelection.getX();
        int ey = (int)endSelection.getY();

        res[0] = Math.min(sx, ex);
        res[1] = Math.min(sy, ey);
        res[2] = Math.max(sx, ex) - Math.min(sx, ex);
        res[3] = Math.max(sy, ey) - Math.min(sy, ey);

        return res;
    }

    /**
     * Borra todos los elementos del container
     */
    public void clean() {
        List comp = getWflComponents();
        remove(comp);
        resetPreferredSize();
    }


    /**
     * Devuelve una lista de WflComponent
     *
     * @return la lista de WflComponent
     */
    public List<WflComponent> getWflComponents(){
        List<WflComponent> res = new ArrayList<WflComponent>();
        Component[] components = this.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if(c instanceof WflComponent)
            {
                WflComponent wc = (WflComponent)c;
                res.add(wc);
            }
        }

        return res;
    }
    /**
     * Obtiene todos los componentes de un tipo determinado.
     * @param type Tipo de componente a buscar
     * @return Lista de componentes de ese tipo.
     */
    public List<WflComponent> getWflComponents(Class type)
    {
        List<WflComponent> res = new ArrayList<WflComponent>();
        List<WflComponent> r = getWflComponents();
        for (int i = 0; i < r.size(); i++) {
            WflComponent wc = (WflComponent) r.get(i);
            if(type.isInstance(wc))
                res.add(wc);
        }
        return res;
    }

    public WflContainerControl getControl() {
        return control;
    }

    /**
     * Cambia el tamaño y la posición de los componentes.
     * Si zoom = 1 los componentes recuperan su tamaño y posición original.
     * El zoom no es ralativo a la posición y tamaño actual sino a la original.
     *
     * @param zoom valor mayor que cero. Este valor ya debe haber sido
     * dividido por cien (por el porcentaje).
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        List wc = getWflComponents();
        for (int i = 0; i < wc.size(); i++) {
            WflComponent c = (WflComponent) wc.get(i);
            c.setZoom(zoom);
        }
    }

    public double getZoom() {
        return zoom;
    }

	/**
	 * Devuelve el/la associatedViewport.
	 * @return el/la associatedViewport.
	 */
	public JViewport getAssociatedViewport() {
		return associatedViewport;
	}

	/**
	 * Setea el/la associatedViewport
	 * @param associatedViewport El/la associatedViewport a setear.
	 */
	public void setAssociatedViewport(JViewport associatedViewport) {
		this.associatedViewport = associatedViewport;
	}

	/**
	 * Sobrescribo este metodo para poder avanzar mas con la ruedita.
	 * @param e Evento de reudita.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point currentView = associatedViewport.getViewPosition();
		currentView.y = currentView.y + e.getWheelRotation() * MOUSE_WHEEL_ACELERATOR;
		if(currentView.y<0)
			currentView.y = 0;
		associatedViewport.setViewPosition(currentView);
	}

	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * Devuelve el/la associatedScrollPane.
	 * @return el/la associatedScrollPane.
	 */
	public JScrollPane getAssociatedScrollPane() {
		return associatedScrollPane;
	}

	/**
	 * Setea el/la associatedScrollPane
	 * @param associatedScrollPane El/la associatedScrollPane a setear.
	 */
	public void setAssociatedScrollPane(JScrollPane associatedScrollPane) {
		this.associatedScrollPane = associatedScrollPane;
	}
}
