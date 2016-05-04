e = EventPattern (());
//
p = e.play;
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