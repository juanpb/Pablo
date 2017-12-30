package crm.core.wfl.editor.gui.util;

import crm.core.wfl.editor.bobj.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * User: JPB
 * Date: Feb 23, 2006
 * Time: 11:50:20 AM
 */
public class WflProcessValidator {

    /**
     * Guarda una lista de errores del proceso que se pasó como parámetro al
     * método validate (la última vez que se lo llámo).
     */
    static List<String> errors = null;

    public static List<String> getErrors(){
        return errors;
    }

    /**
     * Valida el proceso, genera mensajes de error y elimina las pre y las
     * posfunciones que tengan en sus extremos estados y funciones no definidas.
     * No valida nada sobre los paralelismos (estos se validan cuando se
     * agregan al container).
     * @param process
     */
    public static boolean validate(WflProcessBObj process)
    {
        errors = new ArrayList<String>();
        List wflBObjs = process.getWflBObjs();

        //Se debe haber definido el id, versión y name del proceso
        if (process.getId() == null || process.getId().trim().equals(""))
            errors.add("No está definido el Id del proceso");
        if (process.getName() == null || process.getName().trim().equals(""))
            errors.add("No está definido el Nombre del proceso");
        if (process.getVersion() == null || process.getVersion().trim().equals(""))
            errors.add("No está definido la versión del proceso");
        String trace = process.getTrace();
        if (trace != null){
            trace = trace.trim();
            if (!(trace.equals(""))
                    && !trace.equalsIgnoreCase("n")
                    && !trace.equalsIgnoreCase("s")){
                errors.add("Está mal definido el processTrace. (Se le asigna" +
                        " 'Generar seguimientos')");
                process.setTrace(" ");
            }
        }
        List<WflBObj> states = process.getWflBObjs(WflStateBObj.class);
        List<WflMarkBObj> marks = process.getMarks();
        List<WflBObj> functions = process.getWflBObjs(WflFunctionBObj.class);
        List prefunctions = process.getWflBObjs(WflPreFunctionBObj.class);
        List posfunctions = process.getWflBObjs(WflPosFunctionBObj.class);

        validateStates(states);
        validateFunctions(functions);
        validatePrefunctions(prefunctions, functions);
        validatePosfunctions(posfunctions, functions);
        validateRepeatedId(states, functions);
        validatePrefunctions2(prefunctions, states, functions, wflBObjs);
        validatePosfunctions2(posfunctions, functions, states, wflBObjs);
        validateMarks(marks, states);

        return errors.size() > 0;
    }

    //Los hitos deben tener id y nombre definidos.
    //Todos los hitos definidos en los estados deben estar definidos
    //como hitos (estar en marks).
    //La acción de los WflStateMarkBObj debe ser un valor válido.
    //Si un hito es 'no borrable' => en los estados no puede estar como baja.
    private static void validateMarks(List marks, List states) {
        //Los hitos deben tener id y nombre definidos.
        validateMarksAttributes(marks);

        //Todos los hitos definidos en los estados deben estar definidos
        //como hitos (estar en marks).
        for (int i = 0; i < states.size(); i++) {
            WflStateBObj stateBo = (WflStateBObj) states.get(i);
            List milesStates = stateBo.getStateMarks();
            List<WflStateMarkBObj> toDelete = new ArrayList<WflStateMarkBObj>();
            for (int j = 0; j < milesStates.size(); j++) {
                WflStateMarkBObj stateMark = (WflStateMarkBObj) milesStates.get(j);
                WflMarkBObj mBO = stateMark.getMark();

                if (!marks.contains(mBO)){
                    String s = "Al estado " + stateBo.getId() + " se le " +
                            "asignó un Hito no definido.";
                    errors.add(s);
                    toDelete.add(stateMark);
                }
                else{
                    String action = stateMark.getAction();
                    if (action == null
                            || (action.equals(WflStateMarkBObj.ALTA)
                            && action.equals(WflStateMarkBObj.BAJA)
                            && action.equals(WflStateMarkBObj.MODIFICACION))){
                        String s = "El estado " + stateBo.getId() + " tiene " +
                                "definido un Hito con una acción inválida " +
                                "(Se le asigna la acción 'Modificación')" ;
                        errors.add(s);
                    }
                    else if (!mBO.isDeletable()
                            && stateMark.getAction().equals(WflStateMarkBObj.BAJA)){
                        String s = "El estado " + stateBo.getId() + " tiene " +
                                "definido un Hito con acción 'Baja', pero el hito no " +
                                "es borrable (Se le asigna la acción 'Modificación')" ;
                        errors.add(s);
                        stateMark.setAction(WflStateMarkBObj.MODIFICACION);
                    }
                }
            }
            for (int j = 0; j < toDelete.size(); j++) {
                WflStateMarkBObj x = (WflStateMarkBObj) toDelete.get(j);
                milesStates.remove(x);
            }
        }

    }

