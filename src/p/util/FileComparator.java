package p.util;

import java.io.File;
import java.util.Comparator;

/**
 * User: Administrador
 * Date: 22/01/2006
 * Time: 13:58:03
 */
public class FileComparator implements Comparator{

    public int compare(Object o1, Object o2) {
        if (o1 instanceof File && o2 instanceof  File){
            String f = ((File) o1).getName();
            String f2 = ((File) o2).getName();
            int i = f.compareTo(f2);
            return i;
        }
        return 0;
    }

}
