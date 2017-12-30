package crm.core.wfl.editor.xml;

import crm.core.wfl.editor.bobj.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Jan 25, 2006
 * Time: 4:46:31 PM
 * @noinspection EmptyCatchBlock
 */
public class WflDefinitionXMLHandler extends DefaultHandler{

    private List<WflStateBObj> states = new ArrayList<WflStateBObj>();
    private List<WflFunctionBObj> functions = new ArrayList<WflFunctionBObj>();
    private List<WflPreFunctionBObj> preFunctions = new ArrayList<WflPreFunctionBObj>();
    private List<WflPosFunctionBObj> posFunctions = new ArrayList<WflPosFunctionBObj>();
    private List<WflPropertyBObj> properties = new ArrayList<WflPropertyBObj>();
    private List<WflObjectConstraintBObj> constraints = new ArrayList<WflObjectConstraintBObj>();    

    /**
     *lista de WflMarkBObj
     */
    private List<StateMark> stateMarks = new ArrayList<StateMark>();
    private List<WflMarkBObj> marks = new ArrayList<WflMarkBObj>();

    private String processId = null;
    private String processVersion = null;
    private String processName = null;
    private String processTrace = null;
    private String processComment = null;
    private String processSecurityObject = null;

    /**
     * El último state leída
     */
    private WflStateBObj state;

    /**
     * La última function leída
     */
    private WflFunctionBObj function;
    private WflMarkBObj mark;
    private boolean isAComment = false;
    private boolean isAScript = false;
    private boolean isAScriptRB = false;
    private boolean isInDynamicPar;

    public WflDefinitionXMLHandler() {

    }

    public void characters (char buf [], int offset, int len) throws SAXException{
        char[] dest = new char[len];
        System.arraycopy(buf, offset, dest, 0, len);
        String z = new String(dest);
        if (isAComment){
            addComment(z);
        }
        if (isAScript) {
        	if(function!=null)
        		if(function.getScript()==null)
        			function.setScript(z);
        		else
        			function.setScript(function.getScript() + z);
        }
        if (isAScriptRB) {
        	if(function!=null)
        		if(function.getScriptRB()==null)
        			function.setScriptRB(z);
        		else
        			function.setScriptRB(function.getScriptRB() + z);
        }
    }

    private void addComment(String c){
        if (mark != null){
        	if(mark.getCommnet()==null)
        		mark.setCommnet(c);
        	else
        		mark.setCommnet(mark.getCommnet()+c);
        }
        else if (isInDynamicPar){
        	if(state.getCommentParallelism()==null)
        		state.setCommentParallelism(c);
        	else
        		state.setCommentParallelism(state.getCommentParallelism()+c);
        }
        else if (state != null){
        	if(state.getComment()==null)
        		state.setComment(c);
        	else
        		state.setComment(state.getComment()+c);
        }
        else if (function != null){
        	if(function.getComment()==null)
        		function.setComment(c);
        	else
        		function.setComment(function.getComment()+c);
        }
        else{//tiene que ser un comentario del proceso
            processComment = c;
        }

    }
    public void endElement(String uri, String localName, String qName) {
        if ("State".equals(qName))
            state = null;
        else if ("Function".equals(qName))
            function = null;
        else if ("RollbackScript".equals(qName))
        	isAScriptRB = false;
        else if ("ForwardScript".equals(qName))
        	isAScript = false;
        else if ("ProcessWfl".equals(qName))
            calculateStateMarks();
        else if ("Comment".equals(qName))
            isAComment = false;
        else if ("Mark".equals(qName))
            mark = null;
        else if ("DynamicParalelism".equals(qName))
            isInDynamicPar = false;
    }

    public void startElement (String uri, String localName,
              String qName, Attributes attrs){
        if ("ProcessWfl".equals(qName))
            viewProcess(attrs);
        else if ("State".equals(qName))
            viewState(attrs);
        else if ("StateMark".equals(qName))
            viewStateMark(attrs);
        else if ("Mark".equals(qName))
            viewMark(attrs);
        else if ("DynamicParalelism".equals(qName))
            viewDynPar(attrs);
        else if ("Comment".equals(qName))
            viewComment();
        else if ("Function".equals(qName))
            viewFunction(attrs);
        else if ("Rollback".equals(qName))
            viewRollback(attrs);
        else if ("RollbackScript".equals(qName))
        	isAScriptRB = true;
        else if ("Forward".equals(qName))
        	viewForward(attrs);
        else if ("ForwardScript".equals(qName))
        	isAScript = true;
        else if ("PreFunction".equals(qName))
            viewPreFunction(attrs);
        else if ("PosFunction".equals(qName))
            viewPosFunction(attrs);
        else if ("Property".equals(qName))
            viewProperties(attrs);
        else if ("ObjectConstraint".equals(qName))
            viewObjectConstraints(attrs);
    }

