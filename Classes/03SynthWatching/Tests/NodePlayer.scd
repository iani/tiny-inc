a = NodePlayer();

a addDependant: { | ... args | args.postln };

a.addNode({ WhiteNoise.ar(0.1) }.play);

b = { WhiteNoise.ar(0.1) }.play;
b addDependant: { "ba".postln };
NodeWatcher.register(b);

//:

n = NodePlayer();
w = Window().front;
w.view.layout = VLayout(
	Button().states_([["start"], ["stop"]])
	.nodeWatcher(n)
);

//:

n addNode: { WhiteNoise.ar(0.01) }.play