AbstractPlayer {
	var <process, <source;

	start { | args, target, action = \addToHead |
		this.makeProcess(args, target, action)
	}
	stop { if (this.isPlaying) { this.prStop } }
	source_ { | argSource |
		var isPlaying;
		isPlaying = this.isPlaying;
		// [this, thisMethod.name, format("isPlaying? %\n", this.isPlaying)].postln;
		// this.setSource(argSource);
		source = argSource.asSource;
		if (isPlaying) { this.start};
		this.changed(\source);
	}

	setSource { | argSource |
		source = argSource.asSource;
	}

	prStop { process.stop }
}

SimpleSynthPlayer : AbstractPlayer {
	var <processes, <nodes;
	addNode { | argNode |
		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.

		/*  If a node is added by FunctionSynthSource again, before the 
			initial node has time to start, then ignore it.
			Therefore prevent stopping a node that has not started yet.
		*/
		//	process.free;
	
		argNode.onStart (\start, { | n |
				processes.reverse [1..] do: { | p |
				processes remove: p;
				{ p.release }.defer (0.03);
			};
			//			processes = [n.notifier];
		});
		if (process.isNil) {
			argNode.onStart (this, { | n |
				this.nodeStarted (n.notifier);
				//this.changed(\started)
			});
			argNode.onEnd (this, { | n |
				this.nodeEnded (n.notifier);
				// this.stopped
			});
		}{
			// Actions to do if new node replaces a playing node:
			// process.releaseDependants; // cancel notification of previous node
			//	this.removeNotifier (process, \n_go);
			// this.removeNotifier (process, \n_end);
			// this.removeMessage (\n_end);
			// this.prStop;            // stop previous node
			// argNode.onEnd (this, { this.changed(\stopped) });
		};
		process = argNode;
		processes = processes add: argNode;
	}

	nodeStarted { | argNode |
		nodes ?? { nodes = Set () };
		if (nodes.size == 0) { this.changed (\started) };
		nodes add: argNode;
	}

	nodeEnded { | argNode |
		nodes remove: argNode;
		if (nodes.size == 0) { this.changed (\stopped) };
	}
	stopped {
		//	process.releaseDependants;
		process = nil;
		this.changed(\stopped)
	}

		// isPlaying { ^process.isPlaying; }
	isPlaying { ^nodes.size > 0; }

	prStop { process.release }

	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd)
	}
}


SynthPlayer : SimpleSynthPlayer {

	*new { | source |
		^this.newCopyArgs(nil, source.asSource)
	}

	makeProcess { | args, target, action |
		this addNode: source.play(args, target, action)
	}

	release { | dur = 0.1 |
		process !? { process release: dur }
	}

	makePlayerFor { ^this }

	makeSource { | source, name = \player |
		^this.source_ (source);
	}
}

