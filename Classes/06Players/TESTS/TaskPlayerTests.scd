//:
(degree: 0) +> \test;

(degree: 2) +>.third \test;

(degree: 4) +>.fifth \test;

(degree: 7) +>.octave \test;
//:

a = (degree: [0, 1, 3, 2].pseq (inf), dur: 0.1) +>> \test2;

a = (degree: [0, 1, 3, 2].pseq (inf), dur: 0.25, legato: 0.25) +>> \test2;
 
//:
(degree: { ~degree.postln + 2 } ) +>.third \test;

//:
a.start;
//
(degree: 0).asPlayer.start;
//:ψ
a.inspect;

a.player.start;