
a = SynthPlayer({ SinOsc.ar(Rand(2400, 3100), 0, 0.1) });
//: OK. Restarts
a.start;

//: restart test
{
	a.stop;
	0.5.wait;
	20 do: {
		a.start;
		0.05.wait
	};
	a.stop;
}.fork;


//: testing change of source while plqying;
a.start;
//: Setting the source does restart:

a.source = { LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) };
//:
a.stop;

//:
a = { LFTri.ar(Rand(2400, 5100) * [1, 1.1], 0, 0.1) } +> \test;

a.start;
//:
{
	10 do: { | i |
		a = { LFTri.ar(1 + i * 100 * [1, 1.2], 0, 0.1) } +> \test;
		0.1.wait;
	}	
}.fork
//: