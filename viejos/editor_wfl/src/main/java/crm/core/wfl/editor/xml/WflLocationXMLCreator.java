package crm.core.wfl.editor.xml;

import crm.core.wfl.editor.bobj.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.awt.*;

/**
 * User: JPB
 * Date: Jan 26, 2006
 * Time: 5:13:45 PM
 */
public class WflLocationXMLCreator {
    private Document document = null;


    public WflLocationXMLCreator(List locations, String id, String version,
                                 String name)
            throws ParserConfigurationException {
        document = getDocument(locations, id, version, name);
    }


    public Document getDocument(){
        return document;
    }

    private Document getDocument(List locations, String id, String version,
                                 String name)
                throws ParserConfigurationException {
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

        for (int i = 0; i < locations.size(); i++) {
            WflLocationBObj bo = (WflLocationBObj) locations.get(i);
            addWflBObj(doc, pw, bo);
        }
        return doc;
    }

    private void addWflBObj(Document doc, Element parent, WflLocationBObj bo) {
        Element node = doc.createElement("Location");
        String id = bo.getBObjId();
        Point point = bo.getPoint();

        node.setAttribute("componentId", id );
        node.setAttribute("x", point.x+"");
        node.setAttribute("y", point.y+"");
        
        if(bo.getVertices()!=null) {
        	for(Point p : bo.getVertices()) {
                Element verticeNode = doc.createElement("Vertice");
                node.appendChild(verticeNode);
                verticeNode.setAttribute("x", p.x+"");
                verticeNode.setAttribute("y", p.y+"");                
        	}
        }
        
        parent.appendChild(node);
    }
}
