package edu.uwm.basecso;

import java.util.Date;

/**
 * Abstract base class for a plan of record.
 * 
 * @author anupam
 */
public abstract class PlanOfRecord {
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
	public PlanOfRecord(Messageable from, Messageable to, Date sent, CSOPayload<?> payload) {
		this.fromCSO = from;
		this.toCSO = to;
		this.timeSent = sent;
		
		this.setRecordPlanSpecificProperties(payload);
	}
	
	protected abstract void setRecordPlanSpecificProperties(CSOPayload<?> payload);
}
