(degree: [0, 2].prand) +> \test;

//:
(degree: [0, 2]) +> \test;
//:
(degree: [0, 2].pwhite + [7, [1, 3]].prand, dur: 0.1) +> \test;
//:

PatternPlayer ((degree: [0, 2])).start;


\test.asSynthLink.player.player.stream;
