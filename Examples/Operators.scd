


{ WhiteNoise.ar (0.2 ) } +> \test;

() +> \test;

(dur: 0.1) +> \test;

(degree: (0..7).pseq) +> \test;

(degree: { if (~degree %2 == 0) { ~degree + [0, 2]} { ~degree }}) +>.sub1 \test;

