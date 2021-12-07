/**
 * Title: RandomWalkController.java
 * Function:
 * Implementation of a RandomWalk with a CSO Monitor
 * Based on details in Lecture 5 slides
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
 *  11.03.2021	 murikur2@uwm.edu	 refactoring
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.CSO;
import edu.uwm.basecso.CSOMessage;
import edu.uwm.basecso.CSOPayload;
import edu.uwm.basecso.CyberspatialLocation;
import edu.uwm.basecso.GeospatialCoordinates;
import edu.uwm.basecso.InfospatialCoordinates;
import edu.uwm.basecso.SociospatialCoordinates;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implements a random walk controller that handles the setup of one or more walkers 
 * and drawing them.
 * 
 * The main method is the entry point and creates an assessor, generator, and executor 
 * for a CSO to monitor progress and issue directives. The CSO is passed into a RandomWalk instance
 * and then the walk is started.
 * 
 * The main walk loop in drawWalkers() is based on Lecture 5 slides.
 * 
 * @author lonny
 */	
public class RandomWalkController {
	private static long nextId = 0;
    private final int n; //nxn walk boundary 
    private final CSO monitor;
    private boolean isRunning = false;
    private ArrayList<RandomWalker> walkers;
    
    /**
     * Constructor
     * 
     * @param initialBoundary Size of the on screen area to be walked.
     * @param monitor Cannot be null at this time.
     */
    public RandomWalkController(int initialBoundary, CSO monitor) {
    	//construct randomWalk 
        n = initialBoundary;
        this.monitor = monitor;
        walkers = new ArrayList<RandomWalker>();
    }
    
    public void setUp() {
    	this.initDrawSurface();
    	int areaSize = 5;
    	
    	ArrayList<RWBoundary> areas = this.createCoverageAreas(areaSize);
    	
    	for (RWBoundary area : areas) {
    		this.createWalkerForArea(area);
    	}

        monitor.enable();
        
        StdDraw.show();
        StdDraw.pause(50);
    }
    
    private ArrayList<RWBoundary> createCoverageAreas(int areaSize) {
    	ArrayList<RWBoundary> areas = new ArrayList<>();
    	
        RWBoundary boundary = new RWBoundary(areaSize, new Point(0, 0));
        areas.add(boundary);
    	
        boundary = new RWBoundary(areaSize, new Point(45, 45));
        areas.add(boundary);
        
        boundary = new RWBoundary(areaSize, new Point(45, -45));
        areas.add(boundary);
        
        boundary = new RWBoundary(areaSize, new Point(-45, 45));
        areas.add(boundary);
        
        boundary = new RWBoundary(areaSize, new Point(-45, -45));
        areas.add(boundary);
    	
    	return areas;
    }
    
    private void createWalkerForArea(RWBoundary bounds) {
    	RandomWalker rw = new RandomWalker(getNextId(), monitor, bounds.center);
    	
    	walkers.add(rw);
    	
        CSOPayload<RWBoundary> payload = new CSOPayload<RWBoundary>(bounds, 0, new Date(), new Date());
        CSOMessage message = new CSOMessage(rw, monitor, null, payload, new Date());
        
        // send the CSO a payload with the initial walk boundary we want to maintain
        monitor.receiveMessage(message);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(bounds.center.x, bounds.center.y, 5);
    }
    
    private void initDrawSurface() {
        //setup background with StdDraw
        StdDraw.setScale(-n - 0.5, n + 0.5);
        StdDraw.clear(StdDraw.GRAY); //background color
        StdDraw.enableDoubleBuffering();
        
        StdDraw.show();
    }
    
    private void drawWalkers() {
      while(isRunning) {
    	 for (RandomWalker rw : walkers) {
    		 this.drawWalker(rw);
    	 }
    	 
    	 StdDraw.show();
    	 StdDraw.pause(50);
      } 
    }
    
    private void drawWalker(RandomWalker rw) {
    	//Retrieve variables from randomWalkers 
	
		Point p = rw.getCurrentPoint();
		Point prev = rw.getPreviousPoint();
		
		StdDraw.setPenColor(StdDraw.WHITE); //past steps
        StdDraw.filledSquare(prev.x, prev.y, 0.5); 
       
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledSquare(p.x, p.y, 0.5);

   
        rw.setPreviousPoint(new Point(p.x, p.y));
    }
    
    public void start() {
    	this.isRunning = true;
    	
	   	 for (RandomWalker rw : walkers) {
			 rw.start();
		 }
    	
    	this.drawWalkers();
    }
    
    public void stop() {
    	this.isRunning = false;
    	
      	 for (RandomWalker rw : walkers) {
    		 rw.stopWalk();
    	 }
    }

    private static long getNextId() {
    	return nextId++;
    }
    
    public static void main(String[] args) {
//      Logger.setEnabled(false);
    	
     
      RWPlanExecutor executor = new RWPlanExecutor();
      RWPlanGenerator generator = new RWPlanGenerator(executor);
      RWSituationAssessor assessor = new RWSituationAssessor(generator);
      
      CyberspatialLocation location = new CyberspatialLocation(new GeospatialCoordinates(0,0,0), new InfospatialCoordinates(0,0,0), new SociospatialCoordinates(0,0,0));
            
      CSO monitor = new CSORandomWalkMonitor(getNextId(), location, assessor, generator, executor);
       
      //create and setup walkers
      RandomWalkController walkController = new RandomWalkController(50, monitor);  

      walkController.setUp();
      
      //draw walkers, this will loop until stopped.
      walkController.start();

 
      monitor.disable();
    }

}
