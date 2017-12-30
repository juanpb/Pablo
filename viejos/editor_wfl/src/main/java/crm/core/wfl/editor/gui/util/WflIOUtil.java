package crm.core.wfl.editor.gui.util;

import crm.core.wfl.editor.bobj.WflProcessBObj;
import crm.core.wfl.editor.xml.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: JPB
 * Date: Jan 25, 2006
 * Time: 3:46:55 PM
 */
public class WflIOUtil {

    public static List getLocationsBObj(File file)
            throws ParserConfigurationException, SAXException, IOException {
        if (file == null || !file.exists())
            return null;

        SAXParserFactory spf = SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser sp = spf.newSAXParser();
        WflLocationXMLHandler handler = new WflLocationXMLHandler();
        sp.parse(file, handler);

        return handler.getLocations();
    }

    static public WflProcessBObj getProcessBObj(File file)
            throws IOException, ParserConfigurationException, SAXException,
            SAXParseException{
        WflProcessBObj res = new WflProcessBObj();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser sp = spf.newSAXParser();
        WflDefinitionXMLHandler handler = new WflDefinitionXMLHandler();
        sp.parse(file, handler);
        res.setId(handler.getProcessId());
        res.setName(handler.getProcessName());
        res.setTrace(handler.getProcessTrace());
        res.setComment(handler.getProcessComment());
        res.setSecurityObject(handler.getProcessSecurityObject());
        res.setVersion(handler.getProcessVersion());
        res.setWflBObjs(handler.getWflBObjs());
        res.setMarks(handler.getMarks());
        res.setProperties(handler.getProperties());
        res.setObjectConstraints(handler.getObjectConstraints());

        return res;
    }

    public static void saveProcess(File file, WflProcessBObj process)
            throws Exception
    {
        Document doc = getXML(process);
        output(doc, file);
    }

    public static Document getXML(WflProcessBObj process)
            throws ParserConfigurationException {
        WflDefinitionXMLCreator xmlCreator = new WflDefinitionXMLCreator(process);
        return  xmlCreator.getDocument();
    }

    public static void saveLocations(File file, List locations, String id,
                                     String version, String name)
            throws Exception
    {
        WflLocationXMLCreator xmlCreator = new WflLocationXMLCreator(locations,
                id, version ,name);
        Document doc = xmlCreator.getDocument();
        output(doc, file);
    }

    static private void output(Document doc, File file) throws Exception {
        WflXMLWriter.output(doc, file);
    }

}
