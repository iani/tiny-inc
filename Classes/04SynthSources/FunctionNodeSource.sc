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
	var <source;

	*new { | source, server |
		^this.newCopyArgs(source).init(server);
	}

	init { this.subclassResponsibility }
}

FunctionNodeSource : NodeSource {
	var <synthDef;
	var <defName; // auto-generated

	init { | server |
		defName = format("sdef_%", UniqueID.next);
		source !? { this.source_ (source, server, false) };
	}

	source_ { | func, server, sendNow = true |
		source = func;
		synthDef = source.asSynthDef(
			fadeTime: 0.02, //: TODO: must be variable in the synthdef
			name: defName
		);
		if (sendNow) { synthDef.send(server.postln); "SENT TO SERVER".postln; }
	}

	playFunc { | func, args, target, action = \addToHead |
		var node, server;
		target = target.asTarget;
		server = target.server;
		this.source_(func, false);
		node = Synth.basicNew(defName, target);
		synthDef.doSend(server, node.newMsg(target, args, action));
		^node;
	}

	play { | target, args, action | ^Synth(defName, target, args, action) }
}