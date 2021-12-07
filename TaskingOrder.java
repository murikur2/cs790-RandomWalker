package edu.uwm.basecso;

import java.util.Date;

/**
 * Abstract base class for a tasking order.
 * 
 * @author anupam
 */
public abstract class TaskingOrder {
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
	public TaskingOrder(Messageable from, Messageable to, Date sent, CSOPayload<?> payload) {
		this.fromCSO = from;
		this.toCSO = to;
		this.timeSent = sent;
		
		this.setTaskingOrdersSpecificProperties(payload);
	}
	
	protected abstract void setTaskingOrdersSpecificProperties(CSOPayload<?> payload);
}
