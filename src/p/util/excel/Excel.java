package p.util.excel;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Excel {
    private static final Logger logger = Logger.getLogger(Excel.class);

    public static void guardarExcel(XSSFWorkbook excel, String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(filePath));//"D:\\TMP\\howtodoinjava_demo.xlsx"
        excel.write(out);
        out.close();
        logger.info("Se guardó el archivo de salida en " + filePath);
    }

    public static XSSFWorkbook crearExcel(String nombre, List<List<Object>> datos){
        XSSFWorkbook excel = new XSSFWorkbook();
        agregarHoja(excel,  nombre, datos);
        return excel;
    }


    public static void agregarHoja(XSSFWorkbook workbook, String nombre, List<List<Object>> datos){
        XSSFSheet sheet = workbook.createSheet(nombre);

        int rownum = 0;
        for (List<Object> fila : datos) {
            Row row = sheet.createRow(rownum++);
            int cellnum = 0;
            for (Object dato : fila) {
                Cell cell = row.createCell(cellnum++);
                if (dato instanceof String)
                    cell.setCellValue((String) dato);
                else if (dato instanceof Integer)
                    cell.setCellValue((Integer) dato);
                else if (dato instanceof Date)
                    cell.setCellValue(parse((Date) dato));
                else {
                    if (dato != null)
                        cell.setCellValue(dato.toString());
                    else
                        cell.setCellValue(""); //Reemplazo null por ""
                    System.out.println("Ver tipo: " + dato.getClass());
                }
            }
        }
    }

    public static void main(String[] args) {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
//        XSSFSheet sheet2 = workbook.createSheet("Employee Data2");
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[]{1, "Amit", "Shukla"});
        data.put("3", new Object[]{2, "Lokesh", "Gupta"});
        data.put("4", new Object[]{3, "John", "Adwards"});
        data.put("5", new Object[]{4, "Brian", "Schultz"});
        data.put("6", new Object[]{5, new Date(), "Schultz"});

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
                else if (obj instanceof Date)
                    cell.setCellValue(parse((Date) obj));
                else {
                    if (obj != null)
                        cell.setCellValue(obj.toString());
                    else
                        cell.setCellValue(""); //Reemplazo null por ""
                    System.out.println("Ver tipo: " + obj.getClass());
                }
            }
        }
        try {
//            org.apache.crimson.tree.XmlDocument
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("D:\\TMP\\howtodoinjava_demo.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String parse(Date d){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return df.format(d);
    }
}