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
    private final Object eastBuffer;
    private final Object westBuffer;

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

    /**
     * I believe it is working now. What I changed:
     * made the semaphores objects so I could keep my counter logic like had it before and now use wait and notify
     * made a new variable that tells either the eastbound apes or westbound apes to go. This corresponds with a variable in the ape class. Switched from using ladder direction to  check to the ape direction
     * Added a while with a print statement that busy waits? So that we only allow apes to cross the bridge when the all the released apes at least have the firs ring
     *
     * Questions:
     * Am i printing to many print statemnts bc of the while loop
     * do these print statements affect the result?
     * @return
     */

    public int nRungs() {
        return rungCapacity.length;
    }
    // return True if you succeed in grabbing the rung


    public boolean grabRung(int which, boolean apeGoingEast) throws InterruptedException {
//		if (rungCapacity[which] < 1) {
//			return false;
        if ((which == 0) && apeGoingEast) { //
            synchronized (eastBuffer) {
                numThreadsEast++;
                eastBuffer.wait();
            }
        }
        if ((which == rungCapacity.length - 1) && !apeGoingEast) {
            synchronized (westBuffer) {
                numThreadsWest++;
                westBuffer.wait();
            }
        }
        //changeSides();
        sem[which].acquire();

        if  (which == 0 && apeGoingEast) {
            synchronized (eastBuffer) {
                matchCountEast++;
            }
        }
        else {
            if ((which == rungCapacity.length - 1) && !apeGoingEast) {
                synchronized (westBuffer) {
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

    private void changeSides() throws InterruptedException { // called whe synchronize on
        if (ladderDirectionIsEast) {
            for (int i = 0; i < rungCapacity.length; i++) {
                sem[i].acquire();
                System.out.println("East got here, acquired " + i);
                sem[i].release();
                System.out.println("East got here, released " + i);
                //System.out.println("*************");
            }
        } else {
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

            synchronized (eastBuffer){
                eastBuffer.notifyAll();
            }

            int east = numThreadsEast;
            int counterEast = 0;
            while (east != matchCountEast) {
                System.out.println(east + "===============" + matchCountEast); //is this print messing up the output?
            }
            System.out.println(counterEast + "counter EAST");

        } else {
            System.out.println("Going West");
            for (int i = rungCapacity.length - 1; i >= 0; i--) {
                sem[i].acquire();
                System.out.println("West got here, acquired " + i);
                sem[i].release();
                System.out.println("West got here, released " + i);
            }
            System.out.println("testing");
            synchronized (westBuffer){
                westBuffer.notifyAll();

            }
            int west = numThreadsWest;
            int counterWest = 0;
            while (west != matchCountWest) {
                System.out.println(west + "========" + matchCountWest);
            }
            System.out.println(counterWest + "counterWest");
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
