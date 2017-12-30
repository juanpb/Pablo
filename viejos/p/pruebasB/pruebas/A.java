package p.pruebas;


import java.util.prefs.Preferences;

import java.util.*;
import java.io.*;
import java.net.*;


class A {


    public void x(){}
    public A() {
        System.out.println("1");

        System.out.println("4");
    }

    public static void main(String[] args)  {
        new A();
    }

    public static void a() {
    }

    protected void finalize(){
        System.out.println("A.finaleze");
        String n = "C:\\a.log";
        File f = new File(n);
        f.delete();
    }

}