/**
 * Title: RWPlanExecutor.java
 * Function:
 * Implements executing a plan.
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.CSOMessage;
import edu.uwm.basecso.CSOPayload;
import edu.uwm.basecso.PlanExecutionService;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Exchanger;

/**
 *  Executes a plan of type RWPlan.
 * 
 * @author lonny
 */

public class RWPlanExecutor extends PlanExecutionService{
	private Exchanger<Queue<RWPlan>> actionPlanExchanger;
	
	public void processPlans(Queue<RWPlan> actionPlans) {
    	Logger.log("In plan executor.");
    	
    	RWCommandProcess commandProcess = new RWCommandProcess(actionPlanExchanger);
    	commandProcess.processCommands();    	
    	
    	Logger.log("generatePlan run loop has completed.");
	}

    /**
     * Must set up an Exchanger to pass in and receive plan queues
     * 
     * @param exchanger An Exchanger of RWPlan queues across threads.
     */
    public void setExchanger(Exchanger<Queue<RWPlan>> exchanger) {
    	this.actionPlanExchanger = exchanger;
    }
    
    /**
     * Class to handle authorizing plans.
     *
     */
    private class RWCommandProcess {		
    	private Exchanger<Queue<RWPlan>> actionPlanExchanger;
    	private Exchanger<Queue<RWPlan>> planExchanger = new Exchanger<Queue<RWPlan>>();
		private Queue<RWPlan> actionPlans = new LinkedList<>();
		private Queue<RWPlan> plans = new LinkedList<>();
    	
    	public RWCommandProcess(Exchanger<Queue<RWPlan>> actionPlanExchanger) {
			super();
			this.actionPlanExchanger = actionPlanExchanger;
		}

		public void processCommands() {
			Logger.log("Start command process.");

			RWExecuteProcess executeProcess = new RWExecuteProcess(planExchanger);
			executeProcess.start();
			
			DualExchangeProcessor<Queue<RWPlan>, Queue<RWPlan>> processor = new DualExchangeProcessor<>(actionPlanExchanger, planExchanger, this::authorizePlans);
			processor.startExchange(actionPlans, plans);
			
			Logger.log("CommandProcess run loop has completed.");
		}


		private Queue<RWPlan> authorizePlans(Queue<RWPlan> actionPlans) {
			Logger.log("Authorizing " + actionPlans.size() + " plans.");
			
			while (!actionPlans.isEmpty()) {

				RWPlan plan = actionPlans.poll();

				plans.add(plan);
			}
			
			return plans;
		}  	
    }
    
    /**
     * Class to handle executing plans.
     *
     */
    private class RWExecuteProcess extends Thread {
    	private Exchanger<Queue<RWPlan>> planExchanger;
    	private Queue<RWPlan> plans = new LinkedList<>();
   	
		public RWExecuteProcess(Exchanger<Queue<RWPlan>> exchanger) {
			this.planExchanger = exchanger;
			
			this.setName("Executer Thread");
			this.setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			processPlans();
		}
		
		private void processPlans() {
			Logger.log("Start ExecuteProcess.");
			
			SingleExchangeProcessor<Queue<RWPlan>> processor = new SingleExchangeProcessor<>(planExchanger, this::executePlans);
			processor.startExchange(plans);
			
			Logger.log("ExecuteProcess run loop has completed.");
		}

		
		private void executePlans(Queue<RWPlan> plans) {
			Logger.log("Executing " + plans.size() + " plans.");
	        
	        while (!plans.isEmpty()) {	
	        	RWPlan plan = plans.poll();
	        	
	        	executePlan(plan);
	        }
		}	
		
	    /**
	    * Creates message from plan.
	    * Uses message to execute plan on randomWalk
	    * 
	    * @param plan RWPlan
	    */
	    private void executePlan(RWPlan plan) {
	        if (plan.to == null) return;
	        
	        CSOPayload<RWDirectives> payload = new CSOPayload<RWDirectives>(plan.directives, 0, new Date(), new Date());
	        CSOMessage message = new CSOMessage(null, plan.to, null, payload, new Date());
	        
	        plan.to.receiveMessage(message);        
	    }
    }
	
	


}
