/*
 * Created on Feb 12, 2005
 */
package src.jungle;

import java.util.concurrent.Semaphore;

/**
 * @author davew
 *
 * The Ape class is a kind of thread,
 *  since all Apes can go about their activities concurrently
 * Note that each Ape has his or her own name and direction,
 *  but in this system, many Apes will share one Ladder.
 */
public class Ape extends Thread {
	static private final boolean debug=true;  // "static" is shared by all Apes
	static private final double rungDelayMin=0.8;
	static private final double rungDelayVar=1.0;
	private final String _name;
	private final Ladder _ladderToCross;
	private final boolean _goingEast; // if false, going west

	public Ape(String name, Ladder toCross, boolean goingEast) {
		_name = name;
		_ladderToCross = toCross;
		_goingEast = goingEast;
	}
	
	public void run() {
		int startRung, move, endRung;
		System.out.println("Ape " + _name + " starting to go " + (_goingEast?"East.":"West."));
		if (_goingEast) {
			startRung = 0;
			endRung = _ladderToCross.nRungs()-1;
			move = 1;
		} else {
			startRung = _ladderToCross.nRungs()-1;
			endRung = 0;
			move = -1;
		}
		
		if (debug) {
			System.out.println("Ape " + _name + " wants rung " + startRung);
		}
		try {
			if (!_ladderToCross.grabRung(startRung, _goingEast)) {
				System.out.println("Ape " + _name + ": AAaaaaaah! " + " I can't get rung " + startRung + ", I'm falling off the ladder :-(");
				System.out.println("  Ape " + _name + " and has been eaten by the crocodiles!");
				return;  // died
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (debug) {
			System.out.println("Ape " + _name + "  got  rung " + startRung);
		}
		for (int i = startRung+move; i!=endRung+move; i+=move) {
			Jungle.tryToSleep(rungDelayMin, rungDelayVar);
			if (debug){
				System.out.println("Ape " + _name + " wants rung " + i);
			}

			try {
				if (!_ladderToCross.grabRung(i, _goingEast)) {
					System.out.println("Ape " + _name + ": AAaaaaaah! I can't get rung " + i + ", I'm falling off the ladder :-(");
					System.out.println("  Ape " + _name + " has been eaten by the crocodiles!");
					_ladderToCross.releaseRung(i-move); /// so far, we have no way to wait, so release the old lock as we die :-(
					return;  //  died
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			if (debug)
				System.out.println("Ape " + _name + "  got  " + i + " releasing " + (i-move));			
			_ladderToCross.releaseRung(i-move);
		}
		if (debug)
			System.out.println("Ape " + _name + " releasing " + endRung);			
		_ladderToCross.releaseRung(endRung);
		
		System.out.println("Ape " + _name + " finished going " + (_goingEast?"East.":"West."));
		return;  // survived!
	}
}
