package p.aplic.libretadirecciones.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * User: JP
 * Date: 02/05/2005
 * Time: 21:02:21
 */
public class TablaRenderer extends DefaultTableCellRenderer{

    private static final Color FOREROUND_PAR = Color.BLACK;
    private static final Color BACKGROUND_PAR = new Color(232, 232, 232);
    private static final Color FOREROUND_IMPAR = Color.BLACK;
    private static final Color BACKGROUND_IMPAR = new Color(252, 252, 252);
    private static final Color FOREROUND_SELECT = Color.BLACK;
    private static final Color BACKGROUND_SELECT = Color.CYAN;

    public TablaRenderer( ){
    }

    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column)
    {

        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);

        boolean selRow = (table.getSelectedRow() == row);
        //Un color para las filas impares,
        // otro para las pares
        // y otro si está seleccionada

        if (selRow){
            cell.setBackground(BACKGROUND_SELECT);
            cell.setForeground(FOREROUND_SELECT);
        }
        else if (row % 2 == 0 ){
            cell.setBackground(BACKGROUND_PAR);
            cell.setForeground(FOREROUND_PAR);
        }
        else{
            cell.setBackground(BACKGROUND_IMPAR);
            cell.setForeground(FOREROUND_IMPAR);
        }

        return cell;
    }

 }
