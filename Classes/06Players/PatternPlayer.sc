/*  6 May 2016 21:33 */

PatternPlayer {
	var <event;
	var <player;

	*new { | event |
		^this.newCopyArgs (event).init;
	}

	init {
		player = EventPattern (event).asEventStreamPlayer;
	}

	start {
		if (player.isPlaying.not) { player.play }
	}

	stop {
		player.stop;
	}

	isPlaying { ^player.isPlaying }

	addSource { | source |
		^source.addSelfToPatternPlayer (this);
	}
	
	addFilterEvent { | inEvent, name = \player |
		event.addFilterEvent (inEvent, name);
		player.addFilterEvent (inEvent, name);
	}
	
	addFilterFunc { | function, name = \player |
		event.addFilterFunc (function, name);
		player.addFilterEvent (function, name);
	}

	removeFilter { | name = \player |
		event.removeFilter (name);
		player.event.removeFilter (name);
	}

	addKeys { | keyValuePairs |
		event.addKeys (keyValuePairs);
		player.event.addKeys (keyValuePairs);
	}

	addEvent { | inEvent |
		player addEvent: inEvent;
		inEvent keysValuesDo: { | key value |
			event [key] = value;
		}
	}
}