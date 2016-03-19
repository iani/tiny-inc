w = Window().front;
w.view.layout = HLayout(
	Button().nodePlayer(NodePlayer({ WhiteNoise.ar(0.1)}))
);
w.front;


//:
a = NodePlayer({ WhiteNoise.ar(0.1)});
//:
a.inspect;
//:
a.start;