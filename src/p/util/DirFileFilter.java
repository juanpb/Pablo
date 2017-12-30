package p.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DirFileFilter implements FilenameFilter{

    private boolean soloDirectorios = true;
    private boolean soloArchivos = false;
    private String[] ext = null;
    private List<String> excluirDirsEmpiezanCon = new ArrayList<String>();
    private List<String> excluirDirsCaseUnsensitive = new ArrayList<String>();
    private boolean mostrarOcultos = false;
    private boolean excluirExtensiones = false;

    public DirFileFilter(boolean pSoloDirectorios){
        this(pSoloDirectorios, false);
    }

    public DirFileFilter(boolean pSoloDirectorios, boolean pMostrarOcultos){
        this(pSoloDirectorios, "", pMostrarOcultos);
    }

    public DirFileFilter(boolean pSoloDirectorios, boolean pSoloArchivos,
                         boolean pMostrarOcultos){
        this(pSoloDirectorios, pSoloArchivos,  "", pMostrarOcultos);
    }

    public DirFileFilter(boolean pSoloDirectorios, String pExtension){
        this(pSoloDirectorios, pExtension, false);
    }

    public DirFileFilter(boolean pSoloDirectorios, String pExtension,
                         boolean pMostrarOcultos){
        this(pSoloDirectorios, false, pExtension, pMostrarOcultos);
    }

    public DirFileFilter(boolean pSoloDirectorios, boolean pSoloArchivos,
                         String pExtension, boolean pMostrarOcultos){
        String[] extL = new String[1];
        extL[0] = pExtension.toLowerCase();
        soloArchivos = pSoloArchivos;
        mostrarOcultos = pMostrarOcultos;
        soloDirectorios = pSoloDirectorios;
        this.ext = extL;
    }

     public DirFileFilter(boolean pSoloDirectorios, boolean pSoloArchivos,
                         String[] pExtension, boolean pMostrarOcultos){
        soloArchivos = pSoloArchivos;
        mostrarOcultos = pMostrarOcultos;
        soloDirectorios = pSoloDirectorios;
        ext = pExtension;
    }
    public boolean accept(File dir, String name){
        boolean res = true;

        File f = new File(dir, name);
        if (soloDirectorios)
            res = f.isDirectory();
        else{
            if (soloArchivos && !f.isFile())
                res = false;
            else{
                String s = f.getAbsolutePath().toLowerCase();
                if (ext.length > 0){
                    if (coincideExtension(s))
                        res = !excluirExtensiones;
                    else
                        res = excluirExtensiones;
                }
                if (res && !mostrarOcultos) {
                    res = !f.isHidden();
                }
            }
        }
        if (f.isDirectory()){
            String n = f.getName();
            for(String s : excluirDirsEmpiezanCon) {
                if (n.startsWith(s))
                    res = false;
            }
            for(String s : excluirDirsCaseUnsensitive) {
                if (n.startsWith(s))
                    res = false;
            }
        }
        return res;
    }

    private boolean coincideExtension(String s) {
        for(String anExt : ext) {
            String extL = anExt.toLowerCase();
            if (s.endsWith(extL))
                return true;
        }
        return false;
    }

    public void setSoloArchivos(boolean soloArchivos) {
        this.soloArchivos = soloArchivos;
    }

    public boolean isSoloArchivos() {
        return soloArchivos;
    }

    public boolean isSoloDirectorios() {
        return soloDirectorios;
    }

    public void addExcluirDirsEmpiezanCon(String x) {
        excluirDirsEmpiezanCon.add(x);
    }

    public void addExcluirDirsCaseUnsensitive(String x) {
        excluirDirsCaseUnsensitive.add(x);
    }

    public List<String> getExcluirDirsEmpiezanCon() {
        return excluirDirsEmpiezanCon;
    }

    public List<String> getExcluirDirsCaseUnsensitive() {
        return excluirDirsCaseUnsensitive;
    }

    public void setExtensiones(String[] ext) {
        this.ext = ext;
    }

    public boolean isExcluirExtensiones() {
        return excluirExtensiones;
    }

    public void setExcluirExtensiones(boolean excluirExtensiones) {
        this.excluirExtensiones = excluirExtensiones;
    }

    public void addExcluirDirsCaseUnsensitive(List<String> excluirDirsCaseUnsensitive) {
        this.excluirDirsCaseUnsensitive.addAll(excluirDirsCaseUnsensitive);
    }

    public void addExcluirDirsEmpiezanCon(List<String> excluirDirsEmpiezanCon) {
        this.excluirDirsEmpiezanCon.addAll(excluirDirsEmpiezanCon);
    }
}