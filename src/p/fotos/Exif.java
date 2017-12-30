package p.fotos;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Exif{
    private static final Logger logger = Logger.getLogger(Exif.class);

    private String _fNumber = null;
    private String _speed = null;
    private String _meteringMode = null;
    private String _flash = null;
    private String _focalLength = null;
    private String _iso = null;
    private Date fechaOriginal;
    private int thumbnailOffset = 0;
    private int thumbnailLenght = 0;
    private File file;


    public static void main(String[] args) throws IOException {
        String s = "D:\\TMP\\2016-01-09_01 Juliana.jpg";
        String sal = "D:\\TMP\\2016-01-09_01 Juliana_th.tiff";
        final Exif exif = new Exif(s);
        //final byte[] bytes = exif.getThumbnailByte();

        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(sal)) ;

        byte[] b = new byte[fis.available()];
        fis.read(b);

       // exif.grabar(bytes, new File(sal));


        JFrame f = new JFrame();
        JButton but = new JButton(new ImageIcon(b));
        f.setLayout(new GridLayout(1,0));
        f.add(but);
        f.setSize(500,500);
        f.setVisible(true);

    }


    public Exif(String file){
        this(new File(file));
    }

    public Exif(File f){
        try
        {
            file = f;
            Metadata md = JpegMetadataReader.readMetadata(f);
            parse(md);
//            verTodo(md);
//            copiarTM();
        }
        catch (Exception e) {
            logger.error("problemas con el archivo " + f.getAbsolutePath());
            logger.error(e.getMessage(), e);
        }
    }

    public Image getThumbnail(){
        final byte[] bytes = getThumbnailByte();
        return Toolkit.getDefaultToolkit().createImage(bytes);
    }

    public static void grabarThumbnail(String archivoEntrada, String dirSalida){
        File fEn = new File(archivoEntrada);
        Exif exif = new Exif(archivoEntrada);
        File fSal = new File(dirSalida, fEn.getName());
        exif.grabar(exif.getThumbnailByte(), fSal);
    }

    public byte[] getThumbnailByte(){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.skip(thumbnailOffset);
            byte[] lect = new byte[thumbnailLenght];
            fis.read(lect);
            return lect;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    private void grabar(byte[] bytes, File archSalida) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(archSalida);
            fos.write(bytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    private void verTodo(Metadata md) throws MetadataException {
        Iterator directories = md.getDirectoryIterator();
        while (directories.hasNext()) {
            Directory directory = (Directory)directories.next();
            System.out.println("directory.getName() = " + directory.getName());
            Iterator tags = directory.getTagIterator();
            while (tags.hasNext()) {
                Tag tag = (Tag)tags.next();
                System.out.println(tag.getTagName() + " = " + tag.getDescription());
            }
            System.out.println("");
            if (directory.hasErrors()) {
                Iterator errors = directory.getErrors();
                while (errors.hasNext()) {
                    System.out.println("ERROR: " + errors.next());
                }
            }
        }
        System.out.println("------------------------------");
    }

    public String toString(){
        return armarRes();
    }
    private void parse(Metadata md){
        Iterator directories = md.getDirectoryIterator();
        while (directories.hasNext()) {
            Directory d = (Directory)directories.next();
            if (d.getName().equals("Exif")){
                parse(d);
            }
        }
    }

    private String armarRes()
    {
        StringBuilder res = new StringBuilder();
        res.append("" + _fNumber);
        res.append("; " + _speed);
        res.append("; " + _iso);
        res.append("; FocalLength=" + _focalLength);
        res.append("; Flash=" + _flash);
        res.append("; " + _meteringMode);
        return res.toString();
    }


    private void parse(Directory d){
        Iterator it = d.getTagIterator();
        while (it.hasNext())
        {
            Tag t = (Tag) it.next();
            try
            {
                String tn = t.getTagName();
                if (tn.equals("F-Number"))
                    _fNumber = t.getDescription();
                else if (tn.equals("ISO Speed Ratings"))
                    _iso= t.getDescription();
                else if (tn.equals("Focal Length"))
                    _focalLength = t.getDescription();
                else if (tn.equals("Flash"))
                    _flash = t.getDescription();
                else if (tn.equals("Metering Mode"))
                    _meteringMode = t.getDescription();
                else if (tn.equals("Exposure Time"))
                    _speed = t.getDescription();
                else if (tn.equals("Date/Time Original"))
                    fechaOriginal = parsearFecha(t.getDescription());
                else if (tn.equals("Thumbnail Offset"))
                    thumbnailOffset = parsearTamaño(t.getDescription());
                else if (tn.equals("Thumbnail Length"))
                    thumbnailLenght = parsearTamaño(t.getDescription());
            }
            catch (MetadataException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public Date getFechaOriginal(){
        return fechaOriginal;
    }
    private int parsearTamaño(String f){
        //4596 bytes
        if (!f.contains("bytes")){
            System.out.println("VER problema con tamaño: " + f);
            return 0;
        }
        f = f.replace("bytes", "").trim();
        return Integer.parseInt(f);
    }

    private Date parsearFecha(String f){
        //2008:03:06 11:34:05
        try {
            Calendar cal = Calendar.getInstance();
            int año = Integer.parseInt(f.substring(0, 4));
            int mes = Integer.parseInt(f.substring(5, 7)) - 1;
            int dia = Integer.parseInt(f.substring(8, 10));
            int hh = Integer.parseInt(f.substring(11, 13));
            int min = Integer.parseInt(f.substring(14, 16));
            int seg = Integer.parseInt(f.substring(17, 19));

            cal.set(año, mes, dia, hh, min, seg);

            return cal.getTime();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
