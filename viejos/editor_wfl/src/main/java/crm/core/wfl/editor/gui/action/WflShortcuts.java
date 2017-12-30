package crm.core.wfl.editor.gui.action;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * User: JPB
 * Date: Feb 16, 2006
 * Time: 5:12:08 PM
 */
public class WflShortcuts {
    public static final KeyStroke ZOOM =
        KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK);
    public static final KeyStroke SELECT_ALL =
            KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
    public static final KeyStroke CREATE_PARALLELISM =
            KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_MASK);
    public static final KeyStroke EDIT_PARALLELISM =
            KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK);
    public static final KeyStroke DELETE =
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
    public static final KeyStroke REMOVE_PARALLELISM =
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK);    
    public static final KeyStroke ADD_STATE =
            KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK);
    public static final KeyStroke EDIT =
            KeyStroke.getKeyStroke (KeyEvent.VK_ENTER, 0);
    public static final KeyStroke ADD_FUNCTION =
            KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_MASK);
    public static final KeyStroke NEW_FILE =
            KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK);
    public static final KeyStroke OPEN_FILE =
            KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
    public static final KeyStroke PROCESS_DEFINITION =
            KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK);
    public static final KeyStroke PRINT =
            KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
    public static final KeyStroke ADD_PREFUNCTION =
            KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK);
    public static final KeyStroke ADD_POSFUNCTION =
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK);
    public static final KeyStroke SAVE =
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
    public static final KeyStroke SAVE_AS =KeyStroke.getKeyStroke(
            KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK);
    public static final KeyStroke EXIT =
            KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_MASK );
    public static final KeyStroke RESTORE_CURSOR =
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    public static final KeyStroke CREATE_IMAGE =
            KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK );

    public static final KeyStroke ALIGN_TOP = KeyStroke.getKeyStroke(
            KeyEvent.VK_T, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);
    public static final KeyStroke ALIGN_BOTTOM = KeyStroke.getKeyStroke(
            KeyEvent.VK_B, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);
    public static final KeyStroke ALIGN_HORIZONTAL = KeyStroke.getKeyStroke(
            KeyEvent.VK_H, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);
    public static final KeyStroke ALIGN_VERTICAL = KeyStroke.getKeyStroke(
            KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);
    public static final KeyStroke ALIGN_LEFT = KeyStroke.getKeyStroke(
            KeyEvent.VK_L, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);
    public static final KeyStroke ALIGN_RIGHT = KeyStroke.getKeyStroke(
            KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.ALT_MASK);

    public static final KeyStroke VIEW_XML = KeyStroke.getKeyStroke(
            KeyEvent.VK_X, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
    public static final KeyStroke EDIT_TRACE = KeyStroke.getKeyStroke(
            KeyEvent.VK_T, InputEvent.CTRL_MASK);
    

    public static final KeyStroke UP = KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, InputEvent.CTRL_MASK, true);
    public static final KeyStroke DOWN = KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, InputEvent.CTRL_MASK, true);
    public static final KeyStroke LEFT = KeyStroke.getKeyStroke(
            KeyEvent.VK_LEFT, InputEvent.CTRL_MASK, true);
    public static final KeyStroke RIGHT = KeyStroke.getKeyStroke(
            KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK, true);
}
