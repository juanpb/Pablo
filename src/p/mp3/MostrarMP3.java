package p.mp3;

import org.apache.log4j.Logger;
import p.util.DirFileFilter;
import p.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;

/**
 * Regardless of the bitrate of the file, a frame in an MPEG-1 file lasts
 * for 26ms (26/1000 of a second). This works out to around 38fps
 *
 *
 * The header is 4 bytes, 32 bits, big and begins with something called sync.
 * This sync is, at least according to the MPEG standard, 12 set bits in a row.
 * Some add-on standards made later uses 11 set bits and one cleared bit.
 * The sync is directly followed by a ID bit, indicating if the file is a
 * MPEG-1 och MPEG-2 file. 0=MPEG-2 and 1=MPEG-1
 * The layer is defined with the two layers bits. They are oddly defined as
 *      0 0 Not defined
 *      0 1 Layer III
 *      1 0 Layer II
 *      1 1 Layer I
 *
 * With this information and the information in the bitrate field we can
 * determine the bitrate of the audio (in kbit/s) according to this table.
 *
 * BV = Bitratevalue;  n-i <=> MPEG n-Layer i
 * BV       1-I     1-II    1-III   2-I     2-II    2-III
 * 0001     32      32      32      32      32      8
 * 0010     64      48      40      64      48      16
 * 0011     96      56      48      96      56      24
 * 0100     128     64      56      128     64      32
 * 0101     160     80      64      160     80      64
 * 0110     192     96      80      192     96      80
 * 0111     224     112     96      224     112     56
 * 1000     256     128     112     256     128     64
 * 1001     288     160     128     288     160     128
 * 1010     320     192     160     320     192     160
 * 1011     352     224     192     352     224     112
 * 1100     384     256     224     384     256     128
 * 1101     416     320     256     416     320     256
 * 1110     448     384     320     448     384     320
 *
 *
 * The sample rate is described in the frequency field. These values is
 * dependent of which MPEG standard is used according to the following table.
 *
 * Freq     MPEG-1      MPEG-2
 * 00       44100 Hz    22050 Hz
 * 01       48000 Hz    24000 Hz
 * 10       32000 Hz    16000 Hz
 *
 * Three bits is not needed in the decoding process at all. These are the
 * copyright bit, original home bit and the private bit. The copyright has the
 * same meaning as the copyright bit on CDs and DAT tapes, i.e. telling that it
 * is illegal to copy the contents if the bit is set. The original home bit
 * indicates, if set, that the frame is located on its original media. No one
 * seems to know what the privat bit is good for.
 *
 * If the protection bit is NOT set then the frame header is followed by a 16
 * bit checksum, inserted before the audio data. If the padding bit is set then
 * the frame is padded with an extra byte. Knowing this the size of the complete
 * frame can be calculated with the following formula
 *
 * FrameSize = 144 * BitRate / SampleRate [when the padding bit is cleared]
 * FrameSize = (144 * BitRate / SampleRate) + 1 [when the padding bit is set]
 *
 *
 * The mode field is used to tell which sort of stereo/mono encoding that has
 * been used. The purpose of the mode extension field is different for different
 * layers, but I really don't know exactly what it's for.
 * Mode value   mode
 * 00           Stereo
 * 01           Joint stereo
 * 10           Dual channel
 * 11           Mono
 *
 *
 * The last field is the emphasis field. It is used to sort of 're-equalize' the
 * sound after a Dolby-like noise supression. This is not very used and will
 * probably never be. The following noise supression model is used
 * Emphasis value   Emphasis method
 * 00               none
 * 01               50/15ms
 * 1 0              11 CCITT j.17
 *
 *
 */
public class MostrarMP3 {
    private static final Logger logger = Logger.getLogger(MostrarMP3.class);

    private File _file   = null;
    private boolean[] _header = new boolean[32];
    private BitSet _bitset = null;

