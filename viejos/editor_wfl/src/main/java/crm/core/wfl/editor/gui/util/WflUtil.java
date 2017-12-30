package crm.core.wfl.editor.gui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * User: JPB Date: Jan 13, 2006 Time: 2:43:07 PM
 */
public class WflUtil {
	static private Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();

	private static int volatilId = 0;

	private static int topRightTextsize = 9;

	private static int bottomRightTextsize = 9;

	private static int centeredTextSize = 10;

	private static ImageIcon iconAddMark;

	private static ImageIcon iconCreateMark;

	/**
	 * 
	 * @param c
	 *            un String con un color de los definidos como constantes en
	 *            java.awt.Color o con RGB con formato: 1,12,123
	 * 
	 * @return una instancia del Color según el parámetro recibido o null si no
	 *         existe
	 */
	public static Color getColor(String c) {
		Color res = null;
		if (c != null && c.length() > 0 && Character.isLetter(c.charAt(0))) {
			try {
				Field field = Color.class.getField(c);
				res = (Color) field.get(Color.class);
			} catch (Exception e) {
				String msg = "Mal definido el color: " + c;
				WflUtil.showErrorMessage(msg, "Color");
			}
		} else if (c != null && c.length() > 0
				&& Character.isDigit(c.charAt(0))) {
			res = stringToColor(c);
		}
		return res;
	}

	/**
	 * Centro los mensajes teniendo en cuenta el Frame principal de la
	 * aplicación.
	 * 
	 * @param msg
	 * @return la acción por la cual se cerró el diálogo (clic en Sí, No o
	 *         Cancel)
	 */
	public static int showConfirmDialog(String msg, String title) {
		Frame c = WflUtility.getCurrentFrame();
		return JOptionPane.showConfirmDialog(c, msg, title,
				JOptionPane.YES_NO_CANCEL_OPTION);
	}

	/**
	 * Centro los mensajes teniendo en cuenta el Frame principal de la
	 * aplicación.
	 * 
	 * @param msg
	 */
	public static void showErrorMessage(String msg) {
		showErrorMessage(msg, "Error");
	}

