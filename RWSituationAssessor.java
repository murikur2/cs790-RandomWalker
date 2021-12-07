/**
 * Title: RWSituationAssessor.java
 * Function:
 *  Concrete implementation of a SituationAssessmentService.
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
 *  11.03.2021   lghoward@uwm.edu    refactor to add pipeline and more threads
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.CSOMessage;
import edu.uwm.basecso.CSOPayload;
import edu.uwm.basecso.SituationAssessmentService;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * Class that implements a SituationAssessmentService.
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.3.0
 * 
 */
public class RWSituationAssessor extends SituationAssessmentService {
    private Exchanger<Queue<CSOMessage>> messageQueueExchanger;
    private RWPlanGenerator planGenerator;
    
    /**
     * Constructor
     * 
     * @param generator RWPlanGenerator
     */
    public RWSituationAssessor(RWPlanGenerator generator) {
		super();
		this.planGenerator = generator;
		messages = new LinkedList<>();
	}
    
    /**
     * Must set up an Exchanger to pass in and receive message queues
     * 
     * @param exchanger An Exchanger of message queues across threads.
     */
    public void setExchanger(Exchanger<Queue<CSOMessage>> exchanger) {
    	this.messageQueueExchanger = exchanger;
    }
    
	/**
     * Extracts message payload to determine bounds.
     * Assesses data points compared to boundaries
     * in order to set directives (allowNorth, allowSouth, allowWest, allowEast).
     * 
     * @param messages
     * @return void
     */
    @Override
    protected void doAssessments(Queue<CSOMessage> messages) {   	
    	Logger.log("In situation assessor.");
		
		RWFilterProcess filterProcess = new RWFilterProcess(messageQueueExchanger);
		filterProcess.processFilters();
		
		Logger.log("Assessor run loop has completed.");
    }
   
    
    /**
     * Class to handle filtering messages (information).
     *
     */
    private class RWFilterProcess {
    	private Set<Class<?>> filterDatabase = new HashSet<>();
    	private Exchanger<Queue<CSOMessage>> informationExchanger;
    	private Exchanger<Queue<CSOMessage>> eventExchanger = new Exchanger<Queue<CSOMessage>>();
		Queue<CSOMessage> information = new LinkedList<>();
		Queue<CSOMessage> events = new LinkedList<>();
    	
    	public RWFilterProcess(Exchanger<Queue<CSOMessage>> exchanger) {
    		super();
    		this.informationExchanger = exchanger;
    		
    		// fill database with payload types of interest
    		filterDatabase.add(Point.class);
    		filterDatabase.add(RWBoundary.class);
    	}
		
		private void processFilters() {
			Logger.log("Start filter process.");

			RWTriageProcess triage = new RWTriageProcess(eventExchanger);
			triage.start();

			DualExchangeProcessor<Queue<CSOMessage>, Queue<CSOMessage>> processor = new DualExchangeProcessor<>(informationExchanger, eventExchanger, this::doFiltering);			
			processor.startExchange(information, events);
			
			Logger.log("Filter run loop has completed.");
		}
		
		private Queue<CSOMessage> doFiltering(Queue<CSOMessage> information) {
			Logger.log("Filtering " + information.size() + " messages.");
			
			while (!information.isEmpty()) {		
				CSOMessage message = information.poll();
				
				if (filterMessages(message)) {
					events.add(message);
				}
			}
			
			return events;
		}
		
	    private boolean filterMessages(CSOMessage message) {
	    	CSOPayload<?> payload = message.payload;
	    	
	    	return filterDatabase.contains(payload.order.getClass());
	    }
    	
    }
    
    /**
     * Class to triage events.
     *
     */
    private class RWTriageProcess extends Thread {
		private Exchanger<Queue<CSOMessage>> eventExchanger;
    	private Exchanger<Queue<CSOMessage>> situationExchanger = new Exchanger<Queue<CSOMessage>>();
    	private Queue<CSOMessage> situations = new LinkedList<>();
		private Queue<CSOMessage> events = new LinkedList<>();
    	
    	
    	public RWTriageProcess(Exchanger<Queue<CSOMessage>> eventExchanger) {
			super();
			this.eventExchanger = eventExchanger;
			
			this.setName("Triage Thread");
			this.setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			processTriage();
		}

		private void processTriage() {
			Logger.log("Start triage process.");

			RWAnalysisProcess anaylizer = new RWAnalysisProcess(situationExchanger);
			anaylizer.start();

			DualExchangeProcessor<Queue<CSOMessage>, Queue<CSOMessage>> processor = new DualExchangeProcessor<>(eventExchanger, situationExchanger, this::triageEvents);
			processor.startExchange(events, situations);
			
			Logger.log("Triage run loop has completed.");
		}
		
