package crm.core.wfl.editor.gui.cmp;

/**
 * Clase que representa una traza por una funcion ejecutada.
 * Utilizada por el highlighter para determinar si se ejecuto o no
 * una funcion y con que valor de punto salio.
 * @author cm
 *
 */
public class WflFunctionTrace {
	private String functionId;
	private long tokenValue;
	
	
	/**
	 * Constructor con campos.
	 * @param functionId Id de la funcion.
	 * @param tokenValue Token value.
	 */
	public WflFunctionTrace(String functionId, long tokenValue) {
		super();
		this.functionId = functionId;
		this.tokenValue = tokenValue;
	}
	/**
	 * Devuelve el/la functionId.
	 * @return el/la functionId.
	 */
	public String getFunctionId() {
		return functionId;
	}
	/**
	 * Setea el/la functionId
	 * @param functionId El/la functionId a setear.
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	/**
	 * Devuelve el/la tokenValue.
	 * @return el/la tokenValue.
	 */
	public long getTokenValue() {
		return tokenValue;
	}
	/**
	 * Setea el/la tokenValue
	 * @param tokenValue El/la tokenValue a setear.
	 */
	public void setTokenValue(long tokenValue) {
		this.tokenValue = tokenValue;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.functionId.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WflFunctionTrace other = (WflFunctionTrace) obj;
		if (functionId == null) {
			if (other.functionId != null)
				return false;
		} else if (!functionId.equals(other.functionId))
			return false;
		return true;
	}
	
	
}
