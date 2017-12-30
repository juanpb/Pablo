package crm.core.wfl.editor.xml;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflFunctionBObj;
import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.bobj.WflObjectConstraintBObj;
import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.bobj.WflPreFunctionBObj;
import crm.core.wfl.editor.bobj.WflProcessBObj;
import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.bobj.WflStateMarkBObj;
import crm.core.wfl.editor.bobj.WflPropertyBObj;

/**
 * Esta clase arma un xml a partir de un proceso de workflow
 *
 * User: JPB
 * Date: Jan 26, 2006
 * Time: 5:13:45 PM
 */
public class WflDefinitionXMLCreator {
    private Document document = null;


    public WflDefinitionXMLCreator(WflProcessBObj process)
            throws ParserConfigurationException {
        document = getDocument(process);
    }


    public Document getDocument(){
        return document;
    }

    private Document getDocument(WflProcessBObj process)
                throws ParserConfigurationException {
        List wflBObjs = process.getWflBObjs();
        String id = process.getId();
        String version = process.getVersion();
        String name = process.getName();
        String trace = process.getTrace();
        String comment = process.getComment();
        String so = process.getSecurityObject();
        if (trace == null)
            trace = " ";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument(null, "ProcessesWfl", null);

        Element root = doc.getDocumentElement();
        Element pw = doc.createElement("ProcessWfl");
        root.appendChild(pw);
        pw.setAttribute("processDefId", id);
        pw.setAttribute("version", version);
        pw.setAttribute("processDefName", name);
        pw.setAttribute("processTrace", trace);

        if (so != null)
            pw.setAttribute("securityObject", so);

        if (comment != null)
            addComment(doc, pw, comment);

        addMarks(doc, pw, process.getMarks());

        for (int i = 0; i < wflBObjs.size(); i++) {
            WflBObj c = (WflBObj) wflBObjs.get(i);
            addWflBObj(doc, pw, c);
        }
        
        // Agrego properties
        addProperties(doc, pw, process.getProperties());        

        // Agrego restricciones de objetos
        addConstraints(doc, pw, process.getObjectConstraints());
        
        return doc;
    }

    private void addComment(Document doc, Element parent, String comment) {
        Element node = doc.createElement("Comment");
        Text textNode = doc.createTextNode(comment);
        node.appendChild(textNode);
        parent.appendChild(node);
    }

    private void addMarks(Document doc, Element parent, List marks) {
        for (int i = 0; i < marks.size(); i++) {
            WflMarkBObj m = (WflMarkBObj) marks.get(i);
            String id = m.getId();
            String name = m.getName();
            String com = m.getCommnet();
            String deletable = m.isDeletableAsString();

            Element node = doc.createElement("Mark");
            node.setAttribute("markId", id);
            node.setAttribute("markName", name);
            node.setAttribute("markDeletable", deletable);
            if (com  != null)
                addComment(doc, node, com);
            parent.appendChild(node);
        }
    }

    /**
     * Crea los elementos correspondientes a las properties del proceso.
     * @param doc Documento.
     * @param parent Elemento padre para los elementos creados.
     * @param properties Lista de properties a crear.
     */
    private void addProperties(Document doc, Element parent, List properties) {
    	
    	if(properties==null)
    		return;
    	
        for (int i = 0; i < properties.size(); i++) {
        	WflPropertyBObj m = (WflPropertyBObj)properties.get(i);

        	Element node = doc.createElement("Property");
            node.setAttribute("name", m.getName());
            parent.appendChild(node);
        }
    }
    
    /**
     * Crea los elementos correspondientes a las constraints de objetos del proceso.
     * @param doc Documento.
     * @param parent Elemento padre para los elementos creados.
     * @param properties Lista de restricciones a crear.
     */
    private void addConstraints(Document doc, Element parent, List constraints) {
    	
    	if(constraints==null)
    		return;
    	
        for (int i = 0; i < constraints.size(); i++) {
        	WflObjectConstraintBObj m = (WflObjectConstraintBObj)constraints.get(i);

        	Element node = doc.createElement("ObjectConstraint");
            node.setAttribute("sequence", String.valueOf(i));
            node.setAttribute("label", m.getLabel());
            node.setAttribute("notNull", m.isNotNull() ? "S" : "N");
            node.setAttribute("unique", m.isUnique() ? "S" : "N");
            node.setAttribute("targetProcessId", m.getTargetProcessDefinitionId());
            parent.appendChild(node);
        }
    }    

    private void addWflBObj(Document doc, Element parent, WflBObj c) {
        if (c instanceof WflStateBObj)
            addState(doc, parent, (WflStateBObj)c);
        else if (c instanceof WflFunctionBObj)
            addFunction(doc, parent, (WflFunctionBObj)c);
        else if (c instanceof WflPreFunctionBObj)
            addPreFunction(doc, parent, (WflPreFunctionBObj)c);
        else if (c instanceof WflPosFunctionBObj)
            addPosFunction(doc, parent, (WflPosFunctionBObj)c);
    }

    private void addPosFunction(Document doc, Element parent,
                                WflPosFunctionBObj bo) {
        Element node = doc.createElement("PosFunction");
        String funId = bo.getSourceId();
        String stateId = bo.getTargetId();
        String mt = bo.getMaskType();
        String mv = bo.getMaskValue();

        node.setAttribute("functionId", funId );
        node.setAttribute("stateId", stateId);
        if (mt != null)
            node.setAttribute("maskType", mt);
        if (mv != null)
            node.setAttribute("maskValue", mv );

        parent.appendChild(node);
    }

