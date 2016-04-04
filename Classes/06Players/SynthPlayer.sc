AbstractPlayer {
	var <process, <source;

	start { | args, target, action = \addToHead |
		this.makeProcess(args, target, action)
	}
	stop { if (this.isPlaying) { this.prStop } }
	isPlaying { ^process.isPlaying; }
	source_ { | argSource |
		this.setSource(argSource);
		this.changed(\source)
	}

	setSource { | argSource |
		source = argSource.asSource;
	}

	prStop { process.stop }
}

SimpleSynthPlayer : AbstractPlayer {
	var <processes;
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
			argNode.onStart (this, { this.changed(\started) });
			argNode.onEnd (this, { this.changed(\stopped) });
		}{
			// Actions to do if new node replaces a playing node:
			// process.releaseDependants; // cancel notification of previous node
			this.removeNotifier (process, \n_go);
			this.removeNotifier (process, \n_end);
			// this.removeMessage (\n_end);
			// this.prStop;            // stop previous node
			argNode.onEnd (this, { this.changed(\stopped) });
		};
		process = argNode;
		processes = processes add: argNode;
	}

	stopped {
		//	process.releaseDependants;
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

	*new { | source |
		^this.newCopyArgs(nil, source.asSource)
	}

	makeProcess { | args, target, action |
		this addNode: source.play(args, target, action)
	}

	release { | dur = 0.1 |
		process !? { process release: dur }
	}
}