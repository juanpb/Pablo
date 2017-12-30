package p.aplic;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Backup {
    private static final Logger logger = Logger.getLogger(Backup.class);

    private static ZipOutputStream _zos;
    private static String COMENTARIO = "//";

    /**
     * Levanta el archivo archivosAComprimir, que tiene en la primer línea la
     * dirección donde se enviará el zip, y en las demás líneas los nombres de
     * los archivos a comprimir.
     */
    public static void main(String args[]) throws Exception{

        if (args.length == 0){
            String txt = "Pasar como parámetro el archivo con la lista de archivos a comprimir.";
            JOptionPane.showMessageDialog(null, txt, "Faltan parámetros", JOptionPane.ERROR_MESSAGE);
            return ;
        }
        String archivosAComprimir = args[0];

        //creo el archivo con los discos
        File f = new File ("M:\\Discos.txt");
        try {
            f.createNewFile();
            ListaCompleta lc = new ListaCompleta();
            lc.discos(f,"M:");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        File file = new File(archivosAComprimir);
        BufferedReader br = new BufferedReader( new FileReader(file));
        String aZipear = "";
        if (!br.ready())
            return;

        boolean seguir = true;
        while (br.ready() && seguir){
            aZipear = br.readLine();
            if (!aZipear.startsWith(COMENTARIO)){
                seguir = false;
            }
        }

        String _destinoZip = aZipear.trim();
        File fileDes = new File(_destinoZip, "backup_" +
                                p.util.Fechas.getAAAADDMM() + ".zip");

        if (args.length > 0){
            args[0] = fileDes.getAbsolutePath();
        }
        FileOutputStream fos = new FileOutputStream(fileDes);
		_zos = new ZipOutputStream(fos);

        System.out.println("Se guardará el zip en " + fileDes.getPath());
        while (br.ready()){
            aZipear = br.readLine();
            if (!aZipear.startsWith(COMENTARIO))
                dirFunc(aZipear);
        }
        _zos.flush();
        _zos.close();
        fos.close();
        System.out.println("listo el zip");
    }

    /**
     * El archListaBackup debe tener en la primera línea sin comentario el
     * dirctorio donde se guardará el zip
     * @param archListaBackup
     * @throws IOException
     */
    public static void hacerBackup(String archListaBackup)
            throws IOException {
        File file = new File(archListaBackup);
        BufferedReader br = new BufferedReader( new FileReader(file));
        String aZipear = "";
        if (!br.ready())
            return;
        /*
        Levanta el archivo archListaBackup, que tiene en la primer línea la
        dirección donde se enviará el zip, y en las demás líneas los nombres de
        los archivos a comprimir.
        */
        boolean seguir = true;
        while (br.ready() && seguir){
            aZipear = br.readLine();
            if (!aZipear.startsWith(COMENTARIO)){
                seguir = false;
            }
        }
       String directorioDestino = aZipear.trim();
        File fileDes = new File(directorioDestino, "backup_" +
                                p.util.Fechas.getAAAADDMM() + ".zip");

        FileOutputStream fos = new FileOutputStream(fileDes);
		_zos = new ZipOutputStream(fos);

        System.out.println("Se guardará el zip en " + fileDes.getPath());
        while (br.ready()){
            aZipear = br.readLine();
            if (!aZipear.startsWith(COMENTARIO))
                dirFunc(aZipear);
        }
        _zos.flush();
        _zos.close();
        fos.close();
        System.out.println("listo el zip");
    }


    /**
     * Si recibe un archivo, lo guarda. Si recibe un directorio, guarda todos
     * los archivos de ese directorio, y llama recursivamente a sus subdirectorios
     */
    private static void dirFunc(String pAZipear){
        if (pAZipear != null && !pAZipear.trim().equals("")){
            File dirObj = new File (pAZipear);
            if (dirObj.exists()) {
                if (dirObj.isDirectory()) {
                    File [] fileList = dirObj.listFiles();
                    for (File aFileList : fileList) {
                        if (aFileList.isDirectory())
                            dirFunc(aFileList.getPath());
                        else if (aFileList.isFile())
                            zipFunc(aFileList.getPath());
                    }
                }else
                    zipFunc(pAZipear);
            }else
                System.out.println (pAZipear + " no existe.");
        }
    }

    public static void zipFunc (String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedInputStream bis = new BufferedInputStream(fis);

			// Create a Zip Entry and put it into the archive (no data yet).
			ZipEntry fileEntry = new ZipEntry(filePath.substring(3));
			_zos.putNextEntry(fileEntry);

			// Create a byte array object named data and declare byte count variable.
			byte[] data = new byte[1024];
			int byteCount;
			// Create a loop that reads from the buffered input stream and writes
			// to the zip output stream until the bis has been entirely read.
			while ((byteCount = bis.read(data, 0, 1024)) > -1) {
				_zos.write(data, 0, byteCount);
			}

		} catch (IOException e) {
            logger.error(e.getMessage(), e);
		}
	}


    public Backup() {
    }

    public static String dateFunc () {

			Calendar calendar = Calendar.getInstance();
            String YY = (calendar.get(Calendar.YEAR)+"");
			String MM = ((calendar.get(Calendar.MONTH)+1)+"");
			String DD = (calendar.get(Calendar.DAY_OF_MONTH)+"");


			if (MM.length() == 1) {	MM = "0"+MM; }
			if (DD.length() == 1) {	DD = "0"+DD; }

			return (YY+"-"+MM+"-"+DD);
	}
}