package crm.core.wfl.editor.gui.cmp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalToolTipUI;

/**
 * Esta clase representa el user interface de un tooltip multilinea.
 * @author cm
 *
 */
class WflMultiLineToolTipUI extends MetalToolTipUI {
	private String[] strs;

	private Font font = new Font("Monospaced", Font.PLAIN, 12);

	/**
	 * Pineta el componente.
	 * @param g Grafico donde lo dibuja.
	 * @param c Componente.
	 */
	public void paint(Graphics g, JComponent c) {
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		Dimension size = c.getSize();
		g.setColor(c.getBackground());
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(c.getForeground());
		if (strs != null) {
			for (int i = 0; i < strs.length; i++) {
				g.drawString(strs[i], 10, (metrics.getHeight()) * (i + 1));
			}
		}
	}

	/**
	 * Obtiene el tamaño del componente.
	 * @param c Componente.
	 */
	public Dimension getPreferredSize(JComponent c) {
		FontMetrics metrics = c.getFontMetrics(font);

		String tipText = ((JToolTip) c).getTipText();
		if (tipText == null) {
			tipText = "";
		}
		BufferedReader br = new BufferedReader(new StringReader(tipText));
		String line;
		int maxWidth = 0;
		Vector<String> v = new Vector<String>();
		try {
			while ((line = br.readLine()) != null) {
				int width = SwingUtilities.computeStringWidth(metrics, line);
				maxWidth = (maxWidth < width) ? width : maxWidth;
				v.addElement(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		int lines = v.size();
		if (lines < 1) {
			strs = null;
			lines = 1;
		} else {
			strs = new String[lines];
			int i = 0;
			for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
				strs[i] = (String) e.nextElement();
			}
		}
		int height = metrics.getHeight() * lines;
		return new Dimension(maxWidth + 20, height + 10);
	}
}
