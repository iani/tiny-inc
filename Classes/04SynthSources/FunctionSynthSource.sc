SynthDefSource {
	var <source;
	*new { | source |
		^this.newCopyArgs(source).init;
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
	// cached synth for rare case: Resending SynthDef before it completes loading
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

	play { | args, target, action = \addToHead, player |
		var server;
		//		[this, thisMethod.name, "the synthdef is:", synthDef].postln;
		if (synthDef.isNil) {  // Load synthdef if new
			if (synth.isNil) { // if waiting for synthdef, return previous synth
				target = target.asTarget;
				server = target.server; 
				synth = Synth.basicNew(defName, server);
				synthDef = source.asSynthDef(
					fadeTime: 0.02, //: TODO: must be variable in the synthdef
					name: defName
				);
				// [this, thisMethod.name, "sending synthdef now"].postln;
				synthDef.doSend(server, synth.newMsg(target, args, action ? \addToHead));
				this.makeSynthActions (synth, player);
				^synth;
			}{ 
				^synth; // if waiting for synthdef, return previous synth
			}
		}{
			^this.makeSynthActions (Synth(defName, args, target.asTarget, action), player);
		}
	}

	makeSynthActions { | synth, player |
		synth.onStart (player, {
				player.addSynth (synth);
			})
			.onEnd (player, {
				player.removeSynth (synth);
			});
		^synth
	}
}