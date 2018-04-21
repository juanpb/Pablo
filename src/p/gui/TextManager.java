package p.gui;

import org.apache.log4j.Logger;
import p.util.Util;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.*;

/**
 * User: JPB
 * Date: Apr 3, 2009
 * Time: 11:21:44 AM
 */
public class TextManager {
    private static final Logger logger = Logger.getLogger(TextManager.class);

    private JPopupMenu _popupMenu = new JPopupMenu();
    private JMenuItem itemCortar = new JMenuItem("Cortar");
    private JMenuItem itemCopiar = new JMenuItem("Copiar");
    private JMenuItem itemPegar = new JMenuItem("Pegar");
    private JMenuItem itemBorrar = new JMenuItem("Borrar");
    private JMenuItem itemSeleccionarTodo = new JMenuItem("Seleccionar todo");

    private JTextComponent txt;

//    private class UndoHandler implements UndoableEditListener {
//        public void undoableEditHappened(UndoableEditEvent e) {
//            if (undomanager != null) {
//                undomanager.addEdit(e.getEdit());
//            }
//        }
//    }

    public TextManager(JTextComponent textComp) {
        txt = textComp;
        agregarKeyActions();
        armarPopupMenu();
        agregarListener();
    }

    //UndoManager, cortar, pegar, etc
    private void agregarKeyActions() {
        final UndoManager undomanager = new UndoManager();
        undomanager.setLimit(1000);

        Document doc = txt.getDocument();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undomanager.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        txt.getActionMap().put("Undo",
                new AbstractAction("Undo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undomanager.canUndo()) {
                                undomanager.undo();
                            }
                        } catch (CannotUndoException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });

        // Bind the undo action to ctl-Z
        txt.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");


        // Create a redo action and add it to the text component
        txt.getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undomanager.canRedo()) {
                                undomanager.redo();
                            }
                        } catch (CannotRedoException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });

        // Bind the redo action to ctl-Y
        txt.getInputMap().put(KeyStroke.getKeyStroke("control V"), "PASTE");

        txt.getActionMap().put("PASTE",
                new AbstractAction("PASTE") {
                    public void actionPerformed(ActionEvent evt) {
                        pegar();
                    }
                });

        // Bind the pegar action to ctl-V
        txt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), "PASTE");


        txt.getActionMap().put("COPY",
                new AbstractAction("COPY") {
                    public void actionPerformed(ActionEvent evt) {
                        copiar();
                    }
                });
        // Bind the copy action to ctl-c
        txt.getInputMap().put(KeyStroke.getKeyStroke("control C"), "COPY");


        txt.getActionMap().put("CUT",
                new AbstractAction("CUT") {
                    public void actionPerformed(ActionEvent evt) {
                        cortar();
                    }
                });
        txt.getInputMap().put(KeyStroke.getKeyStroke("control X"), "CUT");
    }

    private void agregarListener() {
        txt.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e))
                    mostrarPopupMenu(e);
            }
        });
    }

    private void mostrarPopupMenu(MouseEvent e) {
        String tem = txt.getSelectedText();
        boolean cond = tem != null && tem.length() > 0;
        itemCopiar.setEnabled(cond);
        itemCortar.setEnabled(cond);

        String port = "";
        try {
            port = Util.copiarDesdeElPortapapeles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cond = port != null && port.length() > 0;
        itemPegar.setEnabled(cond);
        _popupMenu.show(txt, e.getX(), e.getY());
    }

    private void armarPopupMenu() {

        itemCortar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cortar();
            }
        });

        itemCopiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copiar();
            }
        });

        itemPegar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pegar();
            }
        });

        itemBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrar();
            }
        });

        itemSeleccionarTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seleccionarTodo();
            }
        });

        _popupMenu.add(itemCortar);
        _popupMenu.add(itemCopiar);
        _popupMenu.add(itemPegar);
        _popupMenu.add(itemBorrar);
        _popupMenu.add(itemSeleccionarTodo);
    }

    private void seleccionarTodo() {
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
    }

    private void borrar() {
        txt.setText("");
    }

    private void pegar() {
        borrarSeleccionado();
        try {
            String txtPP = Util.copiarDesdeElPortapapeles();
            String x = txt.getText();
            if (x.equals("")) {
                txt.setText(txtPP);
            } else {
                int p = txt.getCaretPosition();
                String pre = x.substring(0, p);
                String pos = x.substring(p);
                txt.setText(pre + txtPP + pos);
                txt.setCaretPosition(pre.length() + txtPP.length());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //todo NO ANDA
    private void copiar() {
        String x = txt.getSelectedText();
        Util.pegarEnElPortapapeles(x);
    }

    private void borrarSeleccionado() {
        String x = txt.getText();
        int ini = txt.getSelectionStart();
        int fin = txt.getSelectionEnd();
        String pre = x.substring(0, ini);
        String pos = x.substring(fin);

        txt.setText(pre + pos);
        txt.setCaretPosition(ini);
    }

    private void cortar() {
        String x = txt.getSelectedText();
        Util.pegarEnElPortapapeles(x);
        borrarSeleccionado();
    }
}
