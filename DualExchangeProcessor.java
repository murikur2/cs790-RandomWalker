package edu.uwm.cs790.assignment9;

import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.function.Function;

/**
 * A DualExchangeProcessor encapsulates the logic of a run loop that exchanges a {@link Queue} of data in and another {@link Queue} of data out using {@link Exchanger} instances.
 * It processes the data in the inbound {@link Queue} using the {@link Function} passed in the constructor. The {@link Function} then returns a new {@link Queue} of data
 * that is exchanged through the outbound {@link Exchanger}.
 * 
 * Useful for implementing a pipeline of producer-consumer {@link Thread} instances that need to exchange queues of data, process the data, and pass a {@link Queue}
 * of processed data to the next stage in the pipeline.
 * 
 * @author lonny
 *
 * @param <IN> generic type parameter for any {@link Queue}
 * @param <OUT> generic type parameter for any {@link Queue}
 */
public class DualExchangeProcessor<IN extends Queue<?>, OUT extends Queue<?>> {
	private final Exchanger<IN> inExchanger;
	private final Exchanger<OUT> outExchanger;
	private final Function<IN, OUT> process;
	
	/**
	 * Constructor that takes a inbound and outbound {@link Exchanger} and a Function to handle processing the data.
	 * 
	 * @param inExchanger an {@link Exchanger} that exchanges queues
	 * @param outExchanger an {@link Exchanger} that exchanges queues
	 * @param process a {@link Function} that takes a {@link Queue}, processes the data, and returns a new {@link Queue} to be exchanged
	 */
	public DualExchangeProcessor(Exchanger<IN> inExchanger, Exchanger<OUT> outExchanger, Function<IN, OUT> process) {
		super();
		this.inExchanger = inExchanger;
		this.outExchanger = outExchanger;
		this.process = process;
	}

	/**
	 * Method to start exchanging the passed in {@link Queue} instances in a run loop.
	 * The loop will continue as long as the inbound {@link Queue} is empty.
	 * In other words, to continue exchanging, all data in the inbound {@link Queue} must be consumed
	 * by the {@link Function} passed in the constructor and new data added to the outbound {@link Queue}
	 * so the empty {@link Queue} can be exchanged back with the Thread that owns the inbound side
	 * and the outbound {@link Queue} will have data to exchange to the next Thread.
	 * 
	 * @param inQueue any type of {@link Queue} of inbound data to be processed and then exchanged
	 * @param outQueue any type of {@link Queue} of outbound data to be exchanged
	 */
	public void startExchange(IN inQueue, OUT outQueue) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			Logger.log(e1.getMessage());
		}
		
		do {
			try {
				//Logger.log("Exchanging inbound queue.");
				inQueue = inExchanger.exchange(inQueue);
				//Logger.log("Finished exchanging inbound queue. Size: " + inQueue.size());
			
				if (!inQueue.isEmpty()) {
					outQueue = process.apply(inQueue);
				}
				
				//Logger.log("Exchanging outbound queue.");
				outQueue = outExchanger.exchange(outQueue);
				//Logger.log("Finished exchanging outbound queue. Size: " + outQueue.size());

			} catch (Exception e) {
				Logger.log("Exception occured in dualExchanger: " + e.getMessage());
			}

			
		} while (inQueue.isEmpty());
	}
	
}
