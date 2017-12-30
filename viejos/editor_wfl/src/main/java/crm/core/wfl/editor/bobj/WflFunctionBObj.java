package crm.core.wfl.editor.bobj;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:54:44 PM
 */
public class WflFunctionBObj extends WflBObj{
    public static final String TYPE_NORMAL    = " ";
    public static final String TYPE_ITERATIVE  = "I";

    public static final String EXECUTION_SYNCHRO_SYNCHRONIC    = "S";
    public static final String EXECUTION_SYNCHRO_ASYNCHRONOUS  = "A";

    public static final String EXECUTION_TYPE_SERVER_SYNCHRONIC   = "SS";
    public static final String EXECUTION_TYPE_SERVER_ASYNCHRONOUS = "SA";
    public static final String EXECUTION_TYPE_CLIENT_INTERACTIVE  = "CI";
    public static final String EXECUTION_TYPE_SPECIAL_PROCESS  = "PE";

    public static final String SERVICE_TYPE_JAVA = "J";
    public static final String SERVICE_TYPE_JAVA_SCRIPT = "JS";
    public static final String SERVICE_TYPE_ACTION_SCRIPT  = "AS";   
    
    private String id = null;
    private String name = null;
    private String functionClass = null;
    private String executionSynchro = null;
    private String executionType = null;
    private String executionName = null;
    private String serviceType = null;
    private String script = null;
    private String role = null;
    private String roleParametricObjectId = null;
    
    private boolean carryContext = false;
    private String type = null;
    private int priority = 5;
    private String securityObject;

    private String functionClassRB = null;
    private String executionSynchroRB = null;
    private String executionTypeRB = null;
    private String executionNameRB = null;
    private String serviceTypeRB = null;
    private String scriptRB = null;
    private String roleRB = null;
    private String roleParametricObjectIdRB = null;

    private String comment = null;


    /**
     * Lista con los msg de por qué no es consistente este componente.
     * Se actualiza cuando se llama a isConsistent.
     */
    private List<String> consistentMsg = null;


    public WflFunctionBObj() {
    	this(null, null);

    }
    public WflFunctionBObj(String id, String name) {
        this.id = id;
        this.name = name;

        //valores default
        priority = 5;
        carryContext = false;
        type = TYPE_ITERATIVE;
        serviceType = SERVICE_TYPE_JAVA_SCRIPT;
        serviceTypeRB = null;
    }

    public String getFunctionClass() {
        return functionClass;
    }

    public void setFunctionClass(String functionClass) {
        this.functionClass = functionClass;
    }

    public String getExecutionTypeDescription() {
        if (EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(executionType))
            return "Cliente Interactiva";
        else if (EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(executionType))
            return "Server Asincrónico";
        else if (EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(executionType))
            return "Server Sincrónico";

        return null;
    }
    public String getExecutionTypeRBDescription() {
        if (EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(executionTypeRB))
            return "Cliente Interactiva";
        else if (EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(executionTypeRB))
            return "Server Asincrónico";
        else if (EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(executionTypeRB))
            return "Server Sincrónico";

        return null;
    }

    public String getServiceTypeDescription() {
        if (SERVICE_TYPE_JAVA.equals(serviceType))
            return "Java";
        else if (SERVICE_TYPE_JAVA_SCRIPT.equals(serviceType))
            return "JavaScript";
        return null;
    }

