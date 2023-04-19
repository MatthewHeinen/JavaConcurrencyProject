/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private final Semaphore eastBuffer;
	private final Semaphore westBuffer;
	private boolean directionIsEast = true;


	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		sem = new Semaphore[_nRungs];
		eastBuffer = new Semaphore(1);
		westBuffer = new Semaphore(1);
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


	public boolean grabRung(int which) throws InterruptedException {
//		if (rungCapacity[which] < 1) {
//			return false;
			if((which == 0) && directionIsEast){
				synchronized (westBuffer) {
					westBuffer.acquire();
				}
			}
			if((which == rungCapacity.length-1) && !directionIsEast){
				synchronized (eastBuffer){
					eastBuffer.acquire();
				}
			}
			//changeSides();
			sem[which].acquire();
			rungCapacity[which]--;
			return true;

	}
	public void releaseRung(int which) {
		sem[which].release();
		rungCapacity[which]++;
	}

	public void changeSides() throws InterruptedException {
		//if direction is goingEast and timer hits zero, set goingEast to false so that its west turn
		if(directionIsEast){
			//westBuffer.acquire();
			//add logic to make sure none of the rungs have apes on them

			for (int i = 0; i < rungCapacity.length; i++) {
				sem[i].acquire();
				System.out.println("East got here, acquired " +i);
				sem[i].release();
				System.out.println("East got here, released " +i);
				//System.out.println("*************");
			}

			System.out.println("here");
			eastBuffer.release();
		} if(!directionIsEast) {
			//eastBuffer.acquire();
			for (int i = rungCapacity.length-1; i >= 0; i--) {
				sem[i].acquire();
				System.out.println("West got here, acquired " +i);
				sem[i].release();
				System.out.println("West got here, released " +i);
			}
			System.out.println("testing");
			westBuffer.release();
		}
	}



	public void timing() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Timer is working");
				try {
					System.out.println("**************");
					changeSides();
					if(directionIsEast) {
						directionIsEast = false;
					} else {
						directionIsEast = true;
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		timer.schedule(task, 30, 40);

	}

}
