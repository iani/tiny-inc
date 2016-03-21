TaskPlayer : AbstractPlayer {
	var <>clock, <>quant;
	var <actions;
	var <dur;

	*new { | source, clock, quant |
		^this.newCopyArgs(nil, source, clock, quant).init;
	}

	init {
		actions = IdentityDictionary();
	}

	start {
		// If task has been paused or stopped before its end, 
		// then do not restart task from beginning, but resume.
		if (this.isPlaying) {
			// only resume if not still playing!
			if (process.isPlaying.not) { proces.resume }
		}{
			this.makeProcess;
		}
	}

	makeProcess {
		var stream;
		stream = source.asStream;
		process = Task({
			while { (dur = stream.next).notNil }
			{
				actions do: _.value(dur);
				dur.postln.wait;
			}
		}).play(clock, false, quant);
		process.addDependant({ | task msg |
			if (msg === \stopped and: { task.streamHasEnded }) { process = nil }
		})
	}

	pause {
		process !? { process.pause }
	}

	resume {
		process !? { process.resume }
	}

	reset { process !? { process.reset } }
}