    /**
     * Elimina los hitos que no tengan id
     * @param marks
     */
    private static void validateMarksAttributes(List marks) {
        List<WflMarkBObj> toRemove = new ArrayList<WflMarkBObj>();
        //Los hitos deben tener id y nombre definidos.
        for (int i = 0; i < marks.size(); i++) {
            WflMarkBObj m = (WflMarkBObj) marks.get(i);
            String id = m.getId();
            String n = m.getName();
            if (id == null || id.trim().equals("")){
                String s = "Existe un Hito al cual no se le definió bien el " +
                        "'id' (Este hito se ha eliminado)";
                errors.add(s);
                toRemove.add(m);
            }
            else if (n == null || n.trim().equals("")){
                String s = "Al Hito " + id + " no se le definió el 'nombre' " +
                        "(Se le asigna el mismo valor que el id";
                m.setName(id);
                errors.add(s);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            WflMarkBObj m = (WflMarkBObj) toRemove.get(i);
            marks.remove(m);
        }
    }

    private static void validatePosfunctions2(List posfunctions, List functions,
                                              List states, List wflBObjs) {
        //Las posfunctions deben tener referencias a estados y funciones existentes
        //Elimina de wflBObjs las posfunciones con problemas

        for (int i = 0; i < posfunctions.size(); i++) {
            WflPosFunctionBObj p = (WflPosFunctionBObj) posfunctions.get(i);
            String source = p.getSourceId();
            String target = p.getTargetId();
            boolean remove = false;
            if (source == null){
                errors.add("Existe una posfunción a la cual no se le definió " +
                        "la función asociada");
                remove = true;
            }
            else{
                if (!contains(functions, source)){
                    errors.add("Existe una posfunción a la cual se le " +
                            "asoció una función que no fue definida");
                    remove = true;
                }
            }
            if (target == null){
                errors.add("Existe una posfunción a la cual no se le definió " +
                        "el estado asociado");
                remove = true;
            }
            else{
                if (!contains(states, target)){
                    errors.add("Existe una posfunción a la cual se le " +
                            "asoció un estado que no fue definido");
                    remove = true;
                }
            }
            if (remove)
                wflBObjs.remove(p);
        }
    }

    private static void validatePrefunctions2(List prefunctions, List states,
                                              List functions, List wflBObjs) {
        //Las prefunciones deben tener referencias a estados y funciones existentes
        //Elimina de wflBObjs las prefunciones con problemas

        for (int i = 0; i < prefunctions.size(); i++) {
            WflPreFunctionBObj p = (WflPreFunctionBObj) prefunctions.get(i);
            String source = p.getSourceId();
            String target = p.getTargetId();
            boolean remove = false;
            if (source == null){
                errors.add("Existe una prefunción a la cual no se le definió " +
                        "el estado asociado");
                remove = true;
            }
            else{
                if (!contains(states, source)){
                    errors.add("Existe una prefunción a la cual se le " +
                            "asoció un estado que no fue definido");
                    remove = true;
                }
            }
            if (target == null){
                errors.add("Existe una prefunción a la cual no se le definió " +
                        "la función asociada");
                remove = true;
            }
            else{
                if (!contains(functions, target)){
                    errors.add("Existe una prefunción a la cual se le " +
                            "asoció una función que no fue definida");
                    remove = true;
                }
            }
            if (remove)
                wflBObjs.remove(p);
        }
    }

    private static void validatePosfunctions(List posfunctions, List functions) {
        //Valida que si la posfunción tiene máscara => tenga valor
        // y que todas las funciones tengan por lo menos una posfunción

        boolean maskWithoutValue = false;
        Set<String> set = new HashSet<String>(functions.size());
        for (int i = 0; i < posfunctions.size(); i++) {
            WflPosFunctionBObj p = (WflPosFunctionBObj) posfunctions.get(i);
            if (WflPosFunctionBObj.MASK_TYPE_AND.equals(p.getMaskType())
                    ||WflPosFunctionBObj.MASK_TYPE_OR.equals(p.getMaskType())){
                if (p.getMaskValue() == null || p.getMaskValue().trim().equals(""))
                    maskWithoutValue = true;
            }
            set.add(p.getSourceId());
        }
        if (maskWithoutValue)
            errors.add("Hay, por lo menos, una posfunción que tiene definida una " +
                    "máscara, pero no tiene definido el valor de la misma");

        for (int i = 0; i < functions.size(); i++) {
            WflFunctionBObj f = (WflFunctionBObj) functions.get(i);
            if (!set.contains(f.getBObjId()))
                errors.add("La función " + f.getId() + " no tiene asociada " +
                        "ninguna posfunción");
        }
    }

    private static void validateRepeatedId(List<WflBObj> states, List<WflBObj> functions) {
        //No debe haber id repetidos
        List<WflBObj> stAndFu = new ArrayList<WflBObj>(states);
        stAndFu.addAll(functions);
        Set<String> set = new HashSet<String>(stAndFu.size());
        for (int i = 0; i < stAndFu.size(); i++) {
            WflBObj bo = (WflBObj) stAndFu.get(i);
            String id = bo.getBObjId();
            if (id != null && !id.trim().equals("")){
                boolean b = set.add(id);
                if (!b)
                    errors.add("Existe un id repetido: "+id);
            }
        }
    }

    private static void validatePrefunctions(List prefunctions, List functions) {
        //Valida que los estados no tengan más que una prefunción
        //y que todas las funciones tengan por lo menos una prefunción

        Set<String> states = new HashSet<String>(prefunctions.size());
        Set<String> functionsSet = new HashSet<String>(prefunctions.size());
        for (int i = 0; i < prefunctions.size(); i++) {
            WflPreFunctionBObj p = (WflPreFunctionBObj) prefunctions.get(i);
            String id = p.getSourceId();
            if (id != null && !id.trim().equals("")){
                boolean b = states.add(id);
                if (!b)
                    errors.add("El estado " + id + " tiene más de una prefunción");
            }
            functionsSet.add(p.getTargetId());
        }

        for (int i = 0; i < functions.size(); i++) {
            WflFunctionBObj f = (WflFunctionBObj) functions.get(i);
            if (!functionsSet.contains(f.getId())){
                errors.add("La función " + f.getId() + " no tiene asociada " +
                        "ninguna prefunción");
            }
        }
    }

    private static void validateStates(List states) {
        //Los estados deben tener id, nombre y si tienen máscara deben tener valor
        boolean withoutId = false, withoutName = false, withoutValor = false;
        for (int i = 0; i < states.size(); i++) {
            WflStateBObj s = (WflStateBObj) states.get(i);
            if (s.getId() == null || s.getId().trim().equals(""))
                withoutId = true;
            if (s.getName() == null || s.getName().trim().equals(""))
                withoutName = true;
            if (WflStateBObj.MASK_TYPE_AND.equals(s.getMaskType())
                    || WflStateBObj.MASK_TYPE_OR.equals(s.getMaskType())){
                        if (s.getMaskValue() == null || s.getMaskValue().trim().equals(""))
                            withoutValor = true;
            }
        }
        if (withoutId)
            errors.add("Hay, por lo menos, un estado que no tiene definido el Id");
        if (withoutName)
            errors.add("Hay, por lo menos, un estado que no tiene definido el Nombre");
        if (withoutValor)
            errors.add("Hay, por lo menos, un estado que tiene definida una " +
                    "máscara, pero no tiene definido el valor de la misma");
    }

    private static void validateFunctions(List functions) {
        //Las funciones deben tener id, nombre, clase, ExecutionType y
        // ExecutionSynchro
        boolean withoutId = false, withoutName = false, withoutClass = false;
        boolean withoutET = false, withoutES = false;
        for (int i = 0; i < functions.size(); i++) {
            WflFunctionBObj s = (WflFunctionBObj) functions.get(i);
            String id = s.getId();
            if (id == null || id.trim().equals(""))
                withoutId = true;
            if (s.getName() == null || s.getName().trim().equals("")){
                if (id != null && !id.trim().equals(""))
                    errors.add("La función "+ id + " no tiene definido el Nombre");
                else
                    withoutName = true;
            }

            if (s.getFunctionClass() == null || s.getFunctionClass().trim().equals("")){
                if (id != null && !id.trim().equals(""))
                    errors.add("La función "+ id + " no tiene definida la Clase");
                else
                    withoutClass = true;
            }
            String es = s.getExecutionSynchro();
            if (!WflFunctionBObj.EXECUTION_SYNCHRO_ASYNCHRONOUS.equals(es)
                    && !WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC.equals(es)){
                if (id != null && !id.trim().equals("")){
                    errors.add("La función "+ id + " no tiene bien definido " +
                            "el Execution Synchro.\n (Se le asigna Sincrónica)");
                    s.setExecutionSynchro(WflFunctionBObj.EXECUTION_SYNCHRO_SYNCHRONIC);
                }
                else
                    withoutES = true;
            }
            String et = s.getExecutionType();
            if (!WflFunctionBObj.EXECUTION_TYPE_CLIENT_INTERACTIVE.equals(et)
                    && !WflFunctionBObj.EXECUTION_TYPE_SERVER_ASYNCHRONOUS.equals(et)
                    && !WflFunctionBObj.EXECUTION_TYPE_SPECIAL_PROCESS.equals(et)
                    && !WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC.equals(et)){
                if (id != null && !id.trim().equals("")){
                    errors.add("La función "+ id + " no tiene bien definido " +
                            "el Execution Type.\n (Se le asigna Server sincrónica)");
                    s.setExecutionType(WflFunctionBObj.EXECUTION_TYPE_SERVER_SYNCHRONIC);
                }
                else
                    withoutET = true;
            }
        }
        if (withoutId)
            errors.add("Hay, por lo menos, una función que no tiene definido el Id");
        //Los siguientes mensajes se muestran si la función no tiene definido
        //el id.
        if (withoutName)
            errors.add("Hay, por lo menos, una función que no tiene definido el Nombre");
        if (withoutClass)
            errors.add("Hay, por lo menos, una función que no tiene definida la Clase");
        if (withoutET)
            errors.add("Hay, por lo menos, una función que no tiene o tiene mal" +
                    "definido el Execution Type");
        if (withoutES)
            errors.add("Hay, por lo menos, una función que no tiene o tiene mal" +
                    "definido el Execution Synchro");
    }

    private static boolean contains(List list, String id) {
        for (int i = 0; i < list.size(); i++) {
            WflBObj bObj = (WflBObj) list.get(i);
            if (bObj.getBObjId().equals(id))
                return true;
        }
        return false;
    }
}
