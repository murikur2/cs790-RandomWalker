/**
 * Title: RWPlanGenerator.java
 * Function:
 * Implements generating a plan.
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Exchanger;
import edu.uwm.basecso.PlanGenerationService;

/**
 * A class to generate an RWPlan from an RWAssessment.
 * 
 * @author lonny and anupam
 */
public class RWPlanGenerator extends PlanGenerationService {
    private Exchanger<Queue<RWAssessment>> assessmentExchanger;
    private RWPlanExecutor planExecutor;
    
    /**
     * Constructor
     * 
     * @param planExecutor RWPlanExecutor
     */
    public RWPlanGenerator(RWPlanExecutor planExecutor) {
		super();
		this.planExecutor = planExecutor;
	}
    
    /**
    * Kicks off the plan generation to exchange queues with
    * the situation assessor in a loop.
    * 
    * @param assessments queue of RWAssessment
    */
    @Override
    public void generatePlans(Queue<RWAssessment> assessments) {
    	Logger.log("In plan generator.");
    	
    	RWPolicyProcess policyProcess = new RWPolicyProcess(assessmentExchanger);
    	policyProcess.processPolicies();  	
    	
    	Logger.log("generatePlan run loop has completed.");
    }
    
    /**
     * Must set up an Exchanger to pass in and receive assessment queues
     * 
     * @param exchanger An Exchanger of RWAssessment queues across threads.
     */
    public void setExchanger(Exchanger<Queue<RWAssessment>> exchanger) {
    	this.assessmentExchanger = exchanger;
    }
    
    /**
     * Class to handle applying policies.
     *
     */
    private class RWPolicyProcess {		
    	private Exchanger<Queue<RWAssessment>> coaExchanger;
    	private Exchanger<Queue<RWPlan>> planExchanger = new Exchanger<Queue<RWPlan>>();
		private Queue<RWPlan> plans = new LinkedList<>();
		private Queue<RWAssessment> coas = new LinkedList<>();
    	
    	public RWPolicyProcess(Exchanger<Queue<RWAssessment>> coaExchanger) {
			super();
			this.coaExchanger = coaExchanger;
		}
		
		private void processPolicies() {
			Logger.log("Start policy process.");

			RWResourceProcess resourceProcess = new RWResourceProcess(planExchanger);
			resourceProcess.start();
			
			DualExchangeProcessor<Queue<RWAssessment>, Queue<RWPlan>> processor = new DualExchangeProcessor<>(coaExchanger, planExchanger, this::checkPolicies);
			processor.startExchange(coas, plans);
			
			Logger.log("PolicyProcess run loop has completed.");
		}


		private Queue<RWPlan> checkPolicies(Queue<RWAssessment> coas) {	
			Logger.log("Checking " + coas.size() + " courses of action against policies.");
			
			while (!coas.isEmpty()) {
				RWAssessment coa = coas.poll();
				RWPlan plan = new RWPlan(coa.directives, coa.to);
				plans.add(plan);
			}
			
			return plans;
		}  	
    }
    
    /**
     * Class to allocate resources, create a plan of action, and send to plan execution.
     *
     */
    private class RWResourceProcess extends Thread {
    	private Exchanger<Queue<RWPlan>> planExchanger;
    	private Exchanger<Queue<RWPlan>> actionPlanExchanger = new Exchanger<Queue<RWPlan>>();
    	private Queue<RWPlan> actionPlans = new LinkedList<>();
    	private Queue<RWPlan> plans = new LinkedList<>();
   	
		public RWResourceProcess(Exchanger<Queue<RWPlan>> exchanger) {
			this.planExchanger = exchanger;
			
			this.setName("Analysis Thread");
			this.setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			processResources();
		}
		
		private void processResources() {
			Logger.log("Start ResourceProcess.");
			
			planExecutor.setExchanger(actionPlanExchanger);
			planExecutor.start();
			
			DualExchangeProcessor<Queue<RWPlan>, Queue<RWPlan>> processor = new DualExchangeProcessor<>(planExchanger, actionPlanExchanger, this::checkResources);
			processor.startExchange(plans, actionPlans);
			
			Logger.log("ResourceProcess run loop has completed.");			
		}

		private Queue<RWPlan> checkResources(Queue<RWPlan> plans) {
			Logger.log("Checking resources for " + plans.size() + " plans.");
	        
	        while (!plans.isEmpty()) {	
	        	RWPlan plan = plans.poll();
	        	
	        	actionPlans.add(plan);
	        }
	        
	        return actionPlans;
		}	
    }
    
}
