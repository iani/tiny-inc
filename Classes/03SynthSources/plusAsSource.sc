+ Nil {
	asSource { | server |
		^{ WhiteNoise.ar(0.1).dup }.asSource(server);
	}
}

+ Function {
	asSource { | server |
		^FunctionSynthSource(this, server);
	}
	asPlayer { | server |
		^SynthPlayer(this, server);
	}
}

+ Pattern {
	asSource { ^this }
}