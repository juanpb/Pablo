package p.mp3;

import de.ueberdosis.mp3info.facades.Wamp;
import de.ueberdosis.mp3info.id3v2.ID3V2Tag;
import de.ueberdosis.mp3info.id3v2.ID3V2Writer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MP3File implements Runnable{
    private static final Logger logger = Logger.getLogger(MP3File.class);

    private File _file   = null;
    private boolean _datosCargados = false;

    private String _titulo = "";
    private String _artista = "";
    private String _album = "";
    private String _annus = "";
    private String _comentario = "";
    private String _genero = "";
    private boolean _tieneTag = false;
    private boolean _seModifico = false;

    private boolean _agregarID3v2SiEsNecesario = false;
    private Boolean _tieneID3v2 = null;
    private int _bitrate = 0;

    public static void main(String[] args) throws Exception    {
        System.out.println("empezó");
        String mp3 =   "F:\\tem\\The Beatles\\1969 - AB Road - Complete Get Back Session\\23-01-1969\\Disco 4\\24_The Long And Winding Road (23.84).mp3";
        new MP3File(mp3);

    }


    public MP3File(String nombre)  throws Exception  {
        this(nombre, false);
    }
    public MP3File(String nombre, boolean agregarID3v2SiEsNecesario)
            throws Exception  {
        this(new File(nombre), agregarID3v2SiEsNecesario);
    }


    public MP3File(File f) throws Exception{
        this(f, false);
    }

    public MP3File(File f, boolean agregarID3v2SiEsNecesario)
            throws Exception{
        _agregarID3v2SiEsNecesario = agregarID3v2SiEsNecesario;
        _file = f;

        new Thread(this).start();
    }

    public int getBitrate(){
        return _bitrate;
    }

    public Boolean tieneID3v2(){
        esperar();
        return _tieneID3v2;
    }

    public void setTitulo(String p) {

        if (_titulo.equals(p))
            return;
        _seModifico = true;
        this._titulo = p;
    }
    public String getTitulo() {
        esperar();
        return this._titulo.trim();
    }

    public void setArtista(String p) {
        _seModifico = true;
        this._artista = p;
    }
    public String getArtista() {
        esperar();
        return this._artista.trim();
    }

    public void setAlbum(String p) {
        if (_album.equals(p))
            return;
        _seModifico = true;
        this._album = p;
    }
    public String getAlbum() {
        esperar();
        return this._album.trim();
    }

    public void setAnnus(String p) {
        if (_annus.equals(p))
            return;
        _seModifico = true;
        this._annus = p;
    }
    public String getAnnus() {
        esperar();
        return this._annus.trim();
    }

    public void setComentario(String p) {
        if (_comentario.equals(p))
            return;
        _seModifico = true;
        this._comentario = p;
    }
    public String getComentario() {
        esperar();
        return this._comentario.trim();
    }

    public void setGenero(String p) {
        if (_genero.equals(p))
            return;
        _seModifico = true;
        this._genero = p;
    }

    public String getGenero() {
        esperar();
        return this._genero;
    }

    public boolean tieneTag(){
        return _tieneTag;
    }

    public void grabar() throws Exception{
        if (!_seModifico)
            return;

	    StringBuffer nuevo = new StringBuffer(127);
        nuevo.append("TAG");
        nuevo.append(ponerEspacios(getTitulo(), 30));
        nuevo.append(ponerEspacios(getArtista(), 30));
        nuevo.append(ponerEspacios(getAlbum(), 30));
        nuevo.append(ponerEspacios(getAnnus(), 4));
        nuevo.append(ponerEspacios(getComentario(), 30));
//        nuevo.append(ponerEspacios(getGenero(), 1));//no se modifica

        byte[] aGrabar = nuevo.toString().getBytes();
        RandomAccessFile raf = null;
        long len = _file.length();
        try
        {
            raf = new RandomAccessFile(_file, "rw");
            if (_tieneTag)
                raf.seek(_file.length() - 128);
            else{
                raf.seek(_file.length() );
                len += 128;
            }

            raf.write(aGrabar);
            raf.setLength(len);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            if (raf != null){
                try {
                    raf.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        Boolean yaTiene = tieneID3v2();
        if ((_agregarID3v2SiEsNecesario && esNecesarioID3v2())
                || (yaTiene != null && yaTiene)){
            Wamp v2 = new Wamp();
            v2.setAlbum(getAlbum());
            String artista = getArtista();
            v2.setArtist(artista);
            v2.setComment(getComentario());
            String s = getTitulo();
            v2.setTitle(s);
            v2.setYear(getAnnus());
            ID3V2Tag v2Tag = v2.getV2Tag();
            ID3V2Writer.writeTag(_file, v2Tag);
        }

        _seModifico = false;
    }

    /**
     * Devuelve true si alguno de los tags supera el tamaño máximo de id3v1
     * @return
     */
    public boolean esNecesarioID3v2() {
        return getTitulo().length() > 30
                || getAlbum().length() > 30
                || getArtista().length() > 30
                || getComentario().length() > 30
                || getTitulo().length() > 30;
    }

    public File getFile(){
        return _file;
    }
/******************************************************************************/
/*                          Métodos privados                                  */
/******************************************************************************/


    private void parsear(byte[] info){
        String tag = getString(info, 0, 3);
        if (!tag.equals("TAG")){
            _tieneTag = false;
            return;
        }
        _tieneTag = true;
        _titulo = getString(info, 3, 30);
        _artista = getString(info, 33, 30);
        _album  = getString(info, 63, 30);
        _annus = getString(info, 93, 4);
        _comentario = getString(info, 97, 30);
        _genero     = getString(info, 127, 1);

// '''     _Artist 30 characters        '''
//'''     _Album 30 characters         '''
//'''     _Year 4 characters           '''
//'''     _Comment 30 characters       '''
//'''     _Genre 1 byte (ver al final) '''
    }



    private String getString(byte[] info, int desde, int cant){
        String res;
        int i;

        for (i = 0; i < cant; i++)
        {
            byte b = info[i + desde];
            if (b == 0)
                break;
        }
        res = new String(info);
        int hasta = desde + i;
        res = res.substring(desde, hasta);
        return res;
    }

    private String ponerEspacios (String str, int tamTotal){
        StringBuffer res = new StringBuffer(str);
        while (res.length() < tamTotal)
            res.append(" ");

        return res.toString().substring(0, tamTotal);
    }

    public void esperar(){
        if (_datosCargados) {
        }
        else{
            while(!_datosCargados){
//                String msg = "Todavía falta cargar los datos de " +
//                        _file.getAbsolutePath();
//                System.out.println(msg);
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void run()    {
        RandomAccessFile raf = null;
        try{
            byte[] info = new byte[128];

            if (!_file.exists())
                throw new Exception("No existe el archivo + " +
                        _file.getAbsolutePath());
            if (_file.length() < 128 )
                return;
            long posID3v1 = _file.length() - 128;
            raf = new RandomAccessFile(_file, "r");

            //Me fijo si tiene ID3 v2
            byte[] b = new byte[3];
            raf.seek(0);
            raf.read(b);
            boolean tiene = new String(b).equalsIgnoreCase("ID3");
            _tieneID3v2 = tiene;

            if(!tiene){ //si tiene hay que conseguir su tamaño...
                FrameMP3 fm = new FrameMP3(raf);
                _bitrate = fm.getBitrate();
            }

            raf.seek(posID3v1);

            int le = raf.read(info);
            if (le < 128)
                System.out.println("problemas......");
            raf.close();
            parsear(info);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            if (raf != null)
                try {
                    raf.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }

//        try {
//            _tieneID3v2 = new Boolean(ID3V2Reader.tieneID3v2(_file));
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//            _tieneID3v2 = Boolean.FALSE;
//        }
//        if (!_tieneID3v2.booleanValue()){
//            try {
//                FrameMP3 fr = new FrameMP3(_file);
//                _bitrate =  fr.getBitrate();
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        }

        _datosCargados = true;
    }

    public void agregarID3v2SiEsNecesario(boolean b) {
        _agregarID3v2SiEsNecesario = b;    
    }

}
