package crm.core.wfl.editor.bobj;

import java.util.List;
import java.util.ArrayList;

/**
 * User: JPB
 * Date: Jan 25, 2006
 * Time: 2:46:41 PM
 */
public class WflProcessBObj {
    private String id = null;
    private String name = null;
    private String trace = null;
    private String version = "1.00";
    private String securityObject = null;
    private String comment = null;

    private List<WflBObj> wflBObjs = null;
    private List<WflMarkBObj> marks = null;
    private List<WflPropertyBObj> properties = null;
    private List objectConstraints = null;    

    public WflProcessBObj() {
        this(null);
    }

    public WflProcessBObj(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setWflBObjs(List<WflBObj> wflBObjs) {
        this.wflBObjs = wflBObjs;
    }

    /**
     * @return una lista de WflBObj
     */
    public List<WflBObj> getWflBObjs() {
        return wflBObjs;
    }

    public List<WflBObj> getWflBObjs(Class type) {
        List<WflBObj> res = new ArrayList<WflBObj>();
        for (int i = 0; i < wflBObjs.size(); i++) {
        	WflBObj o = wflBObjs.get(i);
            if(type.isInstance(o))
                res.add(o);
        }
        return res;
    }

    /**
     *
     * @param marks lista de WflMarkBObj
     */
    public void setMarks(List<WflMarkBObj> marks) {
        this.marks = marks;
    }

    /**
     *
     * @return una lista de WflMarkBObj
     */
    public List<WflMarkBObj> getMarks() {
        return marks;
    }

    public String getSecurityObject() {
        return securityObject;
    }

    public void setSecurityObject(String securityObject) {
        this.securityObject = securityObject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

	/**
	 * Obtiene las properties.
	 * @return Retorna las properties.
	 */
	public List<WflPropertyBObj> getProperties() {
		return properties;
	}

	/**
	 * Setea las properties.
	 * @param properties Properties a setear.
	 */
	public void setProperties(List<WflPropertyBObj> properties) {
		this.properties = properties;
	}

	/**
	 * Devuelve el/la objectConstraints.
	 * @return el/la objectConstraints.
	 */
	public List getObjectConstraints() {
		return objectConstraints;
	}

	/**
	 * Setea el/la objectConstraints
	 * @param objectConstraints El/la objectConstraints a setear.
	 */
	public void setObjectConstraints(List objectConstraints) {
		this.objectConstraints = objectConstraints;
	}
}
