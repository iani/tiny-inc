a = SimpleSynthPlayer();

a addDependant: { | ... args | args.postln };

a.addNode({ WhiteNoise.ar(0.01) }.play);
//:
a.stop;

//:
b = SynthPlayer({ SinOsc.ar(Rand(2400, 3100), 0, 0.1) });

b.addListener(\test, { "started".postln; }, { "stopped".postln; });

b.start;

//
b.source = { LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) };
//:
b.stop;

//:

c = SynthPlayer({ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) });

c.start;
c
{
	20 do: {
		c.start;
		0.1.wait;
	};
	c release: 3;
}.fork
