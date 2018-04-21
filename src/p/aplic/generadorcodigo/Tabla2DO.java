package p.aplic.generadorcodigo;

import org.apache.log4j.Logger;
import p.gui.JTextArea_;
import p.util.Constantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tabla2DO {//multiple top level classes
    private static final Logger logger = Logger.getLogger(Tabla2DO.class);

    private JFrame frame = new JFrame("JP");
    private JTextArea_ textArea = new JTextArea_();
    private JButton btnOrdenar = new JButton();
    private JButton btnSQL2DO = new JButton("SQL -> DO");
    private static final String TAB = "    ";
    private String nuevaLinea = "";
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
     * Son los atributos que se usan en la clase DO
     */
    private java.util.List propiedades;
    private static final String CAMPO_ID = "id";

    public static void main(String[] args){
        new Tabla2DO();
    }

    public Tabla2DO(){
        nuevaLinea = "\n";
        try{
            jbInit();
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void jbInit() throws Exception{
        textArea.setText("");
        frame.getContentPane().setLayout(gridBagLayout1);
        btnOrdenar.setMargin(new Insets(2, 1, 2, 1));
        btnOrdenar.setText("Ordenar");
        btnOrdenar.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                String txt = textArea.getText();
                txt = p.util.Util.ordenar(txt);
                textArea.setText(txt);
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);

        btnSQL2DO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sql2do();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, e.toString());
                    String f = "CREATE TABLE FCT_DEF_CONDICIONES_FACT\n" +
                            "(\n" +
                            "  DCF_CONDICION_FACTURACION_ID   VARCHAR2(6)  NOT NULL,\n" +
                            "  DCF_PERIODICIDAD_CANTIDAD      NUMBER(10),\n" +
                            "  CONSTRAINT PK_CFID PRIMARY KEY (DCF_CONDICION_FACTURACION_ID)\n" +
                            ")\n" + "(CONSTRAINT opcional)";
                    JOptionPane.showMessageDialog(null,
                            "La entrada debe tener la forma: " + f);
                }
            }
        });
        frame.getContentPane().add(scrollPane,
                new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(13, 16, 0, 11), 474, 439));
        frame.getContentPane().add(btnOrdenar,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 16, 5, 0), 2, -4));
        frame.getContentPane().add(btnSQL2DO,
                new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(0, 0, 5, 25), 14, -4));
        
        frame.pack();
        frame.setVisible(true);
    }


    private final String PAQUETE = "crm.appl.fct.XXX.domain";
    static final java.util.List IMPORTS = new ArrayList();
    static{
        IMPORTS.add("crm.fwk.annotations.stubProcessor.FwkPortableObject");
        IMPORTS.add("javax.persistence.*");
        IMPORTS.add("crm.fwk.core.persistence.FwkDefaultDO");
    }
    static final java.util.List<String> ANOTACIONES_DE_CLASE = new ArrayList<String>();
    static{
        ANOTACIONES_DE_CLASE.add("@Entity");
        ANOTACIONES_DE_CLASE.add("@FwkPortableObject");
    }

    void sql2do() {
        propiedades = new ArrayList();
        StringBuffer salida = new StringBuffer();
        String constraint = null;

        //paquete + imports
        salida.append("package ").append(PAQUETE).append(";")
                .append(nuevaLinea).append(nuevaLinea);
        for(Object imp : IMPORTS) {
            salida.append("import ").append(imp).append(";").append(nuevaLinea);
        }
        salida.append(nuevaLinea);

        //anotaciones de la clase
        for(String s : ANOTACIONES_DE_CLASE) {
            salida.append(s).append(nuevaLinea);
        }

        String txt = textArea.getText().trim();
        StringTokenizer st = new StringTokenizer(txt, "\n", false);
        String linea = st.nextToken();//CREATE TABLE FCT_DEF_CONDICIONES_FACT
        linea = linea.replaceFirst("CREATE TABLE ", "").trim();

        //si termina con parentesis => lo borro de la línea
        if (linea.endsWith("("))
            linea = linea.substring(0, linea.length()-1).trim();
        else //si no, debe estar en la línea siguiente
            st.nextToken();//(

        salida.append("@Table(name=\"" + linea + "\")").append(nuevaLinea);
        linea = formatoClase(linea);

        //definición de la clase
        salida.append("public class " + linea + "DO").append(nuevaLinea);
        salida.append("        extends FwkDefaultDO{").append(nuevaLinea);
        boolean parar = false;
        while (st.hasMoreTokens() && !parar) {
            linea = st.nextToken().trim();

            if (linea.startsWith(")") || linea.startsWith("CONSTRAINT")){
                parar = true;
                constraint = parsearConstraint(linea);
            }
            else
                agregarAtributo(linea);
        }

        //Si la PK es simple, cambio el nombre del campo de la pk x "id"
        if (constraint != null){
            for(Object propiedade : propiedades) {
                Propiedad pr = (Propiedad) propiedade;
                if (pr.db.equals(constraint))
                    pr.nombre = CAMPO_ID;
            }
        }

        //agrego las propiedades al fuente
        for(Object propiedade : propiedades) {
            Propiedad pr = (Propiedad) propiedade;
            agregarDefinicion(salida, pr);
        }
        salida.append(nuevaLinea).append(nuevaLinea);
        //agrego los set/get
        for(Object propiedade : propiedades) {
            Propiedad pr = (Propiedad) propiedade;
            agregarSetGet(salida, pr);
        }
        agregarImplementacionDeDomainObjectId(salida);
        salida.append("}").append(nuevaLinea);

        //parches
        agregarVersion(salida);
        agregarImports(salida);

        txt = salida.toString();
        textArea.setText(txt);
        p.util.Util.pegarEnElPortapapeles(txt);
        String msg = "Se pegó el código generado en el portapapeles." +
                nuevaLinea + "Agregar @Id adonde corresponda."+
                nuevaLinea + "Modificar las relaciones con otros objetos."+
                nuevaLinea + "Ver la implementación de 'public Object domainObjectId()'.";
        JOptionPane.showMessageDialog(null, msg);
    }

    private String parsearConstraint(String linea){
        //CONSTRAINT PK_ESTADO_CTE PRIMARY KEY (EC_ESTADO_CLIENTE_ID)
        int d = linea.indexOf("(");
        int h = linea.indexOf(")");
        String campo = linea.substring(d+1, h);
        if (campo.indexOf(", ")>0) //PK compuesta
            return null;
        else
            return campo.trim();
    }
    
    private void agregarImplementacionDeDomainObjectId(StringBuffer sb){
        sb.append(TAB + "public Object domainObjectId(){" + nuevaLinea);
        sb.append(TAB + TAB + "return getId();" + nuevaLinea);
        sb.append(TAB + "}" + nuevaLinea);
    }
    
    /**
     * Si existe una línea con '@Column(name="CRM_TIMESTAMP")'
     * => agrega otra con '@Version' 
     * @param sb
     */
    private void agregarVersion(StringBuffer sb){
        int i = sb.indexOf("@Column(name=\"CRM_TIMESTAMP\")");
        if (i > 0){
            sb.insert(i, "@Version" + nuevaLinea + TAB);
        }
        else{
            i = sb.indexOf("@Column(name=\"SGUTIMESTAMP\")");
            if (i > 0){
                sb.insert(i, "@Version" + nuevaLinea + TAB);
            }
        }
    }
    /**
     * Agrego imports que falten para:
     *      BigDecimal,Date
     * @param sb
     */
    private void agregarImports(StringBuffer sb){
        int i = sb.indexOf("BigDecimal");

        if (i > 0){
            //se usa el BigDecimal
            //Lo agrego después de: import javax.persistence.*;
            String x = "import javax.persistence.*;";
            i = sb.indexOf(x) + x.length();
            sb.insert(i, nuevaLinea + "import java.math.BigDecimal;");
        }

        i = sb.indexOf("Date");
        if (i > 0){
            //se usa el BigDecimal
            //Lo agrego después de: import javax.persistence.*;
            String x = "import javax.persistence.*;";
            i = sb.indexOf(x) + x.length();
            sb.insert(i, nuevaLinea + "import java.util.Date;");
        }
    }

    private void agregarDefinicion(StringBuffer sb, Propiedad pr){
        sb.append(TAB + "private " + pr.tipo + " " + pr.nombre + ";")
                .append(nuevaLinea);
    }

    /**
     * A partir de: DCF_CONDICION_FACTURACION_ID   VARCHAR2(6)  NOT NULL,
     * agrega a la lista de propiedades una nueva.
     * @param linea
     */
    private void agregarAtributo(String linea){

        //busco el primer separador que puede ser un tab o un espacio
        int i = linea.indexOf(" ");
        int t = linea.indexOf(Constantes.TAB_CHARACTER);
        if (i < 0 || (t > -1 && t < i))
            i = t;
        String nv = linea.substring(0, i).trim();
        Propiedad pr = new Propiedad();
        pr.db = nv;
        pr.nombre = formatoVariable(nv);
        linea = linea.substring(i+1).trim();
        if (linea.contains("CHAR") || linea.startsWith("CHAR")){
            pr.tipo = "String";
        }
        else if (linea.contains("DATE") || linea.startsWith("DATE")){
            pr.tipo = "Date";
        }
        else if (linea.contains("NUMBER") || linea.startsWith("NUMBER")){
            String x = linea.substring(linea.indexOf("NUMBER") + "NUMBER".length());
            //busco una coma o )
            boolean seguir = true;
            int j = 0;
            while(seguir){
                char c = x.charAt(j++);
                if (c == ')'){
                    //No tiene decimales
                    //si el NUMBER es > de 9 dígitos poner Long
                    seguir = false;
                    pr.tipo = "Integer";
                }
                else if (c == ','){
                    //tiene que seguir un espacio o un número
                    //si al NUMBER no se le pasa parámetro => uso Integer
                    while(seguir){
                        if (x.length() == j){
                            //al NUMBER no se le pasó parámetro => uso Integer
                            pr.tipo = "Integer";
                            seguir = false;
                            break;
                        }
                        c = x.charAt(j++);
                        if (Character.isDigit(c)){
                            seguir = false;
                            if (c == '0')
                                //es de la forma: NUMBER(6,0)
                                pr.tipo = "Integer";
                            else
                                pr.tipo = "BigDecimal";
                        }
                    }
                    //si el NUMBER es > de 9 dígitos poner Long
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "tipo desconocido = " + linea);
            return ;
        }
        propiedades.add(pr);
    }


    //entrada: FCT_DEF_CONDICIONES_FACT
    //salida:  fctDefCondicionesFact
    private String formatoVariable(String x){
        String res= "";
        StringTokenizer st = new StringTokenizer(x, "_", false);
        String s = st.nextToken();//elimino el prefijo
        if((st.hasMoreTokens()))
            res = st.nextToken().toLowerCase();
        else{
            if ("SGUTIMESTAMP".equals(s)){
                return "timestamp";
            }
            else
                JOptionPane.showMessageDialog(null, "El campo " + x + " no tiene prefijo");
        }
        while (st.hasMoreTokens()) {
            res += mayuscPrimerLetra(st.nextToken());
        }
        return res;
    }

    //entrada: FCT_DEF_CONDICIONES_FACT
    //salida:  FctDefCondicionesFact
    private String formatoClase(String x){
        String res= "";
        StringTokenizer st = new StringTokenizer(x, "_", false);
        while (st.hasMoreTokens()) {
            res += mayuscPrimerLetra(st.nextToken());
        }
        return res;
    }

    //entrada: CONDICIONES
    //salida:  Condiciones
    private String mayuscPrimerLetra(String x){
        String res = x.toLowerCase();
        char c = res.charAt(0);
        if (res.length()>1)
            res = Character.toUpperCase(c) + res.substring(1);
        else
            res = res.toUpperCase();
        return res;
    }

    private void agregarSetGet(StringBuffer salida, Propiedad pr){
        hacerSet(salida, pr);
        salida.append(nuevaLinea);
        hacerGet(salida, pr);
        salida.append(nuevaLinea);
    }

    private void hacerSet(StringBuffer salida, Propiedad pr){
        salida.append(TAB + "public void set");
        salida.append(mayuscula(pr.nombre) + "(" + pr.tipo + " "
                + pr.nombre+ "){" + nuevaLinea);
        salida.append(TAB + TAB + "this." + pr.nombre + " =  " + pr.nombre +
                ";" + nuevaLinea);
        salida.append(TAB + "}" + nuevaLinea);
    }

    private void hacerGet(StringBuffer salida, Propiedad pr){
        //asumo que si la propiedad se llama id => es la clave y le agrego @Id
        if (pr.nombre.equals(CAMPO_ID)){
            salida.append(TAB + "@Id" + nuevaLinea);
        }


        salida.append(TAB + "@Column(name=\"" + pr.db + "\")").append(nuevaLinea);
        salida.append(TAB + "public " + pr.tipo + " get");
        salida.append(mayuscula(pr.nombre) + "(){" + nuevaLinea);
        salida.append(TAB + TAB + "return " + pr.nombre + ";" + nuevaLinea);
        salida.append(TAB + "}" + nuevaLinea);
    }



    //supongo x.length > 1
    private String mayuscula(String x){
        String pl = x.substring(0,1);
        return pl.toUpperCase() + x.substring(1, x.length());
    }

}

class Propiedad{
    public String nombre;
    public String tipo;
    public String db;
}