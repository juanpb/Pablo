package p.test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Dec 3, 2007
 * Time: 1:55:33 PM
 */
public class UsoMemConGC {
    private List bg = new ArrayList();
    private static final long CICLOS = 50000;
    public static void main(String[] args) {
        UsoMemConGC x = new UsoMemConGC();
        x.prueba4();
    }

    public void prueba2(){
        for (Object aBg : bg) {
            System.out.print(aBg);
        }
    }

    public void prueba3(){
        RuntimeMXBean b = ManagementFactory.getRuntimeMXBean();
        System.out.println("b.getBootClassPath() = " + b.getBootClassPath());
        System.out.println("b.getClassPath() = " + b.getClassPath());
        System.out.println("b.getInputArguments() = " + b.getInputArguments());
        System.out.println("b.getLibraryPath() = " + b.getLibraryPath());
        System.out.println("b.getManagementSpecVersion() = " + b.getManagementSpecVersion());
        System.out.println("b.getName() = " + b.getName());
        System.out.println("b.getSpecName() = " + b.getSpecName());
        System.out.println("b.getSpecVendor() = " + b.getSpecVendor());
        System.out.println("b.getStartTime() = " + b.getStartTime());
        System.out.println("b.getUptime() = " + b.getUptime());
        System.out.println("b.getVmName() = " + b.getVmName());
        System.out.println("b.getVmVendor() = " + b.getVmVendor());
        System.out.println("b.getVmVersion() = " + b.getVmVersion());


    }
    public void prueba1(){

        Runtime rt = Runtime.getRuntime();
        long tm = rt.totalMemory();
        long ultFreeMem = rt.freeMemory();

        for (int i=0;i < CICLOS; i++) {
            List x = new ArrayList();
            bg.add(x);
            long fm = rt.freeMemory();
            if (ultFreeMem != fm){
                ultFreeMem = fm;
                System.out.println("memoria usada en i= "+ i + ": " + (tm - fm));
            }
        }
    }

    public void prueba4(){
        List x = new ArrayList();
        for (int i=0;i < CICLOS; i++) {
            x.add(new Long(i));
            if (i%10==0)
                System.out.println("i = " + i);
        }
        mostrar(x);        
    }

    private void mostrar(List list){
        for (Object aList : list) {
            Long aLong = (Long) aList;
            System.out.println(aLong + ", ");
        }
    }
}
