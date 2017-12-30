package crm.core.wfl.editor.gui.action;

import crm.core.wfl.editor.bobj.WflLocationBObj;
import crm.core.wfl.editor.bobj.WflProcessBObj;
import crm.core.wfl.editor.gui.cmp.WflComponent;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflPosFunction;
import crm.core.wfl.editor.gui.cmp.WflStateShortCut;
import crm.core.wfl.editor.gui.util.*;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.StringTokenizer;


/**
 * User: JPB
 * Date: Feb 14, 2006
 * Time: 9:58:09 AM
 */
public class WflOpenFileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String name = "OPEN_FILE";
    private static final String desc = "Abrir...";

    private WflContainerControl control = null;

    /**
     * Guarda el path del directorio del último archivo abierto.
     */
    private String currentDirectoryPath = null;

    public WflOpenFileAction(WflContainerControl control) {
        this(control, true);
    }

    public WflOpenFileAction(WflContainerControl control, boolean showText) {
        super(name, WflUtil.createImageIcon(WflConstants.ICON_OPEN_FILE));
        this.control = control;
        String name = showText ? desc : null;
        init(name);
    }

    private void init(String txt){
        //el tooltip (SHORT_DESCRIPTION) va sólo si no hay texto
        putValue(NAME, txt);
        if (txt == null){
            putValue(SHORT_DESCRIPTION, desc);
        }
        putValue(ACCELERATOR_KEY, WflShortcuts.OPEN_FILE);
    }

    public void actionPerformed(ActionEvent e) {
        openFile(null);
    }

    public void openFile(File fileToOpen) {

    	if(fileToOpen!=null && !fileToOpen.getName().endsWith(".xml")) {
            String msg = "Solo se pueden abrir archivos .xml (se recibio " + fileToOpen.getName() + ").";
            WflUtil.showErrorMessage(msg);
            return;
    	}
    	
        boolean wasOK = control.confirmSave();
        if (!wasOK)
            return ;

        WflConstants constants = WflConstants.getInstance();
        
        if (currentDirectoryPath == null){
            
            String s = constants.getProperty(WflConstants.ROOT_PATH);
            File f = null;
            if (s != null)
                f = new File(s);
            if (f != null && f.equals("")
                    && (!f.exists() || !(f.isDirectory())))
                System.err.println("No existe el directorio " + f.getPath());
            else
                currentDirectoryPath = s;
        }
        
        if(fileToOpen==null) { 
	        JFileChooser fc = new JFileChooser(currentDirectoryPath);
	        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        fc.setAcceptAllFileFilterUsed(false);
	
	        FileFilter choosableFilterClassXML = new FileFilter(){
	            public boolean accept(File f){
	                if (f.isDirectory())
	                    return true;
	                String name = f.getName();
	                return name.toLowerCase().endsWith(".xml");
	            }
	
	            public String getDescription(){
	                return "*.xml";
	            }
	        };

	        fc.setFileFilter(choosableFilterClassXML);

	        Frame cf = WflUtility.getCurrentFrame();
	        
	        int returnVal = fc.showOpenDialog(cf);
	        
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	fileToOpen = fc.getSelectedFile();
	        }
        }
	     
        if(fileToOpen!=null) {
        	control.clean();
        	
        	File lastFileOpen = fileToOpen;

        	currentDirectoryPath = lastFileOpen.getParent();

            constants.writeProperty(WflConstants.ROOT_PATH, currentDirectoryPath);
            
            String name2 = lastFileOpen.getName();
            if (!name2.toLowerCase().endsWith(".xml"))
                lastFileOpen = new File(lastFileOpen.getParent(), name2 + ".xml");
            WflUtility.setLastFileOpen(lastFileOpen);
            WflProcessBObj process;
            try {
                process = WflIOUtil.getProcessBObj(lastFileOpen);
            } catch (SAXParseException e) {
                e.printStackTrace();
                String msg = "El archivo " + lastFileOpen.getPath() + " no es" +
                        " un archivo xml bien formado: \n" + e.getMessage();
                WflUtil.showErrorMessage(msg);
                return ;
            }catch(Exception e){
                String msg = "Problemas con el archivo " + lastFileOpen.getPath() +
                        ": \n" + e.getMessage();
                WflUtil.showErrorMessage(msg);
                return ;
            }
            control.load(process);

            File fileLocation = null;
            try{
                //Asigno el nuevo título al frame
                WflUtility.setCurrentFrameTitle(lastFileOpen.getPath(),
                        process.getId());

                //Cargo, si existe, el archivo con los locations de los
                //componentes, si no, uso el WflLayoutManager
                String name = lastFileOpen.getName();
                if (name.toLowerCase().endsWith(".xml")) {
                    String n = name.substring(0, name.length() - 4) +
                            "Locations.xml";
                    fileLocation = new File(lastFileOpen.getParent(), n);
                    java.util.List locations = WflIOUtil.getLocationsBObj(fileLocation);
                    if (locations != null)
                        setLocations(locations);
                    else {
                        List wcs = control.getContainer().getWflComponents();
                        WflLayoutManager.assignLocations(wcs);
                    }
                }

            } catch (SAXParseException e) {
                String msg = "El archivo " + fileLocation.getPath() + " no es" +
                        " un archivo xml bien formado: \n" + e.getMessage();
                WflUtil.showErrorMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                WflUtil.showErrorMessage(e.getMessage(), "Error");
            }
            
            control.setModified(false);
        }
    }

    private void setLocations(java.util.List locations) {
        if (locations == null)
            return;
        
        // Primero tengo que hacer una pasada para ver que locations definen shortcuts.
        // Si hay uno que lo define, tengo que crear el short cut.
        for (int i = 0; i < locations.size(); i++) {
            WflLocationBObj obj = (WflLocationBObj) locations.get(i);
            if(obj.getId().startsWith(WflStateShortCut.PREFIX)) {
            	String sourceToTarget = obj.getId().substring(WflStateShortCut.PREFIX.length());
            	StringTokenizer st = new StringTokenizer(sourceToTarget, WflStateShortCut.SOURCE_TARGET_SEPARATOR);
            	String sourceId = st.nextToken();
            	String targetId = st.nextToken();
            	String posFunctionId = sourceId + "-" + targetId;
            	WflPosFunction posFunction = (WflPosFunction)control.getWflComponent(posFunctionId);
            	if(posFunction!=null)
            		WflReplaceStateByShortCutAction.addShortCut(control, posFunction, obj.getPoint().x, obj.getPoint().y);
            }
        }
        
        for (int i = 0; i < locations.size(); i++) {
            WflLocationBObj obj = (WflLocationBObj) locations.get(i);
            String id = obj.getBObjId();
            WflComponent wflComp = control.getWflComponent(id);
            if (wflComp != null){
            	wflComp.setWflLocation(obj);
            }
        }
        control.repaintContainer();
    }

}