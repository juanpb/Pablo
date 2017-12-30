package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.gui.cmp.*;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtility;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.bobj.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflCreateImagenAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static final String name = "CREATE_IMAGEN";
    private static final String title = "Crear imagen";

    private int WIDTH_INFO = 600;

    private WflContainerControl control = null;
    private static final int HEIGHT_LINE = 22;
    private java.util.List<String> infoPosFunctions;
    private static final int MAX_CHAR_LENGTH = 110;
    private List<String> infoFunctions;

    public WflCreateImagenAction(WflContainerControl control) {
        this(control, true);
    }

    public WflCreateImagenAction(WflContainerControl control, boolean showText) {
        super(name);
        this.control = control;
        String n = showText ? title : null;
        init(n);
    }

    private void init(String txt){
        putValue(NAME, txt);
        if (txt == null)
            putValue(SHORT_DESCRIPTION, title);
        putValue(ACCELERATOR_KEY, WflShortcuts.CREATE_IMAGE);
        putValue(SMALL_ICON, null);
    }

    public void actionPerformed(ActionEvent e) {
        RenderedImage rendImage = createImage();
        try {
            File file = getFile();
            if (file == null)
                return ;

            String ext = file.getName().substring(file.getName().length() - 3);
            ImageIO.write(rendImage, ext, file);
            String msg = "Se ha creado la imagen '" + file.getAbsolutePath() + "'";
            WflUtil.showMessage(msg, "Crear imagen");
        } catch (IOException ex) {
            ex.printStackTrace();
            WflUtil.showErrorMessage(ex.getMessage(), "Crear imagen");
        }
    }

    private File getFile() {
        File res = null;
        Frame parent = WflUtility.getCurrentFrame();
        WflConstants c = WflConstants.getInstance();
        String classpath = c.getProperty(WflConstants.CLASS_PATH);
        JFileChooser fc = new JFileChooser(classpath);
        fc.setAcceptAllFileFilterUsed(false);
        FileFilter choosableFilterClassJPG = new FileFilter(){
            public boolean accept(File f){
                if (f.isDirectory())
                    return true;
                String name = f.getName();
                return name.toLowerCase().endsWith(".jpg");
            }

            public String getDescription(){
                return "*.jpg";
            }
        };
        FileFilter choosableFilterPNG = new FileFilter(){
            public boolean accept(File f){
                if (f.isDirectory())
                    return true;
                String name = f.getName();
                return name.toLowerCase().endsWith(".png");
            }

            public String getDescription(){
                return "*.png";
            }
        };

        fc.setFileFilter(choosableFilterClassJPG);
        fc.addChoosableFileFilter(choosableFilterPNG);
        int i = fc.showDialog(parent, "Grabar imagen como...");

        if (i == JFileChooser.APPROVE_OPTION){
            res = fc.getSelectedFile();
            String ext = fc.getFileFilter().getDescription().substring(1);
            if (!res.getName().endsWith(ext)){
                res = new File(res.getParent(),  res.getName() + ext);
            }
        }
        return res;
    }

    public RenderedImage createImage() {
        infoPosFunctions=null;
        infoFunctions=null;

        WflProcessDefinitionContainer container = control.getContainer();
        Dimension size = container.getSize();
        int width = (int)size.getWidth();
        if (width < WIDTH_INFO)
            width = WIDTH_INFO; //tamaño mínimo para que entre el texto

        int h = 0;
        //Busco el componenete que esté más abajo
        List wcs = container.getWflComponents();
        for (int i = 0; i < wcs.size(); i++) {
            WflComponent wc = (WflComponent) wcs.get(i);
            int h2 = wc.getY() + wc.getHeight();
            if (h2 > h)
                h = h2;
        }
        int heightPicture = h;
        int height = heightPicture;

        //calculo el tamaño que voy a usar para el texto
        int lines = container.getWflComponents(WflState.class).size();
        lines += getInfoFunctions().size();
        int i = control.getMarks().size();
        if (i > 0)
            i +=3; //usados para títulos y espacios
        lines += i;
        i = getInfoPosFunctions().size();
        if (i > 0)
            i +=3; //usados para títulos y espacios
        lines += i;
        lines += 9;//usados para títulos y espacios

        height += lines * HEIGHT_LINE;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Create a graphics contents on the buffered image
        Graphics2D g2d = bi.createGraphics();
        Color background = container.getBackground();
        Color backgroundForText = WflPosFunction.getBackgroundForText();
        Color col = WflConstants.getInstance().getColorProperty(
               WflConstants.CONTAINER_BACKGROUND_IMAGE);
        if (col != null){
            container.setBackground(col);
            WflPosFunction.setBackgroundForText(col);
        }

        container.paint(g2d);
        g2d.setColor(container.getBackground());
        g2d.fillRect(0,heightPicture, width, height-heightPicture);
        addInfo(g2d, heightPicture);
        g2d.dispose();

        container.setBackground(background);
        WflPosFunction.setBackgroundForText(backgroundForText);

        return bi;
    }

    private void addInfo(Graphics2D g2d, int y) {
        g2d.setColor(Color.black);
        int x = 20;
        y += 10;

        Font normalFont = g2d.getFont();
        Font titleFont = normalFont.deriveFont(Font.BOLD);
        //Proceso
        g2d.setFont(titleFont);
        y += HEIGHT_LINE;
        g2d.drawString("Proceso: ", x, y );
        y += HEIGHT_LINE;
        g2d.setFont(normalFont);
        String pId = control.getProcessId();
        String pName = control.getProcessName();
        String pVersion = control.getProcessVersion();
        String pTrace = control.getProcessTrace();
        if (pId == null || pId.trim().equals(""))
            pId = "[Id no definido]";
        if (pName == null || pName.trim().equals(""))
            pName = "[No definido]";
        if (pVersion == null || pVersion.trim().equals(""))
            pVersion = "[No definida]";

        String p = pId + ", nombre: " + pName+ ", versión: " + pVersion + ", ";
        if (pTrace != null && pTrace.equalsIgnoreCase("s"))
            p += "genera seguimientos en un archivo secuencial";
        else if (pTrace != null && pTrace.equalsIgnoreCase("n"))
            p += "no genera seguimientos";
        else
            p += "genera seguimientos";

        g2d.drawString(p, x, y );
        y += HEIGHT_LINE;

        //Estado
        java.util.List txt = getInfoStates();
        y += HEIGHT_LINE;
        g2d.setFont(titleFont);
        g2d.drawString("Estados:", x, y );
        g2d.setFont(normalFont);
        y += HEIGHT_LINE;
        for (int i = 0; i < txt.size(); i++) {
            String s = (String) txt.get(i);
            g2d.drawString(s, x, y );
            y += HEIGHT_LINE;
        }

        //Funciones
        txt = getInfoFunctions();
        y += HEIGHT_LINE;
        g2d.setFont(titleFont);
        g2d.drawString("Funciones:", x, y );
        g2d.setFont(normalFont);
        y += HEIGHT_LINE;
        for (int i = 0; i < txt.size(); i++) {
            String s = (String) txt.get(i);
            g2d.drawString(s, x, y );
            y += HEIGHT_LINE;
        }

        //Posfunciones con máscara
        txt = infoPosFunctions;
        if (txt.size() > 0){
            y += HEIGHT_LINE;
            g2d.setFont(titleFont);
            g2d.drawString("PosFunciones:", x, y );
            g2d.setFont(normalFont);
            y += HEIGHT_LINE;
            for (int i = 0; i < txt.size(); i++) {
                String s = (String) txt.get(i);
                g2d.drawString(s, x, y );
                y += HEIGHT_LINE;
            }
        }

        //Hitos
        txt = getInfoMarks();
        if (txt.size() > 0){
            y += HEIGHT_LINE;
            g2d.setFont(titleFont);
            g2d.drawString("Hitos:", x, y );
            g2d.setFont(normalFont);
            y += HEIGHT_LINE;
            for (int i = 0; i < txt.size(); i++) {
                String s = (String) txt.get(i);
                g2d.drawString(s, x, y );
                y += HEIGHT_LINE;
            }
        }
    }

    private List getInfoMarks() {
        java.util.List<String> res = new ArrayList<String>();
        List marks = control.getMarks();
        for (int i = 0; i < marks.size(); i++) {
            WflMarkBObj m = (WflMarkBObj) marks.get(i);
            String s = m.getId() + ", " + m.getName() + ", ";
            if (m.isDeletable())
                s += "Borrable";
            else
                s += "No Borrable";

            res.add(s);
        }
        return res;
    }

    private List getInfoStates() {
        java.util.List<String> res = new ArrayList<String>();
        WflProcessDefinitionContainer container = control.getContainer();

        List list = container.getWflComponents(WflState.class);
        for (int i = 0; i < list.size(); i++) {
            WflStateBObj s = (WflStateBObj)((WflState) list.get(i)).getBObj();
            String x = s.getId() + ", " + s.getName();
            String mt = s.getMaskType();
            if (mt != null){
                if (s.getMaskType().equals(WflStateBObj.MASK_TYPE_AND))
                    x += ", " + mt + s.getMaskValue();
            }
            String posDes = s.getPositionDescription();
            if (posDes != null)
                x += ", " + posDes;

            String par = s.getParallelism();
            if (WflStateBObj.PARALLELISM_START.equals(par))
                x += ", Inicia Paralelismo";
            else if (WflStateBObj.PARALLELISM_END.equals(par))
                x += ", Finaliza Paralelismo";

            List stateMarks = s.getStateMarks();
            if (stateMarks.size() > 0){
                x += ", Hitos: ";
                for (int j = 0; j < stateMarks.size(); j++) {
                    WflStateMarkBObj sm = (WflStateMarkBObj) stateMarks.get(j);
                    x += sm.getMark().getId() + " " + sm.getActionDescription();
                    if (i < stateMarks.size() -1)
                        x += "; ";
                }
            }
            res.add(x);
        }
        return res;
    }

    private List getInfoFunctions() {
        if (infoFunctions != null)
            return  infoFunctions;
        infoFunctions = new ArrayList<String>();
        WflProcessDefinitionContainer container = control.getContainer();

        List list = container.getWflComponents(WflFunction.class);
        for (int i = 0; i < list.size(); i++) {
            WflFunctionBObj s = (WflFunctionBObj)((WflFunction) list.get(i)).getBObj();
            String x = s.getId() + ", " + s.getName() + ", " +
                    s.getExecutionTypeDescription()+ ", Ejecución " +
                    s.getExecutionSynchroDescription();
            String en = s.getExecutionName();
            if (en != null && !en.equals("") )
                x += " nombre: " + en;

            x += ", " + s.getFunctionClass();
            if (s.isCarryContext())
                x += ", lleva contexto";
            else
                x += ", no lleva contexto";

            if (WflFunctionBObj.TYPE_ITERATIVE.equals(s.getType()))
                x += ", tipo iterativa";
            else
                x += ", tipo normal";

            while (x.length() > MAX_CHAR_LENGTH){
                String z = x.substring(0, MAX_CHAR_LENGTH);
                //parto la línea en la última coma antes de MAX_CHAR_LENGTH
                int j = z.lastIndexOf(",");
                if (j > 0){
                    z = x.substring(0, j-1);
                    infoFunctions.add(z);
                    x = x.substring(j);
                }
                else{

                }
            }
            infoFunctions.add(x);
        }
        return infoFunctions;
    }

    private List getInfoPosFunctions() {
        if (infoPosFunctions != null)
            return  infoPosFunctions;
        infoPosFunctions = new ArrayList<String>();
        WflProcessDefinitionContainer container = control.getContainer();

        List list = container.getWflComponents(WflPosFunction.class);
        for (int i = 0; i < list.size(); i++) {
            WflPosFunctionBObj s = (WflPosFunctionBObj)((WflPosFunction) list.get(i)).getBObj();
            String mt = s.getMaskType();
            if (mt != null){
                String x = "Función: " + s.getSourceId() + ", Estado: " +
                s.getTargetId() + ", " + mt + s.getMaskValue();
                infoPosFunctions.add(x);
            }
        }
        return infoPosFunctions;
    }

}