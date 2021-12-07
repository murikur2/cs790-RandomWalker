/**
 * Title: RWAssessment.java
 * Function:
 * Simple wrapper to represent a situation assessment.
 * Based on details in Lecture 5 slides
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import edu.uwm.basecso.Messageable;

/**
 * Simple wrapper to represent a situation assessment.
 * 
 * @author lonny
 */
	
public class RWAssessment {
    public final RWDirectives directives;
    public final Messageable to;
    
  
    /**
     * Constructor stores directives and message
     * 
     * @param d RWDirective
     * @param m Messageable
     */
    public RWAssessment(RWDirectives d, Messageable m) {
      this.directives = d;
      this.to = m;
    }
}
