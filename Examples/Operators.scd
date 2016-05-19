


{ WhiteNoise.ar (0.1 ).dup } +> \test;

{ GrayNoise.ar (0.1 ).dup } +> \test;

{ SinOsc.ar (2400, 0, 0.1 ).dup } +> \test;


() +> \test;

(dur: 0.1) +> \test;

(degree: (0..7).pseq) +> \test;

(degree: { if (~degree %2 == 0) { ~degree + [2, 4]} { nil }}) +>.sub1 \test;

//:

(degree: 10) +> \test2;

(dur: 0.1) +> \test2;

(degree: nil) +> \test2;

