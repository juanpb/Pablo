package p.tpredes;

import java.io.*;
import nt.util.event.*;
import java.util.*;

/**
 * Conecta un InputStream con un OutputStream.
 * Todo byte que sea factible de ser leido en el InputStream será
 * escrito en el OutputStream.
 * Esta clase ofrece la posibilidad de registrarse como
 * StreamEventListener para ser informado de los errores que
 * ocurren sobre el stream.
 */
public class StreamConnector extends Thread {
    private java.io.InputStream _input;
    private MultipleStream _output;

    private Vector listeners;
    private byte[] buffer;

    private static final int defaultBufferSize = 4096;
    private int bufferSize;

    /**
     * Constructor. Conecta dos streams
     */
    public StreamConnector(MultipleStream output, InputStream _input) {
      this(output, _input, defaultBufferSize);
    }

        /**
         * Conecta dos streams. Se puede especificar el tamaño del buffer
         * entre streams
         */
        public StreamConnector(MultipleStream output, InputStream _input,
                               int bufferSize) {
          super();
          this._input = _input;
          this._output = output;
          this.listeners = new Vector();
          buffer = new byte[bufferSize];
          this.bufferSize = bufferSize;
        }


        /**
         * Se dispara ante un error. Se cierran las conexiones
         * @param e
         */
        public void fireStreamEvent(StreamActionEvent e) {
            try {
                if (_input != null) {
                    _input.close();
                    _input = null;
                }
                if (_output != null) {
                    _output.close();
                    _output = null;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        /**
         * Elimina a un StreamEventListener
         * @param e nt.util.event.StreamEventListener
         */
        public void removeStreamListener(StreamEventListener e) {
          listeners.remove(e);
        }

        /**
         * Este metodo lee del stream de entrada la máxima cantidad de
         * información posible sin superar el tamaño del buffer especificado.
         * Luego escribe esta informacion al stream de salida. En el caso
         * de la informacion en el stream se termine, se finaliza el thread y
         * se avisa a los listeners de lo ocurrido.
         */
        public void run() {

          int bytesReaded;

          while (true) {
            try {
              //Se levanta la cantidad de bytes disponibles en
              //el stream
              int bytesToRead = _input.available();

              //Se lo limita al tamaño del buffer
              if (bytesToRead > bufferSize)
                bytesToRead = bufferSize;

                //Si no hay mas bytes para leer, se verifica que
                //no se haya finalizado la informacion del stream.
              if (bytesToRead == 0) {
                int c = _input.read();// acá se bloquea
                //Caso en el que el stream todavia no terminó
                if (c != -1)
                  _output.write(c);

                _output.flush();

                //Caso en el que terminó
                if (c == -1)
                  throw new IOException("Stream finished");
              }
              else {
                //Si hay mas bytes para leer, se escriben en el
                //buffer y luego en el stream de salida
                bytesReaded = _input.read(buffer, 0, bytesToRead);
                _output.write(buffer, 0, bytesReaded);
                _output.flush();
              }

              //Se permite a otros threads ejecutar
              this.yield();
            }
            catch (IOException e) {
              //Si ocurre un error, se avisa a los listeners
              StreamActionEvent event = new StreamActionEvent(this,
                  StreamActionEvent.CONNECTION_FINISHED, e.getMessage());
              fireStreamEvent(event);
              break;
            }
	}
    }

}
