package crm.core.wfl.editor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;
import crm.core.wfl.editor.gui.dlg.WflZoomFrame;
import crm.core.wfl.editor.gui.mnu.WflMarksBar;
import crm.core.wfl.editor.gui.mnu.WflMenuBar;
import crm.core.wfl.editor.gui.mnu.WflToolBar;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflSplashScreen;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;

/**
 * User: JPB
 * Date: Jan 13, 2006
 * Time: 3:36:36 PM
 */
public class WflMain {
    private JFrame frame = new JFrame("Editor");

    private WflProcessDefinitionContainer definitionContainer = null;

    public static void main(String[] args) {
        new WflMain();
    }

    public static JLabel zoomLabel;
    
    public WflMain() {
        ImageIcon ic = WflUtil.createImageIcon(WflConstants.ICON_SPLASH_SCREEN);
        WflConstants c = WflConstants.getInstance();
        int time = c.getIntProperty(WflConstants.SPLASH_SCREEN_TIME);
        if (time < 0)
            time = 3000;
        try {
            if (time != 0){
                WflSplashScreen splash = new WflSplashScreen(ic, time);
                splash.run();
            }
        } catch (Exception e) {
            System.out.println("No se encontró ls imagen para SplashScreen");
            e.printStackTrace();

        }
        frame.getContentPane().setLayout(new BorderLayout());

        definitionContainer = new WflProcessDefinitionContainer();

        // Creo la ventana de zoom
        WflZoomFrame zoomFrame = new WflZoomFrame();
        definitionContainer.getControl().setZoomFrame(zoomFrame);

        // Defino los tool bars
        WflToolBar toolBar = new WflToolBar(definitionContainer);
        WflMarksBar marksBar = new WflMarksBar(definitionContainer);
        WflMenuBar menuBar = createMenuBar();
        definitionContainer.getControl().setToolBar(toolBar);
        definitionContainer.getControl().setMenuBar(menuBar);        
        frame.setJMenuBar(menuBar);
        JScrollPane spContainer =new JScrollPane(definitionContainer,
                                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Le tengo que setear el viewport asociado para que el container general sepa que parte
        // de él se esta mostrando.
        definitionContainer.setAssociatedViewport(spContainer.getViewport());
        definitionContainer.setAssociatedScrollPane(spContainer);
        spContainer.setTransferHandler(new WflFileTransferHandler(definitionContainer.getControl()));
        
        
        // Seteo los listeners de Mouse. 
        WflZoomMouseListener zoomMouseListener = new WflZoomMouseListener(definitionContainer, zoomFrame);
        definitionContainer.addMouseMotionListener(zoomMouseListener);
        WflComponent.DEFAULT_MOUSE_MOTION_LISTENER = zoomMouseListener; 
        
        frame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
        frame.getContentPane().add(marksBar, BorderLayout.EAST);
        frame.getContentPane().add(spContainer, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = new Dimension(800, 600);
        Point point = WflUtil.getCenter(dim);
        frame.setSize(dim);
        frame.setLocation(point);
        frame.setVisible(true);

        WflUtility.setCurrentFrame(frame);
        WflUtility.setMarksBar(marksBar);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                definitionContainer.getControl().exit();
            }
        });

    }

    private WflMenuBar createMenuBar() {
        return new WflMenuBar(definitionContainer);
    }

}