		private Queue<CSOMessage> triageEvents(Queue<CSOMessage> events) {
			ArrayList<CSOMessage> boundaries = new ArrayList<>();
			ArrayList<CSOMessage> directions = new ArrayList<>();
			
			Logger.log("Triaging " + events.size() + " events.");
			
			//reorder the messages to prefer handling boundaries first
			// need to design a more general way
			while (!events.isEmpty()) {

				CSOMessage event = events.poll();
				CSOPayload<?> payload = event.payload;
				
				if (payload.order instanceof RWBoundary) {
					boundaries.add(event);
		        } else if (payload.order instanceof Point) {
		        	directions.add(event);
		        }
			}
			
			Logger.log("triage ordering boundary events first");
			situations.addAll(boundaries);
			
			Logger.log("triage ordering position events second");
			situations.addAll(directions);
			
			return situations;
		}
    	
    }
    
    /**
     * Class to handle analysis and send to plan generation.
     *
     */
    private class RWAnalysisProcess extends Thread {
    	private Exchanger<Queue<CSOMessage>> situationExchanger;
    	private HashMap<Long, RWBoundary> boundaryMap = new HashMap<>();
    	private Exchanger<Queue<RWAssessment>> coaExchanger = new Exchanger<Queue<RWAssessment>>();
    	private Queue<RWAssessment> coas = new LinkedList<RWAssessment>();
    	private Queue<CSOMessage> situations = new LinkedList<>();
    	
		public RWAnalysisProcess(Exchanger<Queue<CSOMessage>> exchanger) {
			this.situationExchanger = exchanger;

			this.setName("Analysis Thread");
			this.setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			Logger.log("Start analysis process.");
			
			planGenerator.setExchanger(coaExchanger);
			planGenerator.start();
			
			DualExchangeProcessor<Queue<CSOMessage>, Queue<RWAssessment>> processor = new DualExchangeProcessor<>(situationExchanger, coaExchanger, this::analyze);
			processor.startExchange(situations, coas);
			
			Logger.log("Analysis run loop has completed.");
			
		}

		private Queue<RWAssessment> analyze(Queue<CSOMessage> situations) {
			Logger.log("Analyizing " + situations.size() + " situations.");
	        
	        while (!situations.isEmpty()) {
	        	CSOMessage situation = situations.poll();
		        CSOPayload<?> payload = situation.payload;
		        RWAssessment assessment = null;
		        
		        if (payload.order instanceof RWBoundary) {
		        	Logger.log("Got boundary.");
		            RWBoundary order = (RWBoundary) payload.order;
		            
		            long id = situation.fromCSO.getIdentifier();
		            Logger.log("Adding boundary of " + order.bound + " for identifier: " + id);
		            boundaryMap.put(id, order);
		            
		            RWDirectives directive = new RWDirectives(true, true, true, true);
		            assessment = new RWAssessment(directive, situation.fromCSO);
	
		        } else if (payload.order instanceof Point) {
		        	Logger.log("Got new location. " + (Point) payload.order);
		            Point p = (Point) payload.order;
		            
		            long id = situation.fromCSO.getIdentifier();
		            Logger.log("Finding boundary for identifier: " + id);
		            RWBoundary boundary = new RWBoundary(1); //just in case we can't find one.
		            
		            if (boundaryMap.containsKey(id)) {
		            	Logger.log("Found boundary for identifier: " + id);
		            	boundary = boundaryMap.get(id);
		            }
		            
		            boolean allowNorth = true, allowSouth = true, allowEast = true, allowWest = true;
		            
		            if (p.x < boundary.center.x && (p.x - 1) <= boundary.getLeftLimit()) {
		                allowWest = false;
		                Logger.log("Don't allow West. " + p.x);
		            } else if (p.x > boundary.center.x && (p.x + 1) >= boundary.getRightLimit()) {
		                allowEast = false;
		                Logger.log("Don't allow East. " + p.x);
		            }
		            
		            if (p.y < boundary.center.y && (p.y - 1) <= boundary.getBottomLimit()) {
		                allowSouth = false;
		                Logger.log("Don't allow South. " + p.y);
		            } else if (p.y > boundary.center.y && (p.y + 1) >= boundary.getTopLimit()) {
		                allowNorth = false;
		                Logger.log("Don't allow North. " + p.y);
		            }
		          
		            RWDirectives directive = new RWDirectives(allowNorth, allowSouth, allowWest, allowEast);
		            assessment = new RWAssessment(directive, situation.fromCSO);
		        }
		        
		        coas.add(assessment);
	       }
	        
	        return coas;
		}
    	
    }


}
