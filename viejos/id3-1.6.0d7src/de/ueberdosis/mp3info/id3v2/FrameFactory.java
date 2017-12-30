package de.ueberdosis.mp3info.id3v2;

import de.ueberdosis.mp3info.Defines;
import de.ueberdosis.util.OutputCtr;

import java.lang.reflect.Constructor;

/**
 * Produces the actual frame-implementation
 *
 * @author Florian Heer
 * @version $Id: FrameFactory.java,v 1.7 2002/12/20 16:01:45 heer Exp $
 */
class FrameFactory implements Defines {
    /**
     * Produces the actual implementation for a given frame.
     *
     * @param frame a generic Frame
     * @param ds    source from where additional data needed for a frame comes from
     * @return implementation of the special frame
     */
    static ID3V2Frame getFrame(ID3V2Frame frame, DataSource ds) {
        try {
            // Gathering the 4 characters ID code
            String frameID = frame.getFrameID();
            OutputCtr.println(4, "Frametype: " + frame.getFrameID());
            // Hopefully finding a class that matches the code
            // FIXME! There needs to be another path to find external
            // implementations for the needed class, so external developers
            // can create implementations without changing the default
            // implementations
            Class frameClass = Class.forName
                    ("de.ueberdosis.mp3info.id3v2.Frame" + frameID);
            Constructor frameConstructor = frameClass.getConstructor
                    (new Class[]{ID3V2Frame.class, DataSource.class});
            // Instantiate the found class
            Object frameObject = frameConstructor.newInstance
                    (new Object[]{frame, ds});
            // Some final test... This shouldn't happen, but you can never
            // be too careful
            if (frameObject instanceof ID3V2Frame)
                return (ID3V2Frame) frameObject;

        } catch (ClassNotFoundException ex) {
            // After the early return..: an error
            OutputCtr.println(2, "Unknown frame type \""
                    + frame.getFrameID()
                    + "\" encountered.\n");
        } catch (NoSuchMethodException ex) {
            // This should never happen as well!
            OutputCtr.println
                    (1, "Frame " + frame.getFrameID() +
                            "cant be instantiated with a Frame and a DataSource.");
            OutputCtr.print(1, ex);
        } catch (Exception ex) {
            // Hu? Why has this gone wrong?
            ex.printStackTrace();
        }
        return frame;
    }

    /**
     * creates a copy of a frame
     *
     * @param frame data to be copied
     * @return a copy or null
     */
    static ID3V2Frame getFrame(ID3V2Frame frame) {
        try {
            String frameID = frame.getFrameID();
            OutputCtr.println(5, "Frametype: " + frame.getFrameID());
            // FIXME! Is it really neccessary to find the class again?
            // shouldn't it be enough to get the class from the
            // given frame?
            // Or could it be that this method gets an internal implementation
            // of a frame and needs to return an external implementation?
            // If the latter: there needs to be another path to look
            // for classes
            Class frameClass = Class.forName
                    ("de.ueberdosis.mp3info.id3v2.Frame" + frameID);
            Constructor frameConstructor = frameClass.getConstructor(new Class[]{ID3V2Frame.class});
            Object frameObject = frameConstructor.newInstance(new Object[]{frame});
            if (frameObject instanceof ID3V2Frame) {
                return (ID3V2Frame) frameObject;
            }

        } catch (ClassNotFoundException ex) {
            // After the early return..: an error
            OutputCtr.println(2, "Unknown frame type \""
                    + frame.getFrameID()
                    + "\" encountered.\n");
        } catch (NoSuchMethodException ex) {
            // This could happen... not all Frames support this yet!
            OutputCtr.println
                    (1, "Frame " + frame.getFrameID() +
                            "cant be instantiated only with a Frame.");
            OutputCtr.print(1, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

}
