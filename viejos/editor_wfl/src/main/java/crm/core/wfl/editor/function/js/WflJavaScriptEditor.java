package crm.core.wfl.editor.function.js;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.text.PlainDocument;

import org.syntax.jedit.SyntaxStyle;
import org.syntax.jedit.TextAreaDefaults;
import org.syntax.jedit.tokenmarker.JavaScriptTokenMarker;
import org.syntax.jedit.tokenmarker.Token;

import crm.core.wfl.editor.function.WflScriptEditor;

/**
 * Editor para java scripts.
 * @author cm
 */
public class WflJavaScriptEditor extends WflScriptEditor implements MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static TextAreaDefaults areaDefaults;
	
	static {
		SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];
		styles[Token.COMMENT1] = new SyntaxStyle(new Color(57,137,77),true,false);
		styles[Token.COMMENT2] = new SyntaxStyle(Color.blue,true,false);
		styles[Token.KEYWORD1] = new SyntaxStyle(new Color(128,0,128),false,true);
		styles[Token.KEYWORD2] = new SyntaxStyle(new Color(128,0,128),false,true);
		styles[Token.KEYWORD3] = new SyntaxStyle(new Color(128,0,128),false,true);
		styles[Token.LITERAL1] = new SyntaxStyle(new Color(9, 9, 255),false,false);
		styles[Token.LITERAL2] = new SyntaxStyle(new Color(9, 9, 255),false,true);
		styles[Token.LABEL] = new SyntaxStyle(new Color(0x990033),false,true);
		styles[Token.OPERATOR] = new SyntaxStyle(Color.black,false,true);
		styles[Token.INVALID] = new SyntaxStyle(Color.red,false,true);
		areaDefaults = TextAreaDefaults.getDefaults();
		areaDefaults.styles = styles;
	}
	
	/**
	 * Constructor por defecto.
	 *
	 */
	public WflJavaScriptEditor() {
		super(areaDefaults);
        setTokenMarker(new JavaScriptTokenMarker());
        setFont(new Font("monospaced", Font.PLAIN, 12));
        this.getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
        this.addMouseWheelListener(this);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if(getFirstLine() + e.getWheelRotation() >= 0) {
			if(e.getWheelRotation()<0)
				scrollTo(getFirstLine() + e.getWheelRotation(), 1);
			else {
				if(getFirstLine() + this.getVisibleLines()<=this.getLineCount()-1)
					scrollTo(getFirstLine() + this.getVisibleLines(), 1);				
			}
		}
	}

}
