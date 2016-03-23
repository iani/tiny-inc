TaskPlayer : AbstractPlayer {
	var <>clock, <>quant;
	var <players;
	var <dur, <durStream;

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
		durStream = source.asStream;
	}

	start {
		// If task has been paused or stopped before its end, 
		// then do not restart task from beginning, but resume.
		// Note: this.isPlaying = there is still a task to resume
		// process.isPlaying.not = the task has been paused
		// process.isPlaying = the task is playing, and should therefore not re-start
		if (this.isPlaying) {
			// process exists = has not ended by itself, but interrupted (paused)
			// or playing
			// Therefore, if paused, resume - do not reset
			// only resume if paused! (If playing, then do nothing.)
			if (process.isPlaying.not) { process.resume }
		}{
			// process has reached end.  Must reset durStream
			this.makeProcess; // creates new task + plays it
		}
	}

	makeProcess {
		var sourceEvent;
		// Only called when process has reached its end. Therefore: ...
		this.makeDurStream; // ... reset the durStream to the begining
		process = Task({
			while { (dur = durStream.next).notNil }
			{
				sourceEvent = this.getSourceEvent;
				players do: _.play(sourceEvent);
				dur.wait;
			}
		}).play(clock, false, quant); // THE TASK STARTS PLAYING HERE
		process.addDependant({ | task msg |
			if (msg === \stopped and: { task.streamHasEnded }) { process = nil }
		})
		^process;
	}

	// Reset the durStream to the begining
	makeDurStream { durStream = source.asStream }
	
	getSourceEvent { ^(dur: dur) }

	pause {
		process !? { process.pause }
	}

	resume {
		process !? { process.resume }
	}

	reset {
		process !? { process.reset };
		this.makeDurStream;
	}
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

	getSourceEvent { ^(stream.next ?? { () }).put (\dur, dur) }

	reset {
		super.reset;
		this.makeStream;
	}

	// Modifying the player (works also while it is playing)
	addEvent2Self { | inEvent |

	}

	addEvent2Player { | inEvent, player = \player |
		player = players[player];
		player !? { player.addEvent(inEvent) }
	}
	
}