    private void viewComment() {
        isAComment = true;
    }

    /**
     * Agrega una property a la lista de properties.
     * @param attrs Atributos del elemento.
     */
    private void viewProperties(Attributes attrs) {
        String name = attrs.getValue("name");
        WflPropertyBObj property = new WflPropertyBObj(name);
        properties.add(property);
    }

    /**
     * Agrega una restriccion a nivel objetos a la lista de restricciones.
     * @param attrs Atributos del elemento.
     */
    private void viewObjectConstraints(Attributes attrs) {
    	// label="t2" notNull="S" unique="N" targetProcessId=""/>
        String label = attrs.getValue("label");
        boolean notNull = attrs.getValue("notNull").toUpperCase().equals("S");
        boolean unique = attrs.getValue("unique").toUpperCase().equals("S");
        String targetProcessId = attrs.getValue("targetProcessId");        
        WflObjectConstraintBObj constraint = new WflObjectConstraintBObj(label, notNull, unique, targetProcessId);
        constraints.add(constraint);
    }

    private void viewMark(Attributes attrs) {
        String id = attrs.getValue("markId");
        String name = attrs.getValue("markName");
        String deletable = attrs.getValue("markDeletable");
        boolean deletable2 = "s".equalsIgnoreCase(deletable);
        mark = new WflMarkBObj(id, name, deletable2);
        marks.add(mark);
    }

    private void viewStateMark(Attributes attrs) {
        if (state == null){
            String msg = "Se definió un tag 'StateMark' que no está " +
                    "adentro de un 'State'";
            throw new RuntimeException(msg);
        }

        String id = attrs.getValue("markId");
        String action = attrs.getValue("action");
        String stateId = state.getId();
        stateMarks.add(new StateMark(stateId, id, action));
    }

    private void viewDynPar(Attributes attrs) {
        if (state == null){
            String msg = "Se definió un tag 'DynamicParalelism' que no está " +
                    "adentro de un 'State'";
            throw new RuntimeException(msg);
        }
        isInDynamicPar = true;
        String a = attrs.getValue("action");
        String f = attrs.getValue("finalStateId");
        String p = attrs.getValue("pathName");
        String l = attrs.getValue("libraryName");
        String m = attrs.getValue("methodName");

        state.setParallelism(a);
        state.setFinalState(f);
        state.setClassName(p);
        state.setLibraryName(l);
        state.setMethodName(m);
    }

    private void viewPosFunction(Attributes attrs) {
        String s = attrs.getValue("stateId");
        String f = attrs.getValue("functionId");
        String mt = attrs.getValue("maskType");
        String mv = attrs.getValue("maskValue");
        WflPosFunctionBObj pf = new WflPosFunctionBObj(f, s, mt, mv);
        posFunctions.add(pf);
    }

    private void viewPreFunction(Attributes attrs) {
        String s = attrs.getValue("stateId");
        String f = attrs.getValue("functionId");
        WflPreFunctionBObj pf = new WflPreFunctionBObj(s, f);
        preFunctions.add(pf);
    }

    private void viewRollback(Attributes attrs) {
        if (function == null){
            String msg = "Se definió un tag 'Backward' que no está adentro " +
                    "de un 'Function'";
            throw new RuntimeException(msg);
        }
        String s = attrs.getValue("executionSyncro");
        String t = attrs.getValue("executionType");
        String c = attrs.getValue("functionClass");
        String n = attrs.getValue("executionName");
        String st = attrs.getValue("cancServiceType");
        String ro = attrs.getValue("role");
        String ri = attrs.getValue("roleParametricObjectId");

        function.setExecutionTypeRB(t);
        function.setExecutionSynchroRB(s);
        function.setFunctionClassRB(c);
        function.setExecutionNameRB(n);
        function.setServiceTypeRB(st);
        function.setRoleRB(ro);
        function.setRoleParametricObjectIdRB(ri);
    }


