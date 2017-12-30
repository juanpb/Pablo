package crm.core.wfl.editor.xml;

import org.w3c.dom.*;
import org.apache.crimson.tree.TextNode;

import java.util.HashMap;
import java.util.Map;

/**
 * User: JPB
 * Date: Jan 31, 2006
 * Time: 12:44:27 PM
 */
public class WflXMLUtil {

    private static final String TAB  = "    ";
    private static final String NEW_LINE = "\n";

	/**
	 * Constructor privado. Utility Class.
	 */
	private WflXMLUtil() {
		// No hace nada, es solo para esconder el constructor.
	}

    /**
     * Guarda la equivalencia para escapeo de caracteres.
     */
    private static Map<Character, char[]> scapes = new HashMap<Character, char[]>();

    static
    {
        scapes.put(new Character('<'), "&lt;".toCharArray());
        scapes.put(new Character('>'), "&gt;".toCharArray());
        scapes.put(new Character('&'), "&amp;".toCharArray());
        scapes.put(new Character('"'), "&quot;".toCharArray());
        scapes.put(new Character('\''), "&apos;".toCharArray());
        scapes.put(new Character('"'), "&#34;".toCharArray());
        scapes.put(new Character('&'), "&#38;".toCharArray());
        scapes.put(new Character('<'), "&#60;".toCharArray());
        scapes.put(new Character('>'), "&#62;".toCharArray());
        scapes.put(new Character('@'), "&#64;".toCharArray());
        scapes.put(new Character('�'), "&#128;".toCharArray());
        scapes.put(new Character('�'), "&#171;".toCharArray());
        scapes.put(new Character('�'), "&#187;".toCharArray());
        scapes.put(new Character('�'), "&#161;".toCharArray());
        scapes.put(new Character('�'), "&#191;".toCharArray());
        scapes.put(new Character('�'), "&#162;".toCharArray());
        scapes.put(new Character('�'), "&#163;".toCharArray());
        scapes.put(new Character('�'), "&#164;".toCharArray());
        scapes.put(new Character('�'), "&#165;".toCharArray());
        scapes.put(new Character('�'), "&#166;".toCharArray());
        scapes.put(new Character('�'), "&#167;".toCharArray());
        scapes.put(new Character('�'), "&#169;".toCharArray());
        scapes.put(new Character('�'), "&#174;".toCharArray());
        scapes.put(new Character('�'), "&#183;".toCharArray());
        scapes.put(new Character('�'), "&#188;".toCharArray());
        scapes.put(new Character('�'), "&#189;".toCharArray());
        scapes.put(new Character('�'), "&#190;".toCharArray());
        scapes.put(new Character('�'), "&#185;".toCharArray());
        scapes.put(new Character('�'), "&#178;".toCharArray());
        scapes.put(new Character('�'), "&#179;".toCharArray());
        scapes.put(new Character('�'), "&#176;".toCharArray());
        scapes.put(new Character('�'), "&#215;".toCharArray());
        scapes.put(new Character('�'), "&#247;".toCharArray());
        scapes.put(new Character('�'), "&#177;".toCharArray());
        scapes.put(new Character('"'), "&#34;".toCharArray());
        scapes.put(new Character('&'), "&#38;".toCharArray());
        scapes.put(new Character('�'), "&#180;".toCharArray());
        scapes.put(new Character('�'), "&#181;".toCharArray());
        scapes.put(new Character('�'), "&#182;".toCharArray());
        scapes.put(new Character('�'), "&#184;".toCharArray());
        scapes.put(new Character('�'), "&#186;".toCharArray());
        scapes.put(new Character('�'), "&#187;".toCharArray());
        scapes.put(new Character('�'), "&#188;".toCharArray());
        scapes.put(new Character('�'), "&#189;".toCharArray());
        scapes.put(new Character('�'), "&#190;".toCharArray());
        scapes.put(new Character('�'), "&#191;".toCharArray());
        scapes.put(new Character('�'), "&#192;".toCharArray());
        scapes.put(new Character('�'), "&#193;".toCharArray());
        scapes.put(new Character('�'), "&#194;".toCharArray());
        scapes.put(new Character('�'), "&#195;".toCharArray());
        scapes.put(new Character('�'), "&#196;".toCharArray());
        scapes.put(new Character('�'), "&#197;".toCharArray());
        scapes.put(new Character('�'), "&#198;".toCharArray());
        scapes.put(new Character('�'), "&#199;".toCharArray());
        scapes.put(new Character('�'), "&#200;".toCharArray());
        scapes.put(new Character('�'), "&#201;".toCharArray());
        scapes.put(new Character('�'), "&#202;".toCharArray());
        scapes.put(new Character('�'), "&#203;".toCharArray());
        scapes.put(new Character('�'), "&#204;".toCharArray());
        scapes.put(new Character('�'), "&#205;".toCharArray());
        scapes.put(new Character('�'), "&#206;".toCharArray());
        scapes.put(new Character('�'), "&#207;".toCharArray());
        scapes.put(new Character('�'), "&#208;".toCharArray());
        scapes.put(new Character('�'), "&#209;".toCharArray());
        scapes.put(new Character('�'), "&#210;".toCharArray());
        scapes.put(new Character('�'), "&#211;".toCharArray());
        scapes.put(new Character('�'), "&#212;".toCharArray());
        scapes.put(new Character('�'), "&#213;".toCharArray());
        scapes.put(new Character('�'), "&#214;".toCharArray());
        scapes.put(new Character('�'), "&#215;".toCharArray());
        scapes.put(new Character('�'), "&#216;".toCharArray());
        scapes.put(new Character('�'), "&#217;".toCharArray());
        scapes.put(new Character('�'), "&#218;".toCharArray());
        scapes.put(new Character('�'), "&#219;".toCharArray());
        scapes.put(new Character('�'), "&#220;".toCharArray());
        scapes.put(new Character('�'), "&#221;".toCharArray());
        scapes.put(new Character('�'), "&#222;".toCharArray());
        scapes.put(new Character('�'), "&#223;".toCharArray());
        scapes.put(new Character('�'), "&#224;".toCharArray());
        scapes.put(new Character('�'), "&#225;".toCharArray());
        scapes.put(new Character('�'), "&#226;".toCharArray());
        scapes.put(new Character('�'), "&#227;".toCharArray());
        scapes.put(new Character('�'), "&#228;".toCharArray());
        scapes.put(new Character('�'), "&#229;".toCharArray());
        scapes.put(new Character('�'), "&#230;".toCharArray());
        scapes.put(new Character('�'), "&#231;".toCharArray());
        scapes.put(new Character('�'), "&#232;".toCharArray());
        scapes.put(new Character('�'), "&#233;".toCharArray());
        scapes.put(new Character('�'), "&#234;".toCharArray());
        scapes.put(new Character('�'), "&#235;".toCharArray());
        scapes.put(new Character('�'), "&#236;".toCharArray());
        scapes.put(new Character('�'), "&#237;".toCharArray());
        scapes.put(new Character('�'), "&#238;".toCharArray());
        scapes.put(new Character('�'), "&#239;".toCharArray());
        scapes.put(new Character('�'), "&#240;".toCharArray());
        scapes.put(new Character('�'), "&#241;".toCharArray());
        scapes.put(new Character('�'), "&#242;".toCharArray());
        scapes.put(new Character('�'), "&#243;".toCharArray());
        scapes.put(new Character('�'), "&#244;".toCharArray());
        scapes.put(new Character('�'), "&#245;".toCharArray());
        scapes.put(new Character('�'), "&#246;".toCharArray());
        scapes.put(new Character('�'), "&#247;".toCharArray());
        scapes.put(new Character('�'), "&#248;".toCharArray());
        scapes.put(new Character('�'), "&#249;".toCharArray());
        scapes.put(new Character('�'), "&#250;".toCharArray());
        scapes.put(new Character('�'), "&#251;".toCharArray());
        scapes.put(new Character('�'), "&#252;".toCharArray());
        scapes.put(new Character('�'), "&#253;".toCharArray());
        scapes.put(new Character('�'), "&#254;".toCharArray());
        scapes.put(new Character('�'), "&#255;".toCharArray());
    }

