package p.util.xml;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Map;

//import org.apache.crimson.tree.TextNode;

/**
 * User: JPB
 * Date: Jan 31, 2006
 * Time: 12:44:27 PM
 */
public class XMLUtil {

    private static final String TAB  = "    ";
    private static final String NEW_LINE = "\n";

    /**
     * Guarda la equivalencia para escapeo de caracteres.
     */
    private static Map scapes = new HashMap();

    static
    {
        scapes.put('<', "&lt;".toCharArray());
        scapes.put('>', "&gt;".toCharArray());
        scapes.put('&', "&amp;".toCharArray());
        scapes.put('"', "&quot;".toCharArray());
        scapes.put('\'', "&apos;".toCharArray());
        scapes.put('"', "&#34;".toCharArray());
        scapes.put('&', "&#38;".toCharArray());
        scapes.put('<', "&#60;".toCharArray());
        scapes.put('>', "&#62;".toCharArray());
        scapes.put('@', "&#64;".toCharArray());
        scapes.put('�', "&#128;".toCharArray());
        scapes.put('�', "&#171;".toCharArray());
        scapes.put('�', "&#187;".toCharArray());
        scapes.put('�', "&#161;".toCharArray());
        scapes.put('�', "&#191;".toCharArray());
        scapes.put('�', "&#162;".toCharArray());
        scapes.put('�', "&#163;".toCharArray());
        scapes.put('�', "&#164;".toCharArray());
        scapes.put('�', "&#165;".toCharArray());
        scapes.put('�', "&#166;".toCharArray());
        scapes.put('�', "&#167;".toCharArray());
        scapes.put('�', "&#169;".toCharArray());
        scapes.put('�', "&#174;".toCharArray());
        scapes.put('�', "&#183;".toCharArray());
        scapes.put('�', "&#188;".toCharArray());
        scapes.put('�', "&#189;".toCharArray());
        scapes.put('�', "&#190;".toCharArray());
        scapes.put('�', "&#185;".toCharArray());
        scapes.put('�', "&#178;".toCharArray());
        scapes.put('�', "&#179;".toCharArray());
        scapes.put('�', "&#176;".toCharArray());
        scapes.put('�', "&#215;".toCharArray());
        scapes.put('�', "&#247;".toCharArray());
        scapes.put('�', "&#177;".toCharArray());
        scapes.put('"', "&#34;".toCharArray());
        scapes.put('&', "&#38;".toCharArray());
        scapes.put('�', "&#180;".toCharArray());
        scapes.put('�', "&#181;".toCharArray());
        scapes.put('�', "&#182;".toCharArray());
        scapes.put('�', "&#184;".toCharArray());
        scapes.put('�', "&#186;".toCharArray());
        scapes.put('�', "&#187;".toCharArray());
        scapes.put('�', "&#188;".toCharArray());
        scapes.put('�', "&#189;".toCharArray());
        scapes.put('�', "&#190;".toCharArray());
        scapes.put('�', "&#191;".toCharArray());
        scapes.put('�', "&#192;".toCharArray());
        scapes.put('�', "&#193;".toCharArray());
        scapes.put('�', "&#194;".toCharArray());
        scapes.put('�', "&#195;".toCharArray());
        scapes.put('�', "&#196;".toCharArray());
        scapes.put('�', "&#197;".toCharArray());
        scapes.put('�', "&#198;".toCharArray());
        scapes.put('�', "&#199;".toCharArray());
        scapes.put('�', "&#200;".toCharArray());
        scapes.put('�', "&#201;".toCharArray());
        scapes.put('�', "&#202;".toCharArray());
        scapes.put('�', "&#203;".toCharArray());
        scapes.put('�', "&#204;".toCharArray());
        scapes.put('�', "&#205;".toCharArray());
        scapes.put('�', "&#206;".toCharArray());
        scapes.put('�', "&#207;".toCharArray());
        scapes.put('�', "&#208;".toCharArray());
        scapes.put('�', "&#209;".toCharArray());
        scapes.put('�', "&#210;".toCharArray());
        scapes.put('�', "&#211;".toCharArray());
        scapes.put('�', "&#212;".toCharArray());
        scapes.put('�', "&#213;".toCharArray());
        scapes.put('�', "&#214;".toCharArray());
        scapes.put('�', "&#215;".toCharArray());
        scapes.put('�', "&#216;".toCharArray());
        scapes.put('�', "&#217;".toCharArray());
        scapes.put('�', "&#218;".toCharArray());
        scapes.put('�', "&#219;".toCharArray());
        scapes.put('�', "&#220;".toCharArray());
        scapes.put('�', "&#221;".toCharArray());
        scapes.put('�', "&#222;".toCharArray());
        scapes.put('�', "&#223;".toCharArray());
        scapes.put('�', "&#224;".toCharArray());
        scapes.put('�', "&#225;".toCharArray());
        scapes.put('�', "&#226;".toCharArray());
        scapes.put('�', "&#227;".toCharArray());
        scapes.put('�', "&#228;".toCharArray());
        scapes.put('�', "&#229;".toCharArray());
        scapes.put('�', "&#230;".toCharArray());
        scapes.put('�', "&#231;".toCharArray());
        scapes.put('�', "&#232;".toCharArray());
        scapes.put('�', "&#233;".toCharArray());
        scapes.put('�', "&#234;".toCharArray());
        scapes.put('�', "&#235;".toCharArray());
        scapes.put('�', "&#236;".toCharArray());
        scapes.put('�', "&#237;".toCharArray());
        scapes.put('�', "&#238;".toCharArray());
        scapes.put('�', "&#239;".toCharArray());
        scapes.put('�', "&#240;".toCharArray());
        scapes.put('�', "&#241;".toCharArray());
        scapes.put('�', "&#242;".toCharArray());
        scapes.put('�', "&#243;".toCharArray());
        scapes.put('�', "&#244;".toCharArray());
        scapes.put('�', "&#245;".toCharArray());
        scapes.put('�', "&#246;".toCharArray());
        scapes.put('�', "&#247;".toCharArray());
        scapes.put('�', "&#248;".toCharArray());
        scapes.put('�', "&#249;".toCharArray());
        scapes.put('�', "&#250;".toCharArray());
        scapes.put('�', "&#251;".toCharArray());
        scapes.put('�', "&#252;".toCharArray());
        scapes.put('�', "&#253;".toCharArray());
        scapes.put('�', "&#254;".toCharArray());
        scapes.put('�', "&#255;".toCharArray());
    }

