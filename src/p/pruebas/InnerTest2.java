package p.pruebas;

public class InnerTest2 {
    public static void main(String s[]) {
        
        new OuterClass().doSomething(10, 20);
// This is legal
        OuterClass.InnerClass ic = new OuterClass().new InnerClass();
        ic.doSomething();
// Compiler error, local inner classes cannot be accessed from outside
//  OuterClass.LocalInnerClass lic = new OuterClass().new LocalInnerClass();
// lic.doSomething();
        new OuterClass().doAnonymous();
    }
}

class OuterClass {
    final int a = 100;
    private String secret = "Nothing serious";
    public void doSomething(int arg, final int fa) {

        final int x = 100;
        int y = 200;
        System.out.println(this.getClass() + " - in doSomething");
        System.out.print("a = " + a + " secret = " + secret + " arg = " + arg + " fa = " + fa);
        System.out.println(" x = " + x + " y = " + y);
// Compiler error, forward reference of local inner class
// new LocalInnerClass().doSomething();
        abstract class AncestorLocalInnerClass {
        } // inner class can be abstract

        final class LocalInnerClass extends AncestorLocalInnerClass { // can be final
            public void doSomething() {
                System.out.println(this.getClass() + " - in doSomething");
                System.out.print("a = " + a);
                System.out.print(" secret = " + secret);
//      System.out.print(" arg = " + arg);  // Compiler error, accessing non-final argument
                System.out.print(" fa = " + fa);
                System.out.println(" x = " + x);
//      System.out.println(" y = " + y); // Compiler error, accessing non-final variable
            }
        }
        new InnerClass().doSomething(); // forward reference fine for member inner class
        new LocalInnerClass().doSomething();
    }

    abstract class AncestorInnerClass {
    }

    interface InnerInterface {
        final int someConstant = 999;
    } // inner interface

    class InnerClass extends AncestorInnerClass implements InnerInterface {
        public void doSomething() {
            System.out.println(this.getClass() + " - in doSomething");//(x)
            System.out.println("a = " + a + " secret = " + secret + " someConstant = " + someConstant);
        }
    }

    public void doAnonymous() {
        // Anonymous class implementing the inner interface
        System.out.println((new InnerInterface() {
        }).someConstant);

        // Anonymous class extending the inner class
        (new InnerClass() {
            public void doSomething() {
                secret = "secret is changed";
                super.doSomething(); //(x)
            }
        }).doSomething();      //secret = "secret is changed";
    }
}


