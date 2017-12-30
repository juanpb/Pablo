package p.gui;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * User: JPB
 * Date: Jun 29, 2005
 * Time: 2:35:48 PM
 */
public class Tabla extends JTable implements BuscadorTextoItf{
    private boolean _dirDown;
    private boolean _caseSensitive;
    private boolean _fromCursor;
    private boolean _selectText;
    private long _cerroBusc = 0;

    private boolean _habilitarEnterEnFindTextDlg = true;

    public Tabla() {
        init();
    }

    public Tabla(TableModel dm) {
        super(dm);
        init();
    }

    public Tabla(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    public Tabla(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    public Tabla(int numRows, int numColumns) {
        super(numRows, numColumns);
        init();
    }

    public Tabla(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        init();
    }

    public Tabla(final Object[][] rowData, final Object[] columnNames) {
        super(rowData, columnNames);
        init();
    }

    private void init() {
        //Saca el comportamiento de la tecla enter (baja a la fila siguiente)
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).getParent().
                remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        addKeyListener();
    }

    protected void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                //crtl + F => búsqueda
                if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)){
                    abrirBuscador();
                }

                //F3 => busca de nuevo
                if (e.getKeyCode() == KeyEvent.VK_F3){
                    f3();
                }

            }
        });
    }

    public void habilitarEnterEnFindTextDlg(boolean b){
        _habilitarEnterEnFindTextDlg = b;
    }

    static Window getWindowForComponent(Component parentComponent)
        throws HeadlessException {
        if (parentComponent == null)
            return null;
        if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
            return (Window)parentComponent;
        return getWindowForComponent(parentComponent.getParent());
    }

    private void f3(){
        if (_ultTextoBuscado != null)
            buscar(_ultTextoBuscado, _dirDown, _caseSensitive, _fromCursor, _selectText);
        else
            abrirBuscador();
    }

    public void abrirBuscador() {
        Window win = getWindowForComponent(this);

        if (win instanceof Frame)
            _buscadorDlg = new BuscadorDlg((Frame)win, this);
        else
            _buscadorDlg = new BuscadorDlg((Dialog)win, this);
        _buscadorDlg.setModal(true);
        _buscadorDlg.habilitarEnter(_habilitarEnterEnFindTextDlg);
        Dimension d = _buscadorDlg.getSize();

        Dimension d2 = this.getSize();
        int x = (int) (d2.getWidth() - d.getWidth()) / 2;
        int y = (int) (d2.getHeight() - d.getHeight()) / 2;

        x += win.getX();
        y += win.getY();

        if (y > 350)
            y = 350;
        _buscadorDlg.setBounds(x, y, (int)d.getWidth(), (int)d.getHeight());
//        _findDlg.setSize((int)d.getWidth(), (int)d.getHeight());
        _buscadorDlg.setVisible(true);
        _cerroBusc = System.currentTimeMillis();
    }

    public long getCerroBusc(){
        return _cerroBusc;
    }

/******************************************************************************/
/*  Buscador
/******************************************************************************/
    private String _ultTextoBuscado = null;
    private int    _ultPosTextoBuscado = -1;
    private BuscadorDlg _buscadorDlg = null;

    public void buscar(String txt,
                       boolean direDown,
                       boolean caseSensitive,
                       boolean fromCursor,
                       boolean selectText) {
//        _dirDown = direDown;
//        _caseSensitive = caseSensitive;
//        _fromCursor = fromCursor;
//        _selectText = selectText;

        int f = 0;
        int c = 0;
        int columnCount = getModel().getColumnCount();


        //Si es "Buscar siguiente..."
        if (_ultTextoBuscado != null
                && _ultTextoBuscado.equals(txt)
                && _ultPosTextoBuscado >= 0){
                _ultPosTextoBuscado++;
            f = _ultPosTextoBuscado / columnCount;
            c = _ultPosTextoBuscado % columnCount;
        }

        _ultTextoBuscado = txt;
        _ultPosTextoBuscado = findObject(txt, f, c, caseSensitive);

        //Si es "Buscar siguiente..." y llego al final => empiezo de vuelta
        if (_ultPosTextoBuscado < 0 && (f > 0 || c > 0))
            _ultPosTextoBuscado = findObject(txt, 0, 0, caseSensitive);

        if (_ultPosTextoBuscado < 0){
            String msg = "No se encuentra el texto: '" + txt + "'";
            JOptionPane.showMessageDialog(_buscadorDlg, msg, "Búsqueda",
                    JOptionPane.WARNING_MESSAGE);
            _buscadorDlg.setVisible(true);
        }
        else{
            f = _ultPosTextoBuscado / columnCount;
            c = _ultPosTextoBuscado % columnCount;
            setCellSelectionEnabled(true);
            setRowSelectionInterval(f, f);
            setColumnSelectionInterval(c, c);
            Rectangle r = getCellRect(f, c, false);
            scrollRectToVisible(r);
        }
    }

        /**
     * Posiciones:
     *
     *    0     | 1         | 2         |...    | CANT_COL - 1
     * CANT_COL |CANT_COL+1|  ...               |2 * (CANT_COL - 1)
     *
     * devuelve -1 si no lo encontró
     */
    public int findObject(Object objectToFind) {
        return findObject(objectToFind, 0, 0, false);
    }

    public int findObject(Object objectToFind, int filaDesde,
                          int colDesde, boolean caseSensitive) {
        TableModel model = getModel();
        String ab = objectToFind.toString();
        if (caseSensitive == false)
            ab = ab.toLowerCase();
        int c = colDesde;
        for (int f = filaDesde;  f < model.getRowCount(); f++) {

            for (; c < model.getColumnCount(); c++) {
                Object o = model.getValueAt(f,c);
                if (o == null)
                    continue;
                String vc = o.toString();
                if (caseSensitive == false)
                    vc = vc.toLowerCase();
                if (vc.indexOf(ab) >= 0 )
                    return f * getColumnCount() + c;
            }
            c = 0;
        }
        return -1;
    }
}
