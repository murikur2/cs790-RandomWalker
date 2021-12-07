/**
 * Title: CSO.java
 * Function:
 *  Initial implementation of a CSO super class. 
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Abstract base class representing a CSO.
 * Receiving and storing messages is implemented here,
 * but processing the messages must be implemented by a subclass.
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 */
public abstract class CSO implements Messageable {
    protected CyberspatialLocation cyberspatialLocation;
    protected final SituationAssessmentService situationAssessmentService;
    protected final PlanGenerationService planGenerationService;
    protected final PlanExecutionService planExecutionService;
    protected BlockingQueue<CSOMessage> messages;
    private final long identifier;

    /**
     * Constructor of the CSO object. It is expected that subclasses will call super.
     * @param id long identifier for the instance
     * @param location CyberspatialLocation initial cyberspatial location for the instance, can only be modified through messages or a result of the behavior in fulfilling an order
     * @param sas SituationAssessmentService to provide situational assessment. a subclass will use as needed.
     * @param pgs PlanGenerationService to provide generate a plan. a subclass will use as needed.
     * @param pes PlanExecutionService to provide execution of a plan. a subclass will use as needed.
     */
    public CSO(long id, CyberspatialLocation location, SituationAssessmentService sas, PlanGenerationService pgs, PlanExecutionService pes) {

	//assign
        this.cyberspatialLocation = location;
        this.situationAssessmentService = sas;
        this.planGenerationService = pgs;
        this.planExecutionService = pes;
        
        this.identifier = id;
        
        this.messages = new ArrayBlockingQueue<>(100);
    }
    
    /**
     *  Once an instance is created, this will handle any startup needed by starting any needed threads, timers, etc.
     */
    public abstract void enable();
    
    /**
     *  This will handle any shutdown needed by stopping any needed threads, timers, etc.
     */
    public abstract void disable();

    /**
     * Function to receive a message from a different object. Messages will be stored and acted upon the next time processMessages is called.
     * @param message CSOMessage that wraps a payload with an implementation specific order
     * @return boolean indicating whether the message was accepted. 
     */
    public boolean receiveMessage(CSOMessage message) {
        //enqueue message
        messages.offer(message);
        
        return true; //perhaps could be a Future if the caller would want to wait on the result rather than sending a new message with the results 
    }
    
    protected void sendMessage(CSOMessage message) {
        //send message to an endpoint
        //the receiver is part of the message object
        if (message.toCSO != null) {
            message.toCSO.receiveMessage(message);
        }
    }

    /**
     * Allows access to the identifier provided upon creation.
     * @return long as an identifier representing the instance.
     */
    public long getIdentifier() {
       return identifier;
    }
    
    /**
     * Allows access to the current cyberspatial location
     * @return current CyberspatialLocation location
     */
    public CyberspatialLocation getLocation() {
        return this.cyberspatialLocation;
    }

    //pop message from the queue and do something with it using various injected services
    //perhaps rename to run depending on how threading will be handled
    protected abstract void processMessages();

}
