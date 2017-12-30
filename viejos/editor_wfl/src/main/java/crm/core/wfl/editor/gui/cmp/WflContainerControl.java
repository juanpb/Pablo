package crm.core.wfl.editor.gui.cmp;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;

import crm.core.wfl.editor.bobj.WflBObj;
import crm.core.wfl.editor.bobj.WflConnectorBObj;
import crm.core.wfl.editor.bobj.WflFunctionBObj;
import crm.core.wfl.editor.bobj.WflLocationBObj;
import crm.core.wfl.editor.bobj.WflMarkBObj;
import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.bobj.WflPreFunctionBObj;
import crm.core.wfl.editor.bobj.WflProcessBObj;
import crm.core.wfl.editor.bobj.WflPropertyBObj;
import crm.core.wfl.editor.bobj.WflStateBObj;
import crm.core.wfl.editor.bobj.WflStateMarkBObj;
import crm.core.wfl.editor.gui.dlg.WfParallelismDlg;
import crm.core.wfl.editor.gui.dlg.WflFunctionTraceDlg;
import crm.core.wfl.editor.gui.dlg.WflMarkDlg;
import crm.core.wfl.editor.gui.dlg.WflProcessDefinitionDlg;
import crm.core.wfl.editor.gui.dlg.WflZoomFrame;
import crm.core.wfl.editor.gui.mnu.WflConnectorMnu;
import crm.core.wfl.editor.gui.mnu.WflContainerMnu;
import crm.core.wfl.editor.gui.mnu.WflFunctionMnu;
import crm.core.wfl.editor.gui.mnu.WflMarksBar;
import crm.core.wfl.editor.gui.mnu.WflMenuBar;
import crm.core.wfl.editor.gui.mnu.WflPosFunctionMnu;
import crm.core.wfl.editor.gui.mnu.WflStateMnu;
import crm.core.wfl.editor.gui.mnu.WflStateShortCutMnu;
import crm.core.wfl.editor.gui.mnu.WflToolBar;
import crm.core.wfl.editor.gui.util.WflConstants;
import crm.core.wfl.editor.gui.util.WflIOUtil;
import crm.core.wfl.editor.gui.util.WflProcessValidator;
import crm.core.wfl.editor.gui.util.WflUtil;
import crm.core.wfl.editor.gui.util.WflUtility;
import crm.core.wfl.editor.xml.WflXMLUtil;

/**
 * User: JPB
 * Date: Feb 1, 2006
 * Time: 10:34:47 AM
 */
