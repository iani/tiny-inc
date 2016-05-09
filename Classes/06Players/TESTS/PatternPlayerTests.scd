(degree: [0, 2].prand) +>.one \test;

//:
(degree: [0, 2]) +> \test;
//:
(degree: [0, 2].pwhite + [7, [1, 3]].prand, dur: 0.1) +> \test;
//:
PatternPlayer ((degree: [0, 2])).start;

\test.asSynthLink.player.player.stream;

//:

EventPattern(()).asEventStreamPlayer.inspect;


EventPattern(()).asStream;

//:
a = SynthLink ().addSource (());
//:
a.player.player addDependant: { | ... args | args.postln; };
//:
a.start;