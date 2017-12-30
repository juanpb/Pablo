package p.gui.treefs;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TreeModel extends DefaultTreeModel{
    static TreePath _tp = null;
    Nodo _root = null;
    private static final String DISQUETERA = "A:\\";


    public TreeModel(Nodo root){
        super(root);
        _root = root;
    }


    //sobreescribo métodos de DefaultTreeModel

    public Object getChild(Object parent, int index){
        if (!(parent instanceof Nodo)){
            System.out.println("TreeModel.getChild, !parent instanceof Nodo");
            return null;
        }
        Nodo nodo = (Nodo)parent;
        return nodo.getChild(index);
    }

    public int getChildCount(Object parent) {
        if (!(parent instanceof Nodo)){
            System.out.println("TreeModel.getChildCount, !parent instanceof Nodo");
            return 0;
        }
        Nodo nodo = (Nodo)parent;
        return nodo.getChildren().length;
    }


    public Object getRoot() {
        return _root;
    }

    static public void main(String[] args)
    {
//        try{
//            UIManager.setLookAndFeel(new BasicLookAndFeel());
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }


        JFrame frame = new JFrame("TreeTable");
        final TreeFileSystem tree = new TreeFileSystem();

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                Nodo n = (Nodo)tree.getLastSelectedPathComponent();

                if (n == null){
                    System.out.println("node == null");
                    return;
                }
                String x = n.getFile().getAbsolutePath();

                System.out.println("*-*dir = " + x);
//                _tp = tree.getSelectionPath();
//                tree.expandPath(_tp);
            }
        });


        JScrollPane treeView = new JScrollPane(tree);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        frame.getContentPane().add(new JScrollPane(tree));
        frame.pack();
        frame.setVisible(true);

        Nodo no = (Nodo)tree.getTreeModel().getChild(
                        tree.getTreeModel().getRoot(), 2);
        TreeNode[] tnp = tree.getTreeModel().getPathToRoot(no);
        TreePath tp = new TreePath(tnp);
        tree.expandPath(tp);

        tree.setSelectionPath(tp);
        tree.scrollPathToVisible(tp);


        tree.fireTreeExpanded(tp);

    }
}