    private void addPreFunction(Document doc, Element parent,
                                WflPreFunctionBObj bo) {
        Element node = doc.createElement("PreFunction");
        String stateId = bo.getSourceId();
        String funId = bo.getTargetId();
        node.setAttribute("stateId", stateId);
        node.setAttribute("functionId", funId );
        parent.appendChild(node);
    }

    private void addFunction(Document doc, Element parent, WflFunctionBObj bo) {
        Element node = doc.createElement("Function");
        String id = bo.getId();
        String na = bo.getName();
        String es = bo.getExecutionSynchro();
        String et = bo.getExecutionType();
        String fc = bo.getFunctionClass();
        String st = bo.getServiceType();
        String s = bo.getScript();
        String ro = bo.getRole();
        String ri = bo.getRoleParametricObjectId();
        String esRB = bo.getExecutionSynchroRB();
        String etRB = bo.getExecutionTypeRB();
        String fcRB = bo.getFunctionClassRB();
        String enRB = bo.getExecutionNameRB();
        String stRB = bo.getServiceTypeRB();
        String sRB = bo.getScriptRB();
        String roRB = bo.getRoleRB();
        String riRB = bo.getRoleParametricObjectIdRB();
        
        int pr = bo.getPriority();
        String cc = bo.isCarryContext()? " " : "N";
        String ty = bo.getType();
        String en = bo.getExecutionName();
        String co = bo.getComment();
        String so = bo.getSecurityObject();

        if (id != null)
            node.setAttribute("functionId", id);
        if (na != null)
            node.setAttribute("functionName", na );
        if (pr > 0 && pr < 10 )
            node.setAttribute("priority", pr+"" );
        if (co != null)
            addComment(doc, node, co);
        if (so != null)
            node.setAttribute("securityObject", so);

        node.setAttribute("carryContext", cc );
        if (ty != null)
            node.setAttribute("type", ty );

        //Si corresponde, agrego el tag de Rollback
        if (fcRB != null){
            Element rb = doc.createElement("Rollback");
            rb.setAttribute("executionSyncro", esRB);
            rb.setAttribute("executionType", etRB);
            rb.setAttribute("functionClass", fcRB);
            rb.setAttribute("role", roRB);
            rb.setAttribute("roleParametricObjectId", riRB);
            if (enRB != null)
                rb.setAttribute("executionName", enRB);
            rb.setAttribute("cancServiceType", stRB);
            node.appendChild(rb);

            if(sRB != null) {
            	Element rbScript = doc.createElement("RollbackScript");
            	CDATASection textNode = doc.createCDATASection(sRB);
                rbScript.appendChild(textNode);	
            	rb.appendChild(rbScript);
            }
        }

        //Si corresponde, agrego el tag de Forward
        if (es  != null && et != null && fc != null){
            Element forw = doc.createElement("Forward");
            forw.setAttribute("executionSyncro", es);
            forw.setAttribute("executionType", et);
            forw.setAttribute("functionClass", fc);
            forw.setAttribute("role", ro);
            forw.setAttribute("roleParametricObjectId", ri);
            if (en != null)
                forw.setAttribute("executionName", en);
            forw.setAttribute("procServiceType", st);
            node.appendChild(forw);
            
            if(s != null) {
            	Element forwScript = doc.createElement("ForwardScript");
                CDATASection textNode = doc.createCDATASection(s);
                forwScript.appendChild(textNode);
                forw.appendChild(forwScript);
            }
        }
        parent.appendChild(node);
    }

    private void addState(Document doc, Element parent, WflStateBObj bo) {

        Element node = doc.createElement("State");
        String id = bo.getId();
        String name = bo.getName();
        String position = bo.getPosition();
        String maskType = bo.getMaskType();
        String maskValue = bo.getMaskValue();
        String par = bo.getParallelism();
        String com = bo.getComment();
        String cancelState = bo.getCancelState();

        node.setAttribute("stateId", id);
        if (name != null)
            node.setAttribute("stateName", name );
        if (position != null)
            node.setAttribute("position", position );
        if (maskType  != null)
            node.setAttribute("maskType", maskType);
        if (maskValue  != null)
            node.setAttribute("maskValue", maskValue);
        
        if (cancelState  != null)
            node.setAttribute("cancelState", cancelState);
        else
        	node.setAttribute("cancelState", WflStateBObj.NO_CANCEL_STATE);
        
        if (com  != null)
            addComment(doc, node, com);
        if (WflStateBObj.PARALLELISM_START.equals(par)){
            Element e = doc.createElement("DynamicParalelism");
            e.setAttribute("action", WflStateBObj.PARALLELISM_START);

            String fs = bo.getFinalState();
            String cp = bo.getCommentParallelism();
            String c = bo.getClassName();

            if (fs != null)
                e.setAttribute("finalStateId", fs);

            if (cp != null)
                addComment(doc, e, cp);

            if (c != null)
                e.setAttribute("pathName", c);


            e.setAttribute("libraryName", "");
            e.setAttribute("methodName", "");
            node.appendChild(e);
        }
        else if (WflStateBObj.PARALLELISM_END.equals(par)){
            Element e = doc.createElement("DynamicParalelism");
            e.setAttribute("action", WflStateBObj.PARALLELISM_END);
            node.appendChild(e);
        }
        addStateMarks(doc, node, bo);
        parent.appendChild(node);
    }

    private void addStateMarks(Document doc, Element parent, WflStateBObj state) {
        List stateMarks = state.getStateMarks();
        for (int i = 0; i < stateMarks.size(); i++) {
            WflStateMarkBObj m = (WflStateMarkBObj) stateMarks.get(i);

            String action = m.getAction();
            String id = m.getMark().getId();

            Element node = doc.createElement("StateMark");
            node.setAttribute("markId", id);
            node.setAttribute("action", action);
            parent.appendChild(node);
        }
    }
}
