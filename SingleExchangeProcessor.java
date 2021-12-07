package edu.uwm.cs790.assignment9;

import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.function.Consumer;

/**
 * A SingleExchangeProcessor encapsulates the logic of a run loop that exchanges a {@link Queue} of data in from another {@link Thread} using an {@link Exchanger}.
 * It processes the data in the inbound {@link Queue} using the {@link Consumer} passed in the constructor. The {@link Consumer} does some sort of work with the {@link Queue}
 * that is exchanged through the inbound {@link Exchanger}.
 * 
 * Useful for implementing the end of a pipeline of producer-consumer {@link Thread} instances that need to exchange a {@link Queue} of data to process.
 * 
 * @author lonny
 *
 * @param <IN> generic type parameter for any {@link Queue}
 */
public class SingleExchangeProcessor<IN extends Queue<?>> {
	private final Exchanger<IN> inExchanger;
	private final Consumer<IN> process;
	
	/**
	 * Constructor that takes an {@link Exchanger} that exchanges a {@link Queue} and a {@link Consumer} to consume the data in the queue. 
	 * 
	 * @param inExchanger an {@link Exchanger} that exchanges queues
	 * @param process a {@link Consumer} to handle processing the data in a {@link Queue}, but returns nothing
	 */
	public SingleExchangeProcessor(Exchanger<IN> inExchanger, Consumer<IN> process) {
		super();
		this.inExchanger = inExchanger;
		this.process = process;
	}
	
	/**
	 * Method to start processing and exchanging the passed in {@link Queue} instance in a run loop.
	 * The loop will continue as long as the inbound {@link Queue} is empty.
	 * In other words, to continue exchanging, all data in the inbound {@link Queue} must be consumed
	 * by the {@link Consumer} passed in the constructor so the empty {@link Queue} can be exchanged back with 
	 * the {@link Thread} that owns the inbound side.
	 * 
	 * @param inQueue any type of {@link Queue} of inbound data to be processed and then exchanged
	 */
	public void startExchange(IN inQueue) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			Logger.log(e1.getMessage());
		}
		
		do {
			try {
				//Logger.log("Exchanging inbound queue.");
				inQueue = inExchanger.exchange(inQueue);
				//Logger.log("Finished exchanging inbound queue. Size: " + assessments.size());
			
				if (!inQueue.isEmpty()) {
					process.accept(inQueue);
				}

			} catch (Exception e) {
				Logger.log("Exception occured in dualExchanger: " + e.getMessage());
			}

			
		} while (inQueue.isEmpty());
	}
	
}
