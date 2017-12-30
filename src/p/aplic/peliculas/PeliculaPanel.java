package p.aplic.peliculas;

import p.aplic.peliculas.util.Util;
import p.gui.ComboBox;
import p.gui.LabelTextField;
import p.gui.TextArea;
import p.util.Constantes;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * User: JPB
 * Date: Feb 2, 2007
 * Time: 2:13:34 PM
 *
 * Modifica la película asignada o crea una nueva si no se le asignó.
 */
public class PeliculaPanel extends JPanel {

    private LabelTextField nombre = new LabelTextField("Nombre", new Dimension(300, 40));
    private LabelTextField aka = new LabelTextField("AKA", new Dimension(100, 40));
    private LabelTextField annus = new LabelTextField("Año", new Dimension(100, 40));
    private LabelTextField poster1 = new LabelTextField("Poster 1", new Dimension(100, 40));
    private LabelTextField poster2 = new LabelTextField("Poster 2", new Dimension(100, 40));
    private LabelTextField poster3 = new LabelTextField("Poster 3", new Dimension(100, 40));
    private LabelTextField paises = new LabelTextField("Países", new Dimension(100, 40));
    private LabelTextField id = new LabelTextField("Id", new Dimension(100, 40));
    private LabelTextField imdb = new LabelTextField("IMDB puntaje", new Dimension(100, 40));
    private LabelTextField duracion = new LabelTextField("Duración", new Dimension(100, 40));
    private TextArea critica = new TextArea("Crítica", new Dimension(700, 160));
    private TextArea datosTecnicos = new TextArea("Datos Técnicos", new Dimension(200, 160));
    private TextArea comentario = new TextArea("Comentarios", new Dimension(250, 60));
    private TextArea problemas = new TextArea("Problemas", new Dimension(250, 60));
    private ComboBox estado = new ComboBox("Estado", new Dimension(100, 40));
    private JButton btnSacarLineasVacias = new JButton("Sacar líneas vacías");
    private JButton btnCargarNuevoDVD = new JButton("Cargar nuevo DVD");
    private JButton btnCargarHD = new JButton("HD");
    private JButton btnCargarHDyCrearCarpeta = new JButton("carpeta HD");
    private Pelicula pelicula;
    private boolean usóNuevoContadorDVD = false;
    private String raizFotos = System.getProperty("dirRaizFotosCompleto");

    //imdb
//    private JLabel lblIMDB = new JLabel("IMDB");
//    private LabelTextField imdbId = new LabelTextField("Id", new Dimension(100, 40));
//    private LabelTextField imdbDuracion = new LabelTextField("Duración", new Dimension(100, 40));
//    private LabelTextField imdbAnnus = new LabelTextField("Año", new Dimension(100, 40));
//    private LabelTextField imdbPuntaje = new LabelTextField("Puntaje", new Dimension(100, 40));
//    private LabelTextField imdbDirector = new LabelTextField("Persona", new Dimension(100, 40));
//    private LabelTextField imdbActores = new LabelTextField("Actores", new Dimension(100, 40));

