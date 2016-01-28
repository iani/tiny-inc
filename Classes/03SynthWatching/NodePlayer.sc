NodePlayer {
	var <node;

	addNode { | argNode |
		NodeWatcher.register(argNode);
		if (node.isNil) {
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped }
				);
			}
		}{
			node.releaseDependants;
			node.release; // node.free;
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\nodeChanged) },
					\n_end, { this.stopped }
				);
			}
		};
		node = argNode;
	}

	stopped {
		node = nil;
		this.changed(\stopped);
	}
	
	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd);
	}

	isPlaying {
		^node.notNil;
	}
}

NodeSource {
	var <source; // a source that knows how to create a node
	var <target; // the target where the node will be created
	var <args;   // args array used for creating the node
	var <player; // a node player

	*new { | source, target, args |
		^super.newCopyArgs(
			source.asNodeSource, target.asTarget, args ? [], NodePlayer()
		);
	}

	start { // if not playing, then start with current source.
		if (this.isPlaying) {} { this.source = source };
	}

	source_ { | argSource |

	}

	stop {
		
	}

	isPlaying { ^player.isPlaying }
}