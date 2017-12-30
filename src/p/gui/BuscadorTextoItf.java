package p.gui;

/**
 * User: JPB
 * Date: Jun 29, 2005
 * Time: 2:34:15 PM
 */
public interface BuscadorTextoItf {
    public void buscar(String txt,
                     boolean directionDown,
                     boolean caseSensitive,
                     boolean fromCursor,
                     boolean selectText);
}
