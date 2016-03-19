f = FunctionNodeSource();
a = f.playFunc({ WhiteNoise.ar(0.1) });
//:
a.free;
//:
a = f.play; 
//:
a release: 3;
