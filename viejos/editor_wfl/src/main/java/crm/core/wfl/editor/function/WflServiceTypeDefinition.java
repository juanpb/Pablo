package crm.core.wfl.editor.function;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

/**
 * Definicion de un tipo de servicio.
 * @author carlos
 *
 */
public class WflServiceTypeDefinition {

    public WflServiceTypeDefinition() {
    }

    /**
	 * Dado un tipo de servicio, obtiene la definicion.
	 * @param type Tipo de Servicio.
	 * @return Definicion. Si no la encuentra devuelve null.
	 */
	public static WflServiceTypeDefinition getTypeDefinition(String type) {
		for(WflServiceTypeDefinition t : SERVICE_TYPES) {
			if(t.getId().equals(type))
				return t;
		}
		return null;
	}
	
	/**
	 * Servicios por defecto.
	 */
	public static WflServiceTypeDefinition SERVICE_TYPES[] = null;
	
	static
	{
        System.out.println("asdf");
		// Levanto la definicion.
		XStream xs = new XStream();
		InputStream is = xs.getClass().getClassLoader().getResourceAsStream("crm/core/wfl/editor/WflServiceTypeDefinition.xml");
		if(is==null)
			throw new RuntimeException("No se encontro el archivo de definicion de tipos de servicio " + 
					"crm/core/wfl/editor/WflServiceTypeDefinition.xml");
        try {
            SERVICE_TYPES = (WflServiceTypeDefinition[])xs.fromXML(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

	private String  id;
	private String  description;
	private boolean scriptable;
	private String  scriptCompiler;
	private String  scriptEditor;
	private String  defaultFunction;
	private String  defaultScript;
	
	/**
	 * Devuelve el/la defaultScript.
	 * @return el/la defaultScript.
	 */
	public String getDefaultScript() {
		return defaultScript;
	}
	/**
	 * Setea el/la defaultScript.
	 * @param defaultScript El/la defaultScript a setear.
	 */
	public void setDefaultScript(String defaultScript) {
		this.defaultScript = defaultScript;
	}
	/**
	 * Obtiene el/la defaultFunction.
	 * @return defaultFunction.
	 */
	public String getDefaultFunction() {
		return defaultFunction;
	}
	/**
	 * Setea el defaultFunction.
	 * @param defaultFunction a setear.
	 */
	public void setDefaultFunction(String defaultFunction) {
		this.defaultFunction = defaultFunction;
	}
	/**
	 * Obtiene el/la description.
	 * @return description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Setea el description.
	 * @param description a setear.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Obtiene el/la id.
	 * @return id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * Setea el id.
	 * @param id a setear.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Obtiene el/la scriptable.
	 * @return scriptable.
	 */
	public boolean isScriptable() {
		return scriptable;
	}
	/**
	 * Setea el scriptable.
	 * @param scriptable a setear.
	 */
	public void setScriptable(boolean scriptable) {
		this.scriptable = scriptable;
	}
	
	/**
	 * Crea una definicion de tipo de servicio.
	 * @param id Id del tipo de servicio.
	 * @param description Descripcion a mostrar.
	 * @param scriptable Indica si el servicio se define con script o se implementa directamente en una clase java.
	 * @param defaultFunction Indica la funcion java por defaulta usar.
	 * @param defaultScript Script por defecto a setear cuando se crea uno nuevo.
	 */
	public WflServiceTypeDefinition(String id, 
									String description, 
									boolean scriptable, 
									String defaultFunction,
									String defaultScript) {
		super();
		this.id = id;
		this.description = description;
		this.scriptable = scriptable;
		this.defaultFunction = defaultFunction;
		this.defaultScript = defaultScript;
	}
	/**
	 * Devuelve el/la scriptCompiler.
	 * @return el/la scriptCompiler.
	 */
	public String getScriptCompiler() {
		return scriptCompiler;
	}
	/**
	 * Setea el/la scriptCompiler
	 * @param scriptCompiler El/la scriptCompiler a setear.
	 */
	public void setScriptCompiler(String scriptCompiler) {
		this.scriptCompiler = scriptCompiler;
	}
	/**
	 * Devuelve el/la scriptEditor.
	 * @return el/la scriptEditor.
	 */
	public String getScriptEditor() {
		return scriptEditor;
	}
	/**
	 * Setea el/la scriptEditor
	 * @param scriptEditor El/la scriptEditor a setear.
	 */
	public void setScriptEditor(String scriptEditor) {
		this.scriptEditor = scriptEditor;
	}
	

}
