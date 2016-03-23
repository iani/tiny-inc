
e = EventPlayer();
//:
// e.inspect;
//:
e.play((dur: 3));
//:
t = TaskPlayer();
//:
e addTo: t;
//:
t.start;
//:
t.dur = 0.1;
//:
e.pattern = (degree: [-5, 5, 1].pbrown);
//:

////////////////////////////////////////////////////////////////


e = EventPlayer();
//:
// e.inspect;
//:
e.play((dur: 3));
//:
t = PatternTaskPlayer();
//:
e addTo: t;
//:
t.start;
//:
t.dur = 0.1;
//:
e.pattern = (degree: [-5, 5, 1].pbrown);
//:
