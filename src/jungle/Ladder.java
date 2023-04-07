/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.Timer;

/**
 * @author davew
 *
 * The Ladder class is NOT a kind of thread,
 *  since it doesn't actually do anything except get used by Apes.
 * The ladder just keeps track of how many apes are on each rung (always 0 or 1).
 */

public class Ladder {
	private final int[] rungCapacity;
	private final Semaphore[] sem;
	private final Semaphore[] eastBuffer;
	private final Semaphore[] westBuffer;
	private boolean ladderOccupied = true;
	private boolean direction = true;

	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		sem = new Semaphore[_nRungs];
		eastBuffer = new Semaphore[1000];
		westBuffer = new Semaphore[1000];
		// capacity 1 available on each rung
		for (int i=0; i<_nRungs; i++) {
			rungCapacity[i] = 1;
			sem[i] = new Semaphore(1);
		}


	}
	public int nRungs() {
		return rungCapacity.length;
	}
	// return True if you succeed in grabbing the rung


	public boolean grabRung(int which) throws InterruptedException { //i just realized, should i not have the sync keyword here? It seems like It's still working though?
//		if (rungCapacity[which] < 1) {
//			return false;
			changeSides(which);
			sem[which].acquire(); //i need advice on how to do the next part. Should we just put more logic into the semaphores such as more/different aquires+releases, using the wait + notify, or is something else needed such as the sync keyword
			rungCapacity[which]--;
			return true;

	}
	public void releaseRung(int which) {
		sem[which].release();
		rungCapacity[which]++;
	}

	public void changeSides(int which) throws InterruptedException {
		//if direction is goingEast and timer hits zero, set goingEast to false so that its west turn
		if(direction){
			eastGoes(which);
		} else {
			westGoes(which);
		}
	}

	public void eastGoes(int which) throws InterruptedException {
		westBuffer[which].acquire();
		//add logic to make sure none of the rungs have apes on them
		while (ladderOccupied) {
			for (int i = 0; i < rungCapacity.length; i++) {
				if (sem[which].tryAcquire()) {
					ladderOccupied = false;
				} else {
					ladderOccupied = true;
				}
			}
		}
		eastBuffer[which].release();
	}




	public void westGoes(int which) throws InterruptedException {
		eastBuffer[which].acquire();
		//add logic to make sure none of the rungs have apes on them
		while (ladderOccupied) {
			for (int i = 0; i < rungCapacity.length; i++) {
				if (sem[which].tryAcquire()) {
					ladderOccupied = false;
				} else {
					ladderOccupied = true;
				}
			}
		}
		westBuffer[which].release();
	}
}
