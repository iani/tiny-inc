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
	var node, args, target, action, server;

	init { | argServer |
		server = argServer;
		defName = format("sdef_%", UniqueID.next);
		source !? { this.source_ (source, server) };
	}

	source_ { | func, server, sendNow |
		source = func;
		synthDef = source.asSynthDef(
			fadeTime: 0.02, //: TODO: must be variable in the synthdef
			name: defName
		);
		SynthDefLoader.send(synthDef, server, { this.synthDefSent });
	}

	synthDefSent {
		waitForDef = false;
		node !? {
			server.addr.sendMsg(*node.newMsg(target, args, action));
			node = nil;
		};
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

	play { | target, args, action |
		^Synth(defName, target, args, action)
	}
}