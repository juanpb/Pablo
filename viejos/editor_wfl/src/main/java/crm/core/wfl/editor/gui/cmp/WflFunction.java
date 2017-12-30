package crm.core.wfl.editor.gui.cmp;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflFunctionBObj;
import crm.core.wfl.editor.function.WflServiceTypeDefinition;
import crm.core.wfl.editor.gui.dlg.WflDlg;
import crm.core.wfl.editor.gui.dlg.WflFunctionDlg;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Representa una funcion del proceso de negocio.
 * @author Cm
 */
public class WflFunction extends WflComponent {
	private static final long serialVersionUID = 1L;
	private RoundRectangle2D figure = new RoundRectangle2D.Double();
    private WflFunctionBObj bobj = null;

    private Color colorCI, colorSS, colorPE, colorSA, colorHighLight;

    /**
     * Lista con los msg de por qué no es consistente este componente.
     * Se actualiza cuando se llama a isConsistent.
     */
    private java.util.List<String> consistentMsg = null;

    private static ImageIcon icon;


    /**
     * Constructor default.
     */
    public WflFunction()
    {
        this(null);
    }
    public WflFunction(WflFunctionBObj bo)
    {
        super(true, true);
        if (bo == null) {
            bo = new WflFunctionBObj();
            
            // Seteo los defaults de codigos/nombre.
            String dummyId = "new ("+getVolatilId()+")";
            bo.setId(dummyId);
            bo.setName(dummyId);  
            
            // Seteo los defaults de funciones.
            //todo ver qué es esto
            final String serviceType = bo.getServiceType();
            if(serviceType !=null) {
            	WflServiceTypeDefinition td = WflServiceTypeDefinition.getTypeDefinition(serviceType);
            	if(td!=null) {
            		bo.setScript(td.getDefaultScript());
            		bo.setFunctionClass(td.getDefaultFunction());
            	}
            }
        }
        bobj = bo;
        init();
    }

    private void init() {

        WflConstants c = WflConstants.getInstance();
        int i = c.getIntProperty(WflConstants.FUNCTION_WIDTH);
        if (i > 0)
            width = i;
        i = c.getIntProperty(WflConstants.FUNCTION_HEIGHT);
        if (i > 0)
            height = i;

        //Selecciono los colores
        colorCI = c.getColorProperty(WflConstants.FUNCTION_COLOR_CI);
        colorSA = c.getColorProperty(WflConstants.FUNCTION_COLOR_SA);
        colorSS = c.getColorProperty(WflConstants.FUNCTION_COLOR_SS);
        colorPE = c.getColorProperty(WflConstants.FUNCTION_COLOR_PE);
        colorHighLight = c.getColorProperty(WflConstants.COLOR_HIGHLIGHT); 
        String a = c.getProperty(WflConstants.ANTIALIASING);
        if (a != null && a.equals("OFF"))
            useAntialisasing = false;

        widthWithoutZoom = width;
        heightWithoutZoom = height;
        setSize(width, height);
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
        //componente se modifique el tamaño del elipse que representa la función
        int w = (int) (widthWithoutZoom * zoom);
        int h = (int) (heightWithoutZoom * zoom);
        figure.setRoundRect(0, 0, w-1, h-1, 20, 20);
        super.setSize(width, height);
    }

