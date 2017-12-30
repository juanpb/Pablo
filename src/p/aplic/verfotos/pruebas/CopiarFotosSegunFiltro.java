package p.aplic.verfotos.pruebas;

import org.apache.log4j.Logger;
import p.aplic.verfotos.Config;
import p.util.DirFileFilter;
import p.util.UtilFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 20/04/15
 * Time: 11:47
 *
 * Copia fotos del directorio indicado en el archivo de config,
 * al directorio "dest"
 * se puede redefinir el filtro en el código.
 *
 */
public class CopiarFotosSegunFiltro {
    private static final Logger logger = Logger.getLogger(CopiarFotosSegunFiltro.class);

    private static final String dest = "D:\\P\\varios\\Fotos\\_tem\\Li";

    public static void main(String[] args) throws Exception {
        new CopiarFotosSegunFiltro();
    }

    public CopiarFotosSegunFiltro() throws Exception {
        Config config = new Config("D:\\P\\_env\\config\\ver_fotos\\configMover.ini");
        config.setFiltroFotos("Libi");  //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        List<File> fotosDeTodosLosDirectorios = getFotosDeTodosLosDirectorios(config);
        for (File f : fotosDeTodosLosDirectorios) {
            UtilFile.copiar(f, new File(dest, f.getName()) );
        }
        System.out.println("fotosDeTodosLosDirectorios.size() = " + fotosDeTodosLosDirectorios.size());
    }

    private List<File> getFotosDeTodosLosDirectorios(Config config)  {
        List<File> res = null;
        String[] ext = config.getExtensiones().toArray(new String[0]);
        DirFileFilter filtro = new DirFileFilter(false, true, ext, false);
        try {
            res = new ArrayList<File>();
            List<String> dirs = config.getDirectorios();
            for (String dir : dirs) {
                res.addAll(
                        filtrar(UtilFile.archivos(dir, filtro, config.isRecursivo()), config));
            }

            List<String> dirsSF = config.getDirectoriosSinFiltrar();
            if (dirsSF != null){
                for (String dir : dirsSF) {
                    res.addAll((UtilFile.archivos(dir, filtro, config.isRecursivo())));
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return res;
    }

    private List<File> filtrar(List<File> fotos, Config config) {
        List<File> res = new ArrayList<File>();
        for(File foto : fotos) {
            if (pasaFiltro(foto, config))
                res.add(foto);
        }
        return res;
    }

    private boolean pasaFiltro(File file, Config config){
        java.util.List<String> filtros = config.getFiltroFotos();
        if (filtros == null || filtros.size() == 0)
            return !file.isDirectory();
        if (!config.isTipoFiltroOr())
            return pasaFiltroAND(file, config);

        String name = file.getName().toLowerCase();
        for(String filtro : filtros) {
            if (name.contains(filtro.toLowerCase()))
                return !file.isDirectory();
        }
        return false;
    }
    private boolean pasaFiltroAND(File file, Config config){
        java.util.List<String> filtros = config.getFiltroFotos();
        if (filtros == null || filtros.size() == 0)
            return !file.isDirectory();

        String name = file.getName().toLowerCase();
        for(String filtro : filtros) {
            if (!name.contains(filtro.toLowerCase()))
                return false;
        }
        return true;
    }
}
