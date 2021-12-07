/**
 * Title: SituationAssessmentService.java
 * Function:
 * Super class intended to be implemented by a sub-class
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
 *  10.24.2021   lghoward@uwm.edu    made generic
 *  11.03.2021   lghoward@uwm.edu	 removed generics and extended Thread
**/

package edu.uwm.basecso;

import java.util.Queue;

/**
 * Abstract class that extends Thread and is the base for a concrete SAS.
 * 
 * Concrete subclasses implement doAssessments with a message queue
 * to start a run loop to process messages and move them 
 * through a pipeline to do the assessment work.
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 */
public abstract class SituationAssessmentService extends Thread {
	protected Queue<CSOMessage> messages;
	
	
	/**
	 * Constructor
	 */
	public SituationAssessmentService() {
    	this.setName("Assessment Thread");
    	this.setPriority(Thread.MIN_PRIORITY);
	}
	
	@Override
	public void run() {
		doAssessments(messages);
	}
	
    /**
     * Must be implemented by a concrete subclass.
     * 
     * @param messages
     */
    protected abstract void doAssessments(Queue<CSOMessage> messages);
}
