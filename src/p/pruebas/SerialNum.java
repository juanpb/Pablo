package p.pruebas;


 public class SerialNum {
     // The next serial number to be assigned
     private static int nextSerialNum = 0;
     private static StringBuffer _sb = new StringBuffer("1");

     private static ThreadLocal serialNum = new ThreadLocal() {
         protected synchronized Object initialValue() {
             System.out.println("initialValue");
//             return new Integer(nextSerialNum++);
             _sb.append("0");
             return _sb;
         }
     };

     public static StringBuffer get() {
         return (StringBuffer) (serialNum.get());
//         int res = nextSerialNum;
//         nextSerialNum++;
//         return res;
     }

     public static void main(String[] args) {
     }
 }