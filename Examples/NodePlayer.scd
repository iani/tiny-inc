a = SimpleSynthPlayer();

a addDependant: { | ... args | args.postln };

a.addNode({ WhiteNoise.ar(0.01) }.play);

a.stop;

//:
b = SynthPlayer({ SinOsc.ar(Rand(400, 100), 0, 0.1) });

b.addListener(\test, { "started".postln; }, { "stopped".postln; });

b.start;
//:
b.stop;

//:

n = SimpleSynthPlayer();
w = Window().front;
w.view.layout = VLayout(
	Button().synthPlayer(n)
);

//:

n addNode: { WhiteNoise.ar(0.01) }.play;
//:
n.stop;
//:
Server.default.queryAllNodes;