+ Nil {
	asSource { | server |
		^{ WhiteNoise.ar(0.1).dup }.asSource(server);
	}
}

/* Return a FunctionNodeSource.
For efficieny FunctionNodeSource should not run Function:play to create a synth.
Instead, it should add a new SynthFunc to the server when given a function, 
and then start Synth('synthfunctname'). 

However, before doing this, test FunctionNodeSource with Function:play, in order
to check the rest of the functionality. 
*/
+ Function {
	asSource { | server |
		^FunctionNodeSource(this, server);
	}
}