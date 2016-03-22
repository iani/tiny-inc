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

//:
q = TaskPlayer(0.1);
q.start;
e = EventPlayer((freq: 1000)).add(q);
//:
e.pattern = (freq: [1000, 1200]);
//:
e.pattern = (freq: [1000, 1200] + [-100, 400, 100].pbrown);
//:
e.pattern = (freq:  [100, 400, 100].pbrown * [1, 1.5, 2.1]);
//:
q.source = [0.1, 0.2, 1].prand;
//:
q.source = 0.05;
//:
q.source = 0.01;
