/**
 * Title: RWPlan.java
 * Function:
 * Simple wrapper to represent a plan.
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.Messageable;

/**
 * A simple class to represent a Plan.
 * 
 * @author lonny
 */

public class RWPlan {

    public final RWDirectives directives;
    public final Messageable to;
    
    /**
    * Create a plan using directives and a message
    * 
    * @param d RWDirectives 
    * @param m Messageable
    */	
    public RWPlan(RWDirectives d, Messageable m) {
      this.directives = d;
      this.to = m;
    }
}
