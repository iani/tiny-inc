AbstractPlayer {
	var <process, <source;

	start { | args, target, action = \addToHead |
		this.makeProcess(args, target, action)
	}
	stop { if (this.isPlaying) { this.prStop } }
	isPlaying { ^process.notNil; }
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

	addNode { | argNode |
		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.

		/*  If a node is added by FunctionSynthSource again, before the 
			initial node has time to start, then ignore it.
			Therefore prevent stopping a node that has not started yet.
		*/		
		if (process.isNil) {
			argNode.onStart (this, { this.changed(\started) });
			argNode.onEnd (this, { this.changed(\stopped) });
			/*
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped; }
				);
			}
			*/
		}{
			{ process.free }.defer (0.5);
			if (process.isPlaying) {
				// Actions to do if new node replaces a playing node:
				// process.releaseDependants; // cancel notification of previous node
				this.removeNotifier (process, \n_go);
				this.removeNotifier (process, \n_end);
				// this.removeMessage (\n_end);
				this.prStop;            // stop previous node
				argNode addDependant: { | changer, message |
					switch (message,
						// do not notify when started
						// \n_go, { this.changed(\started) },
						\n_end, { this.stopped; }
					)
				}
			} { // if node is waiting to start: stop it as soon as it starts
				// to make room for other node
				process.onStart (\stop, { | n |
					postf ("% must release: %\n", n, n.notifier);
					n.notifier.onEnd (\start, { n.notifier.postln;
						"stopped".postln;
					});
					n.notifier.free;
				});
				/*
				{
					postf ("% is playing %\n", process, process.isPlaying);
					process.release
				}.defer (0.5);
				*/
			}  
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