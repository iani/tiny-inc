/*
	Create synths from a Function.

	For efficieny FunctionNodeSource should not run Function:play to create a synth.
	Instead, it should add a new SynthFunc to the server when given a function, 
	and then start Synth('synthfunctname'). 

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
	var synth;

	init {
		defName = format("sdef_%", UniqueID.next);
		source !? { this.source_ (source) };
	}

	source_ { | func |
		source = func;
		synthDef = nil;
		synth = nil;
	}

	fplay { | func, args, target, action = \addToHead |
		this.source = func;
		^this.play(args, target, action);
	}

	play { | args, target, action = \addToHead |
		var server;
		if (synthDef.isNil) {
			if (synth.isNil) {
				target = target.asTarget;
				server = target.server; 
				synth = Synth.basicNew(defName, server);
				synthDef = source.asSynthDef(
					fadeTime: 0.02, //: TODO: must be variable in the synthdef
					name: defName
				);
				synthDef.doSend(server, synth.newMsg(target, args, action));
				synth.onStart(this, { synth = nil; });
				^synth;
			}{ 
				^synth; // if waiting for synthdef, return previous synth
			}
		}{
			^Synth(defName, args, target.asTarget, action);
		}
	}
}