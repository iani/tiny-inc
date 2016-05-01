TaskPlayer : AbstractPlayer {
	var <>clock, <>quant;
	var <players;
	var <dur, <durStream;
	var <process;
	
	*new { | source = 1, clock, quant |
		^this.newCopyArgs(nil, source, clock, quant).init;
	}

	init {
		players = IdentityDictionary();
	}

	addPlayer { | player |
		//		player.inspect;
		players[player.name] = player
	}
	removePlayer { | player | players[player.name] = nil }

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
			// or playing. Therefore, if paused, resume - do not reset
			// only resume if paused! (IfL playing, then do nothing.)
			if (process.isPlaying.not) { process.resume }
		}{
			// process has reached end.  Must reset durStream
			postf ("% doing % will make processs now\n", this, thisMethod.name);
			this.makeProcess; // creates new task + plays it
		}
	}

	isPlaying { ^process.isPlaying }

	stop {
		process.stop;
	}

	makeProcess {
		var sourceEvent;
		// Only called when process has reached its end. Therefore: ...
		this.makeDurStream; // ... reset the durStream to the begining
			postf ("% doing % will make processs now\n", this, thisMethod.name);
		process = Task({
			while { (dur = durStream.next).notNil }
			{
				sourceEvent = this.getSourceEvent;
				players do: _.play(sourceEvent);
				dur.wait;
			}
		}).play(clock, false, quant); // THE TASK STARTS PLAYING HERE
		this.addNotifier (process, \stopped,  { this.taskStopped });
		this.addNotifier (process, \userStopped,  { this.taskUserStopped });
		/*
		process.addDependant({ | task msg |
			if (msg === \stopped and: { task.streamHasEnded }) { process = nil }
		});
		*/
		^process;
	}

	taskStopped {
		postf ("% : %\n", this, thisMethod.name);
	}

	taskUserStopped {
		postf ("% : %\n", this, thisMethod.name);
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
		this.extractDur(argPattern);
		pattern = EventPattern(argPattern);
		this.makeStream;
	}

	extractDur { | argPattern |
		var durPattern;
		// postf ("%, argPattern %\n", thisMethod.name, argPattern).postln;
		durPattern = argPattern[\dur];
		durPattern !? {
			this.setSource(durPattern);
			argPattern[\dur] = nil;
		}
	}

	makeStream {
		postf ("% doing % pattern is: %\n", this, thisMethod.name, pattern.pattern );
		pattern.postln;
		pattern.pattern.postln;
		stream = pattern.asStream;
	}

	getSourceEvent { ^(stream.next ?? { () }).put (\dur, dur) }

	reset {
		super.reset;
		this.makeStream;
	}

	// Modifying the player (works also while it is playing)
	addEvent2Self { | inEvent |
		this.extractDur(inEvent);
		pattern.addEventContents(inEvent, stream.event)
	}

	
	addEvent2Player { | inEvent, player = \player |
		player = players[player];
		player !? {
			this.extractDur(inEvent);
			player.addEvent(inEvent);
		}
	}

	addPlayerFromEvent { | event, name = \player |
		this.extractDur(event);
		this.addPlayer (EventPlayer(name, event));
	}
}
