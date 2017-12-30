package p.aplic.renombrar;

import p.mp3.MP3File;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RenTableModel extends DefaultTableModel{
    private List<Fila> filas = new ArrayList<Fila>();
    public static final int CANT_COL = 8;

    public static final int COLUMNA_ARCHIVO = 0;
    public static final int COLUMNA_TITULO = 1;
    public static final int COLUMNA_ARTISTA = 2;
    public static final int COLUMNA_ALBUM = 3;
    public static final int COLUMNA_ANNUS = 4;
    public static final int COLUMNA_COMENTARIO = 5;
    public static final int COLUMNA_BITRATE = 6;
    public static final int COLUMNA_TIENE_ID3_V2 = 7;

    private Vector titulosMP3 = new Vector();
    private Vector titulosSoloArchivo = new Vector();

    public RenTableModel(){
        titulosSoloArchivo.add("Archivo");

        titulosMP3.add("Archivo");
        titulosMP3.add("Título");
        titulosMP3.add("Artista");
        titulosMP3.add("Álbum");
        titulosMP3.add("Año");
        titulosMP3.add("Comentario");
        titulosMP3.add("Bitrate");
        titulosMP3.add("v2");

        setColumnIdentifiers(titulosMP3);
    }

    public int getRowCount(){
        if (filas == null)
            return 0;
        else
            return filas.size();
    }

    public int getColumnCount(){
        return CANT_COL;
    }

    public Object getValueAt(int f, int c){
        if (f > getRowCount())
            return "f > getRowCount()";
        if (c > CANT_COL)
            return "c > this.CANT_COL";


        Fila fila = filas.get(f);
        if (c == COLUMNA_ARCHIVO){
            if (fila.esDir())
                return fila.getDir();
            else
                return fila.getFileName();
        }

        if (fila.esDir() || fila.esArchivoNoMP3())
            return "";
        MP3File mf = fila.getMP3File();
        switch (c)
        {
            case COLUMNA_ALBUM:
                return mf.getAlbum();
            case COLUMNA_ANNUS:
                return mf.getAnnus();
            case COLUMNA_ARTISTA:
                return mf.getArtista();
            case COLUMNA_COMENTARIO:
                return mf.getComentario();
            case COLUMNA_TITULO:
                return mf.getTitulo();
            case COLUMNA_BITRATE:{
                int bitrate = mf.getBitrate();
                if (bitrate > 0)
                    return Integer.toString(bitrate);
                else
                    return "";
            }
            case COLUMNA_TIENE_ID3_V2:{
                return mf.tieneID3v2();
            }
            default:
                return "falló switch";
        }
    }

    public void setValueAt(Object nuevo, int f, int c){
        if (f > getRowCount()){
            System.out.println("setValueAt(...), f > getRowCount()");
            return;
        }
        if (c > CANT_COL){
            System.out.println("setValueAt(...), c > this.CANT_COL");
            return ;
        }

        Fila fila = filas.get(f);

        if (c == COLUMNA_ARCHIVO){
            fila.setFileName(nuevo.toString()); 
        }
        else if (fila.esMP3() && c == COLUMNA_TITULO){
            fila.setTitle(nuevo.toString());
        }
        else if (fila.esMP3() && c == COLUMNA_COMENTARIO){
            fila.setComentario(nuevo.toString());
        }
        else if (fila.esMP3() && c == COLUMNA_TIENE_ID3_V2){
        }
        else
            System.out.println("setValue(...), no se implementó para esta columna");
    }


    public void clear(){
        filas.clear();
    }

    public void addFila(Fila f){
        filas.add(f);
    }

    public List<Fila> getFilas(){
        return filas;
    }

    public Fila getFila(int fila){
        return filas.get(fila);
    }
}