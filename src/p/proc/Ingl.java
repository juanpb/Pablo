package p.proc;

import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: Jul 28, 2010
 * Time: 12:34:56 AM
 */
public class Ingl {

    private static Map<String, String> mapPron = new HashMap<String, String>();
    private static Map<String, String> mapVI2Col = new HashMap<String, String>();
    private static Map<String, String> mapVI3Col = new HashMap<String, String>();

    static{
        mapPron.put("A","éi");
        mapPron.put("B","bi");
        mapPron.put("C","si");
        mapPron.put("D","di");
        mapPron.put("E","i");
        mapPron.put("F","ef");
        mapPron.put("G","gi");
        mapPron.put("H","eich");
        mapPron.put("I","ai");
        mapPron.put("J","jei");
        mapPron.put("K","kei");
        mapPron.put("L","el");
        mapPron.put("M","em");
        mapPron.put("N","en");
        mapPron.put("O","ou");
        mapPron.put("P","pi");
        mapPron.put("Q","kiu");
        mapPron.put("R","ar");
        mapPron.put("S","es");
        mapPron.put("T","ti");
        mapPron.put("U","yu");
        mapPron.put("V","vi");
        mapPron.put("W","dobliu");
        mapPron.put("X","ecs");
        mapPron.put("Y","uái");
        mapPron.put("Z","sed");
    }


    public static void main(String[] args) throws IOException {
        verb();
//        mostrar(mapVI2Col);
        if (args.length > 0 ){
            String s = args[0];
            if (s.toLowerCase().equals("3c"))
                mostrarUnaVezCV(mapVI3Col);
            return ;
        }

        mostrarUnaVezCV(mapVI2Col);
    }

    /**
     *
     * contOk = 51 - contMal = 16
     */
    public static void mostrarUnaVezCV(Map<String, String> map) throws IOException {
        String[] keys = new String[map.size()];
        map.keySet().toArray(keys);
        byte[] bs;
        int contOk = 0;
        int contMal = 0;
        List<String> mal = new ArrayList<String>();
        for(String key : keys) {
            String v = map.get(key).trim();

            System.out.println(key);
            bs = new byte[100];
            System.in.read(bs);
            String s = new String(bs);
            s = s.trim();
            if (v.equalsIgnoreCase(s)){
                System.out.println("Bien");
                contOk++;
            }
            else{
                System.out.println(v);
                contMal++;
                mal.add(key + "-" + v);
            }

        }
        System.out.println("contOk = " + contOk);
        System.out.println("contMal = " + contMal);
        System.out.println("mal = " + mal);
    }

    public static void mostrar(Map<String, String> map) throws IOException {
        String[] keys = new String[map.size()];
        map.keySet().toArray(keys);
        final Random r = new Random();
        byte[] bs;

        while (true) {
            String key = keys[r.nextInt(map.size())].trim();
            String v = map.get(key).trim();

            System.out.println(key);
            bs = new byte[100];
            System.in.read(bs);
            String s = new String(bs);
            s = s.trim();
            if (v.equalsIgnoreCase(s))
                System.out.println("Bien");
            else{
                System.out.println(v);
            }

            //al revés
            key = keys[r.nextInt(map.size())];
            v = map.get(key);

            System.out.println(v);
            bs = new byte[100];
            System.in.read(bs);
            s = new String(bs);
            s = s.trim();
            if (key.equalsIgnoreCase(s))
                System.out.println("Bien");
            else{
                System.out.println(key);
            }


        }
    }


    private static void verb(){
        String v = "be, was, been;become, became, become;begin, began, begun;break, broke, broken;bring, brought, brought;build, built, built;buy, bought, bought;can, could, -;catch, caught, caught;choose, chose, chosen;come, came, come;cost, cost, cost;cut, cut, cut;do, did, done;drink, drank, drunk,;drive, drove, driven;eat, ate, eaten ;fall, fell, fallen;feel,felt, felt;find, found, found;fly, flew, flown;forget, forgot, forgotten;get, got, gotten;give, gave,given;go, went, gone;grow, grew, grown;have, had, had;hear, heard, heard;hit, hit, hit;keep, kept, kept;know, knew, known;learn, learnt, learnt;leave, left, left;lend, lent, lent;let, let, let;lose, lost, lost;make, made, made;meet, met, met;pay, paid, paid;put, put, put;read, read, read;ride, rode, ridden;ring, rang, rung;run, ran, run;say, said, said;see, saw, seen;sell, sold, sold;send, sent, sent ;sing, sang, sung;shut, shut, shut;sit, sat, sat;sleep, slept, slept;speak, spoke, spoken;spend, spent, spent;stand, stood, stood;steal, stole, stolen;swim, swam, swum;take, took, taken;teach, taught, taught;tell, told, told;think, thought, thought;throw, threw, thrown;understand, understood, understood;wake, woke, woken;wear, wore, worn;win, won, won;write, wrote, written";
        StringTokenizer st = new StringTokenizer(v, ";", false);
        while(st.hasMoreElements()) {
            String t = st.nextToken();
            try {
                StringTokenizer unT = new StringTokenizer(t, ",", false);
                final String inf = unT.nextToken();
                String segC = unT.nextToken();
                String terC = unT.nextToken();
                mapVI2Col.put(inf, segC);
                mapVI3Col.put(inf, terC);
            } catch (Exception e) {
                System.out.println("Problemas con: " + t);
            }
        }
    }

    private static void verb3Col(){
        String v = "be, was, been;become, became, become;begin, began, begun;break, broke, broken;bring, brought, brought;build, built, built;buy, bought, bought;can, could, -;catch, caught, caught;choose, chose, chosen;come, came, come;cost, cost, cost;cut, cut, cut;do, did, done;drink, drank, drunk,;drive, drove, driven;eat, ate, eaten ;fall, fell, fallen;feel,felt, felt;find, found, found;fly, flew, flown;forget, forgot, forgotten;get, got, gotten;give, gave,given;go, went, gone;grow, grew, grown;have, had, had;hear, heard, heard;hit, hit, hit;keep, kept, kept;know, knew, known;learn, learnt, learnt;leave, left, left;lend, lent, lent;let, let, let;lose, lost, lost;make, made, made;meet, met, met;pay, paid, paid;put, put, put;read, read, read;ride, rode, ridden;ring, rang, rung;run, ran, run;say, said, said;see, saw, seen;sell, sold, sold;send, sent, sent ;sing, sang, sung;shut, shut, shut;sit, sat, sat;sleep, slept, slept;speak, spoke, spoken;spend, spent, spent;stand, stood, stood;steal, stole, stolen;swim, swam, swum;take, took, taken;teach, taught, taught;tell, told, told;think, thought, thought;throw, threw, thrown;understand, understood, understood;wake, woke, woken;wear, wore, worn;win, won, won;write, wrote, written";
        StringTokenizer st = new StringTokenizer(v, ";", false);
        while(st.hasMoreElements()) {
            String t = st.nextToken();
            try {
                StringTokenizer unT = new StringTokenizer(t, ",", false);
                final String inf = unT.nextToken();
                mapVI2Col.put(inf,  unT.nextToken());
                if (!unT.hasMoreTokens()){
                    System.out.println("Prob: " + inf);
                }
            } catch (Exception e) {
                System.out.println("Problemas con: " + t);
            }
        }
    }
}