    /**
     * Escapea caracteres para xml
     * @param pIn String de entrada
     * @return Salida ya escapeada
     */
    public static StringBuffer escapeString(String pIn){
        StringBuffer out = new StringBuffer();
        char in[] = pIn.toCharArray();
        for(char anIn : in)
            out.append(escapeChar(anIn));
        return out;
    }

    /**
     * Escapea caracteres para xml
     * @param pIn String de entrada
     * @return Salida ya escapeada
     */
    public static StringBuffer escapeString(StringBuffer pIn)
    {
        return escapeString(pIn.toString());
    }

    /**
     * Obtiene los caracteres de escape de un caracter
     * @param pChar caracter a escapear
     * @return Caracter escapeado
     */
    public static char[] escapeChar(char pChar)
    {
        Object o = scapes.get(pChar);
        if(o==null)
        {
            char r[] = new char[1];
            r[0] = pChar;
            return r;
        }
        return (char[])o;
    }

    /**
     * Transforma el Document en un String.
     * @param doc
     * @return el xml en un String, tabulado y con salto de l�nea.
     */
    public static StringBuffer xml2String(Document doc){
        StringBuffer sb = new StringBuffer();
        int deep = 0;
        Element elem = doc.getDocumentElement();
        addElement(elem, deep, sb);
        return sb;
    }

    private static void addElement(Node elem, int deep, StringBuffer sb) {
//        if (elem instanceof TextNode){
//            return ;
//        }
        //agrego el nodo
        StringBuffer tab = getTab(deep);

        StringBuffer line = getElement(elem);
        sb.append(tab).append(line).append(NEW_LINE);

        //agrego los hijos
        NodeList nodes = elem.getChildNodes();
        deep++;
        int length = nodes.getLength();
        for (int i = 0; i <length; i++) {
            Node n = nodes.item(i);
            addElement(n, deep, sb);
        }
        //cierro el nodo
        //Si no tiene hijos => ya fue cerrado por "getElement(Node elem)"
        //if (length >0 && !(nodes.item(0) instanceof TextNode))
        if (length >0 )
            sb.append(tab).append("</").append(elem.getNodeName()).
                append(" >").append(NEW_LINE);
    }


    private static StringBuffer getElement(Node elem) {
        StringBuffer res = new StringBuffer("<");
        res.append(elem.getNodeName());
        NamedNodeMap attributes = elem.getAttributes();
        if (attributes != null){
            for (int i = 0; i <attributes.getLength(); i++) {
                Node node = attributes.item(i);
                String name = node.getNodeName();
                String value = node.getNodeValue();
                StringBuffer v = escapeString(value);
                res.append(" ").append(name).append("=\"").append(v).append("\"");
            }
        }
        if (elem.hasChildNodes()){
            res.append(" >");
//            Node fc = elem.getFirstChild();
//            if (fc instanceof TextNode){
//                res.append(fc.getNodeValue());
//                res.append(("</" + elem.getNodeName() + " >"));
//            }
        }
        else
            res.append(" />");
        return res;
    }

    private static StringBuffer getTab(int deep) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < deep; i++) {
            res.append(TAB);
        }
        return res;
    }
}

