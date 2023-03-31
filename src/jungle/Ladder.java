/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * @author davew
 *
 * The Ladder class is NOT a kind of thread,
 *  since it doesn't actually do anything except get used by Apes.
 * The ladder just keeps track of how many apes are on each rung (always 0 or 1).
 */

public class Ladder {
	private final int[] rungCapacity;
	
	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++)
			rungCapacity[i] = 1;
	}
	public int nRungs() {
		return rungCapacity.length;
	}
	// return True if you succeed in grabbing the rung
	Semaphore sem = new Semaphore(1);
	ArrayList<Semaphore> semList = new ArrayList<>();

	public synchronized boolean grabRung(int which) throws InterruptedException {
		if (rungCapacity[which] < 1) {
			return false;
		} else {
			sem.acquire(which);
			rungCapacity[which]--;
			return true;
		}
	}
	public synchronized void releaseRung(int which) {
		rungCapacity[which]++;
		sem.release(which);
	}
}
