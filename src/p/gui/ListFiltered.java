package p.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * User: JPB
 * Date: Apr 17, 2008
 * Time: 1:05:55 PM
 */
public class ListFiltered  extends JPanel {
    private JLabel lbl = new JLabel();
    private TextField txt = new TextField();
    private JList list = new JList();
    private JScrollPane scrollPane = new JScrollPane(list,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    private DefaultListModel modeloSinFiltrar;
    private boolean keySensitive;

    private Object objetoSeleccionado;
    private java.util.List<ChangeListener> changeEventListeners =
            new ArrayList<ChangeListener>();
    private java.util.List<ChangeListener> dobleClicEventListeners =
            new ArrayList<ChangeListener>();

    private static final Color BACKGROUND_SELECCIONADO = Color.RED;
    private static final Color BACKGROUND_SIN_SELECCION = Color.white;

    public ListFiltered() {
        this(null);
    }

    public ListFiltered(String lblText, Dimension d) {
        init(lblText);
        setSize(d);
    }

    public ListFiltered(String lblText) {
        this(lblText, false);
    }
    public ListFiltered(String lblText, boolean keySensitive) {
        this.keySensitive = keySensitive;
        init(lblText);
    }

    public String getText(){
        return txt.getText();
    }

    public void setText(String s) {
        txt.setText(s);
        filtrarModelo(s);
    }

    private void init(String lblText) {
        setLayout(new GridBagLayout());
        Insets ins1 = new Insets(0, 0, 0, 0);

        GridBagConstraints bc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ins1, 0, 0);
        add(lbl, bc);
        bc.gridy = 1;
        add(txt, bc);
        bc.gridy = 2;
        bc.weighty = 1.1;
        add(scrollPane, bc);

        if (lblText != null)
            lbl.setText(lblText);

        addListeners();
    }

    private void addListeners() {
        //uso txt.addKeyListener(new KeyAdapter(){...
        //en vez de txt.getDocument().addDocumentListener (new DocumentListener(){....
        //para evitar que cuando modifico el txt desde código no me vuelva a
        //generar un evento.
        //Desventajas: si pego texto con el menú contextual => no se filtra


        txt.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                String s = txt.getText(); //tiene el texto antes del evento
                filtrarModelo(s);
            }
        });

//        txt.getDocument().addDocumentListener (new DocumentListener(){
//            private long ult = 0;
//            public void changedUpdate(DocumentEvent e) {}
//
//            public void insertUpdate(DocumentEvent e) {
//                long x = System.currentTimeMillis();
//                if (x-ult > 50){
//                    ult = x;
//                    filtrarModelo(txt.getText());
//                }
//            }
//
//            public void removeUpdate(DocumentEvent e) {
//                long x = System.currentTimeMillis();
//                if (x-ult > 50){
//                    ult = x;
//                    filtrarModelo(txt.getText());
//                }
//            }
//        });
//
        list.addKeyListener(new KeyAdapter(){
             public void keyReleased(KeyEvent e) {
                 if (e.getKeyCode() == KeyEvent.VK_ENTER)
                     dobleClic();
             }
        });

        list.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                Object o = list.getSelectedValue();
                if (o != null)
                    seleccionarObjeto(o);                
            }

        });

        list.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    dobleClic();
            }
        });
    }

    private void dobleClic(){
        if (objetoSeleccionado != null){
            ChangeEvent e = new ChangeEvent(this);
            for (ChangeListener listener : dobleClicEventListeners) {
                listener.stateChanged(e);
            }
        }
    }

    private void seleccionarObjeto(Object o){
        objetoSeleccionado = o;
        if (o == null){
            txt.setBackground(BACKGROUND_SIN_SELECCION);
            return ;// ojo: no avisa cuando se limpia...
        }
        txt.setBackground(BACKGROUND_SELECCIONADO);
        txt.setText(o.toString());


        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : changeEventListeners) {
            listener.stateChanged(e);
        }
    }

    /**
     * Se dispara con el enter o el doble clic.
     * @param c
     */
    public void addDobleClicListener(ChangeListener c){
        dobleClicEventListeners.add(c);
    }

    public void addChangeListener(ChangeListener c){
        changeEventListeners.add(c);
    }

    private void filtrarModelo(String t){
        DefaultListModel modeloFiltrado = new DefaultListModel();
        if (t == null || t.equals("")){
            modeloFiltrado = modeloSinFiltrar;
        }
        else{
            String t2 = t;
            if (isKeySensitive())
                t2 = t.toLowerCase();

            int tam = modeloSinFiltrar.getSize();
            for (int i = 0; i < tam; i++) {
                String s = modeloSinFiltrar.getElementAt(i).toString();
                if (isKeySensitive())
                    s = s.toLowerCase();
                if (s.contains(t2))
                    modeloFiltrado.addElement(modeloSinFiltrar.getElementAt(i));
            }
        }
        list.setModel(modeloFiltrado);

        //si queda uno solo => lo cargo en el txt
        if (modeloFiltrado.size() == 1){
            //salvo que ya sea el mismo...
            final Object sel = modeloFiltrado.get(0);
            if (objetoSeleccionado == null
                    || (t != null
                        && !objetoSeleccionado.equals(sel))){
                EventQueue.invokeLater(new Runnable(){
                    public void run() {
                        seleccionarObjeto(sel);
                    }
                });
            }
        }else{
            seleccionarObjeto(null);
        }
    }



    /**************************************************************************/
    /*          delegate methods        */
    /**************************************************************************/


    public Object getSelectedValue() {
        return objetoSeleccionado;
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public Object[] getSelectedValues() {
        return list.getSelectedValuesList().toArray();
    }

    public void setSelectedIndices(int[] indices) {
        list.setSelectedIndices(indices);
    }

    public void setSelectedIndex(int index) {
        list.setSelectedIndex(index);
    }

    public int[] getSelectedIndices() {
        return list.getSelectedIndices();
    }

    public void setSelectionInterval(int anchor, int lead) {
        list.setSelectionInterval(anchor, lead);
    }

    public void addSelectionInterval(int anchor, int lead) {
        list.addSelectionInterval(anchor, lead);
    }

    public void clearSelection() {
        list.clearSelection();
    }

    public void setSelectionMode(int selectionMode) {
        list.setSelectionMode(selectionMode);
    }

    public void setSelectionModel(ListSelectionModel selectionModel) {
        list.setSelectionModel(selectionModel);
    }

    public ListModel getModel() {
        return list.getModel();
    }

    public void setModel(java.util.List model) {
        DefaultListModel listModel = new DefaultListModel();
        for (Object o : model) {
            listModel.addElement(o);
        }
        setModel(listModel);
    }

    public void setModel(DefaultListModel model) {
        modeloSinFiltrar = model;
        list.setModel(model);
    }


    public boolean isKeySensitive() {
        return keySensitive;
    }

    public void setKeySensitive(boolean keySensitive) {
        this.keySensitive = keySensitive;
    }

    
}
