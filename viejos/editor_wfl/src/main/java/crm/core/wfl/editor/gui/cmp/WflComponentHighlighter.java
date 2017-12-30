package crm.core.wfl.editor.gui.cmp;

import java.util.ArrayList;
import java.util.List;

import crm.core.wfl.editor.bobj.WflPosFunctionBObj;
import crm.core.wfl.editor.gui.action.WflTestTokenValueAction;

/**
 * Esta clase se encarga de indicar el grado de remarcado que puede
 * necesitar un componente.
 * @author cm
 *
 */
public class WflComponentHighlighter {

	private static List<WflFunctionTrace> trace = new ArrayList<WflFunctionTrace>();
		
//	static {
//		trace.add(new WflFunctionTrace("CHKAPF", 	1L));
//		trace.add(new WflFunctionTrace("CHKAPF", 	2L));
//		trace.add(new WflFunctionTrace("CHKAPF", 	8L));		
//		trace.add(new WflFunctionTrace("APRASI", 	1L));
//		trace.add(new WflFunctionTrace("PMU4", 		0L));
//		trace.add(new WflFunctionTrace("CHKCOM", 	1L));
//		trace.add(new WflFunctionTrace("ASICOM", 	0L));
//		trace.add(new WflFunctionTrace("PMU5", 		0L));
//		trace.add(new WflFunctionTrace("HADJ", 		0L));
//
//	}
	
	
	/**
	 * Devuelve el/la trace.
	 * @return el/la trace.
	 */
	public static List<WflFunctionTrace> getTrace() {
		return trace;
	}

	
	/**
	 * Obtiene el grado de remarcado que necesita un componente.
	 * @param component Componente.
	 * @return Nivel de remarcado.
	 */
	public static int getHighlightLevel(WflComponent component) {
		int count = 0;
		
		String searchId = component.getId();
		
		// Si es una funcion, y esta en la lista la ilumina.
		if(component instanceof WflFunction) {
				for(WflFunctionTrace s : trace) {
					if(searchId.equals(s.getFunctionId()))
						count++;
				}
		}
		
		else if(component instanceof WflStateShortCut) {
			WflStateShortCut sc = (WflStateShortCut)component;
			List<WflComponent> related = sc.getRelatedComponents(WflPosFunction.class);
			for(WflComponent c : related)
				if(getHighlightLevel(c)>0)
					return 1;
		}
		// Si es estado y esta asociado por dos funciones que esten en la lista
		// asumo que paso por este estado.
		else if(component instanceof WflState) {
			if(component.getId().equals("r")) 
				System.err.println("ok");
	        List rc = component.getRelatedComponents();
	        boolean preFunctionFound = false;
	        boolean postFunctionFound = false;
	        for (int i = 0; i < rc.size(); i++) {
	            Object o = rc.get(i);
	            if (o instanceof WflPreFunction) {
	            	WflFunctionTrace wft = new WflFunctionTrace(((WflPreFunction)o).getTarget().getId(), 0); 
	            	if(trace.contains(wft))
	            		preFunctionFound = true;
	            }
	            else if (o instanceof WflPosFunction) {
	            	WflFunctionTrace wft = new WflFunctionTrace(((WflPosFunction)o).getSource().getId(), 0); 
	            	if(trace.contains(wft)) {
	            		// Tengo que chequear que el punto matchea con alguna de las salidas
	        			WflPosFunction pf = (WflPosFunction)o;
	        			WflPosFunctionBObj pfb = (WflPosFunctionBObj)pf.getBObj();
	        			String functionId = pf.getSource().getId();
	    				
	    				if(pfb.getMaskType()!=null && pfb.getMaskType().trim().length()>0) {
	    					List<Long> values = getTokenValue(functionId);
	    					if(values.size()==0)
	    						return 0;
	    					long maskValue = Long.parseLong(pfb.getMaskValue());
	    					for(Long value : values)
	    						if(WflTestTokenValueAction.matchMask(pfb.getMaskType(), maskValue, value))
	    							postFunctionFound = true;
	    				}
	    				else
	    					postFunctionFound = true;
	            	}
	            }
	        }
	        // Si esta en el medio de dos seleccionados o tiene solo una relacion y esa esta seleccionada, entonces lo pinto.
	        WflState s = (WflState)component;
	        if((preFunctionFound && postFunctionFound) ||
	           (preFunctionFound && s.isInitial()) ||
	           (postFunctionFound && s.isFinal()))	        		
	        	count=1;
		}
		// Si es una pre que conecta dos componentes iluminados, ilumino.
		else if(component instanceof WflPreFunction) {
			if(getHighlightLevel(((WflConnector)component).getSource())>0 &&
			   getHighlightLevel(((WflConnector)component).getTarget())>0 )
				return 1;
		}
		// Si es una post, verifico si tiene mascara, si la tiene, chequeo
		// que la funcion asociada este en la lista y haya terminado con un token 
		// compatible con el valor de mascara de la post, sino
		// solo chequeo que una dos componentes iluminados.
		else if(component instanceof WflPosFunction) {
			WflPosFunction pf = (WflPosFunction)component;
			WflPosFunctionBObj pfb = (WflPosFunctionBObj)pf.getBObj();
			String functionId = pf.getSource().getId();
			if(pfb.getMaskType()!=null && pfb.getMaskType().trim().length()>0) {
				List<Long> values = getTokenValue(functionId);
				if(values.size()==0)
					return 0;
				
				long maskValue = Long.parseLong(pfb.getMaskValue());
				
				for(Long value : values)
					if(WflTestTokenValueAction.matchMask(pfb.getMaskType(), maskValue, value))
						return 1;
			}
			else {
				if(getHighlightLevel(((WflConnector)component).getSource())>0 &&
						   getHighlightLevel(((WflConnector)component).getTarget())>0 )
							return 1;
			}
		}
		return count;
	}
	
	/**
	 * Obtiene el valor de punto con el que termino una funcion.
	 * @param functionId Function Id.
	 * @return Valor de punto si esta, sino null;
	 */
	private static List<Long> getTokenValue(String functionId) {
		List<Long> r = new ArrayList<Long>();
		for(WflFunctionTrace s : trace) {
			if(functionId.equals(s.getFunctionId()))
				r.add(s.getTokenValue());
		}
		return r;
	}
}
