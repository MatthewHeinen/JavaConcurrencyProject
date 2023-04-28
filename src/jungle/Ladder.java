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
    private final Semaphore[] sem; //semaphore that is used to lock the rungs of the ladder
    private final Object eastBuffer; //our objects that lock the ladder on each side.
    private final Object westBuffer;

    private int numThreadsEast = 0; //these are counters that are used to ensure all apes have gone on the ladder
    private int numThreadsWest = 0;
    private int matchCountEast = 0;
    private int matchCountWest = 0;
    private boolean ladderDirectionIsEast = true; //true means ladder is east, false means its west


    public Ladder(int _nRungs) {
        rungCapacity = new int[_nRungs];
        sem = new Semaphore[_nRungs];
        eastBuffer = new Semaphore(1);
        westBuffer = new Semaphore(1);
        // capacity 1 available on each rung
        for (int i = 0; i < _nRungs; i++) {
            rungCapacity[i] = 1;
            sem[i] = new Semaphore(1); //creation of the rung semaphores for part 2
        }


    }

    public int nRungs() {
        return rungCapacity.length;
    }
    // return True if you succeed in grabbing the rung


    /**
     * Allows each ape to be able to grab rungs if it is their turn to do so
     * @param which means which rung
     * @param apeGoingEast the direction of the ape. Is it an east ape or an west ape
     * @return it just returns true, but this is basically just to grab the semaphore
     * @throws InterruptedException needed for the semaphores
     */
    public boolean grabRung(int which, boolean apeGoingEast) throws InterruptedException {
//		if (rungCapacity[which] < 1) {
//			return false;
        if ((which == 0) && apeGoingEast) { //if we're on rung 0 and apes are supposed to go east
            synchronized (eastBuffer) {
                numThreadsEast++; //increment the first east counter
                eastBuffer.wait(); //to ensure we don't have any concurrency issues and only one side of the ladder will go
            }
        }
        if ((which == rungCapacity.length - 1) && !apeGoingEast) { //if we're on the last rung and the apes are supposed to go west
            synchronized (westBuffer) {
                numThreadsWest++; //increment the first west counter
                westBuffer.wait(); //to ensure we don't have any concurrency issues and only one side of the ladder will go
            }
        }
        //changeSides();
        sem[which].acquire(); //now we can acquire the ladder semaphore once we know only one side can go

        if  (which == 0 && apeGoingEast) { //if we're on rung 0 and apes are supposed to go east
            synchronized (eastBuffer) {
                matchCountEast++; //increment the second counter as the ape should be on the first rung
            }
        }
        else {
            if ((which == rungCapacity.length - 1) && !apeGoingEast) { //same logic as the previous if statement but now going west
                synchronized (westBuffer) {
                    matchCountWest++;
                }
            }
        }
        rungCapacity[which]--; //don't allow any other ape to grab that specific rung
        return true;

    }

    /**
     * The ape releases the ring is grabbed
     * @param which the specific rung on the ladder
     */
    public void releaseRung(int which) {
        sem[which].release(); //release the rung semaphore
        rungCapacity[which]++; //make it available to be grabbed
    }

    /**
     * This method has the logic to check if there are any aps on the ladder, flips the ladder direction if it is safe to do so and allow the apes on both sides to go on the ladder
     * @throws InterruptedException needed for the semaphores
     */
    private void changeSides() throws InterruptedException { // called when we want to have apes go onto the ladder
        if (ladderDirectionIsEast) { //ladder is going east
            for (int i = 0; i < rungCapacity.length; i++) {
                sem[i].acquire();
                System.out.println("East got here, acquired " + i); //acquiring and releasing 4 times to get all apes off of the ladder
                sem[i].release();
                System.out.println("East got here, released " + i);
                //System.out.println("*************");
            }
        } else {
            for (int i = rungCapacity.length - 1; i >= 0; i--) {
                sem[i].acquire();
                System.out.println("West got here, acquired " + i); //acquiring and releasing 4 times to get all apes off of the ladder
                sem[i].release();
                System.out.println("West got here, released " + i);
            }
        }
        ladderDirectionIsEast = !ladderDirectionIsEast; //after the checks, we can change ladder direction

        //if direction is goingEast and timer hits zero, set goingEast to false so that its west turn
        if (ladderDirectionIsEast) { //if we're going east
//			if(numThreadsEast ==  matchCountEast){
            System.out.println("Going East, numThreadsEast == " + numThreadsEast + ", matchCountEast ==" + matchCountEast);
            //westBuffer.acquire();
            //add logic to make sure none of the rungs have apes on them

            for (int i = 0; i < rungCapacity.length; i++) {
                sem[i].acquire();
                System.out.println("East got here, acquired " + i); //acquiring and releasing 4 times to get all apes off of the ladder
                sem[i].release();
                System.out.println("East got here, released " + i);
                //System.out.println("*************");
            }

            System.out.println("here in Going East, numThreadsEast == " + numThreadsEast + ", matchCountEast ==" + matchCountEast);

            synchronized (eastBuffer){ //allows apes on the east side to go
                eastBuffer.notifyAll();
            }

            int east = numThreadsEast;
            int counterEast = 0;
            while (east != matchCountEast) {
                System.out.println(east + "===============" + matchCountEast); //this counter ensures that no apes go onto the ladder when apes from the west are on it
            }
            System.out.println(counterEast + " counter EAST");

        } else {
            System.out.println("Going West");
            for (int i = rungCapacity.length - 1; i >= 0; i--) {
                sem[i].acquire();
                System.out.println("West got here, acquired " + i); //acquiring and releasing 4 times to get all apes off of the ladder
                sem[i].release();
                System.out.println("West got here, released " + i);
            }
            System.out.println("testing");
            synchronized (westBuffer){ //allows apes on the west side to go
                westBuffer.notifyAll();

            }
            int west = numThreadsWest;
            int counterWest = 0;
            while (west != matchCountWest) {
                System.out.println(west + "========" + matchCountWest); //this counter ensures that no apes go onto the ladder when apes from the east are on it
            }
            System.out.println(counterWest + " counterWest");
        }
    }

    /**
     * This is a timing function that uses a library to continuously have a timer that calls the changesides() function which cause the east and west apes to take turns
     */
    public void timing() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Timer is working");
                try {
                    //System.out.println("**************");
                    changeSides(); //calls our function we made above
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task, 30, 40);

    }

}