    public String getServiceTypeRBDescription() {
        if (SERVICE_TYPE_JAVA.equals(serviceTypeRB))
            return "Java";
        else if (SERVICE_TYPE_JAVA_SCRIPT.equals(serviceTypeRB))
            return "JavaScript";
        return null;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getBObjId()
    {
        return getId();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutionSynchroDescription() {
        if (EXECUTION_SYNCHRO_ASYNCHRONOUS.equals(executionSynchro))
            return "Asincrónica";
        else if (EXECUTION_SYNCHRO_SYNCHRONIC.equals(executionSynchro))
            return "Sincrónica";

        return null;
    }
    public String getExecutionSynchroRBDescription() {
        if (EXECUTION_SYNCHRO_ASYNCHRONOUS.equals(executionSynchroRB))
            return "Asincrónica";
        else if (EXECUTION_SYNCHRO_SYNCHRONIC.equals(executionSynchroRB))
            return "Sincrónica";

        return null;
    }

    public String getExecutionSynchro() {
        return executionSynchro;
    }

    public void setExecutionSynchro(String executionSynchro) {
        this.executionSynchro = executionSynchro;
    }

    public String getRole() {
		return role;
	}
    
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getRoleParametricObjectId() {
		return roleParametricObjectId;
	}
	
	public void setRoleParametricObjectId(String roleParametricObjectId) {
		this.roleParametricObjectId = roleParametricObjectId;
	}
	
	public String getRoleParametricObjectIdRB() {
		return roleParametricObjectIdRB;
	}
	
	public void setRoleParametricObjectIdRB(String roleParametricObjectIdRB) {
		this.roleParametricObjectIdRB = roleParametricObjectIdRB;
	}
	
	public String getRoleRB() {
		return roleRB;
	}
	
	public void setRoleRB(String roleRB) {
		this.roleRB = roleRB;
	}
	
	/**
     * Analiza si es consistente la función. Una función es consitente si tiene
     * id, nombre y clase distintos de null y no vacíos
     */
    public boolean isConsistent() {
        consistentMsg = new ArrayList<String>();
        boolean res = true;
        if (getId() == null || getId().trim().equals("")){
            res = false;
            consistentMsg .add("Falta definir el id");
        }
        if (getName() == null || getName().trim().equals("")){
            res = false;
            consistentMsg .add("Falta definir el nombre");
        }
        if (getFunctionClass() == null || getFunctionClass().trim().equals("")){
            res = false;
            consistentMsg .add("Falta definir la clase");
        }
        if (getPriority() < 1 || getPriority() > 9 ){
            res = false;
            consistentMsg .add("El valor de la prioridad debe ser mayor que 0" +
                    " y menor que 10");
        }
        if (  !(getRoleParametricObjectId() == null || getRoleParametricObjectId().trim().equals("")) &&
        		(getRole() == null || getRole().trim().equals("")) ){
        	// posee id de rol parametrico pero no tiene rol
            res = false;
            consistentMsg .add("Falta definir el rol (o quitar el id de rol parametrico)");
        }
        if (  !(getRoleParametricObjectIdRB() == null || getRoleParametricObjectIdRB().trim().equals("")) &&
        		(getRoleRB() == null || getRoleRB().trim().equals("")) ){
        	// posee id de rol parametrico pero no tiene rol (para ROLLBACK)
            res = false;
            consistentMsg .add("Falta definir el rol de rollback (o quitar el id de rol parametrico)");
        }

        return res;
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String executionName) {
        this.executionName = executionName;
    }

    public boolean isCarryContext() {
        return carryContext;
    }

    public void setCarryContext(boolean carryContext) {
        this.carryContext = carryContext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFunctionClassRB() {
        return functionClassRB;
    }

    public void setFunctionClassRB(String functionClassRB) {
        this.functionClassRB = functionClassRB;
    }

    public String getExecutionSynchroRB() {
        return executionSynchroRB;
    }

    public void setExecutionSynchroRB(String executionSynchroRB) {
        this.executionSynchroRB = executionSynchroRB;
    }

    public String getExecutionTypeRB() {
        return executionTypeRB;
    }

    public void setExecutionTypeRB(String executionTypeRB) {
        this.executionTypeRB = executionTypeRB;
    }

    public String getExecutionNameRB() {
        return executionNameRB;
    }

    public void setExecutionNameRB(String executionNameRB) {
        this.executionNameRB = executionNameRB;
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
     * Devuelve una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente. La lista representa los posibles errores
     * en la última llamada al metodo isConsistent()
     */
    public List<String> getConsistentMsg(){
        return consistentMsg;
    }
    
	/**
	 * Obtiene el tipo de servicio.
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}
	
	/**
	 * Setea el tipo de servicio.
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * Obtiene el tipo de servicio para rollback.
	 * @return the serviceTypeRB
	 */
	public String getServiceTypeRB() {
		return serviceTypeRB;
	}
	/**
	 * Setea el tipo de servicio para rollback.
	 * @param serviceTypeRB the serviceTypeRB to set
	 */
	public void setServiceTypeRB(String serviceTypeRB) {
		this.serviceTypeRB = serviceTypeRB;
	}
	/**
	 * Obtiene el/la script.
	 * @return script.
	 */
	public String getScript() {
		return script;
	}
	/**
	 * Setea el script.
	 * @param script a setear.
	 */
	public void setScript(String script) {
		this.script = script;
	}
	/**
	 * Obtiene el/la scriptRB.
	 * @return scriptRB.
	 */
	public String getScriptRB() {
		return scriptRB;
	}
	/**
	 * Setea el scriptRB.
	 * @param scriptRB a setear.
	 */
	public void setScriptRB(String scriptRB) {
		this.scriptRB = scriptRB;
	}

}
