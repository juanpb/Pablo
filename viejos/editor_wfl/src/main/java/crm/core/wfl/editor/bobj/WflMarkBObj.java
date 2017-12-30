package crm.core.wfl.editor.bobj;

/**
 * User: JPB
 * Date: Feb 27, 2006
 * Time: 2:51:50 PM
 */
public class WflMarkBObj extends WflBObj{
    private String id = null;
    private String name = null;
    private boolean deletable = false;

    private String commnet = null;

    public WflMarkBObj() {

    }

    public WflMarkBObj(String id, String name, boolean deletable) {
        this.id = id;
        this.name = name;
        this.deletable = deletable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public String getBObjId() {
        return getId();
    }

    public String toString() {
        String s = getId() + " - " + getName() + " -";
        if (!isDeletable())
            s += " No";
        s += " borrable";
        return s;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WflMarkBObj))
            return false;
        WflMarkBObj bo = (WflMarkBObj)obj;
        if (bo == this)
            return true;
        if (id == null)
            return bo.getId() == null;

        return id.equals(bo.getId());
    }

    public int hashCode() {
        if (id == null)
            return 0;
        return id.hashCode();
    }

    public String isDeletableAsString() {
        if (isDeletable())
            return "S";
        else
            return "N";
    }

    public String getCommnet() {
        return commnet;
    }

    public void setCommnet(String commnet) {
        this.commnet = commnet;
    }
}
