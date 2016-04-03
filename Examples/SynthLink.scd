
a = SynthLink(\test);

//:
b = SynthPlayer({ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 2], 0, 0.1) });
//:
a.player = b;
//:


b.start;


//:
{ WhiteNoise.ar (0.1)}.play;
//:

{
	100 do: {
		b.start;
		0.01.wait;
	}
}.fork;

//:
a.start;
//:
a.play;

//:
b.stop;
//:

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

//: Restarts with a new Random frequency
{
	150 do: {
		\test.restart;
		0.1.wait;
	}
	
}.fork;

//: Does not restart
{
	150 do: {
		\test.start;
		0.1.wait;
	}
	
}.fork;

//:
{ | freq = 400 | SinOsc.ar (freq * [1, 5/4], 0, 0.1) } +> \argTest;

//:

\argTest.restart (\freq, 700);

//: restart with parameters
{
	var freq;
	freq = [60, 62, 64, 70, 72, 67, 40, 80, 82].midicps.prand.asStream;
	150 do: {
		\argTest.restart (\freq, freq.next);
		0.05.wait;
	}
	
}.fork;

//:
a = SynthLink();
a.isIndirectWriterOf (\x);

//:

(1..10) [..8];

[1] [0..-1]