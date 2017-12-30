package crm.core.wfl.editor.function;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.TextAreaDefaults;


/**
 * Superclase de todos los editores de scripts.
 * @author cm
 *
 */
public class WflScriptEditor extends JEditTextArea  {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor con los defaults.
	 * @param areaDefaults
	 */
	public WflScriptEditor(TextAreaDefaults areaDefaults) {
		super(areaDefaults);
	}

}
