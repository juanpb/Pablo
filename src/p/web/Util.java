package p.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Util {
    public Util() {
    }


  public static void copiarURL(String pUrl, String destino)

  {
      try{

          URL url  = new URL(pUrl);

          URLConnection urlC = url.openConnection();
          // Copy resource to local file, use remote file

          // if no local file name specified
          InputStream is = url.openStream();

          FileOutputStream fos = new FileOutputStream(destino);

          int oneChar, count=0;
          while ((oneChar=is.read()) != -1){
             fos.write(oneChar);
             count++;
          }

          is.close();

          fos.close();
          System.out.println(count + " byte(s) copied");

      }

      catch (MalformedURLException e)

      { System.err.println(e.toString()); }

      catch (IOException e)

      { System.err.println(e.toString()); }

  }

}