	/**
	 * Centro los mensajes teniendo en cuenta el Frame principal de la
	 * aplicación.
	 * 
	 * @param msg
	 * @param title
	 */
	public static void showErrorMessage(String msg, String title) {
		Frame c = WflUtility.getCurrentFrame();
		JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Centro los mensajes teniendo en cuenta el Frame principal de la
	 * aplicación.
	 * 
	 * @param msg
	 * @param title
	 */
	public static void showMessage(String msg, String title) {
		Frame c = WflUtility.getCurrentFrame();
		JOptionPane.showMessageDialog(c, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static ImageIcon createImageIcon(String s) {
		// Verifico si no estaba en el cache
		Object cached = iconsCache.get(s);
		if (cached != null) {
			return (ImageIcon) cached;
		}
		URL imgURL = Thread.currentThread().getContextClassLoader()
				.getResource(s);
		if (imgURL != null) {
			ImageIcon res = new ImageIcon(imgURL);
			iconsCache.put(s, res);
			return res;
		} else {
			System.err.println("No se encuenta el archivo: " + s);
			return null;
		}
	}

	/**
	 * Parsea un String para crear un color
	 * 
	 * @param c
	 *            con el formato ###,###,###
	 * @return el color representado por el string
	 */
	public static Color stringToColor(String c) {
		String msg = "Mal definido el color: " + c + ". Se debe definir"
				+ " con el formato ###,###,###.";

		int red;
		int green;
		int blue;

		StringTokenizer st = new StringTokenizer(c, ",", false);
		if (st.countTokens() != 3) {
			WflUtil.showErrorMessage(msg, "Color");
			return null;
		}

		try {
			red = Integer.parseInt(st.nextToken());
			green = Integer.parseInt(st.nextToken());
			blue = Integer.parseInt(st.nextToken());
		} catch (Exception e) {
			WflUtil.showErrorMessage(msg, "Color");
			return null;
		}
		return new Color(red, green, blue);
	}

	/**
	 * Agrega en el centro del componente el texto recibido como parámetro
	 * 
	 * @param g2d
	 *            grágico para dibujar
	 * @param comp
	 *            componente donde se va a centrar
	 * @param txt
	 *            texto a agregar
	 */
	public static void centeredText(Graphics2D g2d, JComponent comp,
			String txt, double zoom) {
		centeredText(g2d, comp, txt, null, zoom);
	}

	/**
	 * Agrega en el centro del componente el texto recibido como parámetro
	 * 
	 * @param g2d
	 *            grágico para dibujar
	 * @param comp
	 *            componente donde se va a centrar
	 * @param txt
	 *            texto a agregar
	 * @param background
	 *            color que pone detrás del texto. Si es null no pone nada.
	 */
	public static void centeredText(Graphics2D g2d, JComponent comp,
			String txt, Color background, double zoom) {
		centeredText(g2d, 0, 0, comp.getWidth(), comp.getHeight(), txt,
				background, zoom);
	}

	/**
	 * Agrega en el centro del cuadrante el texto recibido como parámetro,
	 * 
	 * @param g2d
	 *            grágico para dibujar
	 * @param width
	 *            Ancho del cuadrante,
	 * @param height
	 *            Alto del cuadrante.
	 * @param txt
	 *            texto a agregar
	 * @param background
	 *            color que pone detrás del texto. Si es null no pone nada.
	 */
	public static void centeredText(Graphics2D g2d, int baseX, int baseY,
			int width, int height, String txt, Color background, double zoom) {
		if (txt == null || txt.equals(""))
			return;

		int s = (int) (centeredTextSize * zoom);
		g2d.setFont(new Font(null, Font.PLAIN, s));
		FontRenderContext frc = g2d.getFontRenderContext();

		TextLayout layout = new TextLayout(txt, g2d.getFont(), frc);
		Rectangle2D bounds = layout.getBounds();
		float sW = (float) bounds.getWidth();
		float sH = (float) bounds.getHeight();
		float centerX = width / 2;
		float baselineY = (height - sH) / (float) 2.0 + sH;
		float x = centerX - sW / 2;
		if (background != null) {
			g2d.setPaint(background);
			int margin = (int) (5 * zoom);
			g2d.fillRect((int) (x - margin + baseX), (int) (baselineY - sH
					- margin + baseY), (int) (sW + margin * 2),
					(int) (sH + margin * 2));
		}
		g2d.setPaint(Color.black);
		g2d.drawString(txt, x + baseX, baselineY + baseY);

	}

	public static void topRightText(Graphics2D g2d, JComponent comp,
			String txt, double zoom) {
		if (txt == null || txt.equals(""))
			return;
		g2d.setPaint(Color.black);

		int s = (int) (topRightTextsize * zoom);
		g2d.setFont(new Font(null, Font.PLAIN, s));
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(txt, g2d.getFont(), frc);
		Rectangle2D bounds = layout.getBounds();
		float y = (float) bounds.getHeight();
		float x = (float) (comp.getWidth() - bounds.getWidth());

		// lo desplazo un poco a la izquierda y abajo
		x = x - 4;
		y = y + 3;
		g2d.drawString(txt, x, y);
	}

	public static void bottomRightText(Graphics2D g2d, JComponent comp,
			String txt, double zoom) {

		if (txt == null || txt.equals(""))
			return;
		g2d.setPaint(Color.black);

		int s = (int) (bottomRightTextsize * zoom);
		g2d.setFont(new Font(null, Font.PLAIN, s));
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(txt, g2d.getFont(), frc);
		Rectangle2D bounds = layout.getBounds();
		float y = (float) (comp.getHeight() - bounds.getHeight());
		float x = (float) (comp.getWidth() - bounds.getWidth());

		// lo desplazo un poco a la izquierda y abajo
		x = x - 7;
		y = y + 3;
		g2d.drawString(txt, x, y);
	}

	public static Point getCenter(Dimension dim) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (d.getWidth() - dim.getWidth()) / 2;
		int y = (int) (d.getHeight() - dim.getHeight()) / 2;
		return new Point(x, y);
	}

	public static Point getCenter(Dimension dim, Window inWindow) {
		Dimension d = inWindow.getSize();
		int x = (int) (d.getWidth() - dim.getWidth()) / 2;
		int y = (int) (d.getHeight() - dim.getHeight()) / 2;

		x += inWindow.getX();
		y += inWindow.getY();
		return new Point(x, y);
	}

	public static void drawThickLine(Graphics2D g2d, int x1, int y1, int x2,
			int y2) {
		g2d.drawLine(x1, y1, x2, y2);
		g2d.drawLine(x1 - 1, y1, x2 - 1, y2);
		g2d.drawLine(x1 + 1, y1, x2 + 1, y2);
		g2d.drawLine(x1, y1 - 1, x2, y2 - 1);
		g2d.drawLine(x1, y1 + 1, x2, y2 + 1);
	}


	public static int getVolatilId() {
		return volatilId++;
	}

	/** @noinspection UnnecessaryLocalVariable */
	public static ImageIcon createImageIconCreateMark() {
		if (iconCreateMark != null) {
			return iconCreateMark;
		}

		int w = 20;
		Image img = new BufferedImage(w, w, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) img.getGraphics();
		int dist = 1;
		Color background = new Color(204, 204, 204);
		addTriangle(g, w, dist, background);

		// Agrego el símbolo "+"

		g.setPaint(Color.black);
		int tam = 6;
		int nx = w - (tam / 2) - dist;
		int ny = dist;
		int sx = nx;
		int sy = ny + tam;

		int wx = w - tam - dist;
		int wy = dist + tam / 2;
		int ex = w - dist;
		int ey = wy;
		g.drawLine(nx, ny, sx, sy);
		g.drawLine(wx, wy, ex, ey);
		iconCreateMark = new ImageIcon(img);
		return iconCreateMark;
	}

	private static void addTriangle(Graphics2D g, int width, int dist,
			Color background) {
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g.setRenderingHints(qualityHints);

		g.setPaint(background);
		g.fillRect(0, 0, width, width);

		Polygon fig = getTriangle(width, dist);

		WflConstants c = WflConstants.getInstance();
		Color col = c.getColorProperty(WflConstants.MARK_COLOR);

		Color color1 = col.brighter();
		Color color2 = col.darker();
		float x1 = width / 2;
		GradientPaint gp = new GradientPaint(x1, 0, color1, x1, width, color2);
		g.setPaint(gp);
		g.fill(fig);
	}

	public static ImageIcon createImageIconAddMark() {
		if (iconAddMark != null) {
			return iconAddMark;
		}
		int w = 20;
		int dist = 1;

		Image img = new BufferedImage(w, w, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) img.getGraphics();
		WflConstants c = WflConstants.getInstance();
		Color background = c
				.getColorProperty(WflConstants.CONTAINER_BACKGROUND);

		addTriangle(g, w, dist, background);

		iconAddMark = new ImageIcon(img);
		return iconAddMark;
	}

	private static Polygon getTriangle(int width, int dist) {
		// puntos del triángulo
		int[] xp = { width / 2, dist, width - dist };
		int[] yp = { dist, width - dist, width - dist };
		return new Polygon(xp, yp, 3);
	}
}
