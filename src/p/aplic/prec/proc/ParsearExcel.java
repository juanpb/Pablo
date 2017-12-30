package p.aplic.prec.proc;

import org.apache.log4j.Logger;
import p.aplic.prec.Model;
import p.aplic.prec.domain.PrecioDO;
import p.aplic.prec.domain.ProductoDO;
import p.aplic.prec.domain.SuperDO;
import p.util.Fechas;
import p.util.UtilFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: May 2, 2008
 * Time: 12:43:10 PM
 */
public class ParsearExcel {
    private static final Logger logger = Logger.getLogger(ParsearExcel.class);

    private static String archEntr = "D:\\P\\_env\\prc\\prc.txt";
    private static List<ProductoDO> prods;
    private static List<PrecioDO> precios;

    public static void main(String[] args) throws IOException {
        String ini = "D:\\P\\_env\\prc\\config.ini";
        p.util.Util.loadProperties(ini);
        hacer();
    }

    private static void hacer() throws IOException {
        precios = new ArrayList<PrecioDO>();
        List<String> lineas = UtilFile.getArchivoPorLinea(archEntr);
        prods = Model.getProductos();
        for (String s : lineas) {
            parsear(s);
        }
        Model.agregarPrecios(precios);
    }

    private static void parsear(String linea){
        //Makro Carrefour Coto	Jumbo	Wal-mart
        SuperDO sm = new SuperDO(1);
        SuperDO sca = new SuperDO(2);
        SuperDO sco = new SuperDO(3);
        SuperDO sj = new SuperDO(4);
        SuperDO sw = new SuperDO(5);

        StringTokenizer st = new StringTokenizer(linea, ";", false);
        try {
            ProductoDO pr = getProducto(st.nextToken());
            String m = st.nextToken();
            String ca = st.nextToken();
            String co = st.nextToken();
            String j = st.nextToken();
            String w = null;

            if (st.hasMoreTokens()){
                w = st.nextToken();
            }
            agregarP(pr, sm, m);
            agregarP(pr, sca, ca);
            agregarP(pr, sco, co);
            agregarP(pr, sj, j);
            agregarP(pr, sw, w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.out.println("linea = " + linea);
        }

    }

    private static void agregarP(ProductoDO pr, SuperDO s, String fyp){
        if (fyp == null || fyp.equals("") || fyp.equals(";"))
            return ;
        int i = fyp.indexOf(")");
        if (i < 0)
            return ;

        String f = fyp.substring(1, i);
        f = f.replaceAll("-", "/");
        f = f.replaceAll("X", "10");
        Date d = getDate(f);
        String pre = fyp.substring(i+1);
        PrecioDO p = new PrecioDO(pr, string2double(pre), s, d);
        precios.add(p);
//        System.out.println("");
    }

    private static double string2double(String f){
        if (f.indexOf(",")>0)
            f = f.replaceAll(",", ".");

        return Double.parseDouble(f);
    }
    
    private static Date getDate(String f){
        StringTokenizer st = new StringTokenizer(f, "/");
        String d = st.nextToken();
        if(d.length() ==1)
            d = "0"+d;
        String m = st.nextToken();
        if(m.length() ==1)
            m = "0"+m;
        String a = st.nextToken();
        if(a.length() ==1)
            a = "0"+a;
        String s = d + "_" + m + "_" + a;
        Date dateDD_mm_aa = null;
        try {
            dateDD_mm_aa = Fechas.getDateDD_MM_AA(s);
        } catch (Exception e) {
            System.out.println(s);
            logger.error(e.getMessage(), e);
        }

        return dateDD_mm_aa;
    }

    private static ProductoDO getProducto(String pr){
        for (ProductoDO p : prods) {
            if (p.getDescripcion().equals(pr)){
                return p;
            }
        }
        return null;
    }
}
