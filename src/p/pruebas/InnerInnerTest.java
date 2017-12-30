package p.pruebas;

public class InnerInnerTest {

    public static void main(String s[]) {

        new Outer().new Inner().new InnerInner().new InnerInnerInner().doSomething();
        new Outer().new InnerChild().doSomething();
        new Outer2();//.new Inner2().new InnerInner2().doSomething();
        new InnerChild2().doSomething();
    }
}

class Outer {
    String name = "Vel";
    class Inner {
        String name = "Sharmi";
        class InnerInner {
            class InnerInnerInner {
                public void doSomething() {
                    // No problem in accessing without full qualification,
                    // inner-most class variable shadows the outer-most class variable
                    System.out.println(name); // Prints "Sharmi"
                    System.out.println(Outer.this.name); // Prints "Vel", explicit reference to Outer
// error, variable is not inherited from the outer class, it can be just accessible
//      System.out.println(this.name);
//      System.out.println(InnerInner.this.name);
//      System.out.println(InnerInnerInner.this.name);
// error, super cannot be used to access outer class.
// super will always refer the parent, in this case Object
//      System.out.println(super.name);
                    System.out.println(Inner.this.name); // Prints "Sharmi", Inner has declared 'name'
                }
            }
        }
    }

    /* This is an inner class extending an inner class in the same scope */
    class InnerChild extends Inner {
        public void doSomething() {
// compiler error, explicit qualifier needed
// 'name' is inherited from Inner, Outer's 'name' is also in scope
    System.out.println(name);
            System.out.println(Outer.this.name); // prints "Vel", explicit reference to Outer
            System.out.println(super.name); // prints "Sharmi", Inner has declared 'name'
            System.out.println(this.name); // prints "Sharmi", name is inherited by InnerChild
        }
    }
}

class Outer2 {
    static String name = "Vel";
    static class Inner2 {
        static String name = "Sharmi";
        class InnerInner2 {
            public void doSomething() {
                System.out.println(name); // prints "Sharmi", inner-most hides outer-most
                System.out.println(Outer2.name); // prints "Vel", explicit reference to Outer2's static variable
//      System.out.println(this.name); // error, 'name' is not inherited
//      System.out.println(super.name); // error, super refers to Object
            }
        }
    }
}

/* This is a stand-alone class extending an inner class */
class InnerChild2 extends Outer2.Inner2 {
    public void doSomething() {
        System.out.println(name); // prints "Sharmi", Inner2's name is inherited
        System.out.println(Outer2.name); // prints "Vel", explicit reference to Outer2's static variable
        System.out.println(super.name); // prints "Sharmi", Inner2 has declared 'name'
        System.out.println(this.name); // prints "Sharmi", name is inherited by InnerChild2
    }
}