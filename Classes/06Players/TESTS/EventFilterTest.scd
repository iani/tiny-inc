a = EventPattern ((degree: [0, 10].pwhite, dur: 0.2));
p = a.play;
//:
p.isPlaying;
//:
p.stop;
//:
p.addFilterFunc ({ (legato: 0.1, degree: [10, 12]).play;});
//:
p.addFilterEvent ((legato: 0.1, degree: { ~degree + [-1, 2, 3, 4]}));
//:
p;
//:
p.inspect;
//:
p.stream.event;