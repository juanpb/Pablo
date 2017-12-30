package p.pruebas;

/**
 * User: Administrador
 * Date: 04/12/2006
 * Time: 19:22:37
 */


class Ref {
	Object f;
}

class EjTesis1{
	static Object s;

	void m0() {
		Object a = m1();

	}

    Object m1(){
        Ref b = m2();
        Object c = b.f;
        return c;
    }

    Ref m2(){
		Ref d = new Ref();
		Object e = new Object();
		s = e;
		d.f = e;
		return d;
    }
}
