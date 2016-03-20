+ Nil {
	asSource { | server |
		^{ WhiteNoise.ar(0.1).dup }.asSource(server);
	}
}

+ Function {
	asSource { | server |
		^FunctionNodeSource(this, server);
	}
	asPlayer { | server |
		^NodePlayer(this, server);
	}
}