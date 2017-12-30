package p.pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DragTest extends JFrame implements DragSourceListener,
    DragGestureListener {

  DragSource ds;

  JList jl;

  StringSelection transferable;

  String[] items = { "Java", "C", "C++", "Lisp", "Perl", "Python" };

  public DragTest() {
    super("Drag Test");
    setSize(200, 150);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    });
    jl = new JList(items);
    jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    getContentPane().add(new JScrollPane(jl), BorderLayout.CENTER);

    ds = new DragSource();
    DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(jl,
        DnDConstants.ACTION_COPY, this);
    setVisible(true);
  }

  public void dragGestureRecognized(DragGestureEvent dge) {
    System.out.println("Drag Gesture Recognized!");
      System.out.println("jl.getSelectedValue().toString() = " + jl.getSelectedValue().toString());
    transferable = new StringSelection(jl.getSelectedValue().toString());
    ds.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
  }

  public void dragEnter(DragSourceDragEvent dsde) {
    System.out.println("Drag Enter");
  }

  public void dragExit(DragSourceEvent dse) {
    System.out.println("Drag Exit");
  }

  public void dragOver(DragSourceDragEvent dsde) {
    System.out.println("Drag Over");
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {
    System.out.print("Drag Drop End: ");
    if (dsde.getDropSuccess()) {
      System.out.println("Succeeded");
    } else {
      System.out.println("Failed");
    }
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    System.out.println("Drop Action Changed");
  }

  public static void main(String args[]) {
    new DragTest();
  }
}