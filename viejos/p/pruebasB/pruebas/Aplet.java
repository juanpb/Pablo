package p.pruebas;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

public class Aplet extends Applet {
    private static final boolean _imprimir = true;

    private static final byte LIBRE  =  0;
    private static final byte OCUPADO = 1;
    private static final byte NULL   = 2;
    private static final byte PRIMER_CLIC    = 3;
    private static final byte SEGUNDO_CLIC   = 4;

    private byte _clic = PRIMER_CLIC; // indica el clic que está esperando
    private int _fila = 0;
    private int _col = 0;
    private byte[][] _estado = new byte[7][7];
    private Boton _botonOrigen = null;
    private Boton _botonDestino = null;

    private int _libres = 1;
    private int _ocupados = 32;

    private boolean isStandalone = false;
    private Boton _boton02 = new Boton(Boton.OCUPADO);
    private Boton _boton03 = new Boton(Boton.OCUPADO);
    private Boton _boton04 = new Boton(Boton.OCUPADO);
    private Boton _boton12 = new Boton(Boton.OCUPADO);
    private Boton _boton13 = new Boton(Boton.OCUPADO);
    private Boton _boton14 = new Boton(Boton.OCUPADO);
    private Boton _boton20 = new Boton(Boton.OCUPADO);
    private Boton _boton21 = new Boton(Boton.OCUPADO);
    private Boton _boton22 = new Boton(Boton.OCUPADO);
    private Boton _boton23 = new Boton(Boton.OCUPADO);
    private Boton _boton24 = new Boton(Boton.OCUPADO);
    private Boton _boton25 = new Boton(Boton.OCUPADO);
    private Boton _boton26 = new Boton(Boton.OCUPADO);
    private Boton _boton30 = new Boton(Boton.OCUPADO);
    private Boton _boton31 = new Boton(Boton.OCUPADO);
    private Boton _boton32 = new Boton(Boton.OCUPADO);
    private Boton _boton33 = new Boton(Boton.LIBRE);
    private Boton _boton34 = new Boton(Boton.OCUPADO);
    private Boton _boton35 = new Boton(Boton.OCUPADO);
    private Boton _boton36 = new Boton(Boton.OCUPADO);
    private Boton _boton40 = new Boton(Boton.OCUPADO);
    private Boton _boton41 = new Boton(Boton.OCUPADO);
    private Boton _boton42 = new Boton(Boton.OCUPADO);
    private Boton _boton43 = new Boton(Boton.OCUPADO);
    private Boton _boton44 = new Boton(Boton.OCUPADO);
    private Boton _boton45 = new Boton(Boton.OCUPADO);
    private Boton _boton46 = new Boton(Boton.OCUPADO);
    private Boton _boton52 = new Boton(Boton.OCUPADO);
    private Boton _boton53 = new Boton(Boton.OCUPADO);
    private Boton _boton54 = new Boton(Boton.OCUPADO);
    private Boton _boton62 = new Boton(Boton.OCUPADO);
    private Boton _boton63 = new Boton(Boton.OCUPADO);
    private Boton _boton64 = new Boton(Boton.OCUPADO);
    JLabel _lblLibres = new JLabel();
    JLabel _lblOcupados = new JLabel();
    JTextField _txtLibres = new JTextField();
    JTextField _txtOcupados = new JTextField();
    JButton _btmIniciar = new JButton();


    //Get a parameter value
    public String getParameter(String key, String def) {
        return isStandalone ? System.getProperty(key, def) :
            (getParameter(key) != null ? getParameter(key) : def);
    }

    //Construct the applet
    public Aplet() {
    }

