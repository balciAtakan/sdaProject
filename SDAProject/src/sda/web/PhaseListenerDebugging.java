package sda.web;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class PhaseListenerDebugging implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		System.out.println("Nach Phase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		System.out.println("Vor Phase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.ANY_PHASE;
	}

}
