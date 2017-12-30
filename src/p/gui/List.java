package p.gui;

import p.gui.dd.ListTransferHandler;

import javax.swing.*;
import java.util.Vector;

/**
 * User: JPB
 * Date: Apr 7, 2009
 * Time: 8:40:37 AM
 */
public class List extends JList{

    public List() {
        super();
        init();
    }

    private void init() {
        setDragEnabled(true);
        setTransferHandler(new ListTransferHandler());
        setDropMode(DropMode.INSERT);
    }

    public List(ListModel dataModel) {
        super(dataModel);
        init();
    }

    public List(Object[] listData) {
        super(listData);
        init();
    }

    @SuppressWarnings({"UseOfObsoleteCollectionType"})
    public List(Vector<?> listData) {
        super(listData);
        init();
    }

//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                //Turn off metal's use of bold fonts
//	            createAndShowGUI();
//            }
//        });
//    }
//    private static void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("DropDemo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        DefaultListModel listModel = new DefaultListModel();
//        for (int i = 0; i < 10; i++) {
//            listModel.addElement("List Item " + i);
//        }
//
//        //Create and set up the content pane.
//        JComponent newContentPane = new List(listModel);
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }

}
