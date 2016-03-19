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

	*new { | server |
		^this.newCopyArgs(server.asTarget.server).init;
	}

	init { this.subclassResponsibility }
}

FunctionNodeSource : NodeSource {
	var <synthDef;
	var <defName; // auto-generated

	init {
		defName = format("sdef_%", UniqueID.next);
	}

	playFunc { | func, args, fadeTime = 0.02 |
		var node;
		source = func;
		synthDef = source.asSynthDef(
			fadeTime: fadeTime,
			name: defName
		);
		node = Synth.basicNew(defName, server);
		synthDef.doSend(server, node.newMsg(*args));
		^node;
	}
	
	play { | args | ^Synth(defName, *args) }

}