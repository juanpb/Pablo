package crm.core.wfl.editor.gui.cmp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflConnectorBObj;
import crm.core.wfl.editor.bobj.WflLocationBObj;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * Conecta dos componentes de workflow.
 * @author Cm
 */
public class WflConnector extends WflComponent implements WflComponentObserver {
	private static final long serialVersionUID = 1L;
	protected WflComponent source;
    protected WflComponent target;
    WflConnectorBObj bobj = null;
    private int distanceDobleConnector = 5;
    private Color colorHighLight;
    
    /**
     * Contiene los vertices con los puntos intermedios del conector.
     * Los valores de los vertices se definen con valores absolutos en la pantalla.
     * Hay que convertirlos a valores relativos para dibujar.
     */
    private java.util.List<Point> vertices = new ArrayList<Point>();

    /**
     * Margen usado para el tamaño del componente. Si el conector está en
     * posición vertical sería la distancia a la izquierda y a la derecha
     * del conector que conformarían el ancho del componente. Si no se usa este
     * margen no se vería la punta de la flecha.
     */
    private int margin = 10;


    private int marginOfSelected = 10;
    protected Point pointSource = null;
    protected Point pointTarget = null;
    private static int arrowWidth = 2;

    /**
     * Crea un conector que une dos componentes.
     * @param source Componente origen.
     * @param target Componente destino.
     */
    protected WflConnector(WflComponent source, WflComponent target)
    {
        this(source, target, null);
    }

    protected WflConnector(WflComponent source, WflComponent target,
                           WflConnectorBObj bo) {
        super.setSelectable(true);
        super.setDraggable(false);
        
        WflConstants c = WflConstants.getInstance();

        this.colorHighLight = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT); 
        this.source = source;
        this.target = target;
        this.bobj = bo;

        addRelatedComponent(source);
        addRelatedComponent(target);

        source.addObserver(this);
        source.addCascadeDeleteComponent(this);
        source.addRelatedComponent(this);

        target.addObserver(this);
        target.addCascadeDeleteComponent(this);
        target.addRelatedComponent(this);

        updateBounds();
        
