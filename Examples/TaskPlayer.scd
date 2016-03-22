t = TaskPlayer(1);
//:
t.start;
//:
t.actions[\x] = { | dur | (degree: 10.rand, dur: dur).play;  };

//:
t.source = 0.1;
//:
t.source = [0.1, 0.2].prand;
//:
t.stop;