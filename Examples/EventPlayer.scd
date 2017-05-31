
e = EventPlayer();
//:
// e.inspect;
//:
e.play((dur: 3));
//:
t = TaskPlayer();
//:
e addTo: t;
//:
t.start;
//:
t.dur = 0.1;
//:
e.pattern = (degree: Pbrown ([-5, 5, 1], inf));
//:

////////////////////////////////////////////////////////////////


e = EventPlayer();
//:
// e.inspect;
//:
e.play((dur: 3));
//:
t = PatternTaskPlayer();
//:
e addTo: t;
//:
t.start;
//:
t.dur = [0.1.pn(10), 0.05.pn(20), 0.2.pn(5), 1, 0.5].prand;
//:
e.pattern = (degree: [-15, 25, 5].pbrown + [[0, 2], [-2, 1, 3], 0.pn(3)].prand);

//:
t.addEvent2Player((legato: { 0.1 exprand: 1.5 }.pfunc, amp: [0.01, 0.2, 0.01].pbrown));
//:
t.addEvent2Player((ctranspose: Pstutter(20, [-1, 0, 2, 3, 5].prand)));
//:

////////////////////////////////////////////////////////////////
// addEvent2Self

e = EventPlayer();
t = PatternTaskPlayer().pattern = (degree: [-15, 25, 5].pbrown);
e addTo: t;
t.start;
//:
t.dur = [0.1.pn(10), 0.15.pn(4), 0.2.pn(5), 1, 0.5].prand;
//:
t.addEvent2Player((ctranspose: Pstutter(20, [-5, -2, 0, 2, 3].prand)));
//:
e.pattern = (isRest: [Pn(true, 5), Pn(false, 30)].prand);
//:
f = EventPlayer(\player1);
f addTo: t;
f.pattern = (
	degree: { ~degree * -1 + ([1, 2, 3].choose * (0..5)) },
	amp: [[0.01, 0.05, 0.005].pbrown(20), [0.01, 0.01, 0.05].pseq(5)].prand,
	type: Pwrand([\note, \rest], [0.7, 0.2], inf)
);
//:
t.addEvent2Self((legato: Pwrand([0.1, 1.1], [0.7, 0.3], inf)));
//:
(degree: [-5, 5, 1].pbrown) +> \patternTest;

a = PatternTaskPlayer ().addPlayerFromEvent ((degree: 10), \test);

a.start;

//:
a.process.isPlaying;

//:

b = SynthLink ();


//:
b.player = a;

//:

b.start;

//

b.player.start;

//:
b.isPlaying;
//:
b.player.isPlaying;

//:
a.isPlaying;
//:

//:

a = SynthLink ().extractDur ((degree: 10));

(degree: 10) +> \patternTest;


(degree: [-5, 5, 1].pbrown).asPlayer (\test);

PatternTaskPlayer ().addPlayerFromEvent ((degree: 10), \test);

(degree: [0, 10].pwhite, dur: 0.1) +> \test;

(degree: [-10, 0, 1].pbrown, dur: 0.1) +>.player2 \test;

