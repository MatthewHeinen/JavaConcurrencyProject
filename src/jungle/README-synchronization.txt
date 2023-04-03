Description of what I will do:
In order to properly synchronize the grab and release rung methods, I will use semaphores in order to accomplish this task.
I will use semaphores in a way so that each rung will have a semaphore. This will allow each rung to have an acquire and release
method for each rung. Due to this, once one of the rungs has an ape on it, it will not allow another ape to try and go onto this rung.
Since only one ape will therefore be able to be on a single rung at a time, this will synchronize these methods fulfilling what the
task is asking of us to do.


Console Output when the Apes are coming only from the East:
Ape E-1 starting to go East.
Ape E-1 wants rung 0
Ape E-1  got  rung 0
Ape E-1 wants rung 1
Ape E-1  got  1 releasing 0
Ape E-1 wants rung 2
Ape E-1  got  2 releasing 1
Ape E-1 wants rung 3
Ape E-1  got  3 releasing 2
Ape E-1 releasing 3
Ape E-1 finished going East.
Ape E-2 starting to go East.
Ape E-2 wants rung 0
Ape E-2  got  rung 0
Ape E-2 wants rung 1
Ape E-2  got  1 releasing 0
Ape E-2 wants rung 2
Ape E-2  got  2 releasing 1
Ape E-2 wants rung 3
Ape E-2  got  3 releasing 2
Ape E-2 releasing 3
Ape E-2 finished going East.
Ape E-3 starting to go East.
Ape E-3 wants rung 0
Ape E-3  got  rung 0
Ape E-3 wants rung 1
Ape E-3  got  1 releasing 0
Ape E-3 wants rung 2
Ape E-3  got  2 releasing 1
Ape E-3 wants rung 3
Ape E-3  got  3 releasing 2
Ape E-3 releasing 3
Ape E-3 finished going East.
Ape E-4 starting to go East.
Ape E-4 wants rung 0
Ape E-4  got  rung 0
Ape E-4 wants rung 1
Ape E-4  got  1 releasing 0
Ape E-4 wants rung 2
Ape E-4  got  2 releasing 1
Ape E-4 wants rung 3
Ape E-4  got  3 releasing 2
Ape E-4 releasing 3
Ape E-4 finished going East.
Ape E-5 starting to go East.
Ape E-5 wants rung 0
Ape E-5  got  rung 0
Ape E-5 wants rung 1
Ape E-5  got  1 releasing 0
Ape E-5 wants rung 2
Ape E-5  got  2 releasing 1
Ape E-5 wants rung 3
Ape E-5  got  3 releasing 2
Ape E-5 releasing 3
Ape E-5 finished going East.

Console Output when the Apes are coming from both sides (Deadlock occurs):
Ape E-1 starting to go East.
Ape E-1 wants rung 0
Ape E-1  got  rung 0
Ape E-1 wants rung 1
Ape E-1  got  1 releasing 0
Ape E-1 wants rung 2
Ape E-1  got  2 releasing 1
Ape E-2 starting to go East.
Ape E-2 wants rung 0
Ape E-2  got  rung 0
Ape E-1 wants rung 3
Ape E-1  got  3 releasing 2
Ape E-1 releasing 3
Ape E-1 finished going East.
Ape W-1 starting to go West.
Ape W-1 wants rung 3
Ape W-1  got  rung 3
Ape E-2 wants rung 1
Ape E-2  got  1 releasing 0
Ape W-1 wants rung 2
Ape W-1  got  2 releasing 3
Ape E-2 wants rung 2
Ape W-1 wants rung 1
Ape E-3 starting to go East.
Ape E-3 wants rung 0
Ape W-2 starting to go West.
Ape W-2 wants rung 3
Ape E-4 starting to go East.
Ape E-4 wants rung 0
Ape W-3 starting to go West.
Ape W-3 wants rung 3
Ape E-5 starting to go East.
Ape E-5 wants rung 0
Ape W-4 starting to go West.
Ape W-4 wants rung 3
Ape W-5 starting to go West.
Ape W-5 wants rung 3


