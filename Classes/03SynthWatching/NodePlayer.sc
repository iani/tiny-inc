SimpleNodePlayer {
	var <node;

	addNode { | argNode |
		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.

		/*  If a node is added by FunctionNodeSource again, before the 
			initial node has time to start, then ignore it.
			Therefore prevent stopping a node that has not started yet.
		*/			
		if (node.notNil and: { node.isPlaying.not }) { ^this }; // Do nothing if waiting for node to start

		// do not notify when you end: next node is on the way
		if (node.isNil) {
			node.releaseDependants;
			this.prStop;
			argNode addDependant: { | changer, message |
				switch (message,
					// do not notify when started
					// \n_go, { this.changed(\started) },
					\n_end, { this.stopped; }
				);
			}
		}{
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped; }
				);
			}
		};
		node = argNode;
	}

	stopped {
		node.releaseDependants;
		node = nil;
		this.changed(\stopped);
	}

	isPlaying { ^node.notNil; }

	stop { if (this.isPlaying) { this.prStop } }

	prStop { node.release }

	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd);
	}
}

NodePlayer : SimpleNodePlayer {
	classvar <all;
	var <source; // a source that knows how to create a node
	var <target; // the target where the node will be created
	var <args;   // args array used for creating the node
	var <inputs, <outputs;

	*initClass {
		all = [];
	}

	*new { | source, target, args |
		^super.newCopyArgs(
			nil, source.asSource, target.asTarget, args ? [], (), ()
		).init;
	}

	init { all = all add: this }

	start { // if not playing, then start with current source.
		if (this.isPlaying.not) { this.makeNode; };
	}

	makeNode {
		this addNode: source.play(target, args);
	}
	
	source_ { | argSource |
		source = argSource;
		this.changed(\source);
	}
}