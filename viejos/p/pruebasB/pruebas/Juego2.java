package p.pruebas;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;

public class Juego2 extends Applet {
    private static final boolean _imprimir = true;

    private static final byte LIBRE  =  0;
    private static final byte OCUPADO = 1;
    private static final byte NULL   = 2;
    private static final byte PRIMER_CLIC    = 3;
    private static final byte SEGUNDO_CLIC   = 4;

    public static final int CANT_FILAS = 9;
    private static final int CANT_OCUPADOS_INICIAL = 44;

    private byte _clic = PRIMER_CLIC; // indica el clic que está esperando
    private int _fila = 0;
    private int _col = 0;

    private Boton[][] _botones = new Boton[CANT_FILAS][CANT_FILAS];
    private byte[][] _estado = new byte[CANT_FILAS][CANT_FILAS];

    private Boton _botonOrigen = null;
    private Boton _botonDestino = null;
    private JPanel _panel = new JPanel();

    private int _libres = 1;
    private int _ocupados = CANT_OCUPADOS_INICIAL;
    private Stack _Jugada2s = new Stack();

    private boolean isStandalone = false;

    JLabel _lblLibres = new JLabel();
    JLabel _lblOcupados = new JLabel();
    JTextField _txtLibres = new JTextField();
    JTextField _txtOcupados = new JTextField();
    JButton _btmIniciar = new JButton();
  JButton _btnDeshacer = new JButton();


    //Get a parameter value
    public String getParameter(String key, String def) {
        return isStandalone ? System.getProperty(key, def) :
            (getParameter(key) != null ? getParameter(key) : def);
    }

    //Construct the applet
    public Juego2() {
    }

    //Initialize the applet
    public void init() {
        try {
            jbInit();
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

        armarPanel();
        _panel.setBounds(15, 70, 500, 500);


        _lblLibres.setText("Libres:");
        _lblLibres.setBounds(new Rectangle(10, 20, 60, 21));
        _lblOcupados.setBounds(new Rectangle(10, 47, 70, 19));
        _lblOcupados.setText("Ocupados:");
        _txtLibres.setBounds(new Rectangle(80, 20, 32, 21));
        _txtOcupados.setBounds(new Rectangle(80, 46, 32, 21));
        _btmIniciar.setText("Iniciar");
        _btmIniciar.setBounds(new Rectangle(294, 16, 91, 24));
        _btmIniciar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                iniciar(e);
            }
        });
        _btnDeshacer.setBounds(new Rectangle(294, 46, 91, 24));
        _btnDeshacer.setText("Deshacer");
        _btnDeshacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deshacer(e);
            }
        });
        this.add(_lblLibres, null);
        this.add(_txtLibres, null);
        this.add(_lblOcupados, null);
        this.add(_txtOcupados, null);
        this.add(_btmIniciar, null);
        this.add(_btnDeshacer, null);
        this.add(_panel);
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
        Juego2 applet = new Juego2();

        applet.buscarSolucion();
        if (true) return;

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
        frame.setSize(530,600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2,
                          (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }

/******************************************************************************/
/*          			      Métodos privados                                */
/******************************************************************************/

    private void armarPanel(){
        _panel.setLayout(new GridLayout(0, 9));
        for ( int i = 0; i < CANT_FILAS; i++) {
            for (int j = 0; j < CANT_FILAS; j++) {
                Boton b = new Boton();
                b.setSize(50, 50);
                _panel.add(b);
                final int f  = i;
                final int c  = j;
                b.addActionListener(new java.awt.event.ActionListener(){
                            public void actionPerformed(ActionEvent e){
                                clic(f, c, e);
                            }
                        });
                _botones[f][c] = b;
            }
        }
        setEstadoInicial();
    }

    private void setEstadoInicial(){
        _Jugada2s = new Stack();

        for (int i = 0; i < CANT_FILAS; i++) {
            for (int j = 0; j < CANT_FILAS; j++) {
                _botones[i][j].setEstado(Boton.OCUPADO);
            }
        }
        _botones[4][4].setEstado(Boton.LIBRE);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _botones[i][j].setEstado(Boton.NULL);//arriba izq
                _botones[i][j + 6].setEstado(Boton.NULL);//arriba der
                _botones[i + 6][j].setEstado(Boton.NULL);//abajo izq
                _botones[i + 6][j + 6].setEstado(Boton.NULL);//abajo der
            }
        }
    }

    private void clic(int pFila, int pCol, ActionEvent e){
        Boton source = (Boton) e.getSource();

        if (_clic == PRIMER_CLIC){
            if (_botones[pFila][pCol].getEstado() == Boton.LIBRE){
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

            if (_botones [pFila][pCol].getEstado() == Boton.OCUPADO){
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
            if (_botones [filaDM][colDM].getEstado() == Boton.LIBRE){
                print("El del medio está libre");
                return;
            }

            //Jugada2 correcta
            _botones[_fila][_col].setEstado(Boton.LIBRE);  //pongo en LIBRE el origen
            _botones[pFila][pCol].setEstado(Boton.OCUPADO);//pongo en OCUPADO el destino
            _botones[filaDM][colDM].setEstado(Boton.LIBRE);

            _botonDestino = source;
            _botonOrigen.setEstado(Boton.LIBRE);
            _botonDestino.setEstado(Boton.OCUPADO);

            _ocupados--;
            _libres++;
            _txtLibres.setText(Integer.toString(_libres));
            _txtOcupados.setText(Integer.toString(_ocupados));

            Jugada2 jug = new Jugada2(_fila, _col, filaDM, colDM, pFila, pCol);
            _Jugada2s.push(jug);

            _clic = PRIMER_CLIC;
        }
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

    void iniciar(ActionEvent e)
    {
        _clic = PRIMER_CLIC;
        _libres = 1;
        _ocupados = CANT_OCUPADOS_INICIAL;
        _txtLibres.setText(Integer.toString(_libres));
        _txtOcupados.setText(Integer.toString(_ocupados));
        setEstadoInicial();
    }

  void deshacer(ActionEvent e) {
      if (_Jugada2s.empty())
          return ;
      Jugada2 jug = (Jugada2)_Jugada2s.pop();

      _botones[jug.filaOrig][jug.colOrig].setEstado(Boton.OCUPADO);
      _botones[jug.filaMed][jug.colMed].setEstado(Boton.OCUPADO);
      _botones[jug.filaDest][jug.colDest].setEstado(Boton.LIBRE);

      _txtLibres.setText(Integer.toString(--_libres));
      _txtOcupados.setText(Integer.toString(++_ocupados));
  }
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
  private void buscarSolucion(){
      java.util.List Jugada2s = Jugada2sPosibles();
      armarPanel();

      //muevo abajo
      _estado[3][4] = LIBRE;
      _estado[5][4] = OCUPADO;

      Stack pila = new Stack();
      pila.push(_estado);
      while(!pila.empty()){

      }
  }

  private static java.util.List Jugada2sPosibles(){
      java.util.List res = new Vector();
      Jugada2 j = null;
      for (int f = 0; f < CANT_FILAS; f++) {
          for (int c = 0; c < CANT_FILAS; c++) {
              j = new Jugada2(f, c, Jugada2.MOVER_ARRIBA);
              if (j.esValida())
                  res.add(j);
              j = new Jugada2(f, c, Jugada2.MOVER_ABAJO);
              if (j.esValida())
                  res.add(j);
              j = new Jugada2(f, c, Jugada2.MOVER_DERECHA);
              if (j.esValida())
                  res.add(j);
              j = new Jugada2(f, c, Jugada2.MOVER_IZQUIERDA);
              if (j.esValida())
                  res.add(j);
          }
      }

      return res;
  }

    private void iniciarEstados(){
        for (int i = 0; i < CANT_FILAS; i++) {
            for (int j = 0; j < CANT_FILAS; j++) {
                _estado[i][j] = (OCUPADO);
            }
        }
        _botones[4][4].setEstado(Boton.LIBRE);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _estado[i][j] = NULL;//arriba izq
                _estado[i][j + 6] = NULL;//arriba der
                _estado[i + 6][j] = NULL;//abajo izq
                _estado[i + 6][j + 6] = NULL;//abajo der
            }
        }
        _estado[4][4] = LIBRE;
    }

    byte[][] copiaEstados(){
        byte[][] res = new byte[CANT_FILAS][CANT_FILAS];
        for (int i = 0; i < CANT_FILAS; i++) {
            for (int j = 0; j < CANT_FILAS; j++) {
                res[i][j] = _estado[i][j];
            }
        }
        return res;
    }
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
}

