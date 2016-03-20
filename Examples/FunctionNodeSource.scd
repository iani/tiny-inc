f = FunctionNodeSource({ WhiteNoise.ar(0.1) });
a = f.play;

//:
a release: 3;
//:


//:
b = FunctionNodeSource({ | freq = 400 | SinOsc.ar(freq, 0, 0.1) });
{
	10 do: {
		b.play([freq: 400 rrand: 4000 ]);
		1.0.rand.wait;
	};
}.fork;
//:
b.play;
//:
c = { SinOsc.ar(Rand(400, 100), 0, 0.1) }.asSource;
//:
c.source;
//:
c.play;
//:
d = NodePlayer({ LFSaw.ar(Rand(400, 100), 0, 0.01) });
d.start;

//: doSend
d.stop;
//:
d.start;
//:
