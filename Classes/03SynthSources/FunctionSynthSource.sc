SynthDefSource {
	var <source;
	*new { | source ... args |
		^this.newCopyArgs(source).init(*args);
	}

	init { }
	
	play { | args, target, action = \addToHead |
		^Synth(source, args, target.asTarget, action);
	}
}

/*
	Create synths from a Function.

	For efficieny FunctionSynthSource should not run Function:play to create a synth.
	When a new Function is given, the first time that Function:play is called, 
	send the SynthDef created from the Function to the server, 
	and then start the synth.
	For all subsequent times, when the synthdef is already loaded, create a new Synth
	with Synth(defname ...).
*/

FunctionSynthSource : SynthDefSource {
	var <defName; // auto-generated
	var <synthDef;
	var synth;

	init {
		defName = format("sdef_%", UniqueID.next);
	}

	source_ { | func |
		source = func;
		synthDef = nil;
		// synth = nil;
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