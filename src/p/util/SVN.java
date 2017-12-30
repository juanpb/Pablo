package p.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import javax.swing.*;
import java.io.File;

/**
 * User: JPB
 * Date: 3/1/11
 * Time: 9:00 AM
 */
public class SVN {
    public static void main(String[] args) throws Exception {
         String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String f = "J:\\java\\ProyectosJava\\Pablo\\config\\pelis\\películas.xml";
		//        f = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\películas.xml";
        commit(f);
    }

    public static void commit(File f) throws Exception {
        commit(f.getAbsolutePath());
    }

    public static void commit(String fileName) throws Exception {
        String buildXml = System.getProperty("buildXml");
        if (buildXml == null){
            String msg = "Falta definir la property buildXml para que se " +
                    "pueda hacer el commit del archivo " + fileName;
            JOptionPane.showMessageDialog(null, msg);
            throw new Exception(msg);
        }
//        File buildFile = new File("J:\\Java\\ProyectosJava\\Pablo\\build.xml");
        File buildFile = new File(buildXml);
        if (!buildFile.exists()){
            String msg = "No existe el archivo " + buildXml + " y no se " +
                    "pueda hacer el commit del archivo " + fileName;
            JOptionPane.showMessageDialog(null, msg);
            throw new Exception(msg);
        }
        Project p = new Project();
        p.setUserProperty("ant.file", buildFile.getAbsolutePath());
        p.init();
        ProjectHelper helper = ProjectHelper.getProjectHelper();
        p.addReference("ant.projectHelper", helper);
        helper.parse(p, buildFile);
        p.setNewProperty("nombre", fileName);
        p.executeTarget("commitFile");
    }
}
