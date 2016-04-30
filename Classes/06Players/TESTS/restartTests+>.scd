a = { LFTri.ar(Rand(2400, 5100) * [1, 1.1], 0, 0.1) } +> \test;

a;

//:
a.player.process.free;
//:
a.player.process.release;
//:
10 do:{
	a = { SinOsc.ar(Rand(500, 2100) * [1, 1.2], 0, 0.1) } +> \test;
};

//:

a.start;
//:
a.restart;

//:
{
	10 do: {
		a.restart;
		2.0.rand.wait;
	};
}.fork;


//:
{
	100 do: {
		a.restart;
		0.001.wait;
	};
}.fork;


//:
{
	10 do: { | i |
		a = { LFTri.ar(1 + i * 100 * [1, 1.2], 0, 0.1) } +> \test;
		0.1.wait;
	}	
}.fork
//: Does not release previous sound:

(degree: 0, dur: 0.1) +> \test;

(degree: [1, 3, 5].prand) +>.third \test;

(degree: 4) +>.fifth \test;

(degree: 7) +>.octave \test;
//:

(degree: [0, 1, 3, 2].pseq (inf), dur: 0.1) +>> \test;

//:
(degree: [0, 1, 3, 2].pseq (inf), dur: 0.31) +>> \test;

//:
{ WhiteNoise.ar (0.1) } +> \test;
//
(degree: [0, 1].pseq (inf), dur: 0.25, legato: 0.125) +>> \test2;
