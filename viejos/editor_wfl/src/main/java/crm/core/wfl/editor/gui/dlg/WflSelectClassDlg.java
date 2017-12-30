package crm.core.wfl.editor.gui.dlg;

import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipFile;

/**
 * User: JPB
 * Date: Feb 22, 2006
 * Time: 12:02:23 PM
 */
public class WflSelectClassDlg {
    private static JDialog parent = null;
    private static String selected = null;
    private static TableModel model = null;
    private static JTable table = null;
    private static JDialog dlg = null;

    public static String openFinderClass(JDialog parent) {
        WflSelectClassDlg.parent = parent;
        WflConstants c = WflConstants.getInstance();
        String classpath = c.getProperty(WflConstants.CLASS_PATH);
        JFileChooser fc = new JFileChooser(classpath);
        fc.setAcceptAllFileFilterUsed(false);
        FileFilter choosableFilterClassJar = new FileFilter(){
            public boolean accept(File f){
                if (f.isDirectory())
                    return true;
                String name = f.getName();
                return name.endsWith(".class")
                        || name.endsWith(".jar");
            }

            public String getDescription(){
                return "*.class, *.jar";
            }
        };
        FileFilter choosableFilterClass = new FileFilter(){
            public boolean accept(File f){
                if (f.isDirectory())
                    return true;
                String name = f.getName();
                return name.endsWith(".class");
            }

            public String getDescription(){
                return "*.class";
            }
        };
        FileFilter choosableFilterJar = new FileFilter(){
            public boolean accept(File f){
                if (f.isDirectory())
                    return true;
                String name = f.getName();
                return name.endsWith(".jar");
            }

            public String getDescription(){
                return "*.jar";
            }
        };
        fc.setFileFilter(choosableFilterJar);
        fc.addChoosableFileFilter(choosableFilterClass);
        fc.addChoosableFileFilter(choosableFilterClassJar);
        int i = fc.showDialog(parent, "Seleccionar");
        if (i != JFileChooser.APPROVE_OPTION)
            return null;
        File sf = fc.getSelectedFile();
        String name = sf.getName();
        if (name.endsWith(".class")){
            String path = sf.getPath();
            int f = path.indexOf(File.separator);
            String t = path.substring(f+1, sf.getPath().length() - 6);
            String separator = File.separator;
            if (separator.equals("\\"))
                separator = "\\\\";
            t = t.replaceAll(separator, "\\.");
            if (classpath != null){
                String cp = classpath.substring(f+1);
                cp = cp.replaceAll(separator, "\\.");
                if (t.startsWith(cp))
                    t = t.substring(cp.length());
                if (t.startsWith("."))
                    t = t.substring(1);
            }
            return t;
        }
        else if (name.endsWith(".jar")){
            return openJarFile(sf);
        }
        else
            return null;
    }

    private static String openJarFile(File sf) {
        try {
            ZipFile zf = new ZipFile(sf, ZipFile.OPEN_READ);
            model = new WflSelectClassTableModel(zf);
            table =new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            return  showTable(table);
        } catch (IOException e) {
            WflUtil.showErrorMessage(e.getMessage(), "Error al abrir el jar");
            e.printStackTrace();
            return null;
        }
    }

    private static String showTable(JTable table) {
        dlg = new JDialog(parent);
        dlg.setTitle("Seleccionar clase");
        Container cp = dlg.getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridBagLayout());
        JButton btnAccept = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");

        btnAccept.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                accept();
            }
        });
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                cancel();
            }
        });
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (MouseEvent.BUTTON1 == e.getButton()
                        && e.getClickCount() ==2)
                    accept();
            }
        });
        p.add(btnAccept, new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5,5,5,10), 0,0));
        p.add(btnCancel, new GridBagConstraints(1,0,1,1,0,0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(5,5,5,10), 0,0));
        JScrollPane sp = new JScrollPane(table);
        cp.add(sp, BorderLayout.CENTER);
        cp.add(p, BorderLayout.SOUTH);

        dlg.setSize(400, 600);
        Point center = WflUtil.getCenter(dlg.getSize());
        dlg.setLocation(center);
        dlg.setModal(true);
        dlg.setVisible(true);

        return selected;
    }

    private static void accept() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0)
            return ;

        Object clase = model.getValueAt(selectedRow, 0);
        Object path = model.getValueAt(selectedRow, 1);
        selected = path + "." + clase;
        dlg.setVisible(false);
    }

    private static void cancel() {
        selected = null;
        dlg.setVisible(false);
    }

}

class WflSelectClassTableModel extends AbstractTableModel{
    private static final long serialVersionUID = 1L;
    java.util.List<java.util.List<String> > data = new ArrayList<java.util.List<String>>();
    
    public WflSelectClassTableModel(ZipFile zf) {
        init(zf);
    }

    private void init(ZipFile zf) {
        data = new ArrayList<java.util.List<String>>();
        Enumeration en = zf.entries();
        String sep = "/";
        while (en.hasMoreElements()) {
            String o = en.nextElement().toString();
            if (o.endsWith(".class")){
                o = o.substring(0, o.length() - 6);
                int i = o.lastIndexOf(sep);
                String p = o.substring(0,i);
                p = p.replaceAll(sep, "\\.");
                String c = o.substring(i+1);
                java.util.List<String> v = new ArrayList<String>(2);
                v.add(c);
                v.add(p);
                data.add(v);
            }
        }
    }

    public String getColumnName(int column) {
        if (column == 0)
            return "Clase";
        else
            return "Path";
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        java.util.List row = (java.util.List)data.get(rowIndex);
        return row.get(columnIndex);
    }
}