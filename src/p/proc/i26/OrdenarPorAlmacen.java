package p.proc.i26;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: JPB
 * Date: 24/05/16
 * Time: 14:11
 */
public class OrdenarPorAlmacen {
    private static long orderId = 4000000137L;
    private static long orderIdAction = orderId + 1000000000L;

    private static final String archEntrada = "D:\\RootBuild\\int26\\Copia de Prueba Performance.csv";
    private static final String archSalida = "D:\\RootBuild\\int26\\salida.txt";
    private static List<String> warehouses;     // para mantener el orden
    private static Map<String, List<Registro>> entrada;
    private static long contEntrada;

    public static void main(String[] args) throws IOException {
        leerEntrada();
        generarSalida();
        System.out.println("Listo");
    }

    private static void generarSalida() throws IOException {
        List<String> salida = new ArrayList<String>();
        salida.add("warehouseID;MaterialID;orderID;orderActionID;resourceType;resourceID");
        boolean seguir = true;
        int indice = 0;
        while (seguir) {
            seguir = false;
            for (String warehouse : warehouses) {
                List<Registro> registros = entrada.get(warehouse);
                if (registros.size() > indice){
                    seguir = true;
                    Registro regSalida = generarSalida(registros.get(indice));
                    salida.add(regSalida.generarSalida());
                }
            }
            indice++;
        }
        if (contEntrada != salida.size() -1){
            System.out.println("Entrada != salida");
            System.out.println("contEntrada = " + contEntrada);
            System.out.println("salida = " + (salida.size() -1));
        }
        UtilFile.guardartArchivoPorLinea(new File(archSalida),  salida, true);
    }

    private static Registro generarSalida(Registro registro) {
        registro.setOrderID(orderId+"");
        registro.setOrderActionID(orderIdAction + "");
        registro.setResourceType("ICCID");

        orderId++;
        orderIdAction++;
        return registro;
    }

    private static void leerEntrada() throws IOException {
        entrada = new HashMap<String, List<Registro>>();
        warehouses = new ArrayList<String>();

        List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(archEntrada);
        for (String linea : archivoPorLinea) {
            Registro reg = Registro.parsear(linea);
            List<Registro> registrosXWarehouse = entrada.get(reg.getWarehouseID());
            if (registrosXWarehouse == null){
                registrosXWarehouse = new ArrayList<Registro>();
                entrada.put(reg.getWarehouseID(), registrosXWarehouse);
                warehouses.add(reg.getWarehouseID());
            }
            registrosXWarehouse.add(reg);
            contEntrada++;
        }
    }
}
