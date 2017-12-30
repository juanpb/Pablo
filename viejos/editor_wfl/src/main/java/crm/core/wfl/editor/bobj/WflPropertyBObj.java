package crm.core.wfl.editor.bobj;

/**
 * Propiedades asociadas al proceso.
 * @author CM
 */
public class WflPropertyBObj extends WflBObj{

    private String name = null;

    /**
     * Constructor vacio.
     */
    public WflPropertyBObj() {
    	super();
    }

    /**
     * Constructor con parametros.
     * @param name Nombre de property.
     */
	public WflPropertyBObj(String name) {
		super();
		this.name = name;
	}
	
    /**
     * Representacion del alias como un string.
     * @return String con el alias.
     */
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(name);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof WflPropertyBObj))
            return false;
        WflPropertyBObj bo = (WflPropertyBObj)obj;
        if (bo!= null && bo.getName()!=null && name!=null)
            return bo.getName().equals(name);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
    	if(name==null)
    		return 0;
        return name.hashCode();
    }


	/**
	 * Obtiene el name.
	 * @return Retorna el name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setea el name.
	 * @param name a setear.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene el id como un String.
	 * @return Id.
	 */
	@Override
	public String getBObjId() {
		return name;
	}



}
