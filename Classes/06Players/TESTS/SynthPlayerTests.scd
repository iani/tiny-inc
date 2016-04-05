
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
b.stop;
