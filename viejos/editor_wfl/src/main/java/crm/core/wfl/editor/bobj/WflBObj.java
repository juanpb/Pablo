package crm.core.wfl.editor.bobj;

/**
 * User: JPB
 * Date: Jan 23, 2006
 * Time: 4:09:12 PM
 */
public abstract class WflBObj implements Comparable {
    
	/**
	 * {@inheritDoc}
	 */
    public int compareTo(Object o) {
        WflBObj other = (WflBObj)o;
        return other.getBObjId().compareTo(getBObjId());
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean equals(Object obj) {
        if(obj instanceof WflBObj)
            return compareTo(obj)==0;
        return false;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public int hashCode() {
        if (getBObjId() == null)
            return super.hashCode();
        return getBObjId().hashCode();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public abstract String getBObjId();
    
    
}
