package p.pruebas.thumb;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


/**
 * User: JPB
 * Date: Feb 22, 2010
 * Time: 11:21:14 PM
 */
public class PrincipalFrame {
    private String titulo = "X";
    final java.util.List<String> list = Arrays.asList(
            "D:\\P\\Fotos\\mdp\\_n\\2014-01-03_13 Juliana.jpg"
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_02_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_03_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_04_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_05_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_06_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_07_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_08_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_09_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_10_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_11_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_12_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_13_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_14_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_15_Bodega Bianchi.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_16_Villa 25 de Mayo.jpg",
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_18_Dique Reyunos.jpg"   ,
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_19_Dique Reyunos.jpg"    ,
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_20_Dique Reyunos.jpg"     ,
//            "D:\\P\\p2\\Fotos\\2008\\Luna de miel\\2008-12-09_21_Dique Reyunos.jpg"
        );

    public PrincipalFrame() {
        initGUI();
    }


    private void initGUI(){
        JFrame frame = new JFrame(titulo);

        frame.setLayout(new BorderLayout());
//        addListeners();
//        valoresIniciales();
//        addComponentes();
//        setPropiedades();
        final PanelThumb pnl = new PanelThumb(frame);

        long ini = System.currentTimeMillis();

        pnl.setFiles(list);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");

        frame.add(pnl);
        frame.setSize(700,700);
        p.util.GUI.centrar(frame);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
}
