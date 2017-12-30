package crm.core.wfl.editor.bobj;

import java.util.List;
import java.util.ArrayList;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 12:54:44 PM
 */
public class WflStateBObj extends WflBObj{
    public static final String MASK_TYPE_AND = "+";
    public static final String MASK_TYPE_OR = "-";

    public static final String PARALLELISM_START   = "C";
    public static final String PARALLELISM_END   = "F";
    public static final String PARALLELISM_NONE   = "N";

    public static final String POSITION_INITIAL = "I";
    public static final String POSITION_FINAL   = "F";

    public static final String CANCEL_STATE = "S";
    public static final String NO_CANCEL_STATE = "N";

    private String id = null;
    private String name = null;
    private String position = null;
    private String maskType = null;
    private String maskValue = null;
    private String parallelism = PARALLELISM_NONE;
    private String className = null;
    private String finalState = null;
    private String libraryName = null;
    private String methodName = null;
    private String comment = null;
    private String commentParallelism = null;
    private String cancelState = NO_CANCEL_STATE;

    private List<WflStateMarkBObj> stateMarks = new ArrayList<WflStateMarkBObj>();

    /**
     * Lista con los msg de por qué no es consistente este componente.
     * Se actualiza cuando se llama a isConsistent.
     */
    private List<String> consistentMsg = null;

    public WflStateBObj(){
    }

    public WflStateBObj(String id, String name, String position, 
                        String maskType, String valueType, String cancelState) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.maskType = maskType;
        this.maskValue = valueType;
        this.cancelState = cancelState;
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

    public String getPosition() {
        return position;
    }

    public String getPositionDescription() {
        if (POSITION_INITIAL.equals(position))
            return "Inicial";
        else if (POSITION_FINAL.equals(position))
            return "Final";

        return null;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMaskType() {
        return maskType;
    }

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public String getMaskValue() {
        return maskValue;
    }

    public void setMaskValue(String maskValue) {
        this.maskValue = maskValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParallelism() {
        return parallelism;
    }

    public void setParallelism(String parallelism) {
        this.parallelism = parallelism;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Asigna el nombre de la clase del paralelismo.
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Analiza si es consistente la función. Una función es consitente si tiene
     * id, nombre y clase distintos de null y no vacíos
     * @return true si es consistente.
     */
    public boolean isConsistent() {
        consistentMsg = new ArrayList<String>();
        boolean res = true;
        if (getId() == null || getId().trim().equals("")){
            res = false;
            consistentMsg.add("Falta definir el id");
        }
        if (getName() == null || getName().trim().equals("")){
            res = false;
            consistentMsg.add("Falta definir el nombre");
        }
        if (getMaskType() != null && !getMaskType().trim().equals("")){
            String pos = getPosition();
            if (!(POSITION_INITIAL.equals(pos) || POSITION_FINAL.equals(pos))){
                res = false;
                consistentMsg.add("La máscara sólo tiene sentido en estados finales o iniciales");
            }
            else{
                if (getMaskValue() == null || getMaskValue().trim().equals("")){
                    res = false;
                    consistentMsg .add("Si tiene máscara tiene que tener valor");
                }
            }
        }
        return res;
    }


    /**
     * Devuelve una lista con los mensajes de por qué no es consistente o una
     * lista vacía si es consistente. La lista representa los posibles errores
     * en la última llamada al metodo isConsistent()
     * @return   una lista con los mensajes de por qué no es consistente.
     */
    public List<String> getConsistentMsg(){
        return consistentMsg;
    }

    public void addStateMark(WflStateMarkBObj mark) {
        stateMarks.add(mark);
    }

    public List<WflStateMarkBObj> getStateMarks() {
        return stateMarks;
    }

    /**
     * Asigna una nueva lista de hitos
     * @param stateMarks
     */
    public void setStateMarks(List<WflStateMarkBObj> stateMarks) {
        this.stateMarks = stateMarks;
    }

    public boolean hasMark(WflMarkBObj mark) {
        if (mark == null)
            return false;
        List ms = getStateMarks();
        for (int i = 0; i < ms.size(); i++) {
            WflMarkBObj miles =
                    ((WflStateMarkBObj) ms.get(i)).getMark();
            if (mark.equals(miles))
                return true;
        }
        return false;
    }

    /**
     * Si este estado tiene el hito recibido como parámetro => lo elimina.
     *
     * @param mark
     * @return true si lo pudo eliminar.
     */
    public boolean remove(WflMarkBObj mark) {
        List stateMarks = getStateMarks();
        for (int i = 0; i < stateMarks.size(); i++) {
            WflStateMarkBObj x = (WflStateMarkBObj) stateMarks.get(i);
            if (x.getMark().equals(mark)){
                stateMarks.remove(x);
                return true;
            }
        }
        return false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentParallelism() {
        return commentParallelism;
    }

    public void setCommentParallelism(String commentParallelism) {
        this.commentParallelism = commentParallelism;
    }

    /**
     * Indica si el estado dispara la generacion de una cancelacion.
     * @return true si el proceso tiene que ser cancelado al llegar a este estado.
     */
	public String getCancelState() {
		return cancelState;
	}
	
	/**
	 * Indica si el estado genera la cancelacion del proceso o no.
	 * @param cancelState Indica si general a la cancelacion del proceso.
	 */
	public void setCancelState(String cancelState) {
		this.cancelState = cancelState;
	}
}
