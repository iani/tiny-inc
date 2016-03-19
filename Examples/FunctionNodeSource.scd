f = FunctionNodeSource();
a = f.playFunc({ WhiteNoise.ar(0.1) });
//:
a.free;
//:
a = f.play; 
//:
a release: 3;

SynthDescLib.all;
SynthDescLib.all.at(\localhost)// .inspect;