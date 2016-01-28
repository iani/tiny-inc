a = NodePlayer();

a addDependant: { | ... args | args.postln };

a.addNode({ WhiteNoise.ar(0.01) }.play);


//:
b = NodePlayer();

b.addListener(\test, { "started".postln; }, { "stopped".postln; });

b.addNode({ WhiteNoise.ar(0.01) }.play);


//:

n = NodePlayer();
w = Window().front;
w.view.layout = VLayout(
	b = Button().states_([["start"], ["stop"]])
	.nodeWatcher(n)
);

//:
b.value = 1;
//:
n addNode: { WhiteNoise.ar(0.01) }.play;

//:
Server.default.queryAllNodes;