package p.gui.treefs;


import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TreeFileSystem extends JTree{

    private static final String DISQUETERA = "A:\\";

    private TreeModel tm = null;

    public TreeFileSystem(){
        init();
    }

//EJEMPLO para oir seleccion de nodos:
//
//        tree.addTreeSelectionListener(new TreeSelectionListener() {
//            public void valueChanged(TreeSelectionEvent e) {
//                Nodo n = (Nodo)tree.getLastSelectedPathComponent();
//
//                if (n == null){
//                    System.out.println("node == null");
//                    return;
//                }
//                String x = n.getFile().getAbsolutePath();
//
//                System.out.println("dir = " + x);
//            }
//        });


    public TreeModel getTreeModel(){
        return tm;
    }

/******************************************************************************/
/*                          Métodos privados                                  */
/******************************************************************************/
    private void init(){

        Nodo root = new Nodo(new File(File.pathSeparator));
        File[] raices = File.listRoots();
        Nodo[] ch; //new Nodo[raices.length - 1]; //-1 por la disquetera
        List<Nodo> ch2 = new ArrayList<Nodo>();

    for (File childFile : raices) {
        if (!childFile.getAbsolutePath().equals(DISQUETERA)) {
            Nodo n = new Nodo(childFile);
            ch2.add(n);
        }
    }
    ch = new Nodo[ch2.size()];
        for (int i = 0; i < ch2.size(); i++) {
            Nodo nodo = ch2.get(i);
            ch[i] = nodo;
        }


        root.setChildren(ch);

        tm = new TreeModel(root);
        super.setModel(tm);
        super.putClientProperty("JTree.lineStyle", "Angled");

    }
}