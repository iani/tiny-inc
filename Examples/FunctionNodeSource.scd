f = FunctionNodeSource();
a = f.playFunc({ WhiteNoise.ar(0.1) });
//:
a.free;
//:
a = f.play; 
//:
a release: 3;
//:
b = FunctionNodeSource({ SinOsc.ar(Rand(400, 100), 0, 0.1) });

//:

b.play;


//:

b.play(nil);
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