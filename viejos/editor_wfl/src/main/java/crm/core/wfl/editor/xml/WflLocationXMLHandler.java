package crm.core.wfl.editor.xml;

import crm.core.wfl.editor.bobj.WflLocationBObj;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



/**
 * User: JPB
 * Date: Jan 27, 2006
 * Time: 2:55:37 PM
 */
public class WflLocationXMLHandler extends DefaultHandler{
    private List<WflLocationBObj> locations = new ArrayList<WflLocationBObj>();

    public WflLocationXMLHandler() {
    }

    public List getLocations(){
        return locations;
    }

    public void startElement (String uri, String localName,
             String qName, Attributes attrs){
       if ("Location".equals(qName))
           viewLocation(attrs);
       else if("Vertice".equals(qName))
    	   viewVertice(attrs);
    }

    private void viewVertice(Attributes attrs) {
        String x = attrs.getValue("x");
        String y = attrs.getValue("y");
        try {
            Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));
            WflLocationBObj bo = (WflLocationBObj)locations.get(locations.size()-1);
            if(bo.getVertices()==null)
            	bo.setVertices(new ArrayList<Point>());
            bo.getVertices().add(point);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ;
        }
	}

	private void viewLocation(Attributes attrs) {
        String id = attrs.getValue("componentId");
        String x = attrs.getValue("x");
        String y = attrs.getValue("y");
        try {
            Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));
            WflLocationBObj bo = new WflLocationBObj(id, point);
            locations.add(bo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ;
        }
    }

}
