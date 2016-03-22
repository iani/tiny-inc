TaskPlayer : AbstractPlayer {
	var <>clock, <>quant;
	var <actions;
	var <dur, <stream;

	*new { | source = 1, clock, quant |
		^this.newCopyArgs(nil, source, clock, quant).init;
	}

	init {
		actions = IdentityDictionary();
	}

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
				sourceEvent = this.getSourceEvent.put(\dur, dur);
				actions do: _.value(sourceEvent);
				dur.wait;
			}
		}).play(clock, false, quant);
		process.addDependant({ | task msg |
			if (msg === \stopped and: { task.streamHasEnded }) { process = nil }
		})
	}

	getSourceEvent { ^() }

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
		also pass an extra argument generated from a pattern.
	*/
	var <pattern, stream;

	pattern_ { | argPattern |
		pattern = argPattern;
		this.makeStream;
	}

	makeStream { stream = pattern.asStream }

	getSourceEvent { ^stream.next }

	reset {
		super.reset;
		this.makeStream;
	}
}