package crm.core.wfl.editor.gui.cmp;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.gui.dlg.WflDlg;
import crm.core.wfl.editor.gui.dlg.WflStateDlg;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un estado del proceso de negocio.
 * @author Cm
 */
public class WflState extends WflComponent {
	private static final long serialVersionUID = 1L;
	protected Ellipse2D figure = new Ellipse2D.Double();
    private WflStateBObj bobj = null;
    private Color markColor = Color.yellow;
    private Polygon figureStateMark = null;
    private static ImageIcon icon = null;
    private Color stateCancelColor = Color.red;
    private Color colorHighLight = Color.orange;

    /**
     * Lista con los msg de por qué no es consistente este componente.
     * Se actualiza cuando se llama a isConsistent.
     */
    private List<String> consistentMsg = null;
    private int markWidht;

    /**
     * Constructor default.
     */
    public WflState()
    {
        this(null);
    }

    public WflState(WflStateBObj bobj){
        super(true, true);
        if (bobj == null) {
            bobj = new WflStateBObj();
            String dummyId = "new ("+getVolatilId()+")";
            bobj.setId(dummyId);
            bobj.setName(dummyId);            
        }
        this.bobj = bobj;
        init();
    }
    
    
    protected void init() {
        WflConstants c = WflConstants.getInstance();
        int i = c.getIntProperty(WflConstants.STATE_WIDTH);
        if (i > 0)
            width = i;

        i = c.getIntProperty(WflConstants.STATE_HEIGHT);
        if (i > 0)
            height = i;

        markWidht = c.getIntProperty(WflConstants.MARK_WIDHT);
        if (markWidht <= 0)
            markWidht = 10; //valor default

        Color col = c.getColorProperty(WflConstants.STATE_COLOR);
        if (col != null)
            color = col;
        
        col = c.getColorProperty(WflConstants.STATE_CANCEL_COLOR);
        if (col != null)
        	stateCancelColor = col;

        col = c.getColorProperty(WflConstants.SELECTED_COLOR);
        if (col != null)
            selectedColor = col;

        col = c.getColorProperty(WflConstants.MARK_COLOR);
        if (col != null)
            markColor = col;
        
        col = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT);
        if (col != null)
        	colorHighLight = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT);        

        String a = c.getProperty(WflConstants.ANTIALIASING);
        if (a != null && a.equals("OFF"))
            useAntialisasing = false;

        widthWithoutZoom = width;
        heightWithoutZoom = height;
        setSize(width,  height);
    }

    /**
     * Resizes this component so that it has width <code>width</code>
     * and height <code>height</code>.
     * @param width the new width of this component in pixels
     * @param height the new height of this component in pixels
     * @since JDK1.1
     */
    public void setSize(int width, int height) {
        //sobreescribo este método para que además de asignarle tamaño al
        //componente se modifique el tamaño del elipse que representa el estado

        double i = markWidht * zoom;
        figureStateMark = new Polygon(new int[]{0, (int) (i / 2), (int) i},
                    new int[]{(int) i, 0, (int) i}, 3);

        int w = (int) (widthWithoutZoom * zoom);
        int h = (int) (heightWithoutZoom * zoom);
        figure.setFrame(0, 0, w-1, h-1);
        super.setSize(width, height);
    }

    public WflBObj getBObj()
    {
        if (isInitial())
            bobj.setPosition(WflStateBObj.POSITION_INITIAL);
        else if (isFinal())
            bobj.setPosition(WflStateBObj.POSITION_FINAL);
        else if (bobj.getMaskType() != null)
            bobj.setPosition(WflStateBObj.POSITION_FINAL);//Estado final condicional
        else
            bobj.setPosition(null);
        return bobj;
    }

    public boolean isInitial() {
        List rc = getRelatedComponents();
        for (int i = 0; i < rc.size(); i++) {
            Object o = rc.get(i);
            if (o instanceof WflPosFunction)
                return false;
        }
        return true;
    }

    public boolean isFinal() {
        List rc = getRelatedComponents();
        for (int i = 0; i < rc.size(); i++) {
            Object o = rc.get(i);
            if (o instanceof WflPreFunction)
                return false;
        }
        return true;
    }

    public void setBObj(WflBObj bo)
    {
        bobj = (WflStateBObj)bo;
    }

    protected WflDlg getDialog()
    {
        return new WflStateDlg();
    }
    
    /**
     * Obtiene el color del componente.
     * @return Color del componente.
     */
    public Color getColor() {
        Color currentColor = color;
        
        if(this.bobj.getCancelState().equals(WflStateBObj.CANCEL_STATE)) 
        	currentColor = stateCancelColor;
        
        return currentColor;
    }

    /**
     * Repinta el estado.
     * @param g Grafico donde se pintara.
     */
    public void paint(Graphics g) {
    	
        Graphics2D g2d = (Graphics2D)g;
        if (useAntialisasing){
            g2d.setRenderingHints(qualityHints);
        }
        
        Color currentColor = getColor();
        
        int highlight = WflComponentHighlighter.getHighlightLevel(this);

        if(highlight>0)
        	currentColor = colorHighLight; 
        		
        Color color1 = currentColor.brighter();
        Color color2 = currentColor.darker();
        
        int x1 = width / 2;
        int y1 = height / 2;
        GradientPaint gp = new GradientPaint(0, 0, color2, x1, y1, color1, true);
        g2d.setPaint(gp);
        g2d.fill(figure);
        
       	g2d.setPaint(currentColor);
        g2d.draw(figure);
        
        if (bobj.getStateMarks().size() > 0){
            g2d.setColor(markColor);
            g2d.fillPolygon(figureStateMark);
        }
        if (isSelected())
            super.paintAsSelected(g2d);

        String id = bobj.getId();
        if (id != null)
            WflUtil.centeredText(g2d, this, id, zoom);

        String mt2 = bobj.getMaskType();
        if (mt2 != null){
            String txt = mt2;
            String v = bobj.getMaskValue();
            if (v != null)
                txt += v;
            WflUtil.topRightText((Graphics2D)g, this, txt, zoom);
        }
        if (! isConsistent()){
            g2d.setColor(Color.red);
            g2d.drawLine(0,0, width, height);
            g2d.drawLine(width, 0, 0, height);
            java.util.List cm = getConsistentMsg();
            setToolTipText(cm);
        }
        else
            setToolTipText(getTooltip());
    }

    protected List<String> getTooltip() {
        WflStateBObj bo = (WflStateBObj)getBObj();
        List<String> res = new ArrayList<String>();
        res.add("Código      : " + bo.getBObjId());
        res.add("Descripcion : " + bo.getName());
        if (isInParallelism())
            res.add("Paralelilsmo:" + super.getParallelism());        
        return  res;
    }

    /**
     * Un estado es consistente si está relacionado con 0 ó 1 prefunción y/o
     * 0 ó 1 posfunción
     * @return true si es consistente.
     */
    public boolean isConsistent(){
        consistentMsg = new ArrayList<String>();
        WflStateBObj bo = (WflStateBObj)getBObj();
        boolean boConsistent = bo.isConsistent();
        consistentMsg.addAll(bo.getConsistentMsg());

        int posF = 0;
        int preF = 0;
        List rc = getRelatedComponents();
        for (int i = 0; i < rc.size(); i++) {
            Object o = rc.get(i);
            if (o instanceof WflPreFunction)
                preF++;
            else if (o instanceof WflPosFunction)
                posF++;
        }
        if (preF < 1 && posF < 1)
            consistentMsg.add("No tiene prefunciones ni posfunciones");
        if (preF > 1)
            consistentMsg.add("Puede tener a lo sumo una prefunción");

        return boConsistent && !(preF < 1 && posF < 1) && !(preF > 1);
    }

    public boolean contains(int x, int y) {
        return figure.contains(x, y);
    }

    public String getId(){
        return bobj.getId();
    }

    public void setId(String id){
        bobj.setId(id);
    }

    /**
     * Devuelve una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente. La lista representa los posibles errores
     * en la última llamada al metodo isConsistent()
     * @return una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente.
     */
    protected List getConsistentMsg(){
        return consistentMsg;
    }

    public static ImageIcon  createImageIcon() {
        if (icon != null) {
            return icon;
        }

        int w = 20;
//        Image img = new BufferedImage(w, w, BufferedImage.TYPE_BYTE_BINARY);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image img = gc.createCompatibleImage(w,w,Transparency.OPAQUE);

//        Image img = new BufferedImage(w, w, BufferedImage.TYPE_3BYTE_BGR);
//        Image img = new BufferedImage(w, w, Transparency.BITMASK);
        Graphics2D g = (Graphics2D)img.getGraphics();

        g.setRenderingHints(qualityHints);


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

        g.setPaint(new Color(204,204,204));
        g.fillRect(0,0,w,w);
        int m = Math.max(w0, h0);
        double d = (double)w / (double)m;
        Ellipse2D fig = new Ellipse2D.Double();

        double dw = d * w0;
        double dh = d * h0;
        double x = (w-dw)/2;
        double y = (w-dh)/2;
        fig.setFrame(x, y, dw, dh);

        Color col = c.getColorProperty(WflConstants.STATE_COLOR);
        if (col == null)
            col = Color.yellow;

        Color color1 = col.brighter();
        Color color2 = col.darker();
        float x1 = (float)dw/2;
        float y1 = (float)dh/2;
        GradientPaint gp = new GradientPaint(0, 0, color2, x1, y1, color1, true);        
        g.setPaint(gp);
        g.fill(fig);
        
        g.setPaint(col);
        g.draw(fig);
        

        icon = new ImageIcon(img);
        return icon;
    }
}
