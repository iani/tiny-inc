SimpleNodePlayer {
	var <node;

	addNode { | argNode |
		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.
		if (this.isPlaying) {
			node.releaseDependants; // do not notify when you end: next node is on the way
			this.prStop;
			argNode addDependant: { | changer, message |
				switch (message,
					// do not notify when started
					// \n_go, { this.changed(\started) },
					\n_end, {
						node = nil;
						this.changed(\stopped);					
					}
				);
			}
		}{
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, {
						node = nil;
						this.changed(\stopped);					
					}
				);
			}
		};
		node = argNode;
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