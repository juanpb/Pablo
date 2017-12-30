package crm.core.wfl.editor.gui.util;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Title:        JMovics AVarela<p>
 * Description:  Muestra una imagen como Splash Screen, durante el tiempo que
 * se le indique<p>
 * Copyright:    Copyright (c) 2001<p>
 * Company:      CRM S.A.<p>
 * @author       AVarela
 * @version 1.0
 */
public class WflSplashScreen extends JWindow implements Runnable,
    ActionListener
{
	private static final long serialVersionUID = 1L;
    private int timeout;

    public WflSplashScreen(ImageIcon image, int timeout)
    {
        this.timeout = timeout;
        int w = image.getIconWidth() + 5;
        int h = image.getIconHeight() + 5;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - w) / 2;
        int y = (screen.height - h) / 2;
        setBounds(x, y, w, h);

        getContentPane().setLayout(new BorderLayout());
        JLabel picture = new JLabel(image);
        getContentPane().add("Center", picture);
        picture.setBorder(new BevelBorder(BevelBorder.RAISED));
    }

    public void run()
    {
        setVisible(true);
        Timer timer = new Timer(0, this);
        timer.setRepeats(false);
        timer.setInitialDelay(timeout);
        timer.start();
    }

    public void actionPerformed(ActionEvent event)
    {
      setVisible(false);
      dispose();
    }

}