    //Initialize the applet
    public void init() {
        try {
            jbInit();
            iniciarEstados();
            listener();
            _txtLibres.setText(Integer.toString(_libres));
            _txtOcupados.setText(Integer.toString(_ocupados));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        this.setLayout(null);

        _boton64.setBounds(new Rectangle(224, 373, 50, 50));
        _boton63.setBounds(new Rectangle(174, 373, 50, 50));
        _boton62.setBounds(new Rectangle(124, 373, 50, 50));
        _boton54.setBounds(new Rectangle(224, 323, 50, 50));
        _boton53.setBounds(new Rectangle(174, 323, 50, 50));
        _boton52.setBounds(new Rectangle(124, 323, 50, 50));
        _boton46.setBounds(new Rectangle(324, 273, 50, 50));
        _boton45.setBounds(new Rectangle(274, 273, 50, 50));
        _boton44.setBounds(new Rectangle(224, 273, 50, 50));
        _boton43.setBounds(new Rectangle(174, 273, 50, 50));
        _boton42.setBounds(new Rectangle(124, 273, 50, 50));
        _boton41.setBounds(new Rectangle(74, 273, 50, 50));
        _boton40.setBounds(new Rectangle(24, 273, 50, 50));
        _boton36.setBounds(new Rectangle(324, 223, 50, 50));
        _boton35.setBounds(new Rectangle(274, 223, 50, 50));
        _boton34.setBounds(new Rectangle(224, 223, 50, 50));
        _boton33.setBounds(new Rectangle(174, 223, 50, 50));
        _boton32.setBounds(new Rectangle(124, 223, 50, 50));
        _boton31.setBounds(new Rectangle(74, 223, 50, 50));
        _boton30.setBounds(new Rectangle(24, 223, 50, 50));
        _boton26.setBounds(new Rectangle(324, 173, 50, 50));
        _boton25.setBounds(new Rectangle(274, 173, 50, 50));
        _boton24.setBounds(new Rectangle(224, 173, 50, 50));
        _boton23.setBounds(new Rectangle(174, 173, 50, 50));
        _boton22.setBounds(new Rectangle(124, 173, 50, 50));
        _boton21.setBounds(new Rectangle(74, 173, 50, 50));
        _boton20.setBounds(new Rectangle(24, 173, 50, 50));
        _boton14.setBounds(new Rectangle(224, 123, 50, 50));
        _boton13.setBounds(new Rectangle(174, 123, 50, 50));
        _boton12.setBounds(new Rectangle(124, 123, 50, 50));
        _boton04.setBounds(new Rectangle(224, 73, 50, 50));
        _boton03.setBounds(new Rectangle(174, 73, 50, 50));
        _boton02.setBounds(new Rectangle(124, 73, 50, 50));
        _lblLibres.setText("Libres:");
        _lblLibres.setBounds(new Rectangle(10, 20, 60, 21));
        _lblOcupados.setBounds(new Rectangle(10, 47, 70, 19));
        _lblOcupados.setText("Ocupados:");
        _txtLibres.setBounds(new Rectangle(80, 20, 32, 21));
        _txtOcupados.setBounds(new Rectangle(80, 46, 32, 21));
        _btmIniciar.setText("Iniciar");
        _btmIniciar.setBounds(new Rectangle(294, 16, 101, 24));
        _btmIniciar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                _btmIniciar_actionPerformed(e);
            }
        });
        this.add(_boton02, null);
        this.add(_boton03, null);
        this.add(_boton12, null);
        this.add(_boton13, null);
        this.add(_boton22, null);
        this.add(_boton23, null);
        this.add(_boton25, null);
        this.add(_boton26, null);
        this.add(_boton32, null);
        this.add(_boton33, null);
        this.add(_boton35, null);
        this.add(_boton36, null);
        this.add(_boton42, null);
        this.add(_boton43, null);
        this.add(_boton45, null);
        this.add(_boton46, null);
        this.add(_boton52, null);
        this.add(_boton53, null);
        this.add(_boton62, null);
        this.add(_boton63, null);
        this.add(_boton20, null);
        this.add(_boton21, null);
        this.add(_boton30, null);
        this.add(_boton31, null);
        this.add(_boton40, null);
        this.add(_boton41, null);
        this.add(_boton14, null);
        this.add(_boton04, null);
        this.add(_boton24, null);
        this.add(_boton34, null);
        this.add(_boton44, null);
        this.add(_boton54, null);
        this.add(_boton64, null);
        this.add(_lblLibres, null);
        this.add(_txtLibres, null);
        this.add(_lblOcupados, null);
        this.add(_txtOcupados, null);
        this.add(_btmIniciar, null);
    }
    //Start the applet
    public void start() {
    }
    //Stop the applet
    public void stop() {
    }
    //Destroy the applet
    public void destroy() {
    }
    //Get Applet information
    public String getAppletInfo() {
        return "Applet Information";
    }
    //Get parameter info
    public String[][] getParameterInfo() {
        return null;
    }
    //Main method
    public static void main(String[] args) {
        Aplet applet = new Aplet();
        applet.isStandalone = true;
        Frame frame;
        frame = new Frame() {
            protected void processWindowEvent(WindowEvent e) {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                    System.exit(0);
                }
            }
            public synchronized void setTitle(String title) {
                super.setTitle(title);
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            }
        };
        frame.setTitle("Applet Frame");
        frame.add(applet, BorderLayout.CENTER);
        applet.init();
        applet.start();
        frame.setSize(450,500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }

