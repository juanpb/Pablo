package crm.core.wfl.editor.gui.util;

import crm.core.wfl.editor.gui.mnu.WflMarksBar;

import java.awt.*;
import java.io.File;

/**
 * La idea de esta clase es ayudar a la encapsulación de los componentes. Acá
 * se hay métodos que evitan que unos componentes vean a otros.
 *
 * User: JPB
 * Date: Feb 17, 2006
 * Time: 12:01:07 PM
 */
public class WflUtility {
    private static Frame frame = null;
    private static WflMarksBar marksBar = null;


    private static boolean modified = false;
    private static File lastFileOpen = null;

    public static void setCurrentFrame(Frame frame){
        WflUtility.frame = frame;
    }
    public static Frame getCurrentFrame(){
        return frame;
    }

    public static void setCurrentFrameTitle(String id){
        String path = "";
        if (getLastFileOpen() != null)
            path = getLastFileOpen().getPath();
        setCurrentFrameTitle(path,  id);
    }

    public static void setCurrentFrameTitle(String file, String id){
        if (frame == null)
            return ;

        if (file == null)
            file = "";

        if (id == null)
            id = "";
        if (isModified())
            file +="*";
        String tit = "Editor [" + id + "] [" + file + "]";
        frame.setTitle(tit);
    }

    public static Point getCenterOfFrame(Dimension dim) {

        Dimension d = frame.getSize();
        int x = (int) (d.getWidth() - dim.getWidth()) / 2;
        int y = (int) (d.getHeight() - dim.getHeight()) / 2;
        x += frame.getX();
        y += frame.getY();
        return new Point(x, y);
    }

    public static void setModified(boolean b) {
        modified = b;
        String currentTitle = frame.getTitle();
        String newTitle = null;

        if (!b){
            if (currentTitle.endsWith("*]") || currentTitle.endsWith("*")){
                newTitle = currentTitle.replaceFirst("\\*", "");
            }
        }
        else{
            if (currentTitle.endsWith("*]") || currentTitle.endsWith("*")){
                ;//ya está marcado como modificado => no hago nada
            }
            else if (currentTitle.endsWith("]")){
                newTitle = currentTitle.substring(0, currentTitle.length()-1)+"*]";
            }
            else{
                newTitle = currentTitle + " *";
            }
        }
        if (newTitle != null)
            frame.setTitle(newTitle);
    }

    public static boolean isModified() {
        return modified;
    }

    public static void setLastFileOpen(File lastFileOpen) {
        WflUtility.lastFileOpen = lastFileOpen;
    }

    public static File getLastFileOpen() {
        return lastFileOpen;
    }

    public static void setMarksBar(WflMarksBar mb){
        marksBar = mb;
    }

    public static WflMarksBar getMarksBar(){
        return marksBar ;
    }
}
