package p.aplic.verfotos;

import p.gui.LabelTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: JPB
 * Date: Sep 17, 2009
 * Time: 2:58:13 PM
 */
public class ConfigEditor extends JDialog{
    private JCheckBox chkRepetir = new JCheckBox("Repetir"); 
    private JCheckBox chkRecursivo = new JCheckBox("Recursivo"); 
    private JCheckBox chkAleatorio = new JCheckBox("Aleatorio");
    private JCheckBox chkSlideShow = new JCheckBox("Slide Show");
    private JCheckBox chkTipoFiltroOR = new JCheckBox("Filtrar con OR");
//    private LabelTextField txtDirectorios = new LabelTextField("Directorios") ;
    private LabelTextField txtDirectorios = new LabelTextField("Directorios") ;
    private LabelTextField txtDirectoriosSinFiltar = new LabelTextField("Directorios sin filtrar") ;
    private LabelTextField txtTiempoPorFoto = new LabelTextField("Tiempo Por Foto") ;
    private LabelTextField txtExtensiones = new LabelTextField("Extensiones") ;
    private LabelTextField txtFiltroNombre = new LabelTextField("Filtro nombres") ;

    private JButton btnCancelar = new JButton("Cancelar") ;
    private JButton btnAceptar = new JButton("Aceptar") ;

    private  Config config;
    private  boolean seCanceló = false;

    public ConfigEditor(Config conf) {
        config = conf;
        initGUI();
    }

    private void initGUI(){
        setLayout(new GridLayout(0,1));

        add(txtDirectorios);
        add(txtFiltroNombre);
        add(txtDirectoriosSinFiltar);
        add(txtExtensiones);
        add(chkRecursivo)   ;
        add(chkRepetir)    ;
        add(chkAleatorio)    ;
        add(chkSlideShow)    ;
        add(chkTipoFiltroOR)    ;
        add(txtTiempoPorFoto)    ;
        add(btnAceptar)    ;
        add(btnCancelar)    ;

        setPreferredSize(new Dimension(350, 450));
        pack();

        addListeners();
        objetoAPantalla();
//        setVisible(true);
    }

    private void cerrar(){
        setVisible(false);
        dispose();
    }
    
    private void addListeners(){
        final ConfigEditor dis = this;
        btnCancelar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            seCanceló = true;
            cerrar();
            }
        });

        btnAceptar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            pantallaAObjeto();
            cerrar();
            }
        });
    }


    private String list2String(java.util.List<String> list){
        StringBuffer res = new StringBuffer();

        if (list != null){
            for(String s : list) {
                res.append(s).append(";");
            }
        }
        return res.toString();
    }

    private void objetoAPantalla(){
        txtDirectorios.setText(list2String(config.getDirectorios()));
        txtDirectoriosSinFiltar.setText(list2String(config.getDirectoriosSinFiltrar()));
        txtExtensiones.setText(list2String(config.getExtensiones()));
        txtFiltroNombre.setText(list2String(config.getFiltroFotos()));
        txtTiempoPorFoto.setText(config.getTiempoPorFoto()+"");
        chkRecursivo.setSelected(config.isRecursivo());
        chkAleatorio.setSelected(config.isMostrarMezclado());
        chkRepetir.setSelected(config.isRepetir());
        chkSlideShow.setSelected(config.isArrancarSlideShow());
        chkTipoFiltroOR.setSelected(config.isTipoFiltroOr());
    }

    private void pantallaAObjeto(){
        config.setDirectorios(txtDirectorios.getText());
        config.setDirectoriosSinFiltrar(txtDirectoriosSinFiltar.getText());
        config.setExtensiones(txtExtensiones.getText());
        config.setFiltroFotos(txtFiltroNombre.getText());
        config.setTiempoPorFoto(txtTiempoPorFoto.getText());
        config.setRecursivo(chkRecursivo.isSelected());
        config.setRepetir(chkRepetir.isSelected());
        config.setMostrarMezclado(chkAleatorio.isSelected());
        config.setArrancarSlideShow(chkSlideShow.isSelected());
        config.setTipoFiltroOr(chkTipoFiltroOR.isSelected());
    }

    public boolean isSeCanceló() {
        return seCanceló;
    }
}

