



a = EventPattern ().play;

a.addKey (\degree, Pseq ((-10..10), inf));

a.play;

a;
a.isPlaying;


a.stream.event;
a = EventPattern ().play

a.addKey (\dur, Pfunc ({ 0.01.exprand (0.2) }));
a addEvent: (degree: Pseq ([20, 21], inf), dur: 0.25, legato: 0.1);

a. stream.event.degree_ (Pseq ([20, 21], inf).asStream).dur_(1.25).legato_ (0.1);

EventPattern ().play stream.event.degree_ (Pseq ([20, 21], inf).asStream).dur_(1.25).legato_ (0.1);

//:

(degree: Pseq ([20, 21], inf), dur: 0.25, legato: 0.1) +> \e;

//:

0.1 +>.dur \e;

//:



//:
Prand ((-9..12), inf) +>.degree \e;
//:
Prand ([0.1, 0.2], inf) +>.dur \e;
//:

\e.asPatternPlayer.stream.event [\dur] = 0.1;


//:
Pseq ([0.5, 0.1], inf) +>.dur \e;

//:
Server.killAll;