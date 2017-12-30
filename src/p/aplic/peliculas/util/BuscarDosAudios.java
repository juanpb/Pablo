package p.aplic.peliculas.util;

import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;

import java.util.ArrayList;
import java.util.List;

/**
Busca pelis HD con 2 audios
 */
public class BuscarDosAudios {
    public BuscarDosAudios(String[] args) {
                String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\películas.xml";

        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }
        List<Pelicula> repet = new ArrayList<Pelicula>();
        String audio = "audio";

        for (Pelicula p : pelis.getPeliculas()) {
            if (p.getNombre().equals("Celebrity")){
                System.out.println("");
            }
            if (p.isHD()){
                String dt = p.getDatosTecnicos();
                if (dt != null){
                    dt = dt.toLowerCase();

                    if (p.getId().length() > 5){//también es dvd o divx
                        int d = dt.indexOf("versión hd");
                        int h = dt.indexOf("__");
                        if( d > 0){
                            dt = dt.substring(d);
                        }

                    }

                    int i = dt.indexOf(audio);
                    if (i > 0){
                        i = dt.indexOf(audio, i + audio.length());
                        if (i > 0)   {
                            repet.add(p);
                        }
                    }
                }
            }
        }
        if (repet.size() == 0)
            System.out.println("Sin repetidos");
        else{
            for (Pelicula p : repet) {
                System.out.println(p.getNombre() + "(" + p.getAnnus() + ")");
            }
        }

    }

    public static void main(String[] args) {
        args = new String[1];
        args[0] = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\config.ini";
        new BuscarDosAudios(args);
    }
}
