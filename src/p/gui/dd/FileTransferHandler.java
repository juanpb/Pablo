package p.gui.dd;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

public class FileTransferHandler extends TransferHandler {
    private FileTransfer control;
    private JComponent componente;// el cmp que recibió la transf.
    private static final Logger logger = Logger.getLogger(FileTransferHandler.class);

    public FileTransferHandler(FileTransfer control) {
        this.control = control;
    }

    /**
     * Verifico que puedo recibir el drop.
     *
     * @param comp            Componente que esta involucrado.
     * @param transferFlavors Sabores en los que se presenta.
     * @return true si lo puede recibir.
     */
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        for(DataFlavor df : transferFlavors) {
            if (df.getMimeType().equals("application/x-java-file-list; class=java.util.List"))
                return true;
        }
        return false;
    }

    /**
     * Indica que tiene que importar los datos.
     *
     * @param comp Componente que recibe el drop.
     * @param t    Objeto a transferir.
     * @return true si la transferencia se pudo recibir.
     */
    public boolean importData(JComponent comp, Transferable t) {
        if (!canImport(comp, t.getTransferDataFlavors()))
            return false;
        try {
            List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            this.componente = comp;
            if (files.size() == 1) {
                File file = files.get(0);
                control.setFile(file);
            } else if (files.size() > 1) {
                control.setFiles(files);
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public JComponent getComponente() {
        return componente;
    }
}