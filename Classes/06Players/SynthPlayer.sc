AbstractPlayer {
	var <nodes, <source;
	//	var <process;

	start { | args, target, action = \addToHead |
		if (this.isPlaying.not) {
				this.restart(args, target, action)
		}
	}

	restart { | args, target, action = \addToHead |
		this.makeProcess(args, target, action)
	}
	stop { if (this.isPlaying) { this.prStop } }
	source_ { | argSource |
		var isPlaying;
		isPlaying = this.isPlaying;
		// [this, thisMethod.name, format("isPlaying? %\n", this.isPlaying)].postln;
		// this.setSource(argSource);
		source = argSource.asSource;
		if (isPlaying) { this.start };
		this.changed(\source);
	}

	setSource { | argSource |
		source = argSource.asSource;
	}

	prStop {
	 	nodes do: _.release;
		this.resetNodes;
		// this.postln;
		//		"on purpose".blablablabla;
		// "method prStop is not implemented".postln;
		//	process.stop
	}

	resetNodes { nodes = Set () }
	
	isPlaying {
		//	"testing method isPlaying. looking at Nodes size which is the test.".postln;
		/*
		postf ("nodes are: % nodes size is: %, result playing is: %\n",
			nodes, nodes.size, nodes.size > 0
		);
		*/
		^nodes.size > 0; 
	}
}

SimpleSynthPlayer : AbstractPlayer {
	var <processes; //  <nodes;
	addNode { | argNode |
		//		NodeWatcher.register(argNode);
		//  Release previous node if playing,
		//	but prevent that node from triggering a stopped notification when it ends.

		/*  If a node is added by FunctionSynthSource again, before the 
			initial node has time to start, then ignore it.
			Therefore prevent stopping a node that has not started yet.
		*/
		//	process.free;
		/*
		argNode.onStart (\start, { | n |
				processes.reverse [1..].postln do: { | p |
					processes remove: p;
					processes.postln;
					p.postln;
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
		*/
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
		// process = nil;
		this.changed(\stopped)
	}

	/*
	stopIfPlaying {
		if (this.isPlaying) { this.prStop}
	}
	*/
	// isPlaying { ^process.isPlaying; }

	prStop {
		//	this.postln;
		// "method prStop is not implemented!!!!!".postln;
		//`	process.release
		nodes do: _.release;
	}

	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd)
	}

	set { | param, value |
		nodes do: _.set (param, value);
		/*
		nodes do: { | node |
			[node, param, val].postln;
			node.set (param, val);
		}
		*/
	}
}


SynthPlayer : SimpleSynthPlayer {

	*new { | source |
		^this.newCopyArgs(Set (), source.asSource)
	}

	makeProcess { | args, target, action |
		this addNode: source.play(args, target, action, this)
	}

	release { | dur = 0.1 |
		//		process !? { process release: dur }
		nodes do: _.release (dur);
		this.resetNodes;
	}

	makePlayerFor { ^this }

	makeSource { | source, name = \player |
		^this.source_ (source);
	}

	addSynth { | synth |
		// from FunctionSynthSource via Synth:onStart
		if (nodes.size == 0) {
			this.changed (\started);
		};
		nodes.asArray do: { | n |
			n.release;
			nodes remove: n; // prevent re-releasing dead nodes on fast restarts
		};
		nodes add: synth;
	}

	removeSynth { | synth |
		// from FunctionSynthSource via Synth:onEnd
		nodes remove: synth;
		if (nodes.size == 0) {
			this.changed (\stopped);
		};
	}
}