/******************************************************************************/
/*          			      Métodos privados                                */
/******************************************************************************/

    private void iniciarEstados(){
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                _estado[i][j] = OCUPADO;
            }
        }
        _estado[3][3] = LIBRE;

        _estado[0][0] = NULL;
        _estado[0][1] = NULL;
        _estado[1][0] = NULL;
        _estado[1][1] = NULL;

        _estado[0][5] = NULL;
        _estado[0][6] = NULL;
        _estado[1][5] = NULL;
        _estado[1][6] = NULL;

        _estado[5][0] = NULL;
        _estado[5][1] = NULL;
        _estado[6][0] = NULL;
        _estado[6][1] = NULL;

        _estado[5][5] = NULL;
        _estado[5][6] = NULL;
        _estado[6][5] = NULL;
        _estado[6][6] = NULL;
    }

    private void listener(){
        _boton02.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(0 , 2, e);
            }
        });
        _boton03.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(0 ,3 , e);
            }
        });
        _boton04.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(0 ,4 , e);
            }
        });
        _boton12.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(1 ,2 , e);
            }
        });
        _boton13.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(1 ,3 , e);
            }
        });
        _boton14.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(1 ,4 , e);
            }
        });
        _boton20.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2 ,0 , e);
            }
        });
        _boton21.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2 ,1 , e);
            }
        });
        _boton22.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2, 2, e);
            }
        });
        _boton23.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2, 3, e);
            }
        });
        _boton24.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2, 4, e);
            }
        });
        _boton25.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2, 5, e);
            }
        });
        _boton26.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(2, 6, e);
            }
        });
        _boton30.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 0, e);
            }
        });
        _boton31.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 1, e);
            }
        });
        _boton32.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 2, e);
            }
        });
        _boton33.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 3, e);
            }
        });
        _boton34.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 4, e);
            }
        });
        _boton35.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 5, e);
            }
        });
        _boton36.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(3, 6, e);
            }
        });
        _boton40.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 0, e);
            }
        });
        _boton41.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 1, e);
            }
        });
        _boton42.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 2, e);
            }
        });
        _boton43.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 3, e);
            }
        });
        _boton44.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 4, e);
            }
        });
        _boton45.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 5, e);
            }
        });
        _boton46.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(4, 6, e);
            }
        });
        _boton52.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(5, 2, e);
            }
        });
        _boton53.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(5, 3, e);
            }
        });
        _boton54.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(5, 4, e);
            }
        });
        _boton62.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(6, 2, e);
            }
        });
        _boton63.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(6, 3, e);
            }
        });
        _boton64.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                clic(6, 4, e);
            }
        });
    }

    private void clic(int pFila, int pCol, ActionEvent e){
        Boton source = (Boton) e.getSource();

        if (_clic == PRIMER_CLIC){
            if (_estado[pFila][pCol] == LIBRE){
                print("Primer Clic. Libre el " + pFila + " " + pCol);
                return;
            }
            _clic = SEGUNDO_CLIC;
            _fila = pFila;
            _col = pCol;
            _botonOrigen = source;
            _botonOrigen.setEstado(Boton.SELECCIONADO);
        }
        else if (_clic == SEGUNDO_CLIC){
            int filaDM = 0;
            int colDM = 0;
            //Si se cliqueo dos veces sobre el mismo => lo desselecciono
            if (_botonOrigen.equals(source)){
                _botonOrigen.setEstado(Boton.OCUPADO);
                _clic = PRIMER_CLIC;
                return;
            }

            if (_estado[pFila][pCol] == OCUPADO){
                print("Segundo Clic. Ocupado el " + pFila + " " + pCol);
                return;
            }
            //caso 1: de izq a der
            //caso 2: de der a izq
            if (_fila == pFila){
                if (_col == pCol - 2){//caso 1
                    filaDM = _fila;
                    colDM = _col + 1;
                }else if (_col == pCol + 2){//caso 2
                    filaDM = _fila;
                    colDM = _col - 1;
                }
                else
                    return;
            }
            //caso 3: de arriba a abajo
            //caso 4: de abajo a arriba
            else if (_col == pCol){
                if (_fila == pFila - 2){//caso 3
                    filaDM = _fila + 1;
                    colDM = _col ;
                }else if (_fila == pFila + 2){//caso 4
                    filaDM = _fila - 1;
                    colDM = _col ;
                }
                else
                    return;
            }
            else
                return;
            if (_estado[filaDM][colDM] == LIBRE){
                print("El del medio está libre");
                return;
            }

            //jugada correcta
            _estado[_fila][_col] = LIBRE; //pongo en LIBRE el origen
            _estado[pFila][pCol] = OCUPADO; //pongo en OCUPADO el destino
            _estado[filaDM][colDM] = LIBRE;

            _botonDestino = source;
            _botonOrigen.setEstado(Boton.LIBRE);
            _botonDestino.setEstado(Boton.OCUPADO);
            getBoton(filaDM, colDM).setEstado(Boton.LIBRE);

            _ocupados--;
            _libres++;
            _txtLibres.setText(Integer.toString(_libres));
            _txtOcupados.setText(Integer.toString(_ocupados));

            _clic = PRIMER_CLIC;
        }
    }

    private Boton getBoton(int f, int c){System.out.println("____" + f + c);
        switch(f)
        {
            case 0:{
                switch(c){
                    case 2:{
                        return _boton02;
                    }
                    case 3:{
                        return _boton03;
                    }
                    case 4:{
                        return _boton04;
                    }
                }
                break;
            }
            case 1:{
                switch(c){
                    case 2:{
                        return _boton12;
                    }
                    case 3:{
                        return _boton13;
                    }
                    case 4:{
                        return _boton14;
                    }
                }
                break;
            }
            case 2:{
                switch(c){
                    case 0:{
                        return _boton20;
                    }
                    case 1:{
                        return _boton21;
                    }
                    case 2:{
                        return _boton22;
                    }
                    case 3:{
                        return _boton23;
                    }
                    case 4:{
                        return _boton24;
                    }
                    case 5:{
                        return _boton25;
                    }
                    case 6:{
                        return _boton26;
                    }
                }
                break;
            }
            case 3:{
                switch(c){
                    case 0:{
                        return _boton30;
                    }
                    case 1:{
                        return _boton31;
                    }
                    case 2:{
                        return _boton32;
                    }
                    case 3:{
                        return _boton33;
                    }
                    case 4:{
                        return _boton34;
                    }
                    case 5:{
                        return _boton35;
                    }
                    case 6:{
                        return _boton36;
                    }
                }
                break;
            }
            case 4:{
                switch(c){
                    case 0:{
                        return _boton40;
                    }
                    case 1:{
                        return _boton41;
                    }
                    case 2:{
                        return _boton42;
                    }
                    case 3:{
                        return _boton43;
                    }
                    case 4:{
                        return _boton44;
                    }
                    case 5:{
                        return _boton45;
                    }
                    case 6:{
                        return _boton46;
                    }
                }
                break;
            }
            case 5:{
                switch(c){
                    case 2:{
                        return _boton52;
                    }
                    case 3:{
                        return _boton53;
                    }
                    case 4:{
                        return _boton54;
                    }
                }
                break;
            }
            case 6:{
                switch(c){
                    case 2:{
                        return _boton62;
                        }
                    case 3:{
                        return _boton63;
                        }
                    case 4:{
                        return _boton64;
                    }
                }
                break;
            }
        }
        print("todo mal");
        return null;
    }

    private void print(String x)
    {
        if (_imprimir)
            System.out.println("): " + x);
    }

    private void print(Exception x)
    {
        if (_imprimir)
            x.printStackTrace();
    }

    void _btmIniciar_actionPerformed(ActionEvent e)
    {
        iniciarEstados();
        _clic = PRIMER_CLIC;
        _libres = 1;
        _ocupados = 32;
        _txtLibres.setText(Integer.toString(_libres));
        _txtOcupados.setText(Integer.toString(_ocupados));

        _boton02.setEstado(Boton.OCUPADO);
        _boton03.setEstado(Boton.OCUPADO);
        _boton04.setEstado(Boton.OCUPADO);
        _boton12.setEstado(Boton.OCUPADO);
        _boton13.setEstado(Boton.OCUPADO);
        _boton14.setEstado(Boton.OCUPADO);
        _boton20.setEstado(Boton.OCUPADO);
        _boton21.setEstado(Boton.OCUPADO);
        _boton22.setEstado(Boton.OCUPADO);
        _boton23.setEstado(Boton.OCUPADO);;
        _boton24.setEstado(Boton.OCUPADO);
        _boton25.setEstado(Boton.OCUPADO);
        _boton26.setEstado(Boton.OCUPADO);
        _boton30.setEstado(Boton.OCUPADO);
        _boton31.setEstado(Boton.OCUPADO);
        _boton32.setEstado(Boton.OCUPADO);
        _boton33.setEstado(Boton.LIBRE);
        _boton34.setEstado(Boton.OCUPADO);
        _boton35.setEstado(Boton.OCUPADO);
        _boton36.setEstado(Boton.OCUPADO);
        _boton40.setEstado(Boton.OCUPADO);
        _boton41.setEstado(Boton.OCUPADO);
        _boton42.setEstado(Boton.OCUPADO);
        _boton43.setEstado(Boton.OCUPADO);
        _boton44.setEstado(Boton.OCUPADO);
        _boton45.setEstado(Boton.OCUPADO);
        _boton46.setEstado(Boton.OCUPADO);
        _boton52.setEstado(Boton.OCUPADO);
        _boton53.setEstado(Boton.OCUPADO);
        _boton54.setEstado(Boton.OCUPADO);
        _boton62.setEstado(Boton.OCUPADO);
        _boton63.setEstado(Boton.OCUPADO);
        _boton64.setEstado(Boton.OCUPADO);
    }
}