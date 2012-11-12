package guava.immutablecollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.google.common.collect.Queues;

public class QueuesTest{
	@Test
	public void newLinkedBlockingQueue(){
		LinkedBlockingQueue<Integer> q= Queues.newLinkedBlockingQueue(Arrays.asList(1, 2, 3, 4, 5));
		
		assertEquals(5, q.size());
		for(Integer expected : Arrays.asList(1, 2, 3, 4, 5))
			assertEquals(expected, q.poll());
		assertTrue(q.isEmpty());
	}
}