    /**
     * Escapea caracteres para xml
     * @param pIn String de entrada
     * @return Salida ya escapeada
     */
    public static StringBuffer escapeString(String pIn){
        StringBuffer out = new StringBuffer();
    	if(pIn!=null) {
	        char in[] = pIn.toCharArray();
	        for(int i=0; i<in.length; i++)
	            out.append(escapeChar(in[i]));
    	}
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
        Object o = scapes.get(new Character(pChar));
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
        if (elem instanceof TextNode){
            return ;
        }
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
        if (length >0 && !(nodes.item(0) instanceof TextNode))
            sb.append(tab).append("</").append(elem.getNodeName()).
                append(">").append(NEW_LINE);
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
                StringBuffer v = WflXMLUtil.escapeString(value);
                res.append(" ").append(name).append("=\"").append(v).append("\"");
            }
        }
        if (elem.hasChildNodes()){
            res.append(">");
            Node fc = elem.getFirstChild();
            if (fc instanceof CDATASection) {
            	res.append("<![CDATA[");
                res.append(fc.getNodeValue());
            	res.append("]]>");
                res.append(("</" + elem.getNodeName() + " >"));
            }
            else if(fc instanceof TextNode) {
                res.append(WflXMLUtil.escapeString(fc.getNodeValue()).toString());
                res.append(("</" + elem.getNodeName() + " >"));
            }
        }
        else
            res.append("/>");
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

