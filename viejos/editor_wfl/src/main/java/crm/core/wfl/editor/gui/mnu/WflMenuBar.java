package crm.core.wfl.editor.gui.mnu;

import crm.core.wfl.editor.gui.action.*;
import crm.core.wfl.editor.gui.action.pruebas.WflNoTocarAction;
import crm.core.wfl.editor.gui.cmp.WflContainerControl;
import crm.core.wfl.editor.gui.cmp.WflProcessDefinitionContainer;
import crm.core.wfl.editor.gui.util.WflUtility;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * User: JPB
 * Date: Feb 6, 2006
 * Time: 1:29:46 PM
 */
public class WflMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private WflProcessDefinitionContainer container;
    private JCheckBoxMenuItem zoomWindowsMenuItem;

    public WflMenuBar(WflProcessDefinitionContainer container) {
        this.container = container;
        init();
    }

    private void init() {
        WflContainerControl control = container.getControl();

        JMenu mnuFile = new JMenu("Archivo");
        JMenu mnuEdit = new JMenu("Edición");
        JMenu mnuInsert = new JMenu("Insertar");

        JMenuItem itemOpen = new JMenuItem(new WflOpenFileAction(control, true));
        WflNewFileAction crlNew = new WflNewFileAction(control, true);
        JMenuItem itemNew = new JMenuItem(crlNew);
        final JMenuItem itemSave = new JMenuItem(new WflSaveFileAction(control, true));


        JMenuItem itemSaveAs = new JMenuItem(new WflSaveAsFileAction(control, true));
//        JMenuItem itemPrint = new JMenuItem(new WflPrintAction(control,  true));
        JMenuItem itemCreateImagen = new JMenuItem(new WflCreateImagenAction(control,  true));
        JMenuItem itemExit = new JMenuItem(new WflExitAction(control,  true));

        //No visibles
        JMenuItem itemRestoreCursor = new JMenuItem(new WflCancelAddMarkAction(control, false));
        JMenuItem itemMoveCompRight = new JMenuItem(
                new WflMoveComponentAction(control, WflMoveComponentAction.RIGHT, false));
        JMenuItem itemMoveCompDown = new JMenuItem(
                new WflMoveComponentAction(control, WflMoveComponentAction.DOWN, false));
        JMenuItem itemMoveCompLeft = new JMenuItem(
                new WflMoveComponentAction(control, WflMoveComponentAction.LEFT, false));
        JMenuItem itemMoveCompUp = new JMenuItem(
                new WflMoveComponentAction(control, WflMoveComponentAction.UP, false));

        JMenuItem itemNoTocar = new JMenuItem(new WflNoTocarAction(control));
        itemRestoreCursor.setVisible(false);
        itemMoveCompRight.setVisible(false);
        itemMoveCompUp.setVisible(false);
        itemMoveCompLeft.setVisible(false);
        itemMoveCompDown.setVisible(false);
        itemNoTocar.setVisible(false);


        //Archivo
        mnuFile.add(itemNew);
        mnuFile.add(itemOpen);
        mnuFile.add(itemSave);
        mnuFile.add(itemSaveAs);
        mnuFile.addSeparator();
//        mnuFile.add(itemPrint);
        mnuFile.add(itemCreateImagen);
        mnuFile.addSeparator();
        mnuFile.add(itemExit);
        //No visibles
        mnuFile.add(itemRestoreCursor);
        mnuFile.add(itemMoveCompRight);
        mnuFile.add(itemMoveCompLeft);
        mnuFile.add(itemMoveCompDown);
        mnuFile.add(itemMoveCompUp);
        mnuFile.add(itemNoTocar);

        //Edición
        WflProcessDefinitionAction pdAction = new WflProcessDefinitionAction(control, true);
        WflDeleteAction delAction = new WflDeleteAction(control, true);
        WflSelectAllAction selAction = new WflSelectAllAction(control, true);
        WflEditComponentAction editlAction = new WflEditComponentAction(control, true);
        WflParallelismAction addParAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_CREATE, true);
        WflParallelismAction editParAction = new WflParallelismAction(control,
                WflParallelismAction.TYPE_EDIT, true);
        WflParallelismAction removeParAction = new WflParallelismAction(control, 
                WflParallelismAction.TYPE_REMOVE, true);
        mnuEdit.add(new JMenuItem(pdAction));
        mnuEdit.addSeparator();

        mnuEdit.add(new JMenuItem(addParAction));
        mnuEdit.add(new JMenuItem(editParAction));
        mnuEdit.add(new JMenuItem(removeParAction));
        mnuEdit.addSeparator();

        mnuEdit.add(new JMenuItem(delAction));
        mnuEdit.add(new JMenuItem(editlAction));
        mnuEdit.add(new JMenuItem(selAction));
        mnuEdit.addSeparator();
        
        mnuEdit.add(new WflAlignMnu(control));
        mnuEdit.addSeparator();
        
        WflZoomWindowAction zoomAction = new WflZoomWindowAction(control, true);
        zoomWindowsMenuItem = new JCheckBoxMenuItem(zoomAction);
        mnuEdit.add(zoomWindowsMenuItem);

        //Insert
        WflAddStateAction addS = new WflAddStateAction(control, "Estado");
        WflAddFunctionAction addF = new WflAddFunctionAction(control, "Función");
        WflAddPreFunctionAction addPreF = new WflAddPreFunctionAction(control, "preFunción");
        WflAddPosFunctionAction addPosF = new WflAddPosFunctionAction(control, "posFunción");
        mnuInsert.add(new JMenuItem(addS));
        mnuInsert.add(new JMenuItem(addF));
        mnuInsert.add(new JMenuItem(addPreF));
        mnuInsert.add(new JMenuItem(addPosF));

        super.add(mnuFile);
        super.add(mnuEdit);
        super.add(mnuInsert);

        mnuFile.addMenuListener(new MenuListener(){
            public void menuSelected(MenuEvent e){
                boolean b = WflUtility.getLastFileOpen() != null;
                itemSave.setEnabled(b);
            }
            public void menuDeselected(MenuEvent e){}
            public void menuCanceled(MenuEvent e){}
        });

    }

    /**
     * Setea el estado de la ventana de zoom.
     * @param zoomFrameActive Estado de la ventana.
     */
	public void setZoomWindowsStatus(boolean zoomFrameActive) {
		zoomWindowsMenuItem.setSelected(zoomFrameActive);
	}


}
