package p.proc;

import p.util.UtilFile;
import p.util.UtilString;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;


public class CopiarYRen {

    final static private String CLAVE_RAIZ_DESTINO = "raizDestino";
    final static private String CLAVE_ORIGEN = "origen";
    final static private String CLAVE_DESTINO = "destino";
    final static private String CLAVE_ELIMINAR = "eliminar";

    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            System.out.println("uso: java CopiarYRen archInfo");
            System.exit(-1);
        }
        System.out.println("");
        CopiarYRen c = new CopiarYRen();
        String archInfo = args[0];
        c.hacer(archInfo);
        System.out.println("Listo");
    }

    private void hacer(String archInfo) throws Exception {
        File f = new File(archInfo);
        if (!f.exists()){
            System.out.println("No existe el archivo " + f.getAbsolutePath());
            return ;
        }
        int i = 0;
        List lineas = UtilFile.getArchivoPorLinea(f);
        String raizDestino = (String) lineas.get(i);

        if (raizDestino.startsWith(CLAVE_RAIZ_DESTINO + "=")){
            raizDestino = raizDestino.substring(12);
        }
        for (i = 1; i < lineas.size(); i++) {
            String origen = (String) lineas.get(i++);
            if (origen == null || origen.trim().equals(""))
                continue;
            String destino = (String) lineas.get(i++);
            String elim = (String) lineas.get(i);

            if (origen.startsWith(CLAVE_ORIGEN + "=")){
                origen = origen.substring(7);
                if (origen.trim().equals(""))
                    return ;
            }
            if (destino.startsWith(CLAVE_DESTINO + "=")){
                destino = destino.substring(8);
            }
            if (elim.startsWith(CLAVE_ELIMINAR + "=")){
                elim = elim.substring(9);
            }
            copiar(origen, raizDestino + File.separator + destino, elim);
        }
    }

    /**
     * Copia el directorio 'origen' (tiene que ser un directorio) a destino. Si 'elim' es != null renombre el archivo destino eliminando
     * las cadenas de caracteres que estén en 'elim' (separadas por ;)
     * @param origen
     * @param destino
     * @param elim
     * @throws Exception
     */
    private void copiar(String origen, String destino, String elim) throws Exception {
        System.out.println("origen = " + origen);
        System.out.println("destino = " + destino);
        System.out.println("elim = " + elim);
        System.out.println("");

        File fOr = new File (origen);
        if (!fOr.exists()){
            System.out.println("No existe el archivo " + fOr.getAbsolutePath());
            return ;
        }
        File[] files = fOr.listFiles();
        for(File file : files) {
            if (file.isDirectory()) {
                String n = file.getName();
                String nuevoDes = destino + File.separator + n;
                copiar(file.getAbsolutePath(), nuevoDes, elim);
            } else {
                if (!file.getName().toLowerCase().endsWith(".mp3")) {
                    String nn = destino + File.separator + file.getName();
                    UtilFile.copiar(file, new File(nn));
                } else {
                    copiarYRenombrar(file, destino, elim);
                }
            }
        }
    }

    /**
     * Copia fileOrigen a destino. Si 'elim' es != null renombre el archivo destino eliminando
     * las cadenas de caracteres que estén en 'elim' (separadas por ;)
     * @param fileOrigen
     * @param destino
     * @param elim
     * @throws Exception
     */
    private void copiarYRenombrar(File fileOrigen, String destino, String elim)
            throws Exception {
        if (elim == null || elim.trim().equals("")){
            String nn = destino + File.separator + fileOrigen.getName();
            UtilFile.copiar(fileOrigen, new File(nn));
            return ;
        }
        String nom = fileOrigen.getName();
        StringTokenizer st = new StringTokenizer(elim, ";");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            nom = UtilString.reemplazarTodo(nom, s, "");
        }
        String nn = destino + File.separator + nom;
        UtilFile.copiar(fileOrigen, new File(nn));
    }

}
