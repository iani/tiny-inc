e = EventPattern (());
//
p = e.play;
//:
p.stream.event;
//:
p.addKeys ([dur: Pfunc ({ 0.01 exprand: 0.51})]);
//:
p.addEvent ((degree: [0, 7, 14].prand, legato: 0.1));
//:

p.stream.event.degree = 1;
//:
p.stream.event.dur = 0.1;
//:
p.stream.event.dur = 2.1;
//:
p.play;
//:
p.stream.event.subevents = [{ | event |
	// (degree: 20).play;
	event.degree.postln;
	().put (\degree, 10).postln.play;
}];
//:
p.inspect;
//:
p.addFilterFunc ({ | event |
	// (degree: 20).play;
	event.degree.postln;
	().put (\degree, 10).postln.play;
});
//:
p.addFilterFunc ({
	
})