class Jugada2{
    public int filaOrig;
    public int colOrig;
    public int filaDest;
    public int colDest;
    public int filaMed;
    public int colMed;

    public final static int MOVER_ARRIBA = 1;
    public final static int MOVER_ABAJO = 2;
    public final static int MOVER_DERECHA = 3;
    public final static int MOVER_IZQUIERDA = 4;

    public Jugada2(int pFilaOrig, int pColOrig,
                  int pFilaMed, int pColMed,
                  int pFilaDes, int pColDes){
        filaOrig = pFilaOrig;
        filaDest = pFilaDes;
        colOrig = pColOrig;
        colDest = pColDes;
        filaMed = pFilaMed;
        colMed = pColMed;
    }

    public Jugada2(int pFilaOrig, int pColOrig, int mover){
        filaOrig = pFilaOrig;
        colOrig = pColOrig;
        if (mover == MOVER_ARRIBA){
            filaMed = pFilaOrig - 1;
            colMed = pColOrig;

            filaDest = pFilaOrig - 2;
            colDest = pColOrig;
        }
        else if (mover == MOVER_ABAJO){
            filaMed = pFilaOrig + 1;
            colMed = pColOrig;

            filaDest = pFilaOrig + 2;
            colDest = pColOrig;
        }
        else if (mover == MOVER_IZQUIERDA){
            filaMed = pFilaOrig;
            colMed = pColOrig - 1;

            filaDest = pFilaOrig;
            colDest = pColOrig - 2;
        }
        else if (mover == MOVER_DERECHA){
            filaMed = pFilaOrig;
            colMed = pColOrig + 1;

            filaDest = pFilaOrig;
            colDest = pColOrig + 2;
        }
    }

    public boolean esValida(){
        if (filaDest < 0 || filaDest > Juego2.CANT_FILAS -1 ||
            colDest < 0 || colDest > Juego2.CANT_FILAS -1 )
            return false;

        return true;
    }

}
