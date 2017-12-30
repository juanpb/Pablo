package crm.core.wfl.editor.gui.cmp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;


/**
 * Representa atajo (connector).
 * @author Cm
 */
public class WflStateShortCut extends WflState {
	private static final long serialVersionUID = 1L;
	public static final String PREFIX = "SHORT_CUT=";
	public static final String SOURCE_TARGET_SEPARATOR = "-";
	
    private Color colorHighLight = Color.orange;
	private WflState targetState;

    /**
     * Constructor default.
     */
    public WflStateShortCut(WflState targetState)
    {
        super((WflStateBObj)targetState.getBObj());
        this.targetState = targetState;
        
        WflConstants c = WflConstants.getInstance();        
        Color col = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT);
        if (col != null)
        	colorHighLight = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT);        
        
    }

	/* (non-Javadoc)
	 * @see crm.core.wfl.editor.gui.cmp.WflState#getBObj()
	 */
	@Override
	public WflBObj getBObj() {
		return this.targetState.getBObj();
	}

	/* (non-Javadoc)
	 * @see crm.core.wfl.editor.gui.cmp.WflState#isConsistent()
	 */
	@Override
	public boolean isConsistent() {
		return this.targetState.isConsistent();
	}

	/* (non-Javadoc)
	 * @see crm.core.wfl.editor.gui.cmp.WflState#setBObj(crm.core.wfl.editor.bobj.WflBObj)
	 */
	@Override
	public void setBObj(WflBObj bo) {
		this.targetState.setBObj(bo);
	}
	
    protected void init() {

    	WflConstants c = WflConstants.getInstance();

    	int i = c.getIntProperty(WflConstants.SHORT_CUT_WIDTH);
        if (i > 0)
            width = i;

        i = c.getIntProperty(WflConstants.SHORT_CUT_HEIGHT);
        if (i > 0)
            height = i;
        
        
        Color col2  = c.getColorProperty(WflConstants.SHORT_CUT_COLOR);
        if (col2 != null)
            color = col2;

        Color col  = c.getColorProperty(WflConstants.SELECTED_COLOR);
        if (col != null)
            selectedColor = col;

        String a = c.getProperty(WflConstants.ANTIALIASING);
        if (a != null && a.equals("OFF"))
            useAntialisasing = false;

        widthWithoutZoom = width;
        heightWithoutZoom = height;
        setSize(width,  height);
    }
	
    
    /**
     * Repinta el estado.
     * @param g Grafico donde se pintara.
     */
    public void paint(Graphics g) {

    	Graphics2D g2d = (Graphics2D)g;
        
    	if (useAntialisasing)
            g2d.setRenderingHints(qualityHints);
        
    	
        Color currentColor = color;
        
        if(((WflStateBObj)targetState.getBObj()).getCancelState().equals(WflStateBObj.CANCEL_STATE))
        	currentColor = this.targetState.getColor();
        
        int highlight = WflComponentHighlighter.getHighlightLevel(this);

        if(highlight>0)
        	currentColor = colorHighLight;
        
        Color color1 = currentColor.brighter();
        Color color2 = currentColor.darker();
        
        int arrowHeadLength = width / 4;
        int arrowHeadHeight = height / 4;
        int arrowWidth = height / 2;
        int arrowLenght = width / 4 * 3;
        Polygon poly = new Polygon();
        int x = 0;
        int y = arrowHeadHeight;
        poly.addPoint(x, y);
        x = x + arrowLenght;
        poly.addPoint(x, y);
        y = 0;
        poly.addPoint(x, y);
        x += arrowHeadLength; 
        y += arrowHeadHeight + arrowWidth / 2;;
        poly.addPoint(x, y);
        x -= arrowHeadLength;
        y += arrowHeadHeight + arrowWidth / 2;
        poly.addPoint(x, y);
        y -= arrowHeadHeight;
        poly.addPoint(x, y);
        x = 0;
        poly.addPoint(x, y);
        y = arrowHeadHeight;
        poly.addPoint(x, y);
        GradientPaint gp = new GradientPaint(0, 0, color2, width/2, height/2, color1, true);
        g2d.setPaint(gp);
        g2d.fill(poly);
        g2d.setPaint(color2);
        g2d.draw(poly);
        
        if (isSelected())
            super.paintAsSelected(g2d);

        String id = targetState.getId();
        if (id != null) {
            // WflUtil.centeredText(g2d, this, id, zoom);
        	WflUtil.centeredText(g2d, 0, -1, this.getWidth(), this.getHeight(), id, null, zoom);
        }

        if (! isConsistent()){
            g2d.setColor(Color.red);
            g2d.drawLine(0,0, width, height);
            g2d.drawLine(width, 0, 0, height);
            java.util.List cm = super.getConsistentMsg();
            setToolTipText(cm);
        }
        else
            setToolTipText(getTooltip());
    }
    
    public String getId() {
    	WflConnector connector = (WflConnector)this.getRelatedComponents().get(0);
        return PREFIX + connector.getSource().getId() + SOURCE_TARGET_SEPARATOR + targetState.getId();
    }

    public void setId(String id){
        // No hago nada ya que el Id depende del componente relacionado.
    }

	/**
	 * Devuelve el/la targetState.
	 * @return el/la targetState.
	 */
	public WflState getTargetState() {
		return targetState;
	}

}