    public static final int MPEG_1 = 0;
    public static final int MPEG_2 = 1;
    public static final int LAYER_3 = 2;
    public static final int LAYER_2 = 3;
    public static final int LAYER_1 = 4;

    public static final int CHANEL_MODE_STEREO = 10;
    public static final int CHANEL_MODE_JOIN_STEREO = 11;
    public static final int CHANEL_MODE_DUAL_CHANEL = 12;
    public static final int CHANEL_MODE_MONO = 13;


    private static String dir = "D:\\P\\Música\\_mp4\\-\\";
    private static String arch = dir + "Adriana Calcanhotto - 2000 - Público - Clandestino.mp3";

    public static void main(String[] args) throws Exception  {

        File f = new File(dir);
        DirFileFilter dff = new DirFileFilter(false, "mp3");
        File[] files = f.listFiles(dff);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            MostrarMP3 fm = new MostrarMP3(file);
            System.out.println(fm.getLayer() + " = " + file.getName());

        }
//        FrameMP3 fm = new FrameMP3(f);
//
//        System.out.println("fm.frameLenght() = " + fm.frameLenght());
//        System.out.println("fm.getBitrate() = " + fm.getBitrate());
//        System.out.println("fm.getFrequency() = " + fm.getFrequency());
//        System.out.println("fm.getId() = " + fm.getId());
//        System.out.println("fm.getLayer() = " + fm.getLayer());
//        System.out.println("fm.isCopyrighted() = " + fm.isCopyrighted());
//        System.out.println("fm.isOriginal() = " + fm.isOriginal());
//
//        int tam = fm.frameLenght();
//        FileInputStream fis = new FileInputStream(f);
//        byte[] lec = new byte[tam * 500];
//        fis.read(lec);
//        fis.close();
//        FileOutputStream fos = new FileOutputStream("C:\\_" + f.getName());
//        fos.write(lec);
//        fos.close();
//        System.out.println("Listo ");
    }

    public MostrarMP3(String path) throws Exception
    {
      this(new File(path));
    }

    public MostrarMP3(File file)
    {
        RandomAccessFile raf = null;

        try{
            raf = new RandomAccessFile(file, "r");
            init(raf);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally{
            if (raf != null)
                try {
                    raf.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    //No cierra el raf
    private void init(RandomAccessFile raf) throws Exception{
        //si el archivo empieza con el ID3 v2 tiro una exception
        int[] lect = new int[4];

        byte[] b = new byte[3];
        raf.seek(0);
        raf.read(b);
        boolean tiene = new String(b).equalsIgnoreCase("ID3");
        if (tiene){
//            throw new Exception ("Tiene ID3 v2");
        }

        raf.seek(0);
        for (int i = 0; i < 4; i++)
        {
            int x = raf.readUnsignedByte();
            lect[i] = x;
        }
         _bitset = Util.unsignedByte2Bits(lect);
    }

    public MostrarMP3(RandomAccessFile raf) throws Exception {
        init(raf);
    }


    public MostrarMP3(int[] unsignedBytes) throws Exception    {
        _bitset = Util.unsignedByte2Bits(unsignedBytes);
    }


    public boolean esCorrecto(){
      return tieneSync();
    }


    public static int frameLenght(int bitrate, int frec, boolean padding){
        int res = 0;
        if (bitrate < 1000)             // si es 128
            bitrate = bitrate * 1000;   //deberia ser 128000

        res = (144 * bitrate) / frec;
        if (padding)
            res += 1;
        return res;
    }

    public int frameLenght(){
        int bitrate = getBitrate();
        int frec = getFrequency();
        boolean padding = isPadding();

        return frameLenght(bitrate,  frec,  padding);
    }
    /**
     * Devuelve true sii tiene los 12 primeros bits en 1
     */
    public boolean tieneSync(){
        for (int i = 0; i < 12; i++)
        {
            boolean b = _bitset.get(i);
            System.out.println("b = " + b);
            if (!b)
                return false;
        }
        return true;
    }

    public int getId(){
        if (_bitset.get(12))
            return MPEG_1;
        else
            return MPEG_2;
    }

    public int getLayer(){
//        * The layer is defined with the two layers bits. They are oddly defined as
//        *      0 0 Not defined
//        *      0 1 Layer III
//        *      1 0 Layer II
//        *      1 1 Layer I
        if (_bitset.get(13) == false &&
            _bitset.get(14) == true)
            return LAYER_3;
        else if (_bitset.get(13) == true &&
            _bitset.get(14) == false)
            return LAYER_2;
        else if (_bitset.get(13) == true &&
            _bitset.get(14) == true)
            return LAYER_1;
        else
            return -1;
    }


    /**
     * Sólo soporta Layer III
     */
    public int getBitrate(){
        if (getLayer() != LAYER_3){
            return -1;
        }
        boolean a = _bitset.get(16);
        boolean b = _bitset.get(17);
        boolean c = _bitset.get(18);
        boolean d = _bitset.get(19);

        if (getId() == MPEG_1){
            if (!a && !b && !c && d) //0001
                return 32;
            else if (!a && !b && c && !d) //0010
                return 40;
            else if (!a && !b && c && d) //0011
                return 48;
            else if (!a && b && !c && !d) //0100
                return 56;
            else if (!a && b && !c && d) //0101
                return 64;
            else if (!a && b && c && !d) //0110
                return 80;
            else if (!a && b && c && d) //0111
                return 96;
            else if (a && !b && !c && !d) //1000
                return 112;
            else if (a && !b && !c && d) //1001
                return 128;
            else if (a && !b && c && !d) //1010
                return 160;
            else if (a && !b && c && d) //1011
                return 192;
            else if (a && b && !c && !d) //1100
                return 224;
            else if (a && b && !c && d) //1101
                return 256;
            else if (a && !b && c && d) //1110
                return 320;
        } else if (getId() == MPEG_2){
            if (!a && !b && !c && d) //0001
                return 8;
            else if (!a && !b && c && !d) //0010
                return 16;
            else if (!a && !b && c && d) //0011
                return 24;
            else if (!a && b && !c && !d) //0100
                return 32;
            else if (!a && b && !c && d) //0101
                return 64;
            else if (!a && b && c && !d) //0110
                return 80;
            else if (!a && b && c && d) //0111
                return 56;
            else if (a && !b && !c && !d) //1000
                return 64;
            else if (a && !b && !c && d) //1001
                return 128;
            else if (a && !b && c && !d) //1010
                return 160;
            else if (a && !b && c && d) //1011
                return 112;
            else if (a && b && !c && !d) //1100
                return 128;
            else if (a && b && !c && d) //1101
                return 256;
            else if (a && !b && c && d) //1110
                return 320;
        }
        return -1;
    }

    public int getFrequency(){
        boolean a = _bitset.get(20);
        boolean b = _bitset.get(21);

        if (getId() == MPEG_1){
            if (!a && !b)
                return 44100;
            else if (!a && b)
                return 48000;
            else if (!a && b)
                return 32000;
        }else if (getId() == MPEG_2){
            if (!a && !b)
                return 22050;
            else if (!a && b)
                return 24000;
            else if (a && !b)
                return 16000;
        }
        return -1;
    }

    public boolean isPadding(){
        return _bitset.get(22);
    }

    public int getChannelMode(){
        boolean a = _bitset.get(24);
        boolean b = _bitset.get(25);
        if (!a && !b)
            return CHANEL_MODE_STEREO;
        else if (!a && b)
            return CHANEL_MODE_JOIN_STEREO;
        else if (a && !b)
            return CHANEL_MODE_DUAL_CHANEL;
        else if (a && b)
            return CHANEL_MODE_MONO;
        else
            return -1;
    }

    public boolean isCopyrighted(){
        return _bitset.get(28);
    }

    public boolean isOriginal(){
        return _bitset.get(29);
    }
}
