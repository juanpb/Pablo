package crm.core.wfl.editor.gui.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;

/**
 * User: JPB
 * Date: Jan 13, 2006
 * Time: 12:37:03 PM
 * @noinspection EmptyCatchBlock
 */
public class WflConstants {
	
	private static final String CUSTOM_PROPERTIES_FILE = "wfl_editor_default.properties";
	
    private static WflConstants _instance = null;
    private Properties properties = new Properties();
    private Properties customProperties = new Properties();    
    private String propertyFile = "crm/core/wfl/editor/init.properties";
                    //
    private static final String ROOT_IMAGES = "crm/core/wfl/editor/images";
	public static final String ICON_COMPILE = ROOT_IMAGES + "/compile.png";
    public static final String ICON_ACCEPT = ROOT_IMAGES + "/CrmButtonAccept.gif";
    public static final String ICON_CANCEL = ROOT_IMAGES + "/CrmButtonCancel.gif";
    public static final String ICON_EXPAND = ROOT_IMAGES + "/expand.gif";
    public static final String ICON_STATE = ROOT_IMAGES + "/state.gif";
    public static final String ICON_FUNCTION = ROOT_IMAGES + "/function.gif";
    public static final String ICON_PREFUNCTION = ROOT_IMAGES + "/prefunction.gif";
    public static final String ICON_POSFUNCTION = ROOT_IMAGES + "/posfunction.gif";
    public static final String ICON_PROCESS_DEFINITION = ROOT_IMAGES + "/processDefinition.gif";
    public static final String ICON_DELETE = ROOT_IMAGES + "/Delete.gif";
    public static final String ICON_SELECT_ALL = ROOT_IMAGES + "/selectAll.gif";
    public static final String ICON_PARALLELISM_CREATE = ROOT_IMAGES + "/parallelismCreate.gif";
    public static final String ICON_PARALLELISM_EDIT = ROOT_IMAGES + "/parallelismEdit.gif";
    public static final String ICON_PARALLELISM_REMOVE = ROOT_IMAGES + "/parallelismRemove.gif";
    public static final String ICON_NEW_FILE = ROOT_IMAGES + "/newFile.gif";
    public static final String ICON_OPEN_FILE = ROOT_IMAGES + "/openFile.gif";
    public static final String ICON_EXPLORER = ROOT_IMAGES + "/explorer.gif";
    public static final String ICON_SAVE = ROOT_IMAGES + "/save.gif";
    public static final String ICON_SAVE_AS = ROOT_IMAGES + "/saveAs.gif";
    public static final String ICON_EDIT = ROOT_IMAGES + "/edit.gif";
    public static final String ICON_EXIT = ROOT_IMAGES + "/exit.gif";
    public static final String ICON_PRINT = ROOT_IMAGES + "/print.gif";
    public static final String ICON_MARK = ROOT_IMAGES + "/mark.gif";
    public static final String ICON_MARK_CREATE = ROOT_IMAGES + "/markCreate.gif";
    public static final String ICON_MARK_DELETE = ROOT_IMAGES + "/markDelete.gif";
    public static final String ICON_MARK_STATE_REMOVE = ROOT_IMAGES + "/markStateRemove.gif";
    public static final String ICON_MARK_STATE_ADD = ROOT_IMAGES + "/markStateAdd.gif";
    public static final String ICON_MARK_CURSOR = ROOT_IMAGES + "/markCursor.gif";
    public static final String ICON_ALIGN_TOP = ROOT_IMAGES + "/alignTop.gif";
    public static final String ICON_ALIGN_BOTTOM = ROOT_IMAGES + "/alignBottom.gif";
    public static final String ICON_ALIGN_LEFT = ROOT_IMAGES + "/alignLeft.gif";
    public static final String ICON_ALIGN_RIGHT = ROOT_IMAGES + "/alignRight.gif";
    public static final String ICON_ALIGN_HORIZONTAL = ROOT_IMAGES + "/alignHorizontal.gif";
    public static final String ICON_ALIGN_VERTICAL = ROOT_IMAGES + "/alignVertical.gif";
    public static final String ICON_VIEW_XML = ROOT_IMAGES + "/viewXML.gif";
    public static final String ICON_SPLASH_SCREEN = ROOT_IMAGES + "/splashScreen.gif";
    public static final String ICON_PROPERTIES_REMOVE = ROOT_IMAGES + "/markStateRemove.gif";
    public static final String ICON_PROPERTIES_ADD = ROOT_IMAGES + "/markStateAdd.gif";
    public static final String ICON_OBJECT_CONSTRAINT_REMOVE = ROOT_IMAGES + "/markStateRemove.gif";
    public static final String ICON_OBJECT_CONSTRAINT_ADD = ROOT_IMAGES + "/markStateAdd.gif";
    public static final String ICON_VERTICE_REMOVE = ROOT_IMAGES + "/verticeRemove.png";
    public static final String ICON_VERTICE_ADD = ROOT_IMAGES + "/verticeAdd.png";
    public static final String ICON_TOKEN_TEST = ROOT_IMAGES + "/CrmButtonAccept.gif";
	public static final String ICON_ZOOM_WINDOW = ROOT_IMAGES + "/zoom.png";
	public static final String ICON_SHORTCUT_ADD = ROOT_IMAGES + "/shortcutAdd.png";
	public static final String ICON_EDIT_TRACE = ROOT_IMAGES + "/editTrace.png";	
	
	
    /**************************************************************************/
    /*  Propiedades generales
    /**************************************************************************/
    public final static String ANTIALIASING = "ANTIALIASING";
    public final static String SELECTED_COLOR = "SELECTED_COLOR";
    public final static String ROOT_PATH = "ROOT_PATH";
    public final static String CLASS_PATH = "CLASS_PATH";
    public final static String SPLASH_SCREEN_TIME = "SPLASH_SCREEN_TIME";


