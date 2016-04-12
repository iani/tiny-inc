a = { LFTri.ar(Rand(2400, 5100) * [1, 1.1], 0, 0.1) } +> \test;

a.start;
CS//:
{
	10 do: { | i |
		a = { LFTri.ar(1 + i * 100 * [1, 1.2], 0, 0.1) } +> \test;
		0.1.wait;
	}	
}.fork
//: Does not release previous sound:

(degree: 0) +> \test;

(degree: 2) +>.third \test;

(degree: 4) +>.fifth \test;

(degree: 7) +>.octave \test;
//:

a = (degree: [0, 1, 3, 2].pseq (inf), dur: 0.1) +>> \test;

a = (degree: [0, 1, 3, 2].pseq (inf), dur: 0.25, legato: 0.25) +>> \test2;
