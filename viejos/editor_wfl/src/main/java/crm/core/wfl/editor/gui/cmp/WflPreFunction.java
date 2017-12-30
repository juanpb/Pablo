package crm.core.wfl.editor.gui.cmp;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflPreFunctionBObj;
import crm.core.wfl.editor.gui.util.WflConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;

/**
 * User: JPB
 * Date: Jan 13, 2006
 * Time: 2:26:11 PM
 */
public class WflPreFunction extends WflConnector{
	private static final long serialVersionUID = 1L;
	private static ImageIcon icon;

    public WflPreFunction(WflState state, WflFunction function) {
        this(state, function, new WflPreFunctionBObj());
    }
    public WflPreFunction(WflState state, WflFunction function, WflPreFunctionBObj bo) {
        super(state, function, bo);
        init();
    }

    private void init(){
        WflConstants c = WflConstants.getInstance();
        Color col = c.getColorProperty(WflConstants.PRE_FUNCTION_COLOR);
        if (col != null)
            color = col;

        super.setBackground(color);
    }


    public WflBObj getBObj()
    {
        WflPreFunctionBObj res = (WflPreFunctionBObj)super.getBObj();

        res.setSourceId(source.getId());
        res.setTargetId(target.getId());
        return res;
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
        int i = c.getIntProperty(WflConstants.STATE_WIDTH);
        if (i > 0)
            w0 = i;

        i = c.getIntProperty(WflConstants.STATE_HEIGHT);
        if (i > 0)
            h0 = i;

        double m = Math.max((double)w0, h0*(double)3);
        double z = w /m;
        Ellipse2D figState = new Ellipse2D.Double();

        double dw = z * w0;
        double dh = z * h0;
        double x = (w-dw)/2;
        double y = 0;
        figState.setFrame(x, y, dw, dh);

        Color col = c.getColorProperty(WflConstants.STATE_COLOR);
        if (col == null)
            col = Color.yellow;

        Color color1 = col.brighter();
        Color color2 = col.darker();
        float x1 = (float)w/2;

        Point p = new Point((int)x1, 0);
        Point pS = new Point((int)x1, (int)(w/(double)3));
        GradientPaint gp = new GradientPaint(p, color1, pS, color2);
        g.setPaint(gp);
        g.fill(figState);


        //Función
        //Dependiendo de la forma y tamaño de la figura calculo el zoom como
        //para que me entre en la imagen
        w0 = 100;
        h0 = 60;

        i = c.getIntProperty(WflConstants.FUNCTION_WIDTH);
        if (i > 0)
            w0 = i;

        i = c.getIntProperty(WflConstants.FUNCTION_HEIGHT);
        if (i > 0)
            h0 = i;


        m = Math.max((double)w0, h0*(double)3);
        z = w / m;
        RoundRectangle2D figFun = new RoundRectangle2D.Double();

        dw = z * w0;
        dh = z * h0;
        x = (w-dw)/2;
        int y1 = (int)(w - w / (double)3);
        figFun.setFrame(x, y1, dw, dh);

        col = c.getColorProperty(WflConstants.FUNCTION_COLOR_SS);

        color1 = col.brighter();
        color2 = col.darker();

        x1 = (float)w/2;

        Point pF = new Point((int)x1, y1);
        p = new Point((int)x1, (int)w);
        gp = new GradientPaint(pF, color1, p, color2);
        g.setPaint(gp);
        g.fill(figFun);


        g.setRenderingHints(qualityHints);


        //Estado

        //Flecha
        g.setPaint(Color.black);
        g.drawLine(pS.x, pS.y, pF.x, pF.y);

        int d = 3;
        int[] xp = new int[]{pF.x,  pF.x - d+1,  pF.x + d};
        int[] yp = new int[]{pF.y,  pF.y - d,  pF.y - d};
        g.fillPolygon(new Polygon(xp, yp, 3));
        icon = new ImageIcon(img);
        return icon;
    }
}