public class WflContainerControl
        implements MouseListener, MouseMotionListener{

    private WflProcessDefinitionContainer container = null;
    private WflMenuBar menuBar = null;
    private WflToolBar toolBar = null;
    
    private WflZoomFrame zoomFrame = null;
    private boolean zoomFrameActive = false;

    private WflComponent topSelectedComponent = null;
    private WflComponent leftSelectedComponent = null;

    /**
     * Guarda una lista donde cada elemento es una lista que contiene los
     * componentes de un paralelismo dinámico.
     */
    private List<List<WflComponent>> paralelism = new ArrayList<List<WflComponent>>();
    /**
     * Usado para evitar circuitos cuando se recorre el grafo
     */
    private Set<WflComponent> antiOverflow = null;
    private Set<WflComponent> antiOverflow2 = null;

    /**
     * Punto usado de referencia para cuando se mueve un componente. Éste es el
     * último punto para el cual se calculó el movimiento de los componentes
     * seleccionados.
     */
    private Point pointForMove;

    private String processId;
    private String processName;
    private String processVersion;
    private String processTrace;
    private String processComment;
    private String processSecurityObject;
    private static final int SEPARATOR_STATE_FUNCTION = 30;
    private boolean addingMark = false;
    private WflMarkBObj mark = null;
    private String currentDirectoryPath = null;
    private List<WflPropertyBObj> properties = new ArrayList<WflPropertyBObj>();
    private List constraints = new ArrayList();    

    public WflContainerControl() {
    }

    public void setContainer(WflProcessDefinitionContainer container){
        this.container = container;
    }
    
    /**
     * Devuelve la posicion x,y de la esquina superior izquierda
     * que se esta mostrando en el viewport.
     * @return Coordenada de la posicion.
     */
    public Point getTopLeftCorner() {
    	return this.container.getAssociatedViewport().getViewPosition();
    }

    public void setModified(boolean b){
        WflUtility.setModified(b);
    }
    /**************************************************************************/
    /** manejo de eventos del mouse                                           */
    /**************************************************************************/

    public void mouseClicked(MouseEvent e) {
        Object s = e.getSource();

        if(e.getButton()==MouseEvent.BUTTON1){
            if (s instanceof WflComponent){
                WflComponent wc = (WflComponent)s;
                if (!e.isControlDown())
                    deselectAll(wc);
                //wc ya fue marcado como seleccionado en 'mousePressed(MouseEvent e)'

                if (e.getClickCount() == 2){
                    edit(wc);
                }
            }
        }
        else if (e.getButton()==MouseEvent.BUTTON3){

            //Menúes contextuales
            if (container == s ){
                WflContainerMnu mnu = new WflContainerMnu(this);
                mnu.show(container,  e.getX(), e.getY());
            }
            else if (s instanceof WflStateShortCut ){
            	WflStateShortCut state = (WflStateShortCut)s;
                WflStateShortCutMnu mnu = new WflStateShortCutMnu(this, state);
                mnu.show(state,  e.getX(), e.getY());
            }
            else if (s instanceof WflState ){
                WflState state = (WflState)s;
                WflStateMnu mnu = new WflStateMnu(this, state);
                mnu.setEnableAddParallelism(isParallelismValid(state));

                //Si este estado forma parte de un paralelismo => se habilitan
                //los ítems para editarlo/quitarlo, si no se deshabilitan
                mnu.setEnableEditParallelism(state.isInParallelism());

                mnu.show(state,  e.getX(), e.getY());
            }
            else if (s instanceof WflFunction ){
                WflFunction f = (WflFunction)s;
                WflFunctionMnu mnu = new WflFunctionMnu(this, f);

                //Si este estado forma parte de un paralelismo => se habilitan
                //los ítems para editarlo/quitarlo, si no se deshabilitan
                mnu.setEnableEditParallelism(f.isInParallelism());

                mnu.show(f,  e.getX(), e.getY());
            }
            else if (s instanceof WflPreFunction ){
                WflConnector f = (WflConnector)s;
                WflConnectorMnu mnu = new WflConnectorMnu(this, f, false);

                //Si este estado forma parte de un paralelismo => se habilitan
                //los ítems para editarlo/quitarlo, si no se deshabilitan
                mnu.setEnableEditParallelism(f.isInParallelism());
                mnu.setEnableRemoveVertice(isRemoveVerticeValid(f));
                mnu.show(f,  e.getX(), e.getY());
            }
            else if (s instanceof WflPosFunction ) {
            	WflPosFunction pf = (WflPosFunction)s;
                WflPosFunctionMnu mnu = new WflPosFunctionMnu(this, pf, true);

                //Si este estado forma parte de un paralelismo => se habilitan
                //los ítems para editarlo/quitarlo, si no se deshabilitan
                mnu.setEnableEditParallelism(pf.isInParallelism());
                mnu.setEnableRemoveVertice(isRemoveVerticeValid(pf));
                mnu.show(pf,  e.getX(), e.getY());
            }
        }
    }
    
    /**
     * Verifica si se puede remover un vertice de un conector.
     * @param connector Conector asociado.
     * @return true si se puede remover.
     */
    private boolean isRemoveVerticeValid(WflConnector connector) {
    	return connector.getVertices().size()>0;
    }

    /**
     * Verifica si se puede agregar un paralelismo.
     * Se considera válido agregar paralelismo si hay exactamente 2 WflState
     * seleccionados (uno de los cuales es el recibido como parámetro), si
     * desde el recibido como parámetro se puede llegar hasta el segundo por
     * todos los caminos posibles. Y si en todo_ el camino no hay ninguno que
     * forme parte de un paralelismo.
     * @param stateInitial no puede ser null.
     */
    private boolean isParallelismValid(WflState stateInitial) {
        List sc = container.getSelectedComponents(WflState.class);
        if (sc.size() == 2 && sc.contains(stateInitial)){
            WflState s = (WflState)sc.get(0);
            if (s.equals(stateInitial))
                s = (WflState)sc.get(1);
            boolean b = areConnected(stateInitial, s);
            if (!b)
                return false;

            List list = getWflComponents(stateInitial, s);
            for (int i = 0; i < list.size(); i++) {
                WflComponent wc = (WflComponent) list.get(i);
                if (wc.isInParallelism())
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    /**
     * Arma una lista con todos los wflComp que están en el/los camino entre
     * comp1 y comp2.
     * Se asume que están conectados por todos los caminos. Incluye los extremos.
     * @param comp1
     * @param comp2
     */
    public List<WflComponent> getWflComponents(WflComponent comp1, WflComponent comp2) {
        antiOverflow2 = new HashSet<WflComponent>();
        if (comp1.equals(comp2)){
            List<WflComponent> res = new ArrayList<WflComponent>(1);
            res.add(comp1);
            return res;
        }
        return getWflComponents2(comp1, comp2);
    }

    private List<WflComponent> getWflComponents2(WflComponent comp1, WflComponent comp2) {
        List<WflComponent> list = new ArrayList<WflComponent>();

        //caso base
        if (comp1.getRelatedComponents().contains(comp2)){
            List<WflComponent> res = new ArrayList<WflComponent>(2);
            res.add(comp1);
            res.add(comp2);
            return res;
        }

        if (comp1 instanceof WflState){
            List<WflComponent> rc = comp1.getRelatedComponents(WflPreFunction.class);
            //puede tener a lo sumo una prefunción
            if (rc.size() == 1){
                WflPreFunction o = (WflPreFunction)rc.get(0);
                if (antiOverflow2.contains(o))
                    return null;
                else
                    antiOverflow2.add(o);
                list = getWflComponents2(o, comp2);
            }
            else
                return null;
        }
        else if (comp1 instanceof WflFunction){
            List rc = comp1.getRelatedComponents(WflPosFunction.class);
            //puede tener varias posfunciones
            for (int i = 0; i < rc.size(); i++) {
                WflPosFunction posF = (WflPosFunction)rc.get(i);
                if (antiOverflow2.contains(posF))
                    return null;
                else
                    antiOverflow2.add(posF);
                //para cada camino creo un nuevo antiOverflow2
                Set<WflComponent> temSet = new HashSet<WflComponent>(antiOverflow2);
                List<WflComponent> tem = getWflComponents2(posF, comp2);
                antiOverflow2 = temSet;
                //cuando haya varios caminos acá pude haber componentes
                //repetidos (se podria poner un Set en vez de List)
                list.addAll(tem);
            }
        }
        else if (comp1 instanceof WflPosFunction){
            List rc = comp1.getRelatedComponents(WflState.class);
            //puede tener a lo sumo un state
            if (rc.size() == 1){
                WflState o = (WflState)rc.get(0);
                if (antiOverflow2.contains(o))
                    return null;
                else
                    antiOverflow2.add(o);

                list = getWflComponents2(o, comp2);
            }
        }
        else if (comp1 instanceof WflPreFunction){
            List rc = comp1.getRelatedComponents(WflFunction.class);
            //puede tener a lo sumo una función
            if (rc.size() == 1){
                WflFunction o = (WflFunction)rc.get(0);
                if (antiOverflow2.contains(o))
                    return null;
                else
                    antiOverflow2.add(o);

                list = getWflComponents2(o, comp2);
            }
        }

        if (list == null)
            return null;
        else{
            List<WflComponent> res = new ArrayList<WflComponent>(1 + list.size());
            res.add(comp1);
            res.addAll(list);
            return res;
        }
    }

    /**
     * Calcula si desde el primer componente se puede llegar al segundo por
     * todos los caminos posibles.
     * @param comp1
     * @param comp2
     */
    public boolean areConnected(WflComponent comp1, WflComponent comp2) {
        antiOverflow = new HashSet<WflComponent>();
        return areConnected2(comp1, comp2);
    }

    private boolean areConnected2(WflComponent comp1, WflComponent comp2) {
        if (comp1.equals(comp2))
            return true;
        if (comp1 instanceof WflState){
            List rc = comp1.getRelatedComponents(WflPreFunction.class);
            //puede tener a lo sumo una prefunción
            if (rc.size() == 1){
                WflPreFunction o = (WflPreFunction)rc.get(0);
                if (antiOverflow.contains(o))
                    return false;
                else
                    antiOverflow.add(o);
                return areConnected2(o, comp2);
            }
        }
        else if (comp1 instanceof WflFunction){
            List rc = comp1.getRelatedComponents(WflPosFunction.class);
            //puede tener varias posfunciones
            boolean b = true;
            for (int i = 0; i < rc.size(); i++) {
                WflPosFunction posF = (WflPosFunction)rc.get(i);
                if (antiOverflow.contains(posF))
                    return false;
                else
                    antiOverflow.add(posF);

                //para cada camino creo un nuevo antiOverflow
                Set<WflComponent> tem = new HashSet<WflComponent>(antiOverflow);
                b &= areConnected2(posF, comp2);
                antiOverflow = tem;
            }
            return b;
        }
        else if (comp1 instanceof WflPosFunction){
            List rc = comp1.getRelatedComponents(WflState.class);
            //puede tener a lo sumo un state
            if (rc.size() == 1){
                WflState o = (WflState)rc.get(0);
                if (antiOverflow.contains(o))
                    return false;
                else
                    antiOverflow.add(o);

                return areConnected2(o, comp2);
            }
        }
        else if (comp1 instanceof WflPreFunction){
            List rc = comp1.getRelatedComponents(WflFunction.class);
            //puede tener a lo sumo una función
            if (rc.size() == 1){
                WflFunction o = (WflFunction)rc.get(0);
                if (antiOverflow.contains(o))
                    return false;
                else
                    antiOverflow.add(o);

                return areConnected2(o, comp2);
            }
        }

        return false;
    }

    public void mousePressed(MouseEvent e) {
        if(e.getButton() !=MouseEvent.BUTTON1)
            return ;
        Component c = e.getComponent();
        int maxToMoveTop;
        int maxToMoveLeft;

        if (addingMark){
            addingMark = false;
            try{
                if (c instanceof WflState ){
                    String action = WflStateMarkBObj.ALTA;
                    if (e.isControlDown())
                        action = WflStateMarkBObj.MODIFICACION;
                    else if (e.isShiftDown())
                        action = WflStateMarkBObj.BAJA;
                    addMarkState((WflState)c, action);
                }
                else{
                    String msg = "No se puede agregar el hito. Se debe \n" +
                            "seleccionar el hito y luego el estado.";
                    WflUtil.showErrorMessage(msg);
                }
            }
            finally{
                WflUtility.getCurrentFrame().setCursor(Cursor.getDefaultCursor());
                mark = null;
            }
            return ;
        }


        if (!(c instanceof WflComponent)){
            if (!e.isControlDown())
                deselectAll();
            return ;
        }


        WflComponent compClicked = (WflComponent)c;

        if (!compClicked.isSelected()){

             if (!e.isControlDown()){
                //si no se apretó control => desselecciono todos los componentes
                // salvo el componente donde se hizo clic
                 deselectAll(compClicked);
            }
            compClicked.setSelected(true);
        }
        else if (e.isControlDown()){
            //Si ya estaba seleccionado y se hace clic con control => lo marco
            //como no seleccionado
            compClicked.setSelected(false);
            return ;
        }

        //Guardo el punto donde se hizo clic por si se mueve el mouse y
        // tengo que mover todos los wflComp seleccionados
        Point pC = compClicked.getLocation();
        e.translatePoint(pC.x, pC.y);
        pointForMove = e.getPoint();

        //Busco el componente seleccionado que esté más a la izquierda y el
        // que está más arriba para saber hasta dónde se permite mover
        // los componentes seleccionados.
        maxToMoveTop = Integer.MAX_VALUE;
        maxToMoveLeft = Integer.MAX_VALUE;

        List sc = container.getSelectedComponents();
        for(int i=0; i < sc.size(); i++){
            WflComponent wc = (WflComponent) sc.get(i);
            if(wc.getX() < maxToMoveLeft){
                maxToMoveLeft = wc.getX();
                leftSelectedComponent = wc;
            }
            if(wc.getY() + wc.getHeight() < maxToMoveTop){
                maxToMoveTop = wc.getY();
                topSelectedComponent = wc;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        pointForMove = null;
        leftSelectedComponent = null;
        topSelectedComponent = null;
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    /**************************************************************************/
    /** fin manejo de eventos del mouse Dragged                               */
    /**************************************************************************/

    /**************************************************************************/
    /** manejo de eventos del mouse Dragged                                   */
    /**************************************************************************/

    public void mouseDragged(MouseEvent e) {
        if (pointForMove == null) //pasa si se hace drag con el botón derecho
            return ;

        Object s = e.getSource();
        if (!(s instanceof WflComponent))
            return ;
        WflComponent compClicked = (WflComponent)s;
        Point location = compClicked.getLocation();

        e.translatePoint(location.x, location.y);
        Point newPoint = e.getPoint();
        int x = newPoint.x - pointForMove.x;
        int y = newPoint.y - pointForMove.y;

        //No permito que se arrastre más arriba ni más a la izquierda de los
        //márgenes
        int maxX = leftSelectedComponent.getLocation().x;
        int maxY = topSelectedComponent.getLocation().y;
        if (x < 0 && (x *-1) > maxX ){
            x = -maxX;
        }
        if (y < 0 && (y *-1) > maxY)
            y = -maxY;

        //Muevo todos los componentes seleccionados
        List cs = container.getSelectedComponents();
        
        if(cs.size()==1 && cs.get(0) instanceof WflConnector) {
        	// En este caso, hay que mover el vertice mas cercano si existiera solamente.
        	WflConnector c = (WflConnector) cs.get(0);
            c.translateVertice(pointForMove.x, pointForMove.y, x, y);
        }
        else {
	        for (int i = 0; i < cs.size(); i++) {
	            WflComponent c = (WflComponent) cs.get(i);
	            Point point = c.getLocation();
	            point.translate(x, y);
	            c.setNewPosition(point);
	        }
        }
        setModified(true);

        //Actulizo el nuevo punto de referencia para calcular el movimiento de
        //los componentes
        pointForMove = newPoint;
    }

    /**
     * Se llama cuando se mueve el mouse
     * @param e Evento de mouse
     */
    public void mouseMoved(MouseEvent e) {}
    /**************************************************************************/
    /** fin manejo de eventos del mouse                                       */
    /**************************************************************************/

    /**
     * Deselecciona todos los componentes.
     */
    public void deselectAll() {
        deselectAll(null);
    }
    private void deselectAll(WflComponent except) {
        Iterator i = container.getSelectedComponents().iterator();
        while(i.hasNext()){
            WflComponent wc = (WflComponent)i.next();
            if (wc != except)
                wc.setSelected(false);
        }
    }

    public WflState addState() {
        return addState(0,0);
    }
    

    public WflState addState(int x, int y) {
        WflState state = new WflState();

        container.add(state);
        state.setNewPosition(x,y);

        setModified(true);
        return state;
    }

    public WflState addStateCentered(int x, int y) {
        WflState state = new WflState();

        container.add(state);
        state.setLocationCenter(x,y);

        setModified(true);
        return state;
    }
    
    public void addStateShortCut(WflStateShortCut shortCut) {
    	container.add(shortCut);
    	setModified(true);
    }

    public void addStateShortCut(WflStateShortCut shortCut, int x, int y) {
    	container.add(shortCut);
    	shortCut.setNewPosition(x, y);
    	setModified(true);
    }

    public WflFunction addFunction() {
        return addFunction(0,0);
    }

    /**
     * Crea una instancia de WflFunction, la agrega al container de manera tal
     * que quede centrada en el la posición
     * indicada en los parámetros y devuele esa instancia.
     * @param x
     * @param y
     */
    public WflFunction addFunctionCentered(int x, int y) {
        WflFunction f = new WflFunction();
        container.add(f);
        f.setNewPosition(x,y);
        setModified(true);
        return f;
    }

    /**
     * Crea una instancia de WflFunction, la agrega al container el la posición
     * indicada en los parámetros y devuele esa instancia.
     * @param x
     * @param y
     */
    public WflFunction addFunction(int x, int y) {
        WflFunction f = new WflFunction();
        container.add(f);
        f.setNewPosition(x,y);
        setModified(true);
        return f;
    }

    public WflFunction addFunctionToState(WflState state) {
        Point p = state.getMiddleBottom();
        p.translate( -state.getWidth()/2 ,
                (int) (SEPARATOR_STATE_FUNCTION * container.getZoom()));
        WflFunction function = addFunction(p.x, p.y);

        WflPreFunction pf = new WflPreFunction(state, function);
        container.add(pf);
        pf.update((WflComponent)null);
        return function;
    }

    public void addFunctionAndStateToState(WflState state) {
        WflFunction function = addFunctionToState(state);
        addStateToFunction(function);
    }

    public void addStateToFunction(WflFunction function) {

        //Si la función a la cual se le va a agregar un estado forma parte de
        //un paralelismo, tengo que eliminarlo, por eso antes pido confirmación
        //al usuario
        if (function.isInParallelism()){
            String msg = "Si se agrega acá un estado se va a eliminar el " +
                    "paralelismo de la función. ¿Desea continuar?";
            int i = WflUtil.showConfirmDialog(msg, "Agregar estado");
            if (JOptionPane.YES_OPTION != i)
                return ;
            removeParallelism(function);
        }
        Point p = function.getMiddleBottom();
        p.translate(-function.getWidth()/2 ,
                (int) (SEPARATOR_STATE_FUNCTION * container.getZoom()));
        WflState state = addState(p.x, p.y);

        WflPosFunction pf = new WflPosFunction(function, state);
        container.add(pf);
        pf.update((WflComponent)null);
    }

    /**
     * Obtiene todos los componentes seleccionados de un tipo determinado.
     * @param type Tipo de componente a buscar
     * @return Lista de componentes de ese tipo que fueron seleccionado.
     */
    public List<WflComponent> getSelectedComponents(Class type){
        return container.getSelectedComponents(type);
    }
    public List<WflComponent> getSelectedComponents(){
        return container.getSelectedComponents();
    }

    /**
     * Elimina el paralelismo que comienza en initialState y termina en
     * finalState
     * Los dos WflState recibidos deben estar conectados.
     * @param initialState
     * @param finalState
     */
    private void removeParallelism(WflState initialState, WflState finalState) {
        WflStateBObj bo = (WflStateBObj) initialState.getBObj();
        bo.setParallelism(WflStateBObj.PARALLELISM_NONE);
        bo.setClassName(null);
        bo.setFinalState(null);
        bo = (WflStateBObj) finalState.getBObj();
        bo.setParallelism(WflStateBObj.PARALLELISM_NONE);

        List list = getWflComponents(initialState, finalState);
        for (int i = 0; i < list.size(); i++) {
            WflComponent wc = (WflComponent) list.get(i);
            wc.setInParallelism(false);
        }
        container.repaint();

        //borro de la lista de paralelismo esta cadena
        for (int i = 0; i < paralelism.size(); i++) {
            List cad = (List) paralelism.get(i);
            Object o = cad.get(0);
            if (o.equals(initialState)){
                paralelism.remove(cad);
                break;
            }
        }
        setModified(true);
    }

    /**
     * Agrega un paralelismo que va desde el initialState al finalState.
     * Los dos WflState recibidos deben estar conectados.
     * @param initialState
     * @param finalState
     * @param className
     */
    private void addParallelism(WflState initialState, WflState finalState,
                                String className) {
        List<WflComponent> list = getWflComponents(initialState, finalState);
        paralelism.add(list);
        for (int i = 0; i < list.size(); i++) {
            WflComponent wc = (WflComponent) list.get(i);
            wc.setInParallelism(true, initialState, finalState, className);
        }
        container.repaint();
        setModified(true);
    }

    public void load(WflProcessBObj process) {
        boolean b = WflProcessValidator.validate(process);

        List<WflBObj> wflBObjs = process.getWflBObjs();
        List<WflBObj> conn = new ArrayList<WflBObj>();
        List<WflComponent> wflComps = bObj2Comps(wflBObjs, conn);

        processId = process.getId();
        processName = process.getName();
        processVersion = process.getVersion();
        processComment = process.getComment();
        processSecurityObject = process.getSecurityObject();
        properties = process.getProperties();
        constraints = process.getObjectConstraints();
        
        container.add(wflComps);
        WflMarksBar mb = WflUtility.getMarksBar();
        mb.clean();
        List<WflMarkBObj> ms = process.getMarks();
        for (int i = 0; i < ms.size(); i++) {
            WflMarkBObj obj = (WflMarkBObj) ms.get(i);
            mb.addMark(obj);
        }

        //Armo los conectores y los agrego al container
        for (int i = 0; i < conn.size(); i++) {
            WflConnectorBObj c = (WflConnectorBObj) conn.get(i);

            WflComponent src = container.getWflComponent(c.getSourceId());
            WflComponent tar = container.getWflComponent(c.getTargetId());
            WflConnector wflC;
            if (c instanceof WflPreFunctionBObj) {
				if(src instanceof WflState && tar instanceof WflFunction)
					wflC = new WflPreFunction((WflState) src,
							(WflFunction) tar, (WflPreFunctionBObj) c);
				else {
					throw new RuntimeException("Error: src de tipo " + src.getClass().getName() + "(" + src.getId() + ") tiene que ser un estado y tar de tipo " +
												 tar.getClass().getName() + "(" + tar.getId() + ") tiene que ser una funcion.");
				}
			}
            else {
				if(src instanceof WflFunction && tar instanceof WflState)
					wflC = new WflPosFunction((WflFunction) src,
							(WflState) tar, (WflPosFunctionBObj) c);
				else {
					throw new RuntimeException("Error: tar de tipo " + tar.getClass().getName() + "(" + tar.getId() + ")  tiene que ser un estado y src de tipo " +
												 src.getClass().getName() + "(" + src.getId() + ") tiene que ser una funcion.");
				}
			}
            container.add(wflC);
        }

        //Busco los paralelismos
        List<WflComponent> all = new ArrayList<WflComponent>(wflComps.size() + conn.size());
        all.addAll(wflComps);
        all.addAll(container.getWflComponents(WflConnector.class));
        List<String> errorsP = setParalellism(all);

        //Armo una sola lista con los mensajes de error que pudo generar el
        //validador del proceso y los que pudieron aparecer al asignar los
        //paralelismos. Y los muestro.
        List<String> errors;
        if (b){
            errors = WflProcessValidator.getErrors();
            errors.addAll(errorsP);
        }
        else
            errors = errorsP;
        if (errors.size() >0){
            String msg = "";
            for (int i = 0; i < errors.size(); i++) {
                msg += (String) errors.get(i);
                if (i +1 < errors.size())
                    msg += "\n";
            }
            WflUtil.showErrorMessage(msg);
        }

        setModified(false);
    }

    /**
     * Busca los paralelismos y asigna las propiedades correspondientes a los
     * componentes.
     * @param wflcomp
     */
    private List<String> setParalellism(List wflcomp ) {
        List<String> errors = new ArrayList<String>();
        //Busca en la lista los State que inicien un paralelismo;
        for (int i = 0; i < wflcomp.size(); i++) {
            WflComponent wc = (WflComponent) wflcomp.get(i);
            WflBObj bObj = wc.getBObj();
            if (bObj instanceof WflStateBObj){
                WflStateBObj bo = (WflStateBObj) bObj;
                String dp =  bo.getParallelism();
                if (WflStateBObj.PARALLELISM_START.equals(dp)){
                    String e = setParalellism((WflState) wc, wflcomp);
                    if (e != null)
                        errors.add(e);
                }
            }
        }
        return errors;
    }

    /**
     * Crea un paralelismo que empieza en el estado stateInitial. Para ello
     * busca el estado final en la lista wflcomp,
     * luego obtiene todos los componentes que unen el inicial con el final y
     * les asigna el paralelismo.
     * @param stateInitial
     * @param wflcomp
     * @return null si no hubo problemas o un mensaje con el error.
     */
    private String setParalellism(WflState stateInitial, List wflcomp) {
        WflStateBObj bo = (WflStateBObj) stateInitial.getBObj();
        String classNamePar = bo.getClassName();
        String fs = bo.getFinalState();
        WflState stateFinal = getWflState(fs, wflcomp);
        if (stateFinal == null){
            return "El estado " + stateInitial.getId() + " inicia un " +
                    "paralelismo, pero no se definió correctamente el estado " +
                    "final del paralelismo.";
        }
        boolean b = areConnected(stateInitial,  stateFinal);
        if (!b){
            return "El estado " + stateInitial.getId() + " inicia un " +
                    "paralelismo que termina en el estado " +
                    stateFinal.getId() +" pero estos estados no se encuentran " +
                    "correctamente conectados";
        }
        List<WflComponent> list = getWflComponents(stateInitial, stateFinal);
        paralelism.add(list);

        for (int i = 0; i < list.size(); i++) {
            WflComponent wc = (WflComponent) list.get(i);
            wc.setInParallelism(true, stateInitial, stateFinal, classNamePar);
        }
        return null;
    }

    /**
     * Busca en la lista el WflState que tenga el stateId
     * @param stateId
     * @param wflBObjs
     */
    private WflState getWflState(String stateId, List wflBObjs) {
        for (int i = 0; i < wflBObjs.size(); i++) {
            WflComponent wc = (WflComponent) wflBObjs.get(i);
            if (wc instanceof WflState){
                WflStateBObj bo = (WflStateBObj)wc.getBObj();
                if (stateId.equals(bo.getId()))
                    return (WflState) wc;
            }
        }
        return null;
    }

    public WflComponent getWflComponent(String cmpId){
        return container.getWflComponent(cmpId);
    }

    /**
     * Devuelve una lista de WflComponent y llena la lista de conectores
     * recibida como parámetros.
     *
     * @param wflBObjs
     * @param connectorsBObjs
     */
    private java.util.List<WflComponent> bObj2Comps(List wflBObjs, List<WflBObj> connectorsBObjs) {
        java.util.List<WflComponent> res = new ArrayList<WflComponent>(wflBObjs.size());
        for (int i = 0; i < wflBObjs.size(); i++) {
            WflBObj o = (WflBObj) wflBObjs.get(i);
            WflComponent c = null;
            if (o instanceof WflFunctionBObj) {
                c = new WflFunction((WflFunctionBObj) o);
            } else if (o instanceof WflStateBObj) {
                c = new WflState((WflStateBObj) o);
            } else if (o instanceof WflConnectorBObj) {
                connectorsBObjs.add(o);
            }
            if (c != null)
                res.add(c);
        }
        return res;
    }


    /**
     * Revisa los elementos de la lista a borrar buscando WflComponents que
     * formen parte de algún paralelismo. Si lo encuentra, elimina ese
     * paralelismo.
     * @param toDelete
     */
    private void recalculateParallelism(List toDelete) {
        for (int i = 0; i < toDelete.size(); i++) {
            WflComponent wc = (WflComponent) toDelete.get(i);
            if (wc.isInParallelism()){
                List lis = getParallelism(wc);
                removeParallelism(lis);
            }
        }
    }

    /**
     * Elimina el paralelismo.
     * @param lis
     */
    private void removeParallelism(List lis) {
        //Modifico el BO del estado que inicia el paralelismo
        WflState s = (WflState)lis.get(0);
        WflStateBObj bo = (WflStateBObj) s.getBObj();
        bo.setClassName(null);
        bo.setFinalState(null);
        bo.setParallelism(WflStateBObj.PARALLELISM_NONE);

        //Modifico el BO del estado que finaliza el paralelismo
        s = (WflState)lis.get(lis.size() - 1);
        bo = (WflStateBObj) s.getBObj();
        bo.setParallelism(WflStateBObj.PARALLELISM_NONE);

        //modifico los WflComponent de toda la lista
        for (int i = 0; i < lis.size(); i++) {
            WflComponent wc = (WflComponent) lis.get(i);
            wc.setInParallelism(false);
        }
    }

    /**
     * Devuelve una lista con un paralelismo que incluya al componente recibido
     * como parámeto.
     * @param wc
     * @return una lista con un paralelismo que incluya al componente recibido
     * como parámeto.
     */
    public List<WflComponent> getParallelism(WflComponent wc) {
        for (int i = 0; i < paralelism.size(); i++) {
            List<WflComponent> list = paralelism.get(i);
            if (list.contains(wc))
                return list;
        }
        return null;
    }

    public void showProcessDefDlg() {
        WflProcessDefinitionDlg dlg = new WflProcessDefinitionDlg(processId,
                processName, processVersion, processTrace, processSecurityObject, processComment, properties, constraints);

        String oldId = processId;
        dlg.showMe();
        if(dlg.isOK()){
            this.processId = dlg.getId();
            this.processName = dlg.getName();
            this.processVersion = dlg.getVersion();
            this.processTrace = dlg.getTrace();
            this.processComment = dlg.getComment();
            this.processSecurityObject = dlg.getSecurityObject();
            this.properties = dlg.getProperties();
            this.constraints = dlg.getConstraints();
            if (dlg.wasModified()){
                WflUtility.setModified(true);
                if (processId != null && !processId.equals(oldId))
                    WflUtility.setCurrentFrameTitle(processId);
            }
        }
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public String getProcessSecurityObject() {
        return processSecurityObject;
    }

    public String getProcessComment() {
        return processComment;
    }

    public String getProcessName() {
        return processName;
    }

    public String getProcessId() {
        return processId;
    }

    public String getProcessTrace() {
        return processTrace;
    }

    /**
     * Abre el diálogo de edición del componente recibido como parámetro y, en
     * caso del ser necesario, actualiza datos de los demás componentes.
     * @param comp
     */
    public void edit(WflComponent comp) {
        String oldId = comp.getId();
        boolean wasOk = comp.showDialog();
        if (!wasOk)
            return ;

        //Verifico que no se haya puesto un id repetido
        if (comp instanceof WflState || comp instanceof WflFunction){
            String id = comp.getId();
            List<WflComponent> cmps = container.getWflComponents(WflState.class);
            cmps.addAll(container.getWflComponents(WflFunction.class));
            for (int i = 0; i < cmps.size(); i++) {
                WflComponent wc = (WflComponent) cmps.get(i);
                if (wc != comp){
                    String id2 = wc.getId();
                    if (id2 != null && id.equals(id2)){
                        String msg = "Ya existe un componente con el mismo Id";
                        WflUtil.showErrorMessage(msg);
                        comp.setId(oldId);
                        edit(comp);
                        return ;
                    }
                }
            }
        }

        if (comp instanceof WflState){
            //Si es un estado final de un paralelismo => actualizo los datos
            //del estado inicial
            WflState s = (WflState)comp;
            WflStateBObj b = (WflStateBObj) s.getBObj();
            if (WflStateBObj.PARALLELISM_END.equals(b.getParallelism())){
                updateParallelism(s);
            }
        }
    }

    /**
     * Recibe como parámetro el estado final de un paralelismo, cuyo id pudo
     * haber cambiado. Busca el estado inicial del paralelismo y actualiza los
     * datos.
     * @param sf
     */
    private void updateParallelism(WflState sf) {
        List par = getParallelism(sf);
        WflState si = (WflState)par.get(0);
        WflStateBObj boi = (WflStateBObj) si.getBObj();
        WflStateBObj bof = (WflStateBObj) sf.getBObj();
        boi.setFinalState(bof.getId());
    }

    public void removeSelected() {
    	
        List toDelete = container.getSelectedComponents();
        
        if (toDelete.size() == 0){
            String msg = "No hay elementos seleccionados para borrar.";
            WflUtil.showMessage(msg, "Borrar");
        }

        //Verifico si entre los elementos a borrar hay alguno que forme parte
        //de un paralelismo y en caso de que haya pido confirmación de borrado
        boolean hasParal = false;
        for (int i = 0; i < toDelete.size(); i++) {
            WflComponent wc = (WflComponent) toDelete.get(i);
            if (wc.isInParallelism())
                hasParal = true;
        }
        if (hasParal){
            String msg = "Al borrar los elementos seleccionados se eliminará " +
                    "por lo menos un paralelismo. ¿Está seguro que desea " +
                    "continuar con la eliminación de componentes?";
            int i = WflUtil.showConfirmDialog(msg, "Borrar");
            if (i != JOptionPane.OK_OPTION)
                return ;
        }
        
        // Ahora tengo que ver si estoy borrando un short cut de estado para poder crear de nuevo el 
        // connector al estado real.
        List<WflComponent> postFunctionToRecreate = new ArrayList<WflComponent>();
   		Iterator i = toDelete.iterator();
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof WflStateShortCut) {
				WflStateShortCut stateShortCut = (WflStateShortCut)o;
				
				Iterator<WflComponent> relatedIterator = stateShortCut.getRelatedComponents().iterator();
				while(relatedIterator.hasNext()) {
					WflComponent relatedO = relatedIterator.next();
					if(relatedO instanceof WflPosFunction)
						postFunctionToRecreate.add(relatedO);
				}
			}
		}        
        
        recalculateParallelism(toDelete);
        
        container.remove(toDelete);
        
        // Ahora tengo que reemplazar todos las postfunciones que iban a los shortcuts.
        Iterator it = postFunctionToRecreate.iterator();
        while(it.hasNext()) {
        	WflPosFunction oldPostFunction = (WflPosFunction)it.next();
        	WflPosFunction pf = new WflPosFunction(	oldPostFunction.getFunction(), 
        											((WflStateShortCut)oldPostFunction.getState()).getTargetState(),
        											(WflPosFunctionBObj)oldPostFunction.getBObj());
        	this.addPosFunction(pf);
        }
		    		
        setModified(true);
    }

    /**
     * Agrega conectores desde las funciones seleccionadas hasta los estados
     * seleccionados.
     * Si no hay estados o funciones seleccionados se muestra un mensaje de
     * error.
     */
    public void addPosFunction() {
        List states = container.getSelectedComponents(WflState.class);
        List funs = container.getSelectedComponents(WflFunction.class);

        if (states.size()== 0){
            String msg = "Se debe seleccionaron por lo menos un estado";
            WflUtil.showMessage(msg, "Agregar Posfunción");
            return ;
        }
        if (funs.size()==0){
            String msg = "Se debe seleccionaron por lo menos una función";
            WflUtil.showMessage(msg, "Agregar Posfunción");
            return ;
        }

        //Recorro las funcionas y si alguna forma parte de un paralelismo =>
        //elimino el paralelismo (lueo de pedir confimación al usuario)
        List<WflFunction> aBorrar = new ArrayList<WflFunction>();
        for (int i = 0; i < funs.size(); i++) {
            WflFunction f = (WflFunction) funs.get(i);
            if (f.isInParallelism()){
                //Si hay más de un paralelismo en las funciones pregunto sólo
                //la primera vez.
                if (aBorrar.size() == 0){
                    String msg = "Si se agrega la posfunción se va " +
                            "a eliminar el paralelismo de la función. ¿Desea continuar?";
                    int j = WflUtil.showConfirmDialog(msg, "Agregar estado");
                    if (JOptionPane.YES_OPTION != j)
                        return;
                }
                aBorrar.add(f);
            }
        }
        //borro (si existen) los paralelismos
        for (int i = 0; i < aBorrar.size(); i++) {
            WflFunction function = (WflFunction) aBorrar.get(i);
            removeParallelism(function);
        }
        for (int i = 0; i < states.size(); i++) {
            WflState state = (WflState)states.get(i);
            for (int j = 0; j < funs.size(); j++) {
                WflFunction fun = (WflFunction)funs.get(j);
               
                addPosFunction(fun, state);
            }
            state.notifyObservers();
        }

        setModified(true);
    }
    
    /**
     * Agrega una pos funcion.
     * @param fun Funcion.
     * @param state Estado.
     */
    public void addPosFunction(WflFunction fun, WflState state) {
    	addPosFunction(new WflPosFunction(fun, state));
    }
    
    public void addPosFunction(WflPosFunction posFunction) {
        if (exist(posFunction)){
            String msg = "Ya existe una posfunción entre la función " +
            posFunction.getFunction().getId() + " y el estado " + 
            posFunction.getState().getId();
            WflUtil.showErrorMessage(msg);
            return;
        }
        container.add(posFunction);
        posFunction.getFunction().notifyObservers();
        posFunction.update((WflComponent)null);
    }

    /**
     *
     * @param pf
     * @return true si ya existe una posfunción entre la función y el estado de la
     * posfunción recibida como parámetro.
     */
    private boolean exist(WflPosFunction pf){
        WflState state = pf.getState();
        WflFunction function = pf.getFunction();
        List pfs = container.getWflComponents(WflPosFunction.class);
        for (int i = 0; i < pfs.size(); i++)
        {
            WflPosFunction p = (WflPosFunction) pfs.get(i);
            if (state.equals(p.getState())
                    && function.equals(p.getFunction()))
                return true;
        }

        return false;
    }

    public void addPreFunction() {
        List states = container.getSelectedComponents(WflState.class);
        List funs = container.getSelectedComponents(WflFunction.class);

        if (states.size()== 0){
            String msg = "Se debe seleccionaron por lo menos un estado";
            WflUtil.showMessage(msg, "Agregar prefunción");
            return ;
        }
        if (funs.size()==0){
            String msg = "Se debe seleccionaron por lo menos una función";
            WflUtil.showMessage(msg, "Agregar prefunción");
            return ;
        }

        //Si algún estado ya tiene una prefunción => muestro msg de error y no
        //hago nada
        for (int i = 0; i < states.size(); i++) {
            WflState s = (WflState) states.get(i);
            List rc = s.getRelatedComponents(WflPreFunction.class);
            if (rc.size() > 0){
                String msg = "El estado " + s.getId() + " ya tiene una " +
                        "prefunción y no puede tener más de una";
                WflUtil.showMessage(msg, "Agregar prefunción");
                return ;
            }
        }

        for (int i = 0; i < states.size(); i++) {
            WflState state = (WflState)states.get(i);
            for (int j = 0; j < funs.size(); j++) {
                WflFunction fun = (WflFunction)funs.get(j);
                WflPreFunction pf = new WflPreFunction(state, fun);
                container.add(pf);
                fun.notifyObservers();
                pf.update((WflComponent)null);
            }
            state.notifyObservers();
        }
        setModified(true);
    }

    public void selectAll() {
        List list = container.getWflComponents();
        for (int i = 0; i < list.size(); i++) {
            WflComponent wc = (WflComponent) list.get(i);
            wc.setSelected(true);
        }
    }

    public void editSelected() {
        String msg = null;
        List list = container.getSelectedComponents();
        if (list.size() == 1){
            WflComponent wc = (WflComponent) list.get(0);
            edit(wc);
        }
        else if (list.size() == 0)
            msg = "No hay componenetes seleccionados";
        else if (list.size() > 1)
            msg = "Se debe seleccionar un sólo componente";

        if (msg != null)
            WflUtil.showMessage(msg, "Editar componente");
    }


    /**
     * Crea un paralelismo. Si la cantidad de estados seleccionados es distinto
     * de 2 o, siendo 2, no están conectados (por todos los caminos posibles),
     * muestra un msg de error y sale.
     *
     * Si el estado inicial es null (eg, si el action se dispara desde
     * la toolbar) => para que se pueda crear el
     * paralelismo debe haber sólo 2 estados seleccionados y desde uno de
     * ellos (el estado inicial) se debe poder llegar por todos los caminos
     * al segundo (el final), pero desde el final no se tiene que poder
     * llegar al inicial (xq en ese caso no podría diferencial cuál es el
     * inicial y cuál el final).
     *
     * @param stateInitial estado inicial del paralelismo.
     */
    public void createParallelism(WflState stateInitial) {
        List sc = getSelectedComponents(WflState.class);
        //Debe haber  2 estados seleccionados
        if (sc.size() != 2){
            String msg = "Para crear un paralelismo debe haber dos estados seleccionados";
            WflUtil.showMessage(msg, "Crear paralelismo");
            return ;
        }

        if (stateInitial == null){
            String msg = null;
            WflState s1 = (WflState)sc.get(0);
            WflState s2 = (WflState)sc.get(1);
            boolean b1 = areConnected(s1, s2);
            boolean b2 = areConnected(s2, s1);
            if (b1 && !(b2))
                stateInitial = s1;
            else if (b2 && !(b1))
                stateInitial = s2;
            else if (b2 && b1)
                msg = "No se puede distinguir cuál de los dos estados " +
                        "es el inicial. Cree el paralelismo usando el " +
                        "mouse.";
            else
                msg = "Los estados seleccionados deben estar conectados" +
                        " por todos los caminos posibles";

            if (msg != null){
                WflUtil.showMessage(msg, "Crear paralelismo");
                return ;
            }
        }

        WflState stateFinal = (WflState) sc.get(0);
        if (stateFinal.equals(stateInitial))
            stateFinal = (WflState) sc.get(1);

        //tienen que estar conectados
        boolean b = areConnected(stateInitial, stateFinal);
        if (!b){
            String msg = "Para crear un paralelismo los estados seleccionados " +
                    "deben estar conectados, y desde el inicial se tiene que " +
                    "llegar al final por todos los caminos posibles";
            WflUtil.showMessage(msg, "Crear paralelismo");
            return ;
        }
        WflStateBObj bo = (WflStateBObj)stateInitial.getBObj();
        WflStateBObj boF = (WflStateBObj)stateFinal.getBObj();
        WfParallelismDlg dlg = new WfParallelismDlg(bo, boF);
        dlg.showAdd();
        if (dlg.wasOK()){
            bo.setParallelism(WflStateBObj.PARALLELISM_START);
            String className = dlg.getClassName();
            bo.setClassName(className);
            bo.setCommentParallelism(dlg.getComment());
            bo.setFinalState(boF.getId());
            boF.setParallelism(WflStateBObj.PARALLELISM_END);

            addParallelism(stateInitial, stateFinal, className);
        }
    }

    /**
     * Remueve el paralelismo que tiene al componente recibido como parámetro
     * o, si el parámetro es null, busca todos los componentes seleccionados y
     * si todos forman parte del mismo paralelismo, lo borra.
     *
     * @param component
     */
    public void removeParallelism(WflComponent component) {
        List<WflComponent> paralelism;
        if (component == null){
            List sc = container.getSelectedComponents();
            if (sc.size() == 0){
                String msg = "Para borrar un paralelismo se debe seleccionar un componente que esté dentro de un paralelismo";
                WflUtil.showMessage(msg, "Borrar paralelismo");
                return ;
            }

            //Reviso que todos formen parte del mismo paralelismo
            paralelism = getParallelism((WflComponent)sc.get(0));
            if (paralelism == null || !(paralelism.containsAll(sc))){
                String msg = "Para borrar un paralelismo todos los " +
                        "componentes seleccionados tienen que formar parte " +
                        "del mismo paralelismo";
                WflUtil.showMessage(msg, "Borrar paralelismo");
                return ;
            }
        }
        else
            paralelism = getParallelism(component);

        if (paralelism == null || paralelism.size() == 0){
            String msg = "Para borrar un paralelismo se debe seleccionar un componente que esté dentro de un paralelismo";
            WflUtil.showMessage(msg, "Borrar paralelismo");
            return ;
        }
        WflState si = (WflState)paralelism.get(0);
        WflStateBObj bo = (WflStateBObj)si.getBObj();
        WflState sf = (WflState)paralelism.get(paralelism.size()-1);
        WflStateBObj boF = null;
        if (sf != null)
            boF = (WflStateBObj)sf.getBObj();
        WfParallelismDlg dlg = new WfParallelismDlg(bo, boF);
        dlg.setClassName(bo.getClassName());
        dlg.setComment(bo.getComment());
        dlg.showRemove();
        if (dlg.wasOK()){
            removeParallelism(si, sf);
        }
    }

    /**
     * Edita el paralelismo que tiene al componente recibido como parámetro
     * o, si el parámetro es null, busca todos los componentes seleccionados y
     * si todos forman parte del mismo paralelismo, lo edita.
     *
     * @param component
     */
    public void editParallelism(WflComponent component) {
        List<WflComponent> paralelism;
        if (component == null){
            List sc = container.getSelectedComponents();
            if (sc.size() == 0){
                String msg = "Para editar un paralelismo se debe seleccionar un componente que esté dentro de un paralelismo";
                WflUtil.showMessage(msg, "Editar paralelismo");
                return ;
            }

            //Reviso que todos formen parte del mismo paralelismo
            paralelism = getParallelism((WflComponent)sc.get(0));
            if (paralelism == null || !paralelism.containsAll(sc)){
                String msg = "Para editar un paralelismo todos los " +
                        "componentes seleccionados tienen que formar parte " +
                        "del mismo paralelismo";
                WflUtil.showMessage(msg, "Editar paralelismo");
                return ;
            }
        }
        else
            paralelism = getParallelism(component);

        if (paralelism == null || paralelism.size() == 0){
            String msg = "Para editar un paralelismo se debe seleccionar un componente que esté dentro de un paralelismo";
            WflUtil.showMessage(msg, "Editar paralelismo");
            return ;
        }
        WflState si = (WflState)paralelism.get(0);
        WflStateBObj bo = (WflStateBObj)si.getBObj();
        WflState sf = (WflState)getWflComponent(bo.getFinalState());
        WflStateBObj boF = null;
        if (sf != null)
            boF = (WflStateBObj)sf.getBObj();
        WfParallelismDlg dlg = new WfParallelismDlg(bo, boF);
        dlg.setClassName(bo.getClassName());
        dlg.setComment(bo.getCommentParallelism());
        dlg.showEdit();
        if (dlg.wasOK() && dlg.wasModified()){
            bo.setClassName(dlg.getClassName());
            bo.setCommentParallelism(dlg.getComment());
            WflUtility.setModified(true);
        }
    }

    /**
     * Empieza a trabajar con un nuevo archivo. No crea un nuevo archivo físico.
     * Pregunta si quiere guardar el trabajo anterior.
     */
    public void newFile() {
        if (!confirmSave())
            return ;

        clean();
    }

    public void clean() {
        container.clean();
        paralelism.clear();
        properties.clear();
        constraints.clear();
        processId = null;
        processName = null;
        processTrace = null;
        processVersion = null;
        processId = null;
        WflUtility.getMarksBar().clean();

        WflUtility.setLastFileOpen(null);
        WflUtility.setModified(false);
        WflUtility.setCurrentFrameTitle(null, null);
    }

    /**
     * Sale de la aplicación
     */
    public void exit() {
        boolean b = confirmSave();
        if (!b)
            return ;
        System.exit(0);
    }

    /**
     * Pregunta si se quiere grabar y graba si así lo indica el usuario.
     * Devuelve true si grabó o si el usuario no quiso grabar y false si canceló.
     * Si no hubo ninguna modificación => no hace nada y devuelve true
     */
    public boolean confirmSave() {
        if (WflUtility.isModified()){

            String msg = "¿Desea grabar los cambios?";
            int i = WflUtil.showConfirmDialog(msg, "Salir");
            if (i == JOptionPane.CANCEL_OPTION)
                return false;
            else if (i == JOptionPane.YES_OPTION){
                File lastFileOpen = WflUtility.getLastFileOpen();
                if (lastFileOpen != null)
                    return save(lastFileOpen);
                else{
                    return saveAs();
                }
            }
        }
        return true;
    }

    /**
     *
     * @return devuelve true si el archivo se grabó o false si se canceló
     */
    public boolean saveAs() {
        if (currentDirectoryPath == null){
            WflConstants c = WflConstants.getInstance();
            currentDirectoryPath = c.getProperty(WflConstants.ROOT_PATH);
        }
        JFileChooser fc = new JFileChooser(currentDirectoryPath);
        int returnVal = fc.showSaveDialog(container);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.exists()){
                String msg = "El archivo " + file.getName() + " ya existe. ¿" +
                        "Desea sobreescribir?";
                int i = WflUtil.showConfirmDialog(msg, "Grabar como...");
                if (i == JOptionPane.CANCEL_OPTION)
                    return false;
                else if (i == JOptionPane.NO_OPTION){
                     return saveAs();
                }
            }
            return save(file);
        }
        else
            return false;
    }

    /**
     * Devuelve true si se pudo grabar o false si no.
     * @param file
     * @return true si se pudo grabar o false si no.
     */
    public boolean save(File file) {

        //Verifico que todos los componentes estén en un estado válido
        String msg = null;
        List<WflComponent> wflComp = container.getWflComponents();
        for (int i = 0; i < wflComp.size(); i++) {
            WflComponent wc = (WflComponent) wflComp.get(i);
            if (!wc.isConsistent()){
                msg = "No se puede grabar ya que existen componentes " +
                        "en estado inválido";

            }
        }

        //Verifico que no haya id repetidos
        if (msg == null){
            List<WflComponent> comps = container.getWflComponents(WflState.class);
            comps.addAll(container.getWflComponents(WflFunction.class));
            Set<String> set = new HashSet<String>(comps.size());
            for (int i = 0; i < comps.size(); i++) {
                WflComponent wc = (WflComponent) comps.get(i);
                String id = wc.getId();
                boolean b = set.add(id);
                if (!b){
                    msg = "No se puede grabar ya que existe un id repetido: "+id;
                    break;
                }
            }
        }

        if (msg == null){
            //Verifico que se haya definido el id-versión-nombre
            if (getProcessId() == null || getProcessId().trim().equals("")){
                msg = "No se puede grabar ya que no se definió el id del proceso ";
            }
            else if (getProcessVersion() == null || getProcessVersion().trim().equals("")){
                msg = "No se puede grabar ya que no se definió la versión del proceso ";
            }
            else if (getProcessName() == null || getProcessName().trim().equals("")){
                msg = "No se puede grabar ya que no se definió el nombre del proceso ";
            }
        }
        if (msg != null){
            WflUtil.showMessage(msg, "Grabar");
            return false;
        }
        String n = file.getName();
        currentDirectoryPath = file.getParent();
        if (!n.toLowerCase().endsWith(".xml")) {
            n = n + ".xml";
            file = new File(file.getParent(), n);
        }
        try {
            //A partir de la lista de WflComp armo una lista de WflBObj y otra
            //de WflLocation
            Iterator i = wflComp.iterator();
            List<WflBObj> bobjs = new ArrayList<WflBObj>(wflComp.size());
            List<WflLocationBObj> locations = new ArrayList<WflLocationBObj>(wflComp.size());
            while (i.hasNext()) {
                WflComponent c = (WflComponent) i.next();
                WflBObj b = c.getBObj();
                locations.add(c.getWflLocation());
                
                // Verifico que no esten repetidos
                // Esto lo chequeo para los casos en los que hay shor cuts
                Iterator it = bobjs.iterator();
                boolean found = false;
                while(it.hasNext()) {
                	WflBObj wb = (WflBObj)it.next();
                	if(wb.getBObjId().equals(b.getBObjId())) {
                		found = true;
                		break;
                	}
                }
                if(!found)
                	bobjs.add(b);
            }

            String id = getProcessId();
            WflProcessBObj process = new WflProcessBObj(id);
            process.setVersion(getProcessVersion());
            process.setTrace(getProcessTrace());
            process.setName(getProcessName());
            process.setSecurityObject(getProcessSecurityObject());
            process.setComment(getProcessComment());
            process.setWflBObjs(bobjs);
            process.setMarks( getMarks());
            process.setProperties(getProperties());
            process.setObjectConstraints(getObjectConstraints());

            WflIOUtil.saveProcess(file, process);
            WflUtility.setLastFileOpen(file);
            WflUtility.setModified(false);
            WflUtility.setCurrentFrameTitle(file.getAbsolutePath(), id);

            if (n.toLowerCase().endsWith(".xml")) {
                n = n.substring(0, n.length() - 4) + "Locations.xml";
                file = new File(file.getParent(), n);
                WflIOUtil.saveLocations(file, locations, id, processVersion, processName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            WflUtil.showMessage(e.getMessage(), "Error");
            return false;
        }
        return true;
    }
    
    public void showFunctionTraceEditor() {
    	WflFunctionTraceDlg traceEditor = new WflFunctionTraceDlg(WflComponentHighlighter.getTrace());
    	traceEditor.showMe();
    }

    public void showXML() {
        //Verifico que todos los componentes estén en un estado válido
        String msg = null;
        List<WflComponent> wflComp = container.getWflComponents();
        for (int i = 0; i < wflComp.size(); i++) {
            WflComponent wc = (WflComponent) wflComp.get(i);
            if (!wc.isConsistent()){
                msg = "No se puede generar el XML ya que existen componentes " +
                        "en estado inválido";
            }
        }

        //Verifico que no haya id repetidos
        if (msg == null){
            List<WflComponent> comps = container.getWflComponents(WflState.class);
            comps.addAll(container.getWflComponents(WflFunction.class));
            Set<String> set = new HashSet<String>(comps.size());
            for (int i = 0; i < comps.size(); i++) {
                WflComponent wc = (WflComponent) comps.get(i);
                String id = wc.getId();
                boolean b = set.add(id);
                if (!b){
                    msg = "No se puede generar el XML ya que existe un id repetido: "+id;
                    break;
                }
            }
        }

        if (msg == null){
            //Verifico que se haya definido el id-versión-nombre
            if (getProcessId() == null || getProcessId().trim().equals("")){
                msg = "No se puede generar el XML ya que no se definió el id del proceso ";
            }
            else if (getProcessVersion() == null || getProcessVersion().trim().equals("")){
                msg = "No se puede generar el XML ya que no se definió la versión del proceso ";
            }
            else if (getProcessName() == null || getProcessName().trim().equals("")){
                msg = "No se puede generar el XML ya que no se definió el nombre del proceso ";
            }
        }
        if (msg != null){
            WflUtil.showMessage(msg, "Ver XML");
            return ;
        }

        try {
            //A partir de la lista de WflComp armo una lista de WflBObj y otra
            //de WflLocation
            Iterator i = wflComp.iterator();
            List<WflBObj> bobjs = new ArrayList<WflBObj>(wflComp.size());
            while (i.hasNext()) {
                WflComponent c = (WflComponent) i.next();
                WflBObj b = c.getBObj();
                bobjs.add(b);
            }

            String id = getProcessId();
            WflProcessBObj process = new WflProcessBObj(id);
            process.setVersion(getProcessVersion());
            process.setTrace(getProcessTrace());
            process.setName(getProcessName());
            process.setSecurityObject(getProcessSecurityObject());
            process.setComment(getProcessComment());
            process.setWflBObjs(bobjs);
            process.setMarks( getMarks());
            process.setProperties(getProperties());
            process.setObjectConstraints(getObjectConstraints());

            Document xml = WflIOUtil.getXML(process);
            StringBuffer s = WflXMLUtil.xml2String(xml);
            showXML(s);

        } catch (Exception e) {
            e.printStackTrace();
            WflUtil.showMessage(e.getMessage(), "Ver XML");
        }
    }

    private void showXML(StringBuffer s) {
        JTextArea textArea = new JTextArea();
    	textArea.setFont(new Font("monospaced", Font.PLAIN, 12));

        final JDialog dialog = new JDialog(WflUtility.getCurrentFrame());
        Container cp = dialog.getContentPane();
        cp.setLayout(new GridBagLayout());
        JButton btnAccept = new JButton("Aceptar");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JScrollPane sp = new JScrollPane (textArea);
        textArea.setText(s.toString());
        textArea.setEditable(false);
        btnAccept.setSize(100, 20);
        cp.add(sp, new GridBagConstraints(0,0,1,1,1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10), 0,0));
        cp.add(btnAccept, new GridBagConstraints(0,1,1,1,0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 10, 10), 0,0));
        dialog.setSize(760, 700);
        dialog.setVisible(true);
    }

    public void save() {
        File lastFileOpen = WflUtility.getLastFileOpen();
        if (lastFileOpen == null){
            saveAs();
            return ;
        }

        boolean b = save(lastFileOpen);
        if (b)
            WflUtility.setModified(false);
    }

    public void repaintContainer() {
        container.repaint();
    }

    public WflProcessDefinitionContainer getContainer() {
        return container;
    }

    /**
     * Asocia al estado recibido como parámetro el hito seleccionado
     * @param state
     */
    private void addMarkState(WflState state, String action) {
        WflStateBObj bo = (WflStateBObj) state.getBObj();
        if (bo.hasMark(mark)){
            String msg = "Este estado ya tiene el hito " + mark.getId();
            WflUtil.showMessage(msg, "Agregar Hito");
            return ;
        }

         if (!mark.isDeletable()
                && WflStateMarkBObj.BAJA.equals(action)){
            String msg = "El hito no es borrable.\n (Se agrega como Alta)";
            WflUtil.showMessage(msg, "Hito");
            action = WflStateMarkBObj.ALTA;
        }

        bo.addStateMark(new WflStateMarkBObj(action, mark));
        state.repaint();
    }


    public void addMark(WflMarkBObj mark) {
        //Se llama cuando se ha elegido un mark (antes de elegir el
        //estado al cual se va a agregar).
        this.mark = mark;
        Frame cf = WflUtility.getCurrentFrame();
        ImageIcon icon = WflUtil.createImageIconAddMark();
        Point p = new Point(0,0);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                icon.getImage(), p,"Agregar hito");
        cf.setCursor(cursor);
        addingMark = true;
    }

    public void cancelAddMark(){
        mark = null;
        addingMark = false;
        WflUtility.getCurrentFrame().setCursor(Cursor.getDefaultCursor());
    }

    public void createMark() {
        WflMarkBObj m = new WflMarkBObj();
        createMarkDlg(m);
    }

    private void createMarkDlg(WflMarkBObj m) {
        WflMarkDlg dlg = new WflMarkDlg(m);
        dlg.showMe();
        if(dlg.isOK()){
            //Valido que no haya id repetidos
            String id = m.getId();
            List marks = WflUtility.getMarksBar().getMarks();
            for (int i = 0; i < marks.size(); i++) {
                WflMarkBObj m2 = (WflMarkBObj) marks.get(i);
                if (id.equals(m2.getId())){
                    String msg = "Ya existe un Hito con el mismo id: " + id;
                    WflUtil.showErrorMessage(msg);
                    createMarkDlg(m);
                    return ;
                }
            }
            WflUtility.setModified(true);
            createMark(m);
        }
    }

    private void createMark(WflMarkBObj bo) {
        WflMarksBar marksBar = WflUtility.getMarksBar();
        marksBar.addMark(bo);
    }

    /**
     *
     * @return lista de WflMarkBObj
     */
    public List<WflMarkBObj> getMarks() {
        WflMarksBar marksBar = WflUtility.getMarksBar();
        return marksBar.getMarks();
    }

    public void remove(WflMarkBObj mark) {
        WflMarkDlg dlg = new WflMarkDlg(mark);
        dlg.showForDelete();
        if (!dlg.wasOK())
            return ;

        //Si algún estado tiene a este hito aviso antes de borrar
        boolean isUsed = false;
        List states = container.getWflComponents(WflState.class);
        for (int i = 0; i < states.size(); i++) {
            WflState s = (WflState) states.get(i);
            List stateMarks = ((WflStateBObj)s.getBObj()).getStateMarks();
            if (contains(stateMarks,mark)){
                isUsed = true;
                break;
            }
        }
        if (isUsed){
            String msg = "Hay, por lo menos, un estado que usa este Hito. Si se" +
                    " borra se eliminará de todos \n los estados que lo usan. " +
                    "¿Desea borrarlo?";
            int i = WflUtil.showConfirmDialog(msg, "Borrar Hito");
            if (i != JOptionPane.OK_OPTION)
                return ;
        }
        removeWithReference(mark);
    }

    /**
     *
     * @param stateMarks lista de WflStateMarkBObj
     * @param mark
     */
    private boolean contains(List stateMarks, WflMarkBObj mark) {
        for (int i = 0; i < stateMarks.size(); i++) {
            WflStateMarkBObj ms = (WflStateMarkBObj) stateMarks.get(i);
            if (ms.getMark().equals(mark))
                return true;
        }
        return false;
    }

    /**
     * Borra el markdas las referncias que tengan los estados hacia él
     * @param mark
     */
    private void removeWithReference(WflMarkBObj mark) {
        WflUtility.getMarksBar().remove(mark);
        List states = container.getWflComponents(WflState.class);
        for (int i = 0; i < states.size(); i++) {
            WflState s = (WflState) states.get(i);
            boolean wasRemoved =((WflStateBObj)s.getBObj()).remove(mark);
            if (wasRemoved)
                s.repaint();
        }
    }

    public void edit(WflMarkBObj mark) {
        //Si es borrable y algún estado lo tiene como baja => no permito en el
        // diálogo que se lo ponga como no borrable.
        boolean disableDelete = false;
        if (mark.isDeletable()){
            List q = getStatesWith(mark,  WflStateMarkBObj.BAJA);
            disableDelete = q.size() > 0;
        }
        WflMarksBar marksBar = WflUtility.getMarksBar();
        WflMarkDlg dlg = new WflMarkDlg(mark);
        dlg.showForEdit(disableDelete);
        if (dlg.wasModified()){
            WflUtility.setModified(true);
            marksBar.update();
        }
    }

    /**
     * Devuelve una lista de WflStateBObj que tengan el hito recibido como parámetro.
     * @param mark
     * @param action
     */
    private List<WflState> getStatesWith(WflMarkBObj mark, String action){
        List<WflState> res = new ArrayList<WflState>();
        List<WflComponent> states = container.getWflComponents(WflState.class);
        for (int i = 0; i < states.size(); i++) {
            WflState s = (WflState) states.get(i);
            List stateMarks = ((WflStateBObj)s.getBObj()).getStateMarks();
            for (int j = 0; j < stateMarks.size(); j++) {
                WflStateMarkBObj ms = (WflStateMarkBObj) stateMarks.get(j);
                if (ms.getMark().equals(mark))
                    if (ms.getAction().equals(action))
                        res.add(s);
            }
        }
        return res;
    }

    /**
     * Cambia el tamaño y la posición de los componentes.
     * Si zoom = 1 los componentes recuperan su tamaño y posición original.
     * El zoom no es ralativo a la zommposición y tamaño actual sino a la original.
     *
     * @param zoom valor mayor que cero. Este valor ya debe hacer sido
     * dividido por cien (por el porcentaje).
     */
    public void setZoom(double zoom) {
        getContainer().setZoom(zoom);

        repaintContainer();
        getContainer().requestFocus();
        List cs = getContainer().getWflComponents(WflConnector.class);
        for (int i = 0; i < cs.size(); i++) {
            WflConnector c = (WflConnector) cs.get(i);
            c.update((WflComponent)null);
        }
    }

    public double getZoom() {
        return getContainer().getZoom();
    }

	/**
	 * Obtiene las properties.
	 * @return Retorna las properties.
	 */
	public List<WflPropertyBObj> getProperties() {
		return properties;
	}
	
	
	/**
	 * Constraint de los objetos para comenzar el proceso.
	 * @return Lista de constraints.
	 */
	public List getObjectConstraints() {
		return this.constraints;
	}

	/**
	 * Devuelve el/la zoomFrame.
	 * @return el/la zoomFrame.
	 */
	public WflZoomFrame getZoomFrame() {
		return zoomFrame;
	}

	/**
	 * Setea el/la zoomFrame
	 * @param zoomFrame El/la zoomFrame a setear.
	 */
	public void setZoomFrame(WflZoomFrame zoomFrame) {
		this.zoomFrame = zoomFrame;
	}

	/**
	 * Devuelve el/la zoomFrameActive.
	 * @return el/la zoomFrameActive.
	 */
	public boolean isZoomFrameActive() {
		return zoomFrameActive;
	}

	/**
	 * Setea el/la zoomFrameActive
	 * @param zoomFrameActive El/la zoomFrameActive a setear.
	 */
	public void setZoomFrameActive(boolean zoomFrameActive) {
		if(this.zoomFrameActive != zoomFrameActive) {
			this.zoomFrameActive = zoomFrameActive;
			zoomFrame.setVisible(zoomFrameActive);
			this.getContainer().requestFocus();
			this.getToolBar().setZoomWindowsStatus(zoomFrameActive);
			this.getMenuBar().setZoomWindowsStatus(zoomFrameActive);			
		}
	}

	/**
	 * Devuelve el/la menuBar.
	 * @return el/la menuBar.
	 */
	public WflMenuBar getMenuBar() {
		return menuBar;
	}

	/**
	 * Setea el/la menuBar
	 * @param menuBar El/la menuBar a setear.
	 */
	public void setMenuBar(WflMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	/**
	 * Devuelve el/la toolBar.
	 * @return el/la toolBar.
	 */
	public WflToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * Setea el/la toolBar
	 * @param toolBar El/la toolBar a setear.
	 */
	public void setToolBar(WflToolBar toolBar) {
		this.toolBar = toolBar;
	}
	
}
