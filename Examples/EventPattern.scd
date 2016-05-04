
EventPattern((dur: 0.1, degree: [-5, 12, 1].pbrown + [0, [-2, 3]].prand)).play;

//:
EventPattern((dur: 0.1, degree: [-5, 12, 1].pbrown + [0, [-2, 3], [1, -3, 5, -6]].prand)).play;
//:
Pseq([
	EventPattern((degree: (1..8).pseq(2), dur: 0.1)),
	EventPattern((midinote: [50, 100, 1].pbrown(500), dur: 0.01)),
	EventPattern((freq: [200, 1500, 25].pwhite * [1, 1.2, 1.5], dur: 0.15, legato: 0.15)),
	EventPattern((degree: [0, 2, 4] + [0, 5, 1].prand(3), dur: 2, legato: 0.5)),
]).play;
//:

a = EventPattern((dur: 0.1, degree: [-5, 12, 1].pbrown + [0, [-2, 3]].prand));
b = a.asStream;
b.nextN(3);

//:
m = Pseq([
	EventPattern((degree: (1..8).pseq(2), dur: 0.1)),
	EventPattern((midinote: [50, 100, 1].pbrown(500), dur: 0.01)),
	EventPattern((freq: [200, 1500, 25].pwhite * [1, 1.2, 1.5], dur: 0.15, legato: 0.15)),
	EventPattern((degree: [0, 2, 4] + [0, 5, 1].prand(3), dur: 2, legato: 0.5)),
]).asStream;
//:
m.next;
//:
a = EventPattern ((degree: Pn([-10, 1].pseries (20), inf), dur: 1)).play;
//:
a.stop;

//:
a.start;

//:
asEventStreamPlayer