    /**
     * Repinta la funcion.
     * @param g Grafico donde se pintara.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        if (useAntialisasing){
            g2d.setRenderingHints(qualityHints);
        }

        //Selecciono el color dependiendo del ExecutionType
        String tem = null;
        Color col = null;
        String et = bobj.getExecutionType();
         if (WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(et)){
            col = colorCI;
            tem = "CI";
         }
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(et)){
            col = colorSA;
            tem = "SA";
         }
        else if (WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(et)){
            col = colorSS;
            tem = "SS";
         }
        else if (WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS.equals(et)){
            col = colorPE;
            tem = "PE";
         }

        if (col != null)
            color = col;
        else
            color = colorSS;
        
        int highlight = WflComponentHighlighter.getHighlightLevel(this);
        
        if(highlight>0)
        	color = colorHighLight;

        if (color != null){
        	
            Color color1 = color.brighter();
            Color color2 = color.darker();
            int x1 = width / 2;
            int y1 = height / 2;            
            GradientPaint gp = new GradientPaint(0, 0, color2, x1, y1, color1, true);            
            g2d.setPaint(gp);
        }

        g2d.fill(figure);

       	g2d.setPaint(color.darker());
        
        g2d.draw(figure);
        

        if (isSelected())
            super.paintAsSelected(g2d);

        //Dibujo el id
        String id = bobj.getId();
        if (id != null)
            WflUtil.centeredText(g2d, this, id, zoom);

        //Dibujo el ExecutionType
        if (tem != null)
            WflUtil.topRightText(g2d, this, tem, zoom);

        //Dibujo el ExecutionType
        if (highlight > 0)
            WflUtil.bottomRightText(g2d, this, String.valueOf(highlight), zoom);

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

    private java.util.List getTooltip() {
        WflFunctionBObj bo = (WflFunctionBObj)getBObj();
        java.util.List<String> res = new ArrayList<String>();

        res.add("Código          : " + bo.getBObjId());
        res.add("Descripción     : " + bo.getName());
        
        String executionSynchro = bo.getExecutionSynchro();
        if (executionSynchro != null){
            if (executionSynchro.equals(WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC))
            	res.add("Ejecución       : sincrónica");        
            else
            	res.add("Ejecución       : asincrónica");
        }
        res.add("Función         : " + bo.getFunctionClass());
        res.add("Prioridad       : " + bo.getPriority());
        
        if( bo.getRole() != null && !bo.getRole().equals(""))
        {
	        res.add("Rol             : " + bo.getRole());
	        res.add("Rol ParamObjId  : " + bo.getRoleParametricObjectId());
	    }
        
        String en = bo.getExecutionName();
        
        if (en != null && !en.equals(""))
            res.add("Nombre ejecución: " + en);        
        	
        if(bo.getComment()!=null) {
	        res.add("  ");
	        res.add("Comentarios");
	        res.add(bo.getComment());
        }
        return res;
    }
    /**
     * Devuelve una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente. La lista representa los posibles errores
     * en la última llamada al metodo isConsistent()
     * @return una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente.
     */
    private java.util.List getConsistentMsg(){
        return consistentMsg;
    }

    /**
     * Una función es consistente si está relacionado con, por lo menos,
     * 1 posfunción y 1 prefunción, y su BO es consistente.
     */
    public boolean isConsistent(){
        consistentMsg = new ArrayList<String>();
        WflFunctionBObj bo = (WflFunctionBObj)getBObj();
        boolean boConsistent = bo.isConsistent();
        consistentMsg.addAll(bo.getConsistentMsg());

        int posF = 0;
        int preF = 0;
        java.util.List rc = getRelatedComponents();
        for (int i = 0; i < rc.size(); i++) {
            Object o = rc.get(i);
            if (o instanceof WflPreFunction)
                preF++;
            else if (o instanceof WflPosFunction)
                posF++;
        }
        if (preF == 0)
            consistentMsg.add("No tiene prefunciones");
        if (posF == 0)
            consistentMsg.add("No tiene posfunciones");

        return boConsistent && posF > 0 && preF > 0;
    }

    public WflBObj getBObj()
    {
        return bobj;
    }
    public void setBObj(WflBObj bo)
    {
        bobj = (WflFunctionBObj)bo;
    }
    
    protected WflDlg getDialog()
    {
        return new WflFunctionDlg();
    }

    public String getId(){
        return bobj.getId();
    }
    public void setId(String id){
        bobj.setId(id);
    }


    public static ImageIcon  createImageIcon() {
        if (icon != null) {
            return icon;
        }

        int w = 20;
        Image img = new BufferedImage(w, w, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D)img.getGraphics();

        g.setRenderingHints(qualityHints);

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

        g.setPaint(new Color(204,204,204));
        g.fillRect(0,0,w,w);
        int m = Math.max(w0, h0);
        double d = (double)w / (double)m;
        RoundRectangle2D fig = new RoundRectangle2D.Double();

        double dw = d * w0;
        double dh = d * h0;
        double x = (w-dw)/2;
        double y = (w-dh)/2;
        fig.setFrame(x, y, dw, dh);

        Color col = c.getColorProperty(WflConstants.FUNCTION_COLOR_SS);

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
