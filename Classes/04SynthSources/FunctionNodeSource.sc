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
	var <server;

	*new { | source, server |
		^this.newCopyArgs(source, server ?? Server.default).init;
	}

	init { this.subclassResponsibility }
}

FunctionNodeSource : NodeSource {
	var <synthDef;
	var <defName; // auto-generated

	init {
		defName = format("sdef_%", UniqueID.next);
		source !? { this.source = source };
	}

	source_ { | func, sendNow = true |
		source = func;
		synthDef = source.asSynthDef(
			fadeTime: 0.02, //: TODO: must be variable in the synthdef
			name: defName
		);
		if (sendNow) { synthDef.send(server);
			postf("sent % to server %\n", synthDef, server);
		};
	}

	playFunc { | func, args |
		var node;
		this.source_(func, false);
		node = Synth.basicNew(defName, server);
		synthDef.doSend(server, node.newMsg(*args));
		^node;
	}
	
	play { | args | ^Synth(defName, *args) }

}