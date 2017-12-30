package crm.core.wfl.editor.function.js;

import java.io.PrintWriter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.ScriptableObject;

import crm.core.wfl.editor.function.WflScriptCompilerItf;

/**
 * Implementacion para compilar JavaScripts.
 * @author cm
 *
 */
public class WflJavaScriptCompiler implements WflScriptCompilerItf {

	/**
	 * Compila el script dejando el output en os.
	 * @param script Script a compilar.
	 * @param os Print writer donde deja la salida.
	 * @return true si compilo ok, false si tuvo un error.
	 */	
	public boolean compile(String script, PrintWriter os) {
		// Entro al contexto.
		ContextFactory contextFactory = new ContextFactory();
		
		Context cx = contextFactory.enterContext();

		try {
			// Creo el scope para las variables.
			ImporterTopLevel importerScope = new ImporterTopLevel();
			importerScope.initStandardObjects(cx, true);
			ScriptableObject scope = importerScope;
			
			cx.compileFunction(scope, script, "", 1, null);
		}
		catch(Exception e) {
			os.println(e.getMessage());
			os.flush();
			return false;
		}
		return true;
	}

}
