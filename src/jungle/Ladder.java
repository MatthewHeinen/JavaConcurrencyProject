/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

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
	private boolean ladderOccupied = true;
	private boolean directionIsEast = true;
	public int count = 0;
	ArrayList<Integer> list1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1));


	public Ladder(int _nRungs) {
		rungCapacity = new int[_nRungs];
		sem = new Semaphore[_nRungs];
		eastBuffer = new Semaphore(1);
		westBuffer = new Semaphore(1);
		timing(); //is this where the timer should go?
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
			changeSides(which);
			sem[which].acquire();
			rungCapacity[which]--;
			return true;

	}
	public void releaseRung(int which) {
		sem[which].release();
		rungCapacity[which]++;
	}

	public void changeSides(int which) throws InterruptedException {
		//if direction is goingEast and timer hits zero, set goingEast to false so that its west turn
		//timing();
		if(directionIsEast){
			eastGoes(which);
		} else {
			westGoes(which);
		}
	}


	public void eastGoes(int which) throws InterruptedException {
		westBuffer.acquire();
		//add logic to make sure none of the rungs have apes on them

		for (int i = 0; i < rungCapacity.length; i++) {
			sem[which].acquire();
			sem[which].release();
		}

		eastBuffer.release();
	}


	public void westGoes(int which) throws InterruptedException {
		eastBuffer.acquire();
		//add logic to make sure none of the rungs have apes on them
		for (int i = 0; i < rungCapacity.length; i++) {
			sem[which].acquire();
			sem[which].release();
		}

		westBuffer.release();
	}


	public void timing() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("Timer is working");
				if(count % 2 == 0) {
					directionIsEast = true;
				} else {
					directionIsEast = false;
				}
				count++;
			}
		};
		timer.schedule(task, 30, 40);

	}

}
