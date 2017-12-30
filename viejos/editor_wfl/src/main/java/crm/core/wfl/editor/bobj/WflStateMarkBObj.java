package crm.core.wfl.editor.bobj;

/**
 * User: JPB
 * Date: Feb 27, 2006
 * Time: 2:51:50 PM
 */
public class WflStateMarkBObj extends WflBObj{
    public static final String ALTA = "A";
    public static final String BAJA = "B";
    public static final String MODIFICACION = "M";

    private String action = null;
    private WflMarkBObj mark = null;

    public WflStateMarkBObj() {

    }

    public WflStateMarkBObj(String action, WflMarkBObj miles) {
        this.action = action;
        this.mark = miles;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WflMarkBObj getMark() {
        return mark;
    }

    public void setMark(WflMarkBObj mark) {
        this.mark = mark;
    }

    public String getBObjId() {
        return null;
    }

    public String getActionDescription() {
        String action = getAction();
        if (ALTA.equals(action))
            return "Alta";
        else if (BAJA.equals(action))
            return "Baja";
        else if (MODIFICACION.equals(action))
            return "Modificación";
        else
            return "?";
    }

    public static String getActionByDescription(String s) {
        if ("Alta".equals(s))
            return ALTA;
        else if ("Baja".equals(s))
            return BAJA;
        else if ("Modificación".equals(s))
            return MODIFICACION;
        return null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WflStateMarkBObj))
            return false;

        WflStateMarkBObj bo = (WflStateMarkBObj)obj;
        if (getMark() ==null)
            return bo.getMark() == null;

        boolean b = getMark().equals(bo.getMark());
        if (getAction() == null)
            return b && bo.getAction() == null;
        else
            return b && getAction().equals(bo.getAction());
    }

    public int hashCode() {
        return (getAction() == null)? 0: getAction().hashCode() +
            ((getMark() == null)? 0: getMark().hashCode()) ;
    }
}
