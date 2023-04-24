/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author davew
 * <p>
 * The Ladder class is NOT a kind of thread,
 * since it doesn't actually do anything except get used by Apes.
 * The ladder just keeps track of how many apes are on each rung (always 0 or 1).
 */

public class Ladder {
    private final int[] rungCapacity;
    private final Semaphore[] sem;
    private final Semaphore eastBuffer;
    private final Semaphore westBuffer;

    private int numThreadsEast = 0;
    private int numThreadsWest = 0;
    private int matchCountEast = 0;
    private int matchCountWest = 0;
    private boolean ladderDirectionIsEast = true;


    public Ladder(int _nRungs) {
        rungCapacity = new int[_nRungs];
        sem = new Semaphore[_nRungs];
        eastBuffer = new Semaphore(1);
        westBuffer = new Semaphore(1);
        // capacity 1 available on each rung
        for (int i = 0; i < _nRungs; i++) {
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
        if ((which == 0) && ladderDirectionIsEast) {
            synchronized (eastBuffer) {
                synchronized (this) {
                    numThreadsEast++;
                }
                eastBuffer.acquire();
            }
        }
        if ((which == rungCapacity.length - 1) && !ladderDirectionIsEast) {
            synchronized (westBuffer) {
                synchronized (this) {
                    numThreadsWest++;
                }
                westBuffer.acquire();
            }
        }
        //changeSides();
        sem[which].acquire();

        if ((which == 0 && ladderDirectionIsEast)) {
            synchronized (eastBuffer) {
                synchronized (this) {
                    matchCountEast++;
                }
            }
        } else if ((which == rungCapacity.length - 1) && !ladderDirectionIsEast) {
            synchronized (westBuffer) {
                synchronized (this) {
                    matchCountWest++;

                }
            }
        }
        rungCapacity[which]--;
        return true;

    }

    public void releaseRung(int which) {
        sem[which].release();
        rungCapacity[which]++;
    }

    private synchronized void changeSides() throws InterruptedException { // called whe synchronize on
        if (ladderDirectionIsEast) {
            for (int i = 0; i < rungCapacity.length; i++) {
                sem[i].acquire();
                System.out.println("East got here, acquired " + i);
                sem[i].release();
                System.out.println("East got here, released " + i);
                //System.out.println("*************");
            }
        } else if (!ladderDirectionIsEast) {
            for (int i = rungCapacity.length - 1; i >= 0; i--) {
                sem[i].acquire();
                System.out.println("West got here, acquired " + i);
                sem[i].release();
                System.out.println("West got here, released " + i);
            }
        }
        ladderDirectionIsEast = !ladderDirectionIsEast;

        //if direction is goingEast and timer hits zero, set goingEast to false so that its west turn
        if (ladderDirectionIsEast) {
//			if(numThreadsEast ==  matchCountEast){ //ToDo:: Lock this and use .get()
            System.out.println("Going East, numThreadsEast == " + numThreadsEast + ", matchCountEast ==" + matchCountEast);
            //westBuffer.acquire();
            //add logic to make sure none of the rungs have apes on them

            for (int i = 0; i < rungCapacity.length; i++) {
                sem[i].acquire();
                System.out.println("East got here, acquired " + i);
                sem[i].release();
                System.out.println("East got here, released " + i);
                //System.out.println("*************");
            }

            System.out.println("here in Going East, numThreadsEast == " + numThreadsEast + ", matchCountEast ==" + matchCountEast);

            eastBuffer.release(4);

            int east = numThreadsEast;
            while (east != matchCountEast) {

            }

        } else {
            System.out.println("Going West");
            for (int i = rungCapacity.length - 1; i >= 0; i--) {
                sem[i].acquire();
                System.out.println("West got here, acquired " + i);
                sem[i].release();
                System.out.println("West got here, released " + i);
            }
            System.out.println("testing");
            westBuffer.release(4);
            int west = numThreadsWest;
            while (west != matchCountWest) {

            }

        }
    }

    public void timing() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Timer is working");
                try {
                    //System.out.println("**************");
                    changeSides();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task, 30, 40);

    }

}
