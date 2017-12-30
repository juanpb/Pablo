package p.gui.dd;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;

public class ListTransferHandler extends TransferHandler {
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    private int indiceDestino;

    public boolean canImport(TransferHandler.TransferSupport info) {
        // Check for String flavor
        return info.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    protected Transferable createTransferable(JComponent c) {
        return new StringSelection(exportString(c));
    }

    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }

        JList list = (JList) info.getComponent();
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        indiceDestino = dl.getIndex();
        boolean insert = dl.isInsert();

        // Get the string that is being dropped.
        Transferable t = info.getTransferable();
        String data;
        try {
            data = (String) t.getTransferData(DataFlavor.stringFlavor);
        }
        catch (Exception e) {
            return false;
        }

        String[] values = data.split("\n");
        addCount = values.length;
        int index = indiceDestino;
        // Perform the actual import.
        for(String v : values) {
            if (insert) {
                listModel.add(index++, v);
            } else {
                listModel.set(index++, v);
            }
        }

        return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == TransferHandler.MOVE);
    }


    //Bundle up the selected items in the list
    //as a single string, for export.
    protected String exportString(JComponent c) {
        JList list = (JList) c;
        indices = list.getSelectedIndices();
        List values = list.getSelectedValuesList();

        StringBuilder buff = new StringBuilder();
        for (Object val: values) {
            if (buff.length() > 0) {
                buff.append("\n");
            }
            buff.append(val == null ? "" : val.toString());
        }

        return buff.toString();
    }

    //Take the incoming string and wherever there is a
    //newline, break it into a separate item in the list.
    protected void importString(JComponent c, String str) {
        JList target = (JList) c;
        DefaultListModel listModel = (DefaultListModel) target.getModel();
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //So this is not allowed.
        if (indices != null && index >= indices[0] - 1 &&
                index <= indices[indices.length - 1]) {
            indices = null;
            return;
        }

        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        String[] values = str.split("\n");
        addCount = values.length;
        for(String value : values) {
            listModel.add(index++, value);
        }
    }

    //If the remove argument is true, the drop has been
    //successful and it's time to remove the selected items
    //from the list. If the remove argument is false, it
    //was a Copy operation and the original list is left
    //intact.
    protected void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            JList source = (JList) c;
            DefaultListModel model = (DefaultListModel) source.getModel();


            //comentado xq no sé qué hace
//            //If we are moving items around in the same list, we
//            //need to adjust the indices accordingly, since those
//            //after the insertion point have moved.
//            if (addCount > 0) {
//                for(int i = 0; i < indices.length; i++) {
//                    if (indices[i] > addIndex) {
//                        indices[i] += addCount;
//                    }
//                }
//            }

            //Como al exportar se agregó item nuevo => hay que borrar los viejos.
            //Si los ítems subieron => los índices que hay que borrar no son los
            //originales, ya que estos bajaron tantas posiciones como ítems seleccionados
            int sumarSiBajaron = 0;
            if (indices.length > 0 && indices[0] > indiceDestino)
                sumarSiBajaron = indices.length;
            for(int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]  + sumarSiBajaron);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}