    private void viewForward(Attributes attrs) {
        if (function == null){
            String msg = "Se definió un tag 'Forward' que no está adentro " +
                    "de un 'Function'";
            throw new RuntimeException(msg);
        }
        String s = attrs.getValue("executionSyncro");
        String t = attrs.getValue("executionType");
        String c = attrs.getValue("functionClass");
        String e = attrs.getValue("executionName");
        String st = attrs.getValue("procServiceType");
        String ro = attrs.getValue("role");
        String ri = attrs.getValue("roleParametricObjectId");

        function.setExecutionType(t);
        function.setExecutionSynchro(s);
        function.setFunctionClass(c);
        function.setExecutionName(e);
        function.setServiceType(st);
        function.setRole(ro);
        function.setRoleParametricObjectId(ri);
    }

    private void viewFunction(Attributes attrs) {
        String id = attrs.getValue("functionId");
        String name = attrs.getValue("functionName");
        String priority = attrs.getValue("priority");
        String cc = attrs.getValue("carryContext");
        String type = attrs.getValue("type");
        String so = attrs.getValue("securityObject");


        function = new WflFunctionBObj(id, name);
        boolean b = cc == null || !cc.equalsIgnoreCase("n");
        function.setCarryContext(b);
        if (type == null)
            type = WflFunctionBObj.TYPE_ITERATIVE; //valor default
        function.setType(type);

        function.setSecurityObject(so);
        int p = 5;
        if (priority != null){
            try{
                p = Integer.parseInt(priority);
            }
            catch(Exception e){}
        }
        function.setPriority(p);
        functions.add(function);
    }

    private void viewState(Attributes attrs) {
        String id = attrs.getValue("stateId");
        String name = attrs.getValue("stateName");
        String position = attrs.getValue("position");
        String mt = attrs.getValue("maskType");
        String vt = attrs.getValue("maskValue");
        String cancelState = attrs.getValue("cancelState");

        if(cancelState==null || cancelState.trim().length()==0)
        	cancelState = WflStateBObj.NO_CANCEL_STATE;

        state = new WflStateBObj(id, name, position, mt, vt, cancelState);
        states.add(state);
    }

    private void viewProcess(Attributes attrs) {
        processId = attrs.getValue("processDefId");
        processVersion = attrs.getValue("version");
        processName = attrs.getValue("processDefName");
        processTrace = attrs.getValue("processTrace");
        processSecurityObject = attrs.getValue("securityObject");
    }

    public List<WflBObj> getWflBObjs()
    {
        List<WflBObj> l = new ArrayList<WflBObj>();
        l.addAll(states);
        l.addAll(functions);
        l.addAll(preFunctions);
        l.addAll(posFunctions);
        return l;
    }

    public String getProcessName() {
        return processName;
    }    

    public String getProcessTrace() {
        return processTrace;
    }
    public String getProcessSecurityObject() {
        return processSecurityObject;
    }

    public String getProcessComment() {
        return processComment;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public String getProcessId() {
        return processId;
    }

    public List<WflMarkBObj> getMarks(){
        return marks;
    }

    /**
     * Asigna los StateMark a los estados que correspondan.
     */
    private void calculateStateMarks() {
        for (int i = 0; i < stateMarks.size(); i++) {
            StateMark sm = (StateMark) stateMarks.get(i);
            WflMarkBObj m = getMark(sm.id);
            WflStateBObj s = getState(sm.stateId);

            WflStateMarkBObj markStateBObj = new WflStateMarkBObj(sm.action, m);
            s.addStateMark(markStateBObj);
        }
    }

    private WflMarkBObj getMark(String markId) {
        if (markId == null)
            return null;

        for (int i = 0; i < marks.size(); i++) {
            WflMarkBObj s = (WflMarkBObj) marks.get(i);
            if (markId.equals(s.getId()))
                return s;
        }
        return null;
    }

    private WflStateBObj getState(String stateId) {
        if (stateId == null)
            return null;
        for (int i = 0; i < states.size(); i++) {
            WflStateBObj s = (WflStateBObj) states.get(i);
            if (stateId.equals(s.getId()))
                return s;
        }
        return null;
    }

    private class StateMark{
        String stateId;
        String id;
        String action;

        public StateMark(String stateId, String id, String action) {
            this.stateId = stateId;
            this.id = id;
            this.action = action;
        }
    }

	/**
	 * Obtiene las properties.
	 * @return Retorna las properties.
	 */
	public List<WflPropertyBObj> getProperties() {
		return this.properties;
	}
	
	/**
	 * Obtiene las restricciones de objetos.
	 * @return Restricciones de objetos.
	 */
	public List getObjectConstraints() {
		return this.constraints;
	}
}