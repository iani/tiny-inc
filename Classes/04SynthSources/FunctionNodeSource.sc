/*
Create synths from a Function.

For efficieny FunctionNodeSource should not run Function:play to create a synth.
Instead, it should add a new SynthFunc to the server when given a function, 
and then start Synth('synthfunctname'). 

However, before doing this, test FunctionNodeSource with Function:play, in order
to check the rest of the functionality. 
*/

FunctionNodeSource {
	var <synthDef;
	var <name; // auto-generated
	var <playMethod = \addSynthDef; /* method to call when sent the play message.
		When the synthDef has not yet been added: \addSynthDef
		When the synthDef has been added, but not yet loaded on the server: \wait
		When the synthDef has been loaded on the server: doPlay
	*/
	var nodeCache; // stores Synth while waiting for it to actually start
	*new { | source |
		^this.newCopyArgs(source).init;
	}

	init {
		name = format("sdef_%", UniqueID.next);
		
	}

	play { | args |
		this.perform(playMethod, args);
	}

	addSynthDef { | args |
		synthDef = source.asSynthDef;
		
	}
}