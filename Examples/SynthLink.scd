
a = SynthLink(\test);

//:
b = SynthPlayer({ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) });
//:
a.player = b;
//:
b.start;
//:
a.start;
//:
a.play;

{ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) } +> \test;

//:

//:
a.start;

//:
a.restart;
//:
{
	150 do: {
		{ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) } +> \test;
		0.1.wait;
	}
	
}.fork;

//:
{
	150 do: {
		\test.restart;
		0.1.wait;
	}
	
}.fork;

//:
{
	150 do: {
		\test.start;
		0.1.wait;
	}
	
}.fork;

//:
a = SynthLink();
a.isIndirectWriterOf (\x);