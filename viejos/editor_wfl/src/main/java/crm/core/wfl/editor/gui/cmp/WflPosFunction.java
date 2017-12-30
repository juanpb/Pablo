package crm.core.wfl.editor.gui.cmp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.gui.dlg.WflDlg;
import crm.core.wfl.editor.gui.dlg.WflPosFunctionDlg;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

/**
 * User: JPB
 * Date: Jan 13, 2006
 * Time: 2:26:11 PM
 */
public class WflPosFunction extends WflConnector{
	private static final long serialVersionUID = 1L;
	private static Color backgroundForText = null;

    private static ImageIcon icon;

    public WflPosFunction(WflFunction function, WflState state) {
        this(function, state, new WflPosFunctionBObj());
    }

    public WflPosFunction(WflFunction function, WflState state,
                          WflPosFunctionBObj bo) {
        super(function, state, bo);
        
        // Si el estado asociado es un atajo, lo tengo que borrar si borro este elemento.
        // Tambien asocio esta pos funcion al estado target.
        if(state instanceof WflStateShortCut) {
        	addCascadeDeleteComponent(state);
        	((WflStateShortCut)state).getTargetState().addRelatedComponent(this);
        	((WflStateShortCut)state).getTargetState().addCascadeDeleteComponent(this);
        }
        
        init();
    }

    public WflFunction getFunction(){
        return (WflFunction) super.getSource();
    }

    public WflState getState(){
        return (WflState) super.getTarget();
    }

    private void init(){
        WflConstants c = WflConstants.getInstance();
        Color col = c.getColorProperty(WflConstants.POS_FUNCTION_COLOR);
        if (col != null)
            color = col;
        super.setBackground(color);

        col = c.getColorProperty(WflConstants.CONTAINER_BACKGROUND);
        if (col != null)
            backgroundForText = col;

        if (backgroundForText == null)
            backgroundForText = new Color(200,200,200);
    }

    protected WflDlg getDialog()
    {
        return new WflPosFunctionDlg();
    }

    public WflBObj getBObj()
    {
        WflPosFunctionBObj res = (WflPosFunctionBObj)super.getBObj();
        res.setSourceId(source.getId());
        
        if(target instanceof WflStateShortCut)
        	res.setTargetId(((WflStateShortCut)target).getTargetState().getId());
        else
        	res.setTargetId(target.getId());

        return res;
    }

    public void paint(Graphics g) {
        super.paint(g);
        WflPosFunctionBObj bo =(WflPosFunctionBObj )getBObj();
        String mt = bo.getMaskType();
        if (mt != null){
            String txt = mt;
            String v = bo.getMaskValue();
            if (v != null)
                txt += v;

            if(this.getVertices().size()==0)
            	WflUtil.centeredText((Graphics2D)g, this, txt, backgroundForText, zoom);
            else {
            	Point p1 = new Point((Point)this.getVertices().get(0));
            	Point p2 = new Point(this.pointSource.x, this.pointSource.y);
            	
            	// Busco el punto mas cercano del origen.
            	Point pMin = p1;
            	Point pMax = p2;
            	if(p1.distance(0,0) > p2.distance(0, 0)) {
            		pMin = p2;
            		pMax = p1;
            	}
            	
            	// Lo traslado relativo al comienzo de la figura.
            	pMin.x = pMin.x - this.getX();
            	pMin.y = pMin.y - this.getY();
            	pMax.x = pMax.x - this.getX();
            	pMax.y = pMax.y - this.getY();
            	
            	// Invoco pasando como ancho el ancho del cuadrado genrado por el primer vertice.
            	WflUtil.centeredText((Graphics2D)g, 
            						 pMin.x, pMin.y, pMax.x-pMin.x, pMax.y-pMin.y,
            						 txt, 
            						 backgroundForText, 
            						 zoom);
            }
        }
    }

    public static Color getBackgroundForText() {
        return backgroundForText;
    }

    public static void setBackgroundForText(Color backgroundForText) {
        WflPosFunction.backgroundForText = backgroundForText;
    }

    public static ImageIcon  createImageIcon() {
        if (icon != null) {
            return icon;
        }

        double w = 20;

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image img = gc.createCompatibleImage((int)w,(int)w,Transparency.BITMASK);

        Graphics2D g = (Graphics2D)img.getGraphics();

        g.setRenderingHints(qualityHints);
        g.setPaint(new Color(204,204,204));
        g.fillRect(0,0,(int)w,(int)w);

        //Dependiendo de la forma y tamaño de la figura calculo el zoom como
        //para que me entre en la imagen
        int w0 = 100;
        int h0 = 60;
        WflConstants c = WflConstants.getInstance();
        int i = c.getIntProperty(WflConstants.FUNCTION_WIDTH);
        if (i > 0)
            w0 = i;

        i = c.getIntProperty(WflConstants.FUNCTION_HEIGHT);
        if (i > 0)
            h0 = i;

        double m = Math.max((double)w0, h0*(double)3);
        double z = w / m;
        RoundRectangle2D figFun = new RoundRectangle2D.Double();

        double dw = z * w0;
        double dh = z * h0;
        double x = (w-dw)/2;
        double y = 0;
        figFun.setFrame(x, y, dw, dh);

        Color col = c.getColorProperty(WflConstants.FUNCTION_COLOR_SS);

        Color color1 = col.brighter();
        Color color2 = col.darker();
        float x1 = (float)w/2;

        Point p = new Point((int)x1, 0);
        Point pF = new Point((int)x1, (int)(w/3));
        GradientPaint gp = new GradientPaint(p, color1, pF, color2);
        g.setPaint(gp);
        g.fill(figFun);

        //Estado
        w0 = 100;
        h0 = 60;
        i = c.getIntProperty(WflConstants.STATE_WIDTH);
        if (i > 0)
            w0 = i;

        i = c.getIntProperty(WflConstants.STATE_HEIGHT);
        if (i > 0)
            h0 = i;

        m = Math.max((double)w0, h0*(double)3);
        z = w /m;
        Ellipse2D figState = new Ellipse2D.Double();

        dw = z * w0;
        dh = z * h0;
        x = (w-dw)/2;
        int y1 = (int)(w - w / (double)3);
        figState.setFrame(x, y1, dw, dh);

        col = c.getColorProperty(WflConstants.STATE_COLOR);

        color1 = col.brighter();
        color2 = col.darker();
        x1 = (float)w/2;

        Point pS = new Point((int)x1, y1);
        p = new Point((int)x1, (int)w);

        gp = new GradientPaint(pS, color1, p, color2);
        g.setPaint(gp);
        g.fill(figState);

        //Flecha
        g.setPaint(Color.black);
        g.drawLine(pF.x, pF.y, pS.x, pS.y);

        int d = 3;
        int[] xp = new int[]{pS.x,  pS.x - d+1,  pS.x + d};
        int[] yp = new int[]{pS.y,  pS.y - d,  pS.y - d};
        g.fillPolygon(new Polygon(xp, yp, 3));
        icon = new ImageIcon(img);
        return icon;
    }
}





