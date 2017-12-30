package p.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.MemoryImageSource;
import java.net.URL;

/**
 * User: JP
 * Date: 06/05/2005
 * Time: 12:00:02
 */
public class GUI {
    private static Cursor WAIT_CURSOR =
            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    private static Cursor DEFAULT_CURSOR =
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public static void setFullScreen(Window w){
        GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().setFullScreenWindow(w);
    }


    public static void addCancelByEscapeKey(JFrame frame, AbstractAction cancelAction){
        String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
        int noModifiers = 0;
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, noModifiers, false);
        InputMap inputMap =
          frame.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        ;
        inputMap.put(escapeKey, CANCEL_ACTION_KEY);

        frame.getRootPane().getActionMap().put(CANCEL_ACTION_KEY, cancelAction);
    }

    public static void centrar(Container container){
        container.setLocation(GUI.centrar(container.getSize()));
    }
    public static void centrarALaDerecha(Container container){
        container.setLocation(GUI.centrarALaDerecha(container.getSize()));
    }

    private static Point centrarALaDerecha(Dimension tam){
        Dimension d = getMaxSize();
        int x = (int)(d.getWidth() - tam.getWidth());
        int y = (int)(d.getHeight() - tam.getHeight())/2;
        return new Point(x, y);
    }



    public static void abajoALaIzquierda(Container container){
        container.setLocation(GUI.abajoALaIzquierda(container.getSize()));
    }

    private static Point abajoALaIzquierda(Dimension tam){
        Dimension d = getMaxSize();
        int x = 0;
        int y = (int)(d.getHeight() - tam.getHeight());
        //lo subo por la barra de tareas
        y += 30;
        return new Point(x, y);
    }



    private static Point centrar(Dimension tam){
        Dimension d = getMaxSize();
        int x = (int)(d.getWidth() - tam.getWidth())/2;
        int y = (int)(d.getHeight() - tam.getHeight())/2;

        if (x < 0)
            x = 0;
        if (y < 0) 
            y = 0;
        return new Point(x, y);
    }

    /** Sets cursor for specified component to Wait cursor */
    public static void startWaitCursor(JComponent component) {
        RootPaneContainer root =
          (RootPaneContainer)component.getTopLevelAncestor();
        root.getGlassPane().setCursor(WAIT_CURSOR);
        root.getGlassPane().setVisible(true);
    }

    /** Sets cursor for specified component to normal cursor */
    public static void stopWaitCursor(JComponent component) {
        RootPaneContainer root =
          (RootPaneContainer)component.getTopLevelAncestor();
        root.getGlassPane().setCursor(DEFAULT_CURSOR);
        root.getGlassPane().setVisible(false);
    }

    public static void ocultarMouse(RootPaneContainer root, boolean ocultar){
        Cursor cursor;
        if (ocultar){
            int[] pixels = new int[16 * 16];
            Image image = Toolkit.getDefaultToolkit().createImage(
                    new MemoryImageSource(16, 16, pixels, 0, 16));
            cursor = Toolkit.getDefaultToolkit().createCustomCursor
                         (image, new Point(0, 0), "invisibleCursor");
        }
        else{
            cursor =  DEFAULT_CURSOR;
        }
        root.getGlassPane().setCursor(cursor);
        root.getGlassPane().setVisible(true);
    }

    public static ImageIcon createImageIcon(String s) {
        URL imgURL = Thread.currentThread().getContextClassLoader().getResource(s);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
            //
        } else {
            System.err.println("Couldn't find file: " + s);
            return null;
        }
    }

    /**
     * Devuelve el tamaño máximo descontando las barras de tarea, etc
    */
    public static Dimension getMaxSize()	{
        return getMaxSize(null);
    }

    /**
     * Devuelve el tamaño máximo descontando las barras de tarea, etc
     * @param config
     * @return
     */
    public static Dimension getMaxSize(GraphicsConfiguration config)	{
        if (config == null){
            config = GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration();
        }
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        Insets insets = kit.getScreenInsets(config);
        screenSize.width -= (insets.left + insets.right);
        screenSize.height -= (insets.top + insets.bottom);
        return screenSize;
    }

    /**
     * Achica dim si el alto o el ancho de la dimensión recibida como parámetro
     * es mayor que la del monitor.
     * @param dim
     */
    public static void achicarSiEsNecesario(Dimension dim)	{
        Dimension scrSize = GUI.getMaxSize();

        if (scrSize.getHeight() < dim.getHeight())
            dim.height = (int) scrSize.getHeight();

        if (scrSize.getWidth() < dim.getWidth())
            dim.width = (int) scrSize.getWidth();
    }

    public static void maximizar(Frame frame)	{
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        frame.setSize(getMaxSize(config));
        Point xy = getXYParaMaximizar(config);
        frame.setLocation(xy.x, xy.y);
    }

    public static Point getXYParaMaximizar(GraphicsConfiguration config){
        if (config == null){
            config = GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration();
        }
        Toolkit kit = Toolkit.getDefaultToolkit();
        Insets insets = kit.getScreenInsets(config);
        return new Point(insets.left, insets.top);
    }

}
