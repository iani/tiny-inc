TaskPlayer : AbstractPlayer {
	var <>clock, <>quant;
	var <players;
	var <dur, <stream;

	*new { | source = 1, clock, quant |
		^this.newCopyArgs(nil, source, clock, quant).init;
	}

	init {
		players = IdentityDictionary();
	}

	add { | player | players[player.name] = player }
	remove { | player | players[player.name] = nil }

	// meaningful synonym, since source is the source for dur:
	dur_ { | argDur = 1 | this.setSource(argDur) } 
	setSource { | argSource |
		source = argSource;
		stream = source.asStream;
	}

	start {
		// If task has been paused or stopped before its end, 
		// then do not restart task from beginning, but resume.
		// Note: this.isPlaying = there is still a task to resume
		// process.isPlaying.not = the task has been paused
		// process.isPlaying = the task is playing, and should therefore not re-start
		if (this.isPlaying) {
			// only resume if paused! (If playing, then do nothing.)
			if (process.isPlaying.not) { process.resume }
		}{
			this.makeProcess;
		}
	}

	makeProcess {
		var sourceEvent;
		stream = source.asStream;
		process = Task({
			while { (dur = stream.next).notNil }
			{
				sourceEvent = this.getSourceEvent;
				players do: _.play(sourceEvent);
				dur.wait;
			}
		}).play(clock, false, quant);
		process.addDependant({ | task msg |
			if (msg === \stopped and: { task.streamHasEnded }) { process = nil }
		})
	}

	getSourceEvent { ^(dur: dur) }

	pause {
		process !? { process.pause }
	}

	resume {
		process !? { process.resume }
	}

	reset { process !? { process.reset } }
}

PatternTaskPlayer : TaskPlayer {
	/* 
		Create events from a pattern, 
		and pass them to your players for playing.
	*/
	var <pattern, stream;

	pattern_ { | argPattern |
		pattern = argPattern;
		this.makeStream;
	}

	makeStream { stream = pattern.asStream }

	getSourceEvent { ^stream.next.put (\dur, dur) }

	reset {
		super.reset;
		this.makeStream;
	}
}