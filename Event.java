package edu.uwm.basecso;

import java.util.Date;

/**
 * Abstract base class for an event.
 * 
 * @author lonny
 */
public abstract class Event {
    public final Messageable fromCSO;
    public final Messageable toCSO;
    public final Date timeSent;
 
	/**
     * Constructor
     * 
     * @param from long identifier of the CSO issuing the message
     * @param to long identifier of the CSO being messaged
     * @param sent Date sent
     * @param payload CSOPayload contains the CSO typed order
     */    
	public Event(Messageable from, Messageable to, Date sent, CSOPayload<?> payload) {
		this.fromCSO = from;
		this.toCSO = to;
		this.timeSent = sent;
		
		this.setEventSpecificProperties(payload);
	}
	
	protected abstract void setEventSpecificProperties(CSOPayload<?> payload);
}
