package crm.core.wfl.editor.function;

import java.io.PrintWriter;

/**
 * Representa un compilador de scripts.
 * @author cm
 */
public interface WflScriptCompilerItf {

	/**
	 * Compila el script dejando el output en os.
	 * @param script Script a compilar.
	 * @param os Print writer donde deja la salida.
	 * @return true si compilo ok, false si tuvo un error.
	 */	
	boolean compile(String script, PrintWriter os);
}
