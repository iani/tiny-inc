w = Window().front;
w.view.layout = HLayout(
	Button().nodePlayer({ WhiteNoise.ar(0.1)})
);
w.front;
//:
w = Window().front;
w.view.layout = HLayout(
	{ WhiteNoise.ar(0.1)}.button
)

//:
a = NodePlayer({ WhiteNoise.ar(0.1)});
//:
a.inspect;
//:
a.start;
//:

a = { WhiteNoise.ar(0.1) }.asPlayer;

a.start;