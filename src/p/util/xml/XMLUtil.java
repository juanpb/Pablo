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
        scapes.put('€', "&#128;".toCharArray());
        scapes.put('«', "&#171;".toCharArray());
        scapes.put('»', "&#187;".toCharArray());
        scapes.put('¡', "&#161;".toCharArray());
        scapes.put('¿', "&#191;".toCharArray());
        scapes.put('¢', "&#162;".toCharArray());
        scapes.put('£', "&#163;".toCharArray());
        scapes.put('¤', "&#164;".toCharArray());
        scapes.put('¥', "&#165;".toCharArray());
        scapes.put('¦', "&#166;".toCharArray());
        scapes.put('§', "&#167;".toCharArray());
        scapes.put('©', "&#169;".toCharArray());
        scapes.put('®', "&#174;".toCharArray());
        scapes.put('·', "&#183;".toCharArray());
        scapes.put('¼', "&#188;".toCharArray());
        scapes.put('½', "&#189;".toCharArray());
        scapes.put('¾', "&#190;".toCharArray());
        scapes.put('¹', "&#185;".toCharArray());
        scapes.put('²', "&#178;".toCharArray());
        scapes.put('³', "&#179;".toCharArray());
        scapes.put('°', "&#176;".toCharArray());
        scapes.put('×', "&#215;".toCharArray());
        scapes.put('÷', "&#247;".toCharArray());
        scapes.put('±', "&#177;".toCharArray());
        scapes.put('"', "&#34;".toCharArray());
        scapes.put('&', "&#38;".toCharArray());
        scapes.put('´', "&#180;".toCharArray());
        scapes.put('µ', "&#181;".toCharArray());
        scapes.put('¶', "&#182;".toCharArray());
        scapes.put('¸', "&#184;".toCharArray());
        scapes.put('º', "&#186;".toCharArray());
        scapes.put('»', "&#187;".toCharArray());
        scapes.put('¼', "&#188;".toCharArray());
        scapes.put('½', "&#189;".toCharArray());
        scapes.put('¾', "&#190;".toCharArray());
        scapes.put('¿', "&#191;".toCharArray());
        scapes.put('À', "&#192;".toCharArray());
        scapes.put('Á', "&#193;".toCharArray());
        scapes.put('Â', "&#194;".toCharArray());
        scapes.put('Ã', "&#195;".toCharArray());
        scapes.put('Ä', "&#196;".toCharArray());
        scapes.put('Å', "&#197;".toCharArray());
        scapes.put('Æ', "&#198;".toCharArray());
        scapes.put('Ç', "&#199;".toCharArray());
        scapes.put('È', "&#200;".toCharArray());
        scapes.put('É', "&#201;".toCharArray());
        scapes.put('Ê', "&#202;".toCharArray());
        scapes.put('Ë', "&#203;".toCharArray());
        scapes.put('Ì', "&#204;".toCharArray());
        scapes.put('Í', "&#205;".toCharArray());
        scapes.put('Î', "&#206;".toCharArray());
        scapes.put('Ï', "&#207;".toCharArray());
        scapes.put('Ð', "&#208;".toCharArray());
        scapes.put('Ñ', "&#209;".toCharArray());
        scapes.put('Ò', "&#210;".toCharArray());
        scapes.put('Ó', "&#211;".toCharArray());
        scapes.put('Ô', "&#212;".toCharArray());
        scapes.put('Õ', "&#213;".toCharArray());
        scapes.put('Ö', "&#214;".toCharArray());
        scapes.put('×', "&#215;".toCharArray());
        scapes.put('Ø', "&#216;".toCharArray());
        scapes.put('Ù', "&#217;".toCharArray());
        scapes.put('Ú', "&#218;".toCharArray());
        scapes.put('Û', "&#219;".toCharArray());
        scapes.put('Ü', "&#220;".toCharArray());
        scapes.put('Ý', "&#221;".toCharArray());
        scapes.put('Þ', "&#222;".toCharArray());
        scapes.put('ß', "&#223;".toCharArray());
        scapes.put('à', "&#224;".toCharArray());
        scapes.put('á', "&#225;".toCharArray());
        scapes.put('â', "&#226;".toCharArray());
        scapes.put('ã', "&#227;".toCharArray());
        scapes.put('ä', "&#228;".toCharArray());
        scapes.put('å', "&#229;".toCharArray());
        scapes.put('æ', "&#230;".toCharArray());
        scapes.put('ç', "&#231;".toCharArray());
        scapes.put('è', "&#232;".toCharArray());
        scapes.put('é', "&#233;".toCharArray());
        scapes.put('ê', "&#234;".toCharArray());
        scapes.put('ë', "&#235;".toCharArray());
        scapes.put('ì', "&#236;".toCharArray());
        scapes.put('í', "&#237;".toCharArray());
        scapes.put('î', "&#238;".toCharArray());
        scapes.put('ï', "&#239;".toCharArray());
        scapes.put('ð', "&#240;".toCharArray());
        scapes.put('ñ', "&#241;".toCharArray());
        scapes.put('ò', "&#242;".toCharArray());
        scapes.put('ó', "&#243;".toCharArray());
        scapes.put('ô', "&#244;".toCharArray());
        scapes.put('õ', "&#245;".toCharArray());
        scapes.put('ö', "&#246;".toCharArray());
        scapes.put('÷', "&#247;".toCharArray());
        scapes.put('ø', "&#248;".toCharArray());
        scapes.put('ù', "&#249;".toCharArray());
        scapes.put('ú', "&#250;".toCharArray());
        scapes.put('û', "&#251;".toCharArray());
        scapes.put('ü', "&#252;".toCharArray());
        scapes.put('ý', "&#253;".toCharArray());
        scapes.put('þ', "&#254;".toCharArray());
        scapes.put('ÿ', "&#255;".toCharArray());
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
     * @return el xml en un String, tabulado y con salto de línea.
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

