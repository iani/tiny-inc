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
		if (node.isNil) {
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped; }
				);
			}
		}{
			if (node.isPlaying) {
				// Actions to do if new node replaces a playing node:
				node.releaseDependants; // cancel notification of previous node
				this.prStop;            // stop previous node
				argNode addDependant: { | changer, message |
					switch (message,
						// do not notify when started
						// \n_go, { this.changed(\started) },
						\n_end, { this.stopped; }
					);
				}
			} /* {} */  // if node is waiting to start - do nothing
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
	var <source; // a source that knows how to create a node
	var <args;   // args array used for creating the node
	var <target; // the target where the node will be created
	var <action; // addAction for creating nodes
	//	var <inputs, <outputs;

	*new { | source, args, target, action = \addToHead |
		target = target.asTarget;
		^this.newCopyArgs(
			nil, source.asSource(target.server), args ? [], target, action
		);
	}

	start { // if not playing, then start with current source.
		if (this.isPlaying.not) { this.makeNode; };
	}

	makeNode {
		this addNode: source.play(args, target, action);
	}
	
	source_ { | argSource |
		source = argSource;
		this.changed(\source);
	}
}