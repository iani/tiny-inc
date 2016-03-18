/*
	Create synths from a Function.

	For efficieny FunctionNodeSource should not run Function:play to create a synth.
	Instead, it should add a new SynthFunc to the server when given a function, 
	and then start Synth('synthfunctname'). 

	However, before doing this, test FunctionNodeSource with Function:play, in order
	to check the rest of the functionality. 
*/

// Abstract class for holding a source that can create nodes
// Sources can be of kind: Function ... (Pattern?)
NodeSource {
	var <server;
	var <source;

	*new { | server, source |
		^this.newCopyArgs(server.asTarget.server, source).init;
	}

	init { this.subclassResponsibility }
}

FunctionNodeSource : NodeSource {
	var <synthDef;
	var <defName; // auto-generated
	
	var node; // stores Synth while waiting for it to actually start
	var nodeArgs;
	var waitingForDef = false;
	
	init {
		defName = format("sdef_%", UniqueID.next);
		this.loadDef(source ?? { { Out.ar(0, WhiteNoise.ar(0.1).dup) } });
	}

	loadDef { | argDef |
		synthDef = source.asSynthDef;
		waitingForDef = true;
		SynthDefLoader.add(server, synthDef, { this.loadedSynthDef });
	}

	loadedSynthDef {
		waitingForDef = false;
		node !? {
			server.addr.sendMsg(*node.newMsg(*nodeArgs));
			node = nil;
		};
	}

	play { | args |
		if (waitingForDef) {
			nodeArgs = args;
			^node ?? {
				node = Synth.basicNew(defName, server);
			}
		}{
			^Synth(defName, *args);
		};
	}

}