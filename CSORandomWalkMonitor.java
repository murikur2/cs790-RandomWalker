/**
 * Title: CSORandomWalkMonitor.java
 * Function:
 *  Concrete implementation of a CSO that can monitor a RandomWalk
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.CSO;
import edu.uwm.basecso.CSOMessage;
import edu.uwm.basecso.CyberspatialLocation;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Exchanger;


/**
 * RandomWalkMonitor extends CSO base object.
 * The RandomWalkMonitor has a constructor that takes an id,
 * CyberspatialLocation, RWSituationAssesor, RWPlanGenerator, and RWPlanExecutor.
 * 
 * Once an instance is created, it must be enabled() to start processing messages
 * and disabled() to stop.
 * 
 * The receiveMessages() method will take any CSOMessages sent and enqueue them
 * for later processing.
 * 
 * The private processMessages() method takes the messages and passes them on 
 * to the RWSituationAssessor.
 * 
 * @author lonny
 */

//TODO attempted test commit -jlane13 11/19
public class CSORandomWalkMonitor extends CSO {
    ExecutorService ex;
    protected boolean isEnabled = false;

    /**
     * Constructor
     * 
     * @param id long identifier
     * @param location CyberspatialLocation
     * @param sas RWSituationAssessor
     * @param pgs RWPlanGenerator
     * @param pes RWPlanExecutor
     */
    public CSORandomWalkMonitor(long id, CyberspatialLocation location, RWSituationAssessor sas, RWPlanGenerator pgs, RWPlanExecutor pes) {
        super(id, location, sas, pgs, pes);
        
        ex = newSingleThreadExecutor(); 
    }

    @Override
    public void enable() {
        Runnable r = () -> {
            this.processMessages();
        };
        
        this.isEnabled = true;
        
        ex.submit(r);
    }

    @Override
    public void disable() {
        this.isEnabled = false;
        messages.clear();
        ex.shutdown();
    }
    
    @Override
    protected void processMessages() {
    	Thread.currentThread().setName("CSO Processing Thread " + this.getIdentifier());
    	
    	Exchanger<Queue<CSOMessage>> exchanger = new Exchanger<Queue<CSOMessage>>();
    	((RWSituationAssessor) situationAssessmentService).setExchanger(exchanger);
    	
    	Queue<CSOMessage> messageList = new LinkedList<>();
    	
    	situationAssessmentService.start();
    	
        while (isEnabled) {
            try {
            	while (!messages.isEmpty()) {
	                Logger.log("Taking message from queue.");
	                CSOMessage message = messages.take();

	                messageList.add(message);
            	}
                
            	Logger.log("Exchanging message queue of " + messageList.size() + " messages with System Assessment.");
            	messageList = exchanger.exchange(messageList);
            	
            	// playing with the pause to get multiple messages to be processed
            	// in a batch
            	Thread.sleep(70);
                
            } catch (Exception ex) {
            	Logger.log("Caught exception. " + ex.getMessage());
            }
        }
    }

}
