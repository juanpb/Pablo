package p.aplic.renombrarFotos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Thumb {
    private File    archivo;
    private JTextField txtField = new JTextField();
    private RenombrarGUI gui = null;

    /**
     * Usarlo sólo para archivos que no sean MP3
     */
    public Thumb(File f, RenombrarGUI gui){
        setArchivo(f);
        this.gui = gui;
    }

    public Component getPanel() {
        int width = 300;
        ImageIcon ii = new ImageIcon(archivo.getAbsolutePath());
        int h = ii.getIconHeight();
        int w = ii.getIconWidth();
        double proporcionHW = (double) h / w;
        int alt = (int) (proporcionHW * width);

        Image newimg = ii.getImage().getScaledInstance(width, alt, Image.SCALE_SMOOTH);

        JButton bot = new JButton(new ImageIcon(newimg));
        GridBagLayout layout = new GridBagLayout();

        bot.setBorder(BorderFactory.createStrokeBorder(new BasicStroke()));
        txtField.setBorder(BorderFactory.createStrokeBorder(new BasicStroke()));
        txtField.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel pnlThumb = new JPanel(layout);
        pnlThumb.setBorder(BorderFactory.createStrokeBorder(new BasicStroke()));
        int in = 0;
        pnlThumb.add(bot, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(in, in, in, in), 0, 0));
        pnlThumb.add(txtField,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(in, in, in, in), 0, 0));

        bot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicBoton();
            }
        });
        return pnlThumb;
    }

    private void clicBoton() {
        if (gui.renombrarOnClick()){
            String viejo = txtField.getText();
            if (viejo == null)
                viejo = archivo.getName();
            if (!formatoValidoParaRenombrarClic(viejo))
                return ;
            String aInsertar = gui.getAInsertarEnClic() ;
            if (aInsertar.length() == 0)
                return ;
            //formato: AAAA-MM-DD_[contador] [nombres*].jpg

            //Versión que reemplaza lo existente:
//            int indEspacio = viejo.indexOf(" ");
//            if (indEspacio < 0)
//                indEspacio =  viejo.toLowerCase().indexOf(".jpg");
//
//            String nuevo = viejo.substring(0, indEspacio) + " " + aInsertar + ".jpg";

            //Si ya está agregado => no hago nada
            if (viejo.contains(aInsertar))
                return;

            //Versión que agrega al final:
            int indice = viejo.toLowerCase().indexOf(".jpg");
            if (indice < 0){
                indice = viejo.toLowerCase().indexOf(".jpeg");
                if (indice < 0){
                    JOptionPane.showMessageDialog(null, "No se encuentra extensión jpg/jpeg");
                }
            }
            String nombre = viejo.substring(0, indice);
            nombre = nombre.replace(" y ", ", ");
            if (nombre.length() > "2014-01-02_17 N".length())
                nombre += " y";

            String nuevo = nombre + " " + aInsertar + ".jpg";
            txtField.setText(nuevo);
        }
    }


    private boolean formatoValidoParaRenombrarClic(String txt) {
        //AAAA-MM-DD_[contador] [nombres*].jpg
        if (txt.length() < 11){
            JOptionPane.showMessageDialog(null, "Para renombrar se necesita formato: AAAA-MM-DD_[contador] [nombres*].jpg"  );
            return false;
        }
        else
            return true;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setNuevoNombre(String nuevoNom) {
        txtField.setText(nuevoNom);
    }

    public String getNuevoNombre() {
        return txtField.getText();
    }

    public boolean cambio() {
        return !txtField.getText().equals(archivo.getName());
    }

    public void setArchivo(File file) {
        archivo = file;
        txtField.setText(archivo.getName());
    }
}