    public final static String COLOR_HIGHLIGHT = "COLOR_HIGHLIGHT";
    
    /**************************************************************************/
    /*  Propiedades del container
    /**************************************************************************/
    public final static String CONTAINER_BACKGROUND = "CONTAINER_BACKGROUND";
    public final static String CONTAINER_BACKGROUND_IMAGE = "CONTAINER_BACKGROUND_IMAGE";

    /**************************************************************************/
    /*  Propiedades de conectores (pre y pos función)
    /**************************************************************************/
    public final static String CONNECTOR_IN_PARALlELISM_COLOR= "CONNECTOR_IN_PARALlELISM_COLOR";
    /**************************************************************************/
    /*  Propiedades de pre función
    /**************************************************************************/
    public final static String PRE_FUNCTION_COLOR= "PRE_FUNCTION_COLOR";

    /**************************************************************************/
    /*  Propiedades de pre función
    /**************************************************************************/
    public final static String POS_FUNCTION_COLOR = "POS_FUNCTION_COLOR";

    /**************************************************************************/
    /*  Propiedades de los estados
    /**************************************************************************/
    public final static String STATE_COLOR = "STATE_COLOR";
    public final static String STATE_CANCEL_COLOR = "STATE_CANCEL_COLOR";    
    public final static String STATE_WIDTH = "STATE_WIDTH";
    public final static String STATE_HEIGHT = "STATE_HEIGHT";
    public final static String MARK_WIDHT = "MARK_WIDHT";
    public final static String MARK_COLOR = "MARK_COLOR";

    /**************************************************************************/
    /*  Propiedades de los shortcuts
    /**************************************************************************/
    public final static String SHORT_CUT_WIDTH = "SHORT_CUT_WIDTH";
    public final static String SHORT_CUT_HEIGHT = "SHORT_CUT_HEIGHT";
    public final static String SHORT_CUT_COLOR = "SHORT_CUT_COLOR";    
    
    
    /**************************************************************************/
    /*  Propiedades de las funciones
    /**************************************************************************/
    public final static String FUNCTION_COLOR_SS = "FUNCTION_COLOR_SS";
    public final static String FUNCTION_COLOR_SA = "FUNCTION_COLOR_SA";
    public final static String FUNCTION_COLOR_CI = "FUNCTION_COLOR_CI";
    public final static String FUNCTION_COLOR_PE = "FUNCTION_COLOR_PE";
    public final static String FUNCTION_WIDTH = "FUNCTION_WIDTH";
    public final static String FUNCTION_HEIGHT = "FUNCTION_HEIGHT";

    private WflConstants(){
        URL url = Thread.currentThread().
                getContextClassLoader().getResource(propertyFile);

        if (url == null){
            String msg = "No se encuentra el archivo de properties " +
                    propertyFile;
            System.err.println(msg);
            return ;
        }
        try {
        	// Levanto las properties default.
            InputStream is = url.openStream();
            properties.load(is);
            
            // Levanto las custom properties.
            File cp = getCustomPropertyFile();
            if(cp.exists()) {
	            InputStream cpis = new FileInputStream(cp);
	            customProperties.load(cpis);
	            cpis.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static WflConstants getInstance(){
        if (_instance == null)
            _instance = new WflConstants();
        return _instance;
    }

    public String getProperty(String pSt)
    {
    	// Intenta obtener primero la propiedad custom, sino la encuenta busca la interna.
    	String customRes = customProperties.getProperty(pSt);
    	if(customRes!=null)
    		return customRes;
    	
        String res = properties.getProperty(pSt);
        if (res == null){
            System.out.println("La propiedad " + pSt + " no ha sido definida en " +
                    "el archivo de propiedades (" + propertyFile + ").");
        }
        return res;
    }

    public int getIntProperty(String pSt)
    {
        int res = -1;
        String p = properties.getProperty(pSt);
        if(p != null){
            try {
                res = Integer.parseInt(p);
            } catch (Exception ex) {
            }
        }
        return res;
    }

    public Color getColorProperty(String pSt)
    {
        Color res = null;
        String p = properties.getProperty(pSt);
        if(p != null)
            res = WflUtil.getColor(p.trim());
        return res;
    }
    
    /**
     * Guarda en el directorio corriente propiedades.
     * @param name Nombre de la propiedad.
     * @param value Valor de la propiedad.
     */
    public void writeProperty(String name, String value) {
    	customProperties.put(name, value);
    	try {
    		File f = getCustomPropertyFile();
			PrintWriter pw = new PrintWriter(f);
			for(Object k : customProperties.keySet()) {
				pw.print(k.toString());
				pw.print("=");
				String newValue = customProperties.get(k).toString().replaceAll("\\\\", "\\\\\\\\"); 
				pw.println(newValue);
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error al escribir las propiedades default.");
		}
    }
    
    /**
     * Obtiene el file donde debe leer / escribir las propiedades.
     * @return File donde tiene que leer y escribir.
     */
    private File getCustomPropertyFile() {
    	String homeDir = System.getProperty("user.home");
    	return new File(homeDir + File.separator + CUSTOM_PROPERTIES_FILE);
    }

}
