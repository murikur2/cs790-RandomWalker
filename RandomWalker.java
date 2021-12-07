/**
 * Title: RandomWalker.java
 * Function:
 * Implementation of a RandomWalk with a CSO Monitor
 * Based on details in Lecture 5 slides
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   murikur2@uwm.edu    initial refactoring from RandomWalk.java
**/


package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.CSO;
import edu.uwm.basecso.CSOMessage;
import edu.uwm.basecso.CSOPayload;
import edu.uwm.basecso.Messageable;
import java.awt.Point;
import java.util.Date;


/**
 * Encapsulates a random walk and the ability to adjust to directives about allowed directions. 
 * The recieveMessage() method can change the randomWalk's course of direction when needed.
 * 
 * A walker is Runnable to so it can be ran on a background thread and Messeageable
 * so it can participate in messages from a CSO.
 * 
 * @author lonny and anupam
 */	
public class RandomWalker extends Thread implements Messageable {
	private Point currentPoint, prevPoint = new Point(0,0);
	private RWDirectives currentDirectives = new RWDirectives(true, true, true, true);
	private final CSO monitor;
	private final long identifier;
	private boolean isEnabled = false;
	
    public RandomWalker(long id, CSO monitor, Point origin) {
        //Construct and initialize all variables
    	Point p = new Point(origin.x, origin.y);
    	this.setCurrentPoint(p);
    	
    	this.setPreviousPoint(new Point(p.x, p.y));
    	
        this.monitor = monitor;
        
        this.identifier = id;
        
        this.setPriority(MIN_PRIORITY);
        this.setName("Walker Thread " + id);
    }
	
    public void startWalk() {
    	Point cur = getCurrentPoint();
    	int x = cur.x;
    	int y = cur.y;
    	
    	while (isEnabled) {
		    double r = Math.random(); //0.0 < r < 1.0
		    
		    //simulate that it takes a bit of time to walk
		    StdDraw.pause(100);
		    
		    //check to see if the CSO has given us any advice and change r accordingly
		    RWDirectives directive = getCurrentDirectives();
		    
		    if (r < 0.25 && directive.isEastAllowed) x--; //go East
		    else if (r < 0.5 && directive.isWestAllowed) x++; //go West
		    else if (r < 0.75 && directive.isSouthAllowed) y--; //go South
		    else if (r < 1.00 && directive.isNorthAllowed) y++; //go North
		    
		    // send the CSO a message with current x,y
		    informMonitor(new Point(x,y));
    	 }
    }
    
    public void stopWalk() {
    	isEnabled = false;
    	
    	this.interrupt();
    }
    
    //Setter methods
    private synchronized void setCurrentDirectives(RWDirectives directives) {
        currentDirectives = directives;
    }
    

    private synchronized void setCurrentPoint(Point point) {
    	this.currentPoint = point;
    }
    
    public synchronized void setPreviousPoint(Point point) {
    	this.prevPoint = point;
    }
    
    //Getter methods    
    private synchronized RWDirectives getCurrentDirectives() {
    	Logger.log("Getting local current directive.");

        return currentDirectives;
    }
    
    public synchronized Point getCurrentPoint() {
    	return currentPoint;
    }
    
    public synchronized Point getPreviousPoint() {
    	return prevPoint;
    }
    
    public void informMonitor(Point position) {
    	this.setCurrentPoint(position);
        CSOPayload<Point> payload = new CSOPayload<Point>(position, 0, new Date(), new Date());
        CSOMessage message = new CSOMessage(this, monitor, null, payload, new Date());
        Logger.log("Sending new location.");
        monitor.receiveMessage(message); //sent from the main thread
      }
    
    @Override
    public boolean receiveMessage(CSOMessage message) {
        // the monitor will send us back advisory directions
        // should verify order type in the payload first
        // this method could be called from a diffferent thread, 
        // so we may need to synchronize updating the currentDirectives
        
        if (message != null) {
        	Logger.log("Walker " + this.identifier + " received new directive.");

            RWDirectives directives = (RWDirectives) message.payload.order; 
            setCurrentDirectives(directives);
        }
        
        return true;
    }


	@Override
	public void run() {
		//Run on new thread
		isEnabled = true;
		
		startWalk();
	}


	@Override
	public long getIdentifier() {
		return identifier;
	}

}