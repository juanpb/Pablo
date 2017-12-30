package crm.core.wfl.editor.bobj;

/**
 * Restriccion a nivel objeto para poder iniciar un proceso.
 * @author CM
 */
public class WflObjectConstraintBObj extends WflBObj{
	private static int nextVirtualId = 0;
	private int virtualId = nextVirtualId++;
	private String label;
	private boolean notNull;
	private boolean unique;
	private String targetProcessDefinitionId;
	
	
	/**
	 * Constructor con los campos.
	 * @param label Etiqueta.
	 * @param notNull Indica puede o no ser nulo.
	 * @param unique Indica si tiene que ser unico.
	 * @param targetProcessDefinitionId Proceso donde chequea que no exista.
	 */
	public WflObjectConstraintBObj(String label, boolean notNull, boolean unique, String targetProcessDefinitionId) {
		super();
		this.label = label;
		this.notNull = notNull;
		this.unique = unique;
		this.targetProcessDefinitionId = targetProcessDefinitionId;
	}
	
	
	/**
	 * Constructor vacio.
	 */
	public WflObjectConstraintBObj() {
		super();
	}



	/**
	 * Devuelve el/la label.
	 * @return el/la label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * Setea el/la label
	 * @param label El/la label a setear.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * Devuelve el/la notNull.
	 * @return el/la notNull.
	 */
	public boolean isNotNull() {
		return notNull;
	}
	/**
	 * Setea el/la notNull
	 * @param notNull El/la notNull a setear.
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * Devuelve el/la targetProcessDefinitionId.
	 * @return el/la targetProcessDefinitionId.
	 */
	public String getTargetProcessDefinitionId() {
		return targetProcessDefinitionId;
	}
	/**
	 * Setea el/la targetProcessDefinitionId
	 * @param targetProcessDefinitionId El/la targetProcessDefinitionId a setear.
	 */
	public void setTargetProcessDefinitionId(String targetProcessDefinitionId) {
		this.targetProcessDefinitionId = targetProcessDefinitionId;
	}
	/**
	 * Devuelve el/la unique.
	 * @return el/la unique.
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * Setea el/la unique
	 * @param unique El/la unique a setear.
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	/**
	 * Obtiene el id de este objeto.
	 */
    public String getBObjId() {
    	return String.valueOf(this.virtualId);
    }
}
