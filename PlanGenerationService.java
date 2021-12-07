/**
 * Title: PlanGenerationService.java
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

import edu.uwm.cs790.assignment9.RWAssessment;

/**
 * Abstract class that extends Thread and is the base for a concrete PGS.
 * 
 * Concrete subclasses must implement generatePlans with a queue
 * to start a run loop to process the assessments and move them 
 * through a pipeline to do the plan generation work.
 *
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 */
public abstract class PlanGenerationService extends Thread{
	protected Queue<RWAssessment> assessments;

	/**
	 * Constructor
	 */
	public PlanGenerationService() {
    	this.setName("Plan Generation Thread");
    	this.setPriority(Thread.MIN_PRIORITY);
	}
	
	@Override
	public void run() {
		generatePlans(assessments);
	}
	
	
    /**
     * Must be implemented by a concrete subclass.
     * Will return some type representing a plan, which will be handled by a PlanExecutionService.
     * 
     * @param assessments A queue of assessments to handle.
     */
    public abstract void generatePlans(Queue<RWAssessment> assessments);
}

