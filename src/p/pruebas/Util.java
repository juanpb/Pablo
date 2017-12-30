package p.pruebas;

import java.awt.*;

/**
 * User: JPB
 * Date: Dec 23, 2005
 * Time: 11:10:27 AM
 */
public class Util {

    /**
     *
     * @param p1
     * @param p2
     * @return en grados
     */
    public static double getAngle(Point p1, Point p2){
        double x = p1.getX() - p2.getX();
        double y = p1.getY() - p2.getY();
        x = Math.abs(x);
        y = Math.abs(y);
        double v = Math.atan((y/x));
        v = Math.toDegrees(v);
        int cuadrante = getCuadrante(p1,p2);
        if (cuadrante == 1)
            return v;
        else if (cuadrante == 2)
            return 180 - v;
        else if (cuadrante == 3)
            return 180 + v;
        else if (cuadrante == 4)
            return 360 - v;

        return v;
    }

    /**
     * Dado el punto p1 en el cruce de los ejes devuelve 1,2,3 o 4 según donde
     * esté el p2:
     *
     *      |
     *    2 | 1
     * -----x-----
     *    3 | 4
     *      |
     *
     *  0º =< Cuad 1 < 90
     *  90º =< Cuad 2 < 180
     *  180º =< Cuad 3 < 270
     *  270º =< Cuad 4 < 360
     *
     * @param p1
     * @param p2
     * @return
     */
    private static int getCuadrante(Point p1, Point p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        if (x2 > x1 && y2 <= y1)
            return 1;
        if (x2 <= x1 && y2 <= y1)
            return 2;
        if (x2 < x1 && y2 >= y1)
            return 3;
        if (x2 >= x1 && y2 > y1)
            return 4;

        return 0;
    }

    public static Point[] getCircunferencia(int x, int y,
                                            int radio, int cantPuntos){
        Point[] res = new Point[cantPuntos];

        int ptos = cantPuntos /4;
        float grados = (float)90/ptos;
        float ang = 0;
         int i = 0;

        //primer cuadrante
        while (ang < 90) {
            //radian = (pi * degrees) / 180
            double angR = Math.toRadians(ang);
            double sen = Math.sin(angR);
            double cos = Math.cos(angR);

            double alt = radio * sen;
            double hip = radio * cos;

            int yy = (int)(y + alt);
            int xx = (int)(x + hip);

            Point p = new Point(xx, yy);
            res[i++] = p;
            ang += grados;
        }

        //segundo cuadrante
        int ind = i;
        i--;
        for (; i >= 0; i--) {
            Point p = res[i];
            int yy = (int)p.getY();
            int distAX = (int)p.getX() - x;
            int xx = x - distAX;
            Point np = new Point(xx, yy);
            res[ind++] = np;
        }

        //tercer y cuarto cuadrante
        i = ind - 1;
        for (; i >= 0; i--) {
            Point p = res[i];
            int xx = (int)p.getX();
            int distAY = y - (int)p.getY();
            int yy = y + distAY;
            Point np = new Point(xx, yy);
            if (ind < cantPuntos)
                res[ind++] = np;
        }
        return res;
    }

    public static void main(String[] args) {

//        Point[] c = Util.getCircunferencia(100,100, 10 ,100);
//        for (int i = 0; i < c.length; i++) {
//            Point point = c[i];
//            System.out.println("point = " + point);
//        }
        Point p0 = new Point (100,100);

        Point p1 = new Point (150,50);
        Point p2 = new Point (50,50);
        Point p3 = new Point (50,150);
        Point p4 = new Point (150,150);

        System.out.println(getAngle(p0, p1));
        System.out.println(getAngle(p0, p2));
        System.out.println(getAngle(p0, p3));
        System.out.println(getAngle(p0, p4));

    }
}

