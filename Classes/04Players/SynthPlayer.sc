AbstractPlayer {
	var <process, <source;

	start { this.makeProcess }
	stop { if (this.isPlaying) { this.prStop } }
	isPlaying { ^process.notNil; }
	source_ { | argSource |
		this.setSource(argSource);
		this.changed(\source)
	}
	
	setSource { | argSource |
		source = argSource.asSource;
	}
}

SimpleSynthPlayer : AbstractPlayer {

	addNode { | argNode |
		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.

		/*  If a node is added by FunctionSynthSource again, before the 
			initial node has time to start, then ignore it.
			Therefore prevent stopping a node that has not started yet.
		*/		
		if (process.isNil) {
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped; }
				);
			}
		}{
			if (process.isPlaying) {
				// Actions to do if new node replaces a playing node:
				process.releaseDependants; // cancel notification of previous node
				this.prStop;            // stop previous node
				argNode addDependant: { | changer, message |
					switch (message,
						// do not notify when started
						// \n_go, { this.changed(\started) },
						\n_end, { this.stopped; }
					)
				}
			} /* {} */  // if node is waiting to start - do nothing
		};
		process = argNode;
	}

	stopped {
		process.releaseDependants;
		process = nil;
		this.changed(\stopped)
	}

	prStop { process.release }

	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd)
	}
}

SynthPlayer : SimpleSynthPlayer {
	var <args;   // args array used for creating the node
	var <target; // the target where the node will be created
	var <action; // addAction for creating synth
	//	var <inputs, <outputs;

	*new { | source, args, target, action = \addToHead |
		target = target.asTarget;
		^this.newCopyArgs(
			nil, source.asSource(target.server), args ? [], target, action
		)
	}

	makeProcess {
		this addNode: source.play(args, target, action)
	}

	release { | dur = 0.1 |
		process !? { process release: dur }
	}
}