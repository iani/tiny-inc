a = SimpleNodePlayer();

a addDependant: { | ... args | args.postln };

a.addNode({ WhiteNoise.ar(0.01) }.play);

a.stop;

//:
b = NodePlayer();

b.addListener(\test, { "started".postln; }, { "stopped".postln; });

b.addNode({ WhiteNoise.ar(0.01) }.play);

b.stop;

//:

n = SimpleNodePlayer();
w = Window().front;
w.view.layout = VLayout(
	Button().nodePlayer(n)
);

//:

n addNode: { WhiteNoise.ar(0.01) }.play;
//:
n.stop;
//:
Server.default.queryAllNodes;