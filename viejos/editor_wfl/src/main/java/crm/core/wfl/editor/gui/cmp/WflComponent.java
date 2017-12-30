package crm.core.wfl.editor.gui.cmp;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflLocationBObj;
import crm.core.wfl.editor.gui.dlg.WflDlg;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Superclase de todos los componentes de workflow 
 * @author Cm
 */
public abstract class WflComponent extends JComponent{
	private static final long serialVersionUID = 1L;
    protected int width = 100;
    protected int height = 60;
    protected int widthWithoutZoom = 0;
    protected int heightWithoutZoom = 0;

    protected double xWithoutZoom = 0;
    protected double yWithoutZoom = 0;
    protected double zoom = 1;

    protected static RenderingHints qualityHints = new RenderingHints(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
    
    /**
     * Se puede poner un listener default para todos los componentes que se creen.
     */
    public static MouseMotionListener DEFAULT_MOUSE_MOTION_LISTENER = null;
    
    static{
        qualityHints.put(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
    }

    protected int volatilId = WflUtil.getVolatilId();
    private boolean inParallelism = false;
    private WflComponent initialStateParallelism = null;
    private WflComponent finalStateParallelism = null;
    private String classNameParallelism = null;

    /**
     * Es el ancho y alto que tendrá cada uno de los cuadrados que se usan
     * para representar a un componente como seleccionado
     */
    protected int widthSelected = 4;
    protected int widthSelectedWithoutZoom = 4;

    /**
     * Color del recuadro con el cual se marca que un compenente está
     * seleccionado
     */
    protected Color selectedColor = Color.green;

    protected Color color = Color.black;
    protected boolean useAntialisasing = true;

    protected boolean selectable = true;
    protected boolean draggable = true;

    /**
     * Creo una dotted stroke
     */
    protected Stroke dottedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 10, new float[] { 4, 4 }, 0);

    /**
     * Observadores de este componente.
     */
    private List<WflComponentObserver> observers = new ArrayList<WflComponentObserver>();
    
    /**
     * Indica si el componente fue seleccionado.
     */
    private boolean selected = false;

    
    /**
     * Contiene una lista de los componentes a ser borrados en cascada.
     */
    private List<WflComponent> cascadeDeleteComponents = new ArrayList<WflComponent>();

    private List<WflComponent> relatedComponents = new ArrayList<WflComponent>();

    public WflComponent()
    {
        this(true, true);
    }

    public WflComponent(boolean draggable, boolean selectable)
    {
        this.draggable = draggable;
        this.selectable = selectable;
        volatilId = getNewId();
        
        if(DEFAULT_MOUSE_MOTION_LISTENER!=null)
        	addMouseMotionListener(DEFAULT_MOUSE_MOTION_LISTENER);
    }

    public abstract String getId();
    public abstract void setId(String id);

    protected int getVolatilId() {
        return volatilId;
    }

    public WflLocationBObj getWflLocation(){
        Point p = new Point((int)xWithoutZoom, (int)yWithoutZoom);
        return new WflLocationBObj(getId(), p);
    }
    
    public void setWflLocation(WflLocationBObj bo){
    	setPosition(bo.getPoint());
    }
    
    private int getNewId() {
        return volatilId++;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Indica si el comonente se encuentra seleccionado.
     * @return true si esta seleccionado.
     */
    public boolean isSelected() {
        return selected;
    }
    
    
    /**
     * Indica al componente si esta seleccionado o no.
     * Se encarga de repintar el componente.
     * @param selected true o false
     */
    public void setSelected(boolean selected) {
        if (this.selected == selected)
            return ;
        this.selected = selected;
        repaint();
    }    
    
    /**
     * Obtiene el punto medio superior
     * @return Punto medio superior.
     */
    public Point getMiddleTop()
    {
        return new Point(getX() + getWidth()/2, getY());
    }
    
    /**
     * Obtiene el punto medio derecho
     * @return Punto medio derecho.
     */
    public Point getMiddleRight()
    {
        return new Point(getX() + getWidth(), getY()+getHeight()/2);
    }

    /**
     * Obtiene el punto medio izquierdo
     * @return Punto medio izquierdo.
     */
    public Point getMiddleLeft()
    {
        return new Point(getX(), getY()+getHeight()/2);
    }

    /**
     * Obtiene el punto medio inferior
     * @return Punto medio inferior.
     */
    public Point getMiddleBottom()
    {
        return new Point(getX() + getWidth()/2, getY()+getHeight());
    }

    /**
     * Agrega un observador a este componente.
     * @param o Observador a agregar.
     */
    public void addObserver(WflComponentObserver o)
    {
        observers.add(o);
    }
    
    /**
     * Remueve un observador a este componente.
     * @param o Observador a remover.
     */
    public void removeObserver(WflComponentObserver o)
    {
        observers.remove(o);
    }

    /**
     * Avisa a todos los que lo observan que el componente cambió.
     */
    public void notifyObservers()
    {
        Iterator i = observers.iterator();
        while(i.hasNext())
        {
            WflComponentObserver o = (WflComponentObserver)i.next();
            o.update(this);
        }
    }

    /**
     * Pinta un rectángulo bordeando al componente para indicar que está
     * seleccionado
     * @param g2d Graphics2D
     */
    protected void paintAsSelected(Graphics2D g2d) {
        Rectangle2D rect = new Rectangle2D.Double(0,0, width, height);

        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(dottedStroke);
        g2d.setPaint(selectedColor);
        g2d.draw(rect);

        g2d.setStroke(oldStroke);

        Point[] pts = getPointsOfSelected();
        Dimension dim = new Dimension(widthSelected, widthSelected);
        for (int i = 0; i < pts.length; i++) {
            Point p = pts[i];
            rect.setFrame(p, dim);
            g2d.setPaint(selectedColor);
            g2d.fill(rect);
            g2d.setPaint(Color.black);
            g2d.draw(rect);
        }
    }

    /**
     * Calcula los puntos del rectángulo que se dibuja para representar a un
     * componente como seleccionado
     * @return los puntos de los rectángulos para dibujar
     */
    private Point[] getPointsOfSelected() {
        Point[] res = new Point[8];
        /**
          Estos son los puntos que se van a devolver
           0-----1-----2
           |           |
           7           3
           |           |
           6-----5-----4

         Como a partir de estos puntos se dibujarán los cuadraditos tengo que
         tener en cuenta que todos tienen que entrar dentro del rectángulo que
         va desde (0,0) hasta (width-1, height-1), por eso es que a los valores 'y'
         de los puntos 4, 5 y 6 les resto widthSelected y a los 'x' de 2, 3 y 4
         también.
         */

        int x = 0;
        int y = 0;
        Point p = new Point(x, y );
        res[0] = p;
        int ma = (width - widthSelected)/2;
        p = new Point(ma, (int)res[0].getY());
        res[1] = p;
        p = new Point(x + width -1 - widthSelected, (int)res[0].getY() );
        res[2] = p;
        int mh = (height - widthSelected) /2;
        p = new Point((int)res[2].getX(), (int)res[2].getY() + mh);
        res[3] = p;
        p = new Point((int)res[2].getX(), (int)res[2].getY() + height - 1 - widthSelected);
        res[4] = p;
        res[5] = new Point((int)res[1].getX(), (int)res[4].getY() );
        res[6] = new Point((int)res[0].getX(), (int)res[4].getY() );
        res[7] = new Point((int)res[0].getX(), (int)res[3].getY());

        return res;
    }

    /**
     * Devuelve true si el diálogo se cerró luego de apretar el botón "Aceptar"
     * o false en cualquier otro caso
     */
    public boolean showDialog() {
        WflDlg dlg = getDialog();
        if(dlg!=null)
        {
            WflBObj bo = getBObj();
            dlg.setBObj(bo);
            dlg.showMe();
            if (!dlg.wasCancel() && dlg.wasModified()){
                if(dlg.getBObj()!=null){
                    setBObj(dlg.getBObj());
                }
                WflUtility.setModified(true);
            }
            repaint();
        }
        if (dlg != null)
            return !dlg.wasCancel() ;
        else
            return false;
    }
    
    public WflBObj getBObj()
    {
        return null;
    }
    public void setBObj(WflBObj bo)
    {
    }
    
    protected WflDlg getDialog()
    {
        return null;
    }

    /**
     * Calcula el location del componente para que los parámetros recibidos
     * queden en el centro del componente.
     * Este método debe ser llamado una vez que el componente esté en el
     * container, para que el cálculo del centro se haga con el zoom ya
     * asignado (se asigna cuando se agrega al container).
     * @param x
     * @param y
     */
    public void setLocationCenter(int x, int y) {
        x -= width/2;
        y -= height/2;
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;

        setNewPosition(x, y);
    }

    public void setLocation(Point p) {
        setLocation(p.x,  p.y);
    }

    /**
     * Esta es la posición real del componente, independientemente del zoom.
     * No debe usarse desde afuera de esta clase, hay que usar:
     * setPosition(int x, int y) o
     * setNewPosition(int x, int y)
     *
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        notifyObservers();
    }

    /**
     * Esta es la posición original del componente, a la cual hay que aplicarle
     * el zoom.
     * @param p
     */
    public void setPosition(Point p){
        setPosition(p.x, p.y);
    }
    /**
     * Esta es la posición original del componente, a la cual hay que aplicarle
     * el zoom.
     * @param x
     * @param y
     */
    public void setPosition(int x, int y){
        xWithoutZoom = x;
        yWithoutZoom = y;

        x = (int) (x * zoom);
        y = (int) (y * zoom);
        setLocation(x, y);
    }

    /**
     * Esta es la posición real del componente, a la cual no se le aplica
     * el zoom.
     * Se usa cuando se mueve el componente o cuando se agrega a una determinada
     * posición, independientemente del zoom.
     *
     * @param p
     */
    public void setNewPosition(Point p){
        setNewPosition(p.x, p.y);
    }

    /**
     * Esta es la posición real del componente, a la cual no se le aplica
     * el zoom.
     * Se usa cuando se mueve el componente o cuando se agrega a una determinada
     * posición, independientemente del zoom.
     *
     * @param x
     * @param y
     */
    public void setNewPosition(int x, int y){
        if (zoom != 0){
            xWithoutZoom = x / zoom;
            yWithoutZoom = y / zoom;
        }
        setLocation(x, y);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        super.setSize(width, height);
        notifyObservers();
    }


    /**
     * Obtiene los componentes a ser borrados en cascada.
     * @return Lista de componentes a ser borrados en cascada.
     */
    public List<WflComponent> getCascadeDeleteComponents() {
        return cascadeDeleteComponents;
    }
    
    /**
     * Agrega un componente a ser borrado en cascada.
     * @param component Componente a ser borrado en cascada.
     */
    public void addCascadeDeleteComponent(WflComponent component) {
        this.cascadeDeleteComponents.add(component);
    }
    
    /**
     * Remueve un componente a ser borrado en cascada.
     * @param component Componente a remover.
     */
    public void removeCascadeDeleteComponent(WflComponent component) {
        this.cascadeDeleteComponents.remove(component);
    }    

    /**
     * Obtiene los componentes relacionados.
     * @return Lista de componentes relacionados.
     */
    public List<WflComponent> getRelatedComponents() {
        return relatedComponents;
    }

    /**
     * Obtiene todos los componentes relacionados de un tipo determinado.
     * @param type Tipo de componente a buscar
     * @return Lista de componentes de ese tipo que están relacionados.
     */
    public List<WflComponent> getRelatedComponents(Class type)
    {
        List<WflComponent> r = new ArrayList<WflComponent>();
        Iterator i = getRelatedComponents().iterator();
        while(i.hasNext())
        {
            WflComponent c = (WflComponent)i.next();
            if(type.isInstance(c))
                r.add(c);
        }
        return r;
    }


    /**
     * Agrega un componente a la lista de relacionados.
     * @param component Componente relacionado.
     */
    public void addRelatedComponent(WflComponent component) {
        relatedComponents.add(component);
    }

    /**
     * Remueve un componente de la lista de relacionados.
     * @param component Componente a remover.
     */
    public void removeRelatedComponent(WflComponent component) {
        relatedComponents.remove(component);
    }

    /**
     * Es invocado antes de ser removido del contenedor. 
     */
    public void beforeRemove() { }

    /**
     * Verifica si este componente está contenido dentro del rectangulo formado
     * por los parámetros recibidos
     * @param x1
     * @param y1
     * @param width
     * @param height
     * @return true si está totalmente contenido en el rectángulo
     *  false si no.
     */
    public boolean isContainedIn(int x1, int y1, int width, int height) {
        int x = getX();
        int y = getY();

        boolean res = x1 <= x && (x + this.width) <= (x1 + width);
        res &= y1 <= y && (y + this.height) <= (y1 + height);
        return res;
    }


    public int hashCode() {
        return volatilId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof WflComponent){
            WflComponent other = (WflComponent) obj;
            return volatilId == other.volatilId;
        }
        return false;
    }

    public boolean isInParallelism() {
        return inParallelism;
    }

    public void setToolTipText(List text) {
        StringBuffer msg = new StringBuffer();
        if (text != null) {
            for (int i = 0; i < text.size(); i++) {
                String s = (String) text.get(i);
                msg.append(s).append("\n");
            }
        }
        setToolTipText(msg.toString());
    }

    
    private static final int MAX_TOOLTIP_LENGHT = 100;
    
    public void setToolTipText(String text) {
    	if(text==null || text.trim().length()==0) {
    		super.setToolTipText(null);
    		return;
    	}
    	
    	// Lo debo recorrer por lineas, si hay lineas mayores a un maximo, tengo que partir la linea.
    	StringTokenizer st = new StringTokenizer(text, "\n");
    	StringBuffer sb = new StringBuffer();
    	while(st.hasMoreTokens()) {
    		String line = st.nextToken();
    		while(line.length()>MAX_TOOLTIP_LENGHT) {
    			int cut = line.substring(0, MAX_TOOLTIP_LENGHT).lastIndexOf(" ");
    			if(cut==-1)
    				cut = MAX_TOOLTIP_LENGHT;
    			String newLine = line.substring(0, cut+1);
    			sb.append(newLine + "\n");
    			line = line.substring(cut+1);
    		}
    		sb.append(line + "\n");
    	}
        super.setToolTipText(sb.toString());

    }
    
    /* (non-Javadoc)
	 * @see javax.swing.JComponent#createToolTip()
	 */
	@Override
	public JToolTip createToolTip() {
		WflMultiLineToolTip tip = new WflMultiLineToolTip();
        tip.setComponent(this);
        return tip;	
    }

	public boolean isConsistent(){
        return true;
    }

    public double getZoom() {
        return zoom;
    }

    /**
     * Cambia el tamaño y la posición del componente. Si zoom = 1 el componente
     * recupera su tamaño y posición original. El zoom no es ralativo a la
     * posición y tamaño actual sino a la original.
     *
     * @param zoom valor mayor que cero. Este valor ya debe hacer sido
     * dividido por cien (por el porcentaje).
     */
    public void setZoom(double zoom) {

        this.zoom = zoom;
        int x = (int)(xWithoutZoom * zoom);
        int y = (int)(yWithoutZoom * zoom);
        setLocation(x,y);

        widthSelected = (int) (widthSelectedWithoutZoom * zoom);

        setSize((int)(widthWithoutZoom *zoom), (int)(heightWithoutZoom * zoom));
    }

    public void setInParallelism(boolean b){
        setInParallelism(b, null, null, null);
    }

    public void setInParallelism(boolean b, WflComponent initialState,
                                 WflComponent finalState, String className) {
        inParallelism = b;
        initialStateParallelism = initialState;
        finalStateParallelism = finalState;
        classNameParallelism = className;
    }

    /**
     * Arma y devuelve una representación del paralelimo:
     * estado inicial - estado final - clase
     * @return una representación del paralelimo.
     */
    public String getParallelism(){
        if (!isInParallelism())
            return "";
        else{
            return "Paralelismo entre " +
                    getInitialStateParallelism().getId() + " y " +
                    getFinalStateParallelism().getId() + " con la clase " +
                    getClassNameParallelism();
        }
    }

    public WflComponent getInitialStateParallelism() {
        return initialStateParallelism;
    }

    public WflComponent getFinalStateParallelism() {
        return finalStateParallelism;
    }

    public String getClassNameParallelism() {
        return classNameParallelism;
    }

}
