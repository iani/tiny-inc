/*
	Create synths from a Function.

	For efficieny FunctionSynthSource should not run Function:play to create a synth.
	Instead, it should add a new SynthFunc to the server when given a function, 
	and then start Synth('synthfunctname'). 

*/

// Abstract class for holding a source that can create nodes
// Sources can be of kind: Function ... (Pattern?)

SynthDefSource : AbstractSource {
	
	init { }
	
	play { | args, target, action = \addToHead |
		^Synth(source, args, target.asTarget, action);
	}
}

FunctionSynthSource : SynthDefSource {
	var <defName; // uto-generated
	var <synthDef;
	var synth;

	init {
		defName = format("sdef_%", UniqueID.next);
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
		if (synthDef.isNil) {  // Load synthdef if new
			if (synth.isNil) { // if waiting for synthdef, return previous synth
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