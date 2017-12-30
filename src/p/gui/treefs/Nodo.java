package p.gui.treefs;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.Enumeration;

public class Nodo implements TreeNode{
    private static final Logger logger = Logger.getLogger(Nodo.class);

    File     _file;
    Object[] _children;
    DirFileFilter _filtro = new DirFileFilter(true);

    public Nodo(File file) {
	    this._file = file;
    }

    /**
     * Returns the the string to be used to display this leaf in the JTree.
     */
    public String toString() {
        String res = _file.getName();
        if (res.equals(""))
            res = _file.getAbsolutePath().substring(0, 1);
	    return res;
    }

    public File getFile() {
	    return _file;
    }

    /**
     * Loads the children, caching the results in the children ivar.
     */
    protected Object[] getChildren() {
        if (_children != null) {
            return _children;
        }
        try {
            String[] files = _file.list(_filtro);
            if(files != null) {
                _children = new Nodo[files.length];
                String path = _file.getPath();
                for(int i = 0; i < files.length; i++) {
                    File childFile = new File(path, files[i]);
                    _children[i] = new Nodo(childFile);
                }
            }
        }
        catch (SecurityException se) {
            logger.error(se.getMessage(), se);
        }
        return _children;
    }

    protected Object getChild(int n) {
        Object[] x = getChildren();
        if (x.length <= n){
            return null;
        }
        return x[n];
    }

    public void setChildren(Object[] ch){
        _children = ch;
    }

    public boolean equals(Object o){
        if (!(o instanceof Nodo))
            return false;
        Nodo n = (Nodo)o;

        return n.getFile().getAbsolutePath().equals(
                    this.getFile().getAbsolutePath());

    }
/******************************************************************************/
/*                          interfaz TreeNode                                  */
/******************************************************************************/
    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    public TreeNode getChildAt(int childIndex){
        return (TreeNode)getChild(childIndex);
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    public int getChildCount(){
        Object[] z = getChildren();
        if (z == null)
            return 0;
        int res = z.length;
        return res;
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent(){
        return null;
    }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     */
    public int getIndex(TreeNode node){
        Object[] x = getChildren();
        for (int i = 0; i < x.length; i++)
        {
            Object o = x[i];
            if (o.equals(node))
                return i;
        }
        return -1;
    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren(){
        return true;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf(){
        int x = getChildCount();
        return (x == 0);
    }

    /**
     * Returns the children of the reciever as an Enumeration.
     */
    public Enumeration children(){
        return null;
    }
}