package p.aplic.renombrar;

import p.mp3.MP3File;

import java.io.File;

public class Fila{
    private File    _file;
    private MP3File   _mFile;
    private String  _dirCompleto;
    private String  _fileName = "";
    private boolean _archModif = false;
    private boolean _esDir = false;
    private boolean _esArchivoNoMP3 = false;
    private boolean _esMP3 = false;

    /**
     * Usarlo sólo para directorios
     * @param dir
     */
    public Fila(String dir, boolean mostrarCompleto){
        _dirCompleto = dir;
        _file = new File(dir);
        if (!mostrarCompleto){
            int d = dir.lastIndexOf("\\");
            _fileName = dir.substring(d + 1);
        }else
            _fileName = dir;

        _esDir = true;
        _esMP3 = false;
        _esArchivoNoMP3 = false;
    }

    /**
     * Usarlo sólo para archivos que no sean MP3
     */
    public Fila(File f){
        _file = f;
        _fileName = f.getName();
        _esDir = false;
        _esMP3 = false;
        _esArchivoNoMP3 = true;
    }

    /**
     * Usarlo sólo para MP3
     */
    public Fila(File f, MP3File m){
        _file = f;
        _mFile = m;
        _fileName = f.getName();
        _esDir = false;
        _esMP3 = true;
        _esArchivoNoMP3 = false;
    }

    public File getFile() {
        return _file;
    }

    public MP3File getMP3File() {
        return _mFile;
    }


    public String getDir() {
        return _fileName;
    }

    public String getDirCompleto()
    {
        return _dirCompleto;
    }

    public void setArchModif(boolean p) {
        _archModif = p;
    }
    public boolean isArchModif() {
        return _archModif;
    }

    public boolean esDir() {
        return _esDir;
    }
    public boolean esArchivoNoMP3() {
        return _esArchivoNoMP3;
    }
    public boolean esMP3() {
        return _esMP3;
    }

    public boolean esImagen() {
        return _fileName.toLowerCase().endsWith(".jpg") || _fileName.toLowerCase().endsWith(".png") || _fileName.toLowerCase().endsWith(".bmp");
    }

    public String getFileName(){
        return _fileName;
    }

    public String getFileNameCompleto(){
        return _file.getAbsolutePath();
    }

    public void setFileName(String n){
        _fileName = n;
        setArchModif(true);
    }

    public void setTitle(String n){
        _mFile.setTitulo(n);
        setArchModif(true);
    }

    public void setComentario(String n){
        _mFile.setComentario(n);
        setArchModif(true);
    }
}