    public PeliculaPanel() {
        hacerPantalla();
        btnSacarLineasVacias.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                critica.setText(sacarLineasVacias (critica.getText()));
                comentario.setText(sacarLineasVacias (comentario.getText()));
                problemas.setText(sacarLineasVacias (problemas.getText()));
                datosTecnicos.setText(sacarLineasVacias (datosTecnicos.getText()));
            }
        });

        btnCargarHD.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cargarNuevoHD();
                copiarNombreCarpetaAlPP();
            }
        });
        btnCargarHDyCrearCarpeta.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boolean fallo;
                try {
                    Integer a = Integer.parseInt(annus.getText());
                    fallo = a < 1000 || a > 2444;
                } catch (NumberFormatException e1) {
                    fallo = true;
                }
                if (fallo){
                    JOptionPane.showMessageDialog(null, "Mal definido el año: '" + annus.getText() + "'");
                }else{
                    cargarNuevoHD();
                    crearYAbrirCarpetaHD();
                }
            }
        });
        btnCargarNuevoDVD.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cargarNuevoDVD();
            }
        });
        poster1.addFocusListener(new FocusAdapter(){
             public void focusLost(FocusEvent e) {
                 agregarTT(poster1);
             }
        });
        poster2.addFocusListener(new FocusAdapter(){
             public void focusLost(FocusEvent e) {
                 agregarTT(poster2);
             }
        });
        poster3.addFocusListener(new FocusAdapter(){
             public void focusLost(FocusEvent e) {
                 agregarTT(poster3);
             }
        });
    }

    private void agregarTT(LabelTextField tf){
        String t = tf.getText();
        tf.setImageTooltip(raizFotos + "\\" + t);
    }

    private void cargarNuevoDVD() {
        id.setText(btnCargarNuevoDVD.getText());
        usóNuevoContadorDVD = true;

        Pelicula.Estado s = (Pelicula.Estado) estado.getSelectedItem();
        if (s.equals(Pelicula.Estado.BAJANDO))
            estado.setSelectedItem(Pelicula.Estado.SIN_VER);
    }

    private void copiarNombreCarpetaAlPP() {
        String s = nombre.getText() + " (" + annus.getText() + ")";
        p.util.Util.pergarEnElPortapapeles(s);
    }

    private void crearYAbrirCarpetaHD() {
        String nom = nombre.getText();
        //parchecito para cuando se bajan pelis ya existentes en DVD
        if (nom.endsWith("+"))
            nom = nom.substring(0, nom.length()-1);
        String annusTxt = annus.getText().trim();
        String s = reemplazarCaracteresInvalidos(nom.trim()) + " (" + annusTxt + ")";
        String raizHD = getRaizHD(annusTxt.trim());
        File f = new File(raizHD, s);
        boolean existe = f.exists();
        if (!existe)
            existe = f.mkdir();
        if (!existe)
            JOptionPane.showMessageDialog(null, "No se pudo crear la carpeta '" +
                f.getAbsolutePath() + "'");
        else{//abro la carpeta
            String explorerExe = System.getProperty("explorerExe");
//            String cmd = "C:\\WINDOWS\\explorer.exe " + f.getAbsolutePath();
            String cmd = explorerExe + " \"" + f.getAbsolutePath()+ " \"";
            try{
                Runtime.getRuntime().exec(cmd);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        p.util.Util.pergarEnElPortapapeles(f.getAbsolutePath());
    }

    private String getRaizHD_old(String annus) {
        int annusPel = Integer.parseInt(annus);
        int annusPC2 = Integer.MAX_VALUE;
        String annusParaCarpetaHD2 = System.getProperty("annusParaCarpetaHD2");
        if (annusParaCarpetaHD2 != null)
            annusPC2 = Integer.parseInt(annusParaCarpetaHD2);

        if(annusPel >= annusPC2)
            return System.getProperty("dirRaizCarpetaHD2");
        else
            return System.getProperty("dirRaizCarpetaHD");
    }
    private String getRaizHD(String annus) {
        int annusPel = Integer.parseInt(annus);
        int cont = 1;
        boolean buscar = true;
        String res = null;
        while (res == null){
            String dirRaizCarpetaHD = System.getProperty("dirRaizCarpetaHD" + cont);

            if (dirRaizCarpetaHD == null)  {
                throw new RuntimeException("No se definió carpeta para el año " + annus);
            }
            //formato (0-2009)L:\\películas\\
            int j = dirRaizCarpetaHD.indexOf(")");
            String años = dirRaizCarpetaHD.substring(1, j);
            int d = Integer.parseInt(años.substring(0, años.indexOf("-")));
            int h = Integer.parseInt(años.substring(años.indexOf("-")+1));
            if (d <= annusPel && annusPel <= h)
                res = dirRaizCarpetaHD.substring(j+1);

            cont++;
        }
        return res.trim();
    }

    //reemplaza los caracteres que no son válidos para windows
    private String reemplazarCaracteresInvalidos(String orig){
        return orig.replaceAll(":", ",");
    }

    private void cargarNuevoHD() {
        //si había algo le agrego '- HD' al final, pe: DVD 99 - HD
        String txt = id.getText();
        if (!txt.contains("HD")){ //si ya contiene HD no renombro
            if (txt != null && !txt.trim().equals(""))
                id.setText(txt + " - HD");
            else
                id.setText("HD");
        }

        Pelicula.Estado s = (Pelicula.Estado) estado.getSelectedItem();
        if (s.equals(Pelicula.Estado.BAJANDO))
            estado.setSelectedItem(Pelicula.Estado.SIN_VER);
    }

    private String sacarLineasVacias(String text) {
        String res = "";
        StringTokenizer st = new StringTokenizer(text, Constantes.NUEVA_LINEA);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (!s.trim().equals("")){
                res += s + Constantes.NUEVA_LINEA;
            }
        }
        return res;
    }

    private void hacerPantalla() {
        setLayout(new GridBagLayout());
        Insets ins1 = new Insets(5, 5, 5, 5);

        //Fila 1
        int yy = 0;
        add(nombre, new GridBagConstraints(0, yy, 2, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            ins1, 0, 0));
        add(aka, new GridBagConstraints(2, yy, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            ins1, 0, 0));
        add(annus, new GridBagConstraints(3, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));

        //Fila 2
        yy++;
        add(paises, new GridBagConstraints(0, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));
        add(id, new GridBagConstraints(1, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));
        add(imdb, new GridBagConstraints(2, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));

        add(estado, new GridBagConstraints(3, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));

        //Fila 3
        yy++;
        add(poster1, new GridBagConstraints(0, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));
        add(poster2, new GridBagConstraints(1, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));
        add(poster3, new GridBagConstraints(2, yy, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));
        add(duracion, new GridBagConstraints(3, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                ins1, 0, 0));

        //Fila 4
        yy++;
        add(btnSacarLineasVacias, new GridBagConstraints(0, yy, 4, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                ins1, 0, 0));
        add(btnCargarHDyCrearCarpeta, new GridBagConstraints(2, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                ins1, 0, 0));
        JPanel pnl = new JPanel(new BorderLayout(0, 0));
        pnl.add(btnCargarNuevoDVD, BorderLayout.WEST);
        pnl.add(btnCargarHD, BorderLayout.CENTER);
        Insets in = new Insets(0, 0, 0, 0);
        btnCargarHD.setMargin(in);
        btnCargarNuevoDVD.setMargin(in);
        btnCargarNuevoDVD.setPreferredSize(new Dimension(80, 20));
        btnCargarHD.setPreferredSize(new Dimension(40,20));
        btnCargarHDyCrearCarpeta.setPreferredSize(new Dimension(120,20));
        add(pnl, new GridBagConstraints(3, yy, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                ins1, 0, 0));


        //Fila 5
        yy++;
        JPanel pnlAux = new JPanel(new GridBagLayout());
//        JPanel pnlIMDB = new JPanel(new GridBagLayout());
//        pnlIMDB.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        pnlAux.add(critica, new GridBagConstraints(0, 0, 1, 3, 0.8, 0.8,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));
        pnlAux.add(datosTecnicos, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.2,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));
        //Fila 6
        //imdb
//        int x = 0;
//        pnlIMDB.add(imdbId, new GridBagConstraints(x++, 0, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                ins1, 0, 0));
//        pnlIMDB.add(imdbAnnus, new GridBagConstraints(x++, 0, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                ins1, 0, 0));
//        pnlIMDB.add(imdbDuracion, new GridBagConstraints(x++, 0, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                ins1, 0, 0));
//        pnlIMDB.add(imdbPuntaje, new GridBagConstraints(x, 0, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                ins1, 0, 0));
//        pnlIMDB.add(imdbDirector, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
//                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                ins1, 0, 0));
//        pnlIMDB.add(imdbActores, new GridBagConstraints(2, 1, 3, 1, 0.1, 0.1,
//                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                ins1, 0, 0));

//        LabelTextField lblIMDB = new LabelTextField("xxx", new Dimension(120, 20));
//        lblIMDB.setPreferredSize(new Dimension(120, 20));
//        pnlAux.add(lblIMDB, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.WEST, GridBagConstraints.NONE,
//                ins1, 0, 0));
//        pnlAux.add(pnlIMDB, new GridBagConstraints(0, 2, 1, 1, 0.8, 0.8,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                ins1, 0, 0));
        pnlAux.add(comentario, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.2,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));
        pnlAux.add(problemas, new GridBagConstraints(1, 2, 1, 1, 0.2, 0.2,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));
        add(pnlAux, new GridBagConstraints(0, yy, 4, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0));
    }

    public void setPelicula(Pelicula p){
        pelicula = p;
        if (p == null){
            limpiar();
            return ;
        }

        nombre.setText(p.getNombre());
        aka.setText(p.getAka());
        paises.setText(p.getPais());
        id.setText(p.getId());
        imdb.setText(p.getImdbPuntaje());
        estado.setSelectedItem(p.getEstado());
        critica.setText(p.getCritica());
        duracion.setText(p.getDuracion());
        annus.setText(p.getAnnus());
        comentario.setText(p.getComentario());
        problemas.setText(p.getProblemas());
        datosTecnicos.setText(p.getDatosTecnicos());

        java.util.List posters = p.getPosters();
        if (posters.size() > 0){
            String t = posters.get(0).toString();
            poster1.setText(t);
            agregarTT(poster1);
        }
        if (posters.size() > 1){
            String t = posters.get(1).toString();
            poster2.setText(t);
            agregarTT(poster2);
        }
        if (posters.size() > 2){
            String t = posters.get(2).toString();
            poster3.setText(t);
            agregarTT(poster3);
         }
        btnCargarNuevoDVD.setVisible(Pelicula.Estado.BAJANDO.equals(
                p.getEstado()));
        btnCargarHD.setVisible(Pelicula.Estado.BAJANDO.equals(
                p.getEstado()));
        btnCargarHDyCrearCarpeta.setVisible(Pelicula.Estado.BAJANDO.equals(
                p.getEstado()));

//        imdbId.setText(p.getImdbId());
    }


    public void panelAPortaPap(){
        Pelicula pel = getPelicula();
        String x = Util.pelicula2XML(pel);
        p.util.Util.pergarEnElPortapapeles(x);
    }

    public void limpiar(){
        pelicula = null; //para que no pise la vieja
        String s = System.getProperty("estadoInicialParaAlta");
        estado.setSelectedItem(s);
        limpiar(getComponents());
    }

    private void limpiar(Component[] cmps) {
        for(Component c : cmps) {
            if (c instanceof JTextComponent)
                ((JTextComponent)c).setText("");
            else if (c instanceof Container)
                limpiar(((Container)c).getComponents());
        }
    }

    public Pelicula getPelicula(){
        if (pelicula == null)
            pelicula = new Pelicula();

        pelicula.setNombre(vacio2nullYTrim(nombre.getText()));
        pelicula.setAka(vacio2nullYTrim(aka.getText()));
        pelicula.setPais(vacio2nullYTrim(paises.getText()));
        pelicula.setId(vacio2nullYTrim(id.getText()));
        pelicula.setImdbPuntaje(vacio2nullYTrim(imdb.getText()));
        pelicula.setEstado((Pelicula.Estado) estado.getSelectedItem());
        pelicula.setCritica(vacio2nullYTrim(critica.getText()));
        pelicula.setDuracion(vacio2nullYTrim(duracion.getText()));
        pelicula.setAnnus(vacio2nullYTrim(annus.getText()));
        pelicula.setComentario(vacio2nullYTrim(comentario.getText()));
        pelicula.setProblemas(vacio2nullYTrim(problemas.getText()));
        pelicula.setDatosTecnicos(vacio2nullYTrim(datosTecnicos.getText()));

        pelicula.setPosters(new ArrayList<String>());
        if (!poster1.getText().equals(""))
            pelicula.addPoster(poster1.getText());
        if (!poster2.getText().equals(""))
            pelicula.addPoster(poster2.getText());
        if (!poster3.getText().equals(""))
            pelicula.addPoster(poster3.getText());       


        //imdb
//        pelicula.setImdbId(imdbId.getText());
        return pelicula;
    }

    private String vacio2nullYTrim(String x){
        if (x != null && x.trim().equals(""))
            return null;
        else
            return x.trim();
    }

    public void setEstados(java.util.List<Pelicula.Estado> estados) {
        for (Pelicula.Estado est : estados) {
            estado.addItem(est);
        }
        String s = System.getProperty("estadoInicialParaAlta");
        estado.setSelectedItem(s);
    }

    public boolean usóNuevoContadorDVD() {
        return usóNuevoContadorDVD;
    }

    public void setContadorDVD(Integer contDVD) {
        btnCargarNuevoDVD.setText("DVD " + contDVD);
    }
}
