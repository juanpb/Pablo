package p.pruebas;

import java.awt.Color;
import java.io.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class PDF {

    public PDF() throws Exception{

    }

    private String hacerPDF() throws Exception{
        int cantCol = 4;
        String NL = System.getProperty("line.separator");

        Table tabla = new Table(cantCol);
        Document doc = new Document();

        String res = guardarComo();
        PdfWriter.getInstance(doc, new FileOutputStream(res));

        HeaderFooter hf = new HeaderFooter(new Phrase("título"), false);
        HeaderFooter hf2 = new HeaderFooter(new Phrase("título2"), false);
        HeaderFooter hf3 = new HeaderFooter(new Phrase("título3"), false);
        HeaderFooter pie = new HeaderFooter(new Phrase("página"), new Phrase(""));
        hf.setAlignment(HeaderFooter.ALIGN_CENTER);
        hf2.setAlignment(HeaderFooter.ALIGN_RIGHT);
        hf3.setAlignment(HeaderFooter.ALIGN_RIGHT);
        doc.setHeader(hf);
        doc.setHeader(hf2);
        doc.setHeader(hf3);
        doc.setFooter(pie);

        doc.open();

        for (int i = 0; i < 5;i++ ) {
                    doc.add(new Paragraph("Pablo " + i + NL));
        }




        tabla.setBorderWidth((float)0.3);
        tabla.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);

        for (int i = 0; i < cantCol;i++ ) {
            Cell xx = new Cell("CAB-" + i);
            xx.setHeader(true);
            tabla.addCell(xx);
        }

        tabla.endHeaders();

        for (int i = 0; i < 300;i++ ) {
            for (int j = 0; j < cantCol;j++ ) {
                Cell xx;

                if ((i == 5 || i ==12) && j ==3 )
                    xx = new Cell("123456789asadflñskdjfaslñdjfasldjfasdasdfasdfasdfasdasd");
                else
                    xx = new Cell(j + "_" + i);
                xx.setBorderWidth((float)0.2);
                xx.setLeading((float)14);

                xx.setVerticalAlignment(Cell.ALIGN_MIDDLE);
                tabla.addCell(xx);
            }
        }

        doc.add(tabla);
        doc.close();

        return res;
    }

    public static void main(String[] args) throws Exception{

        File f = new File("C:\\TEMP", "z.z");
        f.createNewFile();
        System.out.println("ap: " + f.getAbsolutePath());
        System.out.println("cp: " + f.getCanonicalPath());
        System.out.println(f.getName());
        System.out.println(f.getPath());
        if (true) return ;
        PDF x = new PDF();
        String n = x.hacerPDF();
        Runtime.getRuntime().exec(
            "C:\\Archivos de programa\\Adobe\\Acrobat 5.0\\Acrobat\\Acrobat.exe " + n);

    }
    String guardarComo(){
        long ini = System.currentTimeMillis();
        return "C:\\" + ini + ".pdf";
    }

}