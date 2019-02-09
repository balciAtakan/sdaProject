package main.java.sda.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class PhaseListenerDebugging implements PhaseListener {

	private static Logger log = LogManager.getLogger(PhaseListenerDebugging.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		log.debug("Nach Phase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		log.debug("Vor Phase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.ANY_PHASE;
	}

}
