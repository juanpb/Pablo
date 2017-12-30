package p.aplic;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class Prueba {
    private static final Logger logger = Logger.getLogger(Prueba.class);

    JFrame  _frame = new JFrame();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JTextArea jTextArea1 = new JTextArea();
    JTextArea jTextArea2 = new JTextArea();
    JTextArea jTextArea3 = new JTextArea();
    JTextArea jTextArea4 = new JTextArea();
    JTextArea jTextArea5 = new JTextArea();
    JTextField textField = new JTextField();
    JEditorPane editorPane  =  new JEditorPane();
    JPanel panel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();

    public Prueba() {
        try {
            jbInit();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    public static void main(String[] args) {
        String path = "F:\\Archivos de programa\\KaZaA\\a3\\t";
        File file = new File(path);
        File[] files = file.listFiles(/*new p.util.DirFileFilter(false, "mpg")*/);
        for (int i = 0; i < files.length; i++){
            String x = files[i].getName();
            int h = x.indexOf(".mpg   ");
            File f = files[i];

            if (h + 5 < x.length()){
                String nuevo = x.substring(0, h + 4);
                File newFile = new File(path + "\\" + nuevo );
                if (newFile.exists()){
                    System.out.println("555555555555555555555555555555");
                }
                else{
                    f.renameTo(newFile);
                    System.out.println(newFile.getAbsolutePath());
                }
            }

        }



    }
    private void jbInit() throws Exception {
        _frame.getContentPane().setLayout(gridBagLayout2);
        jLabel1.setText("jLabel1");
        int fTxt = 1;
        int insBot = 10;
        panel.setMinimumSize(new Dimension(40, 80));
        panel.setForeground(Color.red);
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.red));
        _frame.getContentPane().add(panel,
            new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                   GridBagConstraints.BOTH, new Insets(10, 30, 10, 30), 0, 50));
        _frame.getContentPane().add(jLabel1,  new GridBagConstraints(0, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(200, 10, insBot, 10), 0, 0));

        _frame.getContentPane().add(jTextArea1,   new GridBagConstraints(1, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(200, 10, insBot, 10), 34, 7));

        _frame.getContentPane().add(textField,   new GridBagConstraints(2, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(200, 10, insBot, 10), 34, 7));

        _frame.getContentPane().add(jTextArea3,   new GridBagConstraints(3, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(200, 10, insBot, 10), 34, 7));

        _frame.getContentPane().add(jTextArea4,   new GridBagConstraints(4, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(200, 10, insBot, 10), 34, 7));

        _frame.getContentPane().add(jTextArea5,   new GridBagConstraints(5, fTxt, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(200, 10, insBot, 10), 34, 7));

        _frame.pack();
        _frame.setVisible(true);
    }
}
