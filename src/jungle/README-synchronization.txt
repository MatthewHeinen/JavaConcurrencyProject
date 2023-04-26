Description of what I will do for part 2:
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

Description of what I will do for part 3:
In this part, I will use two monitors that will from a buffer from both sides of the ladder to ensure that apes from
both sides can't go onto the ladder at the same time. Basically to do this, I will create an east buffer and west buffer monitors
that when are placed in the first and last position of the ladder. Additionally, we will look at the ape direction to ensure that the correct side of apes are being acquired at
the correct time. Ones both these conditions are satisfied it means the ape wants to go on the ladder, so I will then implement a counter for each ape that does this.
There will be the same logic for each direction. After this, once the ape acquires the first rung semaphore which we made in part 2,
a second counter will be incremented to show the ape has gotten on the ladder. Later, this logic will be useful as we will need the
counters to match to allow the other side of apes to go. Then the ape can decrement the rung semaphore
The next big part of this implementation is the changeSides() function. In this function, we first acquire and release 4 times
to ensure no ape is on the ladder. After this, we can then switch the direction of the ladder to let the other side of apes go.
Then, if we are going east, we do one last check of the ladder and then allow the eastbound apes to go via a notifyall.
We then have the same exact logic but for the west apes when the ladder is going west. Finally, we then check that we don't
allow any apes from the other side to go onto the ladder until our two counters are even that we created earlier. This gives us everything to ensure
everything goes smoothly when the apes are crossing. Finally, we have a timer that keeps calling change sides after a select amount of time
which allows the east and west apes to take turns going on the ladder.

Finish the rest of the explanation. Especially why it is thread safe and other stuff in the third paragraph