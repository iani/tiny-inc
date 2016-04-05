
a = SynthLink(\test);

//:
b = SynthPlayer({ LFTri.ar( Rand(400, 2000) * [1, 1.01 + 2], 0, 0.1) });
//:
a.player = b;
//:

b.inspect;


b.start;


//:
{ WhiteNoise.ar (0.1)}.play;
//:

{
	5000 do: {
		b.start;
		0.001.wait;
	}
}.fork;

//:
a.start;
//:
a.play;

//:
b.stop;
//:
a = { LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) } +> \test;
//:
a.inspect;
//:
p = a.player;
//:
p.source = { LFTri.ar( Rand(400, 2000) * [1, 1.01 + 1.1.rand], 0, 0.1) };
//:
p.start;
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
{ | freq = 400 amp = 0.1 | SinOsc.ar (freq * [1, 5/4], 0, 0.1) * amp } +> \argTest;

//:

\argTest.restart (\freq, 700);

//: restart with parameters
{
	var freq;
	freq = [60, 62, 64, 70, 72, 67, 50, 80, 82].midicps.prand.asStream;
	 50 do: {
		// \argTest.restart (\freq, freq.next);
		[freq: freq.next, amp: 0.001.exprand (0.1)] +> \argTest;
		0.1.wait;
	}
	
}.fork;




//:
a = SynthLink();
a.isIndirectWriterOf (\x);

//:

(1..10) [..8];

[1] [0..-1]