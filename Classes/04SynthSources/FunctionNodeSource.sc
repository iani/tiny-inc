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
		^this.newCopyArgs(source).init(server ?? { server.asTarget.server });
	}

	init { this.subclassResponsibility }
}

FunctionNodeSource : NodeSource {
	var <synthDef;
	var <defName; // auto-generated
	var <waitForDef = true;

	init {
		defName = format("sdef_%", UniqueID.next);
		source !? { this.source_ (source) };
	}

	source_ { | func |
		source = func;
		synthDef = nil;
	}

	play { | args, target, action = \addToHead |
		var synth, server;
		if (synthDef.isNil) {
			target = target.asTarget;
			server = target.server;
			synth = Synth.basicNew(defName, server);
			synthDef = source.asSynthDef(
				fadeTime: 0.02, //: TODO: must be variable in the synthdef
				name: defName
			);
			synthDef.doSend(server, synth.newMsg(target, args, action));
			waitForDef = true;
			synth.onStart(this, { waitForDef = false; });
			^synth;
		}{
			^Synth(defName, args, target.asTarget, action);
		}
	}
}