package crm.core.wfl.editor.bobj;



/**
 * User: JPB
 * Date: Jan 25, 2006
 * Time: 1:19:43 PM
 */
public class WflConnectorBObj extends WflBObj{
    private String sourceId = null;
    private String targetId = null;

    /**
     * {@inheritDoc}
     */
    public String getBObjId()
    {
        return getSourceId() + "-" + getTargetId();
    }
    
    /**
     * Constructor.
     * 
     * @param sourceId salida del conector
     * @param targetId destino del conector
     */
    public WflConnectorBObj(String sourceId, String targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

	/**
	 * Obtiene el sourceId del objeto.
	 * @return el sourceId
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * Setea el sourceId al objeto.
	 * @param sourceId el sourceId a setear
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * Obtiene el targetId del objeto.
	 * @return el targetId
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * Setea el targetId al objeto.
	 * @param targetId el targetId a setear
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
