package p.aplic;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Date;

/*
* Elimina bytes del final de un archivo.Hay que cablear el nombre del archivo
* y la cantidad de bytes a eliminar del final del archivo.
*/
public class CortarMp3 implements ActionListener{
    private static final Logger logger = Logger.getLogger(CortarMp3.class);

    private Frame frame = new Frame("Cortar MP3");
    private Label label1 = new Label();
    private Label label2 = new Label();
    private TextField txtSeg = new TextField();
    private TextField txtFileName = new TextField();
    private TextField _txtMensaje = new TextField();
    private Button btmEjecutar = new Button();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    //cantidad de bytes a eliminar desde el final del archivo
    private static long _bytesAEliminar          = 0;
    private static int  BYTES_POR_SEGUNDO        = 16348;
    private static long _bytesAEliminarPrincipio = 0;

    //nombre del archivo de entrada a modificar
    private static String               _fileName = "";
    private static FileOutputStream     _fos;
    private static FileInputStream      _fis;
    private static BufferedInputStream  _bis;
    private static BufferedOutputStream _bos;
    private static File                 _fileIn;
    private static int                  TAM_ID3 = 128;
    private static boolean              SALVAR_ID3 = true;
    private static String               _archivoProperties = "";
    private static int                  _segundosAEliminar = 0;

    public CortarMp3(){
         crearIU();
    }

    public static void main(String [] arg){

        CortarMp3 cortarMp3 = new CortarMp3();
  }


  private static String agregarBarras(String p){
    if (p == null)
      return "";
    char[] res = new char[100];
    int x = 0;
    for(int i=0; i < p.length(); i++){
      Character c = new Character(p.charAt(i));
      if (c.equals("\\".charAt(0)))
        res[x++] = c.charValue();
      res[x++] = c.charValue();
    }
    return new String(res);

  }

  private void abrirArchivos() throws IOException, FileNotFoundException{
    _fileIn = new File (_fileName);
    if (!_fileIn.exists()){
        _txtMensaje.setText("No existe el archivo " + _fileName);
        return;
    }

  //  _fileIn.renameTo(new File(_archivo + ".viejo"));
//    _fileIn = new File(_archivo + ".viejo");
    File archSalida = new File(_fileName + ".sal");

    _fos = new FileOutputStream(archSalida);
    _fis = new FileInputStream(_fileIn);

    _bis = new BufferedInputStream(_fis);
    _bos = new BufferedOutputStream(_fos);
  }

  private void  editar() throws IOException{
    Date date1, date2;

    if (_segundosAEliminar < 1){
        _txtMensaje.setText("Se cortaron 0 segundos");
        return;
    }
    byte[] arreglo = new byte[128];
    //bytes a eliminar desde el final
    _bytesAEliminar          = _segundosAEliminar * BYTES_POR_SEGUNDO;
    _bytesAEliminarPrincipio = 0 * BYTES_POR_SEGUNDO;

    try{
        abrirArchivos();
    }
    catch(Exception e){
        logger.error(e.getMessage(), e);
        _txtMensaje.setText( e.getMessage());
        return;
    }


    if (SALVAR_ID3){//guardo los últimos 128 bytes del archivo
      long p =  _fileIn.length();
      _fis.skip( p - 128);
      int x = _fis.read(arreglo);
      _bis.close();
      _bis = new BufferedInputStream( new FileInputStream(_fileName));//para resetear
    String xx = new String(arreglo);
    System.out.println("aslñkd: " + arreglo);
    }

System.out.println("_fileIn.length() = " + _fileIn.length());
    long itereraciones = _fileIn.length() - _bytesAEliminar - 128;
                                           //     _bytesAEliminarPrincipio -128;
    int aux;

    date1 = new Date();
    _txtMensaje.setText("empezó\n");
    //_bis.skip(_bytesAEliminarPrincipio);
    for (int i=0; i < itereraciones ; i++){
      aux = _bis.read();
      _bos.write(aux);
    }

    if (SALVAR_ID3)//escribo los últimos 128 bytes del archivo
      _bos.write(arreglo);


    date2 = new Date();
    long tiempo = date2.getTime() - date1.getTime();
    _txtMensaje.setText("Demoró: " + tiempo  +  " milisegundos");
  }

    private void cerrarArchivos(){
        try{
            _bos.close();
            _bis.close();
        }
        catch(IOException e){
            logger.error(e.getMessage(), e);
            _txtMensaje.setText( e.getMessage());
        }
    }

    private void crearIU(){

        frame.setSize(600, 300);
        label1.setText("Segundos a eliminar");
        frame.setLayout(gridBagLayout1);
        txtSeg.setText("1");
        txtFileName.setText(".mp3");
        btmEjecutar.setLabel("Ejecutar");
        label2.setText("Archivo a modificar:");
        frame.add(label1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 11, 0, 0), 9, 14));
        frame.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(34, 11, 0, 0), 14, 0));
        frame.add(txtSeg, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                  new Insets(9, 0, 0, 0), 26, 1));
        frame.add(txtFileName, new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 11, 0, 12), 349, 5));
        frame.add(btmEjecutar, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(0, 84, 6, 33), 29, -4));
        frame.add(_txtMensaje, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                  new Insets(9, 11, 91, 12), 370, 41));
        btmEjecutar.addActionListener(this);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        txtFileName.requestFocus();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        _fileName = txtFileName.getText();
        String s = txtSeg.getText();
        try{
            _segundosAEliminar = Integer.parseInt(s);
        }catch(Exception ex){
            _segundosAEliminar = 0;
            _txtMensaje.setText("Poner bien los segundos\n");
        }
        try{
            editar();
        }catch(Exception ex ){
            ex.printStackTrace();
            _txtMensaje.setText(ex.getMessage());
        }
        cerrarArchivos();
    }
}