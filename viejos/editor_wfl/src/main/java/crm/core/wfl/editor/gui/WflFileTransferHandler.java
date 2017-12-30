package crm.core.wfl.editor.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import crm.core.wfl.editor.gui.action.WflOpenFileAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;


public class WflFileTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;
	private WflOpenFileAction openAction;
	
	public WflFileTransferHandler(WflContainerControl control) {
		openAction = new WflOpenFileAction(control);
	}
	/**
	 * Verifico que puedo recibir el drop.
	 * @param comp Componente que esta involucrado.
	 * @param transferFlavors Sabores en los que se presenta.
	 * @return true si lo puede recibir.
	 */
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		for(DataFlavor df : transferFlavors) {
			if(df.getMimeType().equals("application/x-java-file-list; class=java.util.List"))
				return true;
		}
		return false;
	}

	/**
	 * Indica que tiene que importar los datos.
	 * @param comp Componente que recibe el drop.
	 * @param t Objeto a transferir.
	 * @return true si la transferencia se pudo recibir.
	 */
	@SuppressWarnings("unchecked")
	public boolean importData(JComponent comp, Transferable t) {

		if (!canImport(comp, t.getTransferDataFlavors()))
             return false;
		
		try {
			List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);

			if(files.size()>0) {
				String fileName = files.get(0).getAbsolutePath();
				openAction.openFile(new File(fileName));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
}
