package p.aplic.renombrar;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CustomTableCellRenderer extends DefaultTableCellRenderer
{
    RenombrarGUI _gui = null;

    public CustomTableCellRenderer(RenombrarGUI gui){
        _gui = gui;
    }


    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column)
    {
        Color colorSeleccionado = Color.CYAN;

        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
        java.util.List<Fila> dt = _gui.getDatosTabla();
        Fila fila = dt.get(row);

        if (_gui.modificarDirectorios() ){
            if (tieneCaracterInvalido(value.toString())){
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.RED);
            }
            else{
                if (isSelected)
                    cell.setBackground(colorSeleccionado);
                else
                    cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
        }
        else if (fila.esDir()){
            //Colorea la fila si contiene un directorio
            cell.setBackground(Color.BLACK);
            cell.setForeground(Color.GREEN);
        }
//        else if (column == RenTableModel.COLUMNA_TITULO){
//            if (fila.esImagen()){
//                System.out.println("Es imagen");
//                Exif exif = new Exif(fila.getFile());
//                JLabel lbl = new JLabel();
//                lbl.setIcon(new ImageIcon(exif.getThumbnail()));
//                setValue(lbl);
//            }
//        }
        else if (column == RenTableModel.COLUMNA_ARCHIVO){
            //Colorea la columna de "nombre de archivo" si tiene algún
            //caracter inválido
            String v = (String) value;
            if (tieneCaracterInvalido(v)){
                cell.setForeground(Color.RED);
            }
            else{
                cell.setForeground(Color.BLACK);
            }
            if (isSelected)
                cell.setBackground(colorSeleccionado);
            else
                cell.setBackground(Color.WHITE);
        }
        else if (column == RenTableModel.COLUMNA_ALBUM
            || column == RenTableModel.COLUMNA_ARTISTA
            || column == RenTableModel.COLUMNA_COMENTARIO
            || column == RenTableModel.COLUMNA_TITULO){
            //Colorea la columna de ID3 si supera el tamaño máximo

            String v = (String) value;
            if (v != null && v.length() > 30){
                cell.setForeground(Color.RED);
            }
            else{
                cell.setForeground(Color.BLACK);
            }
            if (isSelected)
                cell.setBackground(colorSeleccionado);
            else
                cell.setBackground(Color.WHITE);
        }
        else if (column == RenTableModel.COLUMNA_ANNUS){
            //Colorea la columna de ID3 si supera el tamaño máximo

            String v = (String) value;
            if (v != null && v.length() > 4){
                cell.setForeground(Color.RED);
            }
            else{
                cell.setForeground(Color.BLACK);
            }
            if (isSelected)
                cell.setBackground(colorSeleccionado);
            else
                cell.setBackground(Color.WHITE);
        }
        else if (column == RenTableModel.COLUMNA_TIENE_ID3_V2){
            //Colorea la columna de ID3 si supera el tamaño máximo
            if (value instanceof Boolean){
                Boolean v = (Boolean) value;
                if (v != null && v.equals(Boolean.TRUE)){
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.RED);
                }
                else{
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.WHITE);
                }
            }
            else{
                //ocurre cuando no es un mp3
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.WHITE);
            }
        }
        return cell;
    }

     private boolean tieneCaracterInvalido(String v) {

         boolean res = false;
         if (v.indexOf("\"") > 0
                || v.indexOf("/") > 0
                || v.indexOf("\\") > 0
                || v.indexOf(":") > 0
                || v.indexOf("*") > 0
                || v.indexOf("?") > 0
                || v.indexOf(">") > 0
                || v.indexOf("<") > 0
                || v.indexOf("|") > 0 )
            res = true;
        else
            res = false;

         return res;
     }
 }