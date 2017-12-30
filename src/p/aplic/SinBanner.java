package p.aplic;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;

/*
 "_direc" tiene el archivo o directorio a partir del cual se buscarán los archivos.
 Sólo trabaja con archivos "*.html" o "htm".
 "_frasePrincipio" y "_fraseFin" son las frases que se buscarán en el archivo. Si
  ambas se encuentran se elimina todo lo que haya entre esas dos líneas (ellas
  incluidas). Si no, no hace nada.
*/
public class SinBanner{
    private static final Logger logger = Logger.getLogger(SinBanner.class);
    private static String _direc = "C:\\My Intranet";

    final static int MAX_LINES = 10000;
    private static String[] temp;//acá se guarda el archivo a modificar
    double contAux = 0;   //para ir viendo algo en la consola mientras labura
    double archModificados = 0; //cuenta la cantidad de archivos modificados

    /*
    String _fraseDesde = "<!-- Spidersoft WebZIP Ad Banner Insert -->";
    String _fraseHasta = "<!-- End of Spidersoft WebZIP Ad Banner Insert-->";
    */

    private static String _fraseDesde = "<!-- Spidersoft WebZIP Banner Ad Insert -->";
    private static String _fraseHasta = "<!-- /Spidersoft WebZIP Banner Ad Insert -->";
    /*
    String _fraseDesde = "<!----EMPIEZA CODIGO DE BARRA DE CIUDAD FUTURA----->";
    String _fraseHasta = "<!----TERMINA CODIGO DE BARRA DE CIUDAD FUTURA----->";
    */

    public static void main(String[] arg) throws Exception{

        if (arg.length > 0){
            p.util.Util.loadProperties(arg[0]);
            String x = System.getProperty("DIRECCION");
            if (x != null && !x.trim().equals("")){
                _direc = x;
                System.out.println("Se seteó la propiedad _direc=" + x);
            }
            x = System.getProperty("FRASE_DESDE");
            if (x != null && !x.trim().equals("")){
                _fraseDesde = x;
                System.out.println("Se seteó la propiedad _fraseDesde=" + x);
            }
            x = System.getProperty("FRASE_HASTA");
            if (x != null && !x.trim().equals("")){
                _fraseHasta = x;
                System.out.println("Se seteó la propiedad _fraseHasta=" + x);
            }

        }
        SinBanner sb = new SinBanner();
        sb._fraseDesde = sb._fraseDesde.toLowerCase();
        sb._fraseHasta = sb._fraseHasta.toLowerCase();

        sb.hacer(new File(_direc));
        System.out.println("Cantidad de archivos modificados: "  + sb.archModificados);
    }

    private void hacer(File file){
        Vector vec = new Vector();
        File[] archs = file.listFiles();//lista de las subcarpetas de file para hacer la recursión

        if (archs == null){ //es nulo si "file" era un archivo y no un directorio
            try{
                if (esHtml(file.toString().toLowerCase()))
                    reemplazar(file);
            }
            catch(IOException e){
                    logger.error(e.getMessage(), e);
            }
        return;
        }

        //si file era un directorio => primero reemplazo sus archivos y luego,
        //recursivamente, los de sus subdirectorios
        for(int i = 0; i < archs.length; i++){
            if (archs[i].isDirectory())
                vec.add(archs[i]); //creo un vector con las subcarpetas
            else{
                try{
                    if (esHtml(archs[i].toString().toLowerCase()))
                        reemplazar(archs[i]);
                }
                catch(IOException e){
                    logger.error(e.getMessage(), e);
                }
            }
        }

        //recursión con las subcarpetas
        Iterator it = vec.iterator();
        while(it.hasNext()){
            File recFile = (File) it.next();
            hacer(recFile);
        }
    }

    private boolean esHtml(String path){
        return path.endsWith("htm") || path.endsWith("html");
    }

    private void reemplazar(File file) throws IOException{
        if (sePuede(file)) //si tiene la "_fraseDesde" y la "fraseFinal"
            borrar(file);  //elimino lo que está en el medio
    }

    /*
        Este método, mientras busca el contenido de "_fraseDesde" y "_fraseHasta",
        guarda todo el archivo (sin lo que está entre estas frases) en la variable
        global String[MAX_LINES] temp;
    */
    private boolean sePuede(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bis = new BufferedReader(new InputStreamReader(fis));
            boolean principio = false;
            boolean fin = false;
            String aux;
            temp = new String[MAX_LINES];
            int j=0;

            //busco "_fraseDesde" y guardo, en "temp[]" lo que hay antes de la frase
            while(!principio && (aux = bis.readLine())!= null){
                if (aux != null && aux.toLowerCase().equals(_fraseDesde.trim())){
                    principio = true;
//                    System.out.println("principio");
                }
                else
                    temp[j++] = aux;
            }
            if (!principio){
//                System.out.println("no principio");
                return false;
            }

            //busco "_fraseHasta" y , una vez que la encuentra, guardo lo que hay después de la frase
            while((aux = bis.readLine())!= null){
                if ( !fin && aux.toLowerCase().equals(_fraseHasta.trim())){
                    fin = true;
                    aux = bis.readLine();//antes de ejecutarse esta línea, aux tiene la frase de fin
//                    System.out.println("fin");
                }
                if (fin)
                    temp[j++] = aux;
            }
            bis.close();
            return principio && fin;
        }
        catch (IOException ex) {
            System.out.println("Problemas: ");
            ex.printStackTrace();
            return false;
        }
    }

    /*
        Piso el archivo "file" con el contenido de temp[]
    */
    private void borrar(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        int i=0;

        while(temp[i] != null){
            bw.write(temp[i++] + "\n");
        }
        bw.close();
//        System.out.println("Archivo modificado (" + archModificados++ + "): " + file.toString() );
    }
}