        String a = c.getProperty(WflConstants.ANTIALIASING);
        if (a != null && a.equals("OFF"))
            useAntialisasing = false;
    }

    public WflBObj getBObj()
    {
        return bobj;
    }

    public void setBObj(WflBObj bo)
    {
        bobj = (WflConnectorBObj)bo;
    }

    public void setPosition(int x, int y) {
    	
    }
    
    /**
     * Llamado cuando se mueve el componente.
     * Aprovecho para actualizar todos los vertices si hubiera.
     * @param x Nueva posicion x
     * @param y Nueva posicion y
     */
    public void setNewPosition(int x, int y) {
    	int offSetX = (int)((this.getX()/zoom - x/zoom));
    	int offSetY = (int)((this.getY()/zoom - y/zoom));
    	
    	for(Point p : vertices) {
    		p.x -= offSetX;
    		p.y -= offSetY;
    	}
    }


    /**
     * Obtiene el punto de coneccion de un componente que se tiene que 
     * conectar a un vertice.
     * @param p Punto del vertice.
     * @param component Componente a conectar.
     * @return Pünto de coneccion del componente.
     */
    private Point getComponentConnectorPoint(Point p, WflComponent component) {
    	if(p.y > component.getMiddleBottom().getY()) {
    		return component.getMiddleBottom();
    	}
    	else if(p.y < component.getMiddleTop().getY()) {
    		return component.getMiddleTop();
    	}
    	else if(p.x < component.getMiddleLeft().getX()) {
    		return component.getMiddleLeft();
    	}
    	else if(p.x > component.getMiddleRight().getX()) {
    		return component.getMiddleRight();
    	}
    	else {
    		// Si esta arriba me da igual cualquier punto.
    		return component.getMiddleRight();
    	}
    }
    
    /**
     * Obtiene el punto mas pequeño (en x e y).
     * @param list Lista de puntos.
     * @return Punto con el x mas pequeño y el y mas pequeño.
     */
    private Point getMinPoint(List<Point> list) {
    	Point r = new Point(list.get(0).x, list.get(0).y);
    	for(Point p : list) {
    		if(p.x < r.x)
    			r.x = p.x;
    		if(p.y < r.y) 
    			r.y = p.y;
    	}
    	return r;
    }
    
    /**
     * Obtiene el punto mas grande (en x e y).
     * @param list Lista de puntos.
     * @return Punto con el x mas grande y el y mas grande.
     */
    private Point getMaxPoint(List<Point> list) {
    	Point r = new Point(list.get(0).x, list.get(0).y);
    	for(Point p : list) {
    		if(p.x > r.x)
    			r.x = p.x;
    		if(p.y > r.y) 
    			r.y = p.y;
    	}
    	return r;
    }

    /**
     * Actualiza los limites del componente.
     */
    private void updateBounds()
    {
    	List<Point> convertedVertices = getVerticesWithZoom();
    	
    	// Verifico si uno con vertices.
    	if(convertedVertices.size()>0) {
    	    pointSource = getComponentConnectorPoint(convertedVertices.get(0), this.source);
  	    	pointTarget = getComponentConnectorPoint(convertedVertices.get(convertedVertices.size()-1), this.target);
    	}
    	else {
    		// Logica vieja, reever.
            boolean isDC = isDoubleConnection();
            Point souB = source.getMiddleBottom();
            Point tarT = target.getMiddleTop();
            if (souB.y <= tarT.y){
                //El Source arriba y el Target abajo
                if (isDC){
                    //los corro un cacho a la izquierda
                    pointTarget = new Point(tarT.x - distanceDobleConnector, tarT.y);
                    pointSource = new Point(souB.x - distanceDobleConnector, souB.y);
                }
                else{
                    pointTarget = tarT;
                    pointSource = souB;
                }
            }
            else{
                Point souT = source.getMiddleTop();
                Point tarB = target.getMiddleBottom();
                if (tarB.y <= souT.y){
                    //El Target arriba y el Source abajo
                    if (isDC){
                        //los corro un cacho a la derecha
                        pointTarget = new Point(tarB.x + distanceDobleConnector, tarB.y);
                        pointSource = new Point(souT.x + distanceDobleConnector, souT.y);
                    }
                    else{
                        pointTarget = tarB;
                        pointSource = souT;
                    }
                }
                else{
                    //Solapados
                    if (souT.x <= tarB.x){
                        //El Target a la derecha y el Source a la izquierda
                        Point souMR = source.getMiddleRight();
                        Point tarML = target.getMiddleLeft();
                        if (isDC){
                        //los corro un cacho hacia arriba
                            pointTarget = new Point(souMR.x, souMR.y - distanceDobleConnector);
                            pointSource = new Point(tarML.x, tarML.y - distanceDobleConnector);
                        }
                        else{
                            pointSource = souMR;
                            pointTarget = tarML;
                        }
                    }
                    else{
                        //El Target a la izquierda y el Source a la derecha
                        Point souML = source.getMiddleLeft();
                        Point tarMR = target.getMiddleRight();
                        if (isDC){
                        //los corro un cacho hacia abajo
                            pointTarget = new Point(souML.x, souML.y + distanceDobleConnector);
                            pointSource = new Point(tarMR.x, tarMR.y + distanceDobleConnector);
                        }
                        else{
                            pointSource = souML;
                            pointTarget = tarMR;
                        }
                    }
                }
            }
    	}

    	List<Point> allPoints = new ArrayList<Point>(convertedVertices);
    	allPoints.add(pointSource);
    	allPoints.add(pointTarget);
    	
    	Point minPoint = getMinPoint(allPoints);
    	Point maxPoint = getMaxPoint(allPoints);
    	
        int y = minPoint.y;
        int y2 = maxPoint.y;
        int x = minPoint.x;
        int x2 = maxPoint.x;
        
        int m = (int) (margin * zoom);
        super.setLocation(x - m, y - m);
        setSize(x2 - x + 2 * m, y2 - y + 2 * m);
        repaint();
    }
    

    /**
     *
     * @return true si este conector une componentes que además están conectados por otro conector.
     */
    private boolean isDoubleConnection()
    {
        java.util.List rc = source.getRelatedComponents(WflConnector.class);
        for (int i = 0; i < rc.size(); i++)
        {
            WflConnector c = (WflConnector) rc.get(i);
            if (c == this)
                continue;
            if ((c.getSource().equals(source) && c.getTarget().equals(target))
                    || (c.getSource().equals(target) && c.getTarget().equals(source)))
                return true;
        }
        return false;
    }

    /**
     * Repinta el conctor.
     * @param g Grafico donde se pintara.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if (useAntialisasing){
            RenderingHints qualityHints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints(qualityHints);
        }

        if (super.isInParallelism()){
            WflConstants c = WflConstants.getInstance();
            Color cip = c.getColorProperty(WflConstants.CONNECTOR_IN_PARALlELISM_COLOR);
            if (cip == null)
                cip = Color.red;
            g2d.setPaint(cip);
            setToolTipText(getTooltip());
        }
        else {
            setToolTipText((String)null);
            g2d.setPaint(color);
        }
        
        int hightlight = WflComponentHighlighter.getHighlightLevel(this);

        if(hightlight>0) {
        	g2d.setPaint(colorHighLight.darker());
        }

        
        // Tengo que hacer los dibujos teniendo en cuenta los puntos intermedios.
        Point initialPoint = new Point(pointSource.x, pointSource.y);        
        Point finalPoint = new Point(pointTarget.x, pointTarget.y);
        
        for(Point p : getVerticesWithZoom()) {
            g2d.drawLine(initialPoint.x-this.getX(), initialPoint.y-this.getY(), p.x-this.getX(), p.y-this.getY());
            
            if(hightlight>0)
            	WflUtil.drawThickLine(g2d, initialPoint.x-this.getX(), initialPoint.y-this.getY(), p.x-this.getX(), p.y-this.getY());
            initialPoint = p;
        }
        drawArrow(g2d, 	initialPoint.x-this.getX(), 
        				initialPoint.y-this.getY(), 
        				finalPoint.x-this.getX(), 
        				finalPoint.y-this.getY(), 
        				4 * zoom,
        				hightlight>0);        
        
        if(isSelected())
            paintAsSelected(g2d);
    }

    private String getTooltip() {
        String res = "";
        if (isInParallelism())
            res += super.getParallelism();
        return  res;
    }

    public void drawArrow(Graphics2D g2d, int xCenter, int yCenter,
            int x, int y, double stroke, boolean thick) {
    	drawArrow(g2d, xCenter, yCenter, x, y, stroke);    	
    	if(thick) {
    		drawArrow(g2d, xCenter-1, yCenter, x-1, y, stroke);
    		drawArrow(g2d, xCenter+1, yCenter, x+1, y, stroke);
    		drawArrow(g2d, xCenter, yCenter-1, x, y-1, stroke);
    		drawArrow(g2d, xCenter, yCenter+1, x, y-1, stroke);    		
    	}
    	
    		
    }

    public void drawArrow(Graphics2D g2d, int xCenter, int yCenter,
                          int x, int y, double stroke) {
        double aDir=Math.atan2(xCenter-x,yCenter-y);
        g2d.drawLine(x,y,xCenter,yCenter);
        g2d.setStroke(new BasicStroke(1f));                 // make the arrow head solid even if dash pattern has been specified
        Polygon tmpPoly=new Polygon();
//        int i1=12+(int)(stroke*2);

        int i1= (int) (arrowWidth * zoom +(int)(stroke*2));
//        int i2=6+(int)stroke;                           // make the arrow head the same size regardless of the length length
        tmpPoly.addPoint(x,y);                          // arrow tip
        tmpPoly.addPoint(x+xCor(i1,aDir+.5),y+yCor(i1,aDir+.5));
        // tmpPoly.addPoint(x+xCor(i2,aDir),y+yCor(i2,aDir));
        tmpPoly.addPoint(x+xCor(i1,aDir-.5),y+yCor(i1,aDir-.5));
        tmpPoly.addPoint(x,y);                          // arrow tip
        g2d.drawPolygon(tmpPoly);
        g2d.fillPolygon(tmpPoly);                       // remove this line to leave arrow head unpainted
     }
     private static int yCor(int len, double dir) {
         return (int)(len * Math.cos(dir));
     }
     private static int xCor(int len, double dir) {
         return (int)(len * Math.sin(dir));
     }


    /**
     * Llamado cada vez que cambia un componente que observo.
     * @param o Comonente observado.
     */
    public void update(WflComponent o) {
        updateBounds();
    }

    /**
     * Es invocado antes de ser removido del contenedor. 
     */
    public void beforeRemove()
    {
        source.removeObserver(this);
        target.removeObserver(this);
        source.removeCascadeDeleteComponent(this);
        source.removeRelatedComponent(this);
        target.removeCascadeDeleteComponent(this);
        target.removeRelatedComponent(this);
    }

    /**
     * Pinta un rectángulo bordeando al componente para indicar que está
     * seleccionado
     * @param g2d Graphics2D
     */
    protected void paintAsSelected(Graphics2D g2d) {
    	
        // Tengo que hacer los dibujos teniendo en cuenta los puntos intermedios.
        Point initialPoint 	= new Point(pointSource.x, pointSource.y);        
        Point finalPoint 	= new Point(pointTarget.x, pointTarget.y);

        Stroke oldStroke = g2d.getStroke();

        g2d.setStroke(dottedStroke);
        g2d.setPaint(selectedColor);
        List<Point> convertedVertices = getVerticesWithZoom();
        for(Point p : convertedVertices) {
            g2d.drawLine(initialPoint.x-this.getX(), initialPoint.y-this.getY(), p.x-this.getX(), p.y-this.getY());
            initialPoint = p;
        }
        g2d.drawLine(initialPoint.x-this.getX(), initialPoint.y-this.getY(), finalPoint.x-this.getX(), finalPoint.y-this.getY());

        g2d.setStroke(oldStroke);
    	
        List<Point> list = new ArrayList<Point>();
        list.addAll(convertedVertices);
        list.add(initialPoint);
        list.add(finalPoint);
        Dimension dim = new Dimension(widthSelected, widthSelected);
        Rectangle2D rect = new Rectangle2D.Double();
        int m = widthSelected/2;
        for (Point pAbsolute : list) {
        	Point p = new Point();
        	p.x = pAbsolute.x - this.getX() - m;
        	p.y = pAbsolute.y - this.getY() - m;
            rect.setFrame(p, dim);
            g2d.setPaint(selectedColor);
            g2d.fill(rect);
            g2d.setPaint(Color.black);
            g2d.draw(rect);
        }
    }


    /**
     * Calcula si el punto representado por los parámetros está dentro de este
     * conector.
     * @param x
     * @param y
     * @return true si está adentro, false si no
     */
    public boolean contains(int x, int y) {
        //Se considera que un punto pertenece al conector si la distancia entre
        //la línea del conector y el punto recibido como parámetro es menor que
        //marginOfSelected

        Point initialPoint 	= new Point(pointSource.x, pointSource.y);        
        Point finalPoint 	= new Point(pointTarget.x, pointTarget.y);
        List<Point> convertedVertices = getVerticesWithZoom();
        boolean acceptedDistance = false;
        for(Point p : convertedVertices) {
            Line2D line = new Line2D.Double(initialPoint.x-this.getX(), initialPoint.y-this.getY(), p.x-this.getX(), p.y-this.getY());
            if(line.ptLineDist(x, y) < marginOfSelected) 
            	acceptedDistance = true;
            initialPoint = p;            
        }
        Line2D line = new Line2D.Double(initialPoint.x-this.getX(), initialPoint.y-this.getY(), finalPoint.x-this.getX(), finalPoint.y-this.getY());
        if(line.ptLineDist(x, y) < marginOfSelected) 
        	acceptedDistance = true;

        //Con 'super.contains(x, y)' calculo que esté dentro del rectángulo
        // formado por este componente ya que 'line.ptLineDist(x, y);' devuelve
        // the distance between thespecified point and the closest point on the
        // infinitely-extended line defined by this <code>Line2D</code>
        return  acceptedDistance && super.contains(x, y);
    }

    public String getId(){
        return source.getId()+ "-" + target.getId();
    }

    public WflComponent getSource()
    {
        return source;
    }

    public WflComponent getTarget()
    {
        return target;
    }

    public void setId(String id){
        //No hago nada porque el id del contector depende de sus extremos
    }

	/**
	 * Devuelve el/la vertices.
	 * @return el/la vertices.
	 */
	public List<Point> getVertices() {
		return vertices;
	}
	
	/**
	 * Agrega un vertice a la lista.
	 * Asume que los valores tienen zoom, realiza la conversion a valores sin zoom.
	 * @param x Valor x con zoom.
	 * @param y Valor y con zoom.
	 */
	public void addVertice(int x, int y) {
		Point pointToAddNoZoom = new Point((int)(x/zoom), (int)(y/zoom));
		// Si no hay vertices lo agrego directamente.
		if(vertices.size()==0)
			getVertices().add(pointToAddNoZoom);
		else {
			List<Point> verticesWithZoom = getVerticesWithZoom();
			
			// Me fijo si entra en los extremos.
			Line2D initialLine = new Line2D.Double(new Point(this.pointSource.x, this.pointSource.y), 
															 verticesWithZoom.get(0));
			Line2D finalLine   = new Line2D.Double(new Point(this.pointTarget.x, this.pointTarget.y), 
															 verticesWithZoom.get(verticesWithZoom.size()-1));
			if(initialLine.ptLineDist(x, y) < marginOfSelected)
				vertices.add(0, pointToAddNoZoom);
			else if(finalLine.ptLineDist(x, y)< marginOfSelected)
				vertices.add(pointToAddNoZoom);
			else {
				// Tengo que ver en cual de las lineas intermedias encaja.
				for(int i=0; i<verticesWithZoom.size()-1; i++) {
					Line2D middleLine   = new Line2D.Double(verticesWithZoom.get(i), verticesWithZoom.get(i+1));
					if(middleLine.ptLineDist(x, y)< marginOfSelected) {
						vertices.add(i+1, pointToAddNoZoom);
						break;
					}
				}
			}
			
		}
	}

	/**
	 * Devuelve el/la vertices.
	 * Los valores de los vertices almacenados no tienen zoom.
	 * @return el/la vertices.
	 */
	public java.util.List<Point> getVerticesWithZoom() {
		List<Point> r = new ArrayList<Point>();
		for(Point p : vertices) {
			r.add(new Point((int)(p.x * zoom), (int)(p.y * zoom)));
		}
		return r;
	}

	/**
	 * Setea el/la vertices.
	 * Los valores de los vertices almacenados no tienen zoom. 
	 * @param vertices El/la vertices a setear.
	 */
	public void setVertices(List<Point> vertices) {
		this.vertices = vertices;
	}

	/**
	 * Remueve el vertice mas cercano.
	 * Asume que los valores tienen zoom.
	 * @param x Valor x con zoom.
	 * @param y Valor y con zoom.
	 */
	public void removeNearestVertice(int x, int y) {
		Point pointToRemove = getNearestVertice(x, y);
		if(pointToRemove!=null) {
			vertices.remove(pointToRemove);
		}
	}
	
	/**
	 * Obtiene el vertice mas cercano.
	 * Asume que los valores tienen zoom.
	 * @param x Valor x con zoom.
	 * @param y Valor y con zoom.
	 */
	private Point getNearestVertice(int x, int y) {
		Point point = new Point((int)(x/zoom), (int)(y/zoom));		
		double minimum = Double.MAX_VALUE;
		Point candidate = null;
		for(Point p : vertices) {
			if(p.distance(point)<minimum) {
				candidate = p;
				minimum = p.distance(point);
			}
		}
		return candidate;
	}

	/**
	 * Traslada el vertice mas cercano a la posicion indicada.
	 * @param x Valor x con zoom.
	 * @param y Valor y con zoom.
	 */
	public void translateVertice(int x, int y, int offSetX, int offSetY) {
		Point verticeToMove = getNearestVertice(x, y);
		if(verticeToMove!=null) {
			verticeToMove.x = (int)((x+offSetX)/zoom);
			verticeToMove.y = (int)((y+offSetY)/zoom);
	    	updateBounds();			
            repaint();			
		}
	}    
	

    public WflLocationBObj getWflLocation(){
        WflLocationBObj bo = super.getWflLocation();
        bo.setVertices(this.vertices);
        return bo;
    }	
    
    
    public void setWflLocation(WflLocationBObj bo){
    	super.setWflLocation(bo);
    	vertices = bo.getVertices();
    	updateBounds();
        repaint();
    }

}
