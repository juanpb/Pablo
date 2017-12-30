package p.test;

import junit.framework.TestCase;
import junit.runner.Version;
import p.util.UtilString;

public class TestUtilString extends TestCase{
    public TestUtilString(String s) {
        super(s);
    }

    public void testSimple666(){
        String s = Version.id();
        System.out.println("s = " + s);
        assertEquals("1234", UtilString.eliminarCantDesdeLaDerecha("123456", 2));

    }
}
